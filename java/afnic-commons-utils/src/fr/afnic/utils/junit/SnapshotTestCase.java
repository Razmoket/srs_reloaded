package fr.afnic.utils.junit;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;

import org.custommonkey.xmlunit.Diff;
import org.custommonkey.xmlunit.XMLTestCase;
import org.custommonkey.xmlunit.XMLUnit;

import fr.afnic.utils.ObjectSerializer;

/**
 * Classe designant un type de testCase base sur les snapshot. Tout objet SnapshotTestCase bénéficie d'une nouvelle methode appelle
 * assertSnapshot(String name,Snapshot snapshot). Le principe de cette methode est que lors du premier appel, on stocke les attributs de l'objet dans
 * un fichier. Lors des sessions de tests ulterieures, on comparera le contenu de l'objet avec les valeurs enregistres. Ainsi on s'assure que l'objet
 * dont on a affectue le snapshot ne bouge pas.
 * 
 * @author ginguene
 * 
 */
public class SnapshotTestCase extends XMLTestCase {

    private static File snapshotDir = new File(".");

    /**
     * * Le principe de cette methode est que lors du premier appel, on stocke les attributs de l'objet dans un fichier. Lors des sessions de tests
     * ulterieures, on comparera le contenu de l'objet avec les valeurs enregistres. Ainsi on s'assure que l'objet dont on a affectue le snapshot ne
     * bouge pas.
     * 
     * @param name
     * @param snapshot
     * @throws IOException
     */
    public void assertSnapshot(final String name, final Snapshot snapshot) throws IOException {

        XMLUnit.setIgnoreComments(true);
        XMLUnit.setIgnoreWhitespace(true);
        XMLUnit.setIgnoreAttributeOrder(true);

        File snapshotFile = null;

        snapshotFile = new File(SnapshotTestCase.snapshotDir.getCanonicalPath() + File.separator + snapshot.getName() + ".xml");

        // Verifie si un snaphot existe sur le disque
        if (snapshotFile.exists()) {
            String xmlTotest = snapshot.toXml();
            String controlXml = "";

            BufferedReader bufferedReader = null;

            try {
                bufferedReader = new BufferedReader(new FileReader(snapshotFile));

                final StringBuffer buffer = new StringBuffer();
                String line = "";
                while ((line = bufferedReader.readLine()) != null) {
                    buffer.append(line + " \n");
                }

                controlXml = buffer.toString();

            } catch (final Exception e) {
                e.printStackTrace();
            } finally {
                if (bufferedReader != null) {
                    try {
                        bufferedReader.close();
                    } catch (final IOException e1) {
                        e1.printStackTrace();
                    }
                }
            }

            Diff diff = null;
            try {
                controlXml = controlXml.replaceAll("\t", "").replaceAll("\n*", "").replace((char) 13, ' ');
                xmlTotest = xmlTotest.replaceAll("\t", "").replaceAll("\n*", "").replace((char) 13, ' ');
                diff = new Diff(controlXml, xmlTotest);
            } catch (final Exception e) {
                e.printStackTrace();
            }
            this.assertXMLIdentical(name, diff, true);

        } else {
            // Si il n'existe pas on le cree

            try {
                if (!SnapshotTestCase.snapshotDir.exists()) {
                    if (!SnapshotTestCase.snapshotDir.mkdirs()) {
                        return;
                    }
                }

                final File parentDir = snapshotFile.getParentFile();
                if (!parentDir.exists()) {
                    if (!parentDir.mkdirs()) {
                        System.out.println(parentDir.getAbsolutePath() + " created");
                    } else {
                        System.out.println("cannot create: " + parentDir.getAbsolutePath());
                    }
                } else {
                    System.out.println("path already exist: " + parentDir.getAbsolutePath());
                }

                final FileWriter fw = new FileWriter(snapshotFile);
                try {
                    fw.write(snapshot.toXml());
                } catch (final IOException e) {
                    throw e;
                } finally {
                    fw.close();
                }

            } catch (final IOException e) {
                e.printStackTrace();
            }
        }
    }

    public void assertSameObject(final String comment, final Object expected, final Object actual) throws Exception {
        final Diff diff = new Diff(ObjectSerializer.toXml(expected), ObjectSerializer.toXml(actual));
        this.assertXMLIdentical(comment + "\nExpected:\n" + ObjectSerializer.toXml(expected) + "\n But was:\n" + ObjectSerializer.toXml(actual),
                diff, true);
    }
}

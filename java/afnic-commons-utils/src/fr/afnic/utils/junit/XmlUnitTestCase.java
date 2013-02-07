package fr.afnic.utils.junit;

import java.io.IOException;

import org.custommonkey.xmlunit.Diff;
import org.custommonkey.xmlunit.XMLTestCase;
import org.custommonkey.xmlunit.XMLUnit;
import org.xml.sax.SAXException;

/**
 * Classe designant un type de testCase base sur les snapshot. Tout objet SnapshotTestCase bénéficie d'une nouvelle methode appelle
 * assertSnapshot(String name,Snapshot snapshot). Le principe de cette methode est que lors du premier appel, on stocke les attributs de l'objet dans
 * un fichier. Lors des sessions de tests ulterieures, on comparera le contenu de l'objet avec les valeurs enregistres. Ainsi on s'assure que l'objet
 * dont on a affectue le snapshot ne bouge pas.
 * 
 * @author ginguene
 * 
 */
public final class XmlUnitTestCase extends XMLTestCase {

    private static final XMLTestCase XML_TEST_CASE = new XMLTestCase() {
    };

    private XmlUnitTestCase() {

    }

    /**
     * * Le principe de cette methode est que lors du premier appel, on stocke les attributs de l'objet dans un fichier. Lors des sessions de tests
     * ulterieures, on comparera le contenu de l'objet avec les valeurs enregistres. Ainsi on s'assure que l'objet dont on a affectue le snapshot ne
     * bouge pas.
     * 
     * @param name
     * @param snapshot
     * @throws IOException
     * @throws SAXException
     */
    public static void assertXmlIdentical(final String description, final String excepted, final String actual) throws SAXException, IOException {
        XMLUnit.setIgnoreComments(true);
        XMLUnit.setIgnoreWhitespace(true);
        XMLUnit.setIgnoreAttributeOrder(true);

        final Diff diff = new Diff(excepted, actual);
        XmlUnitTestCase.XML_TEST_CASE.assertXMLIdentical(description, diff, true);
    }

}

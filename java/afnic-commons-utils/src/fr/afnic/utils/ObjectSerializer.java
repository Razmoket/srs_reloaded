/*
 * $Id: //depot/main/java/afnic-commons-utils/src/fr/afnic/utils/ObjectSerializer.java#6 $ $Revision: #6 $ $Author: barriere $
 */

package fr.afnic.utils;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStream;
import java.io.ObjectInput;
import java.io.ObjectInputStream;
import java.io.ObjectOutput;
import java.io.ObjectOutputStream;
import java.io.OutputStream;

import org.yaml.snakeyaml.Yaml;

import com.thoughtworks.xstream.XStream;
import com.thoughtworks.xstream.io.json.JsonHierarchicalStreamDriver;

/**
 * Clase utilisatier permetant de sauver des objets dans un <br/>
 * fichier xml ou de charger un objet depuis un tel fichier
 * 
 * @author ginguene
 * 
 */
public final class ObjectSerializer {

    private static final XStream XML_XSTREAM = new XStream();
    private static final XStream JSON_XSTREAM = new XStream(new JsonHierarchicalStreamDriver());
    private static final Yaml YAML = new Yaml();

    static {
        ObjectSerializer.XML_XSTREAM.setMode(XStream.NO_REFERENCES);
    }

    /**
     * Constructeur privé pour empecher l'instanciation.
     */
    private ObjectSerializer() {
    }

    /**
     * Charge un objet serialise dans un fichier XML
     * 
     * @throws FileNotFoundException
     *             si le fichier a charger n'existe pas
     * @param fileName
     *            nom du fichier xml contenant un objet serialise
     * @return l'objet deserialise
     */

    public static <F> F loadFromFile(final String fileName, final Class<F> aClass) throws FileNotFoundException, ClassCastException {

        if (fileName == null) {
            throw new IllegalArgumentException("Argument fileName should not be null");
        }

        final File file = new File(fileName);
        if (!file.exists()) {
            throw new FileNotFoundException("File " + fileName + " not found");
        }

        BufferedReader bufferedReader = null;

        try {
            final StringBuffer buffer = new StringBuffer();

            bufferedReader = new BufferedReader(new FileReader(file));
            String line = null;

            while ((line = bufferedReader.readLine()) != null) {
                buffer.append(line);
            }
            final String xml = buffer.toString();

            @SuppressWarnings("unchecked")
            final F object = (F) XML_XSTREAM.fromXML(xml);
            return object;

        } catch (final Exception e) {
            throw new ClassCastException("Cannot cast file " + fileName, e);
        } finally {
            if (bufferedReader != null) {
                try {
                    bufferedReader.close();
                } catch (final IOException e) {
                    e.printStackTrace();
                }
            }
        }
    }

    /**
     * Charge un objet serialise dans un fichier en serialisation simple
     * 
     * @throws FileNotFoundException
     *             si le fichier a charger n'existe pas
     * @param fileName
     *            nom du fichier contenant un objet serialise
     * @return l'objet deserialise
     */

    public static <F> F loadFromFileNotXML(final String fileName, final Class<F> aClass) throws FileNotFoundException, ClassCastException {

        if (fileName == null) {
            throw new IllegalArgumentException("Argument fileName should not be null");
        }

        final File file = new File(fileName);
        if (!file.exists()) {
            throw new FileNotFoundException("File " + fileName + " not found");
        }

        ObjectInput input = null;

        try {
            InputStream fileStream = new FileInputStream(file);
            InputStream buffer = new BufferedInputStream(fileStream);
            input = new ObjectInputStream(buffer);

            @SuppressWarnings("unchecked")
            F object = (F) input.readObject();
            return object;

        } catch (final Exception e) {
            throw new ClassCastException("Cannot cast file " + fileName, e);
        } finally {
            if (input != null) {
                try {
                    input.close();
                } catch (final IOException e) {
                    e.printStackTrace();
                }
            }
        }
    }

    /**
     * Charge un objet depuis une chaine XML
     * 
     * @param xml
     *            Chaine xml correspondant à un objet serializé
     * @return
     * @throws ClassCastException
     *             Si la classe de l'objet qui se trouve dans le xml ne correspond pas au paramete
     */
    public static <T> T loadFromXML(final String xml, final Class<T> aClass) throws ClassCastException {
        @SuppressWarnings("unchecked")
        final T object = (T) XML_XSTREAM.fromXML(xml);
        return object;
    }

    /**
     * Charge un objet depuis une chaine XML
     * 
     * @param sml
     * @return
     * @throws FileNotFoundException 
     * @throws ClassCastException
     */
    public static Object loadFromXML(final String xml) {
        return XML_XSTREAM.fromXML(xml);
    }

    /**
     * Serialize un Objet dans un fichier xml
     * 
     * @param object
     * @param xmlFile
     * @return true si le fichier a bien ete sauve
     */
    public static <T> boolean save(final T object, final String xmlFile) throws Exception {

        if (xmlFile == null) {
            return false;
        }

        FileWriter fileWriter = null;
        try {
            final String xml = ObjectSerializer.toXml(object);
            final File file = new File(xmlFile);

            final File parentDir = file.getParentFile();
            if (parentDir != null && !parentDir.exists()) {
                if (!parentDir.mkdirs()) {
                    throw new Exception("Failed to mkdir" + parentDir.getAbsolutePath());
                }
            }

            fileWriter = new FileWriter(file);
            fileWriter.write(xml);

            return true;
        } finally {
            if (fileWriter != null) {
                fileWriter.close();
            }

        }
    }

    /**
     * Serialize un Objet dans un fichier en serialisation simple
     * 
     * @param object
     * @param xmlFile
     * @return true si le fichier a bien ete sauve
     */
    public static <T> boolean saveNotXML(final T object, final String xmlFile) throws Exception {

        if (xmlFile == null) {
            return false;
        }

        ObjectOutput output = null;
        try {
            final File file = new File(xmlFile);

            final File parentDir = file.getParentFile();
            if (parentDir != null && !parentDir.exists()) {
                if (!parentDir.mkdirs()) {
                    throw new Exception("Failed to mkdir" + parentDir.getAbsolutePath());
                }
            }

            OutputStream fileStream = new FileOutputStream(file);
            OutputStream buffer = new BufferedOutputStream(fileStream);
            output = new ObjectOutputStream(buffer);

            output.writeObject(object);

            return true;
        } finally {
            if (output != null) {
                output.close();
            }

        }
    }

    public static void print(String message, Object object) {
        System.out.println(message + ": " + ObjectSerializer.toXml(object));
    }

    public static void print(Object object) {
        System.out.println("object: " + ObjectSerializer.toXml(object));
    }

    /**
     * Retourne le code XML correspondant a la serialization d'un objet
     * 
     * @param object
     * @return String that represents the object
     */
    public static String toXml(final Object object) {
        try {
            return XML_XSTREAM.toXML(object);
        } catch (Exception e) {
            e.printStackTrace();
            return "erreur conversion XML";
        }
    }

    public static String toYaml(final Object object) {
        return YAML.dump(object);
    }

    public static String toJson(final Object object) {
        return JSON_XSTREAM.toXML(object);
    }

}

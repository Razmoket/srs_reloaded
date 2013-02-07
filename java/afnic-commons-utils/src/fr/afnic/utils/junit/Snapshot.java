package fr.afnic.utils.junit;

import java.io.ByteArrayInputStream;
import java.io.StringWriter;
import java.util.ArrayList;
import java.util.List;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;

import org.jdom.output.Format;
import org.jdom.output.XMLOutputter;
import org.w3c.dom.Document;

import com.thoughtworks.xstream.XStream;
import com.thoughtworks.xstream.io.xml.DomDriver;

/**
 * 
 * Realise un snapshot d'un objet. un snapshot est un fichier XML dans lequel on stocke chacun des attribut de l'objet. Le stockage est recursif, si
 * l'objet contient des objets comme attributs, ces dernier sont egalement stockes dans le xml.
 * 
 * @author ginguene
 * 
 */
public class Snapshot {

    private String name;
    private Object object;
    private List<String> ignoreList;
    private SnapshotType type = SnapshotType.NORMAL;

    // Singleton
    private static XStream xstream = null;

    public Snapshot(final String name, final Object object, final SnapshotType type) {
        if (name == null) {
            throw new IllegalArgumentException("Argument name is null");
        }

        if (object == null) {
            throw new IllegalArgumentException("Argument object is null");
        }

        if (type == null) {
            throw new IllegalArgumentException("Argument type is null");
        }

        this.type = type;
        this.name = name;
        this.object = object;
    }

    /**
     * Cree un snapshot de l'objet passe en parametre. Le nom est utilise comme nom de fichier pour le xml dans lequel sera stocke le snapshot
     * 
     * @param name
     * @param object
     */
    public Snapshot(final String name, final Object object) {
        if (name == null) {
            throw new IllegalArgumentException("Argument name is null");
        }

        if (object == null) {
            throw new IllegalArgumentException("Argument object is null");
        }

        this.name = name;
        this.object = object;
    }

    public String getName() {
        return this.name;
    }

    /**
     * Initialise le nom du snapshot. Ce nom est utilise comme nom pour le fichier xml ou sera stocke le snapshot
     * 
     * @param name
     */
    public void setName(final String name) {
        this.name = name;
    }

    public Object getObject() {
        return this.object;
    }

    /**
     * initialise l'objet donc on veut effectuer un snapshot
     * 
     * @param object
     */
    public void setObject(final Object object) {
        this.object = object;
    }

    /**
     * retourne la chaine xml correspondant à l'objet
     * 
     * @return chaine xml
     */
    public String toXml() {
        String xml = Snapshot.getXstream().toXML(this.object);

        // retire les champs a ignorer du fichier xml
        if (this.ignoreList != null) {
            // enleve tous les retour a la ligne qui perturbent le replaceAll
            xml = xml.replaceAll("\\n", "");
            xml = xml.replaceAll(">\\s*<", "><");
            for (final String field : this.ignoreList) {
                xml = xml.replaceAll("(\\n)?<" + field + "( .*?)*?>.*?</" + field + ">", "");
            }
        }

        // traitement specifique en fonction du mode du snapshot
        if (this.type == SnapshotType.IGNORE_ACCENT) {
            xml = this.deleteAccent(xml);
        }

        return this.format(xml);
    }

    /**
     * Retire tous les accents d'une chaine xml, en remplacant les caractere accentue par le caractere sans accent
     * 
     * @param xml
     * @return
     */
    private String deleteAccent(final String xml) {
        String ret = xml;
        ret = ret.replaceAll("é", "e");
        ret = ret.replaceAll("ê", "e");
        ret = ret.replaceAll("è", "e");
        ret = ret.replaceAll("ë", "e");
        ret = ret.replaceAll("à", "a");
        ret = ret.replaceAll("ä", "a");
        ret = ret.replaceAll("â", "a");
        ret = ret.replaceAll("ô", "o");
        ret = ret.replaceAll("ö", "a");
        ret = ret.replaceAll("ù", "u");
        ret = ret.replaceAll("û", "u");
        ret = ret.replaceAll("ü", "u");
        ret = ret.replaceAll("ï", "i");
        ret = ret.replaceAll("î", "i");
        return ret;
    }

    /**
     * Formatte une chaine xml (indentation entete, etc) a partir d'un texte xml brut
     * 
     * @param xml
     */
    private String format(final String xml) {
        try {
            final DocumentBuilderFactory fabrique = DocumentBuilderFactory.newInstance();
            final DocumentBuilder constructeur = fabrique.newDocumentBuilder();

            final ByteArrayInputStream bai = new ByteArrayInputStream(xml.getBytes());
            final Document document = constructeur.parse(bai);

            final StringWriter sw = new StringWriter();

            final org.jdom.input.DOMBuilder builder = new org.jdom.input.DOMBuilder();
            final org.jdom.Document doc = builder.build(document);
            final XMLOutputter outputter = new XMLOutputter(Format.getPrettyFormat());
            outputter.output(doc, sw);

            return sw.toString();

        } catch (final Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    /**
     * Permet de communiquer le nom d'attributs de l'objet que l'on ne souhaite pas stocker dans le snapshot
     * 
     * @param field
     */
    public void ignoreField(final String field) {
        if (this.ignoreList == null) {
            this.ignoreList = new ArrayList<String>();
        }
        this.ignoreList.add(field);
    }

    /**
     * 
     * Retourne le Xstream a utiliser pour les serializations/deserialization d'objet dans des fichier xml
     * 
     * @return
     */
    private static XStream getXstream() {
        if (Snapshot.xstream == null) {
            Snapshot.xstream = new XStream(new DomDriver());
            Snapshot.xstream.setMode(XStream.NO_REFERENCES);

        }
        return Snapshot.xstream;
    }

}

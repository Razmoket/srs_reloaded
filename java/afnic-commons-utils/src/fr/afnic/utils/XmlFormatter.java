/*
 * $Id: $ $Revision: $ $Author: $
 */

package fr.afnic.utils;

import java.io.StringReader;
import java.io.StringWriter;

import javax.xml.transform.OutputKeys;
import javax.xml.transform.Source;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.stream.StreamResult;
import javax.xml.transform.stream.StreamSource;

/**
 * Permet de formatter une chaine de texte représentant du xml
 * 
 * @author ginguene
 * 
 */
public final class XmlFormatter {

    /**
     * Ignore les tag <!DOCTYPE ...> et les <?xml ... ?>
     * 
     * @param xmlText
     * @return
     */
    public static String formatIgnoringHeader(final String xmlText) {
        return XmlFormatter.format(xmlText.replaceAll("<\\?.*?\\?>", "").replaceAll("<!DOCTYPE .*?>", ""));
    }

    public static String format(final String xmlText) {

        try {
            final Source xmlInput = new StreamSource(new StringReader(xmlText));
            final StringWriter stringWriter = new StringWriter();
            final StreamResult xmlOutput = new StreamResult(stringWriter);
            final Transformer transformer = TransformerFactory.newInstance().newTransformer();
            transformer.setOutputProperty(OutputKeys.INDENT, "yes");
            transformer.setOutputProperty("{http://xml.apache.org/xslt}indent-amount", String.valueOf(4));
            transformer.setOutputProperty(OutputKeys.OMIT_XML_DECLARATION, "yes");
            transformer.setOutputProperty(OutputKeys.STANDALONE, "yes");
            transformer.transform(xmlInput, xmlOutput);
            return xmlOutput.getWriter().toString();

        } catch (final Exception e) {
            throw new RuntimeException(e);
        }
    }

    private XmlFormatter() {

    }

}

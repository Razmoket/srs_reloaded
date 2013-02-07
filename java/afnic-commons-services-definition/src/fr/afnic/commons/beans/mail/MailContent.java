package fr.afnic.commons.beans.mail;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * Represente le corps d'un mail.
 * 
 * @author jginguene
 * 
 */
public class MailContent implements Serializable {

    private static final long serialVersionUID = 1L;

    public List<String> getLines() {
        return lines;
    }

    private List<String> lines;

    public MailContent() {
    }

    public MailContent(List<String> lines) {
        this.setLines(lines);
    }

    public MailContent(String[] lines) {
        this.setLines(lines);
    }

    public void setLines(List<String> lines) {
        this.lines = lines;
    }

    public void setLines(String[] lines) {
        if (lines == null)
            throw new IllegalArgumentException("lines cannot be null");
        this.lines = Arrays.asList(lines);
    }

    public void addLine(String line) {
        if (this.lines == null) {
            this.lines = new ArrayList<String>();
        }
        this.lines.add(line);
    }

    public String toString(String lineSeparator) {
        if (this.lines == null) {
            return "";
        }

        StringBuffer buffer = new StringBuffer();
        for (String line : this.lines) {
            buffer.append(line);
            buffer.append(lineSeparator);
        }
        return buffer.toString();
    }

}

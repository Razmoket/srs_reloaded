package fr.afnic.commons.beans.mail;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * Represente le corps d'un mail.
 * 
 * @author ginguene
 * 
 */
public class Body {

    private List<String> lines;

    public Body() {
        if (this.lines == null) {
            this.lines = new ArrayList<String>();
        }
    }

    public Body(List<String> lines) {
        this.setLines(lines);
    }

    public Body(String[] lines) {
        this.setLines(lines);
    }

    public void setLines(List<String> lines) {
        this.lines = lines;
    }

    public void setLines(String[] lines) {
        if (lines == null) throw new IllegalArgumentException("lines cannot be null");
        this.lines = Arrays.asList(lines);
    }

    public void addLine(String line) {
        this.lines.add(line);
    }

    public String toString(String lineSeparator) {

        StringBuffer buffer = new StringBuffer();
        for (String line : this.lines) {
            buffer.append(line);
            buffer.append(lineSeparator);
        }
        return buffer.toString();
    }

    public List<String> getLines() {
        return this.lines;
    }

}

/*
 * $Id: $
 * $Revision: $
 */

package fr.afnic.commons.utils.ant;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import org.apache.tools.ant.BuildException;
import org.apache.tools.ant.Task;
import org.apache.tools.ant.types.LogLevel;

public class MergeXsdTask extends Task {

    private String dir;
    private File dirFile;

    private File xsdFile;
    private List<File> wsdlFiles;

    public void setDir(String dir) {
        this.dir = dir;
    }

    @Override
    public void execute() throws BuildException {

        try {
            this.log("Running in " + this.dir, LogLevel.INFO.getLevel());
            this.initDirFile();
            this.initXsdFileAndWsdlFiles();

            this.replaceXsdReferenceInWsdlFiles();

            this.log("xsd " + this.xsdFile.getName(), LogLevel.INFO.getLevel());
            for (File file : this.wsdlFiles) {
                this.log("wsdlFiles " + file.getName(), LogLevel.INFO.getLevel());
            }

        } catch (Exception e) {
            e.printStackTrace();
            throw new BuildException("ClasspathTask failed", e);
        }
    }

    private void initDirFile() throws BuildException {
        this.dirFile = new File(this.dir);
        if (!this.dirFile.exists()) {
            throw new BuildException("Dir " + this.dir + " not found");
        }
    }

    private void initXsdFileAndWsdlFiles() {
        this.wsdlFiles = new ArrayList<File>();

        for (File file : this.dirFile.listFiles()) {
            if (this.isXsdFile(file)) {
                this.xsdFile = file;
            }

            if (this.isWsdlFile(file)) {
                this.wsdlFiles.add(file);
            }
        }
    }

    private boolean isXsdFile(File file) {
        return file.getName().endsWith(".xsd");
    }

    private boolean isWsdlFile(File file) {
        return file.getName().endsWith(".wsdl");
    }

    private void replaceXsdReferenceInWsdlFiles() {
        for (File file : this.wsdlFiles) {
            this.replaceXsdReferenceInWsdlFile(file);
        }
    }

    private void replaceXsdReferenceInWsdlFile(File wsdlFile) {
        this.log("Replace xsd reference in " + wsdlFile.getName(), LogLevel.DEBUG.getLevel());

        List<String> lines = this.getWsdlLines(wsdlFile);
        lines = this.changeXsdReferencesInLines(lines);
        this.writeFile(wsdlFile, lines);
    }

    private List<String> changeXsdReferencesInLines(List<String> lines) {
        List<String> changedLines = new ArrayList<String>();
        for (String line : lines) {
            if (line.contains("xsd")) {
                this.log("line: " + line, LogLevel.INFO.getLevel());
            }
            String changedLine = line.replaceFirst("\"\\S*xsd\"", "\"" + this.xsdFile.getName() + "\"");
            changedLines.add(changedLine);
        }
        return changedLines;
    }

    private List<String> getWsdlLines(File file) {
        List<String> lines = new ArrayList<String>();
        BufferedReader reader = null;
        try {
            reader = new BufferedReader(new FileReader(file));
            String line = reader.readLine();
            while (line != null) {
                lines.add(line);
                line = reader.readLine();
            }
        } catch (Exception e) {
            throw new BuildException("getWsdlLines('" + file.getName() + "') failed", e);
        } finally {
            this.close(reader);
        }
        return lines;
    }

    private void writeFile(File file, List<String> lines) {
        FileWriter writer = null;
        try {
            writer = new FileWriter(file);
            for (String line : lines) {
                writer.write(line + "\n");
            }

        } catch (Exception e) {
            throw new BuildException("writeFile('" + file.getName() + "') failed", e);
        } finally {
            this.close(writer);
        }

    }

    private void close(BufferedReader reader) {
        if (reader != null) {
            try {
                reader.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    private void close(FileWriter writer) {
        if (writer != null) {
            try {
                writer.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }
}

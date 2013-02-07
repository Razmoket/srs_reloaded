package fr.afnic.commons.utils.ant;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;

import org.apache.commons.lang.StringUtils;
import org.apache.tools.ant.BuildException;
import org.apache.tools.ant.Task;

import fr.afnic.commons.beans.application.Application;
import fr.afnic.commons.beans.application.Version;
import fr.afnic.commons.beans.application.env.Environnement;
import fr.afnic.commons.services.exception.IllegalArgumentException;
import fr.afnic.commons.utils.perforce.PerforceProcessor;
import fr.afnic.utils.ToStringHelper;
import fr.afnic.utils.closer.CloseException;
import fr.afnic.utils.closer.Closer;

public class VersionTask extends Task {

    // la première étape est l'instanciation de l'objet
    private String p4Port;
    private String p4User;
    private String p4Password;
    private String p4Depot;
    private String destFile;

    // ensuite la méthode init est appelée
    @Override
    public void init() {
        // nothing to do
    }

    // puis pour chaque attribut, un setXXX est appelé (les conversions sont faites par ANT)
    // enfin au bon moment par rapport aux dépendances, execute est appelé
    @Override
    public void execute() throws BuildException {
        System.out.println(this.toString());
        Environnement env = this.getEnv();
        PerforceProcessor p4Process = new PerforceProcessor(this.p4Port, this.p4User, this.p4Password, this.p4Depot);
        Version lastVersion = p4Process.getLastVersion();
        if (lastVersion != null) {
            lastVersion.setApplication(this.getApp());
            lastVersion.setEnvironnement(env);
            System.out.println(lastVersion.getDisplayName());
            this.replaceInFile(lastVersion);
        }
    }

    @Override
    public String toString() {
        return new ToStringHelper("VersionTask").addAllObjectAttributes(this).toString();
    }

    public Application getApp() {
        String projectName = this.getProject().getProperty("project.name");
        projectName = projectName.replace("app-", "");

        projectName = StringUtils.capitalize(projectName);

        return new Application(projectName);
    }

    public Environnement getEnv() {
        try {
            return Environnement.getEnvironnement(System.getenv("AFNIC_MODE"));
        } catch (IllegalArgumentException e) {
            throw new BuildException(e);
        }
    }

    public void replaceInFile(Version version) {
        File file = new File(this.destFile);

        FileReader fileReader = null;
        FileWriter fileWriter = null;
        BufferedReader bufferedReader = null;
        BufferedWriter bufferedWriter = null;

        try {
            fileReader = new FileReader(file);
            bufferedReader = new BufferedReader(fileReader);
            String line = null;
            ArrayList<String> contentFileToWrite = new ArrayList<String>();
            while ((line = bufferedReader.readLine()) != null) {
                if (line.contains("$")) {

                    line = this.replaceValueInLine(line, "${APP_NAME}", version.getApplicationName());
                    line = this.replaceValueInLine(line, "${VERSION_MAJOR}", Integer.toString(version.getMajor()));
                    line = this.replaceValueInLine(line, "${VERSION_MINOR}", Integer.toString(version.getMinor()));
                    line = this.replaceValueInLine(line, "${VERSION_PATCH}", Integer.toString(version.getPatch()));
                    line = this.replaceValueInLine(line, "${VERSION_SUBMIT_NUMBER}", Integer.toString(version.getSubmitNumber()));
                    line = this.replaceEnvInLine(line, "${ENV}", version.getEnvironnement().getName().toUpperCase());
                    contentFileToWrite.add(line);
                } else {
                    contentFileToWrite.add(line);
                }
            }
            fileReader.close();
            fileWriter = new FileWriter(file);
            bufferedWriter = new BufferedWriter(fileWriter);
            for (String lineToWrite : contentFileToWrite) {
                bufferedWriter.write(lineToWrite + "\n");
            }
            bufferedWriter.flush();
            fileWriter.close();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            try {
                Closer.close(fileReader, bufferedReader, fileWriter, bufferedWriter);
            } catch (CloseException e) {
                throw new BuildException(e);
            }
        }

    }

    public String replaceValueInLine(String line, String varName, String value) {
        if (line.contains(varName)) {
            return line.replaceFirst("value=\".*\"", "value=\"" + value + "\"");
        }
        return line;
    }

    public String replaceEnvInLine(String line, String varName, String value) {
        if (line.contains(varName)) {
            return line.replace("DEV", value);
        }
        return line;
    }

    public void setDestFile(String destFile) {
        this.destFile = destFile;
    }

    public void setP4Port(String p4Port) {
        this.p4Port = p4Port;
    }

    public void setP4User(String p4User) {
        this.p4User = p4User;
    }

    public void setP4Password(String p4Password) {
        this.p4Password = p4Password;
    }

    public void setP4Depot(String p4Depot) {
        this.p4Depot = p4Depot;
    }

}

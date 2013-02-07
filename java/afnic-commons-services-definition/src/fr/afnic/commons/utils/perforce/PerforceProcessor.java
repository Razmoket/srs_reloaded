package fr.afnic.commons.utils.perforce;

import java.io.IOException;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.apache.tools.ant.BuildException;

import com.perforce.api.Env;
import com.perforce.api.P4Process;

import fr.afnic.commons.beans.application.Version;

public class PerforceProcessor {

    private final String p4Port;
    private final String p4User;
    private final String p4Password;
    private final String p4Depot;

    private final Env env;

    private static final String PERFORCE_EXECUTABLE = "p4";

    public PerforceProcessor(String p4Port, String p4User, String p4Password, String p4Depot) {
        this.p4Port = p4Port;
        this.p4User = p4User;
        this.p4Password = p4Password;
        this.p4Depot = p4Depot;
        this.env = this.initPerforceEnv();
    }

    private Env initPerforceEnv() {
        Env env = new Env();

        env.setPort(this.p4Port);
        env.setUser(this.p4User);
        env.setPassword(this.p4Password);

        env.setClient("");
        env.setExecutable(PerforceProcessor.PERFORCE_EXECUTABLE);
        return env;
    }

    public String getLastVersionChange() {
        return this.getTagLastChange("VE");
    }

    public String getLastChange() {
        return this.getTagLastChange("");
    }

    public String getTagLastChange(String tag) {
        String[] p4Command = { PerforceProcessor.PERFORCE_EXECUTABLE, "changes", this.p4Depot };
        P4Process p4Process = null;
        try {
            p4Process = new P4Process(this.env);
            p4Process.exec(p4Command);
            String currentLine;
            while (null != (currentLine = p4Process.readLine())) {
                if (currentLine.contains(tag)) {
                    return currentLine;
                }
            }
        } catch (Exception ex) {
            ex.printStackTrace();
        } finally {
            try {
                if (p4Process != null) {
                    p4Process.close();
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        return "";
    }

    private static final String PATTERN_VERSION_STR = ".*'VE\\s*(\\d+)\\.(\\d+)\\.(\\d+).*";
    private static final Pattern PATTERN_VERSION = Pattern.compile(PerforceProcessor.PATTERN_VERSION_STR);

    private static final String PATTERN_REVISION_STR = "Change (\\d*).*";
    private static final Pattern PATTERN_REVISION = Pattern.compile(PerforceProcessor.PATTERN_REVISION_STR);

    public Version getLastVersion() {
        String change = this.getLastVersionChange();
        Matcher matcherVersion = PerforceProcessor.PATTERN_VERSION.matcher(change);
        String major = null;
        String minor = null;
        String patch = null;
        if (matcherVersion.matches()) {
            major = matcherVersion.group(1);
            minor = matcherVersion.group(2);
            patch = matcherVersion.group(3);
        }
        String submitNumber = null;
        String revision = this.getLastChange();
        Matcher matcherRevision = PerforceProcessor.PATTERN_REVISION.matcher(revision);
        if (matcherRevision.matches()) {
            submitNumber = matcherRevision.group(1);
        }

        if (major == null || minor == null || patch == null) {
            throw new BuildException("Invalid Version: " + change);
        }
        if (submitNumber == null) {
            throw new BuildException("Invalid submit number: " + revision);
        }
        return new Version(Integer.parseInt(major), Integer.parseInt(minor), Integer.parseInt(patch), Integer.parseInt(submitNumber));

    }
}

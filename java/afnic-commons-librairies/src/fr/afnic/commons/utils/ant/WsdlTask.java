package fr.afnic.commons.utils.ant;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.PrintStream;
import java.nio.channels.FileChannel;

import org.apache.commons.lang.StringUtils;
import org.apache.tools.ant.BuildException;
import org.apache.tools.ant.Task;

public class WsdlTask extends Task {

    private String wsdlDir;
    private String src;
    private String classes;

    private String dest;
    private String host;
    private String packageName;

    @Override
    public void execute() throws BuildException {
        try {
            System.out.println("wsdlDir:" + this.wsdlDir);
            System.out.println("src:" + this.src);
            System.out.println("classes:" + this.classes);
            System.out.println("dest:" + this.dest);
            System.out.println("host:" + this.host);
            System.out.println("packageName:" + this.packageName);

            File srcDir = new File(this.wsdlDir);
            System.out.println("Host: " + this.host);

            this.classes = this.createDir(this.classes);
            this.src = this.createDir(this.src);
            this.dest = this.createDir(this.dest);

            if (srcDir.exists()) {
                for (File file : srcDir.listFiles()) {
                    System.out.println("analysing " + file.getAbsolutePath() + "...");
                    if (file.getName().endsWith(".wsdl")) {
                        this.processWsdl(file);
                    }
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
            throw new BuildException(e);
        }
    }

    private String createDir(String path) throws IOException {
        File dir = new File(path);
        dir.mkdirs();

        if (!dir.isDirectory() && dir.exists()) {
            throw new BuildException("Dir " + path + " cannot be created");
        }

        System.out.println(dir.getCanonicalPath() + " created");
        return dir.getCanonicalPath();

    }

    public String getWsdlDir() {
        return this.wsdlDir;
    }

    public void setWsdlDir(String wsdlDir) {
        this.wsdlDir = wsdlDir;
    }

    private void processWsdl(File file) throws IOException, InterruptedException {

        String destWsdl = this.dest + "/" + file.getName();
        try {
            new File(destWsdl).getParentFile().mkdirs();
            BufferedReader reader = new BufferedReader(new FileReader(file));
            FileWriter writer = new FileWriter(destWsdl);

            String serviceName = StringUtils.capitalise(file.getName().replaceAll(".wsdl", ""));

            String line = reader.readLine();
            while (line != null) {
                if (!line.startsWith("<?xml")) {
                    line = line.replaceAll("wsdlsoap:address location=\".*\"", "wsdlsoap:address location=\"" + this.host + "/" + serviceName + "\"");
                    writer.write("\n" + line);
                }
                line = reader.readLine();
            }
            reader.close();
            writer.close();
            System.out.println(destWsdl + " created");

            this.buildStubFromLocalFile(destWsdl);
        } catch (IOException e) {
            System.err.println("Failed to create " + destWsdl);
            throw e;
        }
    }

    private void buildStubFromLocalFile(String wsdlName) throws IOException, InterruptedException {

        String finalPackageName = new File(wsdlName).getName().replaceAll("\\.wsdl", "").toLowerCase();

        String localWsdl = "./build/wsdl/" + new File(wsdlName).getName();
        copy(wsdlName, localWsdl);

        System.out.println("build stub for " + finalPackageName + " from file " + localWsdl);

        String[] cmdarray = new String[] { "wsimport",
                                          "-d",
                                          this.classes,
                                          "-p",
                                          this.packageName + "." + finalPackageName,
                                          "-s",
                                          this.src,
                                          wsdlName

        };

        final Process process = Runtime.getRuntime().exec(cmdarray);
        this.show(process.getInputStream(), System.out);
        this.show(process.getErrorStream(), System.err);

        process.waitFor();
    }

    private void show(final InputStream stream, final PrintStream printStream) {
        new Thread() {
            @Override
            public void run() {
                try {
                    BufferedReader reader = new BufferedReader(new InputStreamReader(stream));
                    String line = "";
                    try {
                        while ((line = reader.readLine()) != null) {
                            printStream.println(line);
                        }
                    } finally {
                        reader.close();
                    }
                } catch (IOException ioe) {
                    ioe.printStackTrace();
                }
            }
        }.start();
    }

    public String getSrc() {
        return this.src;
    }

    public void setSrc(String src) {
        this.src = src;
    }

    public String getDest() {
        return this.dest;
    }

    public void setDest(String dest) {
        this.dest = dest;
    }

    public String getHost() {
        return this.host;
    }

    public void setHost(String host) {
        this.host = host;
    }

    public String getPackageName() {
        return this.packageName;
    }

    public void setPackageName(String packageName) {
        this.packageName = packageName;
    }

    public String getClasses() {
        return this.classes;
    }

    public void setClasses(String classes) {
        this.classes = classes;
    }

    private static boolean copy(final String srcPath, final String destPath) throws IOException {

        File src = new File(srcPath);
        File dest = new File(destPath);

        if (!src.exists()) {
            throw new FileNotFoundException("File " + src + " not found");
        }

        if (dest.exists()) {
            return false;
        }

        // on cree le repertoire contenant le fichier de destination
        final File destDir = dest.getParentFile();
        if (destDir != null && !destDir.exists()) {
            if (!destDir.mkdirs()) {
                throw new IOException("Failed to mkdirs " + destDir.getAbsolutePath());
            }
        }

        FileChannel in = null; // canal d'entrée
        FileChannel out = null; // canal de sortie
        try {
            in = new FileInputStream(src).getChannel();
            out = new FileOutputStream(dest).getChannel();

            // Copie depuis le in vers le out
            in.transferTo(0, in.size(), out);

            return true;
        } catch (final IOException e) {
            throw e;
        } finally {
            if (in != null) {
                try {
                    in.close();
                } catch (final IOException e) {
                    e.printStackTrace();
                }
            }
            if (out != null) {
                try {
                    out.close();
                } catch (final IOException e) {
                    e.printStackTrace();
                }
            }
        }

    }

}

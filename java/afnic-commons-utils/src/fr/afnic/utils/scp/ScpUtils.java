/*
 * $Id: $
 * $Revision: $
 */

package fr.afnic.utils.scp;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

import com.jcraft.jsch.ChannelExec;
import com.jcraft.jsch.JSch;
import com.jcraft.jsch.JSchException;
import com.jcraft.jsch.Session;
import com.jcraft.jsch.UserInfo;

/**
 * Classe facilitant l'envoie de fichier vers un serveur ssh
 * 
 * 
 * 
 */
public final class ScpUtils {

    private final String srcFile;
    private final String destFile;
    private final SshConf sshConf;

    private ChannelExec channel;
    private Session session;
    private OutputStream out;
    private InputStream in;

    private ScpUtils(SshConf sshConf, String srcFile, String destFile) {
        this.srcFile = srcFile;
        this.destFile = destFile;
        this.sshConf = sshConf;
    }

    public static void copyFromLocalToSsh(SshConf sshConf, String srcFile, String destFile) {
        new ScpUtils(sshConf, srcFile, destFile).copyFromLocalToSsh();
    }

    public void copyFromLocalToSsh() {

        try {

            this.createConnectedSession(this.sshConf);
            this.initChannelAndStreams();

            this.out.write(this.getC0644Command().getBytes());
            this.out.flush();
            this.checkAck();
            this.sendFileContent();

            this.out.flush();
            this.out.close();

            this.channel.disconnect();
            this.session.disconnect();
        } catch (Exception e) {
            throw new RuntimeException("copyFromLocalToSsh() from " + this.srcFile + " to " + this.destFile.toString() + " failed", e);
        }

    }

    private void createConnectedSession(SshConf sshConf) throws Exception {
        JSch jsch = new JSch();

        if (sshConf.hasKeyFile()) {
            jsch.addIdentity(sshConf.getKeyFile(), sshConf.getPassPhrase());
        }

        this.session = jsch.getSession(sshConf.getUser(), sshConf.getHost(), 22);
        this.session.setUserInfo(this.getUserInfo());
        this.session.connect();
    }

    private UserInfo getUserInfo() {
        UserInfo userInfo = null;
        if (this.sshConf.hasPassword()) {
            userInfo = new SinglePasswordUserInfo(this.sshConf.getPassword());
        } else {
            userInfo = new KeyUserInfo();
        }
        return userInfo;
    }

    private void initChannelAndStreams() throws JSchException, IOException {
        this.channel = (ChannelExec) this.session.openChannel("exec");

        this.channel.setCommand(this.getScpCommand());
        this.channel.setErrStream(System.err);
        this.channel.setExtOutputStream(System.out);
        this.channel.setOutputStream(System.out);
        this.out = this.channel.getOutputStream();
        this.in = this.channel.getInputStream();
        this.channel.connect();
        this.checkAck();

    }

    private String getScpCommand() {
        return "scp -p -t " + this.destFile;

    }

    private String getC0644Command() {
        long filesize = (new File(this.srcFile)).length();
        String command = "C0644 " + filesize + " ";
        if (this.srcFile.lastIndexOf('/') > 0) {
            command += this.srcFile.substring(this.srcFile.lastIndexOf('/') + 1);
        } else {
            command += this.srcFile;
        }
        command += "\n";
        return command;
    }

    private void sendFileContent() throws IOException {
        FileInputStream fis = new FileInputStream(this.srcFile);
        byte[] buf = new byte[1024];
        while (true) {
            int len = fis.read(buf, 0, buf.length);
            if (len <= 0) break;
            this.out.write(buf, 0, len);
            this.out.flush();

        }
        fis.close();
        fis = null;

        // send '\0'
        buf[0] = 0;
        this.out.write(buf, 0, 1);
        this.out.flush();
        this.checkAck();
    }

    private int checkAck() throws IOException {
        int b = this.in.read();
        // b may be 0 for success,
        // 1 for error,
        // 2 for fatal error,
        // -1
        if (b == 0) return b;
        if (b == -1) return b;

        if (b == 1 || b == 2) {
            StringBuffer sb = new StringBuffer();
            int c;
            do {
                c = this.in.read();
                sb.append((char) c);
            } while (c != '\n');
            if (b == 1) { // error
                throw new RuntimeException("checkAck error: " + sb.toString());
            }
            if (b == 2) { // fatal error
                throw new RuntimeException("checkAck fatal error: " + sb.toString());
            }
        }
        return b;
    }

    /**
     * Petit exemple d'utilisation
     * 
     * @param args
     */
    public static void main(String[] args) {
        SshConf sshConf = new SshConf();
        sshConf.setHost("jaspe.prive");
        sshConf.setPassword("ginguene");
        sshConf.setUser("ginguene");
        ScpUtils.copyFromLocalToSsh(sshConf, "/home/ginguene/Desktop/test-from.txt", "/home/ginguene/Desktop/test-scp.txt");
    }
}

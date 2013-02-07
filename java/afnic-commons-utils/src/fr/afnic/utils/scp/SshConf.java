/*
 * $Id: $
 * $Revision: $
 */

package fr.afnic.utils.scp;

public class SshConf {

    private static final int DEFAULT_SSH_PORT = 22;

    private int port = SshConf.DEFAULT_SSH_PORT;
    private String host;
    private String user;
    private String password;
    private String keyFile;
    private String passPhrase = "";

    public String getPassPhrase() {
        return this.passPhrase;
    }

    public void setPassPhrase(String passPhrase) {
        this.passPhrase = passPhrase;
    }

    public String getKeyFile() {
        return this.keyFile;
    }

    public void setKeyFile(String keyFile) {
        this.keyFile = keyFile;
    }

    public int getPort() {
        return this.port;
    }

    public void setPort(int port) {
        this.port = port;
    }

    public String getHost() {
        return this.host;
    }

    public void setHost(String host) {
        this.host = host;
    }

    public String getUser() {
        return this.user;
    }

    public void setUser(String user) {
        this.user = user;
    }

    public String getPassword() {
        return this.password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public boolean hasPassword() {
        return this.password != null;
    }

    public boolean hasKeyFile() {
        return this.keyFile != null;
    }

}

package fr.afnic.commons.beans;

/**
 * Parametres de connexion à un serveur mail
 * 
 * @author ginguene
 * 
 */
public class Authentification {
    private String userName;
    private String password;

    public Authentification() {

    }

    public Authentification(String userName, String password) {
        this.userName = userName;
        this.password = password;
    }

    public String getUserName() {
        return this.userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public String getPassword() {
        return this.password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

}

/*
 * $Id: $
 * $Revision: $
 */

package fr.afnic.utils.scp;

import com.jcraft.jsch.UserInfo;

/**
 * User info basé sur l'utilisation de clé publique/clé privé
 * 
 * 
 * 
 */
public class KeyUserInfo implements UserInfo {

    @Override
    public String getPassphrase() {
        return "";
    }

    @Override
    public String getPassword() {
        return null;
    }

    @Override
    public boolean promptPassphrase(String message) {
        return true;
    }

    @Override
    public boolean promptPassword(String message) {
        return true;
    }

    @Override
    public boolean promptYesNo(String message) {
        return true;
    }

    @Override
    public void showMessage(String message) {
    }

}
/*
 * $Id: $
 * $Revision: $
 */

package fr.afnic.utils.scp;

import com.jcraft.jsch.UserInfo;

public class SinglePasswordUserInfo implements UserInfo {

    private String password;

    public SinglePasswordUserInfo(String password) {
        this.password = password;
    }

    @Override
    public String getPassphrase() {
        return null;
    }

    @Override
    public String getPassword() {
        return this.password;
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
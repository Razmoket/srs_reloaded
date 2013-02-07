package fr.afnic.commons.services.mock;

import java.util.HashMap;
import java.util.List;

import fr.afnic.commons.beans.mail.SentEmail;
import fr.afnic.commons.beans.mail.reception.EmailBoxException;
import fr.afnic.commons.beans.mail.reception.IEmailBox;

public class MockEmailBox implements IEmailBox {

    private final HashMap<String, SentEmail> map;

    MockEmailBox(HashMap<String, SentEmail> map) {
        this.map = map;
    }

    @Override
    public int getUnreadEmailCount() throws EmailBoxException {
        return 0;
    }

    @Override
    public List<SentEmail> getUnreadEmails() throws EmailBoxException {
        return null;
    }

    @Override
    public int getEmailCount() throws EmailBoxException {
        return this.map.size();
    }

    @Override
    public List<SentEmail> getEmails() throws EmailBoxException {
        return null;
    }

    @Override
    public void deleteEmail(SentEmail mail) throws EmailBoxException {
    }

    @Override
    public void deleteEmail(String mailId) throws EmailBoxException {
    }

    @Override
    public void setAsReadMail(SentEmail mail) throws EmailBoxException {

    }

    @Override
    public void setAsReadMail(String mailId) throws EmailBoxException {

    }

}

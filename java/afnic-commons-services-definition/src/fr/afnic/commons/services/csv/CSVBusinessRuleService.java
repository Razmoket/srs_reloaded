package fr.afnic.commons.services.csv;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.core.io.ClassPathResource;

import fr.afnic.commons.beans.profiling.users.UserId;
import fr.afnic.commons.services.IBusinessRuleService;
import fr.afnic.commons.services.exception.NotFoundException;
import fr.afnic.commons.services.exception.ServiceException;
import fr.afnic.commons.services.tld.TldServiceFacade;

public class CSVBusinessRuleService implements IBusinessRuleService {

    // associe à une extension les données suivantes : l'extension, le BE destination de suppression controlées
    // et 3 contacts : holder, contact tech, contact admin
    private Map<String, String[]> mapUserForControlledDelete;

    private static final String CONFIG_FILE = "controlledDeleteConfiguration.csv";

    private List<String> listHolder;

    public CSVBusinessRuleService() throws ServiceException {
        this.mapUserForControlledDelete = null;
    }

    private void importAndValidateControlledDelete() throws ServiceException {
        this.mapUserForControlledDelete = new HashMap<String, String[]>();
        this.listHolder = new ArrayList<String>();

        try {
            this.importCSVFile(CONFIG_FILE);
        } catch (IOException e) {
            throw new ServiceException("error loading the file", e);
        }
        this.validateControlledDeleteConfiguration();
    }

    private void importCSVFile(String path) throws IOException, ServiceException {
        BufferedReader br = null;
        ClassPathResource classPathRessource = null;
        try {
            classPathRessource = new ClassPathResource(path);

            br = new BufferedReader(new FileReader(classPathRessource.getFile().getAbsolutePath()));
        } catch (IOException e) {
            throw new ServiceException("importCSVFile : cannot read " + classPathRessource.getFile().getAbsolutePath(), e);
        }

        String line = br.readLine();

        while ((line = br.readLine()) != null) {
            String[] items = line.split(";");
            String first = items[0];
            this.mapUserForControlledDelete.put(first, items);
            this.listHolder.add(items[2]);
        }
        br.close();
    }

    // premier test, on vérifie que chaque BE a 3 contacts associés
    // second test, on vérifie que les contacts sont bien rattachés aux BE
    private void validateControlledDeleteConfiguration() throws ServiceException {
        for (String extension : this.mapUserForControlledDelete.keySet()) {
            String[] arrayClients = this.mapUserForControlledDelete.get(extension);
            String customer = null;
            if (arrayClients.length != 5) {
                throw new IllegalArgumentException("CSV file is malformatted.");
            }
            for (String client : arrayClients) {
                if ((client == null) || ("".equals(client))) {
                    throw new IllegalArgumentException("CSV file is malformatted.");
                }
                if (customer == null) {
                    customer = client;
                }
                if (!(customer.equals(client))) {
                    this.validateClient(customer, client);
                }
            }
        }
    }

    private void validateClient(String customer, String nicHandle) throws ServiceException {
        /*WhoisContact retour = ServiceFacade.getContactService().getContactWithHandle(nicHandle);
        if (retour == null) {
            throw new IllegalArgumentException("Invalid Nichandle " + nicHandle + ".");
        }
        if ((retour.getRegistrarCode() == null) || ("".equals(retour.getRegistrarCode()))) {
            throw new IllegalArgumentException("No customer for the Nichandle " + nicHandle + ".");
        }
        if (retour.getRegistrarCode().equals(customer)) {
            throw new IllegalArgumentException("Nichandle " + nicHandle + "not linked with customer " + customer + ".");
        }*/
    }

    @Override
    public String getCustomerForExtension(String extension, UserId userId, TldServiceFacade tld) throws ServiceException {
        if (this.mapUserForControlledDelete == null) {
            this.importAndValidateControlledDelete();
        }
        String[] tmp = this.mapUserForControlledDelete.get(extension);
        if (tmp == null)
            throw new NotFoundException("Cette extension n'est pas configurée pour la suppression contrôlée : " + extension);
        return tmp[1];
    }

    @Override
    public String getHolderForExtension(String extension, UserId userId, TldServiceFacade tld) throws ServiceException {
        if (this.mapUserForControlledDelete == null) {
            this.importAndValidateControlledDelete();
        }
        String[] tmp = this.mapUserForControlledDelete.get(extension);
        if (tmp == null)
            throw new NotFoundException("Cette extension n'est pas configurée pour la suppression contrôlée : " + extension);
        return tmp[2];
    }

    @Override
    public String getTechCForExtension(String extension, UserId userId, TldServiceFacade tld) throws ServiceException {
        if (this.mapUserForControlledDelete == null) {
            this.importAndValidateControlledDelete();
        }
        String[] tmp = this.mapUserForControlledDelete.get(extension);
        if (tmp == null)
            throw new NotFoundException("Cette extension n'est pas configurée pour la suppression contrôlée : " + extension);
        return tmp[3];
    }

    @Override
    public String getAdminCForExtension(String extension, UserId userId, TldServiceFacade tld) throws ServiceException {
        if (this.mapUserForControlledDelete == null) {
            this.importAndValidateControlledDelete();
        }
        String[] tmp = this.mapUserForControlledDelete.get(extension);
        if (tmp == null)
            throw new NotFoundException("Cette extension n'est pas configurée pour la suppression contrôlée : " + extension);
        return tmp[4];
    }

    @Override
    public List<String> getListHolder(UserId userId, TldServiceFacade tld) throws ServiceException {
        if (this.listHolder == null) {
            this.importAndValidateControlledDelete();
        }
        return this.listHolder;
    }

}

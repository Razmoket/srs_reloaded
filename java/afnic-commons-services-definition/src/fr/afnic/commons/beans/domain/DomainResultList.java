package fr.afnic.commons.beans.domain;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import fr.afnic.commons.beans.customer.Customer;
import fr.afnic.commons.beans.list.Column;
import fr.afnic.commons.beans.list.Line;
import fr.afnic.commons.beans.list.SimpleResultList;
import fr.afnic.commons.beans.logs.ILogger;
import fr.afnic.commons.beans.profiling.users.UserId;
import fr.afnic.commons.services.exception.ServiceException;
import fr.afnic.commons.services.facade.AppServiceFacade;
import fr.afnic.commons.services.tld.TldServiceFacade;
import fr.afnic.utils.StringUtils;

public class DomainResultList extends SimpleResultList {

    private static final ILogger LOGGER = AppServiceFacade.getLoggerService().getLogger(DomainResultList.class);

    private final static Column NAME = new Column("Nom de domaine", "domain_name", true);
    private final static Column UTF8 = new Column("Utf8", "utf8");
    private final static Column REGISTRAR_NAME = new Column("Bureau d'enregistrement", "registrar_name");
    private final static Column ACCOUNT_MANAGER = new Column("chargé de clientèle", "account_manager");
    private final static Column TITULAIRE_NAME = new Column("Titulaire", "holder_name");
    private final static Column ANNIVERSARY_DATE = new Column("Date anniversaire", "anniversary_date");
    private final static Column STATUS = new Column("Statut", "domain_status");

    private List<Domain> domains;

    public DomainResultList(List<Domain> domains, UserId userId, TldServiceFacade tld) {
        super(userId, tld);
        try {

            Customer customer = null;
            String domainName = null;
            this.domains = domains;

            Map<String, String> mapManager = new HashMap<String, String>();

            this.addColumn(NAME);
            this.addColumn(UTF8);
            this.addColumn(REGISTRAR_NAME);
            this.addColumn(ACCOUNT_MANAGER);
            this.addColumn(TITULAIRE_NAME);
            this.addColumn(ANNIVERSARY_DATE);
            this.addColumn(STATUS);

            if (domains != null) {
                for (int i = 0; i < domains.size(); i++) {
                    Domain domain = domains.get(i);
                    domainName = domain.getName();

                    Line line = new Line();
                    if (domain.getNameDetail() != null) {
                        line.addValue(NAME, StringUtils.splitString(domain.getNameDetail().getLdh(), 35));
                        line.addValue(UTF8, StringUtils.splitString(domain.getNameDetail().getUtf8(), 35));
                    } else {
                        line.addValue(NAME, domain.getName());
                        line.addValue(UTF8, "");
                    }

                    if (domain.getRegistrarCode() != null) {
                        line.addValue(REGISTRAR_NAME, domain.getRegistrarName());
                    } else {
                        line.addValue(REGISTRAR_NAME, "[Aucun]");
                    }
                    if (!domain.getStatus().equals(DomainStatus.Free)) {
                        String tmp = mapManager.get(domain.getRegistrarCode());
                        if (tmp == null) {
                            customer = domain.getCustomer();
                            if (customer != null && customer.hasAccountManager()) {
                                mapManager.put(domain.getRegistrarCode(), customer.getAccountManagerLogin());
                                tmp = customer.getAccountManagerLogin();
                            } else {
                                mapManager.put(domain.getRegistrarCode(), "[Aucun]");
                                tmp = "[Aucun]";
                            }
                        }
                        line.addValue(ACCOUNT_MANAGER, tmp);
                    } else {
                        line.addValue(ACCOUNT_MANAGER, "[Aucun]");
                    }
                    if (domain.hasHolder()) {
                        line.addValue(TITULAIRE_NAME, domain.getHolderHandle());
                    } else {
                        line.addValue(TITULAIRE_NAME, "[Aucun]");
                    }
                    line.addValue(ANNIVERSARY_DATE, domain.getAnniversaryDateStr());
                    line.addValue(STATUS, domain.getStatusAsString());
                    this.addLine(line);
                }
            }

        } catch (Exception e) {
            throw new RuntimeException("UserResultList() failed", e);
        }
    }

    public static void addCsvBody(StringBuffer buffer, List<Domain> listDomain) {
        Map<String, String> mapManager = new HashMap<String, String>();
        Customer customer = null;
        String accountManager = null;

        try {
            for (Domain domain : listDomain) {
                if (!domain.getStatus().equals(DomainStatus.Free)) {
                    accountManager = mapManager.get(domain.getRegistrarCode());
                    if (accountManager != null) {
                    } else {
                        customer = domain.getCustomer();
                        if (customer != null && customer.hasAccountManager()) {
                            mapManager.put(domain.getRegistrarCode(), customer.getAccountManagerLogin());
                            accountManager = customer.getAccountManagerLogin();
                        } else {
                            mapManager.put(domain.getRegistrarCode(), "[Aucun]");
                            accountManager = "[Aucun]";
                        }
                    }
                    addCsvLine(buffer, domain, accountManager);
                } else {
                    addCsvLine(buffer, domain, "[Aucun]");
                }
            }
        } catch (ServiceException e) {
            LOGGER.error("DomainSearchResultPanel : addCsvBody failed.", e);
            throw new RuntimeException("convert() failed", e);
        }
    }

    public static void addCsvLine(StringBuffer buffer, Domain domain, String accountManager) {
        buffer.append(domain.getName());
        buffer.append(";");
        if (domain.getRegistrarCode() != null) {
            buffer.append(domain.getRegistrarName());
        } else {
            buffer.append("[Aucun]");
        }
        buffer.append(";");
        buffer.append(accountManager);
        buffer.append(";");
        if (domain.hasHolder()) {
            buffer.append(domain.getHolderHandle());
        } else {
            buffer.append("[Aucun]");
        }
        buffer.append(";");
        buffer.append(domain.getAnniversaryDateStr());
        buffer.append(";");
        buffer.append(domain.getStatusAsString());
        buffer.append("\r\n");
    }

    public static StringBuffer createCsvHeader(StringBuffer stringBuffer) {
        stringBuffer.append(NAME.getDescription());
        stringBuffer.append(";");
        stringBuffer.append(REGISTRAR_NAME.getDescription());
        stringBuffer.append(";");
        stringBuffer.append(ACCOUNT_MANAGER.getDescription());
        stringBuffer.append(";");
        stringBuffer.append(TITULAIRE_NAME.getDescription());
        stringBuffer.append(";");
        stringBuffer.append(ANNIVERSARY_DATE.getDescription());
        stringBuffer.append(";");
        stringBuffer.append(STATUS.getDescription());
        stringBuffer.append("\r\n");
        return stringBuffer;
    }

    @Override
    public CharSequence createStream() throws ServiceException {

        StringBuffer back = new StringBuffer();
        back.append(StringUtils.UTF_8_HEADER);
        createCsvHeader(back);
        if ((this.domains != null) && (this.domains.size() != 0)) {
            addCsvBody(back, this.domains);
        }
        return back;
    }

}

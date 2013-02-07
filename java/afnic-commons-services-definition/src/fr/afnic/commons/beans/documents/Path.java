package fr.afnic.commons.beans.documents;

import java.io.Serializable;

import fr.afnic.commons.beans.profiling.users.UserId;
import fr.afnic.commons.services.exception.ServiceException;
import fr.afnic.commons.services.facade.AppServiceFacade;
import fr.afnic.commons.services.facade.exception.ServiceFacadeException;
import fr.afnic.commons.services.tld.TldServiceFacade;
import fr.afnic.commons.utils.Preconditions;

public class Path implements Serializable {

    private final String value;

    /**
     * Retourne un chemin qui commence et fini par /.
     * ex: /path/path/
     * 
     * @return
     */
    private static String getNormalizedPath(String path) {
        String normalizedPath = path;
        if (!normalizedPath.startsWith("/")) {
            normalizedPath = "/" + normalizedPath;
        }

        if (!normalizedPath.endsWith("/")) {
            normalizedPath = normalizedPath + "/";
        }

        return normalizedPath.replaceAll("//", "/");
    }

    public Path(String value) {
        Preconditions.checkNotEmpty("value", value);
        this.value = getNormalizedPath(value);
    }

    public String getValue() {
        return this.value;
    }

    public Path getParent() {
        if (this.value.equals("/")) {
            throw new RuntimeException("Cannot call getParent( ) on root Folder");
        }

        String parentValue = this.value.replaceAll("[^/]*/$", "");
        return new Path(parentValue);
    }

    public String getTitle() {
        if (this.value.equals("/")) {
            throw new RuntimeException("Cannot call getParent( ) on root Folder");
        }
        String title = this.value.substring(0, this.value.length() - 1);
        return title.substring(title.lastIndexOf("/") + 1);
    }

    public Folder getParentFolder(UserId userId, TldServiceFacade tld) throws ServiceException, ServiceFacadeException {
        return AppServiceFacade.getDocumentService().getOrCreateFolder(this.getParent(), userId, tld);
    }

    public String getName() {
        if (this.isRoot()) {
            return "";
        } else {
            String name = this.value.replace(this.getParent().getValue(), "");
            return name.replaceAll("/", "");
        }

    }

    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = prime * result + ((this.value == null) ? 0 : this.value.hashCode());
        return result;
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) return true;
        if (obj == null) return false;
        if (this.getClass() != obj.getClass()) return false;
        Path other = (Path) obj;
        if (this.value == null) {
            if (other.value != null) return false;
        } else if (!this.value.equals(other.value)) return false;
        return true;
    }

    public boolean isRoot() {
        return this.value.equals("/");
    }

    @Override
    public String toString() {
        return this.value;
    }
}

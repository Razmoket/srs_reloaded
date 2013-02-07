/*
 * $Id: $
 * $Revision: $
 * $Author: $
 */

package fr.afnic.commons.utils;

import java.util.Collection;

import org.apache.commons.lang.StringUtils;

import fr.afnic.commons.beans.profiling.users.UserId;
import fr.afnic.commons.beans.validatable.IValidatable;
import fr.afnic.commons.beans.validatable.InvalidDataException;
import fr.afnic.commons.beans.validatable.NumberId;
import fr.afnic.commons.checkers.NichandleChecker;
import fr.afnic.commons.services.exception.ServiceException;
import fr.afnic.commons.services.exception.invalidformat.InvalidFormatException;
import fr.afnic.commons.services.exception.invalidformat.InvalidNichandleException;
import fr.afnic.commons.services.facade.AppServiceFacade;
import fr.afnic.commons.services.tld.TldServiceFacade;

public final class Preconditions {

    private Preconditions() {
    }

    /**
     * Vérification pour une Collection de parametres. <br/>
     * Cette dernière ne peut etre null et si il s'agit d'un IValidatable, sa fonction validate ne doit pas lever d'exception.
     * 
     * @param <E>
     * @param object
     * @param name
     * @return
     * @throws InvalidFormatException
     */
    public static <F extends Collection<E>, E> F checkParameters(F objects, String name) throws InvalidFormatException {
        F notNullObjects = Preconditions.checkNotNull(objects, name);
        for (E object : notNullObjects) {
            Preconditions.checkParameter(object, "element of " + name);
        }
        return notNullObjects;
    }

    /**
     * Vérification pour un parametre. <br/>
     * Ce dernier ne peut etre null et si il s'agit dun IValidatable, sa fonction validate ne doit pa lever d'exception.
     * 
     * @param <E>
     * @param object
     * @param name
     * @return
     * @throws InvalidFormatException
     */
    public static <E> E checkParameter(E object, String name) throws InvalidFormatException {
        E notNullObject = Preconditions.checkNotNull(object, name);
        if (notNullObject instanceof IValidatable) {
            try {
                ((IValidatable) notNullObject).validate();
            } catch (InvalidDataException e) {
                // /Adaptation faite suite au refactoring du pattern validatable.
                // TODO lever directement une InvalidDataException dans cette méthode, mais cela nécéssiterait de modifier
                // De nombreux tests qui attendent une InvalidFormatException, il vaut mieux attendre d'avoir plus de temps pour le faire.
                throw new InvalidFormatException(name + ": " + e.getMessage());
            }
        }
        return notNullObject;
    }

    /**
     * Vérification qu'un parametre n'est pas null.
     * 
     * @param <E>
     * @param object
     * @param name
     * @return
     */
    public static <E> E checkNotNull(E object, String name) {
        if (object == null) {
            throw new IllegalArgumentException(name + " cannot be null.");
        } else {
            return object;
        }
    }

    public static int checkNotZero(int num, String name) {
        if (num == 0) {
            throw new IllegalArgumentException(name + " cannot be 0.");
        } else {
            return num;
        }
    }

    public static int checkGreaterThan(int num, int minValue, String name) {
        if (num < minValue) {
            throw new IllegalArgumentException(name + " cannot greater than " + minValue + ".");
        } else {
            return num;
        }
    }

    public static int checkBetween(int num, int minValue, int maxValue, String name) {
        if (num < minValue || num > maxValue) {
            throw new IllegalArgumentException(name + " must be between " + minValue + " and " + maxValue + ".");
        } else {
            return num;
        }
    }

    public static <E> E checkIsNull(E object, String name) {
        if (object != null) {
            throw new IllegalArgumentException(name + " must be null.");
        } else {
            return object;
        }
    }

    public static String checkNotLongerThan(String value, int maxLength, String name) {
        if (value != null && value.length() >= maxLength) {
            throw new IllegalArgumentException(name + " is too long expected " + maxLength + " get " + value.length() + ".");
        }
        return value;
    }

    public static void checkTrue(boolean object, String name) {
        if (!object) {
            throw new IllegalArgumentException(name + " cannot be false.");
        }
    }

    public static void checkFalse(boolean object, String name) {
        if (!object) {
            throw new IllegalArgumentException(name + " cannot be true.");
        }
    }

    /**
     * Vérification qu'un parametre n'est pas null. Si il est null, retourne l'exception passé en parametre
     * 
     * @param <E>
     * @param object
     * @param e
     * @return
     */
    public static <E> E checkNotNull(E object, ServiceException e) throws ServiceException {
        if (object == null) {
            throw e;
        } else {
            return object;
        }
    }

    /**
     * 
     * Vérifie qu'une String n'est pas null ou vide ("") ou ne contient que des espaces.
     * 
     * @param str
     * @param name
     * @return
     */
    public static String checkNotBlank(String str, String name) {
        if (StringUtils.isBlank(str)) {
            throw new IllegalArgumentException(name + " cannot be blank.");
        } else {
            return str;
        }
    }

    /**
     * 
     * Vérifie qu'une String n'est pas null ou vide ("").
     * 
     * @param str
     * @param name
     * @return
     */
    public static String checkNotEmpty(String str, String name) {
        if (StringUtils.isEmpty(str)) {
            throw new IllegalArgumentException(name + " cannot be empty.");
        } else {
            return str;
        }
    }

    /**
     * 
     * Vérifie qu'une String n'est pas null ou vide ("").
     * 
     * @param str
     * @param name
     * @return
     */
    public static <OBJECT> OBJECT[] checkNotEmpty(OBJECT[] array, String name) {
        Preconditions.checkNotNull(array, name);
        if (array.length == 0) {
            throw new IllegalArgumentException(name + " cannot be empty.");
        } else {
            return array;
        }
    }

    /**
     * Vérifie qu'une collection n'est ni nulle ni vide.
     * 
     * @param <E>
     * @param collection
     * @param name
     * @return
     */
    public static <E> Collection<E> checkNotEmpty(Collection<E> collection, String name) {
        Preconditions.checkNotNull(collection, name);
        if (collection.isEmpty()) {
            throw new IllegalArgumentException(name + " cannot be empty.");
        } else {
            return collection;
        }
    }

    public static <IDENTIFIER extends NumberId<?>> IDENTIFIER checkIsExistingIdentifier(IDENTIFIER identifier, String name, UserId userId, TldServiceFacade tld) throws ServiceException {
        Preconditions.checkNotNull(identifier, name);
        if (identifier.isNotExisting(userId, tld)) {
            throw new IllegalArgumentException(name + ": " + identifier.getName() + "[" + identifier.getValue() + "] does not exist.");
        }
        return identifier;
    }

    public static String checkIsExistingNicHandle(String nicHandle, String name, UserId userId, TldServiceFacade tld) throws ServiceException {
        Preconditions.checkNicHandle(nicHandle, name);
        if (!AppServiceFacade.getWhoisContactService().isExistingNicHandle(nicHandle, userId, tld)) {
            throw new IllegalArgumentException(name + ": nicHandle[" + nicHandle + "] does not exist.");
        }
        return nicHandle;
    }

    public static String checkNicHandle(String nicHandle, String name) throws InvalidFormatException {
        Preconditions.checkNotNull(nicHandle, name);
        try {
            new NichandleChecker().check(nicHandle);
            return nicHandle;
        } catch (InvalidNichandleException e) {
            throw new IllegalArgumentException(name + ": " + e.getMessage());
        }
    }

}

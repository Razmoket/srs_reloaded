/*
 * $Id: //depot/main/java/afnic-commons-services-definition/src/fr/afnic/commons/beans/NicHandle.java#10 $
 * $Revision: #10 $
 * $Author: ginguene $
 */

package fr.afnic.commons.beans;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

import fr.afnic.commons.services.exception.ServiceException;
import fr.afnic.commons.services.exception.invalidformat.InvalidFormatException;

/**
 * Identifiant d'un contact au sein de l'afnic. Cet identifiant est utilisé par les bureaux d'enregistrement et le support pour identifier les
 * contacts concernées.
 * 
 * TODO le découpage en prefix/num/suffix est inutile, une simple valeur devrait suffire
 * 
 * @author ginguene
 * 
 * 
 */
public class NicHandle {

    private String prefix;
    private String num;
    private String suffix;

    /*
     * Chaine représentant le pattern d'un nichandle
     */
    private static final String PATTERN_STR = "^([A-Z]+)([1-9][0-9]*)?-(FRNIC|AFNIC|FR[0-9]*)$";

    private static final Pattern PATTERN = Pattern.compile(PATTERN_STR);

    /**
     * Cree un nicHandle à partir des elements qui le composent
     * 
     * @param prefix
     * @param number
     * @param suffix
     */
    public NicHandle(String prefix, String num, String suffix) {
        this.prefix = prefix;
        this.num = num;
        this.suffix = suffix;
    }

    public static boolean isValidNicHandle(String nicHandle) {
        try {
            new NicHandle(nicHandle);
            return true;
        } catch (Exception e) {
            return false;
        }
    }

    /**
     * Cree un nichandle a partir d'un champs correspondant a l'expression reguliere suivante:<br/>
     * ^([A-Z]+)([1-9][0-9]*)?-(FRNIC|AFNIC)$<br>
     * Le nichandle est decoupe en prefix,num et suffix
     * 
     * 
     * @param nicHandle
     *            de format <prefix><number>-<suffix>
     * @throws IllegalArgumentException
     *             si le format du nichandle est invalide
     */
    public NicHandle(String nicHandle) throws ServiceException {
        if (nicHandle == null) throw new ServiceException("Argument nicHandle cannot be null");

        // pas très élégant mais je n'ai pas le temps et la GED me balance des
        // nichandle se terminant par des espace, sniff...
        while (nicHandle.endsWith(" ")) {
            nicHandle = nicHandle.trim();
        }

        Matcher matcher = PATTERN.matcher(nicHandle);
        // si recherche fructueuse
        if (matcher.matches()) {
            if (matcher.groupCount() == 3) {
                this.prefix = matcher.group(1);
                this.num = matcher.group(2);
                this.suffix = matcher.group(3);

                if (this.num == null) {
                    this.num = "0";
                }
            } else {
                throw new ServiceException("Argument nicHandle (" + nicHandle + ") format should be <prefix><number>-<suffix> ");
            }
        } else {
            // rien ne match
            throw new InvalidFormatException(nicHandle, NicHandle.class, PATTERN_STR);
        }
    }

    public String getPrefix() {
        return this.prefix;
    }

    public void setPrefix(String prefix) {
        this.prefix = prefix;
    }

    public String getNum() {
        return this.num;
    }

    public int getNumAsInt() {
        return Integer.parseInt(this.num);
    }

    public void setNum(String num) {
        this.num = num;
    }

    public String getSuffix() {
        return this.suffix;
    }

    public void setSuffix(String suffix) {
        this.suffix = suffix;
    }

    @Override
    public String toString() {
        if (!"0".equals(this.num)) {
            return this.prefix + this.num + "-" + this.suffix;
        } else {
            return this.prefix + "-" + this.suffix;
        }
    }

    public static String toString(String prefix, String num, String suffix) {
        return new NicHandle(prefix, num, suffix).toString();
    }

}

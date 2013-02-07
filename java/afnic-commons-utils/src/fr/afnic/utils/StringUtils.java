/*
 * $Id: //depot/main/java/afnic-commons-utils/src/fr/afnic/utils/StringUtils.java#1 $ $Revision: #1 $ $Author: ginguene $
 */

package fr.afnic.utils;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Random;

/**
 * Classe utilitaire permettant de générer des chaines de caractères.
 * 
 * @author ginguene
 * 
 */
public final class StringUtils {

    /** Generateur de nombres aléatoires */
    private static final Random RANDOM = new Random(System.currentTimeMillis());

    public static final String UTF_8_HEADER = new String(new byte[] { (byte) 0xEF, (byte) 0xBB, (byte) 0xBF });

    /**
     * Constructeur privé pour empecher l'implémentation de la classe
     */
    private StringUtils() {
    }

    /** Format partie date inclue dans le code d'autorisation */
    public static final SimpleDateFormat REFERENCE_DATE_FORMAT = new SimpleDateFormat("yyyyMMdd");

    /**
     * Genere un mot composé de lettre aléatoires d'une longueur passée <br/>
     * en parametre. <br/>
     * On alterne voyelles et consonne dans le mot pour obtenir des <br/>
     * mots prononcable.
     * 
     * @param length
     *            Nombre de caractères du mot à générer
     * 
     * @return un mot d'une longueur composé d'une alternance de consonne <br/>
     *         et de voyelle d'une longueru que l'on précise en parametre
     */
    public static String generateWord(final int length) {
        final String[] voyelles = { "A", "E", "I", "O", "U", "Y" };
        final String[] consonnes = { "Z", "R", "T", "P", "S", "D", "F", "G", "J", "K", "L", "M", "W", "X", "C", "V", "B", "N" };

        final StringBuffer buffer = new StringBuffer();
        for (int i = 0; i < length; i++) {
            if (i % 2 == 0) {
                // consonne
                final int rand = StringUtils.RANDOM.nextInt(consonnes.length);
                buffer.append(consonnes[rand]);
            } else {
                // voyelle
                final int rand = StringUtils.RANDOM.nextInt(voyelles.length);
                buffer.append(voyelles[rand]);
            }
        }
        return buffer.toString();
    }

    /**
     * Retourne les 8 derniers derniers caractères du timestamp
     * 
     * @return Les 8 derniers derniers caractères du timestamp
     */
    public static String getTimestampEnd() {
        final long timestamp = System.currentTimeMillis();
        final String timestampStr = Long.toString(timestamp);
        final String timestampEnd = timestampStr.substring(timestampStr.length() - 8, timestampStr.length());
        return timestampEnd;
    }

    /***
     * Génère un texte assez gros avec des caractères spéciaux idéal pour tester <br/>
     * les stockages de commentaires/descriptions et les problèmatiques d'encodages.
     * 
     * @return Une chaine de caractère longue et contenant des caractères spéciaux.
     */
    public static String generateComment() {
        final StringBuffer buffer = new StringBuffer();
        buffer.append("mon commentaire\nsur plusieurs lignes\navec des caractères spéciaux " + "[" + StringUtils.generateSpecialCharacters()
                      + "]<tag></tag> " + new Date());

        int i = 0;

        while (buffer.toString().length() < 4000) {
            buffer.append("\nline ");
            buffer.append(i);
            buffer.append(" :");
            buffer.append(StringUtils.generateWord(5));
            buffer.append(" ");
            buffer.append(StringUtils.generateWord(20));
            buffer.append(" ");
            buffer.append(StringUtils.generateWord(50));
            i++;
        }
        return buffer.toString();

    }

    /**
     * Génère une string contenant tous les caractères qui peuvent poser des problèmes d'encodage.
     * 
     * @return {@link String}
     * 
     */
    public static String generateSpecialCharacters() {
        return "#@?!;:à&€éêéêëîïôùûüÿæœéêëîïôùûüÿæœ";

    }

    /**
     * Retire les accents d'une chaine de caractère
     * 
     * @param s
     * @return
     */
    public static String unaccent(String s) {
        s = s.replaceAll("[èéêë]", "e");
        s = s.replaceAll("[ûù]", "u");
        s = s.replaceAll("[ïî]", "i");
        s = s.replaceAll("[àâ]", "a");
        s = s.replaceAll("Ô", "o");

        s = s.replaceAll("[ÈÉÊË]", "E");
        s = s.replaceAll("[ÛÙ]", "U");
        s = s.replaceAll("[ÏÎ]", "I");
        s = s.replaceAll("[ÀÂ]", "A");
        s = s.replaceAll("Ô", "O");

        s = s.replaceAll("œ", "oe");
        return s;
    }

    /**
     * Pour éviter les lignes trop longues découpe les lignes en introduisant des espaces
     */
    public static String splitString(String str, int maxLength) {
        String ret = "";
        int count = 0;

        for (int i = 0; i < str.length(); i++) {
            count++;
            ret += str.charAt(i);
            if (count == maxLength) {
                count = 0;
                ret += "<br/>";
            }
        }
        return ret;

    }
}

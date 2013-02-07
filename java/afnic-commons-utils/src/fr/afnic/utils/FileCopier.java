package fr.afnic.utils;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.nio.channels.FileChannel;

/**
 * Classe permettant de copier des fichier car java n'a pas de moyen natif de le faire
 * 
 * @author ginguene
 * 
 */
public final class FileCopier {

    /**
     * Constructeur privé pour empecher l'instanciation
     */
    private FileCopier() {
    }

    /**
     * @see FileCopier#copy
     */
    public static boolean copy(final File src, final String dest) throws IOException {
        return FileCopier.copy(src, new File(dest));
    }

    /**
     * @see FileCopier#copy
     */
    public static boolean copy(final String src, final File dest) throws IOException {
        return FileCopier.copy(new File(src), dest);
    }

    /**
     * @see FileCopier#copy
     */
    public static boolean copy(final String src, final String dest) throws IOException {
        return FileCopier.copy(new File(src), new File(dest));
    }

    /**
     * Copie un fichier. Si le fichier de destination existe deja, la copie ne se fait pas. Si le repertorie contenant le fichier de destination
     * n'existe pas, ce repertoire est cree
     * 
     * @param src
     * @param dest
     * @return true si la copie s'est bien passe
     * @throws Exception
     */
    public static boolean copy(final File src, final File dest) throws IOException {

        if (!src.exists()) {
            throw new FileNotFoundException("File " + src + " not found");
        }

        if (dest.exists()) {
            return false;
        }

        // on cree le repertoire contenant le fichier de destination
        final File destDir = dest.getParentFile();
        if (destDir != null && !destDir.exists()) {
            if (!destDir.mkdirs()) {
                throw new IOException("Failed to mkdirs " + destDir.getAbsolutePath());
            }
        }

        FileChannel in = null; // canal d'entrée
        FileChannel out = null; // canal de sortie
        try {
            in = new FileInputStream(src).getChannel();
            out = new FileOutputStream(dest).getChannel();

            // Copie depuis le in vers le out
            in.transferTo(0, in.size(), out);

            return true;
        } catch (final IOException e) {
            throw e;
        } finally {
            if (in != null) {
                try {
                    in.close();
                } catch (final IOException e) {
                    e.printStackTrace();
                }
            }
            if (out != null) {
                try {
                    out.close();
                } catch (final IOException e) {
                    e.printStackTrace();
                }
            }
        }

    }
}

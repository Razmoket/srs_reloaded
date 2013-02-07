package fr.afnic.utils;

public final class ExceptionUtils {

    /**
     * Retourne le message de la cause mère de toutes la chaine d'exception.
     * 
     * @return Le message de la première exception rencontréé
     */
    public static String getFirstCauseMessage(final Exception exception) {
        if (exception.getCause() != null) {
            return ExceptionUtils.getFirstCauseMessage(exception.getCause());
        } else {
            return exception.getMessage();
        }
    }

    /**
     * Retourne le message de la cause mère de toutes la chaine d'exception <br/>
     * liée à l'exception passée en parametre.
     * 
     * @param throwable
     *            Exception dont on veut le message de la cause mère.
     * 
     * @return Le message de la première exception rencontréé.
     */
    private static String getFirstCauseMessage(final Throwable throwable) {
        if (throwable.getCause() != null) {
            return ExceptionUtils.getFirstCauseMessage(throwable.getCause());
        } else {
            return throwable.getMessage();
        }
    }

    private ExceptionUtils() {

    }

}

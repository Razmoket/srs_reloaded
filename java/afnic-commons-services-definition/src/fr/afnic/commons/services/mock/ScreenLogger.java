/*
 * $Id: //depot/main/java/afnic-commons-services-definition/src/fr/afnic/commons/services/mock/ScreenLogger.java#8 $
 * $Revision: #8 $
 * $Author: ginguene $
 */

package fr.afnic.commons.services.mock;

import java.io.PrintStream;
import java.text.SimpleDateFormat;
import java.util.Date;

import fr.afnic.commons.beans.logs.ILogger;
import fr.afnic.commons.beans.logs.Level;

/**
 * Logger simple qui écrit les message de debug/info/trace dans System.out et de error/warn dans system.err.
 * 
 * @author ginguene
 * 
 */
public class ScreenLogger implements ILogger {

    private final String className;
    private static final SimpleDateFormat FORMAT = new SimpleDateFormat("dd/MM/yyy HH:mm:ss.S");

    private static Level level = Level.TRACE;

    public static void setLevel(Level level) {
        ScreenLogger.level = level;
    }

    public ScreenLogger(Class<?> clazz) {
        this.className = clazz.getSimpleName();
    }

    @Override
    public void debug(String message, Exception e) {
        this.writteMessage(Level.DEBUG, message, e);
    }

    @Override
    public void debug(String message) {
        this.writteMessage(Level.DEBUG, message);
    }

    @Override
    public void error(String message, Exception e) {
        this.writteMessage(Level.ERROR, message, e);
    }

    @Override
    public void error(String message) {
        this.writteMessage(Level.ERROR, message);
    }

    @Override
    public void info(String message, Exception e) {
        this.writteMessage(Level.INFO, message, e);
    }

    @Override
    public void info(String message) {
        this.writteMessage(Level.INFO, message);
    }

    @Override
    public void trace(String message, Exception e) {
        this.writteMessage(Level.TRACE, message, e);
    }

    @Override
    public void trace(String message) {
        this.writteMessage(Level.TRACE, message);
    }

    @Override
    public void warn(String message, Exception e) {
        this.writteMessage(Level.WARN, message, e);
    }

    @Override
    public void warn(String message) {
        this.writteMessage(Level.WARN, message);
    }

    @Override
    public boolean isDebugEnabled() {
        return true;
    }

    @Override
    public boolean isInfoEnabled() {
        return true;
    }

    @Override
    public boolean isTraceEnabled() {
        return true;
    }

    public void writteMessage(Level level, String message, Exception e) {
        this.writteMessage(level, message);
        e.printStackTrace();
    }

    static int a = 0;

    public void writteMessage(Level level, String message) {
        if (level.intValue() >= ScreenLogger.level.intValue()) {
            PrintStream printStream = this.getPrintStreamForLevel(level);
            printStream.println(a++ + "[" + level + "][" + ScreenLogger.FORMAT.format(new Date()) + "][" + this.className + "]" + level.toString() + ": " + message);
        }

    }

    private PrintStream getPrintStreamForLevel(Level level) {
        if (level == Level.WARN || level == Level.ERROR) {
            return System.err;
        } else {
            return System.out;
        }
    }

    @Override
    public String getName() {
        return this.className;
    }

}

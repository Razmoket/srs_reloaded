/*
 * $Id: //depot/main/java/afnic-commons-services-definition/src/fr/afnic/commons/beans/logs/Log.java#3 $
 * $Revision: #3 $
 * $Author: ginguene $
 */

package fr.afnic.commons.beans.logs;

import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * 
 * Représente une ligne de log
 * 
 * @author ginguene
 * 
 */
public class Log {

    private SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss S");

    private long timeStamp;
    private String message;
    private String loggerName;
    private String threadName;
    private Level level;
    private String stacktrace;

    public boolean hasStacktrace() {
        return !"".equals(this.getStacktrace());
    }

    public String getStacktrace() {
        if (stacktrace == null) return "";
        return stacktrace;
    }

    public void setStacktrace(String stacktrace) {
        this.stacktrace = stacktrace;
    }

    public long getTimeStamp() {
        return timeStamp;
    }

    public void setTimeStamp(long timeStamp) {
        this.timeStamp = timeStamp;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public String getLoggerName() {
        return loggerName;
    }

    public void setLoggerName(String loggerName) {
        this.loggerName = loggerName;
    }

    public String getThreadName() {
        return threadName;
    }

    public void setThreadName(String threadName) {
        this.threadName = threadName;
    }

    public Level getLevel() {
        return level;
    }

    public void setLevel(Level level) {
        this.level = level;
    }

    public Date getDate() {
        return new Date(this.timeStamp);
    }

    public String getDateStr() {
        return format.format(this.getDate());
    }

}

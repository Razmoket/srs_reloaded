package fr.afnic.utils.sql.pool;

public class PoolSqlConnectionConfiguration {

    /**url de connection a la base de donnée*/
    private String url;

    /**Nombre maximal de connection fournies par le pool*/
    private int poolSize = 20;

    /**Nombre maximal d'utilisation d'une connection*/
    private int maxUsePerConnection = 100;

    /**Nombre maximal de réutilisation d'une connection en ms*/
    private long maxConnectionUseTime = 10000;

    /**Temp maximal d'attente d'une connection en ms*/
    private long timeout = 30000;

    public void setTimeout(long timeout) {
        this.timeout = timeout;
    }

    public long getTimeout() {
        return this.timeout;
    }

    public long getMaxConnectionUseTime() {
        return this.maxConnectionUseTime;
    }

    public void setMaxConnectionUseTime(long maxConnectionUseTime) {
        this.maxConnectionUseTime = maxConnectionUseTime;
    }

    public String getUrl() {
        return this.url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public int getPoolSize() {
        return this.poolSize;
    }

    public void setPoolSize(int poolSize) {
        this.poolSize = poolSize;
    }

    public int getMaxUsePerConnection() {
        return this.maxUsePerConnection;
    }

    public void setMaxUsePerConnection(int maxUsePerConnection) {
        this.maxUsePerConnection = maxUsePerConnection;
    }

}

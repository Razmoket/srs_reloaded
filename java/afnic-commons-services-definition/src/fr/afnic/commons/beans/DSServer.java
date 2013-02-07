package fr.afnic.commons.beans;

import java.io.Serializable;

import fr.afnic.commons.beans.dnssec.AlgoHash;
import fr.afnic.commons.beans.dnssec.DigestHash;

public class DSServer implements Serializable {

    private static final long serialVersionUID = 1L;

    private int keyTag;
    private AlgoHash algoHash;
    private DigestHash digestHash;
    private String keyDigest;

    public int getKeyTag() {
        return keyTag;
    }

    public void setKeyTag(int keyTag) {
        this.keyTag = keyTag;
    }

    public AlgoHash getAlgoHash() {
        return algoHash;
    }

    public void setAlgoHash(AlgoHash algoHash) {
        this.algoHash = algoHash;
    }

    public DigestHash getDigestHash() {
        return digestHash;
    }

    public void setDigestHash(DigestHash digestHash) {
        this.digestHash = digestHash;
    }

    public String getKeyDigest() {
        return keyDigest;
    }

    public void setKeyDigest(String keyDigest) {
        this.keyDigest = keyDigest;
    }

}

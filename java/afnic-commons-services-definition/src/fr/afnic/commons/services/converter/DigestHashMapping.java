package fr.afnic.commons.services.converter;

import fr.afnic.commons.beans.dnssec.DigestHash;

public class DigestHashMapping extends ConstantTypeMapping<Integer, DigestHash> {

    public DigestHashMapping() {
        super(Integer.class, DigestHash.class);
    }

    @Override
    protected void populateMap() {
        this.addMapping(0, DigestHash.RESERVED);
        this.addMapping(1, DigestHash.SHA_1);
        this.addMapping(2, DigestHash.SHA_256);
        this.addMapping(3, DigestHash.GOST_R_34_11_94);
        this.addMapping(4, DigestHash.SHA_384);
    }
}

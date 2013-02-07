package fr.afnic.commons.services.converter;

import fr.afnic.commons.beans.dnssec.AlgoHash;

public class AlgoHashMapping extends ConstantTypeMapping<Integer, AlgoHash> {

    public AlgoHashMapping() {
        super(Integer.class, AlgoHash.class);
    }

    @Override
    protected void populateMap() {
        this.addMapping(0, AlgoHash.NON_RENSEIGNE);
        this.addMapping(1, AlgoHash.RSAMD5);
        this.addMapping(2, AlgoHash.DH);
        this.addMapping(3, AlgoHash.DSA);
        this.addMapping(4, AlgoHash.ECC);
        this.addMapping(5, AlgoHash.RSASHA1);
        this.addMapping(6, AlgoHash.DSA_NSEC3_SHA1);
        this.addMapping(7, AlgoHash.RSASHA1_NSEC3_SHA1);
        this.addMapping(8, AlgoHash.RSASHA256);
        this.addMapping(10, AlgoHash.RSASHA512);
        this.addMapping(12, AlgoHash.ECC_GOST);
        this.addMapping(13, AlgoHash.ECDSAP256SHA256);
        this.addMapping(14, AlgoHash.ECDSAP384SHA384);
        this.addMapping(252, AlgoHash.INDIRECT);
        this.addMapping(253, AlgoHash.PRIVATEDNS);
        this.addMapping(254, AlgoHash.PRIVATEOID);
    }
}

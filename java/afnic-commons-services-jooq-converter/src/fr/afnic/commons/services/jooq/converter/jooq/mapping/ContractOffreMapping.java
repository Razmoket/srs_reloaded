package fr.afnic.commons.services.jooq.converter.jooq.mapping;

import fr.afnic.commons.beans.contract.ContractOffre;
import fr.afnic.commons.services.converter.ConstantTypeMapping;

public class ContractOffreMapping extends ConstantTypeMapping<Integer, ContractOffre> {

    public ContractOffreMapping() {
        super(Integer.class, ContractOffre.class);
    }

    @Override
    protected void populateMap() {
        this.addMapping(Integer.valueOf(1), ContractOffre.Mbe);
        this.addMapping(Integer.valueOf(2), ContractOffre.Upm);
        this.addMapping(Integer.valueOf(3), ContractOffre.Upp);
        this.addMapping(Integer.valueOf(4), ContractOffre.Ci);
        this.addMapping(Integer.valueOf(5), ContractOffre.Hpp);
        this.addMapping(Integer.valueOf(6), ContractOffre.Ca);
        this.addMapping(Integer.valueOf(7), ContractOffre.Opt1);
        this.addMapping(Integer.valueOf(8), ContractOffre.Opt2);
        this.addMapping(Integer.valueOf(9), ContractOffre.Squaw);
        this.addMapping(Integer.valueOf(10), ContractOffre.Parl);
        this.addMapping(Integer.valueOf(11), ContractOffre.Institu);
        this.addMapping(Integer.valueOf(12), ContractOffre.Fond);
        this.addMapping(Integer.valueOf(13), ContractOffre.Ampleur);
        this.addMapping(Integer.valueOf(14), ContractOffre.Acces);
        this.addMapping(Integer.valueOf(15), ContractOffre.Essor);
    }

}

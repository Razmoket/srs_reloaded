package fr.afnic.commons.services.converter;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Set;

import fr.afnic.commons.beans.logs.ILogger;
import fr.afnic.commons.beans.profiling.users.UserId;
import fr.afnic.commons.services.exception.ServiceException;
import fr.afnic.commons.services.facade.AppServiceFacade;
import fr.afnic.commons.services.tld.TldServiceFacade;

public abstract class ConverterFacade {

    protected static final ILogger LOGGER = AppServiceFacade.getLoggerService().getLogger(ConverterFacade.class);

    protected ConverterFacade() {
    }

    protected ArrayList<AbstractConverter<?, ?>> listOfConverter = new ArrayList<AbstractConverter<?, ?>>();

    protected ArrayList<ConstantTypeMapping<?, ?>> listOfMapping = new ArrayList<ConstantTypeMapping<?, ?>>();

    @SuppressWarnings("unchecked")
    public <IN, OUT> OUT convertImpl(IN instanceObject, Class<OUT> outClass, UserId userId, TldServiceFacade tld) throws ServiceException {
        if (instanceObject == null) {
            return null;
        }

        AbstractConverter<IN, OUT> converter = this.getConverter((Class<IN>) instanceObject.getClass(), outClass);

        if (converter == null) {
            converter = this.getConverter((Class<IN>) instanceObject.getClass().getSuperclass(), outClass);

            if (converter == null) {
                //on cherche un mapping
                ConstantTypeMapping<IN, OUT> mapping = this.getMapping((Class<IN>) instanceObject.getClass(), outClass);
                if (mapping != null) {
                    return mapping.getCommonsValue(instanceObject, userId, tld);
                } else {
                    ConstantTypeMapping<OUT, IN> mappingOther = this.getMapping(outClass, (Class<IN>) instanceObject.getClass());
                    if (mappingOther != null) {
                        return mappingOther.getOtherModelValue(instanceObject);
                    }
                }
                throw new IllegalArgumentException("Can't find a converter with inClass:" + instanceObject.getClass() + " outClass:" + outClass);
            }
        }
        return converter.convert(instanceObject, userId, tld);
    }

    public <IN, OUT> List<OUT> convertListImpl(List<IN> listInstanceObject, Class<OUT> outClass, UserId userId, TldServiceFacade tld) throws ServiceException {
        ArrayList<OUT> returnList = new ArrayList<OUT>();
        if (listInstanceObject != null) {
            for (IN inObject : listInstanceObject) {
                returnList.add(this.convertImpl(inObject, outClass, userId, tld));
            }
        }
        return returnList;
    }

    public <IN, OUT> Set<OUT> convertSetImpl(Set<IN> listInstanceObject, Class<OUT> outClass, UserId userId, TldServiceFacade tld) throws ServiceException {
        Set<OUT> returnSet = new HashSet<OUT>();
        if (listInstanceObject != null) {
            for (IN inObject : listInstanceObject) {
                returnSet.add(this.convertImpl(inObject, outClass, userId, tld));
            }
        }
        return returnSet;
    }

    public <IN, OUT> List<OUT> convertIteratorImpl(Iterator<IN> iteratorInstanceObject, Class<OUT> outClass, UserId userId, TldServiceFacade tld) throws ServiceException {
        List<OUT> returnSet = new ArrayList<OUT>();
        if (iteratorInstanceObject != null) {
            while (iteratorInstanceObject.hasNext()) {
                IN inObject = iteratorInstanceObject.next();
                returnSet.add(this.convertImpl(inObject, outClass, userId, tld));
            }
        }
        return returnSet;
    }

    @SuppressWarnings("unchecked")
    protected <IN, OUT> AbstractConverter<IN, OUT> getConverter(Class<IN> inClass, Class<OUT> outClass) throws ServiceException {
        //Correspondance exacte entre les classe d'entréés et de sortie
        for (AbstractConverter<?, ?> converter : this.listOfConverter) {
            if (converter.getInClass().equals(inClass) && converter.getOutClass().equals(outClass)) {
                return (AbstractConverter<IN, OUT>) converter;
            }
        }

        for (AbstractConverter<?, ?> converter : this.listOfConverter) {
            if (converter.getInClass().isInterface()
                && converter.getInClass().isAssignableFrom(inClass)
                && converter.getOutClass().equals(outClass)) {
                return (AbstractConverter<IN, OUT>) converter;
            }
        }

        for (AbstractConverter<?, ?> converter : this.listOfConverter) {
            if (converter.getInClass().isInterface()
                && converter.getInClass().isAssignableFrom(inClass)
                && converter.getOutClass().isAssignableFrom(outClass)) {
                return (AbstractConverter<IN, OUT>) converter;
            }
        }

        return null;
    }

    @SuppressWarnings("unchecked")
    protected <IN, OUT> ConstantTypeMapping<IN, OUT> getMapping(Class<IN> inClass, Class<OUT> outClass) throws ServiceException {
        for (ConstantTypeMapping<?, ?> mapping : this.listOfMapping) {
            if (mapping.getOtherModelClass().equals(inClass) && mapping.getCommonsClass().equals(outClass)) {
                return (ConstantTypeMapping<IN, OUT>) mapping;
            }
        }

        for (ConstantTypeMapping<?, ?> mapping : this.listOfMapping) {
            if (mapping.getOtherModelClass().equals(inClass.getSuperclass()) && mapping.getCommonsClass().equals(outClass)) {
                return (ConstantTypeMapping<IN, OUT>) mapping;
            }
        }

        for (ConstantTypeMapping<?, ?> mapping : this.listOfMapping) {
            if (mapping.getOtherModelClass().equals(inClass) && mapping.getCommonsClass().equals(outClass.getSuperclass())) {
                return (ConstantTypeMapping<IN, OUT>) mapping;
            }
        }
        return null;
    }

    protected void registerConverterImpl(final AbstractConverter<?, ?> converter) {
        if (!this.listOfConverter.contains(converter)) {
            this.listOfConverter.add(converter);
        }
    }

    protected void registerMappingImpl(final ConstantTypeMapping<?, ?> mapping) {
        if (!this.listOfMapping.contains(mapping)) {
            this.listOfMapping.add(mapping);
        }
    }

    @Override
    public String toString() {
        StringBuilder builder = new StringBuilder("ConverterFacade with " + this.listOfConverter.size() + " converters:\n");

        for (AbstractConverter<?, ?> converter : this.listOfConverter) {
            builder.append(" -  " + converter.getInClass().getSimpleName() + " to " + converter.getOutClass().getSimpleName() + "\n");
        }
        return builder.toString();
    }
}

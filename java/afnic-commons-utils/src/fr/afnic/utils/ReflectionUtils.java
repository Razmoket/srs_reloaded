package fr.afnic.utils;

import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map.Entry;

import org.apache.commons.lang.StringUtils;

import com.google.common.base.Preconditions;

/***
 * 
 * Met à disposition des méthodes facilitant la reflexion.
 * 
 * @author ginguene
 * 
 */
public final class ReflectionUtils {

    /**
     * Retourne la liste des champs accéssible via des getter et setter.<br/>
     * La notion de champs peut etre virtuelle.<br/>
     * Par ex si une class a une methode getName et une autre setName,<br/>
     * meme si elle ne possède pas de champs name, Name sera retournée dans la liste.<br/>
     * 
     * @param clazz
     * @return
     */
    public static List<String> findFieldsNameWithGetterAndSetter(final Class<?> clazz) {
        final List<String> list = new ArrayList<String>();
        for (final Method getter : ReflectionUtils.findGettersAndSetters(clazz).keySet()) {
            list.add(getter.getName().replaceFirst("get", ""));
        }
        return list;
    }

    /**
     * retourne un getter pour un champs dont on passe le nom en parametre.
     * 
     * @param fieldName
     * @param clazz
     * @return
     * @throws NoSuchMethodException
     */
    public static Method findGetter(final String fieldName, final Class<?> clazz) throws NoSuchMethodException {
        Preconditions.checkNotNull(fieldName, "fieldName cannot be null.");
        Preconditions.checkNotNull(clazz, "fieldName cannot be clazz.");

        final String getterName = "get" + StringUtils.capitalize(fieldName);

        for (final Method method : clazz.getMethods()) {
            if (ReflectionUtils.isGetter(method) && getterName.equals(method.getName())) {
                return method;
            }
        }

        throw new NoSuchMethodException("no method " + getterName + " in class " + clazz.getSimpleName());

    }

    public static Method findSetter(final String fieldName, final Class<?> clazz) throws NoSuchMethodException {
        Preconditions.checkNotNull(fieldName, "fieldName cannot be null.");
        Preconditions.checkNotNull(clazz, "fieldName cannot be clazz.");

        final String getterName = "set" + StringUtils.capitalize(fieldName);

        for (final Method method : clazz.getMethods()) {
            if (ReflectionUtils.isSetter(method) && getterName.equals(method.getName())) {
                return method;
            }
        }

        throw new NoSuchMethodException("no method " + getterName + " in class " + clazz.getSimpleName());

    }

    /**
     * retourne une map contenant en clé le getter, et en valeur le setter. La map contient des association de getter setter concernant le meme
     * champs. ex: sur une classe Personne possédant les méthodes suivantes: getPhone() setFirstName(...) getFirstName(); setLastName(...)
     * 
     * 
     * 
     * 
     * @return
     */
    public static HashMap<Method, Method> findGettersAndSetters(final Class<?> clazz) {
        final HashMap<Method, Method> map = new HashMap<Method, Method>();

        final HashMap<String, Method> getters = new HashMap<String, Method>();
        final HashMap<String, Method> setters = new HashMap<String, Method>();

        for (final Method method : clazz.getMethods()) {
            if (ReflectionUtils.isGetter(method)) {
                getters.put(method.getName().replaceFirst("get", ""), method);
            } else if (ReflectionUtils.isSetter(method)) {
                setters.put(method.getName().replaceFirst("set", ""), method);
            }
        }

        for (final Entry<String, Method> entry : getters.entrySet()) {
            if (setters.containsKey(entry.getKey())) {
                final Method setter = setters.get(entry.getKey());
                final Method getter = entry.getValue();
                if (setter.getParameterTypes()[0].equals(getter.getReturnType())) {
                    map.put(getter, setter);
                }
            }
        }

        return map;
    }

    public static boolean isGetter(final Method method) {
        return method.getReturnType() != null && method.getName().startsWith("get") && method.getParameterTypes().length == 0;
    }

    public static boolean isSetter(final Method method) {
        return method.getReturnType() == void.class && method.getName().startsWith("set") && method.getParameterTypes().length == 1;

    }

    private ReflectionUtils() {

    }

}

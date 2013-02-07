package fr.afnic.commons.test;

import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map.Entry;

import junit.framework.Assert;

/**
 * Classe permettant de vérifier que les getter/setter d'un bean fonctionnent correctement.<br/>
 * Seules les couple de getter/setter permettant d'agir sur le meme attribut sont testée.<br/>
 * ex soit une classe avec les méthode getAttr1(), setAttr1(), getAttr2() et setAttr3(), seules<br/>
 * les méthode getAttr1() et setAttr1() seront testées. <br/>
 * De plus si l'attribut, doit etre soit un type primitif, soit une String soit un objet ayant un constructeur sans parametre.<br/>
 * La classe testée doit etre instanciable (pas de classe abstraite).
 * 
 * 
 * 
 * 
 * @author ginguene
 * 
 */
public final class BeanTestCase {

    public static final IBeanTestCaseMethodRule ACCEPT_ALL = new IBeanTestCaseMethodRule() {
        @Override
        public boolean hasToAssertSetter(Method setter) {
            return true;
        };
    };

    public static void assertSettersAndGetters(Class<?> clazz) throws Exception {
        BeanTestCase.assertSettersAndGetters(clazz, BeanTestCase.ACCEPT_ALL);
    }

    public static void assertSettersAndGetters(Class<?> clazz, IBeanTestCaseMethodRule rule) throws Exception {

        HashMap<String, Method> setters = new HashMap<String, Method>();
        HashMap<String, Method> getters = new HashMap<String, Method>();

        for (Method method : clazz.getMethods()) {
            if (method.getName().startsWith("set")) {
                if (rule.hasToAssertSetter(method)) {
                    String setterAttributeName = method.getName().replaceFirst("set", "").toLowerCase();
                    setters.put(setterAttributeName, method);
                }
            }
            if (method.getName().startsWith("get")) {
                String getterAttributeName = method.getName().replaceFirst("get", "").toLowerCase();
                getters.put(getterAttributeName, method);
            }
        }

        List<String> attributesWithGetterAndSetter = new ArrayList<String>();

        for (Entry<String, Method> setterEntry : setters.entrySet()) {
            String setterAttributeName = setterEntry.getKey();
            for (Entry<String, Method> getterEntry : getters.entrySet()) {
                String getterAttributeName = getterEntry.getKey();
                if (setterAttributeName.equals(getterAttributeName)) {
                    attributesWithGetterAndSetter.add(setterAttributeName);
                }
            }
        }

        for (String attributeName : attributesWithGetterAndSetter) {
            Method setter = setters.get(attributeName);
            Method getter = getters.get(attributeName);

            Class<?>[] types = setter.getParameterTypes();

            if (types != null && types.length == 1) {
                Class<?> paramClass = types[0];

                List<Object> instancesToTest = BeanTestCase.getInstancesOfClass(paramClass);
                for (Object instanceToTest : instancesToTest) {
                    BeanTestCase.assertInstanceForGetterAndSetter(clazz, getter, setter, instanceToTest);
                }
            }
        }

    }

    private static void assertInstanceForGetterAndSetter(Class<?> classToTest, Method getter, Method setter, Object instanceToTest) throws Exception {
        Object object = null;
        try {
            object = classToTest.newInstance();
        } catch (Exception e) {
            throw new Exception(classToTest.getSimpleName() + " cannot be instanciated", e);
        }

        try {
            setter.invoke(object, instanceToTest);
        } catch (Exception e) {
            Assert.fail(classToTest.getSimpleName() + "." + setter.getName() + "(" + instanceToTest + ") failed " + e.getMessage());
        }

        Object getterReturn = null;
        try {
            getterReturn = getter.invoke(object);
        } catch (Exception e) {
            Assert.fail(classToTest.getSimpleName() + "." + getter.getName() + "() failed with value " + instanceToTest + ":" + e.getMessage());
        }

        try {
            Assert.assertEquals(classToTest.getSimpleName() + "." + setter.getName() + "(" + instanceToTest + ")/" + getter.getName() + " failed ",
                                instanceToTest,
                                getterReturn);

        } catch (Exception e) {
            e.printStackTrace();
            Assert.fail(classToTest.getSimpleName() + "." + setter.getName() + "(" + instanceToTest + ")/" + getter.getName() + " failed : " + e.getMessage());
        }
    }

    private static List<Object> getInstancesOfClass(Class<?> clazz) throws Exception {
        if (clazz.isPrimitive()) {
            return BeanTestCase.getInstancesOfClassForPrimitiveClass(clazz);
        } else {
            return BeanTestCase.getInstancesOfClassForNonPrimitiveClass(clazz);
        }
    }

    private static List<Object> getInstancesOfClassForNonPrimitiveClass(Class<?> clazz) throws Exception {
        List<Object> instances = new ArrayList<Object>();

        if (Collection.class.isAssignableFrom(clazz)) {
            return instances;
        }

        if (clazz.isEnum()) {
            for (Object val : clazz.getEnumConstants()) {
                instances.add(val);
            }
            return instances;
        }

        //instances.add(null);

        if (clazz == String.class) {
            instances.add("test");
        } else {
            try {
                instances.add(clazz.newInstance());
            } catch (Exception e) {
                // l'attribut du setter ne possede pas de constructeur sans parametre.
                //Dans ce cas là, le getter/setter ne sera pas testé
            }
        }

        return instances;
    }

    private static List<Object> getInstancesOfClassForPrimitiveClass(Class<?> clazz) throws Exception {
        return Arrays.asList(BeanTestCase.getInstanceOfClassForPrimitiveClass(clazz));
    }

    private static Object getInstanceOfClassForPrimitiveClass(Class<?> clazz) throws Exception {

        String typeName = clazz.getName();
        if (typeName.equals("boolean")) {
            return Boolean.TRUE;
        }

        if (typeName.equals("int")) {
            return Integer.valueOf(2);
        }

        if (typeName.equals("char")) {
            return Character.valueOf('c');
        }

        if (typeName.equals("byte")) {
            return Byte.valueOf((byte) 2);
        }

        if (typeName.equals("short")) {
            return Short.valueOf((short) 2);
        }

        if (typeName.equals("long")) {
            return Long.valueOf(2);
        }

        if (typeName.equals("float")) {
            return Float.valueOf(2.1f);
        }

        if (typeName.equals("double")) {
            return Double.valueOf(2.1);
        }

        throw new Exception("no instance defined for primitive type: " + typeName);
    }

    private BeanTestCase() {

    }

}

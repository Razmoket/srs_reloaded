package fr.afnic.utils;

import java.lang.reflect.Method;
import java.util.HashMap;
import java.util.List;
import java.util.Map.Entry;

import junit.framework.Assert;

import org.junit.Test;

public class ReflectionUtilsTest {

    @Test
    public void testFindFieldsNameWithGetterAndSetter() {
        final List<String> names = ReflectionUtils.findFieldsNameWithGetterAndSetter(Person.class);
        Assert.assertEquals(1, names.size());
        Assert.assertEquals("FirstName", names.get(0));
    }

    @Test
    public void testFindGettersAndSetters() {
        final HashMap<Method, Method> map = ReflectionUtils.findGettersAndSetters(Person.class);

        Assert.assertEquals(1, map.size());

        final Entry<Method, Method> entry = map.entrySet().iterator().next();
        final Method getter = entry.getKey();
        final Method setter = entry.getValue();

        Assert.assertEquals("getFirstName", getter.getName());
        Assert.assertEquals("setFirstName", setter.getName());
    }

    @Test
    public void testIsGetter() {
        for (final Method method : Person.class.getMethods()) {
            if (method.getName().equals("getFirstName")) {
                Assert.assertTrue(ReflectionUtils.isGetter(method));
            }

            if (method.getName().equals("setFirstName")) {
                Assert.assertFalse(ReflectionUtils.isGetter(method));
            }

            if (method.getName().equals("getLastName")) {
                Assert.assertTrue(ReflectionUtils.isGetter(method));
            }

            if (method.getName().equals("setNickName")) {
                Assert.assertFalse(ReflectionUtils.isGetter(method));
            }

            if (method.getName().equals("getWithParam")) {
                Assert.assertFalse(ReflectionUtils.isGetter(method));
            }

            if (method.getName().equals("setWithParam")) {
                Assert.assertFalse(ReflectionUtils.isGetter(method));
            }

            if (method.getName().equals("getId")) {
                Assert.assertTrue(ReflectionUtils.isGetter(method));
            }

            if (method.getName().equals("setId")) {
                Assert.assertFalse(ReflectionUtils.isGetter(method));
            }

        }
    }

    @Test
    public void testIsSetter() {
        for (final Method method : Person.class.getMethods()) {
            if (method.getName().equals("getFirstName")) {
                Assert.assertFalse(ReflectionUtils.isSetter(method));
            }

            if (method.getName().equals("setFirstName")) {
                Assert.assertTrue(ReflectionUtils.isSetter(method));
            }

            if (method.getName().equals("getLastName")) {
                Assert.assertFalse(ReflectionUtils.isSetter(method));
            }

            if (method.getName().equals("setNickName")) {
                Assert.assertTrue(ReflectionUtils.isSetter(method));
            }

            if (method.getName().equals("getWithParam")) {
                Assert.assertFalse(ReflectionUtils.isSetter(method));
            }

            if (method.getName().equals("setWithParam")) {
                Assert.assertTrue(ReflectionUtils.isSetter(method));
            }

            if (method.getName().equals("setWithTwoParam")) {
                Assert.assertFalse(ReflectionUtils.isSetter(method));
            }

            if (method.getName().equals("getId")) {
                Assert.assertFalse(ReflectionUtils.isSetter(method));
            }

            if (method.getName().equals("setId")) {
                Assert.assertTrue(ReflectionUtils.isSetter(method));
            }

        }
    }

    /**
     * Classe utilisée pour faire les tests
     * 
     * @author ginguene
     * 
     */
    class Person {

        public String getFirstName() {
            return "";
        }

        public void setFirstName(final String param) {
        }

        public String getLastName() {
            return "";
        }

        public void setNickName(final String param) {
        }

        public String getWithParam(final int nb) {
            return "";
        }

        public void setWithParam(final String param) {
        }

        public void setWithTwoParam(final String param1, final String param2) {
        }

        public String getId() {
            return "";
        };

        public void setId(final int param) {
        };

    }

}

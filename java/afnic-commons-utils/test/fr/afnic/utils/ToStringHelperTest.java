package fr.afnic.utils;

import junit.framework.Assert;

import org.junit.Test;

public class ToStringHelperTest {

    @Test
    public void testOk() {
        Assert.assertEquals("ToStringHelper: tutu[toto]", new ToStringHelper("ToStringHelper").add("tutu", "toto").toString());
        Assert.assertEquals("ToStringHelper: param1[val1] param2[val2]",
                new ToStringHelper("ToStringHelper").add("param1", "val1").add("param2", "val2").toString());
    }

    @Test
    public void testAddAllAttributes() {
        Assert.assertEquals("TestObject: param1[value1] param2[value2]", new ToStringHelper().addAllObjectAttributes(new TestObject()).toString());
    }

    @Test
    public void testAddAvecConstructeurParDefaut() {
        final ToStringHelper tsh = new ToStringHelper("ToStringHelper");
        Assert.assertEquals("ToStringHelper: tutu[toto]", tsh.add("tutu", "toto").toString());
        Assert.assertEquals("ToStringHelper: tutu[toto] param1[value1] param2[value2]", tsh.addAllObjectAttributes(new TestObject()).toString());
    }

    static class TestObject {
        private String param1 = "value1";
        String param2 = "value2";

        public String getParam1() {
            return this.param1;
        }

        public void setParam1(final String param1) {
            this.param1 = param1;
        }

        public String getParam2() {
            return this.param2;
        }

        public void setParam2(final String param2) {
            this.param2 = param2;
        }
    }

}

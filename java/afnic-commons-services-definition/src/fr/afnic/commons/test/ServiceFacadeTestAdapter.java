/*
 * $Id: $
 * $Revision: $
 * $Author: $
 */

package fr.afnic.commons.test;

import java.util.List;

import junit.framework.JUnit4TestAdapter;
import junit.framework.JUnit4TestAdapterCache;
import junit.framework.Test;
import junit.framework.TestResult;

import org.junit.Ignore;
import org.junit.runner.Description;
import org.junit.runner.Runner;
import org.junit.runner.manipulation.Filter;
import org.junit.runner.manipulation.NoTestsRemainException;
import org.junit.runner.manipulation.Sorter;
import org.junit.runner.notification.RunNotifier;
import org.junit.runners.BlockJUnit4ClassRunner;
import org.junit.runners.model.FrameworkMethod;
import org.junit.runners.model.InitializationError;
import org.junit.runners.model.Statement;

/**
 * TODO A nettoyer
 * 
 * @author ginguene
 * 
 */
public class ServiceFacadeTestAdapter extends JUnit4TestAdapter {
    @Override
    public List<Test> getTests() {
        return super.getTests();
    }

    private final Class<?> fNewTestClass;

    private Runner fRunner;

    private final JUnit4TestAdapterCache fCache;

    public ServiceFacadeTestAdapter(Class<?> newTestClass, ServiceFacadeContractTest serviceFacadeContractTest) throws InitializationError {
        super(newTestClass);
        this.fCache = new JUnit4TestAdapterCacheDecorator(JUnit4TestAdapterCache.getDefault(), serviceFacadeContractTest);
        this.fNewTestClass = newTestClass;

        this.fRunner = new RunnerDecorator(newTestClass, serviceFacadeContractTest);

    }

    @Override
    public int countTestCases() {
        return this.fRunner.testCount();
    }

    @Override
    public void run(TestResult result) {
        this.fRunner.run(this.fCache.getNotifier(result, this));
    }

    // reflective interface for Eclipse
    @Override
    public Class<?> getTestClass() {
        return this.fNewTestClass;
    }

    @Override
    public Description getDescription() {
        Description description = this.fRunner.getDescription();
        return this.removeIgnored(description);
    }

    private Description removeIgnored(Description description) {
        if (this.isIgnored(description))
                return Description.EMPTY;
        Description result = description.childlessCopy();
        for (Description each : description.getChildren()) {
            Description child = this.removeIgnored(each);
            if (!child.isEmpty())
                    result.addChild(child);
        }
        return result;
    }

    private boolean isIgnored(Description description) {
        return description.getAnnotation(Ignore.class) != null;
    }

    @Override
    public String toString() {
        return this.fNewTestClass.getName();
    }

    @Override
    public void filter(Filter filter) throws NoTestsRemainException {
        filter.apply(this.fRunner);
    }

    @Override
    public void sort(Sorter sorter) {
        sorter.apply(this.fRunner);
    }

    static class JUnit4TestAdapterCacheDecorator extends JUnit4TestAdapterCache {

        private static final long serialVersionUID = 1L;

        private JUnit4TestAdapterCache cache;
        private transient ServiceFacadeContractTest serviceFacadeContractTest;

        public JUnit4TestAdapterCacheDecorator(JUnit4TestAdapterCache cache, ServiceFacadeContractTest serviceFacadeContractTest) {
            this.cache = cache;
            this.serviceFacadeContractTest = serviceFacadeContractTest;
        }

        @Override
        public List<Test> asTestList(Description description) {
            return this.cache.asTestList(description);
        }

        @Override
        public RunNotifier getNotifier(TestResult result, JUnit4TestAdapter adapter) {
            return this.cache.getNotifier(result, adapter);
        }

        @Override
        public Test asTest(Description description) {
            return new TestDecorator(this.cache.asTest(description), this.serviceFacadeContractTest);
        }

    }

    static class TestDecorator implements Test {

        private Test test;
        private ServiceFacadeContractTest serviceFacadeContractTest;

        public TestDecorator(Test test, ServiceFacadeContractTest serviceFacadeContractTest) {
            this.test = test;
            this.serviceFacadeContractTest = serviceFacadeContractTest;
        }

        @Override
        public int countTestCases() {
            return this.test.countTestCases();
        }

        @Override
        public void run(TestResult result) {
            this.serviceFacadeContractTest.createAndUseNewServiceFacade();
            this.test.run(result);
        }

    }

    static class RunnerDecorator extends BlockJUnit4ClassRunner {

        private ServiceFacadeContractTest serviceFacadeContractTest;

        public RunnerDecorator(Class<?> klass, ServiceFacadeContractTest serviceFacadeContractTest) throws InitializationError {
            super(klass);
            this.serviceFacadeContractTest = serviceFacadeContractTest;
        }

        @Override
        protected Statement methodInvoker(FrameworkMethod method, Object test) {
            try {
                this.serviceFacadeContractTest.createAndUseNewServiceFacade();
            } catch (Exception e) {
                e.printStackTrace();
            }
            return super.methodInvoker(method, test);
        }
    }

}

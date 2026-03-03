package core.metrics;

import org.testng.IAnnotationTransformer;
import org.testng.annotations.ITestAnnotation;

import java.lang.reflect.Constructor;
import java.lang.reflect.Method;

/**
 * TestNG transformer that disables tests which are in the
 * flaky quarantine list for the current environment.
 */
public class FlakyQuarantineTransformer implements IAnnotationTransformer {

    @Override
    @SuppressWarnings("rawtypes")
    public void transform(ITestAnnotation annotation,
                          Class testClass,
                          Constructor testConstructor,
                          Method testMethod) {

        if (testMethod == null) {
            return;
        }

        String testName = testMethod.getName();

        if (FlakyQuarantineManager.isQuarantined(testName)) {
            annotation.setEnabled(false);
        }
    }
}


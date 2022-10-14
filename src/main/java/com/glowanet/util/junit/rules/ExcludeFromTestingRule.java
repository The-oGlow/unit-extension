package com.glowanet.util.junit.rules;

import com.glowanet.annotation.ExcludeFromTesting;
import com.glowanet.util.reflect.ReflectionHelper;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.junit.internal.runners.statements.InvokeMethod;
import org.junit.rules.TestRule;
import org.junit.runner.Description;
import org.junit.runners.model.Statement;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

public class ExcludeFromTestingRule implements TestRule {

    private static final Logger LOGGER = LogManager.getLogger();

    @Override
    public Statement apply(Statement base, Description description) {
        return new ExcludableStatement(base, description);
    }

    private static class ExcludableStatement extends Statement {
        private       Object      instance;
        private final Statement   base;
        private final Description description;

        public ExcludableStatement(Statement base, Description description) {
            this.base = base;
            if (base instanceof InvokeMethod) {
                this.instance = ReflectionHelper.readField("target", base);
            }
            this.description = description;
        }

        @Override
        public void evaluate() throws Throwable {
            boolean shouldIgnore = false;
            ExcludeFromTesting annotation = description.getAnnotation(ExcludeFromTesting.class);
            if (annotation != null) {
                shouldIgnore = assumeResultTrue(annotation.assumeTrue());
            }
            if (shouldIgnore) {
                String testMethod = description.getClassName() + "#" + description.getMethodName() + "()";
                LOGGER.info("Test is excluded '{}'", testMethod);
            } else {
                base.evaluate();
            }
        }

        @SuppressWarnings("java:S1166")
        private boolean assumeResultTrue(String methodName) {
            Object result = true;
            if (methodName != null && !methodName.isEmpty()) {
                try {
                    String className = description.getClassName();
                    Class<?> clazz = Class.forName(className);
                    Method methodClazz = clazz.getMethod(methodName);
                    result = methodClazz.invoke(instance, (Object[]) null);
                } catch (ClassNotFoundException | NoSuchMethodException | IllegalAccessException | InvocationTargetException e) {
                    result = true;
                }
            }
            return (boolean) result;
        }
    }
}

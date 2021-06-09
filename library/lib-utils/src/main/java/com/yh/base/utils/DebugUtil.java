package com.yh.base.utils;

public class DebugUtil {
    public static void checkInMethod(Class cls, String methodName) throws Exception {
        while (cls != Object.class) {
            String className = cls.getName();
            StackTraceElement[] elements = Thread.currentThread().getStackTrace();
            for (StackTraceElement element : elements) {
                if (className.equals(element.getClassName()) && methodName.equals(element.getMethodName())) {
                    return;
                }
            }
            cls = cls.getSuperclass();
        }
        throw new Exception("must call in " + cls.getName() + "." + methodName);
    }
}

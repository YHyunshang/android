package com.yh.base.utils;

import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.lang.reflect.Modifier;
import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;

public class Reflector {

    public static Class getenericClass(Object obj, int idx) {
        Type type = obj.getClass().getGenericSuperclass();
        if (type instanceof ParameterizedType) {
            return (Class) ((ParameterizedType) type).getActualTypeArguments()[idx];
        }
        return null;
    }

    public static <T> T invoke(Object obj, String methodName, Class[] cls, Object... args) {
        try {
            Method method = getDeclaredMethod(obj.getClass(), methodName, cls);
            if (method != null) {
                method.setAccessible(true);
                return (T) method.invoke(obj, args);
            }
        } catch (IllegalAccessException | InvocationTargetException e) {
            e.printStackTrace();
        }
        return null;
    }

    public static <T> T invoke(Class classz, String methodName) {
        return (T) invoke(null, classz, methodName, new Class[0]);
    }

    public static <T> T invoke(Class classz, String methodName, Class[] cls, Object... args) {
        return (T) invoke(null, classz, methodName, cls, args);
    }

    public static <T> T invoke(Object obj, Class classz, String methodName) {
        return (T) invoke(obj, classz, methodName, new Class[0]);
    }

    public static <T> T invoke(Object obj, Class classz, String methodName, Class[] cls, Object... args) {
        try {
            Method method = classz.getDeclaredMethod(methodName, cls);
            method.setAccessible(true);
            return (T) method.invoke(obj, args);
        } catch (NoSuchMethodException e) {
            e.printStackTrace();
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        } catch (InvocationTargetException e) {
            e.printStackTrace();
            throw new RuntimeException(e);
        }
        return null;
    }

    /**
     *      * 直接设置对象属性值,无视private/protected修饰符,不经过setter函数.
     * <p>
     *     
     */
    public static void setFieldValue(final Object object, final String fieldName, final Object value) {
        Field field = getDeclaredField(object, fieldName);

        if (field == null)
            throw new IllegalArgumentException("Could not find field [" + fieldName + "] on target [" + object + "]");

        makeAccessible(field);

        try {
            field.set(object, value);
        } catch (IllegalAccessException e) {
        }
    }

    public static <T> T getFieldValue(final Object object, final String fieldName) {
        Field field = getDeclaredField(object, fieldName);

        if (field == null)
            throw new IllegalArgumentException("Could not find field [" + fieldName + "] on target [" + object + "]");

        makeAccessible(field);

        try {
            return (T) field.get(object);
        } catch (IllegalAccessException e) {
        }
        return null;
    }

    /**
     *      * 循环向上转型,获取对象的DeclaredField.
     * <p>
     *     
     */
    public static Field getDeclaredField(final Object object, final String fieldName) {
        return getDeclaredField(object.getClass(), fieldName);
    }

    /**
     *      * 循环向上转型,获取类的DeclaredField.
     * <p>
     *     
     */
    @SuppressWarnings("unchecked")
    public static Field getDeclaredField(final Class clazz, final String fieldName) {
        for (Class superClass = clazz; superClass != Object.class; superClass = superClass.getSuperclass()) {
            try {
                return superClass.getDeclaredField(fieldName);
            } catch (NoSuchFieldException e) {
                // Field不在当前类定义,继续向上转型
            }
        }

        return null;

    }

    public static Method getDeclaredMethod(final Class clazz, final String fieldName, Class<?>... parameterTypes) {
        for (Class superClass = clazz; superClass != Object.class; superClass = superClass.getSuperclass()) {
            try {
                return superClass.getDeclaredMethod(fieldName, parameterTypes);
            } catch (NoSuchMethodException e) {
                // Field不在当前类定义,继续向上转型
            }
        }

        return null;

    }


    /**
     *      * 强制转换fileld可访问.
     * <p>
     *     
     */

    protected static void makeAccessible(Field field) {
        if (!Modifier.isPublic(field.getModifiers()) || !Modifier.isPublic(field.getDeclaringClass().getModifiers())) {
            field.setAccessible(true);

        }
    }


    /**
     * 构造实例对象
     *
     * @param classz
     * @param arg
     * @param <T>
     * @return
     */
    public static <T> T createInstance(Class<T> classz, Object arg) {
        try {
            return (T) classz.getConstructor(arg.getClass()).newInstance(arg);
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        } catch (InstantiationException e) {
            e.printStackTrace();
        } catch (InvocationTargetException e) {
            e.printStackTrace();
        } catch (NoSuchMethodException e) {
            e.printStackTrace();
        }
        return null;
    }

    /**
     * 查找类上范型类
     *
     * @param obj
     * @param position
     * @param <VB>
     * @return
     */
    public static <VB> VB getVbClazz(Object obj, int position) {
        Type type = obj.getClass().getGenericSuperclass();
        if (type instanceof ParameterizedType) {
            Type argument = ((ParameterizedType) type).getActualTypeArguments()[position];
            return (VB) argument;
        }
        return null;
    }

    public static <VB> VB getSuperTypeEx(Object obj, int position, Class classType) {
        Class cls = obj.getClass().getSuperclass();
        Type type = obj.getClass().getGenericSuperclass();
        while (cls != classType && cls != Object.class) {
            type = cls.getGenericSuperclass();
            cls = cls.getSuperclass();
        }
//        Type type = cls.getGenericSuperclass();
        if (type instanceof ParameterizedType) {
            Type argument = ((ParameterizedType) type).getActualTypeArguments()[position];
            return (VB) argument;
        }
        return null;
    }

    /**
     * 查找类上范型类
     *
     * @param obj
     * @param position
     * @param <VB>
     * @return
     */
    public static <VB> VB getSuperType(Object obj, int position) {
        Type type = obj.getClass().getGenericSuperclass();
        if (type instanceof ParameterizedType) {
            Type argument = ((ParameterizedType) type).getActualTypeArguments()[position];
            return (VB) argument;
        }
        return null;
    }
}

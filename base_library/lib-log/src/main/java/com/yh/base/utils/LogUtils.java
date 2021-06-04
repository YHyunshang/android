package com.yh.base.utils;

import com.github.naturs.logger.Logger;
import com.github.naturs.logger.Printer;
import com.github.naturs.logger.android.adapter.AndroidLogAdapter;
import com.github.naturs.logger.android.strategy.converter.AndroidLogConverter;
import com.github.naturs.logger.android.strategy.log.LogcatLogStrategy;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.PrintStream;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

/**
 * Log工具，类似android.util.Log，不需定义Tag，直接打印内容，除自定义显著标识再定义
 * <p>
 * <p>
 * <p>
 * https://github.com/naturs/Logger
 * 支持直接打印 {JSON，Collection、Map、多维数组、Intent、Bundle}的格式化输出
 * 全局配置log输出
 * 个性化设置Tag
 * 准确显示调用方法、行，快速定位所在文件位置
 * 支持android系统对象Intent、Bundle打印
 * <p>
 * <p>
 * <p>
 * -------------------------------范例
 * ---输出字符串
 * LogUtils.d("12345");
 * <p>
 * ---输出参数
 * LogUtils.d("12%s3%d45", "a", 0);
 * <p>
 * ---Exception 日志打印，可加标识
 * LogUtils.e("ErrorLog", new NullPointerException("12345"));
 * <p>
 * ---obj---start
 * 输出异常
 * LogUtils.obj(new NullPointerException("456"));
 * <p>
 * 输出对象
 * CommentBean person = new CommentBean();
 * person.setPosition(11);
 * person.setParamType("pengwei");
 * LogUtils.obj(person);
 * <p>
 * 打印数据集合
 * List<CommentBean> list = new ArrayList<>();
 * for (int i = 0; i < 4; i++) {
 * CommentBean commentBean = new CommentBean();
 * commentBean.setCategory("Type=" + i);
 * commentBean.setCommentId(String.valueOf(i));
 * list.add(commentBean);
 * }
 * LogUtils.obj(list);
 * <p>
 * 打印数组
 * double[][] doubles = {{1.1, 1.6, 1.7, 30, 33},
 * {1.2, 1.6, 1.7, 30, 33},
 * {1.3, 1.6, 1.7, 30, 33},
 * {1.4, 1.6, 1.7, 30, 33}};
 * LogUtils.obj(doubles);
 * --- obj---end
 * <p>
 * ---输出json（json默认debug打印）
 * String json = "{\"name\":\"abc\",\"age\":18,\"other\":{\"other1\":\"otherValue1\",\"other2\":otherValue2}}";
 * LogUtils.json(json);
 * <p>
 * ---自定义tag
 * LogUtils.tagLog("我是自定义tag", "我是打印内容");
 * LogUtils.tag("--->").e("我是自定义tag打印的内容");
 */
public class LogUtils {

    private static final String TAG = "";

    private LogUtils() {
    }

    public static void initLogConfig() {
        boolean isLoggable = Config.isIsDebug();

        Logger.addLogAdapter(new AndroidLogAdapter(YHPrettyFormatStrategy
                .newBuilder()
                .tag(TAG)
                .logStrategy(new LogcatLogStrategy())
                .optimizeSingleLine(true)
                .showThreadInfo(false)
                .build()) {

            @Override
            public boolean isLoggable(int priority, String tag) {
                return isLoggable;
            }

        });
        
        Logger.setLogConverter(new AndroidLogConverter());
    }

    private static Printer printer() {
        return Logger.invokeClass(LogUtils.class);
    }

    public static Printer tag(String tag) {
        return printer().tag(tag);
    }

    public static void tagLog(String tag, String message) {
        tag(tag).d(message);
    }

    public static void v(String message, Object... args) {
        printer().wtf(message, args);
    }

    public static void d(String msg) {
        printer().d(msg);
    }

    public static void d(String message, Object... args) {
        printer().d(message, args);
    }

    public static void i(String message, Object... args) {
        printer().i(message, args);
    }

    public static void w(String message, Object... args) {
        printer().w(message, args);
    }

    public static void e(String message, Object... args) {
        printer().e(null, message, args);
    }

    public static void e(String message, Throwable throwable, Object... args) {
        printer().e(throwable, message, args);
    }

    public static void e(String message, Throwable throwable) {
        printer().e(throwable, message);
    }

    public static void obj(Object object) {
        printer().obj(object);
    }

    public static void obj(String message, Object object) {
        printer().obj(message, object);
    }

    public static void obj(int priority, String message, Object object) {
        printer().obj(priority, message, object);
    }

    public static void json(String json) {
        printer().json(json);
    }

    public static void json(String message, String json) {
        printer().json(message, json);
    }

    public static void xml(String xml) {
        printer().xml(xml);
    }

    public static void writeErrorLog(Throwable ex, String logDir) {
        String info = null;
        ByteArrayOutputStream baos = null;
        PrintStream printStream = null;
        try {
            baos = new ByteArrayOutputStream();
            printStream = new PrintStream(baos);
            ex.printStackTrace(printStream);
            byte[] data = baos.toByteArray();
            info = new String(data);
        } catch (Exception e) {
            e("Error Log", e);
        } finally {
            try {
                if (printStream != null) {
                    printStream.close();
                }
                if (baos != null) {
                    baos.close();
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        File dir = new File(logDir);
        if (!dir.exists()) {
            dir.mkdirs();
        }
        File file = new File(dir, getCurrentDateString() + ".txt");
        try {
            FileOutputStream fileOutputStream = new FileOutputStream(file, true);
            fileOutputStream.write(info.getBytes());
            fileOutputStream.close();
        } catch (FileNotFoundException e) {
            e("Error Log", e);
        } catch (IOException e) {
            e("Error Log", e);
        }
    }

    public static String getCurrentDateString() {
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd",
                Locale.getDefault());
        Date nowDate = new Date();
        return sdf.format(nowDate);
    }
}

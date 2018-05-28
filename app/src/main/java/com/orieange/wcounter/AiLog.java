package com.orieange.wcounter;

import android.os.Environment;
import android.util.Log;

import java.io.File;
import java.io.FileOutputStream;
import java.io.PrintWriter;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * 日志管理类
 * 用于优徒
 *
 * @author wzz
 */
public class AiLog {
    public final static String TAG_WZZ = "wzz---";
    public static boolean isOutToFile = false;//配置是否输出文件
    private static PrintWriter ms_logPrint = null;
    private static DateFormat fmt = new SimpleDateFormat("MM-dd hh:mm:ss.SSS");
    private static boolean isPrintLog = true;//是否打印日志

    public static final String CARDPATH = Environment.getExternalStorageDirectory().getAbsolutePath();
    public static final String LOGPATH = CARDPATH + "/utrip/log";
    public static final String LOGNAME = "sdk_log";

    public static String saveFile(String str, String filePath) {
        try {
            File file = new File(filePath);
            if (!file.exists()) {
                File dir = new File(file.getParent());
                dir.mkdirs();
                file.createNewFile();
            }
            FileOutputStream outStream = new FileOutputStream(file);
            outStream.write(str.getBytes());
            outStream.close();
        } catch (Exception e) {
            e.printStackTrace();
        }

        return filePath;
    }

    public static String timeTag() {
        long time = System.currentTimeMillis();//long now = android.os.SystemClock.uptimeMillis();
        SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd_HHmmss");
        Date d1 = new Date(time);
        String t1 = format.format(d1);
        return t1;
    }

    public static String timeTag(long time) {
        SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd_HHmmss");
        Date d1 = new Date(time);
        String t1 = format.format(d1);
        return t1;
    }

    public static void v(String tag, String msg) {
        d(tag, msg, 0);
    }

    public static void d(String tag, String msg) {
        d(tag, msg, 1);
    }

    public static void i(String tag, String msg) {
        d(tag, msg, 2);
    }

    public static void w(String tag, String msg) {
        d(tag, msg, 3);
    }

    public static void e(String tag, String msg) {
        d(tag, msg, 4);
    }

    private static void d(String tag, String msg, int options) {
        if (isOutToFile) {
            if (null != ms_logPrint) {
                ms_logPrint.println(fmt.format(new Date(System.currentTimeMillis())) + " " + tag + " " + msg + "\r");
                ms_logPrint.flush();
            }
        }
        if (isPrintLog) {
            if (options == 0) {
                Log.v(tag, msg);
            } else if (options == 1) {
                Log.d(tag, msg);
            } else if (options == 2) {
                Log.i(tag, msg);
            } else if (options == 3) {
                Log.w(tag, msg);
            } else if (options == 4) {
                Log.e(tag, msg);
            }
        }
    }

    public static int line(Exception e) {
        StackTraceElement[] trace = e.getStackTrace();
        if (trace == null || trace.length == 0)
            return -1; //
        return trace[0].getLineNumber();
    }

    public static String fun(Exception e) {
        StackTraceElement[] trace = e.getStackTrace();
        if (trace == null)
            return ""; //
        return trace[0].getMethodName();
    }

    public static String className(Exception e) {
        StackTraceElement[] trace = e.getStackTrace();
        if (trace == null)
            return ""; //
        return trace[0].getClassName();
    }

    public static String tag(Exception e) {
        return className(e) + "|" + fun(e) + "|" + line(e) + "|";
    }
}

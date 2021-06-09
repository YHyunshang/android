package com.yh.sdk.download;

import android.app.Activity;
import android.content.Context;
import android.net.ConnectivityManager;
import android.os.Build;
import android.os.Environment;
import android.os.StatFs;
import android.os.SystemClock;
import android.text.TextUtils;
import android.util.Log;

import java.io.ByteArrayOutputStream;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.io.RandomAccessFile;
import java.io.UnsupportedEncodingException;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLEncoder;
import java.security.MessageDigest;
import java.util.Map;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

/**
 * Created by zhangzhiyuan on 2017/5/12.
 */

public class DownloadUtil {

    public static final String TAG = "Bridge_HttpUtil";

    public static String getUrlParam(Map<String, Object> params) throws UnsupportedEncodingException {
        StringBuilder sb = new StringBuilder();
        if (params != null) {
            for (Map.Entry<String, Object> param : params.entrySet()) {
                if (param.getValue() != null) {
                    sb.append(URLEncoder.encode(param.getKey(), "UTF-8"));
                    sb.append("=");
                    sb.append(URLEncoder.encode(param.getValue().toString(), "UTF-8"));
                    sb.append("&");
                }
            }
            if (sb.length() > 0)
                sb.setLength(sb.length() - 1);
        }
        return sb.toString();
    }

    public static boolean isNetworkConnected() {
        try {
            final ConnectivityManager connectivityManager = (ConnectivityManager) activity
                    .getSystemService(Context.CONNECTIVITY_SERVICE);
            if (connectivityManager.getActiveNetworkInfo().isConnected()) {
                return true;
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return false;
    }

    private static Activity activity;

    public static void init(Activity activity) {
        DownloadUtil.activity = activity;
    }

    private static ExecutorService executor = Executors.newFixedThreadPool(10);

    public static interface RequestListener {
        void onSuccess(byte[] responseData) throws Exception;

        void onFail(int errorCode) throws Exception;

        public static final int ERROR_TIMEOUT = -1;
        public static final int ERROR_NETWORK = -2;
        public static final int ERROR_FAIL = -3;
    }

    public static void requestGet(final String url, final Map<String, Object> params, final RequestListener listener) {
        executor.execute(new Runnable() {
            @Override
            public void run() {
                HttpURLConnection httpUrlConnection = null;
                InputStream in = null;
                ByteArrayOutputStream bw = null;
                try {
                    String urlStr = url;
                    String paramStr = getUrlParam(params);
                    if (!TextUtils.isEmpty(paramStr)) {
                        urlStr += "?" + paramStr;
                    }
                    httpUrlConnection = (HttpURLConnection) new URL(urlStr).openConnection();
                    httpUrlConnection.setConnectTimeout(10 * 1000);
                    httpUrlConnection.setReadTimeout(10 * 1000);
                    HttpConnectionWaitThread thread = new HttpConnectionWaitThread(httpUrlConnection);
                    thread.start();
                    thread.join(20 * 1000);
                    int retCode = thread.getResponseCode();
                    if (HttpURLConnection.HTTP_GATEWAY_TIMEOUT == retCode) {
                        throw new java.net.SocketTimeoutException("bad code " + retCode);
                    }
                    if (retCode >= HttpURLConnection.HTTP_BAD_REQUEST) {
                        throw new Exception("bad code " + retCode);
                    }

                    in = httpUrlConnection.getInputStream();
                    bw = new ByteArrayOutputStream();
                    long total = 0;
                    byte[] data = new byte[1024 * 1024];
                    while (true) {
                        int len = in.read(data);
                        if (len > 0) {
                            bw.write(data, 0, len);
                            total += len;
                        } else if (len < 0) {
                            break;
                        }
                    }
                    try {
                        listener.onSuccess(bw.toByteArray());
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                } catch (java.net.ConnectException e) {
                    e.printStackTrace();
                    try {
                        listener.onFail(RequestListener.ERROR_NETWORK);
                    } catch (Exception ex) {
                        e.printStackTrace();
                    }
                } catch (java.net.UnknownHostException e) {
                    e.printStackTrace();
                    try {
                        listener.onFail(RequestListener.ERROR_NETWORK);
                    } catch (Exception ex) {
                        e.printStackTrace();
                    }
                } catch (java.net.SocketTimeoutException e) {
                    e.printStackTrace();
                    try {
                        if (isNetworkConnected()) {
                            listener.onFail(RequestListener.ERROR_TIMEOUT);
                        } else {
                            listener.onFail(RequestListener.ERROR_NETWORK);
                        }
                    } catch (Exception ex) {
                        e.printStackTrace();
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                    try {
                        if (isNetworkConnected()) {
                            listener.onFail(RequestListener.ERROR_FAIL);
                        } else {
                            listener.onFail(RequestListener.ERROR_NETWORK);
                        }
                    } catch (Exception ex) {
                        e.printStackTrace();
                    }
                } finally {
                    try {
                        httpUrlConnection.disconnect();
                    } catch (Throwable e) {
                    }
                    try {
                        in.close();
                    } catch (Throwable e) {
                    }
                    try {
                        bw.close();
                    } catch (Throwable e) {
                    }
                }
            }
        });
    }

    public static interface DownloadListener {
        void onSuccess() throws Exception;

        void onDownloading(long curBytes, long totalBytes) throws Exception;

        void onFail(int errorCode) throws Exception;

        public static final int ERROR_TIMEOUT = -1;
        public static final int ERROR_NETWORK = -2;
        public static final int ERROR_CREATE_FILE = -3;
        public static final int ERROR_SPACE = -4;
        public static final int ERROR_SIZE = -5;
        public static final int ERROR_MD5 = -6;
        public static final int ERROR_FAIL = -7;
    }

    public static class FileDlEntry {
        public long curBytes = 0;
        public long fileSize = 0;
        public String fileMD5;
        private String path;

        public void setPath(String path) {
            this.path = path;
        }

        public void save() {
            try {
                DataOutputStream ds = new DataOutputStream(new FileOutputStream(path));
                try {
                    ds.writeLong(curBytes);
                    ds.writeLong(fileSize);
                    if (fileMD5 != null)
                        ds.writeUTF(fileMD5);
                } catch (Exception e) {
                    //e.printStackTrace();
                }
                ds.close();
            } catch (Exception e) {
                //e.printStackTrace();
            }
        }

        public void load() {
            try {
                DataInputStream ds = new DataInputStream(new FileInputStream(path));
                try {
                    curBytes = ds.readLong();
                    fileSize = ds.readLong();
                    fileMD5 = ds.readUTF();
                } catch (Exception e) {
                    //e.printStackTrace();
                }
                ds.close();
            } catch (Exception e) {
                //e.printStackTrace();
            }
        }
    }

    public static long getAvailableSize(String path) {
        if (Environment.MEDIA_MOUNTED.equals(Environment.getExternalStorageState())) {
            StatFs fileStats = new StatFs(path);
            fileStats.restat(path);
            if (Build.VERSION.SDK_INT < Build.VERSION_CODES.JELLY_BEAN_MR2)
                return (long) fileStats.getAvailableBlocks() * (long) fileStats.getBlockSize();
            else
                return fileStats.getAvailableBlocksLong() * fileStats.getBlockSizeLong();
        }
        return -1;
    }

    private static class HttpConnectionWaitThread extends Thread {
        private HttpURLConnection conn;
        private int responseCode = HttpURLConnection.HTTP_GATEWAY_TIMEOUT;
        private Exception exception;

        public HttpConnectionWaitThread(HttpURLConnection conn) {
            this.conn = conn;
        }

        public void run() {
            try {
                conn.connect();
                set(conn.getResponseCode(), null);
            } catch (Exception e) {
                e.printStackTrace();
                set(0, e);
            }
        }

        private synchronized void set(int responseCode, Exception exception) {
            this.responseCode = responseCode;
            this.exception = exception;
        }

        public synchronized int getResponseCode() throws Exception {
            if (exception != null)
                throw exception;
            return this.responseCode;
        }
    }

    public static void requestDownload(final String url, final String filePath, final Long fileSize,
                                       final String fileMD5, final Map<String, Object> params, final long retryMax,
                                       final DownloadListener listener) {
        executor.execute(new Runnable() {
            @Override
            public void run() {
                HttpURLConnection httpUrlConnection = null;
                InputStream in = null;
                RandomAccessFile rs = null;
                try {
                    File downloadFile = new File(filePath);
                    File downloadDir = downloadFile.getParentFile();
                    FileDlEntry entry = new FileDlEntry();
                    entry.setPath(downloadDir.getAbsolutePath() + "/." + downloadFile.getName());
                    entry.load();
                    entry.fileMD5 = fileMD5;
                    int retryCnt = 0;
                    try {
                        if (!downloadFile.exists()) {
                            if (!downloadDir.exists())
                                downloadDir.mkdirs();
                            downloadFile.createNewFile();
                            entry.curBytes = 0;
                        }
                        long availableSize = getAvailableSize(downloadDir.getAbsolutePath().toString());
                        if (fileSize != null && availableSize != -1 && availableSize < fileSize) {
                            try {
                                listener.onFail(DownloadListener.ERROR_SPACE);
                            } catch (Exception ex) {
                                ex.printStackTrace();
                            }
                            return;
                        }
                        if (!TextUtils.isEmpty(fileMD5) && !TextUtils.isEmpty(entry.fileMD5)
                                && !fileMD5.equalsIgnoreCase(entry.fileMD5)
                                || fileSize != null && entry.fileSize != 0 && fileSize != entry.fileSize
                                || entry.fileSize > 0 && entry.curBytes > entry.fileSize) {
                            downloadFile.delete();
                            downloadFile.createNewFile();
                            entry.curBytes = 0;
                        }
                        if (entry.fileSize > 0 && entry.fileSize == entry.curBytes
                                && entry.fileSize == downloadFile.length()) {
                            if (fileMD5 != null && fileMD5.length() == 32
                                    && fileMD5.equalsIgnoreCase(getFileMD5String(filePath))) {
                                try {
                                    listener.onSuccess();
                                } catch (Exception e) {
                                    e.printStackTrace();
                                }
                                return;
                            }
                        }
                        if (entry.curBytes < 0)
                            entry.curBytes = 0;
                        rs = new RandomAccessFile(filePath, "rw");
                    } catch (Exception e) {
                        e.printStackTrace();
                        try {
                            listener.onFail(DownloadListener.ERROR_CREATE_FILE);
                        } catch (Exception ex) {
                            ex.printStackTrace();
                        }
                        return;
                    }
                    final int MAX_RETRY_GAP_TICK = 5000;
                    long lastRetryTime = -1;
                    while (true) {
                        try {
                            long curTime = SystemClock.elapsedRealtime();
                            long delayTime = curTime - lastRetryTime;
                            Log.v(TAG, "start:" + curTime + "-" + lastRetryTime + "=" + delayTime);
                            if (delayTime < MAX_RETRY_GAP_TICK) {
                                Thread.sleep(MAX_RETRY_GAP_TICK - (curTime - lastRetryTime));
                            }
                            lastRetryTime = curTime;
                            String urlStr = url;
                            String paramStr = getUrlParam(params);
                            if (!TextUtils.isEmpty(paramStr)) {
                                urlStr += "?" + paramStr;
                            }
                            try {
                                httpUrlConnection = (HttpURLConnection) new URL(urlStr).openConnection();
                                httpUrlConnection.setConnectTimeout(10 * 1000);
                                httpUrlConnection.setReadTimeout(10 * 1000);
                                HttpConnectionWaitThread thread = new HttpConnectionWaitThread(httpUrlConnection);
                                thread.start();
                                thread.join(20 * 1000);
                                int retCode = thread.getResponseCode();
                                if (HttpURLConnection.HTTP_GATEWAY_TIMEOUT == retCode) {
                                    throw new java.net.SocketTimeoutException("bad code " + retCode);
                                }
                                if (retCode >= HttpURLConnection.HTTP_BAD_REQUEST) {
                                    throw new Exception("bad code " + retCode);
                                }
                                long totalBytes = httpUrlConnection.getContentLength();
                                if (totalBytes > 0) {
                                    if (fileSize != null) {
                                        if (totalBytes != fileSize) {
                                            try {
                                                listener.onFail(DownloadListener.ERROR_SIZE);
                                            } catch (Exception ex) {
                                                ex.printStackTrace();
                                            }
                                            return;
                                        }
                                    }
                                    entry.fileSize = totalBytes;
                                } else {
                                    if (fileSize != null)
                                        entry.fileSize = fileSize;
                                }
                            } finally {
                                httpUrlConnection.disconnect();
                            }
                            if (entry.fileSize > 0 && entry.curBytes >= entry.fileSize)
                                break;
                            httpUrlConnection = (HttpURLConnection) new URL(urlStr).openConnection();
                            httpUrlConnection.setConnectTimeout(10 * 1000);
                            httpUrlConnection.setReadTimeout(10 * 1000);
                            httpUrlConnection.setRequestProperty("Range", "bytes=" + entry.curBytes + "-");

                            HttpConnectionWaitThread thread = new HttpConnectionWaitThread(httpUrlConnection);
                            thread.start();
                            thread.join(20 * 1000);
                            int retCode = thread.getResponseCode();
                            if (HttpURLConnection.HTTP_GATEWAY_TIMEOUT == retCode) {
                                throw new java.net.SocketTimeoutException("bad code " + retCode);
                            }
                            if (retCode >= HttpURLConnection.HTTP_BAD_REQUEST) {
                                throw new Exception("bad code " + retCode);
                            }

                            in = httpUrlConnection.getInputStream();

                            long downloadBytes = 0;
                            long totalBytes = httpUrlConnection.getContentLength();
                            retryCnt = 0;
                            lastRetryTime = SystemClock.elapsedRealtime();
                            int bufSize = 64 * 1024 * 1024;
                            if (entry.fileSize > 0) {
                                try {
                                    rs.setLength(entry.fileSize);
                                } catch (Exception e) {
                                    e.printStackTrace();
                                    try {
                                        listener.onFail(DownloadListener.ERROR_SPACE);
                                    } catch (Exception ex) {
                                        ex.printStackTrace();
                                    }
                                    return;
                                }
                                int percent = (int) (entry.fileSize / 512);
                                if (percent > 0) {
                                    bufSize = percent;
                                }
                            } else {
                                rs.setLength(entry.curBytes);
                            }
                            rs.seek(entry.curBytes);

                            byte[] data = new byte[bufSize];
                            while (totalBytes <= 0 || downloadBytes < totalBytes) {
                                int len = in.read(data);
                                if (len > 0) {
                                    try {
                                        rs.write(data, 0, len);
                                    } catch (Exception e) {
                                        e.printStackTrace();
                                        try {
                                            listener.onFail(DownloadListener.ERROR_SPACE);
                                        } catch (Exception ex) {
                                            ex.printStackTrace();
                                        }
                                        return;
                                    }
                                    downloadBytes += len;
                                    entry.curBytes += len;
                                    try {
                                        listener.onDownloading(entry.curBytes, entry.fileSize);
                                    } catch (Exception ex) {
                                        ex.printStackTrace();
                                    }
                                    entry.save();
                                } else if (len < 0) {
                                    break;
                                }
                            }
                            if (downloadFile.length() > entry.curBytes)
                                rs.setLength(entry.curBytes);
                            break;
                        } catch (java.net.ConnectException e) {
                            e.printStackTrace();
                            if (++retryCnt > retryMax) {
                                try {
                                    listener.onFail(DownloadListener.ERROR_NETWORK);
                                } catch (Exception ex) {
                                    e.printStackTrace();
                                }
                                return;
                            }
                        } catch (java.net.UnknownHostException e) {
                            e.printStackTrace();
                            if (++retryCnt > retryMax) {
                                try {
                                    listener.onFail(DownloadListener.ERROR_NETWORK);
                                } catch (Exception ex) {
                                    e.printStackTrace();
                                }
                                return;
                            }
                        } catch (java.net.SocketTimeoutException e) {
                            e.printStackTrace();
                            if (++retryCnt > retryMax) {
                                try {
                                    if (isNetworkConnected()) {
                                        listener.onFail(DownloadListener.ERROR_TIMEOUT);
                                    } else {
                                        listener.onFail(DownloadListener.ERROR_NETWORK);
                                    }
                                } catch (Exception ex) {
                                    e.printStackTrace();
                                }
                                return;
                            }
                        } catch (Exception e) {
                            e.printStackTrace();
                            if (++retryCnt > retryMax) {
                                try {
                                    if (isNetworkConnected()) {
                                        listener.onFail(DownloadListener.ERROR_FAIL);
                                    } else {
                                        listener.onFail(DownloadListener.ERROR_NETWORK);
                                    }
                                } catch (Exception ex) {
                                    e.printStackTrace();
                                }
                                return;
                            }
                        } finally {
                            entry.save();
                            try {
                                httpUrlConnection.disconnect();
                            } catch (Throwable e) {
                            }
                            try {
                                in.close();
                            } catch (Throwable e) {
                            }
                        }
                    }
                    if (fileMD5 != null && fileMD5.length() == 32) {
                        if (!fileMD5.equalsIgnoreCase(getFileMD5String(filePath))) {
                            downloadFile.delete();
                            try {
                                listener.onFail(DownloadListener.ERROR_MD5);
                            } catch (Exception e) {
                                e.printStackTrace();
                            }
                            return;
                        }
                    }
                    try {
                        rs.close();
                    } catch (Throwable e) {
                    }
                    try {
                        listener.onSuccess();
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                } finally {
                    try {
                        rs.close();
                    } catch (Throwable e) {
                    }
                }
            }
        });
    }

    public static String getFileMD5String(String path) {
        InputStream strm = null;
        try {
            MessageDigest msgdigest = MessageDigest.getInstance("MD5");
            File file = new File(path);
            int totalBytes = (int) file.length();
            strm = new FileInputStream(file);
            byte[] buff = new byte[1024];
            int readBytes;
            while (totalBytes > 0) {
                readBytes = strm.read(buff, 0, totalBytes > 1024 ? 1024 : totalBytes);
                if (readBytes > 0) {
                    msgdigest.update(buff, 0, readBytes);
                    totalBytes -= readBytes;
                }
            }
            byte[] data = msgdigest.digest();
            StringBuilder msg = new StringBuilder();
            for (byte b : data) {
                msg.append(String.format("%02x", b));
            }
            return msg.toString();
        } catch (Exception e) {
            // TODO Auto-generated catch block
            // e.printStackTrace();
            return null;
        } finally {
            try {
                strm.close();
            } catch (Exception e) {
            }
        }
    }
}
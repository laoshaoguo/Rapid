package com.zycx.luban;

import android.text.TextUtils;
import android.util.Log;

import java.io.File;
import java.io.FileInputStream;
import java.util.ArrayList;
import java.util.List;

public enum Checker {
    SINGLE;

    private static List<String> format = new ArrayList<>();
    private static final String JPG = "jpg";
    private static final String JPEG = "jpeg";
    private static final String PNG = "png";
    private static final String WEBP = "524946";
    private static final String GIF = "gif";

    static {
        format.add(JPG);
        format.add(JPEG);
        format.add(PNG);
        format.add(WEBP);
        format.add(GIF);
    }

    @Deprecated
    public boolean isImage(String path) {
        if (TextUtils.isEmpty(path)) {
            return false;
        }

        String suffix = path.substring(path.lastIndexOf(".") + 1, path.length());
        return format.contains(suffix.toLowerCase());
    }

    public boolean isJPG(String path) {
        if (TextUtils.isEmpty(path)) {
            return false;
        }

        String suffix = path.substring(path.lastIndexOf("."), path.length()).toLowerCase();
        return suffix.contains(JPG) || suffix.contains(JPEG);
    }

    public boolean isWEBP(File source) {
        return WEBP.equals(getFileHeader(source));
    }

    public String extSuffix(String path) {
        if (TextUtils.isEmpty(path)) {
            return ".jpg";
        }

        return path.substring(path.lastIndexOf("."), path.length());
    }

    public boolean needCompress(int leastCompressSize, String path) {
        if (leastCompressSize > 0) {
            File source = new File(path);
            getFileHeader(source);
            Log.d("needCompress::", "" + source.length());
            return source.exists() && source.length() > (leastCompressSize << 10) || Checker.SINGLE.isWEBP(source);
        }
        return true;
    }

    private static String bytesToHexSting(byte[] src) {
        StringBuilder stringBuilder = new StringBuilder();
        if (null == src || src.length <= 0) {
            return null;
        }
        for (byte aSrc : src) {
            int v = aSrc & 0xFF;
            String hv = Integer.toHexString(v);
            if (hv.length() < 2) {
                stringBuilder.append(0);
            }
            stringBuilder.append(hv);
        }
        return stringBuilder.toString();
    }

    private static String getFileHeader(File file) {
        String res = null;
        FileInputStream fis = null;
        try {
            fis = new FileInputStream(file);
            //获取文件头的前六位
            byte[] b = new byte[3];
            fis.read(b, 0, b.length);
            String fileCode = bytesToHexSting(b);
            res = fileCode;
            Log.d("getFileHeader::", res);
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            try {
                fis.close();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        return res;
    }
}

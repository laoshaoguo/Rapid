package com.zhiyicx.common.utils;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.PixelFormat;
import android.graphics.Rect;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.support.annotation.FloatRange;
import android.text.TextUtils;
import android.util.Base64;
import android.view.View;
import android.widget.TextView;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.klinker.android.link_builder.Link;
import com.klinker.android.link_builder.LinkBuilder;
import com.zhiyicx.common.config.ConstantConfig;

import org.jetbrains.annotations.NotNull;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.OutputStream;
import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;
import java.net.URLEncoder;
import java.nio.charset.Charset;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Random;
import java.util.Set;

import okhttp3.MediaType;
import okhttp3.ResponseBody;
import okio.Buffer;
import okio.BufferedSource;
import retrofit2.Response;

/**
 * @Describe 转换相关工具类
 * @Author Jungle68
 * @Date 2016/12/15
 * @Contact 335891510@qq.com
 */

public class ConvertUtils {

    private ConvertUtils() {
        throw new UnsupportedOperationException("u can't instantiate me...");
    }

    /**
     * 16 进制
     */
    private static final char lexDigits[] = {'0', '1', '2', '3', '4', '5', '6', '7', '8', '9', 'a', 'b', 'c', 'd', 'e', 'f'};
    private static final char hexDigits[] = {'0', '1', '2', '3', '4', '5', '6', '7', '8', '9', 'A', 'B', 'C', 'D', 'E', 'F'};

    /**
     * 字符串转换，用于评论名字颜色与点击处理
     *
     * @param textView
     * @param links
     */
    public static void stringLinkConvert(TextView textView, List<Link> links) {
        if (links.isEmpty()) {
            return;
        }
        LinkBuilder.on(textView)
                .setFindOnlyFirstMatchesForAnyLink(true)
                .addLinks(links)
                .build();
    }

    public static void stringLinkConvert(TextView textView, List<Link> links, boolean onlyFirst) {
        if (links.isEmpty()) {
            return;
        }
        try {
            LinkBuilder.on(textView)
                    .setFindOnlyFirstMatchesForAnyLink(onlyFirst)
                    .addLinks(links)
                    .build();
        }catch (Exception e){
            e.printStackTrace();
        }
    }

    /**
     * 解析错误信息 des to see {@see https://slimkit.github.io/plus-docs/v2/#messages }
     *
     * @param response1 需要解析的数据，json str
     * @return 抛给用户的 message
     */
    public static String praseErrorMessage(String response1) {
        String message = "";

        Map<String, Object> errorMessageMap = new Gson().fromJson(response1,
                new TypeToken<Map<String, Object>>() {
                }.getType());
        for (Map.Entry<String, Object> entry : errorMessageMap.entrySet()) {

            if (entry.getValue() instanceof String) {
                if ("message".equals(entry.getKey())) {
                    message = (String) entry.getValue();
                }
            } else if (entry.getValue() instanceof Map) {
                if ("errors".equals(entry.getKey())) {
                    message = praseErrorMessage((new Gson().toJson(entry.getValue())));
                }
            } else if (entry.getValue() instanceof String[]) {
                try {
                    message = ((String[]) entry.getValue())[0];
                } catch (Exception ignored) {
                }
                break;
            } else if (entry.getValue() instanceof List) {
                try {
                    message = (String) ((List) entry.getValue()).get(0);
                } catch (Exception ignored) {
                }
                break;
            }
        }
        return message;

    }

    /**
     * 数字格式转换，超过 9999 用 “1万”，数字不展示超过五位数，超过9999则显示1万，超过11000则显示1.1万、1.2万，超过99999则显示10万、11万
     *
     * @param number
     * @return
     */
    public static String numberConvert(int number) {
        if (number > 9999) {
            if (number >= 100000) {
                return number / 10000 + "万";
            } else {
                return number / 10000 + "." + ((number - number / 10000 * 10000) / 1000) + "万";
            }
        }
        return String.valueOf(number);
    }

    /**
     * ⦁	消息的字数显示不超过99，超过99均显示99
     *
     * @param number
     * @return
     */
    public static String messageNumberConvert(int number) {
        if (number > 99) {
            return String.valueOf(99);
        }
        return String.valueOf(number);
    }

    /**
     * 获取网络返回数据体内容
     *
     * @param response 返回体
     * @return
     */
    public static String getResponseBodyString(Response response) throws IOException {
        ResponseBody responseBody = response.errorBody();
        BufferedSource source = responseBody.source();
        source.request(Long.MAX_VALUE); // Buffer the entire body.
        Buffer buffer = source.buffer();
        //获取content的压缩类型
        String encoding = response
                .headers()
                .get("Content-Encoding");
        Buffer clone = buffer.clone();
        return praseBodyString(responseBody, encoding, clone);
    }

    /**
     * 解析返回体数据内容
     *
     * @param responseBody 返回体
     * @param encoding     编码
     * @param clone        数据
     * @return
     */
    public static String praseBodyString(ResponseBody responseBody, String encoding, Buffer clone) {
        String bodyString;//解析response content
        if (encoding != null && encoding.equalsIgnoreCase("gzip")) {//content使用gzip压缩
            bodyString = ZipHelper.decompressForGzip(clone.readByteArray());//解压
        } else if (encoding != null && encoding.equalsIgnoreCase("zlib")) {//content使用zlib压缩
            bodyString = ZipHelper.decompressToStringForZlib(clone.readByteArray());//解压
        } else {//content没有被压缩
            Charset charset = Charset.forName("UTF-8");
            MediaType contentType = responseBody.contentType();
            if (contentType != null) {
                charset = contentType.charset(charset);
            }
            bodyString = clone.readString(charset);
        }
        return bodyString;
    }

    /**
     * 去除头部符号
     *
     * @param str
     * @param symbol
     * @return
     */
    public static String removeSymbolStartWith(String str, String symbol) {

        if (str.startsWith(symbol)) {
            str = removeSymbolStartWith(str.substring(1, str.length()), symbol);
        }
        return str;
    }

    /**
     * 去除尾部符号
     *
     * @param str
     * @param symbol
     * @return
     */
    public static String removeSymbolEndWith(String str, String symbol) {

        if (str.endsWith(symbol)) {
            str = removeSymbolEndWith(str.substring(0, str.length() - 1), symbol);
        }

        return str;
    }

    /**
     * list 去重
     *
     * @param list
     */
    public static void removeDuplicate(List list) {
        HashSet h = new HashSet(list);
        list.clear();
        list.addAll(h);
    }

    /**
     * 删除重复元素并保持顺序
     *
     * @param list
     */
    public static void removeDuplicateWithOrder(List list) {
        Set set = new HashSet();
        List newList = new ArrayList();
        for (Iterator iter = list.iterator(); iter.hasNext(); ) {
            Object element = iter.next();
            if (set.add(element)) {
                newList.add(element);
            }
        }
        list.clear();
        list.addAll(newList);
    }

    /**
     * byteArr 转 hexString
     * <p>例如：</p>
     * bytes2HexString(new byte[] { 0, (byte) 0xa8 }) returns 00A8
     *
     * @param bytes 字节数组
     * @return 16进制大写字符串
     */
    public static String bytes2HexString(byte[] bytes) {
        if (bytes == null) {
            return null;
        }
        int len = bytes.length;
        if (len <= 0) {
            return null;
        }
        char[] ret = new char[len << 1];
        for (int i = 0, j = 0; i < len; i++) {
            ret[j++] = lexDigits[bytes[i] >>> 4 & 0x0f];
            ret[j++] = lexDigits[bytes[i] & 0x0f];
        }
        return new String(ret);
    }

    /**
     * hexString 转 byteArr
     * <p>例如：</p>
     * hexString2Bytes("00A8") returns { 0, (byte) 0xA8 }
     *
     * @param hexString 十六进制字符串
     * @return 字节数组
     */
    public static byte[] hexString2Bytes(String hexString) {
        if (TextUtils.isEmpty(hexString)) {
            return null;
        }
        int len = hexString.length();
        if (len % 2 != 0) {
            hexString = "0" + hexString;
            len = len + 1;
        }
        char[] hexBytes = hexString.toUpperCase().toCharArray();
        byte[] ret = new byte[len >> 1];
        for (int i = 0; i < len; i += 2) {
            ret[i >> 1] = (byte) (hex2Dec(hexBytes[i]) << 4 | hex2Dec(hexBytes[i + 1]));
        }
        return ret;
    }

    /**
     * hexChar 转 int
     *
     * @param hexChar hex 单个字节
     * @return 0..15
     */
    private static int hex2Dec(char hexChar) {
        if (hexChar >= '0' && hexChar <= '9') {
            return hexChar - '0';
        } else if (hexChar >= 'A' && hexChar <= 'F') {
            return hexChar - 'A' + 10;
        } else {
            throw new IllegalArgumentException();
        }
    }

    /**
     * charArr 转 byteArr
     *
     * @param chars 字符数组
     * @return 字节数组
     */
    public static byte[] chars2Bytes(char[] chars) {
        if (chars == null || chars.length <= 0) {
            return null;
        }
        int len = chars.length;
        byte[] bytes = new byte[len];
        for (int i = 0; i < len; i++) {
            bytes[i] = (byte) (chars[i]);
        }
        return bytes;
    }

    /**
     * byteArr 转 charArr
     *
     * @param bytes 字节数组
     * @return 字符数组
     */
    public static char[] bytes2Chars(byte[] bytes) {
        if (bytes == null) {
            return null;
        }
        int len = bytes.length;
        if (len <= 0) {
            return null;
        }
        char[] chars = new char[len];
        for (int i = 0; i < len; i++) {
            chars[i] = (char) (bytes[i] & 0xff);
        }
        return chars;
    }

    /**
     * 以 unit 为单位的内存大小转字节数
     *
     * @param memorySize 大小
     * @param unit       单位类型
     *                   <ul>
     *                   <li>{@link ConstantConfig.MemoryUnit#BYTE}: 字节</li>
     *                   <li>{@link ConstantConfig.MemoryUnit#KB}  : 千字节</li>
     *                   <li>{@link ConstantConfig.MemoryUnit#MB}  : 兆</li>
     *                   <li>{@link ConstantConfig.MemoryUnit#GB}  : GB</li>
     *                   </ul>
     * @return 字节数
     */
    public static long memorySize2Byte(long memorySize, ConstantConfig.MemoryUnit unit) {
        if (memorySize < 0) {
            return -1;
        }
        switch (unit) {
            default:
            case BYTE:
                return memorySize;
            case KB:
                return memorySize * ConstantConfig.KB;
            case MB:
                return memorySize * ConstantConfig.MB;
            case GB:
                return memorySize * ConstantConfig.GB;
        }
    }

    /**
     * 字节数转以 unit 为单位的内存大小
     *
     * @param byteNum 字节数
     * @param unit    单位类型
     *                <ul>
     *                <li>{@link ConstantConfig.MemoryUnit#BYTE}: 字节</li>
     *                <li>{@link ConstantConfig.MemoryUnit#KB}  : 千字节</li>
     *                <li>{@link ConstantConfig.MemoryUnit#MB}  : 兆</li>
     *                <li>{@link ConstantConfig.MemoryUnit#GB}  : GB</li>
     *                </ul>
     * @return 以unit为单位的size
     */
    public static double byte2MemorySize(long byteNum, ConstantConfig.MemoryUnit unit) {
        if (byteNum < 0) {
            return -1;
        }
        switch (unit) {
            default:
            case BYTE:
                return (double) byteNum;
            case KB:
                return (double) byteNum / ConstantConfig.KB;
            case MB:
                return (double) byteNum / ConstantConfig.MB;
            case GB:
                return (double) byteNum / ConstantConfig.GB;
        }
    }

    /**
     * 字节数转合适内存大小
     * <p>保留3位小数</p>
     *
     * @param byteNum 字节数
     * @return 合适内存大小
     */
    @SuppressLint("DefaultLocale")
    public static String byte2FitMemorySize(long byteNum) {
        if (byteNum < 0) {
            return "shouldn't be less than zero!";
        } else if (byteNum < ConstantConfig.KB) {
            return String.format("%.1fB", byteNum + 0.05);
        } else if (byteNum < ConstantConfig.MB) {
            return String.format("%.1fKB", byteNum / ConstantConfig.KB + 0.05);
        } else if (byteNum < ConstantConfig.GB) {
            return String.format("%.1fMB", byteNum / ConstantConfig.MB + 0.05);
        } else {
            return String.format("%.1fGB", byteNum / ConstantConfig.GB + 0.05);
        }
    }

    /**
     * 字节数转合适内存大小，单位B K M G
     * <p>保留3位小数</p>
     *
     * @param byteNum 字节数
     * @return 合适内存大小
     */
    @SuppressLint("DefaultLocale")
    public static String byte2FitMemorySizeUnit(long byteNum) {
        if (byteNum < 0) {
            return "shouldn't be less than zero!";
        } else if (byteNum < ConstantConfig.KB) {
            return String.format("%.1fB", byteNum + 0.05);
        } else if (byteNum < ConstantConfig.MB) {
            return String.format("%.1fK", byteNum / ConstantConfig.KB + 0.05);
        } else if (byteNum < ConstantConfig.GB) {
            return String.format("%.1fM", byteNum / ConstantConfig.MB + 0.05);
        } else {
            return String.format("%.1fG", byteNum / ConstantConfig.GB + 0.05);
        }
    }

    /**
     * 以 unit 为单位的时间长度转毫秒时间戳
     *
     * @param timeSpan 毫秒时间戳
     * @param unit     单位类型
     *                 <ul>
     *                 <li>{@link ConstantConfig.TimeUnit#MSEC}: 毫秒</li>
     *                 <li>{@link ConstantConfig.TimeUnit#SEC }: 秒</li>
     *                 <li>{@link ConstantConfig.TimeUnit#MIN }: 分</li>
     *                 <li>{@link ConstantConfig.TimeUnit#HOUR}: 小时</li>
     *                 <li>{@link ConstantConfig.TimeUnit#DAY }: 天</li>
     *                 </ul>
     * @return 毫秒时间戳
     */
    public static long timeSpan2Millis(long timeSpan, ConstantConfig.TimeUnit unit) {
        switch (unit) {
            default:
            case MSEC:
                return timeSpan;
            case SEC:
                return timeSpan * ConstantConfig.SEC;
            case MIN:
                return timeSpan * ConstantConfig.MIN;
            case HOUR:
                return timeSpan * ConstantConfig.HOUR;
            case DAY:
                return timeSpan * ConstantConfig.DAY;
        }
    }

    /**
     * 毫秒时间戳转以 unit 为单位的时间长度
     *
     * @param millis 毫秒时间戳
     * @param unit   单位类型
     *               <ul>
     *               <li>{@link ConstantConfig.TimeUnit#MSEC}: 毫秒</li>
     *               <li>{@link ConstantConfig.TimeUnit#SEC }: 秒</li>
     *               <li>{@link ConstantConfig.TimeUnit#MIN }: 分</li>
     *               <li>{@link ConstantConfig.TimeUnit#HOUR}: 小时</li>
     *               <li>{@link ConstantConfig.TimeUnit#DAY }: 天</li>
     *               </ul>
     * @return 以 unit 为单位的时间长度
     */
    public static long millis2TimeSpan(long millis, ConstantConfig.TimeUnit unit) {
        switch (unit) {
            default:
            case MSEC:
                return millis;
            case SEC:
                return millis / ConstantConfig.SEC;
            case MIN:
                return millis / ConstantConfig.MIN;
            case HOUR:
                return millis / ConstantConfig.HOUR;
            case DAY:
                return millis / ConstantConfig.DAY;
        }
    }

    /**
     * 毫秒时间戳转合适时间长度
     *
     * @param millis    毫秒时间戳
     *                  <p>小于等于0，返回null</p>
     * @param precision 精度
     *                  <p>precision = 0，返回null</p>
     *                  <p>precision = 1，返回天</p>
     *                  <p>precision = 2，返回天和小时</p>
     *                  <p>precision = 3，返回天、小时和分钟</p>
     *                  <p>precision = 4，返回天、小时、分钟和秒</p>
     *                  <p>precision >= 5，返回天、小时、分钟、秒和毫秒</p>
     * @return 合适时间长度
     */
    @SuppressLint("DefaultLocale")
    public static String millis2FitTimeSpan(long millis, int precision) {
        if (millis <= 0 || precision <= 0) {
            return null;
        }
        StringBuilder sb = new StringBuilder();
        String[] units = {"天", "小时", "分钟", "秒", "毫秒"};
        int[] unitLen = {86400000, 3600000, 60000, 1000, 1};
        precision = Math.min(precision, 5);
        for (int i = 0; i < precision; i++) {
            if (millis >= unitLen[i]) {
                long mode = millis / unitLen[i];
                millis -= mode * unitLen[i];
                sb.append(mode).append(units[i]);
            }
        }
        return sb.toString();
    }

    /**
     * bytes 转 bits
     *
     * @param bytes 字节数组
     * @return bits
     */
    public static String bytes2Bits(byte[] bytes) {
        StringBuilder sb = new StringBuilder();
        for (byte aByte : bytes) {
            for (int j = 7; j >= 0; --j) {
                sb.append(((aByte >> j) & 0x01) == 0 ? '0' : '1');
            }
        }
        return sb.toString();
    }

    public static String getStringMD5(@NotNull String string) {
        if (TextUtils.isEmpty(string)) {
            return "";
        }
        MessageDigest md5 = null;
        try {
            md5 = MessageDigest.getInstance("MD5");
            byte[] bytes = md5.digest(string.getBytes());
            String result = "";
            for (byte b : bytes) {
                String temp = Integer.toHexString(b & 0xff);
                if (temp.length() == 1) {
                    temp = "0" + temp;
                }
                result += temp;
            }
            return result;
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        }
        return "";
    }

    /**
     * bits 转 bytes
     *
     * @param bits 二进制
     * @return bytes
     */
    public static byte[] bits2Bytes(String bits) {
        int lenMod = bits.length() % 8;
        int byteLen = bits.length() / 8;
        // 不是8的倍数前面补0
        if (lenMod != 0) {
            for (int i = lenMod; i < 8; i++) {
                bits = "0" + bits;
            }
            byteLen++;
        }
        byte[] bytes = new byte[byteLen];
        for (int i = 0; i < byteLen; ++i) {
            for (int j = 0; j < 8; ++j) {
                bytes[i] <<= 1;
                bytes[i] |= bits.charAt(i * 8 + j) - '0';
            }
        }
        return bytes;
    }

    /**
     * inputStream 转 outputStream
     *
     * @param is 输入流
     * @return outputStream子类
     */
    public static ByteArrayOutputStream input2OutputStream(InputStream is) {
        if (is == null) {
            return null;
        }
        try {
            ByteArrayOutputStream os = new ByteArrayOutputStream();
            byte[] b = new byte[ConstantConfig.KB];
            int len;
            while ((len = is.read(b, 0, ConstantConfig.KB)) != -1) {
                os.write(b, 0, len);
            }
            return os;
        } catch (IOException e) {
            e.printStackTrace();
            return null;
        } finally {
            CloseUtils.closeIO(is);
        }
    }

    /**
     * outputStream 转 inputStream
     *
     * @param out 输出流
     * @return inputStream子类
     */
    public ByteArrayInputStream output2InputStream(OutputStream out) {
        if (out == null) {
            return null;
        }
        return new ByteArrayInputStream(((ByteArrayOutputStream) out).toByteArray());
    }

    /**
     * inputStream 转 byteArr
     *
     * @param is 输入流
     * @return 字节数组
     */
    public static byte[] inputStream2Bytes(InputStream is) {
        if (is == null) {
            return null;
        }
        return input2OutputStream(is).toByteArray();
    }

    /**
     * byteArr 转 inputStream
     *
     * @param bytes 字节数组
     * @return 输入流
     */
    public static InputStream bytes2InputStream(byte[] bytes) {
        if (bytes == null || bytes.length <= 0) {
            return null;
        }
        return new ByteArrayInputStream(bytes);
    }

    /**
     * outputStream 转 byteArr
     *
     * @param out 输出流
     * @return 字节数组
     */
    public static byte[] outputStream2Bytes(OutputStream out) {
        if (out == null) {
            return null;
        }
        return ((ByteArrayOutputStream) out).toByteArray();
    }

    /**
     * outputStream 转 byteArr
     *
     * @param bytes 字节数组
     * @return 字节数组
     */
    public static OutputStream bytes2OutputStream(byte[] bytes) {
        if (bytes == null || bytes.length <= 0) {
            return null;
        }
        ByteArrayOutputStream os = null;
        try {
            os = new ByteArrayOutputStream();
            os.write(bytes);
            return os;
        } catch (IOException e) {
            e.printStackTrace();
            return null;
        } finally {
            CloseUtils.closeIO(os);
        }
    }

    /**
     * inputStream 转 string按编码
     *
     * @param is          输入流
     * @param charsetName 编码格式
     * @return 字符串
     */
    public static String inputStream2String(InputStream is, String charsetName) {
        if (is == null || TextUtils.isEmpty(charsetName)) {
            return null;
        }
        try {
            return new String(inputStream2Bytes(is), charsetName);
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
            return null;
        }
    }

    /**
     * string 转 inputStream按编码
     *
     * @param string      字符串
     * @param charsetName 编码格式
     * @return 输入流
     */
    public static InputStream string2InputStream(String string, String charsetName) {
        if (string == null || TextUtils.isEmpty(charsetName)) {
            return null;
        }
        try {
            return new ByteArrayInputStream(string.getBytes(charsetName));
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
            return null;
        }
    }

    /**
     * outputStream 转 string 按编码
     *
     * @param out         输出流
     * @param charsetName 编码格式
     * @return 字符串
     */
    public static String outputStream2String(OutputStream out, String charsetName) {
        if (out == null || TextUtils.isEmpty(charsetName)) {
            return null;
        }
        try {
            return new String(outputStream2Bytes(out), charsetName);
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
            return null;
        }
    }

    /**
     * string 转 outputStream 按编码
     *
     * @param string      字符串
     * @param charsetName 编码格式
     * @return 输入流
     */
    public static OutputStream string2OutputStream(String string, String charsetName) {
        if (string == null || TextUtils.isEmpty(charsetName)) {
            return null;
        }
        try {
            return bytes2OutputStream(string.getBytes(charsetName));
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
            return null;
        }
    }

    /**
     * bitmap 转 byteArr
     *
     * @param bitmap bitmap对象
     * @param format 格式
     * @return 字节数组
     */
    public static byte[] bitmap2Bytes(Bitmap bitmap, Bitmap.CompressFormat format) {
        if (bitmap == null) {
            return null;
        }
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        bitmap.compress(format, 100, baos);
        return baos.toByteArray();
    }

    /**
     * byteArr 转 bitmap
     *
     * @param bytes 字节数组
     * @return bitmap
     */
    public static Bitmap bytes2Bitmap(byte[] bytes) {
        return (bytes == null || bytes.length == 0) ? null : BitmapFactory.decodeByteArray(bytes, 0, bytes.length);
    }

    /**
     * drawable 转 bitmap
     *
     * @param drawable drawable对象
     * @return bitmap
     */
    public static Bitmap drawable2Bitmap(Drawable drawable) {
        Bitmap bitmap = null;
        try {
            bitmap = drawable == null ? null : ((BitmapDrawable) drawable).getBitmap();
        } catch (Exception e) {
            int w = drawable.getIntrinsicWidth();
            int h = drawable.getIntrinsicHeight();
            Bitmap.Config config =
                    drawable.getOpacity() != PixelFormat.OPAQUE ? Bitmap.Config.RGB_565
                            : Bitmap.Config.RGB_565;
            bitmap = Bitmap.createBitmap(w, h, config);
            Canvas canvas = new Canvas(bitmap);
            drawable.setBounds(0, 0, w, h);
            drawable.draw(canvas);
        }
        return bitmap;
    }

    /**
     * bitmap 转 drawable
     *
     * @param res    resources对象
     * @param bitmap bitmap对象
     * @return drawable
     */
    public static Drawable bitmap2Drawable(Resources res, Bitmap bitmap) {
        return bitmap == null ? null : new BitmapDrawable(res, bitmap);
    }

    public static Bitmap drawable2BitmapWithWhiteBg(Context context, Drawable drawable, int defaultRes) {
        // 建立对应 bitmap
        Bitmap bitmap;
        int w, h;
        Bitmap.Config config;
        Rect oldBounds;
        try {
            // 取 drawable 的长宽
            w = drawable.getIntrinsicWidth();
            h = drawable.getIntrinsicHeight();

            // 取 drawable 的颜色格式
            config = drawable.getOpacity() != PixelFormat.OPAQUE ? Bitmap.Config.ARGB_8888
                    : Bitmap.Config.RGB_565;
            bitmap = Bitmap.createBitmap(w, h, config);
        } catch (Exception e) {
            config = Bitmap.Config.RGB_565;
            bitmap = BitmapFactory.decodeResource(context.getResources(), defaultRes).copy(config, true);
            w = bitmap.getWidth();
            h = bitmap.getHeight();
            drawable = ConvertUtils.bitmap2Drawable(context.getResources(), bitmap);
        }
        // 建立对应 bitmap 的画布
        Canvas canvas = new Canvas(bitmap);
        canvas.drawColor(Color.WHITE);
        oldBounds = new Rect(drawable.getBounds());
        drawable.setBounds(0, 0, w, h);
        // 把 drawable 内容画到画布中
        drawable.draw(canvas);
        drawable.setBounds(oldBounds);
        return bitmap;
    }


    /**
     * 然后给写一张白色背景
     * 给图片画一张背景
     *
     * @param color
     * @param orginBitmap
     * @return
     */
    public static Bitmap drawBg4Bitmap(int color, Bitmap orginBitmap) {
        Paint paint = new Paint();
        paint.setColor(color);
        Bitmap bitmap = Bitmap.createBitmap(orginBitmap.getWidth(),
                orginBitmap.getHeight(), orginBitmap.getConfig());
        Canvas canvas = new Canvas(bitmap);
        canvas.drawRect(0, 0, orginBitmap.getWidth(), orginBitmap.getHeight(), paint);
        canvas.drawBitmap(orginBitmap, 0, 0, paint);
        return bitmap;
    }

    /**
     * drawable 转 byteArr
     *
     * @param drawable drawable对象
     * @param format   格式
     * @return 字节数组
     */
    public static byte[] drawable2Bytes(Drawable drawable, Bitmap.CompressFormat format) {
        return drawable == null ? null : bitmap2Bytes(drawable2Bitmap(drawable), format);
    }

    /**
     * byteArr 转 drawable
     *
     * @param res   resources对象
     * @param bytes 字节数组
     * @return drawable
     */
    public static Drawable bytes2Drawable(Resources res, byte[] bytes) {
        return res == null ? null : bitmap2Drawable(res, bytes2Bitmap(bytes));
    }

    /**
     * view 转 Bitmap
     *
     * @param view 视图
     * @return bitmap
     */
    public static Bitmap view2Bitmap(View view) {
        if (view == null) {
            return null;
        }
        Bitmap ret = Bitmap.createBitmap(view.getWidth(), view.getHeight(), Bitmap.Config.ARGB_8888);
        Canvas canvas = new Canvas(ret);
        Drawable bgDrawable = view.getBackground();
        if (bgDrawable != null) {
            bgDrawable.draw(canvas);
        } else {
            canvas.drawColor(Color.WHITE);
        }
        view.draw(canvas);
        return ret;
    }

    /**
     * dp 转 px
     *
     * @param dpValue dp值
     * @return px值
     */
    public static int dp2px(Context context, float dpValue) {
        final float scale = context.getResources().getDisplayMetrics().density;
        return (int) (dpValue * scale + 0.5f);
    }

    /**
     * px 转 dp
     *
     * @param pxValue px值
     * @return dp值
     */
    public static int px2dp(Context context, float pxValue) {
        final float scale = context.getResources().getDisplayMetrics().density;
        return (int) (pxValue / scale + 0.5f);
    }

    /**
     * sp 转 px
     *
     * @param spValue sp值
     * @return px值
     */
    public static int sp2px(Context context, float spValue) {
        final float fontScale = context.getResources().getDisplayMetrics().scaledDensity;
        return (int) (spValue * fontScale + 0.5f);
    }

    /**
     * px 转 sp
     *
     * @param pxValue px值
     * @return sp值
     */
    public static int px2sp(Context context, float pxValue) {
        final float fontScale = context.getResources().getDisplayMetrics().scaledDensity;
        return (int) (pxValue / fontScale + 0.5f);
    }

    public static <T> T base64Str2Object(String productBase64) {
        T device = null;
        if (productBase64 == null) {
            return null;
        }
        // 读取字节
        byte[] base64 = Base64.decode(productBase64.getBytes(), Base64.DEFAULT);

        // 封装到字节流
        ByteArrayInputStream bais = new ByteArrayInputStream(base64);
        try {
            // 再次封装
            ObjectInputStream bis = new ObjectInputStream(bais);
            // 读取对象
            device = (T) bis.readObject();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return device;
    }

    public static <T> String object2Base64Str(T object) {
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        try {   //Device为自定义类
            // 创建对象输出流，并封装字节流
            ObjectOutputStream oos = new ObjectOutputStream(baos);
            // 将对象写入字节流
            oos.writeObject(object);
            // 将字节流编码成base64的字符串
            return new String(Base64.encode(baos
                    .toByteArray(), Base64.DEFAULT));
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    public static String file2Base64Str(String path) {
        if (TextUtils.isEmpty(path)) {
            return null;
        }
        InputStream is = null;
        byte[] data = null;
        String result = null;
        try {
            is = new FileInputStream(path);
            //创建一个字符流大小的数组。
            data = new byte[is.available()];
            //写入数组
            is.read(data);
            //用默认的编码格式进行编码
            result = Base64.encodeToString(data, Base64.DEFAULT);
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            if (null != is) {
                try {
                    is.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }

        }
        return result;
    }

    public static byte[] file2Bytes(String path) {
        if (TextUtils.isEmpty(path)) {
            return null;
        }
        InputStream is = null;
        byte[] data = null;
        try {
            is = new FileInputStream(path);
            //创建一个字符流大小的数组。
            data = new byte[is.available()];
            //写入数组
            is.read(data);
            //用默认的编码格式进行编码
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            if (null != is) {
                try {
                    is.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }

        }
        return data;
    }

    public static <T> String object2JsonStr(T obj) {
        return new Gson().toJson(obj);
    }

    /**
     * 替换 emoji 长度 =1
     *
     * @param str
     * @return
     */
    public static int stringLenghtDealForEmoji(CharSequence str) {
        int emojiLenght = emojiStrLenght(str);
        return (str.length() - emojiLenght) + stringEmojiCount(str);
    }

    /**
     * @param str
     * @return 一个 emoji 占两个字，通过长度返回个数
     */
    public static int stringEmojiCount(CharSequence str) {
        int emojiLenght = emojiStrLenght(str);
        return emojiLenght / 2;
    }

    /**
     * @param str
     * @return 字符串中 emoji 字符长度
     */
    public static int emojiStrLenght(CharSequence str) {
        int emojiLenght = 0;
        int len = str.length();
        for (int i = 0; i < len; i++) {
            if (isEmojiCharacter(str.charAt(i))) {
                emojiLenght++;
            }
        }
        return emojiLenght;
    }

    /**
     * emoji 所占的长度
     *
     * @param emojiNUm emoji 个数
     * @return
     */
    public static int emojiStrLenght(int emojiNUm) {
        return 2 * emojiNUm;
    }

    private static boolean isEmojiCharacter(char codePoint) {
        return !(codePoint == 0x0 || codePoint == 0x9 || codePoint == 0xA || codePoint == 0xD || codePoint >= 0x20 && codePoint <= 0xD7FF ||
                codePoint >= 0xE000 && codePoint <= 0xFFFD);
    }

    /**
     * @param color 处理的颜色
     * @param i     处理的步长
     * @return 处理完后较深的颜色
     */
    public static int getDarkerColor(int color, @FloatRange(from = 0, to = 1) float i) {
        float redrate = 0.299f;
        float greenrate = 0.587f;
        float bluerate = 0.114f;

        int red = (color >> 16) & 0xFF;
        int green = (color >> 8) & 0xFF;
        int blue = color & 0xFF;

        return Color.rgb((int) Math.max(red - redrate * i * 0xFF, 0),
                (int) Math.max(green - greenrate * i * 0xFF, 0),
                (int) Math.max(blue - bluerate * i * 0xFF, 0));
    }

    public static String toBase64(Bitmap bitmap) {
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.PNG, 100, baos);
        byte[] bytes = baos.toByteArray();

        return Base64.encodeToString(bytes, Base64.NO_WRAP);
    }

    public static Bitmap toBitmap(Drawable drawable) {
        if (drawable instanceof BitmapDrawable) {
            return ((BitmapDrawable) drawable).getBitmap();
        }

        int width = drawable.getIntrinsicWidth();
        width = width > 0 ? width : 1;
        int height = drawable.getIntrinsicHeight();
        height = height > 0 ? height : 1;

        Bitmap bitmap = Bitmap.createBitmap(width, height, Bitmap.Config.ARGB_8888);
        Canvas canvas = new Canvas(bitmap);
        drawable.setBounds(0, 0, canvas.getWidth(), canvas.getHeight());
        drawable.draw(canvas);

        return bitmap;
    }

    public static Bitmap decodeResource(Context context, int resId) {
        return BitmapFactory.decodeResource(context.getResources(), resId);
    }

    public static long getCurrentTime() {
        return System.currentTimeMillis();
    }

    /**
     * 生成漂亮的颜色
     */
    public static int generateBeautifulColor() {
        Random random = new Random();
        //为了让生成的颜色不至于太黑或者太白，所以对3个颜色的值进行限定
        int red = random.nextInt(150) + 50;//50-200
        int green = random.nextInt(150) + 50;//50-200
        int blue = random.nextInt(150) + 50;//50-200
        return Color.rgb(red, green, blue);//使用r,g,b混合生成一种新的颜色
    }

    /**
     * 获得状态栏的高度
     */
    public static int getStatusHeight(Context context) {
        int statusHeight = -1;
        try {
            Class<?> clazz = Class.forName("com.android.internal.R$dimen");
            Object object = clazz.newInstance();
            int height = Integer.parseInt(clazz.getField("status_bar_height").get(object).toString());
            statusHeight = context.getResources().getDimensionPixelSize(height);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return statusHeight;
    }

    public static String converInt2HexColor(int color) {
        return "#" + Integer.toHexString(color).substring(2);
    }


    /**
     * 网页编码  RFC-3986
     *
     * @param original
     * @return null if fails
     */
    public static String urlencode(String original) {
        try {
            //return URLEncoder.encode(original, "utf-8");
            //fixed: to comply with RFC-3986
            return URLEncoder.encode(original, "utf-8").replace("+", "%20").replace("*", "%2A").replace("%7E", "~");
        } catch (UnsupportedEncodingException e) {
            //  Logger.e(e.toString());
        }
        return "";
    }

    /**
     * 网页解码  RFC-3986
     *
     * @param original
     * @return null if fails
     */
    public static String urldecode(String original) {
        try {
            //return URLEncoder.encode(original, "utf-8");
            //fixed: to comply with RFC-3986
            original = original.replace("%20", "+").replace("%2A", "*").replace("~", "%7E");
            return URLDecoder.decode(original, "utf-8");
        } catch (UnsupportedEncodingException e) {
            //  Logger.e(e.toString());
        }
        return "";
    }


}

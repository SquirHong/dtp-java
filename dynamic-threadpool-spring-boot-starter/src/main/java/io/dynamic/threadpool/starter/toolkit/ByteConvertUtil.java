package io.dynamic.threadpool.starter.toolkit;

/**
 * 字节转换工具类.
 */
public class ByteConvertUtil {

    /**
     * 字节转换.
     *
     * @param size
     * @return
     */
    public static String getPrintSize(long size) {
        long covertNum = 1024;
        if (size < covertNum) {
            return size + "B";
        } else {
            size = size / covertNum;
        }
        if (size < covertNum) {
            return size + "KB";
        } else {
            size = size / covertNum;
        }
        if (size < covertNum) {
            size = size * 100;
            return (size / 100) + "." + (size % 100) + "MB";
        } else {
            size = size * 100 / covertNum;
            return (size / 100) + "." + (size % 100) + "GB";
        }
    }

}

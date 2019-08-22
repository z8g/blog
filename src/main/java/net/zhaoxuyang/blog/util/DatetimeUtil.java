/*
     _                                                                _   
 ___| |__   __ _  _____  ___   _ _   _  __ _ _ __   __ _   _ __   ___| |_ 
|_  / '_ \ / _` |/ _ \ \/ / | | | | | |/ _` | '_ \ / _` | | '_ \ / _ \ __|
 / /| | | | (_| | (_) >  <| |_| | |_| | (_| | | | | (_| |_| | | |  __/ |_ 
/___|_| |_|\__,_|\___/_/\_\\__,_|\__, |\__,_|_| |_|\__, (_)_| |_|\___|\__|
                                 |___/             |___/                  
*/
package net.zhaoxuyang.blog.util;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * @名称 日期时间 - 工具类
 * @作者 zhaoxuyang
 * @版本 v1.0
 * @日期 2019-01-01
 */
public class DatetimeUtil {

    public static void main(String[] args){
        
        System.out.println(DatetimeUtil.formatFileName("123"));
        System.out.println(DatetimeUtil.formatFileName("123.txt"));
        System.out.println(DatetimeUtil.formatFileName("123.txt.abc"));
        System.out.println(DatetimeUtil.formatNow());
        System.out.println(DatetimeUtil.formatDateTime(new Date().getTime()));
        /*
            20190602203148
            20190602203148.txt
            20190602203148.abc
            2019-06-02 20:31:48
            2019-06-02 20:31:48
        */
    }

    private static final DateFormat SDF_GMT = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
    private static final DateFormat SDF_FILE_NAME = new SimpleDateFormat("yyyyMMddHHmmss");
    private static final DateFormat SDF_MONTH = new SimpleDateFormat("yyyy-MM");
    
    public static Date formatString(String yearMonth){
        try {
            return SDF_MONTH.parse(yearMonth);
        } catch (ParseException ex) {
            return new Date();
        }
    }   
    public static String formatDate(Date date){
        return SDF_MONTH.format(date);
    }
    /**
     * @描述 获取当前系统时间并格式化
     * @return yyyy-MM-dd HH:mm:ss 形式的当前系统时间
     */
    public static String formatNow() {
        return SDF_GMT.format(new Date());
    }

    public static String formatDateTime(long length) {
        return SDF_GMT.format(new Date(length));
    }

    /**
     * 格式化文件名：把一个文件名转换成【当前时间】＋【.扩展名】
     *
     * @param fileName　文件名
     * @return
     */
    public static String formatFileName(String fileName) {
        int lastIndex = fileName.lastIndexOf(".");
        String right = lastIndex < 0 ? "" : fileName.substring(lastIndex);
        String time = SDF_FILE_NAME.format(new Date());
        return String.format("%s%s", time, right);
    }

}

package com.easy.ms.service.base.utils;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.Duration;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.LinkedHashSet;
import java.util.Set;

import javax.xml.datatype.DatatypeConfigurationException;
import javax.xml.datatype.DatatypeFactory;
import javax.xml.datatype.XMLGregorianCalendar;

import org.apache.commons.lang3.time.DateFormatUtils;

import lombok.extern.slf4j.Slf4j;

@Slf4j
public class DateUtils extends org.apache.commons.lang3.time.DateUtils {

    private static String[] parsePatterns = { "yyyy-MM-dd", "yyyy-MM-dd HH:mm:ss", "yyyy-MM-dd'T'HH:mm:ss",
            "yyyy-MM-dd'T'HH:mm:ss.SSS", "yyyy-MM-dd HH:mm", "yyyy-MM-dd'T'HH:mm:ss", "yyyy/MM/dd",
            "yyyy/MM/dd HH:mm:ss", "yyyy/MM/dd HH:mm" };

    /**
     * 得到当前日期字符串 格式（yyyy-MM-dd）
     */
    public static String getDate() {
        return getDate("yyyy-MM-dd");
    }

    /**
     * 得到当前日期字符串 格式（yyyy-MM-dd） pattern可以为："yyyy-MM-dd" "HH:mm:ss" "E"
     */
    public static String getDate(String pattern) {
        return DateFormatUtils.format(new Date(), pattern);
    }

    /**
     * 得到日期字符串 默认格式（yyyy-MM-dd） pattern可以为："yyyy-MM-dd" "HH:mm:ss" "E"
     */
    public static String formatDate(Date date, Object... pattern) {
        String formatDate = null;
        if (pattern != null && pattern.length > 0) {
            formatDate = DateFormatUtils.format(date, pattern[0].toString());
        } else {
            formatDate = DateFormatUtils.format(date, "yyyy-MM-dd");
        }
        return formatDate;
    }
    
    /**
     * 得到日期字符串 默认格式（yyyy-MM-dd） pattern可以为："yyyy-MM-dd" "HH:mm:ss" "E"
     */
    public static String formatDate(XMLGregorianCalendar xmlDate, Object... pattern) {
        String formatDate = null;
        GregorianCalendar ca = xmlDate.toGregorianCalendar();  
        Date date = ca.getTime();  
        if (pattern != null && pattern.length > 0) {
            formatDate = DateFormatUtils.format(date, pattern[0].toString());
        } else {
            formatDate = DateFormatUtils.format(date, "yyyy-MM-dd");
        }
        return formatDate;
    }

    /**
     * 得到日期时间字符串，转换格式（yyyy-MM-dd HH:mm:ss）
     */
    public static String formatDateTime(Date date) {
        return formatDate(date, "yyyy-MM-dd HH:mm:ss");
    }

    /**
     * 得到日期字符串，转换格式（yyyy-MM-dd）
     */
    public static String formatDate(Date date) {
        return formatDate(date, "yyyy-MM-dd");
    }

    /**
     * 得到时间字符串，转换格式（HH:mm:ss）
     */
    public static String formatTime(Date date) {
        return formatDate(date, "HH:mm:ss");
    }

    /**
     * 得到当前时间字符串 格式（HH:mm:ss）
     */
    public static String getTime() {
        return formatDate(new Date(), "HH:mm:ss");
    }

    /**
     * 得到当前日期和时间字符串 格式（yyyy-MM-dd HH:mm:ss）
     */
    public static String getDateTime() {
        return formatDate(new Date(), "yyyy-MM-dd HH:mm:ss");
    }

    /**
     * 得到当前年份字符串 格式（yyyy）
     */
    public static String getYear(Date date) {
        return formatDate(date, "yyyy");
    }

    /**
     * 得到当前月份字符串 格式（MM）
     */
    public static String getMonth(Date date) {
        return formatDate(date, "MM");
    }

    /**
     * 得到当天字符串 格式（dd）
     */
    public static String getDay(Date date) {
        return formatDate(date, "dd");
    }

    /**
     * 得到当前星期字符串 格式（E）星期几
     */
    public static String getWeek() {
        return formatDate(new Date(), "E");
    }

    /**
     * 日期型字符串转化为日期 格式 { "yyyy-MM-dd", "yyyy-MM-dd HH:mm:ss", "yyyy-MM-dd HH:mm",
     * "yyyy/MM/dd", "yyyy/MM/dd HH:mm:ss", "yyyy/MM/dd HH:mm" }
     */
    public static Date parseDate(Object str) {
        if (str == null) {
            return null;
        }
        try {
            return parseDate(str.toString(), parsePatterns);
        } catch (ParseException e) {
            return null;
        }
    }

    /**
     * 获取过去的天数
     *
     * @param date
     *
     * @return
     */
    public static long pastDays(Date date) {
        long t = new Date().getTime() - date.getTime();
        return t / (24 * 60 * 60 * 1000);
    }

    public static Date getDateStart(Date date) {
        if (date == null) {
            return null;
        }
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        try {
            date = sdf.parse(formatDate(date, "yyyy-MM-dd") + " 00:00:00");
        } catch (ParseException e) {
            log.error(e.getMessage(), e);
        }
        return date;
    }

    public static Date getDateEnd(Date date) {
        if (date == null) {
            return null;
        }
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        try {
            date = sdf.parse(formatDate(date, "yyyy-MM-dd") + " 23:59:59");
        } catch (ParseException e) {
            log.error(e.getMessage(), e);
        }
        return date;
    }

    /**
     * 判断字符串是否是日期
     *
     * @param timeString
     *
     * @return
     */
    public static boolean isDate(String timeString) {
        SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd");
        format.setLenient(false);
        try {
            format.parse(timeString);
        } catch (Exception e) {
            return false;
        }
        return true;
    }

    /**
     * 格式化时间
     *
     * @param timestamp
     *
     * @return
     */
    public static String dateFormat(Date timestamp) {
        SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        return format.format(timestamp);
    }

    public static String getDuration(Date startDate, Date endDate) {
        long diff = endDate.getTime() - startDate.getTime();
        long seconds = diff / 1000;
        Duration duration = Duration.ofSeconds(seconds);
        return duration.toString();
    }

    /**
     * 得到几天前的时间
     *
     * @param d
     * @param day
     *
     * @return
     */
    public static Date getDateBefore(Date d, int day) {
        Calendar now = Calendar.getInstance();
        now.setTime(d);
        now.set(Calendar.DATE, now.get(Calendar.DATE) - day);
        return now.getTime();
    }

    /**
     * 得到几天后的时间
     *
     * @param d
     * @param day
     *
     * @return
     */
    public static Date getDateAfter(Date d, int day) {
        Calendar now = Calendar.getInstance();
        now.setTime(d);
        now.set(Calendar.DATE, now.get(Calendar.DATE) + day);
        return now.getTime();
    }

    /**
     * 得到今天之前几天的时间
     *
     * @param day
     *
     * @return
     */
    public static Date getNowBefore(int day) {
        return getDateBefore(new Date(), day);
    }

    /**
     * 得到今天之后几天的时间
     *
     * @param day
     *
     * @return
     */
    public static Date getNowAfter(int day) {
        return getDateAfter(new Date(), day);
    }

    /**
     * 加上指定天数
     *
     * @param num
     * @param date
     *
     * @return
     *
     * @exception ParseException
     */
    public static Date plusDay(int num, Date date) {
        Calendar c = Calendar.getInstance();
        c.setTime(date);
        c.add(Calendar.DATE, num);
        return c.getTime();
    }

    public static int convertToMinutes(String string) {
        javax.xml.datatype.Duration duration = convertDuration(string);
        int days = duration.getDays();
        int hours = duration.getHours();
        int minutes = duration.getMinutes();
        return days * 24 * 60 + hours * 60 + minutes;
    }

    public static int convertToSeconds(String string) {
        javax.xml.datatype.Duration duration = convertDuration(string);
        int days = duration.getDays();
        int hours = duration.getHours();
        int minutes = duration.getMinutes();
        int seconds = duration.getSeconds();
        return days * 24 * 60 * 60 + hours * 60 * 60 + minutes * 60 + seconds;
    }

    public static javax.xml.datatype.Duration convertDuration(String duration) {
        try {
            return DatatypeFactory.newInstance().newDuration(duration);
        } catch (DatatypeConfigurationException e) {
            e.printStackTrace();
        }
        return null;
    }

    /**
     * @param date
     * @param filed
     * @param amount
     *
     * @return
     */
    public static Date add(Date date, int filed, int amount) {
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(date);
        calendar.add(filed, amount);
        return calendar.getTime();
    }

    public static Set<String> monthDay(int monthes) {
        Set<String> month = new LinkedHashSet<String>();
        Date today = new Date();
        Date limit = add(today, Calendar.MONTH, monthes);
        /*
         * for (int i = 0; i < 30; i++) { month.add(formatDate(add(today,
         * Calendar.DAY_OF_MONTH, i))); }
         */
        while (today.before(limit)) {
            today = add(today, Calendar.DAY_OF_MONTH, 1);
            month.add(formatDate(today));
        }
        return month;
    }

    public static java.util.Date getDateByMinuteOffset(java.util.Date date, int minuteOffset) {
        try {
            Calendar cal = Calendar.getInstance();
            cal.setTime(date);
            cal.add(Calendar.MINUTE, minuteOffset);
            SimpleDateFormat timeFormatter = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
            date = timeFormatter.parse(timeFormatter.format(cal.getTime()));
        } catch (ParseException ex) {
        }
        return date;
    }

}

package com.wch.libs.util;

import java.text.DateFormat;
import java.text.Format;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;

/**
 * 日期相关帮助类
 * 
 * @function
 * @author JiangCS
 * @version 1.0, 2014年12月16日 上午11:31:57
 */
public class DateUtil
{
    /** 年份 */
    public ArrayList<String> years = null;
    /** 月 */
    public ArrayList<String> months = null;
    /** 十二小时 */
    public ArrayList<String> hour12 = null;
    /** 分钟 */
    public ArrayList<String> minute = null;
    /** 二十四小时 */
    public ArrayList<String> hour24 = null;
    /** 单例 */
    public static DateUtil model;
    /** 每月的天数 */
    public static final int[] MONTH_DAY = { 31, 28, 31, 30, 31, 30, 31, 31, 30,
            31, 30, 31 };
    /** 十二小时制 */
    public static final int HOUR = 12;
    /** 二十四小时制 */
    public static final int HOUR_OF_DAY = 24;

    /**
     * 获取两个日期之间的所有日期
     * 
     * @param startDay
     * @param endDay
     * @return
     */
    public static ArrayList<String> getDaysBetweenDate(Calendar startDay, Calendar endDay)
    {
        if (startDay == null || endDay == null)
        {
            return null;
        }
        startDay.set(Calendar.HOUR, 0);
        startDay.set(Calendar.MINUTE, 0);
        startDay.set(Calendar.SECOND, 0);
        endDay.set(Calendar.HOUR, 0);
        endDay.set(Calendar.MINUTE, 0);
        endDay.set(Calendar.SECOND, 0);
        if (startDay.compareTo(endDay) > 0)
        {
            return null;
        }
        ArrayList<String> dateList = new ArrayList<String>();
        Calendar currentPrintDay = startDay;
        dateList.add(getFormateDate(currentPrintDay));
        while (true)
        {
            // 日期加一
            currentPrintDay.add(Calendar.DATE, 1);
            // 判断是否达到终了日
            if (currentPrintDay.compareTo(endDay) > 0)
            {
                break;
            }
            dateList.add(getFormateDate(currentPrintDay));
        }
        return dateList;
    }
    
    /**
     * 日期格式化 yy-MM-dd
     * 
     * @param year
     * @param month
     * @param day
     * @return
     */
    public static String getFormateDate(int year, int month, int day)
    {
        StringBuilder builder = new StringBuilder();
        builder.append(year);
        builder.append("-");
        builder.append(month < 10 ? "0" + month : month);
        builder.append("-");
        builder.append((day < 10) ? "0" + day : day);
        return builder.toString();
    }
    
    /**
     * 日期格式化 yy-MM-dd
     * 
     * @param calendar
     * @return
     */
    public static String getFormateDate(Calendar calendar)
    {
        StringBuilder builder = new StringBuilder();
        builder.append(calendar.get(Calendar.YEAR));
        builder.append("-");
        builder.append((calendar.get(Calendar.MONTH) + 1) < 10 ? "0" + (calendar.get(Calendar.MONTH) + 1)
            : (calendar.get(Calendar.MONTH) + 1));
        builder.append("-");
        builder.append((calendar.get(Calendar.DAY_OF_MONTH) < 10) ? "0" + calendar.get(Calendar.DAY_OF_MONTH)
            : calendar.get(Calendar.DAY_OF_MONTH));
        return builder.toString();
    }
    
    public static String formateDateToTimeStr(String time)
    {
        String timeOut = "2015年01月01日";
        if (time == null)
        {
            return timeOut;
        }
        SimpleDateFormat sdfIn = new SimpleDateFormat("yyyyMMddHHmmss");
        SimpleDateFormat sdfOut = new SimpleDateFormat("yyyy/MM/dd");
        try
        {
            Date date = sdfIn.parse(time);
            timeOut = sdfOut.format(date);
        }
        catch (ParseException e)
        {
            return timeOut;
        }
        return timeOut;
    }
    
    public static String formateDateToTime(String time)
    {
        String timeOut = "2015/01/01";
        if (time == null)
        {
            return timeOut;
        }
        SimpleDateFormat sdfIn = new SimpleDateFormat("yyyyMMdd");
        SimpleDateFormat sdfOut = new SimpleDateFormat("yyyy/MM/dd");
        try
        {
            Date date = sdfIn.parse(time);
            timeOut = sdfOut.format(date);
        }
        catch (ParseException e)
        {
            return timeOut;
        }
        return timeOut;
    }
    
    public static String getFormateDate(String timeIn)
    {
        String timeOut = "2015年01月01日 00:00:00";
        if (timeIn == null)
        {
            return timeOut;
        }
        SimpleDateFormat sdfIn = new SimpleDateFormat("yyyyMMddHHmmss");
        SimpleDateFormat sdfOut = new SimpleDateFormat("yyyy/MM/dd HH:mm:ss");
        try
        {
            Date date = sdfIn.parse(timeIn);
            timeOut = sdfOut.format(date);
        }
        catch (ParseException e)
        {
            e.printStackTrace();
        }
        return timeOut;
    }
    
    public static long getTimeInMillis(String time)
    {
        Calendar c = Calendar.getInstance();
        try
        {
            c.setTime(new SimpleDateFormat("yyyyMMddHHmmss").parse(time + "000000"));
        }
        catch (ParseException e)
        {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        return c.getTimeInMillis();
    }
    
    public static long formatStrtoLong(String time)
    {
        Calendar c = Calendar.getInstance();
        try
        {
            c.setTime(new SimpleDateFormat("yyyyMMddHHmmss").parse(time));
        }
        catch (ParseException e)
        {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        return c.getTimeInMillis();
    }
    
    public static long getcurrentTimeMillis()
    {
        Calendar cal = Calendar.getInstance();
        cal.set(Calendar.HOUR_OF_DAY, 0);
        cal.set(Calendar.SECOND, 0);
        cal.set(Calendar.MINUTE, 0);
        cal.set(Calendar.MILLISECOND, 0);
        // System.out.println(cal.getTimeInMillis());
        // SimpleDateFormat sdfOut = new
        // SimpleDateFormat("yyyy/MM/dd HH:mm:ss");
        // String timeOut = sdfOut.format(cal.getTimeInMillis());
        // System.out.println("timeOut=" + timeOut);
        return cal.getTimeInMillis();
    }
    
    public static boolean isBeyondMaxDay(String beginDate, String endDate, int day)
    {
        long millis = getTimeInMillis(endDate) - getTimeInMillis(beginDate);
        long countDay = millis / (3600 * 24 * 1000);
        if (countDay > day)
        {
            return true;
        }
        else
        {
            return false;
        }
    }
    
    public static String getCurrentTimeStr()
    {
        Format format = new SimpleDateFormat("yyyyMMddHHmmss");
        String timString = format.format(new Date());
        return timString;
    }
    
    // 把日期转为字符串
    public static String ConverToString(Date date, String format)
    {
        DateFormat df = new SimpleDateFormat(format);// format中填形如"yyyy/MM/dd HH:mm"
        if (date == null)
        {
            return "- -";
        }
        return df.format(date);
    }
    
    // 把字符串转为日期
    public static Date ConverToDate(String strDate, String format)
    {
        DateFormat df = new SimpleDateFormat(format);// format中填形如 yyyyMMddHHmmss
        Date date = null;
        try
        {
            date = df.parse(strDate);
        }
        catch (ParseException e)
        {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        return date;
    }

    /**
     * 获取单例
     *
     * @return
     */
    public static DateUtil getSingleton() {
        if (null == model) {
            model = new DateUtil();
        }
        return model;
    }

    /**
     * 获取所有的年份
     *
     * @return
     */
    public ArrayList<String> getYears() {
        if (years == null) {
            years = new ArrayList<String>();
            years.clear();
            for (int i = 1970; i < 2037; i++) {
                years.add(String.valueOf(i));
            }
        }
        return years;
    }

    /**
     * 获取所有的月份
     *
     * @return
     */
    public ArrayList<String> getMonths() {
        if (months == null) {
            months = new ArrayList<String>();
            months.clear();
            for (int i = 1; i < 13; i++) {
                if (i < 10) {
                    months.add("0" + String.valueOf(i));
                } else {
                    months.add(String.valueOf(i));
                }
            }
        }
        return months;
    }

    /**
     * 获取天数
     *
     * @param year
     * @param month
     * @return
     */
    public ArrayList<String> getDays(int year, int month) {
        int num = MONTH_DAY[month];
        if (isLeapYear(year) && month == 1)
            num += 1;
        ArrayList<String> days = new ArrayList<String>();
        for (int i = 1; i <= num; i++) {
            if (i < 10) {
                days.add("0" + String.valueOf(i));
            } else {
                days.add(String.valueOf(i));
            }
        }
        return days;
    }

    /**
     * 是否是闰年
     *
     * @param year
     * @return
     */
    public boolean isLeapYear(int year) {
        if ((year % 4 == 0 && year % 100 != 0) || year % 400 == 0)
            return true;
        return false;
    }

    /**
     * 返回小时
     *
     * @param TYPE
     *            HOUR 12小时，HOUR_OF_DAY 24小时
     * @return
     */
    public ArrayList<String> getHours(int TYPE) {
        if (TYPE == HOUR) {
            if (hour12 == null) {
                hour12 = new ArrayList<String>();
                for (int i = 1; i <= HOUR; i++) {
                    if (i < 10) {
                        hour12.add("0" + String.valueOf(i));
                    } else {
                        hour12.add(String.valueOf(i));
                    }
                }
            }
            return hour12;
        } else if (TYPE == HOUR_OF_DAY) {
            if (hour24 == null) {
                hour24 = new ArrayList<String>();
                for (int i = 0; i < HOUR_OF_DAY; i++) {
                    if (i < 10) {
                        hour24.add("0" + String.valueOf(i));
                    } else {
                        hour24.add(String.valueOf(i));
                    }
                }
            }
            return hour24;
        }
        return null;
    }

    /**
     * 返回分钟
     *
     * @return
     */
    public ArrayList<String> getMinute() {
        if (minute == null) {
            minute = new ArrayList<String>();
            for (int i = 0; i < 60; i++) {
                if (i < 10) {
                    minute.add("0" + String.valueOf(i));
                } else {
                    minute.add(String.valueOf(i));
                }
            }
        }
        return minute;
    }

}

// 
// Decompiled by Procyon v0.5.36
// 

package com.netease.hz.imp.trans.util;

import java.security.NoSuchAlgorithmException;
import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.text.ParseException;
import java.util.Calendar;
import java.text.SimpleDateFormat;
import java.util.Date;

public class DateUtil
{
    public static String currentDay() {
        final Date now = new Date();
        final SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
        final String day = dateFormat.format(now);
        return day;
    }
    
    public static String AddOneDay(final String businessDay) {
        try {
            final SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd");
            Date date = simpleDateFormat.parse(businessDay);
            final Calendar c = Calendar.getInstance();
            c.setTime(date);
            c.add(5, 1);
            date = c.getTime();
            return simpleDateFormat.format(date);
        }
        catch (ParseException e) {
            System.out.println("business day is error:" + businessDay);
            return null;
        }
    }
    
    public static Long getTime(final String day, final String hour) throws ParseException {
        final String date_str = String.format("%s %s", day, hour);
        final SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        final Date date = simpleDateFormat.parse(date_str);
        return date.getTime();
    }
    
    public static int betweenDays(final String endDateStr, final String startDateStr) {
        try {
            final SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd");
            final Date startDate = simpleDateFormat.parse(startDateStr);
            final Date endDate = simpleDateFormat.parse(endDateStr);
            final Calendar calendar = Calendar.getInstance();
            calendar.setTime(startDate);
            final long timeInMillis1 = calendar.getTimeInMillis();
            calendar.setTime(endDate);
            final long timeInMillis2 = calendar.getTimeInMillis();
            final int betweenDays = (int)((timeInMillis2 - timeInMillis1) / 86400000L);
            return betweenDays;
        }
        catch (ParseException e) {
            System.out.println(String.format("betweenDays day is error starDate: %s, endDate:%s", startDateStr, endDateStr));
            return 0;
        }
    }
    
    public static String addDay(final String dateStr, final int day) throws ParseException {
        final SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd");
        final Date startDate = simpleDateFormat.parse(dateStr);
        final Calendar calendar = Calendar.getInstance();
        calendar.setTime(startDate);
        calendar.add(5, day);
        final Date newDate = calendar.getTime();
        return simpleDateFormat.format(newDate);
    }
    
    public static String time2DateStr(final Long time) {
        final SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        return simpleDateFormat.format(new Date(time));
    }
    
    public static String time2DateStr(final Long time, final String format) {
        final SimpleDateFormat simpleDateFormat = new SimpleDateFormat(format);
        return simpleDateFormat.format(new Date(time));
    }
    
    public static String getMD5(final String dec) {
        try {
            final MessageDigest digest = MessageDigest.getInstance("MD5");
            digest.update(dec.getBytes(StandardCharsets.UTF_8));
            final byte[] messageDigest = digest.digest();
            final StringBuffer hexString = new StringBuffer();
            for (int i = 0; i < messageDigest.length; ++i) {
                final String shaHex = Integer.toHexString(messageDigest[i] & 0xFF);
                if (shaHex.length() < 2) {
                    hexString.append(0);
                }
                hexString.append(shaHex);
            }
            return hexString.toString().toUpperCase();
        }
        catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
            return "";
        }
    }
}

package com.xianglin.appserv.common.util;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.apache.commons.lang3.StringUtils;

/**
 * 日期操作
 *
 * @author bb-he
 * @data 2015-9-14
 */
public class DateUtils {

	/** yyyy-MM-dd */
	public static final String DATE_FMT = "yyyy-MM-dd";
	/** yyyy-MM-dd HH:mm:ss */
	public static final String DATETIME_FMT = "yyyy-MM-dd HH:mm:ss";
	/** yyyy-MM-dd HH:mm */
	public static final String DATETIME_NOSECOND = "yyyy-MM-dd HH:mm";
	/** HH:mm:ss */
	public static final String TIME_FMT = "HH:mm:ss";
	/** yyyyMMdd */
	public static final String DATE_TPT_TWO = "yyyyMMdd";
	/** yyyy-MM */
	public static final String DATE_TPT_THREE = "yyyy-MM";
	/** yyyyMM */
	public static final String DATE_TPT_FOUR = "yyyyMM";

	public static Date getStartOfDay(Date date) {

		Calendar c = Calendar.getInstance();
		c.setTime(date);
		c.set(Calendar.HOUR_OF_DAY, c.getActualMinimum(Calendar.HOUR_OF_DAY));
		c.set(Calendar.MINUTE, c.getActualMinimum(Calendar.MINUTE));
		c.set(Calendar.SECOND, c.getActualMinimum(Calendar.SECOND));
		return c.getTime();
	}

	public static Date getEndOfDay(Date date) {

		Calendar c = Calendar.getInstance();
		c.setTime(date);
		c.set(Calendar.HOUR_OF_DAY, c.getActualMaximum(Calendar.HOUR_OF_DAY));
		c.set(Calendar.MINUTE, c.getActualMaximum(Calendar.MINUTE));
		c.set(Calendar.SECOND, c.getActualMaximum(Calendar.SECOND));
		return c.getTime();
	}

	/**
	 * 获取当年的第一天
	 * 
	 * @return
	 */
	public static Date getFirstDayOfYear() {

		Calendar c = Calendar.getInstance();
		c.set(Calendar.DAY_OF_YEAR, 1);
		c.set(Calendar.HOUR_OF_DAY, 0);
		c.set(Calendar.MINUTE, 0);
		c.set(Calendar.SECOND, 0);
		return c.getTime();
	}

	/**
	 * 获取当前时间
	 * 
	 * @return
	 */
	public static Date getNow() {
		Calendar c = Calendar.getInstance();
		return c.getTime();
	}

	/**
	 * date转为String
	 * 
	 * @param date
	 * @return
	 */
	static public String getDateStr(Date date) {
		SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		return format.format(date);
	}

	/**
	 * date转为String 精确到分
	 * 
	 * @param date
	 * @return
	 */
	static public String getDateStr2(Date date) {
		SimpleDateFormat format = new SimpleDateFormat("yyyy/MM/dd HH:mm");
		return format.format(date);
	}

	/**
	 * 根据制定日期格式获取上个月的日期
	 * 
	 * @return
	 */
	public static String getLastMonthDate() {
		Calendar c = Calendar.getInstance();
		c.add(Calendar.MONTH, -1);
		SimpleDateFormat format = new SimpleDateFormat(DATE_TPT_THREE);
		String time = format.format(c.getTime());
		return time;
	}

	/**
	 * 讲当前日期返回，格式：yyyyMMddHHmmssS
	 *
	 * @author bb-he
	 * @return String 日期字符串
	 */
	public static String formatDateSeq() {
		SimpleDateFormat sdf = new SimpleDateFormat(DATETIME_FMT);
		return sdf.format(new Date());
	}

	/**
	 * 将日期转换成指定格式字符串
	 *
	 * @param date
	 * @param str
	 * @return
	 */
	public static String formatDate(Date date, String str) {
		SimpleDateFormat sdf = new SimpleDateFormat(str);
		return sdf.format(date);
	}

	/**
	 * 将日期字符串转换成指定格式的date
	 * 
	 * @param str
	 *            日期字符串
	 * @param formatStr
	 *            日期格式
	 * @return
	 */
	public static Date formatStr(String str, String formatStr) {

		Date date = null;
		if (str != null) {
			DateFormat sdf = new SimpleDateFormat(formatStr);
			try {
				date = sdf.parse(str);
			} catch (ParseException e) {
				e.printStackTrace();
			}
		}
		return date;
	}

	/**
	 * 得到本月的第一天，格式：yyyy-MM-dd hh:mm:ss
	 *
	 * @return
	 */
	public static String getMonthFirstDay() {

		Calendar calendar = Calendar.getInstance();
		calendar.set(Calendar.DAY_OF_MONTH, calendar.getActualMinimum(Calendar.DAY_OF_MONTH));
		return formatDate(calendar.getTime(), DATE_FMT);
	}

	/**
	 * 得到指定时间月份的第一天(0:0:0)
	 */
	public static Date getMonthFirstDay(Date date) {

		Calendar calendar = Calendar.getInstance();
		calendar.setTime(date);
		calendar.set(calendar.get(Calendar.YEAR), calendar.get(Calendar.MONTH), 1, 0, 0, 0);
		return calendar.getTime();

	}

	/**
	 * 得到下个月的第一天
	 */
	public static String getNextMonthFirstDay() {

		Calendar calendar = Calendar.getInstance();
		calendar.set(Calendar.DAY_OF_MONTH, calendar.getActualMinimum(Calendar.DAY_OF_MONTH));
		calendar.set(Calendar.MONTH, calendar.get(Calendar.MONTH) + 1);
		return formatDate(calendar.getTime(), DATE_FMT);
	}

	/**
	 * 得到本月的最后一天
	 *
	 * @return
	 */
	public static String getMonthLastDay() {

		Calendar calendar = Calendar.getInstance();
		calendar.set(Calendar.DAY_OF_MONTH, calendar.getActualMaximum(Calendar.DAY_OF_MONTH));
		return formatDate(calendar.getTime(), DATE_FMT);
	}

	/**
	 * 得到批定月的最后一天(23:59:59)
	 *
	 * @param date
	 * @return
	 */
	public static Date getMonthLastDay(Date date) {

		Calendar calendar = Calendar.getInstance();
		calendar.setTime(date);
		calendar.set(calendar.get(Calendar.YEAR), calendar.get(Calendar.MONTH) + 1, 1, 0, 0, 0);
		calendar.set(Calendar.SECOND, calendar.get(Calendar.SECOND) - 1);
		return calendar.getTime();
	}

	/**
	 * 得到本周的第一天
	 */
	public static String getWeek() {
		Calendar calendar = Calendar.getInstance();
		calendar.set(Calendar.DAY_OF_WEEK, 2);

		// 获取本周第一天的日期
		String weekBegin = formatDate(calendar.getTime(), DATE_FMT);
		// 获得当前日期的日期
		String nowDate = formatDate(Calendar.getInstance().getTime(), DATE_FMT);

		// 如果本周第一天日期大于当前日期，那么将日期减6天返回
		if (parse(DATE_FMT, weekBegin).getTime() > parse(DATE_FMT, nowDate).getTime()) {
			return getNDay(-6);
		}
		return formatDate(calendar.getTime(), DATE_FMT);
	}

	/**
	 * 得到n个星期前
	 *
	 * @param n
	 * @return
	 */
	public static String getLastestWeek(int n) {
		Calendar calendar = Calendar.getInstance();
		calendar.set(Calendar.WEEK_OF_YEAR, calendar.get(Calendar.WEEK_OF_YEAR) - n);
		return formatDate(calendar.getTime(), DATE_FMT);
	}

	/**
	 * 得到n个月前
	 *
	 * @param n
	 * @return
	 */
	public static String getLastestMonth(int n) {
		Calendar calendar = Calendar.getInstance();
		calendar.set(Calendar.MONDAY, calendar.get(Calendar.MONTH) - n);
		return formatDate(calendar.getTime(), DATE_FMT);
	}

	/**
	 * 根据制定日期获取当天时间
	 *
	 * @return
	 */
	public static String getDay(String formatStr) {
		Date date = new Date();
		return DateUtils.formatDate(date, formatStr);
	}

	/**
	 * 获取第二天时间
	 *
	 * @return
	 */
	public static String getTomorrow() {
		Calendar now = Calendar.getInstance();
		now.set(Calendar.DAY_OF_YEAR, now.get(Calendar.DAY_OF_YEAR) + 1);
		return DateUtils.formatDate(now.getTime(), DATE_FMT);
	}

	/**
	 * 获取N天前后的时间
	 * 
	 * @param n
	 * @return
	 */
	public static String getNDay(int n) {
		Calendar now = Calendar.getInstance();
		now.set(Calendar.DAY_OF_YEAR, now.get(Calendar.DAY_OF_YEAR) + n);
		return DateUtils.formatDate(now.getTime(), DATE_FMT);
	}

	/**
	 * 获取n个月以后的时间字符串
	 *
	 * @param day
	 * @param n
	 * @return
	 */
	public static Date getDateAfterSeveralMonth(Date day, int n) {
		Calendar c = Calendar.getInstance();
		c.setTime(day);
		c.set(Calendar.YEAR, c.get(Calendar.YEAR) + n);
		return c.getTime();
	}

	/**
	 * 获取n个 月以后的时间(毫秒)
	 *
	 * @param day
	 * @param n
	 * @return
	 */
	public static long getDateTimeAfterSeveralMonth(Date day, int n) {

		Calendar c = Calendar.getInstance();
		c.setTime(day);
		c.set(Calendar.YEAR, c.get(Calendar.YEAR) + n);
		return c.getTimeInMillis();
	}

	/**
	 * 时间比较返回天数
	 *
	 * @param fDate
	 * @param oDate
	 * @return
	 */
	public static int getIntervalDays(Date fDate, Date oDate) {

		if ((null == fDate) || (null == oDate)) {

			return -1;
		}
		long intervalMilli = oDate.getTime() - fDate.getTime();
		return (int) (intervalMilli / (24 * 60 * 60 * 1000));
	}

	public static int getIntervalMinutes(Date fDate, Date oDate) {

		if ((null == fDate) || (null == oDate)) {
			return -1;
		}
		long intervalMilli = oDate.getTime() - fDate.getTime();
		return (int) (intervalMilli / (1000 * 60));

	}

	public static int getIntervalSeconds(Date fDate, Date oDate) {

		if ((null == fDate) || (null == oDate)) {
			return -1;
		}
		long intervalMilli = oDate.getTime() - fDate.getTime();
		return (int) (intervalMilli / (1000));

	}

	/**
	 * 时间比较返回天数
	 *
	 * @param fDate
	 * @param oDate
	 * @return
	 */
	public static int daysOfTwo(Date fDate, Date oDate) {

		Calendar aCalendar = Calendar.getInstance();

		aCalendar.setTime(fDate);

		int day1 = aCalendar.get(Calendar.DAY_OF_YEAR);

		aCalendar.setTime(oDate);

		int day2 = aCalendar.get(Calendar.DAY_OF_YEAR);

		return day2 - day1;

	}

	public static long sceondOfTwoDate(Date createtime, int minuttes) {

		Date datenow = new Date();
		long diff = ((createtime.getTime() + (minuttes * 60 * 1000)) - datenow.getTime()) / 1000;
		if (diff <= 0) {
			diff = 0;
		}
		return diff;
	}

	public static Date getNextDay(Date sDate) {

		return addDate(sDate, 1);
	}

	public static Date getYesterDay() {

		return addDate(DateUtils.getNow(), -1);

	}

	public static Date addDate(Date date, int i) {
		Calendar calendar = Calendar.getInstance();
		calendar.setTime(date);
		calendar.add(Calendar.DAY_OF_YEAR, i);
		return calendar.getTime();
	}

	/**
	 * 得到本月的第一天
	 *
	 * @return
	 */
	public static Date getFirstDayOfCurrentMonth() {
		Calendar calendar = Calendar.getInstance();
		calendar.set(calendar.get(Calendar.YEAR), calendar.get(Calendar.MONTH), 1, 0, 0, 0);
		return calendar.getTime();
	}

	/**
	 * 返回当前月的最后一天最后一秒
	 *
	 * @return
	 */
	public static Date getLastTimeOfLastDayOfCurrentMonth() {
		Calendar calendar = Calendar.getInstance();
		calendar.set(calendar.get(Calendar.YEAR), calendar.get(Calendar.MONTH) + 1, 1, 0, 0, 0);
		calendar.set(Calendar.SECOND, calendar.get(Calendar.SECOND) - 1);
		return calendar.getTime();
	}

	/**
	 * 返回指定天的最后一秒的时间
	 *
	 * @param sDate
	 * @return
	 */
	public static Date getLastestTimeOfDay(Date sDate) {
		Calendar calendar = Calendar.getInstance();
		calendar.setTime(sDate);
		calendar.set(calendar.get(Calendar.YEAR), calendar.get(Calendar.MONTH), calendar.get(Calendar.DAY_OF_MONTH), 0,
				0, 0);
		calendar.set(Calendar.DAY_OF_YEAR, calendar.get(Calendar.DAY_OF_YEAR) + 1);
		calendar.set(Calendar.SECOND, calendar.get(Calendar.SECOND) - 1);
		return calendar.getTime();
	}

	public static String skipDateTime(String timeStr, int skipDay) {
		String pattern = "yyyy-MM-dd HH:mm";
		Date d = parse(pattern, timeStr);
		Date date = skipDateTime(d, skipDay);
		return formatDateTime(pattern, date);
	}

	public static java.util.Date parse(String pattern, String strDateTime) {
		java.util.Date date = null;
		if ((strDateTime == null) || (pattern == null)) {
			return null;
		}
		try {
			SimpleDateFormat formatter = new SimpleDateFormat(pattern);
			formatter.setLenient(false);
			date = formatter.parse(strDateTime);
		} catch (ParseException e) {
			e.printStackTrace();
		}
		return date;
	}

	public static String formatDateTime(String pattern, java.util.Date date) {
		String strDate = null;
		String strFormat = pattern;
		SimpleDateFormat dateFormat = null;

		if (date == null) {
			return "";
		}
		dateFormat = new SimpleDateFormat(strFormat);
		strDate = dateFormat.format(date);

		return strDate;
	}

	public static Date skipDateTime(Date d, int skipDay) {

		if (d == null) {
			return null;
		}
		Calendar calendar = Calendar.getInstance();
		calendar.setTime(d);
		calendar.add(Calendar.DATE, skipDay);
		return calendar.getTime();
	}

	/**
	 * 给指定的时间加上指定的天数小时数、分钟数和秒数后返回
	 */
	public static Date getDateTime(Date date, int skipDay, int skipHour, int skipMinute, int skipSecond) {

		if (null == date) {
			return null;
		}

		GregorianCalendar cal = new GregorianCalendar();
		cal.setTime(date);

		cal.add(Calendar.DAY_OF_MONTH, skipDay);
		cal.add(Calendar.HOUR_OF_DAY, skipHour);
		cal.add(Calendar.MINUTE, skipMinute);
		cal.add(Calendar.SECOND, skipSecond);

		return cal.getTime();
	}

	/**
	 * 根据生日（yyyy-MM-dd）获取年龄
	 * 
	 * @param birthday
	 * @return
	 */
	public static int getAge(String birthday) {
		if (StringUtils.isBlank(birthday)) {
			return -1;
		}
		Date d1 = DateUtils.formatStr(birthday, DATE_FMT);
		long times = (new Date().getTime()) - d1.getTime();
		int age = Long.valueOf((times / (1000 * 60 * 60 * 24)) / 365).intValue();
		return age > 0 ? age : 1;
	}

	/**
	 * 从身份证号码中获取生日日期
	 * 
	 * @param cardID
	 * @return
	 */
	public static Date getBirthday(String cardID) {
		if (StringUtils.isBlank(cardID)) {
			return null;
		}

		if (cardID.length() == 15) {
			Pattern p = Pattern
					.compile("^[1-9]\\d{5}(\\d{2}((((0[13578])|(1[02]))(([0-2][0-9])|(3[01])))|(((0[469])|(11))(([0-2][0-9])|(30)))|(02[0-2][0-9])))\\d{3}$");
			Matcher m = p.matcher(cardID);
			if (m.matches()) {
				String s = m.group(1);
				try {
					return new SimpleDateFormat("yyyyMMdd").parse("19" + s);
				} catch (ParseException e) {
				}
			}
		} else if (cardID.length() == 18) {
			Pattern p = Pattern
					.compile("^[1-9]\\d{5}(\\d{4}((((0[13578])|(1[02]))(([0-2][0-9])|(3[01])))|(((0[469])|(11))(([0-2][0-9])|(30)))|(02[0-2][0-9])))\\d{3}[\\dX]$");
			Matcher m = p.matcher(cardID);
			if (m.matches()) {
				String s = m.group(1);
				try {
					return new SimpleDateFormat("yyyyMMdd").parse(s);
				} catch (ParseException e) {
				}
			}
		}

		return null;
	}

	/**
	 * 获取当前日0时0分0秒
	 * 
	 * @return
	 */
	public static Date getEarlestTimeOfDay(Date sDate) {
		Calendar calendar = Calendar.getInstance();
		calendar.setTime(sDate);
		calendar.set(calendar.get(Calendar.YEAR), calendar.get(Calendar.MONTH), calendar.get(Calendar.DAY_OF_MONTH), 0,
				0, 0);
		return calendar.getTime();
	}

	/**
	 * 获取某天最早时间精确到"00:00:00" liuhong
	 * 
	 * 
	 * @param chooseDay由当前时间往前推几天
	 *            ,往前推一天传-1
	 * @return
	 */
	public static Date getStartDate(int chooseDay) {
		Calendar cal = Calendar.getInstance();
		cal.add(Calendar.DATE, chooseDay);
		String startTime = new SimpleDateFormat("yyyy-MM-dd ").format(cal.getTime()) + "00:00:00";
		Date startDate = DateUtils.formatStr(startTime, "yyyy-MM-dd HH:mm:ss");
		return startDate;

	}

	/**
	 * 获取某天最晚时间精确到"23:59:59" liuhong
	 * 
	 * 
	 * @param chooseDay由当前时间往前推几天
	 *            ,往前推一天传-1
	 * @return
	 */
	public static Date getEndDate(int chooseDay) {
		Calendar cal = Calendar.getInstance();
		cal.add(Calendar.DATE, chooseDay);
		String endTime = new SimpleDateFormat("yyyy-MM-dd ").format(cal.getTime()) + "23:59:59";
		Date endDate = DateUtils.formatStr(endTime, "yyyy-MM-dd HH:mm:ss");
		return endDate;

	}

	/**
	 * 计算收益时间
	 * 
	 * @param diffDay
	 *            收益间隔时间，T+1还是T+2
	 * @return
	 */
	public static Date calculateGainsDate(int diffDay) {
		Calendar now = Calendar.getInstance();
		if (isWeekDay(now)) {
			if (1 == now.get(Calendar.DAY_OF_WEEK)) {
				now.add(Calendar.DATE, 1);
			} else {
				now.add(Calendar.DATE, 2);
			}
		}

		now.add(Calendar.DATE, diffDay);
		if (isWeekDay(now)) {
			now.add(Calendar.DATE, 2);
		}

		return now.getTime();
	}

	private static boolean isWeekDay(Calendar now) {
		return 1 == now.get(Calendar.DAY_OF_WEEK) || 7 == now.get(Calendar.DAY_OF_WEEK);
	}

	/**
	 * 计算预计到账时间
	 * 
	 * @param date
	 * @return
	 */
	public static Date getExpectIncomeTime(Date date) {
		Calendar cal = Calendar.getInstance();
		cal.setTime(date);
		if (cal.get(Calendar.DAY_OF_WEEK) == 1) {
			cal.add(Calendar.DAY_OF_YEAR, 1);
			cal.set(Calendar.HOUR_OF_DAY, 10);
		} else if (cal.get(Calendar.DAY_OF_WEEK) == 7) {
			cal.add(Calendar.DAY_OF_YEAR, 2);
			cal.set(Calendar.HOUR_OF_DAY, 10);
		} else {
			int hour = cal.get(Calendar.HOUR_OF_DAY);
			if (hour < 9) {
				cal.set(Calendar.HOUR_OF_DAY, 9);
				cal.set(Calendar.MINUTE, 0);
			} else if (hour >= 18) {
				cal.add(Calendar.DAY_OF_YEAR, 1);
				if (cal.get(Calendar.DAY_OF_WEEK) == 7) {
					cal.add(Calendar.DAY_OF_YEAR, 2);
				}
				cal.set(Calendar.HOUR_OF_DAY, 9);
				cal.set(Calendar.MINUTE, 0);
			}
			cal.add(Calendar.MINUTE, 60);
		}
		return cal.getTime();
	}

	/**
	 * 根据calendar 获取相对应的值 ex:<br/>
	 * <em>	calendar :Calendar.MONTH</em><br/>
	 * <em>date:2016-06</em><br/>
	 * return 6
	 * 
	 * @param date
	 * @param calendar
	 * @return
	 */
	public static String getDateValue(Date date, int calendar) {
		if (date == null) {
			return "";
		}
		Calendar c = Calendar.getInstance();
		c.setTime(date);

		return String.valueOf(c.get(calendar) + 1);
	}

	 /**
     * 获取某月的最后一天
     * @Title:getLastDayOfMonth
     * @Description:
     * @param:@param year
     * @param:@param month
     * @param:@return
     * @return:String
     * @throws
     */
    public static String getLastDayOfMonth(int year,int month)
    {
        Calendar cal = Calendar.getInstance();
        //设置年份
        cal.set(Calendar.YEAR,year);
        //设置月份
        cal.set(Calendar.MONTH, month-1);
        //获取某月最大天数
        int lastDay = cal.getActualMaximum(Calendar.DAY_OF_MONTH);
        //设置日历中月份的最大天数
        cal.set(Calendar.DAY_OF_MONTH, lastDay);
        //格式化日期
        SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMdd");
        String lastDayOfMonth = sdf.format(cal.getTime());
         
        return lastDayOfMonth;
    }
    
    /**
     * 根据日期取得星期几  
     * @throws ParseException 
     * 
     * */
    public static String getWeek(String date){
        String[] weeks = new String[0];
        int week_index = 0;
        try {
            weeks = new String[]{"周日","周一","周二","周三","周四","周五","周六"};
            Calendar cal = Calendar.getInstance();
            SimpleDateFormat myFormatter = new SimpleDateFormat("yyyyMMdd");
            Date myDate = myFormatter.parse(date);
            cal.setTime(myDate);
            week_index = cal.get(Calendar.DAY_OF_WEEK) - 1;
            if(week_index<0){  
                week_index = 0;  
            }
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return weeks[week_index];  
    }  
    
    /**
     * 
     * 获取今天 yyyyMMdd
     * */
    public static String getToday(){
		Date date = new Date();// 取时间
		Calendar calendar = new GregorianCalendar();
		calendar.setTime(date);
		date = calendar.getTime();
		SimpleDateFormat formatter = new SimpleDateFormat("yyyyMMdd");
		String dateString = formatter.format(date);
		return dateString;
    }
    
    /**
     * 
     *  获取昨天 yyyyMMdd
     * */
    public static String getYesterday(){
		Date date = new Date();// 取时间
		Calendar calendar = new GregorianCalendar();
		calendar.setTime(date);
		calendar.add(calendar.DATE, -1);// 把日期往后增加一天.整数往后推,负数往前移动
		date = calendar.getTime(); // 这个时间就是日期往后推一天的结果
		SimpleDateFormat formatter = new SimpleDateFormat("yyyyMMdd");
		String dateString = formatter.format(date);
		return dateString;
    }
    
    
	public static void main(String[] args) {
		System.out.println(DateUtils.formatDate(new Date(), DateUtils.DATE_TPT_FOUR));
		System.out.println(getStartOfDay(DateUtils.getNow()));
		System.out.println(getEndOfDay(DateUtils.getNow()));
		Calendar cal = Calendar.getInstance();
		cal.set(Calendar.DAY_OF_WEEK, 6);
		cal.set(Calendar.HOUR_OF_DAY, 19);
		System.out.println(cal.getTime());
		System.out.println(DateUtils.getExpectIncomeTime(cal.getTime()));

		System.out.println(getDateValue(DateUtils.formatStr("2017-01", DateUtils.DATE_TPT_THREE), Calendar.MONTH));

	}
	
}

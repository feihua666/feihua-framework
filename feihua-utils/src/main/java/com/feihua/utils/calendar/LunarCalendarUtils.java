package com.feihua.utils.calendar;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

/**
 * 农历日历工具类
 * 该工具类支持的最早日期为 1900-01-31
 * 该工具类支持的最晚日期为 2049-12-31
 * Created by yangwei
 * Created at 2018/9/12 10:21
 */
public class LunarCalendarUtils {

    private static final ThreadLocal<SimpleDateFormat> threadLocal = new ThreadLocal<SimpleDateFormat>();
    private static final Object object = new Object();

    /**
     * 农历数据，从1900-2049
     */
    final static long[] lunarInfo = new long[]{
        0x04bd8, 0x04ae0, 0x0a570, 0x054d5, 0x0d260, 0x0d950, 0x16554, 0x056a0, 0x09ad0, 0x055d2,
        0x04ae0, 0x0a5b6, 0x0a4d0, 0x0d250, 0x1d255, 0x0b540, 0x0d6a0, 0x0ada2, 0x095b0, 0x14977,
        0x04970, 0x0a4b0, 0x0b4b5, 0x06a50, 0x06d40, 0x1ab54, 0x02b60, 0x09570, 0x052f2, 0x04970,
        0x06566, 0x0d4a0, 0x0ea50, 0x06e95, 0x05ad0, 0x02b60, 0x186e3, 0x092e0, 0x1c8d7, 0x0c950,
        0x0d4a0, 0x1d8a6, 0x0b550, 0x056a0, 0x1a5b4, 0x025d0, 0x092d0, 0x0d2b2, 0x0a950, 0x0b557,
        0x06ca0, 0x0b550, 0x15355, 0x04da0, 0x0a5d0, 0x14573, 0x052d0, 0x0a9a8, 0x0e950, 0x06aa0,
        0x0aea6, 0x0ab50, 0x04b60, 0x0aae4, 0x0a570, 0x05260, 0x0f263, 0x0d950, 0x05b57, 0x056a0,
        0x096d0, 0x04dd5, 0x04ad0, 0x0a4d0, 0x0d4d4, 0x0d250, 0x0d558, 0x0b540, 0x0b5a0, 0x195a6,
        0x095b0, 0x049b0, 0x0a974, 0x0a4b0, 0x0b27a, 0x06a50, 0x06d40, 0x0af46, 0x0ab60, 0x09570,
        0x04af5, 0x04970, 0x064b0, 0x074a3, 0x0ea50, 0x06b58, 0x055c0, 0x0ab60, 0x096d5, 0x092e0,
        0x0c960, 0x0d954, 0x0d4a0, 0x0da50, 0x07552, 0x056a0, 0x0abb7, 0x025d0, 0x092d0, 0x0cab5,
        0x0a950, 0x0b4a0, 0x0baa4, 0x0ad50, 0x055d9, 0x04ba0, 0x0a5b0, 0x15176, 0x052b0, 0x0a930,
        0x07954, 0x06aa0, 0x0ad50, 0x05b52, 0x04b60, 0x0a6e6, 0x0a4e0, 0x0d260, 0x0ea65, 0x0d530,
        0x05aa0, 0x076a3, 0x096d0, 0x04bd7, 0x04ad0, 0x0a4d0, 0x1d0b6, 0x0d250, 0x0d520, 0x0dd45,
        0x0b5a0, 0x056d0, 0x055b2, 0x049b0, 0x0a577, 0x0a4b0, 0x0aa50, 0x1b255, 0x06d20, 0x0ada0
    };

    /**
     * 二十四节气初始数据，从小寒开始的获取间隔时间，单位为分钟
     */
    private final static int[] solarTerm24Info = {
            0, 21208, 42467, 63836, 85337, 107014, 128867, 150921,
            173149, 195551, 218072, 240693, 263343, 285989, 308563, 331033,
            353350, 375494, 397447, 419210, 440795, 462224, 483532, 504758
    };

    /**
     * 支持的阴历开始年份
     */
    final static int startYear = 1900;
    /**
     * 支持的阴历结束年份
     */
    final static int endYear = 2049;
    /**
     * 农历开始日期
     * 日柱：甲辰
     */
    final static String lunarDateStart = startYear + "-01-01";
    /**
     * 公历开始日期
     * 农历1900-01-01和公历1900-01-31是同一天
     */
    final static String calDateStart = startYear + "-01-31";
    /**
     * 节气参考值，该日期是小寒
     */
    final static String solarTermRefer = startYear + "-01-06 02:05:00";

    /**
     * 生肖
     */
    public enum SymbolicAnimals {
        shu("鼠",1),
        niu( "牛", 2),
        hu( "虎",3),
        tu( "兔",4),
        longg( "龙",5),
        she( "蛇",6),
        ma( "马",7),
        yang( "羊",8),
        hou( "猴",9),
        ji( "鸡",10),
        gou( "狗",11),
        zhu( "猪",12);

        String value;
        int number;
        SymbolicAnimals(String animals,int number) {
            this.value = animals;
            this.number = number;
        }

        public String getChineseName() {
            return value;
        }

        public int getNumber() {
            return number;
        }

        public static SymbolicAnimals getByNumber(int number){
            for (SymbolicAnimals symbolicAnimals : SymbolicAnimals.values()) {
                if (symbolicAnimals.getNumber() == number){
                    return symbolicAnimals;
                }
            }
            return null;
        }
    }
    public enum Tiangan{
        jia("甲",1),
        yi("乙",2),
        bing("丙",3),
        ding("丁",4),
        wu("戊",5),
        ji("己",6),
        geng("庚",7),
        xin("辛",8),
        ren("壬",9),
        gui("癸",10);

        String value;
        int number;
        Tiangan(String value,int number) {
            this.value = value;
            this.number = number;
        }

        public String getChineseName() {
            return value;
        }

        public int getNumber() {
            return number;
        }
        public static Tiangan getByNumber(int number){
            for (Tiangan tiangan : Tiangan.values()) {
                if (tiangan.getNumber() == number){
                    return tiangan;
                }
            }
            return null;
        }
    }
    public enum Dizhi{
        zi("子",1),
        chou("丑",2),
        yin("寅",3),
        mou("卯", 4),
        chen("辰",5),
        si(	"巳",6),
        wu("午",7),
        wei("未",8),
        shen("申",9),
        you("酉",10),
        xu("戌",11),
        hai("亥" ,12);
        String value;
        int number;
        Dizhi(String value,int number) {
            this.value = value;
            this.number = number;
        }

        public String getChineseName() {
            return value;
        }

        public int getNumber() {
            return number;
        }
        public static Dizhi getByNumber(int number){
            for (Dizhi dizhi : Dizhi.values()) {
                if (dizhi.getNumber() == number){
                    return dizhi;
                }
            }
            return null;
        }
    }

    /**
     * 农历整数日
     */
    public enum ChinaBaseTen {
        chu("初",1),
        shi("十",2),
        ershi("廿",3),
        sanshi("卅", 4);
        String value;
        int number;
        ChinaBaseTen(String value, int number) {
            this.value = value;
            this.number = number;
        }

        public String getChineseName() {
            return value;
        }

        public int getNumber() {
            return number;
        }
        public static ChinaBaseTen getByNumber(int number){
            for (ChinaBaseTen chinaDay : ChinaBaseTen.values()) {
                if (chinaDay.getNumber() == number){
                    return chinaDay;
                }
            }
            return null;
        }

        /**
         * 根据农历天转汉字
         * @param lunarDay 农历天
         * @return
         */
        public static ChinaBaseTen getByDay(int lunarDay){
            if (lunarDay > 30 || lunarDay < 1)
                return null;
            if (lunarDay == 10) {
                lunarDay--;
            }
            return ChinaBaseTen.getByNumber(lunarDay / 10 + 1);

        }
    }

    /**
     * 十二月数字
     */
    public enum ChineseNumber{
        yi("一","正", 1),
        er("二","二",2),
        san("三","三",3),
        si("四","四",4),
        wu("五", "五", 5),
        liu("六","六", 6),
        qi("七", "七", 7),
        ba("八","八",8),
        jiu("九","九",9),
        shi("十","十二",10),
        shiyi("十一","冬",11),
        shier("十二","腊",12);
        String value;
        String monthName;
        int number;
        ChineseNumber(String value,String monthName,int number) {
            this.value = value;
            this.monthName = monthName;
            this.number = number;
        }

        public String getChineseName() {
            return value;
        }

        public int getNumber() {
            return number;
        }
        public static ChineseNumber getByNumber(int number){
            for (ChineseNumber chineseNumber : ChineseNumber.values()) {
                if (chineseNumber.getNumber() == number){
                    return chineseNumber;
                }
            }
            return null;
        }
        /**
         * 根据农历天转汉字
         * @param lunarDay 农历天
         * @return
         */
        public static ChineseNumber getByDay(int lunarDay){
            if (lunarDay > 30 || lunarDay < 1)
                return null;
            int n = lunarDay % 10 == 0 ? 9 : lunarDay % 10 - 1;
            return ChineseNumber.getByNumber(n + 1);

        }
    }

    /**
     * 二十四节气
     */
    public enum SolarTerm24 {
        xiaohan("小寒",1 ),
        dahan("大寒",2 ),
        lichun("立春",3 ),
        yushui("雨水",4 ),
        jingzhe("惊蛰",5 ),
        chunfen("春分",6 ),
        qingming("清明",7 ),
        guyu("谷雨",8 ),
        lixia("立夏",9 ),
        xiaoman("小满",10),
        mangzhong("芒种",11),
        xiazhi("夏至",12),
        xiaoshu("小暑",13),
        dashu("大暑",14),
        liqiu("立秋",15),
        chushu("处暑",16),
        bailu("白露",17),
        qiufen("秋分",18),
        hanlu("寒露",19),
        shuangjiang("霜降",20),
        lidong("立冬",21),
        xiaoxue("小雪",22),
        daxue("大雪",23),
        dongzhi("冬至",24);
        String value;
        int number;
        SolarTerm24(String value, int number) {
            this.value = value;
            this.number = number;
        }

        public String getChineseName() {
            return value;
        }

        public int getNumber() {
            return number;
        }
        public static SolarTerm24 getByNumber(int number){
            for (SolarTerm24 jieqi : SolarTerm24.values()) {
                if (jieqi.getNumber() == number){
                    return jieqi;
                }
            }
            return null;
        }

        /**
         * 根据公历日期获取节气
         * @param lunarDate
         * @return
         */
        public static SolarTerm24 getByDate(Date lunarDate){
            return getSolarTerm24(lunarDate);

        }
    }

    /**
     * 传统节日
     */
    public enum TraditionalFestival {
        chunjie("01-01","春节"),
        yuanxiaojie("01-15","元宵节"),
        longtaitoujie("02-02","龙抬头节"),
        duanwujie("05-05","端午节"),
        qixijie("07-07"," 七夕情人节"),
        guijie("07-14"," 鬼节"),
        zhongqiujie("08-15"," 中秋节"),
        chongyangjie("09-09"," 重阳节"),
        labajie("12-08","腊八节"),
        beifangxiaonian("12-23","北方小年(扫房)"),
        nanfangxiaonian("12-24","南方小年(掸尘)"),
        //注意除夕需要其它方法进行计算,没有固定日期
        chuxi("01-00","除夕");

        String date;
        String chineseName;

        TraditionalFestival(String date,String chineseName) {

            this.date = date;
            this.chineseName = chineseName;
        }

        public String getDate() {
            return date;
        }

        public String getChineseName() {
            return chineseName;
        }
    }

    /**
     * 十二星座
     */
    public enum Constellation{
        baiyang("03-21","04-19","白羊座",1),
        jinniu ("04-20","05-20","金牛座",2),
        shuangzi("05-21","06-21","双子座",3),
        juxie("06-22","07-22","巨蟹座",4),
        shizi("07-23","08-22","狮子座",5),
        chunv("08-23","09-22","处女座",6),
        tiancheng("09-23","10-23","天秤座",7),
        tianxie("10-24","11-22","天蝎座",8),
        sheshou("11-23","12-21","射手座",9),
        mojie("12-22","01-19","摩羯座",10),
        shuiping("01-20","02-18","水瓶座",11),
        shuangyu("02-19","03-20","双鱼座",12);
        String start;
        String end;
        String chineseName;
        int number;
        Constellation(String start,String end,String chineseName, int number) {
            this.chineseName = chineseName;
            this.number = number;
            this.start = start;
            this.end = end;
        }

        public String getStart() {
            return start;
        }

        public String getEnd() {
            return end;
        }

        public String getChineseName() {
            return chineseName;
        }

        public int getNumber() {
            return number;
        }

        public static Constellation getByNumber(int number){
            for (Constellation constellation : Constellation.values()) {
                if (number == constellation.getNumber()){
                    return constellation;
                }
            }
            return null;
        }
    }

    /**
     * 按公历某月第几个星期几的节日
     */
    public enum WeekSolarHoliday {
        muqinjie(5, 2, 7, "母亲节"),
        zhucanri(5, 3, 7, "全国助残日"),
        fuqinjie(6, 3, 7, "父亲节"),
        hepingri(9, 3, 2, "国际和平日"),
        longrenjie(9, 4, 7, "国际聋人节"),
        zhufangri(10, 1, 1, "国际住房日"),
        jianqingzaihairi(10, 2, 3, "国际减轻自然灾害日"),
        ganenjie(11, 4, 4, "感恩节");

        int month;
        int whichWeek;
        int day;
        String chineseName;
        WeekSolarHoliday(int month, int whichWeek, int day, String chineseName) {
            this.month = month;
            this.whichWeek = whichWeek;
            this.day = day;
            this.chineseName = chineseName;
        }

        public int getMonth() {
            return month;
        }

        public int getWhichWeek() {
            return whichWeek;
        }

        public int getDay() {
            return day;
        }

        public String getChineseName() {
            return chineseName;
        }
    }

    /**
     * 按公历计算的节日
     */
    public enum SolarHoliday{
        yuandan("01-01","元旦"),
        shijieshidiri("02-02", "世界湿地日"),
        guojiqixiangjie("02-10", "国际气象节"),
        qingrenjie("02-14", "情人节"),
        guojihaibaori("03-01", "国际海豹日"),
        xueleifengjinianri("03-05", "学雷锋纪念日"),
        funvjie("03-08", "妇女节"),
        zhishujie("03-12", "植树节 孙中山逝世纪念日"),
        guojijingchari("03-14", "国际警察日"),
        xiaofeizhequanyiri("03-15", "消费者权益日"),
        guojihanghairi("03-17", "中国国医节 国际航海日"),
        shijiesenlinri("03-21", "世界森林日 消除种族歧视国际日 世界儿歌日"),
        shijieshuiri("03-22", "世界水日"),
        shijiefangzhijiehebingri("03-24", "世界防治结核病日"),
        yurenjie("04-01", "愚人节"),
        shijieweishengri("04-07", "世界卫生日"),
        shijiediqiuri("04-22", "世界地球日"),
        laodongjie("05-01", "劳动节"),
        qingnianjie("05-04", "青年节"),
        shijiehongshiziri("05-08", "世界红十字日"),
        guojihushijie("05-12", "国际护士节"),
        shijiewuyanri("05-31", "世界无烟日"),
        guojiertongjie("06-01", "国际儿童节"),
        shijiehuanjingbaohuri("06-05", "世界环境保护日"),
        guojijinduri("06-26","国际禁毒日"),
        xiangganghuiguijinianri("07-01","建党节 香港回归纪念日 世界建筑日"),
        shijierenkouri("07-11","世界人口日"),
        jianjunjie("08-01","建军节"),
        fuqinjie("08-08","中国男子节 父亲节"),
        kangrizhanzhengshengli("08-15","抗日战争胜利纪念"),
        maozhuxishishijinian("09-09" ,"毛主席逝世纪念"),
        jiaoshijie("09-10","教师节"),
        jiuyibashibianjinianri("09-18","九·一八事变纪念日"),
        guojiaiyari("09-20","国际爱牙日"),
        shijielvyouri("09-27","世界旅游日"),
        kongzidanchen("09-28","孔子诞辰"),
        guoqingjie("10-01", "国庆节 国际音乐日"),
        laorenjie("10-06", "老人节"),
        lianheguori("10-24", "联合国日"),
        shijieqingnianjie("11-10", "世界青年节"),
        sunzhongshandanchenjinian("11-12", "孙中山诞辰纪念"),
        shijieaizibingri("12-01", "世界艾滋病日"),
        shijiecanjirenri("12-03", "世界残疾人日"),
        aomenhuiguijinian("12-20", "澳门回归纪念"),
        pinganye("12-24", "平安夜"),
        shengdanjie("12-25", "圣诞节"),
        maozhuxidanchenjinian("12-26", "毛主席诞辰纪念");

        String monthday;
        String chineseName;
        SolarHoliday(String monthday,String chineseName) {
            this.monthday = monthday;
            this.chineseName = chineseName;
        }

        public String getMonthday() {
            return monthday;
        }

        public String getChineseName() {
            return chineseName;
        }
    }

    /**
     * 二十八星宿
     */
    public enum ChinaConstellation{
        // 青龙七宿
        jiaomujiao("角木蛟",1),kangjinlong("亢金龙",2),nvtufu("女土蝠",3),fangritu("房日兔",4),xinyuehu("心月狐",5),weihuohu("尾火虎",6),jishuimao("箕水豹",7),
        // 玄武七宿
        doumuxie("斗木獬",8),niujinniu("牛金牛",9),dituhe("氐土貉",10),xurishu("虚日鼠",11),weiyueyan("危月燕",12),shihuozhu("室火猪",13),bishuixu("壁水獝",14),
        // 白虎七宿
        kuimulang("奎木狼",15),loujingou("娄金狗",16),weituzhi("胃土彘",17),maoriji("昴日鸡",18),biyuewu("毕月乌",19),zihuohou("觜火猴",20),shenshuiyuan("参水猿",21),
        // 朱雀七宿
        jingmuhan("井木犴",22),guijinyang("鬼金羊",23),liutuzhang("柳土獐",24),xingrima("星日马",25),zhangyuelu("张月鹿",26),yihuoshe("翼火蛇",27),zhenshuiyin("轸水蚓",28);
        String chineseName;
        int number;
        ChinaConstellation(String chineseName, int number) {
            this.chineseName = chineseName;
            this.number = number;
        }

        public String getChineseName() {
            return chineseName;
        }

        public int getNumber() {
            return number;
        }
        public static ChinaConstellation getByNumber(int number){
            for (ChinaConstellation chinaConstellation : ChinaConstellation.values()) {
                if(number == chinaConstellation.getNumber()){
                    return chinaConstellation;
                }
            }
            return null;
        }
    }

    /**
     * 二十四星宿参照表
     */
    public static String ChinaConstellationRefer[][] = {
            {"", "正月", "二月", "三月", "四月", "五月", "六月", "七月", "八月", "九月", "十月", "十一月", "十二月"},
            {"1日", "室", "奎", "胃", "毕", "参", "鬼", "张", "角", "氐", "心", "斗", "虚"},
            {"2日", "壁", "娄", "昴", "觜", "井", "柳", "翼", "亢", "房", "尾", "女", "危"},
            {"3日", "奎", "胃", "毕", "参", "鬼", "星", "轸", "氐", "心", "箕", "虚", "室"},
            {"4日", "娄", "昴", "觜", "井", "柳", "张", "角", "房", "尾", "斗", "危", "壁"},
            {"5日", "胃", "毕", "参", "鬼", "星", "翼", "亢", "心", "箕", "女", "室", "奎"},
            {"6日", "昴", "觜", "井", "柳", "张", "轸", "氐", "尾", "斗", "虚", "壁", "娄"},
            {"7日", "毕", "参", "鬼", "星", "翼", "角", "房", "箕", "女", "危", "奎", "胃"},
            {"8日", "觜", "井", "柳", "张", "轸", "亢", "心", "斗", "虚", "室", "娄", "昴"},
            {"9日", "参", "鬼", "星", "翼", "角", "氐", "尾", "女", "危", "壁", "胃", "毕"},
            {"10日", "井", "柳", "张", "轸", "亢", "房", "箕", "虚", "室", "奎", "昴", "觜"},
            {"11日", "鬼", "星", "翼", "角", "氐", "心", "斗", "危", "壁", "娄", "毕", "参"},
            {"12日", "柳", "张", "轸", "亢", "房", "尾", "女", "室", "奎", "胃", "觜", "井"},
            {"13日", "星", "翼", "角", "氐", "心", "箕", "虚", "壁", "娄", "昴", "参", "鬼"},
            {"14日", "张", "轸", "亢", "房", "尾", "斗", "危", "奎", "胃", "毕", "井", "柳"},
            {"15日", "翼", "角", "氐", "心", "箕", "女", "室", "娄", "昴", "觜", "鬼", "星"},
            {"16日", "轸", "亢", "房", "尾", "斗", "虚", "壁", "胃", "毕", "参", "柳", "张"},
            {"17日", "角", "氐", "心", "箕", "女", "危", "奎", "昴", "觜", "井", "星", "翼"},
            {"18日", "亢", "房", "尾", "斗", "虚", "室", "娄", "毕", "参", "鬼", "张", "轸"},
            {"19日", "氐", "心", "箕", "女", "危", "壁", "胃", "觜", "井", "柳", "翼", "角"},
            {"20日", "房", "尾", "斗", "虚", "室", "奎", "昴", "参", "鬼", "星", "轸", "亢"},
            {"21日", "心", "箕", "女", "危", "壁", "娄", "毕", "井", "柳", "张", "角", "氐"},
            {"22日", "尾", "斗", "虚", "室", "奎", "胃", "觜", "鬼", "星", "翼", "亢", "房"},
            {"23日", "箕", "女", "危", "壁", "娄", "昴", "参", "柳", "张", "轸", "氐", "心"},
            {"24日", "斗", "虚", "室", "奎", "胃", "毕", "井", "星", "翼", "角", "房", "尾"},
            {"25日", "女", "危", "壁", "娄", "昴", "觜", "鬼", "张", "轸", "亢", "心", "箕"},
            {"26日", "虚", "室", "奎", "胃", "毕", "参", "柳", "翼", "角", "氐", "尾", "斗"},
            {"27日", "危", "壁", "娄", "昴", "觜", "井", "星", "轸", "亢", "房", "箕", "女"},
            {"28日", "室", "奎", "胃", "毕", "参", "鬼", "张", "角", "氐", "心", "斗", "虚"},
            {"29日", "壁", "娄", "昴", "觜", "井", "柳", "翼", "亢", "房", "尾", "女", "危"},
            {"30日", "奎", "胃", "毕", "参", "鬼", "星", "轸", "氐", "心", "箕", "虚", "室"}};
    /**
     * 计算闰月
     * @param lunarYear 农历年
     * @return 0代表没有闰月，大于0代表闰月的月份
     */
    final public static int leapMonth(int lunarYear) {
        return (int) (lunarInfo[lunarYear - startYear] & 0xf);
    }

    /**
     * 计算闰月的月分天数
     * @param lunarYear 农历年
     * @return 0代表没有闰月，大于0代表闰月份天数
     */
    final public static int leapMonthDays(int lunarYear) {
        if (leapMonth(lunarYear) != 0) {
            if ((lunarInfo[lunarYear - startYear] & 0x10000) != 0)
                return 30;
            else
                return 29;
        } else
            return 0;
    }

    /**
     * 计算农历年份的总天数
     * @param lunarYear 农历年
     * @return
     */
    final public static int yearDays(int lunarYear) {
        // 29天 X 12个月
        int i, sum = 348;
        for (i = 0x8000; i > 0x8; i >>= 1) {
            if ((lunarInfo[lunarYear - startYear] & i) != 0) sum += 1;
        }
        return (sum + leapMonthDays(lunarYear));
    }

    /**
     * 计算某年某月的总天数
     * @param lunarYear 农历年
     * @param lunarMonth
     * @return
     */
    final public static int monthDays(int lunarYear, int lunarMonth) {
        if ((lunarInfo[lunarYear - startYear] & (0x10000 >> lunarMonth)) == 0)
            return 29;
        else
            return 30;
    }

    /**
     * 计算年的生肖
     * @param lunarYear 农历年
     * @return
     */
    final public static SymbolicAnimals symbolicAnimalsYear(int lunarYear){
        return SymbolicAnimals.getByNumber((lunarYear - 4) % 12 + 1);
    }

    /**
     * 计算年天干
     * @param lunarYear 农历年
     * @return
     * 1900年立春后为庚子年
     */
    final public static Tiangan tianganYear(int lunarYear){
        // 也可以这样
        /*int num = (lunarYear - 3)%10;
        if(num == 0){
            num = 10;
        }*/
        int num = lunarYear - startYear + 36;
        return Tiangan.getByNumber(num % 10 + 1);
    }

    /**
     * 计算月天干
     * @param lunarYear 农历年
     * @param lunarmonth 农历月
     * @return
     * 根据表推算，天干年不同正月的开始天干月不同，但有规律
     */
    final public static Tiangan tianganMonth(int lunarYear,int lunarmonth){
        LunarCalendarUtils.Tiangan tiangan = LunarCalendarUtils.tianganYear(lunarYear);
        int num = tiangan.getNumber();
        // 因为甲乙丙丁戊和已庚辛壬癸的正月天干是相同的，所有都按甲乙丙丁戊算起，这是5的来历
        if (num > 5) {
            num = num - 5;
        }

        int firstMonthNum = (num * 2 +1) % 10;
        int monthNum = (firstMonthNum + lunarmonth - 1) % 10;
        if(monthNum == 0){
            monthNum = 10;
        }
        return Tiangan.getByNumber(monthNum);
    }
    /**
     * 计算日天干
     * @param lunarYear 农历年
     * @param lunarMonth 农历月
     * @param lunarDay 农历日
     * @return
     */
    final public static Tiangan tianganDay(int lunarYear,int lunarMonth,int lunarDay){
        LunarDate date = new LunarDate(lunarYear,lunarMonth,lunarDay,0,0,0);
        int num = CalendarUtils.getIntervalDays(LunarToCalendar(date),CalendarUtils.stringToDate(calDateStart));
        if(num >= 0){
            num = num  % 10 + 1;
        }else{
            num = 10 - (num  % 10 + 1);
        }
        return Tiangan.getByNumber(num);
    }

    /**
     * 获取时辰天干
     * @param lunarYear
     * @param lunarMonth
     * @param lunarDay
     * @param lunarHour
     * @param lunarMinite
     * @return
     */
    final public static Tiangan tianganHour(int lunarYear,int lunarMonth,int lunarDay,int lunarHour,int lunarMinite){
        Tiangan tianganDay = tianganDay(lunarYear,lunarMonth,lunarDay);
        int num = tianganDay.getNumber();
        // 因为甲乙丙丁戊和已庚辛壬癸的正月天干是相同的，所有都按甲乙丙丁戊算起，这是5的来历
        if (num > 5) {
            num = num - 5;
        }
        // 第一个时辰子时的天数字
        int firstHourNum = num * 2 -1;


        // 计算给定的时间已经过了多少个时辰
        int _hour = lunarHour;
        int _minite = lunarMinite;
        if(_minite != 0){
            _hour += 1;
        }
        int _num = _hour/2;
        if(_num >=12){
            _num = 0;
        }

        // 最后得到天干
        int rNum = (firstHourNum + _num) % 10;
        if(rNum == 0){
            rNum = 10;
        }
        return Tiangan.getByNumber(rNum);
    }
    /**
     * 计算年地支
     * @param lunarYear 农历年
     * @return
     */
    final public static Dizhi dizhiYear(int lunarYear){
        // 也可以这样
        /*int num = (lunarYear - 3)%12;
        if(num == 0){
            num = 12;
        }*/
        int num = lunarYear - startYear + 36;
        return Dizhi.getByNumber(num % 12 + 1);
    }
    /**
     * 计算月地之
     * @param lunarMonth 农历月
     * @return
     * 每个月的地支总是固定的,而且总是从寅月开始
     */
    final public static Dizhi dizhiMonth(int lunarMonth){
        int num = (lunarMonth + 2) % 12;
        if(num == 0){
            num  = 12;
        }
        return Dizhi.getByNumber(num);
    }
    /**
     * 计算日地之
     * @param lunarYear 农历年
     * @param lunarMonth 农历月
     * @param lunarDay 农历天
     * @return
     */
    final public static Dizhi dizhiDay(int lunarYear,int lunarMonth,int lunarDay){
        LunarDate date = new LunarDate(lunarYear,lunarMonth,lunarDay,0,0,0);
        int num = CalendarUtils.getIntervalDays(LunarToCalendar(date),CalendarUtils.stringToDate(calDateStart));
        if(num >= 0){
            num = num  % 12 + 5;
        }else{
            num = 12 - (num  % 12 + 5);
        }
        num = num % 12;
        if(num == 0)
            num = 12;
        return Dizhi.getByNumber(num);
    }

    /**
     * 计算时辰地之
     * @param hour 农历时 24小时制
     * @param minute 农历分
     * @return
     */
    final public static Dizhi dizhiHour(int hour,int minute){
        int _hour = hour;
        int _minute = minute;
        if(_minute !=0){
            _hour += 1;
        }
        int num = _hour/2;

        if(num >=12){
            num = 0;
        }
        return Dizhi.getByNumber(num + 1);
    }
    /**
     * 公历日期转农历
     * @param date 公历日期
     * @return 农历日期
     */
    public static LunarDate CalendarToLunar(Date date) {
        int yearCyl, monCyl, dayCyl;
        int leapMonth = 0;
        Date baseDate = CalendarUtils.stringToDate(calDateStart);
        Date nowaday=date;

        //求出和1900年1月31日相差的天数
        int offset = (int) ((nowaday.getTime() - baseDate.getTime()) / 86400000L);
        dayCyl = offset + 40;
        monCyl = 14;

        //用offset减去每农历年的天数
        // 计算当天是农历第几天
        //i最终结果是农历的年份
        //offset是当年的第几天
        int iYear, daysOfYear = 0;
        for (iYear = startYear; iYear < 10000 && offset > 0; iYear++) {
            daysOfYear = yearDays(iYear);
            offset -= daysOfYear;
            monCyl += 12;
        }
        if (offset < 0) {
            offset += daysOfYear;
            iYear--;
            monCyl -= 12;
        }
        //农历年份
        int year = iYear;

        yearCyl = iYear - 1864;
        leapMonth = leapMonth(iYear); //闰哪个月,1-12
        boolean leap = false;

        //用当年的天数offset,逐个减去每月（农历）的天数，求出当天是本月的第几天
        int iMonth, daysOfMonth = 0;
        for (iMonth = 1; iMonth < 13 && offset > 0; iMonth++) {
            //闰月
            if (leapMonth > 0 && iMonth == (leapMonth + 1) && !leap) {
                --iMonth;
                leap = true;
                daysOfMonth = leapMonthDays(year);
            } else
                daysOfMonth = monthDays(year, iMonth);

            offset -= daysOfMonth;
            //解除闰月
            if (leap && iMonth == (leapMonth + 1)) leap = false;
            if (!leap) monCyl++;
        }
        //offset为0时，并且刚才计算的月份是闰月，要校正
        if (offset == 0 && leapMonth > 0 && iMonth == leapMonth + 1) {
            if (leap) {
                leap = false;
            } else {
                leap = true;
                --iMonth;
                --monCyl;
            }
        }
        //offset小于0时，也要校正
        if (offset < 0) {
            offset += daysOfMonth;
            --iMonth;
            --monCyl;
        }
        int month = iMonth;
        int day = offset + 1;
        int hour = CalendarUtils.getHour(date);
        int minute = CalendarUtils.getMinute(date);
        int second = CalendarUtils.getSecond(date);
        return new LunarDate(year , month, day ,hour, minute ,second);
    }

    /**
     * 农历转公历
     * @param lunarDate 农历日期
     * @return 公历日期
     */
    public static Date LunarToCalendar(LunarDate lunarDate){
        int year = lunarDate.getYear();
        int mon = lunarDate.getMonth();
        int day = lunarDate.getDay();

        int dt = 0;

        for(int y = startYear;y < year; y++){ //计算整年天数
            dt += yearDays(y);
        }

        if(leapMonth(year) != 0 && leapMonth(year) < mon){//闰月在前,先计算闰月天数
            dt += leapMonthDays(year);
        }

        //计算整月天数
        for(int i = 1,d = 0x8000;i < mon; i++, d>>=1) {
            dt += (lunarInfo[year-startYear] & d) == 0 ? 29: 30;
        }
        //当月天数
        dt += day;
        int hour = lunarDate.getHour();
        int minute = lunarDate.getMinute();
        int second = lunarDate.getSecond();
        Date date = CalendarUtils.stringToDate(calDateStart + " "+hour + ":" + minute + ":" + second);
        return CalendarUtils.addDay(date,dt - 1);
    }


    /**
     * 返回公历年节气的日期
     *
     * @param year 指定公历年份
     * @param number 指定节气序号(1从小寒算起)
     * @return
     */
    private static Date getSolarTerm24Date(int year, int number) {
        long l = (long) 31556925974.7 * (year - startYear)
                + solarTerm24Info[number-1] * 60000L;
        Date tempDate = CalendarUtils.stringToDate(solarTermRefer, CalendarUtils.DateStyle.YYYY_MM_DD_HH_MM_SS);
        l = l + tempDate.getTime();
        return new Date(l);
    }

    /**
     * 获取指定公历日期的二十四节气
     * @param date 公历日期
     * @return
     */
    public static SolarTerm24 getSolarTerm24(Date date){
        int length = SolarTerm24.values().length;
        int year = Integer.parseInt(CalendarUtils.dateToString(date, "yyyy"));
        Map<String,SolarTerm24> map = new HashMap<>();
        int i = 0;
        for (SolarTerm24 solarTerm24 : SolarTerm24.values()) {
            i++;
            Date tempDate = getSolarTerm24Date(year, i);
            map.put(CalendarUtils.dateToString(tempDate, CalendarUtils.DateStyle.YYYY_MM_DD), solarTerm24);
        }

        return map.get(CalendarUtils.dateToString(date, CalendarUtils.DateStyle.YYYY_MM_DD));
    }

    /**
     * 获取指定公历日期的星座
     * @param date 公历日期
     * @return
     */
    public static Constellation getConstellation(Date date){
        int month = CalendarUtils.getMonth(date);
        int day = CalendarUtils.getDay(date);
        int temp =  month * 100 + day;
        int number = 1;
        if (((temp >= 321) && (temp <= 419))) { number = 0; }
        else if ((temp >= 420) && (temp <= 520)) { number = 1; }
        else if ((temp >= 521) && (temp <= 620)) { number = 2; }
        else if ((temp >= 621) && (temp <= 722)) { number = 3; }
        else if ((temp >= 723) && (temp <= 822)) { number = 4; }
        else if ((temp >= 823) && (temp <= 922)) { number = 5; }
        else if ((temp >= 923) && (temp <= 1022)) { number = 6; }
        else if ((temp >= 1023) && (temp <= 1121)) { number = 7; }
        else if ((temp >= 1122) && (temp <= 1221)) { number = 8; }
        else if ((temp >= 1222) || (temp <= 119)) { number = 9; }
        else if ((temp >= 120) && (temp <= 218)) { number = 10; }
        else if ((temp >= 219) && (temp <= 320)) { number = 11; }
        else { number = 0; }
        return Constellation.getByNumber(number + 1);
    }

    /**
     * 获取指定公历日期的以周计算的节日，某月第几个星期几的节日
     * @param date 公历日期
     * @return
     */
    public static WeekSolarHoliday getWeekHoliday(Date date){
        CalendarUtils.Week week = CalendarUtils.getWeek(date);
        int month = CalendarUtils.getMonth(date);
        for (WeekSolarHoliday weekSolarHoliday : WeekSolarHoliday.values()) {
            // 如果月份和星期几都相同
            if(month == weekSolarHoliday.getMonth() && week.getNumber() == weekSolarHoliday.getDay() &&  CalendarUtils.getWeekOfMonth(date) == weekSolarHoliday.getWhichWeek()){
               return weekSolarHoliday;
            }
        }
        return null;
    }

    /**
     * 获取指定公历日期的节日
     * @param date 公历日期
     * @return
     */
    public static SolarHoliday getSolarHoliday(Date date){
        for (SolarHoliday solarHoliday : SolarHoliday.values()) {
            if(solarHoliday.getMonthday().equals(CalendarUtils.dateToString(date, CalendarUtils.DateStyle.MM_DD))){
                return solarHoliday;
            }
        }
        return null;
    }

    /**
     * 获取星宿
     * @param lunarMonth 农历月
     * @param lunarDay 农历日
     * @return
     */
    public static ChinaConstellation getChinaConstellation(int lunarMonth,int lunarDay){

        int month = lunarMonth;
        int day = lunarDay;
        String chinaConstellationReferPrefix =  ChinaConstellationRefer[day][month];
        for (ChinaConstellation chinaConstellation : ChinaConstellation.values()) {
            if(chinaConstellation.getChineseName().startsWith(chinaConstellationReferPrefix)){
                return chinaConstellation;
            }
        }
       return null;
    }

    /**
     * 根据农历日期获取节日
     * @param lunarYear
     * @param lunarMonth
     * @param lunarDay
     *
     * @return
     */
    public static TraditionalFestival getTraditionalFestival(int lunarYear,int lunarMonth,int lunarDay){


        String monthday = lunarMonth + "-" + lunarDay;
        //除夕判断
        int month = lunarMonth;
        if(12 == month){
            int monthDays = monthDays(lunarYear,month);
            // 是除夕，因为给定的日期是12月的最后一天
            if (lunarDay == (monthDays)){
                monthday = TraditionalFestival.chuxi.getDate();
            }
        }
        for (TraditionalFestival traditionalFestival : TraditionalFestival.values()) {
            if(traditionalFestival.getDate().equals(monthday)){
                return traditionalFestival;
            }
        }
        return null;
    }
    /**
     * 获取SimpleDateFormat
     * @param pattern 日期格式
     * @return SimpleDateFormat对象
     * @throws RuntimeException 异常：非法日期格式
     */
    private static SimpleDateFormat getDateFormat(String pattern) throws RuntimeException {
        SimpleDateFormat dateFormat = threadLocal.get();
        if (dateFormat == null) {
            synchronized (object) {
                if (dateFormat == null) {
                    dateFormat = new SimpleDateFormat(pattern);
                    dateFormat.setLenient(true);
                    threadLocal.set(dateFormat);
                }
            }
        }
        dateFormat.applyPattern(pattern);
        return dateFormat;
    }
    /**
     * 农历日期转农历字符
     * @param date
     * @param dateStyle
     * @return
     */
    public static String lunarDateToString(LunarDate date, CalendarUtils.DateStyle dateStyle){
        if(date.getMonth() == 2 && date.getDay() <=  28){
            String lunarDateStr = date.getYear()+ "-"+date.getMonth() + "-" + date.getDay() + " "+date.getHour() + ":" + date.getMinute() + ":" + date.getSecond();
            return CalendarUtils.dateToString(CalendarUtils.stringToDate(lunarDateStr,true),dateStyle,true);
        }else{
            return dateStyle.getValue().replace("yyyy",date.getYear() + "").replace("MM",date.getMonth() + "")
                    .replace("dd",date.getDay() + "").replace("HH",date.getHour() + "")
                    .replace("mm",date.getMinute() + "").replace("ss",date.getSecond() + "");

        }

    }

    /**
     * 农历日期字符串转农历日期
     * @param date
     * @return
     */
    public static LunarDate stringTolunarDate(String date){
        LunarDate lunarDate = _stringTolunarDate(date);
        if(lunarDate.getMonth() == 3 && lunarDate.getDay() == 1){
            int month = 2;
            int day = 29;
            String lunarDateStr1 = lunarDate.getYear()+ "-"+ month + "-" + day + " "+lunarDate.getHour() + ":" + lunarDate.getMinute() + ":" + lunarDate.getSecond();
            LunarDate lunarDate1 = _stringTolunarDate(lunarDateStr1);
            if(lunarDate.equals(lunarDate1)){
                lunarDate1.setMonth(month);
                lunarDate1.setDay(day);
                return lunarDate1;
            }
            day = 30;
            String lunarDateStr2 = lunarDate.getYear()+ "-"+ month + "-" + day + " "+lunarDate.getHour() + ":" + lunarDate.getMinute() + ":" + lunarDate.getSecond();
            LunarDate lunarDate2 = _stringTolunarDate(lunarDateStr2);
            if(lunarDate.equals(lunarDate2)){
                lunarDate2.setMonth(month);
                lunarDate2.setDay(day);
                return lunarDate2;
            }
        }
        return lunarDate;
    }
    private static LunarDate _stringTolunarDate(String date){


        Date _date = CalendarUtils.stringToDate(date,true);
        int year = CalendarUtils.getYear(_date);
        int month = CalendarUtils.getMonth(_date);
        int day = CalendarUtils.getDay(_date);
        int hour = CalendarUtils.getHour(_date);
        int minute = CalendarUtils.getMinute(_date);
        int second = CalendarUtils.getSecond(_date);
        return new LunarDate(year,month,day,hour,minute,second);
    }
}

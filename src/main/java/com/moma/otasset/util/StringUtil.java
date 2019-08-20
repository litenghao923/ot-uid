package com.moma.otasset.util;

import lombok.extern.slf4j.Slf4j;

import java.text.MessageFormat;
import java.text.SimpleDateFormat;
import java.util.*;

/**
 * file:StringUtil
 * <p>
 * 文件简要说明
 *
 * @author 2018-06-14 hgl 创建初始版本
 * @version V1.0  简要版本说明
 * @par 版权信息：
 * 2018 Copyright 河南艾鹿网络科技有限公司 All Rights Reserved.
 */
@Slf4j
public class StringUtil {

    private static final String ALL_LETTER = "ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz";

    private static final String ALL_LETTER_NUM = "ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz1234567890";
    private static final String ALL_BIG_LETTER_NUM = "ABCDEFGHIJKLMNOPQRSTUVWXYZ1234567890";

    // 判断字符串是否为空
    public static boolean isEmpty(String str) {
        if (Objects.equals(null, str) || Objects.equals("", str.trim())) {
            return true;
        } else {
            return false;
        }
    }

    // 判断字符串是否不为空
    public static boolean isNotEmpty(String str) {
        return !isEmpty(str);
    }

    /**
     * 获取随机字母
     *
     * @param length 获取的长度
     * @return 随机获取的字符串
     */
    public static String getRandomLetter(int length) {
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < length; i++) {
            sb.append(ALL_LETTER.charAt(new Random().nextInt(ALL_LETTER.length())));
        }
        return sb.toString();
    }

    /**
     * 获取随机字母
     *
     * @param length 获取的长度
     * @return 随机获取的字符串
     */
    public static String getRandomBigLetterAndNum(int length) {
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < length; i++) {
            sb.append(ALL_BIG_LETTER_NUM.charAt(new Random().nextInt(ALL_BIG_LETTER_NUM.length())));
        }
        return sb.toString();
    }



    /**
     * 获6位唯一编码
     *
     * @return 随机获取的字符串
     */
    public static String getUnRepeatSixCode() {
        String sixChar = "";
        SimpleDateFormat sdf = new SimpleDateFormat("yyMMddHHmmss");
        Date date = new Date();
        String time = sdf.format(date);
        for (int i = 0; i < time.length() / 2; i++) {
            String singleChar;
            String x = time.substring(i * 2, (i + 1) * 2);
            int b = Integer.parseInt(x);
            if (b < 10) {
                singleChar = Integer.toHexString(Integer.parseInt(x));
            } else if (b >= 10 && b < 36) {
                singleChar = String.valueOf((char) (Integer.parseInt(x) + 55));
            } else {
                singleChar = String.valueOf((char) (Integer.parseInt(x) + 61));
            }
            sixChar = sixChar + singleChar;

        }
        //System.out.println("生成一个6位不可重复的字符编码是：" + sixChar);
        return sixChar;
    }

    //根据id生成邀请码
    public static String getInvite(Long userId){
        Long sum = 100000 + userId ;
        String strReverse=new StringBuffer(sum.toString()).reverse().toString();
        return strReverse;
    }

    /**
     * 字符串 过长 省略
     *
     * @param s     原字符串
     * @param limit 长度上线(大于此长度用省略号代替)
     * @return java.lang.String
     * @date 2018/3/9
     * @author hgl
     * @see
     * @since
     */
    public static String getStringLimit(String s, int limit) {
        if (isEmpty(s)) {
            return "";
        }
        if (s.length() <= limit) {
            return s;
        }
        return s.substring(0, limit) + "...";
    }

    public static String toUpperCaseFirstOne(String s){
        if (StringUtil.isEmpty(s)){
            return "";
        }
        if(Character.isUpperCase(s.charAt(0))) {
            return s;
        } else {
            return (new StringBuilder()).append(Character.toUpperCase(s.charAt(0))).append(s.substring(1)).toString();
        }
    }

    /** 发送 短信 电话 提醒 组织内容  主要用户 除价格提醒外 其他提醒 */
    public static String getPhoneContent(String keyword1,String keyword2){
        String templet = "您关注的{0}数据异常：{1}。请及时查看";
        return MessageFormat.format(templet,keyword1,keyword2);
    }

    /** 大额转账使用 字符串 过长 省略  截取后样式：0x0c37ab……ed5ba8(btc钱包) */
    public static String getStringOmitByBigAmount(String s, int limit1,int limit2) {
        if (isEmpty(s)) {
            return "";
        }
        if(s.indexOf("（") > 0){
            String postfix = s.substring(s.indexOf("（"),s.length());
            return getStringOmit(s.substring(0,s.indexOf("（")),limit1,limit2) + postfix;
        }
//		if(s.indexOf("（") > 0){
//			return getStringOmit(s.substring(0,s.indexOf("（")-1),limit1,limit2);
//		}
        return getStringOmit(s,limit1,limit2);
    }

    /**
     * 字符串 过长 省略  截取后 格式：0x0c37ab……ed5ba8
     *
     * @date 2018-07-18
     * @author hgl
     * @since
     * @see
     * @param s
     * @param limit1 省略号 前 字符段 长度
     * @param limit2 省略号 后 字符段 长度
     *
     * @return java.lang.String
     */
    public static String getStringOmit(String s, int limit1,int limit2) {
        if (isEmpty(s)) {
            return "";
        }
        if (s.length() <= limit1 + limit2) {
            return s;
        }
        return s.substring(0, limit1) + "……" + s.substring(s.length()-limit2, s.length());
    }

    /** 大额转账使用 字符串 过长 省略 截掉小括号 截取前样式 ：0x0c37abAAAAAAAAed5ba8(btc钱包)  截取后样式：0x0c37ab……ed5ba8 */
    public static String getStringOmitByBigAmountCut(String s, int limit1,int limit2) {
        if (isEmpty(s)) {
            return "";
        }
        if(s.indexOf("（") > 0){
            return getStringOmit(s.substring(0,s.indexOf("（")),limit1,limit2);
        }
        return getStringOmit(s,limit1,limit2);
    }

    //掩饰手机号
    public static String changeNumberToLetter(String phone){
        phone=phone.replace("0","A");
        phone=phone.replace("1","B");
        phone=phone.replace("2","C");
        phone=phone.replace("3","D");
        phone=phone.replace("4","E");
        phone=phone.replace("5","F");
        phone=phone.replace("6","G");
        phone=phone.replace("7","H");
        phone=phone.replace("8","I");
        phone=phone.replace("9","J");
        return phone;
    }

    //转换出手机号
    public static String changeLetterToNumber(String phone){
        phone=phone.replace("A","0");
        phone=phone.replace("B","1");
        phone=phone.replace("C","2");
        phone=phone.replace("D","3");
        phone=phone.replace("E","4");
        phone=phone.replace("F","5");
        phone=phone.replace("G","6");
        phone=phone.replace("H","7");
        phone=phone.replace("I","8");
        phone=phone.replace("J","9");
        return phone;
    }

    //判断是否包含日文
    public static Boolean checkJapanese(String text)
    {
        Set<Character.UnicodeBlock> japaneseUnicodeBlocks = new HashSet<Character.UnicodeBlock>()
        {{
            add(Character.UnicodeBlock.HIRAGANA);
            add(Character.UnicodeBlock.KATAKANA);
            add(Character.UnicodeBlock.CJK_UNIFIED_IDEOGRAPHS);
        }};
        for (char c : text.toCharArray())
        {
            if (japaneseUnicodeBlocks.contains(Character.UnicodeBlock.of(c)))
            {
                return true;
            }
        }
        return false;
    }


}

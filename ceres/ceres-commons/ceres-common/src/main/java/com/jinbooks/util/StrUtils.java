/*
 * Copyright [2025] [JinBooks of copyright http://www.jinbooks.com]
 * 
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 * 
 *     http://www.apache.org/licenses/LICENSE-2.0
 * 
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 *
 */
 

 

 

package com.jinbooks.util;

import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.apache.commons.lang3.StringUtils;

import net.sourceforge.pinyin4j.PinyinHelper;
import net.sourceforge.pinyin4j.format.HanyuPinyinCaseType;
import net.sourceforge.pinyin4j.format.HanyuPinyinOutputFormat;
import net.sourceforge.pinyin4j.format.HanyuPinyinToneType;
import net.sourceforge.pinyin4j.format.HanyuPinyinVCharType;

public final class StrUtils{

	static final Pattern 	MOBILE_PATTERN 		= Pattern.compile("^((13[0-9])|(14[0-9])|(15[0-9])|(16[0-9])|(17[0-9])|(18[0-9])|(19[0-9]))\\d{8}$");
    
	static final Pattern 	START_UPPER_PATTERN 		= Pattern.compile("^[A-Z]");
	
	static final Pattern 	START_LOWER_PATTERN 		= Pattern.compile("^[a-z]");
	
	static final Pattern 	START_NUMBER_PATTERN 		= Pattern.compile("^[0-9]");
	
	static final Pattern 	UPPER_PATTERN 		= Pattern.compile("[A-Z]");
	
	static final Pattern 	LOWER_PATTERN 		= Pattern.compile("[a-z]");
	
	static final Pattern 	NUMBER_PATTERN 		= Pattern.compile("[0-9]");
	/*
     * 获取指定UTF-8模式字节长度的字符串
     */
    public static String limitLength(String strValue, int bytelen) {

        // 中文汉字占用三个字节
        int strlen = bytelen / 3;
        int real_bytelen = strlen * 3;

        // 如果为NULL或�?空，则直接返�?
        if (null == strValue || "".equalsIgnoreCase(strValue)) {
            return "";
        }

        try {
            byte[] utf8_bytes = strValue.getBytes("UTF-8");
            if (utf8_bytes.length <= bytelen) {
                return strValue;
            }
            byte[] cutoff_bytes = new byte[real_bytelen];
            System.arraycopy(utf8_bytes, 0, cutoff_bytes, 0, real_bytelen);

            String strResult = new String(cutoff_bytes, "UTF-8");

            return strResult;

        } catch (Exception e) {
            if (strValue.length() < strlen) {
                return strValue;
            }
            return strValue.substring(0, strlen);
        }

    }

    /**
     * 对url进行编码�?
     * 
     * @param ori_url 要编码的url
     * @return 返回url
     */
    public static String urlEncode(String url) {
        try {
            String tempstr = URLEncoder.encode(url, "UTF-8");
            return tempstr.replaceAll("\\+", "%20");
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
            return url;
        }
    }

    /**
     * 编码url
     * 
     * @param ori_url
     * @return
     */
    public static String urlDecode(String url) {
        try {

            String tempstr = URLDecoder.decode(url.replaceAll("%20", "\\+"), "UTF-8");
            return tempstr;
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
            return url;
        }
    }

    /**
     * �?��字符串是否包含特殊字�?
     * 
     * @param str
     * @return
     */
    public static Boolean specialWord(String string) {
        String regEx = "[`~!@#$%^&*()+=|{}':;',\\[\\].<>/?~！@#�?…�?&*（）—�?+|{}【�?‘；：�?“�?。，、？]";
        return Pattern.compile(regEx).matcher(string).find();
    }

    /**
     * @param str
     * @return
     */
    public static Boolean startWithUpper(String string) {
        return START_UPPER_PATTERN.matcher(string).find();
    }

    /**
     * @param str
     * @return
     */
    public static Boolean startWithLower(String string) {
        return START_LOWER_PATTERN.matcher(string).find();
    }

    /**
     * @param str
     * @return
     */
    public static Boolean startWithNumber(String string) {
        return START_NUMBER_PATTERN.matcher(string).find();
    }

    /**
     * @param str
     * @return
     */
    public static Boolean containsLower(String string) {
        return LOWER_PATTERN.matcher(string).find();
    }

    /**
     * @param str
     * @return
     */
    public static Boolean containsUpper(String string) {
        return UPPER_PATTERN.matcher(string).find();
    }

    /**
     * @param str
     * @return
     */
    public static Boolean containsChinese(String string) {
        String regEx = "[\u2E80-\u9FFF]+$";
        return Pattern.compile(regEx).matcher(string).find();
    }

    /**
     * 密码不包含全部或部分的用户账户名 �?��str2中是否包含str中全部或部分的数�?
     * 
     * @param str
     * @param str2
     * @return
     */
    public static Boolean containsPartOrAll(String string, String string2) {
        if (StringUtils.isNotEmpty(string) && StringUtils.isNotEmpty(string2)) {
            return Pattern.compile("[" + string + "]").matcher(string2).find();
        }
        return false;
    }

    public static boolean containsSpace(String string) {
        return string.lastIndexOf(" ") != -1;
    }

    public static Boolean isNumber(String string) {
        return NUMBER_PATTERN.matcher(string).find();
    }

    /**
     * 返回字符串中包含的大写字母的
     * 
     * @param str
     * @return
     */
    public static int countUpper(String string) {
        int count = 0;
        for (int i = 0; i < string.toCharArray().length; i++) {
            if (containsUpper(String.valueOf(string.charAt(i)))) {
                count++;
            }
        }
        return count;
    }

    /**
     * @param str
     * @return
     */
    public static int countLower(String string) {
        int count = 0;
        for (int i = 0; i < string.toCharArray().length; i++) {
            if (containsLower(String.valueOf(string.charAt(i)))) {
                count++;
            }
        }
        return count;
    }

    public static int countNumber(String string) {
        int count = 0;
        for (int i = 0; i < string.toCharArray().length; i++) {
            if (isNumber(String.valueOf(string.charAt(i)))) {
                count++;
            }
        }
        return count;
    }

    /**
     * @param str
     * @return
     */
    public static int countSpecialWord(String string) {
        int count = 0;
        for (int i = 0; i < string.toCharArray().length; i++) {
            if (specialWord(String.valueOf(string.charAt(i)))) {
                count++;
            }
        }
        return count;
    }

    public static List<String> string2List(String string, String split) {
        String[] strs = {};
        if (string != null && !string.equals("")) {
            strs = string.split(split);
        }
        ArrayList<String> resultList = new ArrayList<>(0);
        for (int i = 0; i < strs.length; i++) {
            if (strs[i] != null && !strs[i].equals("")) {
                resultList.add(strs[i]);
            }
        }
        resultList.trimToSize();
        return resultList;
    }

    public static String list2String(List<String> list, String split) {
        String string = "";
        if (list == null) {
            return string;
        }
        for (int i = 0; i < list.size(); i++) {
            if (list.get(i) != null && !list.get(i).equals("")) {
                string += list.get(i) + split;
            }
        }
        if (string.length() > 0) {
            string = string.substring(0, string.length() - 1);
        }
        return string;
    }

    public static int parse2Integer(String string) {
        Integer value = 0;
        try {
            value = Integer.parseInt(string);
        } catch (Exception e) {
            throw new RuntimeException("parse " + string + " to  Integer error.");
        }
        return value;
    }

    /**
     * 处理如id=name形式的字符串
     * 
     * @param proValue
     * @param key
     * @param value
     * @return
     */
    public static Map<String, List<String>> processStr(String proValue, String key, String value) {
        Map<String, List<String>> map = new HashMap<>();
        List<String> idList = new ArrayList<>();
        List<String> nameList = new ArrayList<>();
        if (StringUtils.isNotEmpty(proValue)) {
            List<String> list = StrUtils.string2List(proValue, ",");
            for (String str : list) {
                idList.add(str.split("\\,")[0]);
                nameList.add(str.split("\\,")[1]);
            }
        }
        map.put(key, idList);
        map.put(value, nameList);
        return map;
    }

    /**
     * 获得集合对象中某�?��性字段的值，中间用split隔开
     * 
     * @param list     集合对象
     * @param propName 属�?名称
     * @param split    分隔�?
     * @return
     */
    @SuppressWarnings("rawtypes")
    public static String convertPropVal(List list, String propName, String split) {
        String retVal = "";
        for (Object obj : list) {
            if (StringUtils.isNotEmpty(retVal)) {
                retVal += split;
            }
            retVal += BeanUtil.getValue(obj, propName);
        }
        return retVal;
    }

    /**
     * 将对象类型的集合转换为String类型的集�?
     * 
     * @param list
     * @param propName
     * @return
     */
    @SuppressWarnings("rawtypes")
    public static List<String> convertList(List list, String propName) {
        List<String> strList = new ArrayList<>();
        for (Object obj : list) {
            strList.add(BeanUtil.getValue(obj, propName));
        }
        return strList;
    }

    /**
     * 将对象类型的集合转换为String类型的Map集合
     * 
     * @param list
     * @param propNames
     * @return
     */
    @SuppressWarnings("rawtypes")
    public static Map<String, List<String>> convertListToMap(List list, String... propNames) {
        Map<String, List<String>> map = new HashMap<>();
        for (String name : propNames) {
            List<String> strList = new ArrayList<>();
            for (Object obj : list) {
                strList.add(BeanUtil.getValue(obj, name));
            }
            map.put(name, strList);
        }
        return map;
    }

    public static String convertWeekStr(String str, String split) {
        String retVal = "";
        try {
            if (StringUtils.isNotEmpty(str)) {
                String[] array = str.split("\\" + split);
                for (int i = 0; i < array.length; i++) {
                    if (StringUtils.isNotEmpty(retVal)) {
                        retVal += split;
                    }
                    retVal += (Integer.parseInt(array[i]) + 1);
                }
            }
        } catch (NumberFormatException e) {
            e.printStackTrace();
        }
        return retVal;
    }

    public static List<String> convertToSingleList(List<String> list) {
        return convertToSingleList(list, ",");
    }

    public static List<String> convertToSingleList(List<String> list, String reg) {
        List<String> result = new ArrayList<String>();
        if (list != null && list.size() > 0) {
            for (String str : list) {
                String[] arrStr = str.split(reg);
                for (int i = 0; i < arrStr.length; i++) {
                    result.add(arrStr[i]);
                }
            }
        }
        return result;
    }

    /**
     * 汉字转换位汉语拼音
     * 
     * @param hanYu Chinese
     * @param first true is Convert first,else all
     * @return 拼音
     */
    /*
     * public static String hanYu2Pinyin(String hanYu,boolean first){ String pinyin
     * = ""; char[] nameChar = hanYu.toCharArray(); HanyuPinyinOutputFormat
     * defaultFormat = new HanyuPinyinOutputFormat();
     * defaultFormat.setCaseType(HanyuPinyinCaseType.LOWERCASE);
     * defaultFormat.setToneType(HanyuPinyinToneType.WITHOUT_TONE); for (int i = 0;
     * i < nameChar.length; i++) { if (nameChar[i] > 128) { try { if(first){ pinyin
     * += PinyinHelper.toHanyuPinyinStringArray(nameChar[i],
     * defaultFormat)[0].charAt(0); }else{ pinyin +=
     * PinyinHelper.toHanyuPinyinStringArray(nameChar[i], defaultFormat)[0]; } }
     * catch (BadHanyuPinyinOutputFormatCombination e) { e.printStackTrace(); }
     * }else{ pinyin += nameChar[i]; } } return pinyin; }
     */
    public static Map<String, String> aduserName2Map(String activeDirectoryUserName) {
        if (StringUtils.isEmpty(activeDirectoryUserName)) {
            return null;
        }
        Map<String, String> map = new HashMap<String, String>();
        int index = 0;
        if ((index = activeDirectoryUserName.indexOf("\\")) > 0) {
            map.put("domain", activeDirectoryUserName.substring(0, index));
            map.put("userName", activeDirectoryUserName.substring(index + 1, activeDirectoryUserName.length()));
        } else if ((index = activeDirectoryUserName.indexOf("@")) > 0) {
            map.put("userName", activeDirectoryUserName.substring(0, index));
            map.put("domain", activeDirectoryUserName.substring(index + 1));
        } else {
            map.put("userName", activeDirectoryUserName);
        }
        return map;
    }

    /**
     * 验证是否为正确的邮箱号
     *
     * @param email
     * @return
     */
    public static boolean isValidEmail(String email) {
        // 1、\\w+表示@之前至少要输入一个匹配字母或数字或下划线 \\w 单词字符：[a-zA-Z_0-9]
        // 2、(\\w+\\.)表示域名. 如新浪邮箱域名是sina.com.cn
        // {1,3}表示可以出现一次或两次或者三次.
        String reg = "\\w+@(\\w+\\.){1,3}\\w+";
        Pattern pattern = Pattern.compile(reg);
        boolean flag = false;
        if (email != null) {
            Matcher matcher = pattern.matcher(email);
            flag = matcher.matches();
        }
        return flag;
    }
    /**
     * 验证是否为手机号
     *
     * @param mobileNo
     * @return
     */
    public static boolean isValidMobileNo(String mobileNo) {
        // 1、(13[0-9])|(15[02789])|(18[679])|(17[0-9]) 13段 或者15段 18段17段的匹配
        // 2、\\d{8} 整数出现8次
        boolean flag = false;
        Matcher match = MOBILE_PATTERN.matcher(mobileNo);
        if (mobileNo != null) {
            flag = match.matches();
        }
        return flag;
    }

    public static List<String> sqlInjection = null;
    
    static{
        sqlInjection = new ArrayList<>();
        sqlInjection.add("--");
        sqlInjection.add(";");
        sqlInjection.add("/");
        sqlInjection.add("\\");
        sqlInjection.add("#");
        sqlInjection.add("drop");
        sqlInjection.add("create");
        sqlInjection.add("delete");
        sqlInjection.add("alter");
        sqlInjection.add("truncate");
        sqlInjection.add("update");
        sqlInjection.add("insert");
        sqlInjection.add("and");
        sqlInjection.add("or");
    }
    
    public static boolean filtersSQLInjection(String filters) {
        for(String s : sqlInjection) {
            if(filters.indexOf(s)>-1) {
                return true;
            }
        }
        return false;
    }
    
    /**
	 * 诸葛亮 zhugeliang
	 * @param name
	 * @return
	 */
	public static String getPinYinName(String name){
		try {
	        HanyuPinyinOutputFormat pinyinFormat = new HanyuPinyinOutputFormat();
	        pinyinFormat.setCaseType(HanyuPinyinCaseType.LOWERCASE);
	        pinyinFormat.setToneType(HanyuPinyinToneType.WITHOUT_TONE);
	        pinyinFormat.setVCharType(HanyuPinyinVCharType.WITH_V);
	        return PinyinHelper.toHanYuPinyinString(name, pinyinFormat, "",false);
		}catch(Exception e) {
			e.printStackTrace();
		}
		return "";
    }
	
	/**
	 * 诸葛亮 zhugl
	 * @param name
	 * @return
	 */
	public static String getPinYinShortName(String name){
		try {
			char[] strs = name.toCharArray();
			StringBuilder pinyinName = new StringBuilder("");
			for(int i=0;i<strs.length;i++) {
				if(i == 0) {
					pinyinName.append(getPinYinName(strs[i]+""));
				}else {
					pinyinName.append(getPinYinName(strs[i]+"").charAt(0));
				}
			}
			return pinyinName.toString();
		}catch(Exception e) {
			e.printStackTrace();
		}
		return "";
    }
	
	public static String getPinYinShort(String name){
		try {
			char[] strs = name.toCharArray();
			StringBuilder pinyinName = new StringBuilder("");
			for(int i=0;i<strs.length;i++) {
				pinyinName.append(getPinYinName(strs[i]+"").charAt(0));
			}
			return pinyinName.toString();
		}catch(Exception e) {
			e.printStackTrace();
		}
		return "";
    }
    
}

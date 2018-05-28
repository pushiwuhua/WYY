package com.orieange.wcounter;

import android.text.SpannableStringBuilder;

import com.orieange.wcounter.data.StockPool;

import org.junit.Test;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

import static org.junit.Assert.*;

/**
 * Example local unit test, which will execute on the development machine (host).
 *
 * @see <a href="http://d.android.com/tools/testing">Testing documentation</a>
 */
public class ExampleUnitTest {
    @Test
    public void addition_isCorrect() throws Exception {
        assertEquals(4, 2 + 2);
    }

    @Test
    public void testMatcher() {
        String text = "var hq_str_s_sz000598=\"兴蓉环境,5.70,0.00,0.00,184854,10549\";\n" +
                "var hq_str_s_sz000001=\"平安银行,10.89,0.08,0.74,1501020,162541\";";
        Matcher matcher = Pattern.compile("000598=\"\\S+\"", Pattern.CASE_INSENSITIVE).matcher(text);
        while (matcher.find()) {
            System.out.println("TextUtil testMatcher:" + matcher.group(0) + "," + matcher.regionStart() + "," + matcher.regionEnd());
            System.out.println("TextUtil testMatcher:" + matcher.start() + "," + matcher.end());
            String content = matcher.group(0);
            String subContent = content.substring(content.indexOf("\"")+1, content.lastIndexOf("\""));
            System.out.println(subContent);
        }
    }
    @Test
    public void testString() {
        String s = String.format("%-20.2f",0.01f);
        System.out.println(s + ",length:" + s.length());
        String s1 = String.format("%-20.2f",0.001f);
        System.out.println(s1 + ",length:" + s1.length());
    }
}
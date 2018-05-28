package com.orieange.wcounter;

import android.content.Context;
import android.support.test.InstrumentationRegistry;
import android.support.test.runner.AndroidJUnit4;
import android.text.SpannableStringBuilder;

import com.orieange.wcounter.data.StockPool;

import org.junit.Test;
import org.junit.runner.RunWith;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

import static org.junit.Assert.*;

/**
 * Instrumentation test, which will execute on an Android device.
 *
 * @see <a href="http://d.android.com/tools/testing">Testing documentation</a>
 */
@RunWith(AndroidJUnit4.class)
public class ExampleInstrumentedTest {
    @Test
    public void useAppContext() throws Exception {
        // Context of the app under test.
        Context appContext = InstrumentationRegistry.getTargetContext();

        assertEquals("com.orieange.wcounter", appContext.getPackageName());
    }

    @Test
    public void testMatcher() {
        AiLog.i(AiLog.TAG_WZZ, "TextUtildd testMatcher");
        Matcher matcher = Pattern.compile("你好", Pattern.CASE_INSENSITIVE).matcher("你好你好你好你好你好");
        while (matcher.find()) {
            AiLog.i(AiLog.TAG_WZZ, "TextUtil testMatcher:" + matcher.group(0) + "," + matcher.regionStart() + "," + matcher.regionEnd());
            AiLog.i(AiLog.TAG_WZZ, "TextUtil testMatcher:" + matcher.start() + "," + matcher.end());
        }
    }

    @Test
    public void testCount() {
        SpannableStringBuilder sb = StockPool.quickCountStockPrice(5.76);
        AiLog.i(AiLog.TAG_WZZ, "ExampleInstrumentedTest testCount:" + sb);
    }
}

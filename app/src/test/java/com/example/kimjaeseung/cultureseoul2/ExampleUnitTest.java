package com.example.kimjaeseung.cultureseoul2;

import org.junit.Test;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * Example local unit test, which will execute on the development machine (host).
 *
 * @see <a href="http://d.android.com/tools/testing">Testing documentation</a>
 */
public class ExampleUnitTest {
    @Test
    public void testDateModule() throws Exception {

        String yesterday = "2017-08-07";

//        assertThat(result,is("내일"));
    }

    @Test
    public void testDateType() throws Exception {

        String s1="20170825";
        String s2="20170830";

        String s3="20170823";

        DateFormat df = new SimpleDateFormat("yyyyMMdd");

        Date d1 = df.parse( s1 );
        Date d2 = df.parse( s2 );
        Date d3 = df.parse(s3);

        if(d3.compareTo(d1) > 0)
            System.out.println("d3 > d1");
        else
            System.out.println("d3 < d1");

        if(d3.compareTo(d2) < 0)
            System.out.println("d3 < d2");
        else
            System.out.println("d3 > d2");

        if(d2.compareTo(d1) < 0)
            System.out.println("d2 < d1");
        else
            System.out.println("d2 > d1");

        System.out.println(d1.toString());
        System.out.println(d2.toString());
        System.out.println(d3.toString());

    }
}
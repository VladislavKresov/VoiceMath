package com.example.voiceinputmath;

import org.junit.Test;

import static com.example.voiceinputmath.Formatter.formatLine;
import static com.example.voiceinputmath.Formatter.regEx;
import static org.junit.Assert.*;

/**
 * Example local unit test, which will execute on the development machine (host).
 *
 * @see <a href="http://d.android.com/tools/testing">Testing documentation</a>
 */
public class ExampleUnitTest {
    @Test
    public void addition_isCorrect() {
        assertEquals(4, 2 + 2);
    }

    @Test
    public void test_Formatter(){
        System.out.println(formatLine(" 1 2 x ^ 2 + 1 3 x -v 1 4=0"));
        System.out.println(regEx(formatLine("12x^2+13x-14=0"),Formatter.NUMERAL_PATTERN));
        System.out.println(regEx(formatLine("12x^2+13x+14=0"),Formatter.LINEAR_PATTERN));
        System.out.println(regEx(formatLine("12x^2+13x+14=0"),Formatter.POWERED_PATTERN));

    }

    @Test
    public void test_reduce(){
        Solver test = new Solver(formatLine("1x-6x=2"));
        System.out.println(test.linear(test.equation));
    }

    @Test
    public void test_quadratic(){
        Solver test = new Solver(formatLine("x^2=25"));
        System.out.println(test.powered(test.equation));
    }
}
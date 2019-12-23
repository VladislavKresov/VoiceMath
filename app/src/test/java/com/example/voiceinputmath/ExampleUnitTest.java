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
        Solver test = new Solver(formatLine("3x^3+2x^2+1x+5+3x^3+2x^2+1x+5=0"));
        System.out.println(test.reduce(test.equation));
    }
}
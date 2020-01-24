package com.example.voiceinputmath;

import java.util.ArrayList;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class Formatter {
    public static final String NUMERAL_PATTERN = "(\\+|\\-)\\d+\\s";
    public static final String LINEAR_PATTERN = "(\\+|\\-)\\d*x\\s";
    public static final String POWERED_PATTERN = "(\\+|\\-)\\d+x\\^\\d+\\s";
    public static final String POW_PATTERN = "\\^\\d+";

    public static ArrayList<String> regEx(String line, String pattern){
        ArrayList<String> founds = new ArrayList<>();
        Pattern regex = Pattern.compile(pattern);
        Matcher matcher = regex.matcher(line);
        while (matcher.find()) {
            founds.add(line.substring(matcher.start(), matcher.end()).replace(" ",""));
        }
        return founds;
    }

    public static String formatLine(String line) {

        line = line.replaceAll(" ","");

        if(line.charAt(0)!='+' && line.charAt(0)!='-') line = "+"+line;

        for (int i = 0; i < line.length()-1; i++) {
            if(line.charAt(i)=='=' && line.charAt(i+1)!='+' && line.charAt(i+1)!='-')
                line = line.substring(0,i+1)+"+"+line.substring(i+1);
        }

        line = line.replaceAll("((И|и)кс|(X|x))","x");
        line = line.replaceAll("(В|в)степени","^");
        line = line.replaceAll("(К|к)вадрат","^2");

        line = line.replaceAll("(П|п)люс","+");
        line = line.replaceAll("(М|м)инус","-");
        line = line.replaceAll("(Н|н)(о|у)л(ь|ю)","0");
        line = line.replaceAll("(О|о)дин","1");
        line = line.replaceAll("(Д|д)ва","2");
        line = line.replaceAll("(Т|т)ри","3");
        line = line.replaceAll("(Ч|ч)етыре","4");
        line = line.replaceAll("(П|п)ять","5");
        line = line.replaceAll("(Ш|ш)есть","6");
        line = line.replaceAll("(С|с)емь","7");
        line = line.replaceAll("(В|в)осемь","8");
        line = line.replaceAll("(Д|д)евять","9");
        line = line.replaceAll("(Р|р)авно","=");

        line = line.replaceAll("[a-w]","");
        line = line.replaceAll("[y-z]","");
        line = line.replaceAll("[A-W]","");
        line = line.replaceAll("[Y-Z]","");
        line = line.replaceAll("[а-я]","");
        line = line.replaceAll("[А-Я]","");
        line = line.replaceAll("\\+"," +");
        line = line.replaceAll("-"," -");
        line = line.replaceAll("="," = ");
        line = line.replaceAll("(-\\+|\\+-)"," -");
        line = line.replaceAll("(\\+\\+|--)"," +");
        line = line.replaceAll("\\^1","");


        for (int i = 0; i < line.length() - 1; i++) {
            if((line.charAt(i)=='+' || line.charAt(i)=='-') && line.charAt(i+1)=='x')
                line = line.substring(0,i+1)+"1"+line.substring(i+1);

        }

        line += " ";
        return line;
    }

    public static String output(String line){

        line = line.replaceAll(" ","");
        line = line.replaceAll("1x","x");
        if(line.charAt(0)=='+') line = line.substring(1);

        return line;
    }


}

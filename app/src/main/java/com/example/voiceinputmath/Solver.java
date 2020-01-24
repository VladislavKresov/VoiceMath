package com.example.voiceinputmath;

import java.util.ArrayList;

public class Solver {

    public String equation;

    public Solver(String equation){
        this.equation = equation;
    }

    private String flip(String line){
        for (int i = 0; i < line.length(); i++) {
            if(line.charAt(i)=='+')
                line = line.substring(0,i) + "-" + line.substring(i+1);
            else if(line.charAt(i)=='-')
                line = line.substring(0,i) + "+" + line.substring(i+1);
        }
        return line;
    }

    public String reduce(String equation){

        String answer = "";

        if(equation.contains("="))
        {
            for (int i = 0; i < equation.length()-1; i++) {
                if(equation.charAt(i)=='=')
                    equation = equation.substring(0,i)+flip(equation.substring(i+1));
            }
        }

        ArrayList<String> numeral = Formatter.regEx(equation, Formatter.NUMERAL_PATTERN);
        ArrayList<String> linear = Formatter.regEx(equation, Formatter.LINEAR_PATTERN);
        ArrayList<String> powered = Formatter.regEx(equation, Formatter.POWERED_PATTERN);

        String numbers ="0";
        for (String i:numeral) {
            numbers = Integer.parseInt(numbers)+Integer.parseInt(i)+"";
        }

        String linears = "0";
        for (String i:linear) {
            linears = Integer.parseInt(linears)+Integer.parseInt(i.replace("x",""))+"";
        }
        linears+="x";

        for (int i = 0; i < powered.size()-1; i++) {
            for (int j = i+1; j < powered.size(); j++) {
                if(Formatter.regEx(powered.get(i),Formatter.POW_PATTERN).get(0).equals(Formatter.regEx(powered.get(j),Formatter.POW_PATTERN).get(0))){
                    String pow = Formatter.regEx(powered.get(i),Formatter.POW_PATTERN).get(0);
                    String num1 = powered.get(i).replace("x"+pow,"");
                    String num2 = powered.get(j).replace("x"+pow,"");

                    powered.set(i, Integer.parseInt(num1)+Integer.parseInt(num2)+"x"+pow);
                    powered.remove(j);
                }
            }
        }

        for (String i:powered) {
            if(!i.replace("x"+Formatter.regEx(i,Formatter.POW_PATTERN).get(0),"").equals("0"))
            answer += Formatter.formatLine(i);
        }
        if(Integer.parseInt(linears.replace("x",""))!=0)
            answer += Formatter.formatLine((linears));

        answer += Formatter.formatLine(numbers);

        if(answer.equals(""))
            answer+="0";

        return Formatter.formatLine(answer);
    }

    public String linear(String equation) {
        equation = reduce(equation);
        if(equation.contains("x") && !Formatter.regEx(equation,Formatter.NUMERAL_PATTERN).isEmpty()) {
            int coef = Integer.parseInt(Formatter.regEx(equation, Formatter.LINEAR_PATTERN).get(0).replace("x", ""));
            int val = -Integer.parseInt(Formatter.regEx(equation, Formatter.NUMERAL_PATTERN).get(0));
            double x = (double) val / coef;
            if(x-(int)x==0)
                equation = "x=" + (int)x;
            else
                equation = "x=" + val + "/" + coef;
        }

        return Formatter.output(equation);
    }

    public String quadratic(String equation){
            String answer;

            equation = reduce(equation);

            int a;
            int b=0;
            int c=0;

            a = Integer.parseInt(Formatter.regEx(equation,Formatter.POWERED_PATTERN).get(0).replaceAll("x\\^\\d",""));
            if(!Formatter.regEx(equation,Formatter.LINEAR_PATTERN).isEmpty())
                b = Integer.parseInt(Formatter.regEx(equation,Formatter.LINEAR_PATTERN).get(0).replaceAll("x",""));
            if(!Formatter.regEx(equation,Formatter.NUMERAL_PATTERN).isEmpty())
                c = Integer.parseInt(Formatter.regEx(equation,Formatter.NUMERAL_PATTERN).get(0));

            int discriminant = b*b-4*a*c;
            answer = "D="+discriminant + "\n";
            if(discriminant<0)
                answer += "Нет решений для действительных чисел";
            else
                if(discriminant == 0){
                    double x =(double) -b/2*a;
                    if((x-(int)x)%10==0)
                        answer += "Один действительный корень: "+x;
                    else
                        answer += "Один действительный корень: "+-b+"/"+2*a;
                }
                else {
                    double x1 = (-b-Math.sqrt(discriminant))/2*a;
                    double x2 = (-b+Math.sqrt(discriminant))/2*a;
                    answer += "Два действительных корня:\n";

                    answer+="x=";

                    if((x1-(int)x1)%10==0)
                        answer+=(int)x1;
                    else
                    {
                        if((-b-Math.sqrt(discriminant))%10==0)
                            answer +=(int)(-b-Math.sqrt(discriminant)) + "/" + 2*a;
                        else answer += "(" + -b + " - " + "-/"+discriminant+" )" + "/" +2*a;
                    }

                    answer+="\nи\nx=";

                    if((x2-(int)x2)%10==0)
                        answer+=(int)x2;
                    else
                    {
                        if((-b-Math.sqrt(discriminant))%10==0)
                            answer += (int)(-b+Math.sqrt(discriminant)) + "/" + 2*a;
                        else answer += "(" + -b + " + " + "-/"+discriminant+" )" + "/" +2*a;
                    }
                }

                return answer;
    }

    public String powered(String equation){
        equation = reduce(equation);
        ArrayList<String> powers = Formatter.regEx(equation,Formatter.POW_PATTERN);
        if(!powers.isEmpty()) {
            int maxPow = Integer.parseInt(powers.get(0).replace("^", ""));
            for (int i = 0; i < powers.size(); i++) {
                int pow = Integer.parseInt(powers.get(i).replace("^", ""));
                if (pow > maxPow)
                    maxPow = pow;
            }

            if (maxPow == 2)
                return Formatter.output(equation)+"=0" + "\n" + quadratic(equation);
            else
                equation += "=0";
        }
        else
            equation = linear(equation);
        return Formatter.output(equation);
    }

}

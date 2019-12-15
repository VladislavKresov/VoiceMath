package com.example.voiceinputmath;

public class Solver {

    public static String linear(String equation){
        int n = equation.length(), sign = 1, coeff = 0;
        int total = 0, i = 0;

        for (int j = 0; j < n; j++)
        {
            if (equation.charAt(j) == '+' || equation.charAt(j) == '-')
            {
                if (j > i)
                    total += sign * Integer.parseInt(equation.substring(i, j));
                i = j;
            }

            // для: x, -x, +x
            else if (equation.charAt(j) == 'x')
            {
                if ((i == j) || equation.charAt(j - 1) == '+')
                    coeff += sign;

                else if (equation.charAt(j - 1) == '-')
                    coeff -= sign;

                else
                    coeff += sign * Integer.parseInt(equation.substring(i, j));
                i = j + 1;
            }

            else if (equation.charAt(j) == '=')
            {
                if (j > i)
                    total += sign * Integer.parseInt(equation.substring(i, j));
                sign = -1;
                i = j + 1;
            }
        }

        if (i < n)
            total = total + sign * Integer.parseInt(equation.substring(i));

        if (coeff == 0 && total == 0)
            return "Inf";

        if (coeff == 0 && total != 0)
            return "No";

        int ans = -total / coeff;
        return (Integer.toString(ans));
    }

    public static String quadratic(String equation){
            if(equation.contains("=")) {
            int a = 0, b = 0, c = 0;
            int i = 0, n = equation.length();
            int signA = 1, signB = 1, signC = 1;

            for (int j = 0; j < n; j++) {
                if (equation.charAt(j) == '+' || equation.charAt(j) == '-')
                {
                    if (j > i)
                        c += signC * Integer.parseInt(equation.substring(i, j));
                    i = j;
                }
                // для: x^2, -x^2, +x^2, x, -x, +x
                else if (equation.charAt(j) == 'x')
                {
                    if(j+2<n)
                    {
                        if (equation.charAt(j + 1) == '^' && equation.charAt(j + 2) == '2') {
                            if ((i == j) || equation.charAt(j - 1) == '+')
                                a += signA;

                            else if (equation.charAt(j - 1) == '-')
                                a -= signA;

                            else
                                a += signA * Integer.parseInt(equation.substring(i, j));
                            j+=2;
                            i = j + 1;
                        } else {
                            if ((i == j) || equation.charAt(j - 1) == '+')
                                b += signB;

                            else if (equation.charAt(j - 1) == '-')
                                b -= signB;

                            else
                                b += signB * Integer.parseInt(equation.substring(i, j));
                            i = j + 1;
                        }
                    }
                    else {
                        if ((i == j) || equation.charAt(j - 1) == '+')
                            b += signB;

                        else if (equation.charAt(j - 1) == '-')
                            b -= signB;

                        else
                            b += signB * Integer.parseInt(equation.substring(i, j));
                        i = j + 1;
                    }
                } else if (equation.charAt(j) == '=')
                {
                    if (j > i)
                        c += signC * Integer.parseInt(equation.substring(i, j));
                    signC = -1;
                    i = j + 1;
                }
            }
            if (i < n)
                c = c + signC * Integer.parseInt(equation.substring(i));

            if(a == 0)
                throw new IllegalArgumentException();
            else {
                String ans = "";
                if((b*b-4*a*c)<0){
                    ans+="No";
                } else {
                    double x1 = (-b + Math.sqrt(b * b - 4 * a * c)) / 2 * a;
                    double x2 = (-b - Math.sqrt(b * b - 4 * a * c)) / 2 * a;
                    if (x1 == x2) {
                        ans+="x="+x1;
                    } else {
                        ans+=x1+"or"+x2;
                    }
                }
                return ans;
            }

        } else throw new IllegalArgumentException();
    }

    public static String powered(String equation){
        int i = 0, total = 0, coeff = 0, sign = 1, power = 1;
        int N = equation.length();
        for (int j = 0; j < N; j++) {
            if (equation.charAt(j) == '+' || equation.charAt(j) == '-')
            {
                if (j > i)
                    total += sign * Integer.parseInt(equation.substring(i, j));
                i = j;
            }

            // для: x, -x, +x
            else if (equation.charAt(j) == 'x')
            {
                if ((i == j) || equation.charAt(j - 1) == '+')
                    coeff += sign;

                else if (equation.charAt(j - 1) == '-')
                    coeff -= sign;

                else
                    coeff += sign * Integer.parseInt(equation.substring(i, j));
                i = j + 1;
            }

            else if (equation.charAt(j) == ')')
            {
                if (j > i)
                    total += sign * Integer.parseInt(equation.substring(i, j));
                i = j + 2;
                break;
            }
        }
        if (i < N)
            power = Integer.parseInt(equation.substring(i,N));

        if (coeff == 0){
            return "" + Math.pow(total,power);
        }

        if (coeff != 0 && total == 0){
            return "x^"+power;
        }

        if(coeff!=0){
            String answer = "";
             int n = power;
            for (int m = 0; m <= n; m++) {
                answer+=(factorial(n)/(factorial(m)*factorial(n-m)))*((int)Math.pow(coeff,n-m))*((int)Math.pow(total,m)) + "x^"+(n-m);
                if(m<n)
                    answer+=" + ";
            }
            return answer;
        }

        return null;

    }

    public static int factorial(int n) {
        if (n == 0) return 1;
        return n * factorial(n-1);
    }
}

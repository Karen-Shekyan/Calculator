import java.io.*;
import java.util.*;

//Calculator built for annoying processes in math (think MAD and similar).
//First arg is the function (NO SPACES). Subraction uses "_" to avoid confusion with negative numbers.
//Second arg is the operation (camel case where needed, remove punctuation).
//There is rounding error. Severity depends on the function and the value of x. (Possibly due to overflow and floating points. Not worth fixing rn)
//Currently implemented:
// -Numerical methods of integration
//   -Trapezoidal Rule (trapezoidalRule)
//   -Midpoint Rule    (midpointRule)
//   -Simpson's Rule   (simpsonsRule)
//
//=============== WORK IN PROGRESS ===============
// -Methods of approximation:
//   -Euler's Method   (eulersMethod)

public class calculator {
//creates and returns polynomial data structure. Has same time/space complexity as recursion...
  public static ArrayList<ArrayList<ArrayList<ArrayList<ArrayList<String>>>>> funcBreaker (String func) {
    String[] add = func.split("\\+");

    ArrayList<ArrayList<String>> sub = new ArrayList<ArrayList<String>>();
    for (int i = 0; i < add.length; i++) {
      String[] r1 = add[i].split("_");
      ArrayList<String> row1 = new ArrayList<String>(Arrays.asList(r1));
      sub.add(row1);
    }

    ArrayList<ArrayList<ArrayList<String>>> mul = new ArrayList<ArrayList<ArrayList<String>>>();
    for (int i = 0; i < add.length; i++) {
      ArrayList<ArrayList<String>> row2 = new ArrayList<ArrayList<String>>();
      for (int j = 0; j < sub.get(i).size(); j++) {
        String[] r1 = sub.get(i).get(j).split("\\*");
        ArrayList<String> row1 = new ArrayList<String>(Arrays.asList(r1));
        row2.add(row1);
      }
      mul.add(row2);
    }

    ArrayList<ArrayList<ArrayList<ArrayList<String>>>> div = new ArrayList<ArrayList<ArrayList<ArrayList<String>>>>();
    for (int i = 0; i < add.length; i++) {
      ArrayList<ArrayList<ArrayList<String>>> row3 = new ArrayList<ArrayList<ArrayList<String>>>();
      for (int j = 0; j < sub.get(i).size(); j++) {
        ArrayList<ArrayList<String>> row2 = new ArrayList<ArrayList<String>>();
        for (int k = 0; k < mul.get(i).get(j).size(); k++) {
          String[] r1 = mul.get(i).get(j).get(k).split("/");
          ArrayList<String> row1 = new ArrayList<String>(Arrays.asList(r1));
          row2.add(row1);
        }
        row3.add(row2);
      }
      div.add(row3);
    }

    ArrayList<ArrayList<ArrayList<ArrayList<ArrayList<String>>>>> data = new ArrayList<ArrayList<ArrayList<ArrayList<ArrayList<String>>>>>();
    for (int i = 0; i < add.length; i++) {
      ArrayList<ArrayList<ArrayList<ArrayList<String>>>> row4 = new ArrayList<ArrayList<ArrayList<ArrayList<String>>>>();
      for (int j = 0; j < sub.get(i).size(); j++) {
        ArrayList<ArrayList<ArrayList<String>>> row3 = new ArrayList<ArrayList<ArrayList<String>>>();
        for (int k = 0; k < mul.get(i).get(j).size(); k++) {
          ArrayList<ArrayList<String>> row2 = new ArrayList<ArrayList<String>>();
          for (int l = 0; l < div.get(i).get(j).get(k).size(); l++) {
            String[] r1 = div.get(i).get(j).get(k).get(l).split("\\^");
            ArrayList<String> row1 = new ArrayList<String>(Arrays.asList(r1));
            row2.add(row1);
          }
          row3.add(row2);
        }
        row4.add(row3);
      }
      data.add(row4);
    }

    return data;
  }

//evaluates polynomial data structure, returning its value.
  public static double doPolymonial (ArrayList<ArrayList<ArrayList<ArrayList<ArrayList<String>>>>> data) {
    Double ans = 0.0;
    for (int i = 0; i < data.size(); i++) {
      Double ans1 = 0.0;
      for (int j = 0; j < data.get(i).size(); j++) {
        Double ans2 = 0.0;
        for (int k = 0; k < data.get(i).get(j).size(); k++) {
          Double ans3 = 0.0;
          for (int l = 0; l < data.get(i).get(j).get(k).size(); l++) {
            Double ans4 = 0.0;
            for (int m = 0; m < data.get(i).get(j).get(k).get(l).size(); m++) {
              Double num = Double.parseDouble(data.get(i).get(j).get(k).get(l).get(m));
              if (m == 0) {
                ans4 += num;
              }
              else {
                ans4 = Math.pow(ans4,num);
              }
            }
            if (l == 0) {
              ans3 += ans4;
            }
            else {
              ans3 /= ans4;
            }
          }
          if (k == 0) {
            ans2 += ans3;
          }
          else {
            ans2 *= ans3;
          }
        }
        if (j == 0) {
          ans1 += ans2;
        }
        else {
          ans1 -= ans2;
        }
      }
      ans += ans1;
    }

    return ans;
  }

//does the function, calling polynomial evaluator.
  public static Double doFunc (String func, Double x) {
    //replace x
    while (func.indexOf("x") != -1) {
      int place = func.indexOf("x");
      func = func.substring(0,place) + x + func.substring(place+1);
    }
    //parse for e
    for (int i = 0; i < func.length(); i++) {
      if (func.charAt(i) == 'e') {
        //make sure it's 'e', not 'sec' and such
        if (i == 0) {
          if ("+_*/^(".contains(""+func.charAt(i+1))) {
            func = func.substring(0,i) + 2.718281828459045 + func.substring(i+1);
          }
        }
        else if (i == func.length()-1) {
          if ("+_*/^g)".contains(""+func.charAt(i-1))) {
            func = func.substring(0,i) + 2.718281828459045 + func.substring(i+1);
          }
        }
        else {
          if ("+_*/^g(".contains(""+func.charAt(i-1)) && "+_*/^)(".contains(""+func.charAt(i+1))) {
            func = func.substring(0,i) + 2.718281828459045 + func.substring(i+1);
          }
        }
      }
    }

    //parse for pi
    for (int i = 0; i < func.length()-1; i++) {
      if (func.substring(i,i+2).equals("pi")) {
        func = func.substring(0,i) + Math.PI + func.substring(i+2);
      }
    }

    //must pad function so it does final step
    func = "(" + func + ")";
    while (func.indexOf(")") != -1) {
      int end = func.indexOf(")");
      int start = 0;
      for (int i = end-1; i >= 0; i--) {
        if (func.charAt(i) == '(') {
          start = i;
          break;
        }
      }
      String poly = func.substring(start+1,end);
      Double num = doPolymonial(funcBreaker(poly));
      String prefix = "";
      if (start >= 3) {
        prefix = func.substring(start-3,start);
      }

      //prefix operators (ie sin(), log_(), etc...)
      if (start != 0 && Character.isDigit(func.charAt(start-1))) {//logarithm check
        int startlog = 0;
        String base = "";
        for (int i = start-1; i >= 0; i--) {
          char current = func.charAt(i);
          if (Character.isDigit(current) || current == '.') {
            base = current + base;
          }
          else {
            startlog = i-2;
            break;
          }
        }
        Double b = Double.parseDouble(base);
        num = Math.log(num)/Math.log(b);
        func = func.substring(0,startlog) + num + func.substring(end+1);
      }
      else if (prefix.equals("sin")) {
        num = Math.sin(num);
        func = func.substring(0,start-3) + num + func.substring(end+1);
      }
      else if (prefix.equals("cos")) {
        num = Math.cos(num);
        func = func.substring(0,start-3) + num + func.substring(end+1);
      }
      else if (prefix.equals("tan")) {
        num = Math.tan(num);
        func = func.substring(0,start-3) + num + func.substring(end+1);
      }
      else if (prefix.equals("sec")) {
        num = 1/Math.cos(num);
        func = func.substring(0,start-3) + num + func.substring(end+1);
      }
      else if (prefix.equals("csc")) {
        num = 1/Math.sin(num);
        func = func.substring(0,start-3) + num + func.substring(end+1);
      }
      else if (prefix.equals("cot")) {
        num = 1/Math.tan(num);
        func = func.substring(0,start-3) + num + func.substring(end+1);
      }
      else {
        func = func.substring(0,start) + num + func.substring(end+1);
      }
    }

    return Double.parseDouble(func);
  }

//running this with the commented function yeilds increasingly large errors as x decreases. May be due to this (   v    ) term.
  // public static void main(String[] args) throws IOException {//(301.2+log10((x*9^3)/(x*sin(pi/8)+30_x^4)))/(9999*x^-1+0.9*1/x_e)_0
  //   BufferedReader f = new BufferedReader(new InputStreamReader(System.in));
  //   String function = f.readLine();
  //   String input = f.readLine();
  //   Double x = Double.parseDouble(input);
  //   System.out.println(doFunc(function,x));
  // }

  public static double trapezoidalRule(String func, double a, double b, int n) {
    double ans = 0;
    for (int i = 0; i <= n; i++) {
      double x = a + i*(b-a)/n;
      double y = doFunc(func,x);
      if (i == 0 || i == n) {
        ans += y;
      }
      else {
        ans += 2*y;
      }
    }
    ans = ans * (b-a)/(2*n);
    return ans;
  }

  public static double simpsonsRule(String func, double a, double b, int n) {
    if (n % 2 != 0) {
      throw new IllegalArgumentException("n must be even to apply simpsonsRule");
    }
    else {
      double ans = 0;
      for (int i = 0; i <= n; i++) {
        double x = a + i*(b-a)/n;
        double y = doFunc(func,x);
        if (i == 0 || i == n) {
          ans += y;
        }
        else if (i % 2 == 1) {
          ans += 4*y;
        }
        else {
          ans += 2*y;
        }
      }
      ans = ans * (b-a)/(3*n);
      return ans;
    }
  }

  public static double midpointRule(String func, double a, double b, int n) {
    double ans = 0;
    for (int i = 0; i < n; i++) {
      double x = (i*(b-a)/n + (i+1)*(b-a)/n) / 2 + a;
      double y = doFunc(func,x);
      ans += y;
    }
    ans = ans * (b-a)/n;
    return ans;
  }

  public static void main(String[] args) throws IOException {//maybe catch this instead?


    BufferedReader f = new BufferedReader(new InputStreamReader(System.in));
    System.out.println("Enter your function. Do not use spaces. Use \"_\" for subtraction. Parenthesis do not imply multiplication.\nDO NOT use ln for natural logarithm, enter \"loge(x)\"");
    String function = f.readLine();
    System.out.println("Enter the implemented operation you would like to preform. If you would like to simply compute the function at x,\npress enter.");
    String operation = f.readLine();


    // if (operation.equals("eulersMethod")) {
    //   System.out.println("Enter the initial value of x. This must be a number, even if the function does not depend on x.");
    //   double x0 = Double.parseDouble(f.readLine());
    //   System.out.println("Enter the initial value of y. This must be a number, even if the function does not depend on y.");
    //   double y0 = Double.parseDouble(f.readLine());
    //   System.out.println("Enter the value of x at which the function y(x) should be approximated.");
    //   double xf = Double.parseDouble(f.readLine());
    //
    //   System.out.println("If you would like to use step size, enter \"1\". If you would like to use step number, enter \"2\".");
    //   int mode = Integer.parseInt(f.readLine());
    //   if (mode == 1) {
    //     //IMPLEMENT FRACTIONS SO THIS WORKS AS INTENDED*****************************************************************************************
    //     System.out.println("Enter the step size. The difference between the initial value and final value of x must be divisible by this.");
    //     String h = f.readLine();
    //
    //   }
    //   else if (mode == 2){
    //     System.out.println("Enter the step number. This must be an integer.");
    //     int n = Integer.parseInt(f.readLine());
    //     for (int i = 0; i < n; i++) {
    //
    //     }
    //   }
    //   else {
    //     System.out.println();
    //   }
    // }


    if (operation.equals("trapezoidalRule")) {
      System.out.println("Enter the lower bound.");
      double a = Double.parseDouble(f.readLine());
      System.out.println("Enter the upper bound.");
      double b = Double.parseDouble(f.readLine());
      System.out.println("Enter the value of n (integer).");
      int n = Integer.parseInt(f.readLine());
      System.out.println(trapezoidalRule(function,a,b,n));
    }


    else if (operation.equals("simpsonsRule")) {
      System.out.println("Enter the lower bound.");
      double a = Double.parseDouble(f.readLine());
      System.out.println("Enter the upper bound.");
      double b = Double.parseDouble(f.readLine());
      System.out.println("Enter the value of n (integer, even).");
      int n = Integer.parseInt(f.readLine());
      System.out.println(simpsonsRule(function,a,b,n));
    }


    else if (operation.equals("midpointRule")) {
      System.out.println("Enter the lower bound.");
      double a = Double.parseDouble(f.readLine());
      System.out.println("Enter the upper bound.");
      double b = Double.parseDouble(f.readLine());
      System.out.println("Enter the value of n (integer).");
      int n = Integer.parseInt(f.readLine());
      System.out.println(midpointRule(function,a,b,n));
    }


    else {
      System.out.println("Enter the value of x. This must be a number, even if the function is constant.");
      double x = Double.parseDouble(f.readLine());
      System.out.println(doFunc(function,x));
    }

  }
}

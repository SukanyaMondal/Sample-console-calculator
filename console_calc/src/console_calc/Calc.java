package console_calc;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.Arrays;


public class Calc {
    private static final ArrayList<Character> DIVIDERS = new ArrayList<Character>
            (Arrays.asList('*', '/', '-', '+'));
    private static final int RIGHT_DIRECTION = 1;
    private static final int LEFT_DIRECTION = -1;

    public static void main(String[] args) {
        BufferedReader reader = new BufferedReader(new InputStreamReader(System.in));
        String expression = "";

        System.out.print("Enter expression: ");
        try {
            expression = reader.readLine();
        } catch (IOException e) {
            e.printStackTrace();
        }
        System.out.println("Given expression: " + expression);

        expression = prepareExpression(expression);

        System.out.println("Prepared expression: " + expression);

        System.out.println("Final answer: " + calc(expression));

    }

        private static String calc(String expression) {
        int pos = 0;
        System.out.println("Solving the expression: "+expression);
            if (-1 != (pos = expression.indexOf("("))) {

            String subexp = extractExpressionFromBraces(expression,pos);
            expression = expression.replace("("+subexp+")", calc(subexp));

            return calc(expression);

       
        } else if (-1 != (pos = expression.indexOf("sin"))) {

            pos += 2;

            String number = extractNumber(expression, pos, RIGHT_DIRECTION);

            expression = expression.replace("sin"+number, 
                    Double.toString(Math.sin(Math.toRadians(Double.parseDouble(number)))));

            return calc(expression);

        } else if (-1 != (pos = expression.indexOf("cos"))) {

            pos += 2;

            String number = extractNumber(expression, pos, RIGHT_DIRECTION);
            
            expression = expression.replace("cos"+number, 
                    Double.toString(Math.cos(Math.toRadians(Double.parseDouble(number)))));
            return calc(expression);
            

        } else if (-1 != (pos = expression.indexOf("tan"))) {

            pos += 2;
            String number = extractNumber(expression, pos, RIGHT_DIRECTION);
            
            expression = expression.replace("tan"+number, 
                    Double.toString(Math.tan(Math.toRadians(Double.parseDouble(number)))));
            
            return calc(expression);
            
            
        } else if (-1 != (pos = expression.indexOf("exp"))) {

            pos += 2;

            String number = extractNumber(expression, pos, RIGHT_DIRECTION);

            expression = expression.replace("exp" + number, 
                    Double.toString(Math.exp(Double.parseDouble(number))));

            return calc(expression);


        } else if (expression.indexOf("*") > 0 | expression.indexOf("/") > 0) {

            int multiPosition = expression.indexOf("*");
            int diviPosition = expression.indexOf("/");

            pos = Math.min(multiPosition, diviPosition);
            if (multiPosition < 0) pos = diviPosition; else if (diviPosition < 0) pos = multiPosition; 
                
            char divider = expression.charAt(pos);

            String leftNumb = extractNumber(expression, pos, LEFT_DIRECTION);
            String rightNumb = extractNumber(expression, pos, RIGHT_DIRECTION);

            expression = expression.replace(leftNumb + divider + rightNumb, 
                calcShortExp(leftNumb, rightNumb, divider));

            return calc(expression);


        } else if (expression.indexOf("+") > 0 | expression.indexOf("-") > 0) {

            int sumPosition = expression.indexOf("+");
            int subsPosition = expression.indexOf("-");

            pos = Math.min(sumPosition, subsPosition);

            if (sumPosition < 0) pos = subsPosition; else if (subsPosition < 0) pos = sumPosition;

            char divider = expression.charAt(pos);

            String leftNumb = extractNumber(expression, pos, LEFT_DIRECTION);
            String rightNumb = extractNumber(expression, pos, RIGHT_DIRECTION);

            expression = expression.replace(leftNumb + divider + rightNumb, 
                calcShortExp(leftNumb, rightNumb, divider));

            return calc(expression);

        } else return expression;
    }

    private static String extractExpressionFromBraces(String expression, int pos) {
        int braceDepth = 1;
        String subexp="";

        for (int i = pos+1; i < expression.length(); i++) {
            switch (expression.charAt(i)) {
                case '(':
                    braceDepth++;
                    subexp += "(";
                    break;
                case ')':
                    braceDepth--;
                    if (braceDepth != 0) subexp += ")";
                    break;
                default:
                    if (braceDepth > 0) subexp += expression.charAt(i);

            }
            if (braceDepth == 0 && !subexp.equals("")) return subexp;
        }
        return "Failure!";
    }

    private static String extractNumber(String expression, int pos, int direction) {

        String resultNumb = "";
        int currPos = pos + direction;

       
        if (expression.charAt(currPos) == '-') {
            resultNumb+=expression.charAt(currPos);
            currPos+=direction;
        }

        for (; currPos >= 0 &&
               currPos < expression.length() &&
               !DIVIDERS.contains(expression.charAt(currPos));
               currPos += direction) {
            resultNumb += expression.charAt(currPos);
        }

        if (direction==LEFT_DIRECTION) resultNumb = new StringBuilder(resultNumb).reverse().toString();

        return resultNumb;
    }

    private static String calcShortExp(String leftNumb, String rightNumb, char divider) {
        switch (divider) {
        	case '+':
        		return Double.toString(Double.parseDouble(leftNumb) + Double.parseDouble(rightNumb));
        	case '-':
        		return Double.toString(Double.parseDouble(leftNumb) - Double.parseDouble(rightNumb));
        
            case '*':
                return Double.toString(Double.parseDouble(leftNumb) * Double.parseDouble(rightNumb));
            case '/':
                return Double.toString(Double.parseDouble(leftNumb) / Double.parseDouble(rightNumb));
            default:
                return "0";
        }

    }

    private static String prepareExpression(String expression) {

        expression = expression.replace("E", Double.toString(Math.E));
        expression = expression.replace("PI", Double.toString(Math.PI));
        expression = expression.replace(" ", "");

        return expression;
    }
}
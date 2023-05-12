package com.example.project2;

public class Equation {
    private final String data;
    private boolean valid;
    private double result;
    private String postfix;

    Equation(String data){

        this.data=data;
        this.valid = isBalanced(data);
        convertPostfix();
        evaluate();


    }

    public String getData() {
        return data;
    }

    public boolean isValid() {
        return valid;
    }

    public double getResult() {
        return result;
    }

    public String getPostfix() {
        return postfix;
    }
    private void convertPostfix(){
        CursorStack stack = new CursorStack();
        int l = stack.createStack();
        StringBuilder postfix = new StringBuilder(data.length());
        String[] pos = data.split(" ");
        String c;
        int numCount=0,opCount =0;

        if(isValid()) {
            for (String po : pos) { //check for every part of the string
                c = po;
                if (isNum(c)) {  //if it's a number, output it
                    postfix.append(c).append(" ");
                    numCount++;
                } else if (c.equals("(")) { //if it is an opening parenthesis, push it to stack
                    stack.push(c, l);
                } else if (isOperator(c)) { //if operator, do the following
                    opCount++;
                    if (stack.isEmpty(l) || stack.getPeek(l).equals("(")
                            || getPriority(c) > getPriority(stack.getPeek(l).toString()))
                        stack.push(c, l);
                        //if stack is empty or opening parenthesis or current operator higher than peek, push current operator to stack
                    else {
                        while (!stack.isEmpty(l) && getPriority(c) <= getPriority(stack.getPeek(l).toString())) {
                            postfix.append(stack.pop(l).getData().toString()).append(" ");
                            //while current operator has lower priority than peek, pop peek and output it
                        }
                        stack.push(c, l);
                    }
                } else if (c.equals(")")) {  //if closing parenthesis, pop everything and output it until open parenthesis is reached
                    while (!stack.getPeek(l).toString().equals("("))
                        postfix.append(stack.pop(l).getData().toString()).append(" ");
                    stack.pop(l);
                }
            }
            while (!stack.isEmpty(l)) {  //when no more inputs, pop everything in stack and output it
                postfix.append(stack.pop(l).getData().toString()).append(" ");
            }
            if(numCount != opCount+1)
                this.postfix= "Invalid equation";
            else
                this.postfix = postfix.toString();
        }
        else this.postfix = "Imbalanced equation";
    }

    private int getPriority(String x) { //check priority of operator
        return switch (x) {
            case "^" -> 3;
            case "*", "/" -> 2;
            case "+", "-" -> 1;
            default -> 0;
        };
    }


    public static boolean isOperator(String c){ //check if token is operator
        return c.equals("^") || c.equals("*") || c.equals("/") || c.equals("+") || c.equals("-");
    }

    public static boolean isNum(String c) { //check if token is number
        try{
            Double.parseDouble(c);
            return true;
        }
        catch (Exception e){
            return false;
        }
    }
    private void evaluate() { //function to evaluate value
        if(isValid()) {
            try {
                String[] str = postfix.split(" ");
                CursorStack stack = new CursorStack();
                int l = stack.createStack();

                for (String s : str) {
                    if (isNum(s)) { //if token is number, push to stack
                        stack.push(Double.parseDouble(s), l);
                    } else {  //if operand, pop last two numbers and perform operation
                        double num2 = (double) stack.pop(l).getData();
                        double num1 = (double) stack.pop(l).getData();
                        double result = switch (s.charAt(0)) {
                            case '+' -> num1 + num2;
                            case '-' -> num1 - num2;
                            case '*' -> num1 * num2;
                            case '/' -> num1 / num2;
                            case '^' -> (int) Math.pow(num1, num2);
                            default -> 0;
                        };
                        stack.push(result, l);
                    }
                }
                this.result = (double) stack.pop(l).getData();
            } catch (Exception e) {
                this.postfix = "Invalid equation";
                this.result = 0;
                this.valid = false;
            }
        }
        else this.result = 0;
    }
    public boolean isBalanced(String data){  //function to check if equation is balanced
        CursorStack balance =new CursorStack();
        int l=balance.createStack();
        char[] car = data.toCharArray();

        for (char c : car) {
            if ((c == '('))
                balance.push(c + "".trim(), l);
            else if ((c == ')')) {

                if (balance.isEmpty(l)) { //if closing parenthesis found but stack is empty, imbalanced equation
                   return  false;
                } else {
                    balance.pop(l);

                }
            }


        }
        return balance.isEmpty(l);
    }

}

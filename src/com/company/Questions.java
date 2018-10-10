package com.company;

import java.util.Scanner;
import java.util.Stack;

public class Questions {
    private static Stack<Float> operatedNumber = new Stack<Float>();
    private static Stack<Character> operatedSign = new Stack<Character>();
    private static int signCount;
    private static char[] signArray = new char[]{'+', '-', '×', '÷', '(', ')', '='};
    private static char[][] signPriority = new char[][]{{'>', '>', '<', '<', '<', '>', '>'},
            {'>', '>', '<', '<','<', '>', '>'}, {'>', '>', '>', '>', '<', '>', '>'}, {'>', '>', '>', '>', '<', '>', '>'},
            {'<', '<', '<', '<','<', '=', ' '}, {'>', '>', '>', '>', '>', '>', ' '}};
    private static StringBuilder expression = new StringBuilder();

    public static void main(String[] args) {
        // TODO Auto-generated method stub
        System.out.println("请输入题目数：");
        Scanner in = new Scanner(System.in);
        int qNumber = in.nextInt();
        if (isValid(qNumber)) {
            printQuestions(qNumber);
        }
    }

    private static int getPosition(Character sign) {
        for (int i = 0; i < signArray.length; i++) {
            if (sign == signArray[i]) {
                return i;
            }
        }
        return -1;
    }

    private static boolean isValid(int number) {
        boolean isValid = true;
        if (number <= 0) {
            System.out.println("题目数不得少于1!");
            return false;
        }
        return isValid;
    }

    private static Stack<Character> setOperatedSign() {
        Stack<Character> sign = new Stack<Character>();
        signCount = (int) (Math.random() * 4) + 1;
        sign.add('=');
        for (int i = 0; i < signCount; i++) {
            int index = (int) (Math.random() * 4);
            sign.add(signArray[index]);
        }
        sign = addKuohao(sign);
        System.out.println(sign);
        return sign;
    }

    public static Stack<Character> addKuohao(Stack<Character> operatedSign) {
        boolean flag = false;
        for (int i = operatedSign.size()-1; i > 1; i--) {
            if (operatedSign.get(i) == '÷' || operatedSign.get(i) == '×') {
                if (operatedSign.get(i-1) == '+' || operatedSign.get(i-1) == '-') {
                    operatedSign.add(i,'(');
                    flag = true;
                }
            }
            if (flag) {
                int index = (int)Math.random() * (i-1) + 1;
                operatedSign.add(index,')');
                break;
            }
        }
        return operatedSign;
    }
    private static Stack<Float> setOperatedNum() {
        Stack<Float> operatedNum = new Stack<Float>();
        for (int i = 0; i < signCount + 1; i++) {
            int number = (int) (Math.random() * 100) + 1;
            operatedNum.add((float)number);
        }
        return operatedNum;
    }

    private static float calculate(Float number1, Character sign, Float number2) {
        switch (sign) {
            case '+':
                return number1 + number2;
            case '-':
                return number1 - number2;
            case '×':
                return number1 * number2;
            case '÷':
                return number1 / number2;
        }
        return -1;
    }

    private static Float getResult(Stack<Float> operatedNumber, Stack<Character> operatedSign) {
        int count = 0, flag=1;
        Stack<Float> tempNumber = new Stack<Float>();
        Stack<Character> tempSign = new Stack<Character>();
        Float newData = null, numberTop = null, numberNext = null, numberNextNext = null;
        Character signTop = null, signNext = null;
        while (operatedNumber.size() != 1 && operatedSign.size() != 1) {
            switch (signPriority[getPosition(operatedSign.get(operatedSign.size() - 1))][getPosition(operatedSign.get(operatedSign.size() - 2))]) {
                case '>':
                    numberTop = operatedNumber.pop();
                    numberNext = operatedNumber.pop();
                    signTop = operatedSign.pop();
                    newData = calculate(numberTop, signTop, numberNext);
                    operatedNumber.push(newData);
                    break;
                case '<':
                    numberTop = operatedNumber.pop();
                    numberNext = operatedNumber.pop();
                    numberNextNext = operatedNumber.pop();
                    signTop = operatedSign.pop();
                    signNext = operatedSign.pop();
                    if (!operatedSign.isEmpty()&& (signNext == '(' || operatedSign.get(operatedSign.size()-1)=='(')) {
                        if (operatedSign.get(operatedSign.size()-1)=='(') {
                            operatedSign.pop();
                            operatedNumber.push(numberNextNext);
                            flag=0;
                        }
                        else {
                            operatedNumber.add(numberNextNext);
                            operatedNumber.add(numberNext);
                        }
                        while(operatedSign.get(operatedSign.size()-1)!=')') {
                            tempSign.add(operatedSign.pop());
                            count++;
                        }
                        tempSign.push('=');
                        for (int i =0; i<count+1; i++) {
                            tempNumber.add(operatedNumber.pop());
                        }
                        tempNumber = setNumberReserve(tempNumber);
                        tempSign = setSignReserve(tempSign);
                        if (flag==1) {
                            newData = calculate(numberTop, signTop, getResult(tempNumber,tempSign));
                            operatedNumber.push(newData);
                        }
                        else {
                            newData = calculate(numberNext, signNext, getResult(tempNumber,tempSign));
                            operatedNumber.push(newData);
                            operatedNumber.push(numberTop);
                            operatedSign.push(signTop);
                        }
                    }
                    else {
                        newData = calculate(numberNext, signNext, numberNextNext);
                        operatedSign.push(signTop);
                        operatedNumber.push(newData);
                        operatedNumber.push(numberTop);
                    }
                    break;
                case '=':
                    operatedSign.pop();
                    operatedSign.pop();
                    break;
                default:
                    break;
            }
        }
        return operatedNumber.get(0);
    }

    private static void printQuestions(int qNumber) {
        for (int i = 0; i < qNumber; i++) {
            operatedSign = setOperatedSign();
            operatedNumber = setOperatedNum();
            setPositiveNum();
            System.out.print("第" + (i + 1) + "题 :");
            for (int k=operatedSign.size()-1,j = operatedNumber.size()-1; j > -1&&k>-1; j--,k--) {
                expression.append(operatedNumber.get(j).intValue());
                expression.append(operatedSign.get(k));
                if (k>0&&operatedSign.get(k-1)=='(') {
                    expression.append(operatedSign.get(k-1));
                    k--;
                }
                if(operatedSign.get(k)==')') {
                    expression.append(operatedSign.get(k-1));
                    k--;
                }
            }
            System.out.print(expression);
            getResult(operatedNumber, operatedSign);
            if(operatedNumber.get(0).intValue() == operatedNumber.get(0)) {
                System.out.println(operatedNumber.get(0).intValue());
            }
            else {
                System.out.printf("%.2f\n",operatedNumber.get(0));
            }
            expression.delete(0, expression.length());
        }
    }

    private static void setPositiveNum() {

    }
    private static Stack<Float> setNumberReserve(Stack<Float> number) {
        for(int i=number.size()-1,j=0; i>=j; i--,j++) {
            Float temp1 = number.get(i);
            Float temp2 = number.get(j);
            number.set(i,temp2);
            number.set(j,temp1);
        }
        return number;
    }
    private static Stack<Character> setSignReserve(Stack<Character> sign) {
        for(int i=sign.size()-1,j=0; i>=j; i--,j++) {
            Character temp1 = sign.get(i);
            Character temp2 = sign.get(j);
            sign.set(i,temp2);
            sign.set(j,temp1);
        }
        return sign;
    }
}

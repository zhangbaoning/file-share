package me.bn.fileshare;

import java.util.ArrayList;
import java.util.List;
import java.util.Stack;

public class Test {
    public static void main(String[] args) {

        char out;
        String exp = "5+2*(3*(2/0))";
        exp = exp.replaceAll(" ","");
        List<String> list= new ArrayList();
        Stack<Character> stack = new Stack();
        StringBuilder sb = new StringBuilder();
        for (char c : exp.toCharArray()) {
            // 优先级最高
            if (c==')'){
                if (!"".equals(sb.toString())){
                    list.add(sb.toString());
                }
                sb= new StringBuilder();
                while (!stack.empty()&&stack.peek()!='(') {
                     out =  stack.pop();
                     if (out!='('){
                         list.add(String.valueOf(out));
                         //sb.append(out);
                     }

                }
                stack.pop();

            }
            else if (c=='*'||c=='/'){
                if (!"".equals(sb.toString())){
                    list.add(sb.toString());
                }

                sb= new StringBuilder();
                if (!stack.empty()&&stack.peek()==('*'|'/')){

                    out =  stack.pop();
                    if (out!='('){
                       // sb.append(out);
                        list.add(String.valueOf(out));
                    }

                }

                stack.push(c);
            }
            else if (c=='+'||c=='-'){
                if (!"".equals(sb.toString())){
                    list.add(sb.toString());
                }
                sb= new StringBuilder();
                // 如果它的顶是*/ 就输出到（或者为空


                while (!stack.empty()&&stack.peek()!='(') {

                    out =  stack.pop();
                    if (out!='('){
                        //sb.append(out);
                        list.add(String.valueOf(out));
                    }
                }

                stack.push(c);
            }else if (c=='('){
                stack.push(c);
            }else{
                sb.append(c);
            }
        }
        if (!"".equals(sb.toString())){
            list.add(sb.toString());
        }
        while (!stack.empty()){
            char output = stack.pop();
            if (output!='('){
                list.add(String.valueOf(output));
                sb= new StringBuilder();
            }
        }

        System.out.println(list);
        cal(list);
        System.out.println(sb.toString());
    }
    static void cal(List<String> list){
        float result = 0;
        Stack<String> stack = new Stack();
        for (String s : list) {
            if ("+".equals(s)||"-".equals(s)||"*".equals(s)||"/".equals(s)){
                String second = stack.pop();
                String first = stack.pop();
                try {
                    System.out.println(first+s+second+"="+String.valueOf(process(first,s,second)));
                    result = process(first,s,second);
                    stack.push(String.valueOf(result));
                } catch (Exception e) {
                    System.out.println("输入表达式 = NaN");
                   break;
                }

            }else {
                stack.push(s);
            }
        }
        System.out.printf("%.2f",result);
    }
    static float process(String first,String op,String second) {
        switch (op){

            case ("+"):return Float.valueOf(first)+Float.valueOf(second);
            case ("-"):return Float.valueOf(first)-Float.valueOf(second);
            case ("*"):return Float.valueOf(first)*Float.valueOf(second);
            case ("/"):return Float.valueOf(first)/Float.valueOf(second);
        }
        return 0;
    }
}

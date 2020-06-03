package me.bn.fileshare;

import java.util.EmptyStackException;
import java.util.Stack;

public class Postfix {

        /**
         * ����׺ת��Ϊ��׺���ʽ
         *
         * @param infix
         * @return
         */
        public static String infixToPostfix(String infix) {
            // ������ջ-----'+ - * / ( )'
            Stack<Character> op = new Stack<Character>();
            StringBuilder postfixStr = new StringBuilder("");
            char[] prefixs = infix.trim().toCharArray();
            Character ch;
            for (int i = 0; i < prefixs.length; i++) {
                ch = prefixs[i];
                // ��������� 0~9
                if (ch >= '0' && ch <= '9') {
                    // vlaues.push(Integer.valueOf(ch));
                    postfixStr.append(ch);
                    continue;
                }
                // "("---ֱ��ѹջ
                if ('(' == ch) {
                    op.push(ch);
                    continue;
                }
                /*
                 * '+ - * /'----��ջֱ��ѹջ������ջ��Ԫ�رȽϣ� ���ȼ�����ջ��Ԫ���򵯳�ջֱ���������ȼ���Ȼ��ߵ͵ľ�ֹͣ��ջ
                 * ��󽫸ò�����ѹջ
                 */

                if ('+' == ch || '-' == ch) {
                    // ֻҪջ��Ϊ�գ�ջ��Ԫ�ز���'(' �͵�ջ
                    while (!op.empty() && (op.peek() != '(')) {
                        postfixStr.append(op.pop());
                    }
                    op.push(ch);
                    continue;
                }
                if ('*' == ch || '/' == ch) {
                    // ֻҪջ��Ϊ�գ�ջ��Ԫ����'* /' �͵�ջ
                    while (!op.empty() && (op.peek() == '*' || op.peek() == '/')) {
                        postfixStr.append(op.pop());
                    }
                    op.push(ch);
                    continue;
                }
                // ')'----��ʼ��ջֱ��������һ��'('
                if (')' == ch) {
                    while (!op.empty() && op.peek() != '(') {
                        postfixStr.append(op.pop());
                    }
                    op.pop();// ----��'('Ԫ�ص���
                    continue;
                }
            }
            // �����ַ�������ϲ�����ջ�������ݾ�ȫ����ջ
            while (!op.empty())
                postfixStr.append(op.pop());
            return postfixStr.toString();
        }

        /**
         * �����׺���ʽ
         *
         * @param postfix
         * @return
         */
        public static int sumPostfix(String postfix){
            //������ջ---��ʱֻ����������
            Stack<Integer> values;
            int result=0;
            try {
                values = new Stack<Integer>();
                char [] postfixs =postfix.trim().toCharArray();
                Character ch;
                for(int i=0;i<postfixs.length;i++){
                    ch=postfixs[i];
                    if(ch >= '0' && ch <= '9') {
                        values.push(Integer.valueOf(String.valueOf(ch)));//---�����ջ
                        //���ߣ�*****����������˼ά
                        //values.push(Integer.valueOf(ch-'0'));
                    }else {
                        result=operate(ch, values.pop(),values.pop());
                        values.push(result);

                    }

                }
                result=values.pop();
                if(!values.empty()){
                    throw  new Exception();
                }

            } catch (NumberFormatException e) {
                System.out.println("����ת�����쳣");
            }catch(EmptyStackException e){//ջ��Ϊ�ջ��ڽ��е�ջ����
                System.out.println("��׺���ʽ��ʽ������");
            }catch(Exception e){  // ���Ŷ�����ջ�л��ж������
                System.out.println("��׺���ʽ��ʽ������");
            }
            return result;
        }
        public static int operate(char op,int value1,int value2){
            int result=0;
            //��ס�õڶ�����ջ��ֵvalue2���Ӽ��˳�����һ����ջ��ֵvalue1
            switch ((int) op) {
                case 43://'+'
                    result=value2+value1;
                    break;
                case 45://'-'
                    result=value2-value1;
                    break;
                case 42://'*'
                    result=value2*value1;
                    break;
                case 47://'/'
                    result=value2/value1;
                    break;
                default:
                    break;
            }
            return result;
        }
        public static void main(String[] args) {
            String str="2+3*(5-6/4)/2+6-3*3/2";
            System.out.println(str+":"+infixToPostfix(str));
            str=infixToPostfix(str);
            System.out.println("result:"+sumPostfix(str));
        }


    }


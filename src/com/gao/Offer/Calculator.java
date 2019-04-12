package com.gao.Offer;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.util.*;

public class Calculator  implements ActionListener {
    static TextField tf = new TextField();

    public static void main(String [] args) {
        //设置窗口
        Frame f = new Frame("Calculator");
        f.setLayout(new BorderLayout());
        f.setLocation(300,400);
        f.setSize(400,300);
        f.setVisible(true);
        f.setBackground(new Color(204,204,255));
        addWindowClosingEvent(f);

        //将Frame分为北和非北
        Panel p1 = new Panel(new BorderLayout());
        Panel p2 = new Panel(new GridLayout(4,4));
        f.add(p1,BorderLayout.NORTH);
        f.add(p2);

        //设置P1
        p1.add(tf);
        Button clear = new Button("Clear");
        p1.add(clear,BorderLayout.EAST);


        //设置p2的button
        Button[] buttons = new Button[16];
        for(int i=0;i<10;i++) {
            buttons[i] = new Button(""+i);
            p2.add(buttons[i]);
        }
        buttons[10] = new Button("/");
        buttons[11] = new Button("*");
        buttons[12] = new Button("-");
        buttons[13] = new Button("+");
        buttons[14] = new Button("=");
        buttons[15] = new Button(".");
        for(int i=10;i<16;i++) {
            p2.add(buttons[i]);
        }
        Calculator c = new Calculator();
        Calculator cc = new Calculator();
        clear.addActionListener(cc);
//        Monitor1 m = new Monitor1();
        for (int i=0;i<16;i++) {
            buttons[i].addActionListener(c);
        }
    }

    //关闭窗口
    private static void addWindowClosingEvent(Frame f) {
        f.addWindowListener(new WindowAdapter() {
            @Override
            public void windowClosing(WindowEvent arg0) {
                System.exit(0);
            }

        });
    }

    String s;
    Stack s1 = new Stack();
    Stack s2 = new Stack();
    double a=0.0;
    double b=0.0;
    int flg=0;//标示是否需要进行小树运算
    int d = 0;//标示小数点有几位
    int count = 1;//标示表达式有多少项
    int flag = 0;//标示是操作符还是计算好的数字

    @Override
    public void actionPerformed(ActionEvent e) {
        //清空数据
        System.out.println(e.getActionCommand()+"e.getActionCommand()");
        if(e.getActionCommand()=="Clear") {
            tf.setText("");
        }

        if(s2.search('#')==-1) {
            s2.push('#');
        }
        Map<Integer,Object> map = new HashMap<Integer,Object>();
        s = e.getActionCommand();
        if((s.toCharArray()[0]!='+')&&(s.toCharArray()[0]!='-')&&(s.toCharArray()[0]!='*')&&(s.toCharArray()[0]!='/')&&(s.toCharArray()[0]!='=')) {
            flag = 1;
            if(s.toCharArray()[0]!='.') {//输入是整数
                if(flg==0) {
                    a = a*10+s.toCharArray()[0]-48;
                }else {
                    b += (s.toCharArray()[0]-48)*Math.pow(0.1,++d);
                }
            }else {//输入是小数
                flg = 1;
            }
        }else {
            if(flag==1) {
                map.put(count++,a+b);
                a= 0.0;
                b=0.0;
                d = 0;
                flag = 0;
                flg = 0;
            }
        }
        if((s.toCharArray()[0]!='+')||(s.toCharArray()[0]!='-')||(s.toCharArray()[0]!='*')||(s.toCharArray()[0]!='/')) {
            if(flag==0) {
                map.put(count++,s.toCharArray()[0]);
            }
        }

        //遍历表达式Map
        Set<Map.Entry<Integer, Object>> set = map.entrySet();
        Iterator<Map.Entry<Integer, Object>> iter = set.iterator();
        while (iter.hasNext()) {
            Map.Entry<Integer, Object> me = iter.next();

            if(me.getValue().toString().toCharArray()[0]=='+') {
                if(level('+')>level(s2.peek().toString().toCharArray()[0])) {
                    s2.push(me.getValue());
                }else {
                    s1.push(s2.pop());
                    s2.push(me.getValue());
                    System.out.println("s1"+s1);
                    System.out.println("s2"+s2);
                }
            } else if(me.getValue().toString().toCharArray()[0]=='-') {
                if(level('-')>level(s2.peek().toString().toCharArray()[0])) {
                    s2.push(me.getValue());
                }else {
                    s1.push(s2.pop());
                    s2.push(me.getValue());
                    System.out.println("s1"+s1);
                    System.out.println("s2"+s2);
                }
            } else if(me.getValue().toString().toCharArray()[0]=='*') {
                if(level('*')>level(s2.peek().toString().toCharArray()[0])) {
                    s2.push(me.getValue());
                }
            } else if(me.getValue().toString().toCharArray()[0]=='/') {
                if(level('/')>level(s2.peek().toString().toCharArray()[0])) {
                    s2.push(me.getValue());
                }
            } else if(me.getValue().toString().toCharArray()[0]=='=') {
                while(!(s2.empty())) {
                    s1.push(s2.pop());
                }
                System.out.println("s1"+s1);
                System.out.println("s2"+s2);
                Answer(s1);
            } else {
                s1.push(me.getValue());
                System.out.println("s1"+s1);
                System.out.println("s2"+s2);
            }
            System.out.println(me.getKey() + "，" + me.getValue());
        }
    }
    //优先级函数
    public int level(char c) {
        int l1;
        int l2;
        if(c == '+') {
            l1 = 1;
            return l1;
        }else if(c=='-') {
            l1 = 1;
            return l1;
        }else if(c=='*') {
            l2 = 2;
            return l2;
        }else if(c=='/') {
            l2 = 2;
            return l2;
        }
        return  0;
    }
    //后缀表达式求值：如果该项是操作数，则将其压入栈，如果是操作符，则连续从栈中推出两个操作数，进行运算，并将
    //计算结果重新压入栈中

    public void Answer(Stack s1) {

        double jia = 0.0;
        double jian ;
        double cheng = 1.0;
        double chu;
        Stack stack = new Stack();
        s1.pop();
        Stack ss = new Stack();
        while (!(s1.empty())) {
            ss.push(s1.pop());
        }
        while (!(ss.isEmpty())) {
            if((ss.peek().toString().toCharArray()[0] !='+') && (ss.peek().toString().toCharArray()[0] !='-') && (ss.peek().toString().toCharArray()[0] !='*') &&(ss.peek().toString().toCharArray()[0] !='/')) {
                stack.push(ss.pop());
                System.out.println(stack+"stack");
            }else {
                if(!(ss.isEmpty())) {
                    if(ss.peek().toString().toCharArray()[0] =='+') {
                        ss.pop();
                        for(int i=0;i<2;i++) {
                            jia += Double.parseDouble(stack.pop().toString());
                        }
                        stack.push(jia);
                        System.out.println(stack+"stack");
                    }
                }
                if(!(ss.isEmpty())) {
                    if(ss.peek().toString().toCharArray()[0] =='-') {
                        ss.pop();
                        double a = Double.parseDouble(stack.pop().toString());
                        double b = Double.parseDouble(stack.pop().toString());
                        jian = b-a;
                        stack.push(jian);
                        System.out.println(stack+"stack");
                    }
                }
                if(!(ss.isEmpty())) {
                    if(ss.peek().toString().toCharArray()[0] =='*') {
                        ss.pop();
                        for(int i=0;i<2;i++) {
                            cheng *= Double.parseDouble(stack.pop().toString());
                        }
                        stack.push(cheng);
                        System.out.println(stack+"stack");

                    }
                }
                if(!(ss.isEmpty())) {
                    if(ss.peek().toString().toCharArray()[0] =='/') {
                        ss.pop();
                        double a = Double.parseDouble(stack.pop().toString());
                        double b = Double.parseDouble(stack.pop().toString());
                        chu = b/a;
                        stack.push(chu);
                        System.out.println(stack+"stack");

                    }
                }


            }
        }
        //结果保留两位小数
        System.out.println("answer"+String.format("%.2f",Double.parseDouble(stack.peek().toString())));
        //传值给文本框
        tf.setText(String.format("%.2f",Double.parseDouble(stack.peek().toString())));
    }
}

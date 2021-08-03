package com.example.lib.collection;

import com.example.lib.Hero;

import java.util.ArrayList;
import java.util.List;

public class Node {
    //左子节点
    public Node leftNode;
    //右子节点
    public Node rightNode;

    public Object value;

    public void add(Object v) {
        System.out.println(v+"vvvvv");
        System.out.println(value+"value");
        if (null == value) {
            value = v;
        }else {
            System.out.println(v+"add");
            //如果当前节点有值，就进行判断，新增的值与当前值打大少关系
            if((Integer)v - ((Integer)value) <= 0) {
                if (null == leftNode)
                    leftNode = new Node();
                System.out.println(v+"--");
                leftNode.add(v);
            }else {
                if (null == rightNode) {
                    rightNode = new Node();
                }
                System.out.println(v+"++");
                rightNode.add(v);
            }
        }
    }

    public List<Object> values() {
        List<Object> values = new ArrayList<>();

        //左节点的遍历结果
        if (null != leftNode)
            values.addAll(leftNode.values());

        //添加当前节点
        System.out.println(value+"++value");

        values.add(value);
        //添加右节点

        if (null != rightNode) {
            values.addAll(rightNode.values());
        }

        return values;
    }

    public static void main(String[] args) {
        int randoms[] = new int[] { 67, 7, 30, 73, 10, 0, 78, 81, 10, 74 };

        Node roots = new Node();
        for (int number : randoms) {
            System.out.println(number+"-number-");

            roots.add(number);
        }

        System.out.println(roots.values());
    }
}

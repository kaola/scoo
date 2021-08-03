package com.example.lib.collection;

import com.example.lib.Hero;

import java.util.LinkedList;
import java.util.List;
import java.util.Queue;

public class TestCollection {
    public static void main(String[] args) {
        List ll = new LinkedList<Hero>();
        Queue<Hero> q = new LinkedList<>();

        System.out.print("初始化队列：\t");
        q.offer(new Hero("Hero1"));
        q.offer(new Hero("Her02"));
        q.offer(new Hero("Her03"));
        q.offer(new Hero("Her04"));

        System.out.println(q);
        System.out.println("把第一个元素取poll()出来:\t");
        //取出第一个Hero，FIFO先进先出
        Hero h = q.poll();
        System.out.println(h);
        System.out.print("取出第一个元素之后的队列:\t");
        System.out.println(q);

        h = q.peek();
        System.out.print("查看peek()第一个元素:\t");
        System.out.println(h);
        System.out.print("查看并不会导致第一个元素被取出来:\t");
        System.out.println(q);
    }
}

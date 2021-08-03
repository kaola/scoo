package com.example.lib;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

public class TestException {
    public static void main(String[] args) {
        File f = new File("e:/LOL.exe");

        try {
            new FileInputStream(f);
            SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
            Date d = sdf.parse("2016-01-02");
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (ParseException e) {
            e.printStackTrace();
        }
        finally {
            System.out.println("无论文件是否存在， 都会执行的代码");
        }
        method1();
    }

    private static void method1() {
        try {
            method2();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
    }

    private static void method2() throws FileNotFoundException {
        File f = new File("d:/LOL.exe");

        new FileInputStream(f);
    }
}
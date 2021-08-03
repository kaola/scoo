package com.example.lib;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;

public class TestStream {

    private static FileOutputStream fos;

    public static void main(String[] args) {
//        try {
//            File f = new File("E:/apktool/apktool.bat");
//            FileInputStream fis = new FileInputStream(f);
//
//            byte[] all = new byte[(int)f.length()];
//            fis.read(all);
//            for (byte b : all) {
//                System.out.println(b);
//            }
//            //每次使用完流，都应该进行关闭
//            fis.close();
//        } catch (FileNotFoundException e) {
//            e.printStackTrace();
//        } catch (IOException e) {
//            e.printStackTrace();
//        }

//        File f = new File("e:/lol2.txt");
//        byte data[] = {88,89};
//
//        try {
//            fos = new FileOutputStream(f);
//            fos.write(data);
//        } catch (FileNotFoundException e) {
//            e.printStackTrace();
//        } catch (IOException e) {
//            e.printStackTrace();
//        } finally {
//            if (null != fos) {
//                try {
//                    fos.close();
//                } catch (IOException e) {
//                    e.printStackTrace();
//                }
//            }
//        }

        File f = new File("e:/lol2.txt");
        try (FileReader fr = new FileReader(f)) {
            char[] all = new char[(int)f.length()];
            fr.read(all);
            for (char b : all) {
                System.out.println(b);
            }
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }

        File f1 = new File("e:/lol2.txt");

        try(FileWriter fr = new FileWriter(f1)) {
            String data = "abcdefg";
            char[] cs = data.toCharArray();
            fr.write(cs);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}

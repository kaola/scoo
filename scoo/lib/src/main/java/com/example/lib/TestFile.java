package com.example.lib;

import java.io.File;
import java.io.IOException;
import java.util.Date;

public class TestFile {

    public static void main(String[] args) throws IOException {
        //绝对路径
//        File f1 = new File("e:/LOLFolder");
//        System.out.println("f1的绝对路径：" + f1.getAbsolutePath());
//
//        //相对路径，相对于工作目录
//        File f2 = new File("LoL.exe");
//        System.out.println("f2的绝对路径：" + f2.getAbsolutePath());
//
//        File f3 = new File(f1,"LoL.exe");
//        System.out.println("f3的绝对路径：" + f3.getAbsolutePath());

         File f = new File("E:/apktool/apktool.bat");
         System.out.println("当前文件是：" +f);

         System.out.println("判断是否存在: "+ f.exists());

         System.out.println("判断是否是文件夹:"+f.isDirectory());
         //文件长度
         System.out.println("获取文件的长度:" +f.length());

         long time = f.lastModified();
        Date d = new Date(time);
        System.out.println("获取文件的最后修改时间："+d);

        f.list();
        File[] fs = f.listFiles();
        f.getParent();
        f.getParentFile();
        f.mkdir();
        f.mkdirs();
        f.createNewFile();
        f.getParentFile().mkdirs();
        f.listRoots();
        f.delete();
        f.deleteOnExit();


    }
}

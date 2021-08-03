package com.example.lib;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;

public class TestStreamThree {
    public static void main(String[] args) {
        File f = new File("e:/lol.txt");
        try(
                FileWriter fw = new FileWriter(f);
                PrintWriter pw = new PrintWriter(fw);
                ) {
            pw.println("garen kill teemo");
            pw.println("teemo revive after 1 minutes");
            pw.println("teemo try to garen, but killed again");

        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}

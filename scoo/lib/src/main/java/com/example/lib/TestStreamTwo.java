package com.example.lib;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;

public class TestStreamTwo {
    public static void main(String[] args) {
        File  f = new File("e:/lol.txt");
      try(FileReader fr = new FileReader(f);
          BufferedReader br = new BufferedReader(fr);
      ) {
          while (true) {
              String line = br.readLine();
              if (null == line)
                  break;
              System.out.println(line);
          }
      } catch (FileNotFoundException e) {
          e.printStackTrace();
      } catch (IOException e) {
          e.printStackTrace();
      }
    }
}

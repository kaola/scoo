package com.example.lib.thread;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.List;

public class TestThreadFive {
   List<Integer> list = new ArrayList<>();
   List<Integer> list2 = Collections.synchronizedList(list);
}

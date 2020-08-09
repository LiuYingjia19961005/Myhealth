package com.itheima.jobs;

import com.itheima.App;

import java.util.Date;

public class JobDemo {
    public void run() {
        System.out.println("自定义定时任务" + new Date());
    }

    public static void main(String[] args) {
        App.class.getDeclaredFields();
    }
}

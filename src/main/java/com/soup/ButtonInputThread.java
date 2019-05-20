package com.soup;


import java.awt.*;

public class ButtonInputThread implements Runnable {

    private Thread thread;
    Robot robot;
    int[] keyPresses;
    int preSleep, sleepAfterKey;

    public ButtonInputThread(int preSleep, int sleepAfterKey, int[] keys) {
        try {
            robot = new Robot();
            keyPresses = keys;
            this.preSleep = preSleep;
            this.sleepAfterKey = sleepAfterKey;
        } catch (AWTException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void run() {
        if (preSleep > 0) {
            try {
                Thread.sleep(preSleep);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
        for (int key : keyPresses) {
            robot.keyPress(key);
            if (sleepAfterKey > 0) {
                try {
                    Thread.sleep(sleepAfterKey);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        }

        return;
    }

    public void start() {
        System.out.println("Starting " + this.getClass().getName() + "!");
        if (thread == null) {
            thread = new Thread(this, "ButtonInputThread");
            thread.start();
        }
    }
}

package com.jankinwu.gptassistant.queue;

import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingQueue;

public class AnswerBlockingQueue {

    private static volatile BlockingQueue<String> instance;

    public AnswerBlockingQueue() {
    }

    public static void add(String msg) {
        getInstance().add(msg);
    }

    public static String take() throws InterruptedException {
        return getInstance().take();
    }

    // 获取实例的方法
    public static BlockingQueue<String> getInstance() {
        if (instance == null) {
            synchronized (AnswerBlockingQueue.class) {
                if (instance == null) {
                    instance = new LinkedBlockingQueue<>();
                }
            }
        }
        return instance;
    }
}

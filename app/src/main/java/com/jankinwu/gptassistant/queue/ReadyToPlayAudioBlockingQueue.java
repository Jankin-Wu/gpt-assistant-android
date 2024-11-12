package com.jankinwu.gptassistant.queue;

import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingQueue;

public class ReadyToPlayAudioBlockingQueue {

    private static volatile BlockingQueue<byte[]> instance;

    public ReadyToPlayAudioBlockingQueue() {
    }

    public static void add(byte[] msg) {
        getInstance().add(msg);
    }

    public static byte[] take() throws InterruptedException {
        return getInstance().take();
    }

    // 获取实例的方法
    public static BlockingQueue<byte[]> getInstance() {
        if (instance == null) {
            synchronized (ReadyToPlayAudioBlockingQueue.class) {
                if (instance == null) {
                    instance = new LinkedBlockingQueue<>();
                }
            }
        }
        return instance;
    }
}

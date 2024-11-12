package com.jankinwu.gptassistant.utils;

import android.media.AudioFormat;
import android.media.AudioManager;
import android.media.AudioTrack;

import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

public class AudioUtils {

    private static final Lock lock = new ReentrantLock();

    public static void playWavAudio(byte[] wavBytes) {
        lock.lock(); // 获取锁
        try {
            // 解析WAV文件头
            int dataStartIndex = 0;
            try {
                // WAV文件头通常为44字节
                if (wavBytes.length < 44) {
                    throw new IllegalArgumentException("WAV file is too short.");
                }

                // 检查WAV文件的RIFF头
                if (wavBytes[0] != 'R' || wavBytes[1] != 'I' || wavBytes[2] != 'F' || wavBytes[3] != 'F') {
                    throw new IllegalArgumentException("Not a valid WAV file.");
                }

                // 找到数据部分的起始位置
                for (int i = 0; i < wavBytes.length - 4; i++) {
                    if (wavBytes[i] == 'd' && wavBytes[i + 1] == 'a' && wavBytes[i + 2] == 't' && wavBytes[i + 3] == 'a') {
                        dataStartIndex = i + 8; // 数据开始后的偏移量
                        break;
                    }
                }

                // 如果没有找到数据部分
                if (dataStartIndex == 0) {
                    throw new IllegalArgumentException("Data chunk not found.");
                }

                // WAV音频的参数
                int sampleRate = 32000; // 你可以根据文件头信息动态获取
                int channelConfig = AudioFormat.CHANNEL_OUT_MONO; // 你可以根据文件头信息动态获取
                int audioFormat = AudioFormat.ENCODING_PCM_16BIT; // PCM编码

                // 计算AudioTrack的缓冲区大小
                int bufferSize = AudioTrack.getMinBufferSize(sampleRate, channelConfig, audioFormat);
                AudioTrack audioTrack = new AudioTrack(
                        AudioManager.STREAM_MUSIC,
                        sampleRate,
                        channelConfig,
                        audioFormat,
                        bufferSize,
                        AudioTrack.MODE_STREAM
                );

                // 开始播放
                audioTrack.play();

                // 写入音频数据
                audioTrack.write(wavBytes, dataStartIndex, wavBytes.length - dataStartIndex);

                // 播放完成后释放资源
                audioTrack.stop();
                audioTrack.release();
            } catch (Exception e) {
                e.printStackTrace();
            }
        } finally {
            lock.unlock(); // 释放锁
        }
    }
}

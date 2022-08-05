package com.shichen.ffmpeg;

import com.shichen.speechrecognition.DetectQuiet;
import com.shichen.speechrecognition.VoiceUtil;
import java.io.IOException;
import javax.sound.sampled.AudioFormat;
import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.DataLine;
import javax.sound.sampled.TargetDataLine;
import javax.sound.sampled.UnsupportedAudioFileException;
import lombok.extern.slf4j.Slf4j;

/**
 * 录制文件
 *
 * @author tangsc
 * @date 2022/08/03
 */
@Slf4j
public class RecordFile {

    /**
     * 记录音频
     *
     * @author tangsc
     * @date 2022/08/03
     */
    public void audioRecord() {
        // 1. 设置音频解码器 ps 最好是系统支持的格式，否则 getLine() 会发生错误
        // 采样率 : 44.1k 44100.0F
        // 采样率位数 : 16 位
        // 通道数 : 2 (立体声)
        // 是否签名 : true
        // 字节顺序 bigEndian [true :big-endian 字节顺序 false: little-endian 详细见 ByteOrder 类]
        AudioFormat audioFormat = new AudioFormat(AudioFormat.Encoding.PCM_SIGNED, 44100, 16, 2, 4, 44100, false);
        log.info("准备开启音频");
        // 通过设置好的音频编解码器获取数据线信息
        DataLine.Info dataLineInfo = new DataLine.Info(TargetDataLine.class, audioFormat);
        TargetDataLine line = null;
        String path = "G:\\test\\voice_ai\\test002.wav";
        try {
            line = (TargetDataLine) AudioSystem.getLine(dataLineInfo);
            line.open();
            line.start();
            // 获得当前音频采样率
            final int sampleRate = (int) audioFormat.getSampleRate();
            // 获取当前音频通道数量
            final int numChannels = audioFormat.getChannels();
            // 初始化音频缓冲区(size是音频采样率*通道数)
            int audioBufferSize = sampleRate * numChannels;
            // 缓冲区
            byte[] audioBytes = new byte[audioBufferSize];
            // 读流
            int num = 0;


            // 真正录音
            RecordAudio recordAudioThread = new RecordAudio(path);
            recordAudioThread.start();
            // 主线程睡一秒
            long endTime = System.currentTimeMillis() + 10 * 1000;

            while (true) {
                int read = line.read(audioBytes, 0, line.available());
                if (read < 0) {
                    break;
                }
                boolean silence = DetectQuiet.readData(audioBytes);
                if (silence) {
                    num = 0;
                } else {
                    num++;
                }

                if (System.currentTimeMillis() > endTime) {
                    recordAudioThread.stopRecording();
                    break;
                }
            }

        } catch (Exception e) {
            log.error("读流出现错误");
            e.printStackTrace();
        } finally {
            line.stop();
            line.close();
        }
        try {
            String result = new VoiceUtil().getWord(path);
            log.info("=====================================");
            log.info("识别结果：{}", result);

        } catch (IOException e) {
            e.printStackTrace();
        } catch (UnsupportedAudioFileException e) {
            e.printStackTrace();
        }

    }

    public String identifyAudio(String path,int time) throws UnsupportedAudioFileException, IOException {

        RecordAudio thread = new RecordAudio(path);
        thread.start();
        try {
            Thread.sleep(1000 * time);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        thread.stopRecording();
        return new VoiceUtil().getWord(path);
    }

    public static void main(String[] args) {
        String path = "G:\\test\\voice_ai\\123.wav";
        try {
            new RecordFile().identifyAudio(path, 5);
        } catch (UnsupportedAudioFileException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}

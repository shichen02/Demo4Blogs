package com.shichen.ffmpeg;

import javax.sound.sampled.AudioFormat;
import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.DataLine;
import javax.sound.sampled.Mixer;
import javax.sound.sampled.TargetDataLine;
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
    public void audioRecord(){
        // 1. 设置音频解码器 ps 最好是系统支持的格式，否则 getLine() 会发生错误
        // 采样率 : 44.1k
        // 采样率位数 : 16 位
        // 通道数 : 2 (立体声)
        // 是否签名 : true
        // 字节顺序 bigEndian [true :big-endian 字节顺序 false: little-endian 详细见 ByteOrder 类]
        AudioFormat audioFormat = new AudioFormat(44100.0F, 16, 2, true, false);

        log.info("准备开启音频");

        // 通过AudioSystem获取本地音频混合器信息
        Mixer.Info[] minfoSet = AudioSystem.getMixerInfo();
        // 通过AudioSystem获取本地音频混合器
//        Mixer mixer = AudioSystem.getMixer(minfoSet[AUDIO_DEVICE_INDEX]);
        // 通过设置好的音频编解码器获取数据线信息
        DataLine.Info dataLineInfo = new DataLine.Info(TargetDataLine.class, audioFormat);

    }

    public static void main(String[] args) {

    }
}

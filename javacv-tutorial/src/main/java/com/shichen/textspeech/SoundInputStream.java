package com.shichen.textspeech;

import java.io.IOException;
import javax.sound.sampled.AudioFormat;
import javax.sound.sampled.AudioInputStream;
import javax.sound.sampled.AudioSystem;
import java.io.File;
import javax.sound.sampled.UnsupportedAudioFileException;
import lombok.Data;
import lombok.extern.slf4j.Slf4j;

/**
 * 本代码将调用javax.sound库
 * 得到音频输入流可无阻塞地读取（或跳过）的最大字节数
 * 音频输入流里声音的音频数据格式、音频输入流的帧长度
 *
 * @author tangsc
 * @date 2022/09/09
 */
@Slf4j
@Data
public class SoundInputStream {

    /**
     * 保存音频输入流
     */
    private AudioInputStream audioInputStream = null;
    /**
     * 保存音频输入流可无阻塞地读取（或跳过）的最大字节数
     */
    private int soundInputStreamMaxByte;
    /**
     * 保存音频输入流里声音的音频数据格式
     */
    private AudioFormat soundInputStreamSoundDataFormat = null;
    /**
     * 保存音频输入流的帧长度
     */
    private long soundInputStreamFrameLength;

    /**
     * 清空所有内容，释放内存
     *
     * @author tangsc
     * @date 2022/09/09
     */
    public void clearAll() {
        audioInputStream = null;
        soundInputStreamSoundDataFormat = null;
    }

    /**
     * 声音输入流
     *
     * @return
     * @author tangsc
     * @date 2022/09/09
     */
    public SoundInputStream(){
    }
    /**
     * 声音输入流 包装者
     *
     * @param soundFile 声音文件
     * @return
     * @throws IOException                   ioexception
     * @throws UnsupportedAudioFileException 不支持音频文件异常
     * @author tangsc
     * @date 2022/09/09
     */
    public SoundInputStream(File soundFile) throws IOException, UnsupportedAudioFileException {
        audioInputStream = AudioSystem.getAudioInputStream(soundFile);
        soundInputStreamMaxByte = audioInputStream.available();
        soundInputStreamSoundDataFormat = audioInputStream.getFormat();
        soundInputStreamFrameLength = audioInputStream.getFrameLength();
    }
    /**
     * 设置音频输入流
     *
     * @param soundFile 声音文件
     * @throws Exception 异常
     * @author tangsc
     * @date 2022/09/09
     */
    public SoundInputStream setSoundInputStreamByFile(File soundFile) throws Exception {
        audioInputStream = AudioSystem.getAudioInputStream(soundFile);
        soundInputStreamMaxByte = audioInputStream.available();
        soundInputStreamSoundDataFormat = audioInputStream.getFormat();
        soundInputStreamFrameLength = audioInputStream.getFrameLength();
        return this;

    }

    /**
     * 显示输入流
     *
     * @throws Exception 异常
     * @author tangsc
     * @date 2022/09/09
     */
    public void showSoundInputStream() throws Exception {
        log.info(
            "音频输入流可无阻塞地读取（或跳过）的最大字节数为：" + soundInputStreamMaxByte + " byte 即 " + soundInputStreamMaxByte / 1024.0 + " kb 或 " +
                soundInputStreamMaxByte / 1024.0 / 1024 + " mb");
        log.info("音频输入流里声音数据的音频格式为：" + soundInputStreamSoundDataFormat);
        log.info("音频输入流的帧长度为：" + soundInputStreamFrameLength + " frame");
        log.info("==============");
    }
}

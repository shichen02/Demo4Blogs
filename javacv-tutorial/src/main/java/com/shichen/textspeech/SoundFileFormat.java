package com.shichen.textspeech;

import java.io.IOException;
import javax.sound.sampled.AudioFileFormat;
import javax.sound.sampled.AudioFormat;
import javax.sound.sampled.AudioSystem;
import java.io.File;
import java.time.Duration;
import javax.sound.sampled.UnsupportedAudioFileException;
import lombok.Data;
import lombok.extern.slf4j.Slf4j;

/**
 * 本代码将调用javax.sound库，解析音频文件的格式
 * 得到例如音频时长、文件类型帧长、编码类型等信息
 *
 * @author tangsc
 * @date 2022/09/09
 */
@Slf4j
@Data
public class SoundFileFormat {
    /**
     * 保存音频文件格式
     */
    private AudioFileFormat soundFileFormat = null;
    /**
     * 保存音频文件类型，如 WAV
     */
    private AudioFileFormat.Type soundFileType = null;
    /**
     * 保存音频文件大小，以字节位为单位
     */
    private int soundFileLength;
    /**
     * 保存音频数据格式
     */
    private AudioFormat soundDataFormat = null;
    /**
     * 保存音频数据大小，以帧为单位
     */
    protected int soundDataFrameLength;
    /**
     * 保存音频数据大小，以字节为单位
     */
    private int soundDataByteLength;
    /**
     * 保存以此音频数据格式的声音，每秒播放或录制的帧数,即帧速率 frame/s
     */
    private float soundFrameRate;
    /**
     * 保存音频数据每帧的大小以字节为单位 frame/byte
     */
    private int soundFrameSize;
    /**
     * 保存以此音频数据格式的音频通道数（1表示单声道，2表示立体声）
     */
    private int soundChannels;
    /**
     * 保存以此音频数据格式的声音编码类型
     */
    private AudioFormat.Encoding soundEncoding = null;
    /**
     * 保存以此音频数据格式的声音，每秒播放或录制的样本数，即采样率 frame/s。
     */
    protected float soundSampleRate;
    /**
     * 保存音频数据每个样本的大小以字节为单位 个/byte
     */
    private int soundSampleSizeBits;
    /**
     * 保存音频数据的字节储存顺序
     */
    private boolean soundIsBigEndian;
    /**
     * 保存声音秒数
     */
    private Duration soundSecond = null;

    /**
     * 声音文件格式
     *
     * @return
     * @author tangsc
     * @date 2022/09/09
     */
    public SoundFileFormat() {
    }
    /**
     * 声音文件格式 装饰者
     *
     * @param soundFile 声音文件
     * @return
     * @throws UnsupportedAudioFileException 不支持音频文件异常
     * @throws IOException                   ioexception
     * @author tangsc
     * @date 2022/09/09
     */
    public SoundFileFormat(File soundFile) throws UnsupportedAudioFileException, IOException {
        soundFileFormat = AudioSystem.getAudioFileFormat(soundFile);
        soundDataFormat = soundFileFormat.getFormat();
        soundFileType = soundFileFormat.getType();
        soundFileLength = soundFileFormat.getByteLength();
        soundDataFrameLength = soundFileFormat.getFrameLength();
        soundFrameRate = soundDataFormat.getFrameRate();
        soundFrameSize = soundDataFormat.getFrameSize();
        soundDataByteLength = soundFrameSize * soundDataFrameLength;
        soundChannels = soundDataFormat.getChannels();
        soundEncoding = soundDataFormat.getEncoding();
        soundSampleRate = soundDataFormat.getSampleRate();
        soundSampleSizeBits = soundDataFormat.getSampleSizeInBits();
        soundIsBigEndian = soundDataFormat.isBigEndian();
        soundSecond = Duration.ofSeconds(Math.round(soundDataFrameLength / soundSampleRate));
    }

    /**
     * 清空所有内容，释放内存
     *
     * @author tangsc
     * @date 2022/09/09
     */
    public void clearAll() {
        soundFileFormat = null;
        soundFileType = null;
        soundDataFormat = null;
        soundEncoding = null;
        soundSecond = null;
    }

    /**
     * 获取音频文件格式
     *
     * @param soundFile
     * @throws Exception
     */
    public SoundFileFormat setSoundFileFormatByFile(File soundFile) throws UnsupportedAudioFileException, IOException {
        soundFileFormat = AudioSystem.getAudioFileFormat(soundFile);
        soundDataFormat = soundFileFormat.getFormat();
        soundFileType = soundFileFormat.getType();
        soundFileLength = soundFileFormat.getByteLength();
        soundDataFrameLength = soundFileFormat.getFrameLength();
        soundFrameRate = soundDataFormat.getFrameRate();
        soundFrameSize = soundDataFormat.getFrameSize();
        soundDataByteLength = soundFrameSize * soundDataFrameLength;
        soundChannels = soundDataFormat.getChannels();
        soundEncoding = soundDataFormat.getEncoding();
        soundSampleRate = soundDataFormat.getSampleRate();
        soundSampleSizeBits = soundDataFormat.getSampleSizeInBits();
        soundIsBigEndian = soundDataFormat.isBigEndian();
        soundSecond = Duration.ofSeconds(Math.round(soundDataFrameLength / soundSampleRate));
        return this;
    }

    /**
     * 显示声音文件格式
     *
     * @throws Exception 异常
     * @author tangsc
     * @date 2022/09/09
     */
    public void showSoundFileFormat() throws Exception {
        log.info("==============");
        log.info("音频文件大小为：{} byte 即 {} kb 或 {} mb", soundFileLength, soundFileLength / 1024.0, soundFileLength / 1024.0 / 1024.0);
//        log.info(
//            "音频文件大小为：" + soundFileLength + " byte 即 " + soundFileLength / 1024.0 + " kb 或 " + soundFileLength / 1024.0 / 1024.0 +
//                " mb");
        log.info("音频文件类型为：" + soundFileType);
        log.info("音频数据帧长度为：" + soundDataFrameLength + " frame");
        log.info("以此音频数据格式声音的帧速率为：" + soundFrameRate + " frame/s");
        log.info("音频数据每帧的大小为：" + soundFrameSize + " byte");
        log.info("音频数据的大小为：" + soundDataByteLength + " byte 即 " + soundDataByteLength / 1024.0 + " kb 或 " +
            soundDataByteLength / 1024.0 / 1024.0 + " mb");

        if (soundChannels == 1) {
            log.info("以此音频数据格式的音频通道为1：单声道");
        } else {
            log.info("以此音频数据格式的音频通道为2：立体声");
        }
        log.info("以此音频数据格式的声音编码类型为：" + soundEncoding);
        log.info("以此音频数据格式声音的采样率为：" + soundSampleRate + " frame/s");
        log.info("每个样本的位数为：" + soundSampleSizeBits + " bit");
        if (soundIsBigEndian == true) {
            log.info("音频数据的字节储存顺序为：big-endian");
        } else {
            log.info("音频数据的字节储存顺序为：littele-endian");
        }
//        log.info("音频时长(四舍五入后)为：" + soundSecond.toSeconds() + " s");
        log.info("音频时长(四舍五入后)为：" + soundSecond + " s");
        //log.info("音频时长为：" + soundSecond.toMinutes() + " min " + (soundSecond.toSeconds() - soundSecond.toMinutes() * 60) + " s");
        //log.info(soundSecond.toMinutes() + " : " + (soundSecond.toSeconds() - soundSecond.toMinutes() * 60));
        log.info("==============");
    }
}


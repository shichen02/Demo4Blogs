package com.shichen.speechrecognition;

import com.sun.media.sound.WaveFileReader;
import com.sun.media.sound.WaveFileWriter;
import java.io.BufferedInputStream;
import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.RandomAccessFile;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.Objects;
import javax.sound.sampled.AudioFileFormat;
import javax.sound.sampled.AudioFormat;
import javax.sound.sampled.AudioInputStream;
import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.UnsupportedAudioFileException;
import lombok.extern.slf4j.Slf4j;
import org.vosk.LibVosk;
import org.vosk.LogLevel;
import org.vosk.Model;
import org.vosk.Recognizer;

/**
 *  自动语音识别（Automatic Speech Recognition，ASR)
 *
 * @author tangsc
 * @date 2022/08/08
 */
@Slf4j
public class VoiceUtil {

    /**
     * vosk repository https://github.com/alphacep/vosk-api
     * vosk website    https://alphacephei.com/vosk/models
     */
    private String VOSKMODELPATH = "E:\\code\\cvtest\\modules\\vosk-model-small-cn-0.22";

    /**
     * 得到消息
     *
     * @param filePath 文件路径
     * @return {@link String }
     * @throws IOException                   ioexception
     * @throws UnsupportedAudioFileException 不支持音频文件异常
     * @author tangsc
     * @date 2022/08/08
     */
    public String getWord(String filePath) throws IOException, UnsupportedAudioFileException {
        if (Objects.isNull(VOSKMODELPATH)) {
            throw new RuntimeException("无效的VOS模块！");
        }
        byte[] bytes = Files.readAllBytes(Paths.get(filePath));
        // 转换为16KHZ
        reSamplingAndSave(bytes, filePath);
        File f = new File(filePath);
        RandomAccessFile rdf = null;
        rdf = new RandomAccessFile(f, "r");
        log.info("声音尺寸:{}", toInt(read(rdf, 4, 4)));
        log.info("音频格式:{}", toShort(read(rdf, 20, 2)));
        short track = toShort(read(rdf, 22, 2));
        log.info("1 单声道 2 双声道: {}", track);
        log.info("采样率、音频采样级别 16000 = 16KHz: {}", toInt(read(rdf, 24, 4)));
        log.info("每秒波形的数据量：{}", toShort(read(rdf, 22, 2)));
        log.info("采样帧的大小：{}", toShort(read(rdf, 32, 2)));
        log.info("采样位数：{}", toShort(read(rdf, 34, 2)));
        rdf.close();
        LibVosk.setLogLevel(LogLevel.WARNINGS);
        try (Model model = new Model(VOSKMODELPATH);
             InputStream ais = AudioSystem.getAudioInputStream(new BufferedInputStream(new FileInputStream(filePath)));
             // 采样率为音频采样率的声道倍数
             Recognizer recognizer = new Recognizer(model, 16000 * track)) {
            int nbytes;
            byte[] b = new byte[4096];
            int i = 0;
            while ((nbytes = ais.read(b)) >= 0) {
                i += 1;
                if (recognizer.acceptWaveForm(b, nbytes)) {
                    log.info("center:{}",recognizer.getResult());
                } else {
                    log.info("center:{}",recognizer.getPartialResult());
                }
            }
            String result = recognizer.getFinalResult();
            log.info("识别结果：{}", result);
            if (Objects.nonNull(result)) {
                return result;
            }
            return "";
        }
    }

    public static int toInt(byte[] b) {
        return (((b[3] & 0xff) << 24) + ((b[2] & 0xff) << 16) + ((b[1] & 0xff) << 8) + ((b[0] & 0xff) << 0));
    }

    public static short toShort(byte[] b) {
        return (short) ((b[1] << 8) + (b[0] << 0));
    }

    public static byte[] read(RandomAccessFile rdf, int pos, int length) throws IOException {
        rdf.seek(pos);
        byte result[] = new byte[length];
        for (int i = 0; i < length; i++) {
            result[i] = rdf.readByte();
        }
        return result;
    }

    public static void reSamplingAndSave(byte[] data, String path) throws IOException, UnsupportedAudioFileException {
        WaveFileReader reader = new WaveFileReader();
        AudioInputStream audioIn = reader.getAudioInputStream(new ByteArrayInputStream(data));
        AudioFormat srcFormat = audioIn.getFormat();
        int targetSampleRate = 16000;
        AudioFormat dstFormat = new AudioFormat(srcFormat.getEncoding(),
            targetSampleRate,
            srcFormat.getSampleSizeInBits(),
            srcFormat.getChannels(),
            srcFormat.getFrameSize(),
            srcFormat.getFrameRate(),
            srcFormat.isBigEndian());
        AudioInputStream convertedIn = AudioSystem.getAudioInputStream(dstFormat, audioIn);
        File file = new File(path);
        WaveFileWriter writer = new WaveFileWriter();
        writer.write(convertedIn, AudioFileFormat.Type.WAVE, file);
    }

    public static void main(String[] args) throws UnsupportedAudioFileException, IOException {
        new VoiceUtil().getWord("G:\\test\\voice_ai\\123.wav");
    }
}
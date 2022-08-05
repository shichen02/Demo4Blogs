package com.shichen.speechrecognition;


import java.io.File;
import java.io.IOException;
import java.nio.ByteBuffer;
import java.nio.ByteOrder;

import javax.sound.sampled.AudioInputStream;
import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.UnsupportedAudioFileException;
import lombok.extern.slf4j.Slf4j;


/**
 * 识别音频中是否有声音
 *
 * @author tangsc
 * @date 2022/08/04
 */
@Slf4j
public class DetectQuiet {

    public static void main(String[] args) throws UnsupportedAudioFileException, IOException {
        String filePath = "G:\\test\\voice_ai\\123.wav";
        loadFile(filePath);
//       loadFile("./data/安静.wav");
//		 loadFile("./data/有声.wav");
    }

    /**
     * 读取文件
     */
    public static void loadFile(String path) throws UnsupportedAudioFileException, IOException {
        File f = new File(path);
        AudioInputStream audioStream = AudioSystem.getAudioInputStream(f);

        readData(audioStream);
    }

    /**
     * 读取数据
     *
     * @throws IOException
     */
    public static void readData(AudioInputStream audioStream) throws IOException {
        byte[] frame = new byte[2048];

        DetectSoundFilter soundFilter = new DetectSoundFilter();

        while (true) {
            int read = audioStream.read(frame);
            if (read < 0) {
                break;
            }

            double[] byteToDouble = byteToDouble(frame);
            boolean silence = soundFilter.isSilence(byteToDouble);
            if (silence) {
                log.info("这是安静的");
            } else {
                log.info("这是有声音的");
            }
        }
    }

    public static boolean readData(byte[] frame) throws IOException {


        DetectSoundFilter soundFilter = new DetectSoundFilter();

        double[] byteToDouble = byteToDouble(frame);
        return soundFilter.isSilence(byteToDouble);

    }


    /**
     * pcm转成浮点
     *
     * @param data
     * @return
     */
    public static double[] byteToDouble(byte[] data) {
        ByteBuffer buf = ByteBuffer.wrap(data);

        buf.order(ByteOrder.BIG_ENDIAN);
        int i = 0;
        double[] dData = new double[data.length / 2];

        while (buf.remaining() > 1) {
            short s = buf.getShort();
            // real
            dData[i] = (double) s / 32767.0;
            ++i;
        }
        return dData;
    }

}


package com.shichen.textspeech;

import java.io.File;
import org.junit.Test;


/**
 * java.sound 包测试
 *
 * @author tangsc
 * @date 2022/09/09
 */
public class JavaSoundTest {

    /**
     * 测试读取音频文件格式
     *
     * @author tangsc
     * @date 2022/09/09
     */
    @Test
    public void testFileFormat() {
        SoundFileFormat fileFormat = new SoundFileFormat();
        File file = new File("G:\\test\\voice_ai\\1659318826367.wav");
        try {
            fileFormat.setSoundFileFormatByFile(file);
            fileFormat.showSoundFileFormat();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * 测试读入声音输入流
     *
     * @author tangsc
     * @date 2022/09/09
     */
    @Test
    public void testSoundInputStream() {
        SoundInputStream soundInputStream = new SoundInputStream();
        File file = new File("G:\\test\\voice_ai\\1659318826367.wav");
        try {
            soundInputStream.setSoundInputStreamByFile(file);
            soundInputStream.showSoundInputStream();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * 测试音频文件播放
     *
     * @author tangsc
     * @date 2022/09/09
     */
    @Test
    public void testPlay() {
        String path = "G:\\test\\voice_ai\\1_4_2.wav";
        SoundPlayer soundPlayer = new SoundPlayer(path);
        try {
            soundPlayer.start(false);
        } catch (Exception e) {
            e.printStackTrace();
        }
        long beginTime = System.currentTimeMillis();
        boolean pause = false;
        boolean continues = false;
        boolean stop = false;
        // 无限循环 是为了保证主线程一直在运行
        while (true) {
            //do noting
            if (System.currentTimeMillis() > (beginTime + 1000 * 10) && !pause) {
                soundPlayer.pause();
                pause = true;
            }
            if (System.currentTimeMillis() > (beginTime + 1000 * 20) && !continues) {
                soundPlayer.continues();
                continues = true;
            }
            if (System.currentTimeMillis() > (beginTime + 1000 * 30) && !stop) {
                soundPlayer.stop();
                stop = true;
            }

        }
    }
}
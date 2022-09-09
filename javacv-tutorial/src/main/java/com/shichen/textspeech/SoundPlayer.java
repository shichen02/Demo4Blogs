package com.shichen.textspeech;

import java.io.File;
import lombok.extern.slf4j.Slf4j;
//import java.time.Instant;

@Slf4j
public class SoundPlayer {

    private SoundInputStream soundInputStream;
    /**
     * 保存为字符串类型的绝对地址，例："C:\\Users\\17512\\Desktop\\bgm.wav";
     */
    private String soundFilePath = null;
    /**
     * 保存音频文件
     */
    private File soundFile = null;

    /**
     * 记录音频是否播放
     */
    private volatile boolean running = false;  //
    /**
     * 记录是否循环播放
     */
    private boolean isLoop = false;
    /**
     * 播放音频的任务主线程（音频数据读取输出）
     */
    private Thread playThread = null;
    /**
     * 播放音频的任务次线程（控制主线程暂停、继续）
     */
    private Thread secondThread = null;
    /**
     * 缓冲区
     */
    private byte tempBuffer[] = null;
    /**
     * 保存得到的采样数
     */
    private int count;
    //private static Instant startTime = null; 待后续开发
    //private static Instant endTime = null;待后续开发
    private SoundSourceDataLine dsdl;

    /**
     * 清空所有内容，释放内存
     *
     * @author tangsc
     * @date 2022/09/09
     */
    private void clearAll() {
        soundFile = null;
        playThread = null;
        secondThread = null;
        tempBuffer = null;
        //startTime = null;待后续开发
        //endTime = null;待后续开发
    }

    /**
     * 初始化需要设置音频文件绝对路径
     *
     * @param absolutePath 绝对路径
     * @author tangsc
     * @date 2022/09/09
     */
    public SoundPlayer(String absolutePath)  {
        soundFilePath = absolutePath;
        //数据流传输过程：AudioInputStream -> SourceDataLine;
        if (soundFilePath == null) {
            log.error("未设置声音文件绝对路径");
            return;
        }

    }


    private void playMusic() throws Exception{
        soundFile = new File(soundFilePath);

        SoundFileFormat soundFileFormat = null;
        try {
            soundFileFormat = new SoundFileFormat(soundFile);
            soundFileFormat.showSoundFileFormat();
        } catch (Exception e) {
            log.error("声音格式不支持", e);
        }

        this.soundInputStream = new SoundInputStream(soundFile);
        this.dsdl =  new SoundSourceDataLine().loadSourceDataLine(soundFileFormat.getSoundDataFormat());
        try {
            synchronized (this) {
                running = true;
            }
            //通过数据行读取音频数据流，发送到混音器;
            log.info("播放开始");
            tempBuffer = new byte[256];
            //音频输入流读取指定最大大小（tempBuff.length）数据并传输到源数据线，off为传输数据在字节数组（tempBuffer）开始保存数据的位置，最终读取大小保存进count
            while ((count = soundInputStream.getAudioInputStream().read(tempBuffer)) > 0) {
                log.debug("音频输入流读取了" + count + "个字节");
                synchronized (this) {
                    while (!running) {
                        wait();
                    }
                }
                //源数据线进行一次数据输出，将从字节数组中位置off开始将读取到的数据输出到缓冲区（输出大小为count）（混音器从混音器读取数据播放）
                dsdl.getSourceDataLine().write(tempBuffer, 0, count);
            }
            stopMusic();
        } catch (Exception ex) {
            ex.printStackTrace();
        }

    }

    private void playMusic(boolean isLoop) throws Exception {
        try {
            if (isLoop) {
                while (isLoop) {
                    playMusic();
                }
            } else {
                playMusic();
            }

        } catch (Exception ex) {
            ex.printStackTrace();
        }

    }


    /**
     * 暂停播放音频
     */
    private void pauseMusic() {
        synchronized (this) {
            running = false;
            notifyAll();
        }
    }

    /**
     * 继续播放音乐
     */
    private void continueMusic() {
        synchronized (this) {
            running = true;
            notifyAll();
        }
    }

    /**
     * 停止播放音频
     *
     * @throws Exception 异常
     * @author tangsc
     * @date 2022/09/09
     */
    private void stopMusic() throws Exception {
        synchronized (this) {
            running = false;
            isLoop = false;
            log.info("播放结束");
            //清空数据行并关闭
            this.dsdl.getSourceDataLine().drain();
            this.dsdl.getSourceDataLine().close();
            this.dsdl.clearAll();
            clearAll();
        }
    }

    /**
     * 外部调用控制方法:生成音频主线程；
     *
     * @param loop
     * @throws Exception 异常
     * @author tangsc
     * @date 2022/09/09
     */
    public void start(boolean loop) throws Exception {
        isLoop = loop;
        playThread = new Thread(new Runnable() {
            public void run() {
                try {
                    playMusic(isLoop);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        });
        playThread.start();
    }

    /**
     * 外部调用控制方法：暂停音频线程
     *
     * @author tangsc
     * @date 2022/09/10
     */
    public void pause() {
        secondThread = new Thread(new Runnable() {
            public void run() {
                System.out.println("调用暂停");
                pauseMusic();
            }
        });
        secondThread.start();
    }

    /**
     * 外部调用控制方法：继续音频线程
     *
     * @author tangsc
     * @date 2022/09/10
     */
    public void continues() {
        secondThread = new Thread(new Runnable() {
            public void run() {
                log.info("调用继续");
                continueMusic();
            }
        });
        secondThread.start();
    }

    //外部调用控制方法：结束音频线程
    public void stop() {
        secondThread = new Thread(new Runnable() {
            public void run() {
                log.info("调用停止");
                try {
                    stopMusic();
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        });
        secondThread.start();
    }
}


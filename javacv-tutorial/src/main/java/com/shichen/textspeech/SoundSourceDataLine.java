package com.shichen.textspeech;

import javax.sound.sampled.AudioFormat;
import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.DataLine;
import javax.sound.sampled.SourceDataLine;
import lombok.Data;

/**
 * 本代码将调用javax.sound库
 * 得到数据线信息并加载源数据线
 *
 * @author tangsc
 * @date 2022/09/09
 */
@Data
public class SoundSourceDataLine {
    /**
     * 保存数据线信息
     */
    private DataLine.Info dataLineInformationl = null;
    /**
     * 保存源数据线
     */
    private SourceDataLine sourceDataLine = null;

    /**
     * 清空所有内容，释放内存
     *
     * @author tangsc
     * @date 2022/09/09
     */
    public void clearAll() {
        this.dataLineInformationl = null;
        this.sourceDataLine = null;
    }

    /**
     * 加载源数据线
     *
     * @param soundInputStreamSoundDataFormat 声音输入流数据格式
     * @throws Exception 异常
     * @author tangsc
     * @date 2022/09/09
     */
    public SoundSourceDataLine loadSourceDataLine(AudioFormat soundInputStreamSoundDataFormat) throws Exception {
        //定义数据线信息，即包装音频信息（记录数据线类，音频数据格式，缓冲区大小）
        this.dataLineInformationl = new DataLine.Info(SourceDataLine.class, soundInputStreamSoundDataFormat);
        //获得源数据线
        this.sourceDataLine = (SourceDataLine) AudioSystem.getLine(dataLineInformationl);
        //设置源数据线打开的音频数据格式和源数据线的缓冲区大小(可指定)
        this.sourceDataLine.open(soundInputStreamSoundDataFormat);
        //源数据线开始工作
        this.sourceDataLine.start();
        return this;
    }
}


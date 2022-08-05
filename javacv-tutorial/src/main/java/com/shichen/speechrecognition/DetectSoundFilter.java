package com.shichen.speechrecognition;


/**
 * 识别声音过滤器
 * 用于判断有没有声音，甚至可以判断声音的响度
 *
 * @author tangsc
 * @date 2022/08/04
 */
public class DetectSoundFilter {

    /**
     * 声音响度阈值 // db
     */
//    public double soundThreshold = -60;
    public double soundThreshold = -80;
    /**
     * 是否安静
     *
     * @param buffer
     * @return boolean
     * @author tangsc
     * @date 2022/08/04
     */
    public boolean isSilence(final double[] buffer) {
        double currentSPL = soundPressureLevel(buffer);
        System.out.println(currentSPL);
        return currentSPL < soundThreshold;
    }

    /**
     * 声压计算
     * @param buffer
     * @return
     */
    private double soundPressureLevel(final double[] buffer) {
        double value = Math.pow(localEnergy(buffer), 0.5);
        value = value / buffer.length;
        return linearToDecibel(value);
    }

    /**
     * 线性转换为dB值
     * @param value
     * @return
     */
    private double linearToDecibel(final double value) {
        return 20.0 * Math.log10(value);
    }

    /**
     * 音频帧去负数
     * @param buffer
     * @return
     */
    private double localEnergy(final double[] buffer) {
        double power = 0.0D;
        for (double element : buffer) {
            power += element * element;
        }
        return power;
    }
}



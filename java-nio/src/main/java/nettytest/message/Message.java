package nettytest.message;

public class Message {
    /**
     * ==================设置=========================
     */
    /**
     * 实时波束设置
     */
    public static final Byte cfg_realbeam = 0x03;
    /**
     * 开机唤醒波速设置
     */
    public static final Byte cfg_wakeup = 0x04;
    /**
     * 记录音频设置
     */
    public static final Byte cfg_DUMP_AUDIO = 0x07;

    /**
     * 静音设置
     */
    public static final Byte cfg_mute = 0x08;
    /**
     * 4&6麦切换设置
     */
    public static final Byte cfg_micnumber = 0x0B;

    /**
     * 上报信息格式选择
     */
    public static final Byte cfg_jsonflag = 0x0c;
    /**
     * 输出音量设置
     */
    public static final Byte cfg_hpvolume = 0x14;
    /**
     * 麦克风增益设置
     */
    public static final Byte cfg_micvolume = 0x15;
    /**
     * 参考信号增益设置
     */
    public static final Byte cfg_refvolume = 0x16;
    /**
     * 休眠模式
     */
    public static final Byte cfg_sleep = 0x20;
    /**
     * Json送初始化命令发送返回
     */
    public static final Byte json_init = 0x2f;
    /**
     * 主版本号和次版本号
     */
    public static final Byte cfg_master_cfg_minor = 0x3f;
    /**
     * =====================设置============================
     */

    /**
     * 云端返回 iat
     */
    public static final Byte iat = 0x40;
    /**
     * 云端返回 nlp
     */
    public static final Byte nlp = 0x41;
    /**
     * 云端返回ttp
     */
    public static final Byte ttp = 0x43;
    /**
     * 云端返回其他
     */
    public static final Byte other = 0x45;
    /**
     * 离线识别返回
     */
    public static final Byte off_line = 0x46;
    /**
     * tts 主动合成
     */
    public static final Byte tts = 0x4f;
    /**
     * 主动唤醒
     */
    public static final Byte notify = 0x5f;
    /**
     * WiFi 配置信息返回
     */
    public static final Byte wifi_conf = 0x6f;


    private Byte model;

    private Byte value;


    public Byte getModel() {
        return model;
    }

    public void setModel(Byte model) {
        this.model = model;
    }

    public Byte getValue() {
        return value;
    }

    public void setValue(Byte value) {
        this.value = value;
    }
}

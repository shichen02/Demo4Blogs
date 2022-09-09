package com.shichen.textspeech;

import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.Mixer;
import java.util.Vector;

/**
 * 本代码将调用javax.sound库
 * 得到系统中已经安装的各种混音器
 *
 * @author tangsc
 * @date 2022/09/09
 */
public class MixerInformation {
    //音频混音器相关===========================================================
    //保存系统已安装的音频混音器集合
    private static Mixer.Info[] mixerInformation = null;
    //保存音频混音器的描述
    private static Vector<String> mixerDescription = null;
    //保存音频混音器的名称
    private static Vector<String> mixerName = null;
    //保存音频混音器的供应商
    private static Vector<String> mixerVendor = null;
    //保存混音器的版本
    private static Vector<String> mixerVersion = null;

    //清空所有内容，释放内存
    protected static void clearAll(){
        Mixer.Info[] mixerInformation = null;
        String mixerDescription = null;
        String mixerName = null;
        String mixerVendor = null;
        String mixerVersion = null;
    }

    //获取音频混音器
    protected static void getMixerInformation(){
        mixerInformation = AudioSystem.getMixerInfo();
        mixerDescription = new Vector<String>();
        mixerName = new Vector<String>();
        mixerVendor = new Vector<String>();
        mixerVersion = new Vector<String>();
        for(Mixer.Info i : mixerInformation){
            mixerDescription.add( i.getDescription() );
            mixerName.add( i.getName() );
            mixerVendor.add( i.getVendor() );
            mixerVersion.add( i.getVersion() );
        }
    }

    protected static void showMixerInformation(){
        for(int i = 0; i < mixerInformation.length; i++){
            System.out.println("系统已安装的音频混音器" + (i + 1) + " ：" + mixerInformation[i]);
            System.out.println("描述：" + mixerDescription.get(i));
            System.out.println("名称：" + mixerName.get(i));
            System.out.println("供应商：" + mixerVendor.get(i));
            System.out.println("版本：" + mixerVersion.get(i));
            System.out.println("==============");
        }
    }

}



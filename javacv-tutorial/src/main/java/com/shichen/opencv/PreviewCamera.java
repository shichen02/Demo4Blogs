package com.shichen.opencv;

import javax.swing.JFrame;
import org.bytedeco.javacv.CanvasFrame;
import org.bytedeco.javacv.FrameGrabber;
import org.bytedeco.javacv.OpenCVFrameGrabber;

/**
 * 01 使用 opencv 进行相机预览
 *
 * @author tangsc
 * @date 2022/08/03
 */
public class PreviewCamera {
    public static void main(String[] args) {
        // 1. 新建 opencv 抓取器，一般的电脑和移动设备中的默认摄像头序号是 0
        OpenCVFrameGrabber grabber = new OpenCVFrameGrabber(0);

        try {
            grabber.start();
        } catch (FrameGrabber.Exception e) {
            e.printStackTrace();
        }

        // 2. 创建预览窗口
        CanvasFrame preview = new CanvasFrame("摄像头预览");
        preview.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);

        while (preview.isDisplayable()){
            try {
                // 3. 抓取并展示
                preview.showImage(grabber.grab());
            } catch (FrameGrabber.Exception e) {
                e.printStackTrace();
            }
        }

        // 4. 释放资源
        try {
            grabber.close();
        } catch (FrameGrabber.Exception e) {
            e.printStackTrace();
        }
    }
}

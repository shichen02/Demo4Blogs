package test.paddle.ocr;

import ai.djl.*;
import ai.djl.inference.Predictor;
import ai.djl.modality.Classifications;
import ai.djl.modality.cv.Image;
import ai.djl.modality.cv.ImageFactory;
import ai.djl.modality.cv.output.*;
import ai.djl.modality.cv.util.NDImageUtils;
import ai.djl.ndarray.*;
import ai.djl.repository.zoo.*;
import ai.djl.paddlepaddle.zoo.cv.objectdetection.PpWordDetectionTranslator;
import ai.djl.paddlepaddle.zoo.cv.imageclassification.PpWordRotateTranslator;
import ai.djl.paddlepaddle.zoo.cv.wordrecognition.PpWordRecognitionTranslator;
import ai.djl.translate.*;
import cn.hutool.core.io.file.PathUtil;
import cn.hutool.core.io.resource.ResourceUtil;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.net.URL;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.concurrent.ConcurrentHashMap;

/**
 * DJL OCR 采用 paddle-engine 实现。
 * demo 地址：https://docs.djl.ai/jupyter/paddlepaddle/paddle_ocr_java_zh.html
 *
 * @author tangsc
 * @date 2022/09/12
 */
public class OpticalCharacterRecognition {

    private static String level = "debug";
    /**
     * 框选文字
     */
    private Predictor<Image, DetectedObjects> detector = null;
    /**
     * 判断旋转
     */
    private Predictor<Image, Classifications> rotateClassifier = null;
    /**
     * 图片转文字
     */
    private Predictor<Image, String> recognizer = null;

    public void testOcr() throws IOException, ModelNotFoundException, MalformedModelException,
        TranslateException {
        // 载入图片
        String url = "https://resources.djl.ai/images/flight_ticket.jpg";
        Image img = ImageFactory.getInstance().fromUrl(url);
//        Path path = Paths.get("/Users/tangsc/Pictures/test.png");
//        Image img = ImageFactory.getInstance().fromFile(path);
        img.getWrappedImage();

        // 进行文字框选检测
        DetectedObjects detectedObj = getDetectedObjects(img);
        Image newImage = img.duplicate();
        newImage.drawBoundingBoxes(detectedObj);
        newImage.getWrappedImage();
        saveImg(newImage, "detected");
        // 得到框选后的其中一个子项
        List<DetectedObjects.DetectedObject> boxes = detectedObj.items();
        Image sample = getSubImage(img, boxes.get(5).getBoundingBox());
        sample.getWrappedImage();
        saveImg(sample, "subImg");

        List<String> names = new ArrayList<>();
        List<Double> prob = new ArrayList<>();
        List<BoundingBox> rect = new ArrayList<>();

        for (int i = 0; i < boxes.size(); i++) {
            Image subImg = getSubImage(img, boxes.get(i).getBoundingBox());
            if (subImg.getHeight() * 1.0 / subImg.getWidth() > 1.5) {
                subImg = rotateImg(subImg);
            }
            Classifications.Classification result = getRotateResult(subImg);
            if ("Rotate".equals(result.getClassName()) && result.getProbability() > 0.8) {
                subImg = rotateImg(subImg);
            }
            String name = pictureToText(subImg);
            names.add(name);
            prob.add(-1.0);
            rect.add(boxes.get(i).getBoundingBox());
        }
        newImage.drawBoundingBoxes(new DetectedObjects(names, prob, rect));
        newImage.getWrappedImage();
        saveImg(newImage, "result");
    }

    private void saveImg(Image sample, String fileName) throws IOException {
        if (!level.equals("debug")) {
            return;
        }
        File file = new File("D:\\Develop\\test\\" + fileName + ".png");
        FileOutputStream fileOutputStream = new FileOutputStream(file);
        sample.save(fileOutputStream, "png");
    }

    /**
     * 进行文字检测
     *
     * @param img img
     * @return {@link DetectedObjects }
     * @throws IOException             ioexception
     * @throws ModelNotFoundException  模型没有发现异常
     * @throws MalformedModelException 畸形模型异常
     * @throws TranslateException      翻译异常
     * @author tangsc
     * @date 2022/09/12
     */
    private DetectedObjects getDetectedObjects(Image img)
        throws IOException, ModelNotFoundException, MalformedModelException, TranslateException {
        if (Objects.isNull(detector)) {
            Criteria<Image, DetectedObjects> criteria1 = Criteria.builder()
                .optEngine("PaddlePaddle")
                .setTypes(Image.class, DetectedObjects.class)
                .optModelUrls("https://resources.djl.ai/test-models/paddleOCR/mobile/det_db.zip")
                .optTranslator(new PpWordDetectionTranslator(new ConcurrentHashMap<String, String>()))
                .build();

            ZooModel<Image, DetectedObjects> detectionModel = criteria1.loadModel();
            detector = detectionModel.newPredictor();
        }

        return detector.predict(img);
    }

//    private DetectedObjects getDetectedObjects(Image img)
//            throws IOException, ModelNotFoundException, MalformedModelException, TranslateException {
//        if (Objects.isNull(detector)) {
//            Criteria<Image, DetectedObjects> criteria1 = Criteria.builder()
//                    .setTypes(Image.class, DetectedObjects.class)
//                    .optEngine("OnnxRuntime")
//                    .optModelPath(Paths.get("D:/Develop/Code/Demo4Blogs/djl-demo/src/main/resources"))
//                    .optModelName("model/sim_best")
//                    .optTranslator(new PpWordDetectionTranslator(new ConcurrentHashMap<String, String>()))
//                    .build();
//
//            ZooModel<Image, DetectedObjects> detectionModel = criteria1.loadModel();
//            detector = detectionModel.newPredictor();
//        }
//
//        return detector.predict(img);
//    }

    /**
     * 用于确认图片以及文字是否需要旋转
     *
     * @throws ModelNotFoundException
     * @throws MalformedModelException
     * @throws IOException
     */
    private Classifications.Classification getRotateResult(Image sample)
        throws ModelNotFoundException, MalformedModelException, IOException,
        TranslateException {
        if (Objects.isNull(rotateClassifier)) {
            Criteria<Image, Classifications> criteria2 = Criteria.builder()
                .optEngine("PaddlePaddle")
                .setTypes(Image.class, Classifications.class)
                .optModelUrls("https://resources.djl.ai/test-models/paddleOCR/mobile/cls.zip")
                .optTranslator(new PpWordRotateTranslator())
                .build();
            ZooModel<Image, Classifications> rotateModel = criteria2.loadModel();
            rotateClassifier = rotateModel.newPredictor();
        }
        Classifications predict = rotateClassifier.predict(sample);
        System.out.println(predict);
        return predict.best();
    }

    /**
     * 图片转文字的
     *
     * @param sample 样本
     * @throws ModelNotFoundException  模型没有发现异常
     * @throws MalformedModelException 畸形模型异常
     * @throws IOException             io异常
     * @throws TranslateException      翻译异常
     * @author tangsc
     * @date 2022/09/12
     */
    public String pictureToText(Image sample)
        throws ModelNotFoundException, MalformedModelException, IOException, TranslateException {
        if (Objects.isNull(recognizer)) {
            Criteria<Image, String> criteria3 = Criteria.builder()
                .optEngine("PaddlePaddle")
                .setTypes(Image.class, String.class)
                .optModelUrls("https://resources.djl.ai/test-models/paddleOCR/mobile/rec_crnn.zip")
                .optTranslator(new PpWordRecognitionTranslator())
                .build();

            ZooModel<Image, String> recognitionModel = criteria3.loadModel();
            recognizer = recognitionModel.newPredictor();
        }

        return recognizer.predict(sample);
    }

    /**
     * 得到子图像
     *
     * @param img img
     * @param box 盒子
     * @return {@link Image }
     * @author tangsc
     * @date 2022/09/12
     */
    private Image getSubImage(Image img, BoundingBox box) {
        Rectangle rect = box.getBounds();
        double[] extended = extendRect(rect.getX(), rect.getY(), rect.getWidth(), rect.getHeight());
        int width = img.getWidth();
        int height = img.getHeight();
        int[] recovered = {
            (int) (extended[0] * width),
            (int) (extended[1] * height),
            (int) (extended[2] * width),
            (int) (extended[3] * height)
        };
        return img.getSubImage(recovered[0], recovered[1], recovered[2], recovered[3]);
    }

    /**
     * 延长矩形
     *
     * @param xmin   xmin
     * @param ymin   ymin
     * @param width  宽度
     * @param height 高度
     * @return {@link double[] }
     * @author tangsc
     * @date 2022/09/12
     */
    private double[] extendRect(double xmin, double ymin, double width, double height) {
        double centerx = xmin + width / 2;
        double centery = ymin + height / 2;
        if (width > height) {
            width += height * 2.0;
            height *= 3.0;
        } else {
            height += width * 2.0;
            width *= 3.0;
        }
        double newX = centerx - width / 2 < 0 ? 0 : centerx - width / 2;
        double newY = centery - height / 2 < 0 ? 0 : centery - height / 2;
        double newWidth = newX + width > 1 ? 1 - newX : width;
        double newHeight = newY + height > 1 ? 1 - newY : height;
        return new double[] {newX, newY, newWidth, newHeight};
    }

    /**
     * 旋转图片
     *
     * @param image 图像
     * @return {@link Image }
     * @author tangsc
     * @date 2022/09/12
     */
    private Image rotateImg(Image image) {
        try (NDManager manager = NDManager.newBaseManager()) {
            NDArray rotated = NDImageUtils.rotate90(image.toNDArray(manager), 1);
            return ImageFactory.getInstance().fromNDArray(rotated);
        }
    }


    public static void main(String[] args) {
        try {
            new OpticalCharacterRecognition().testOcr();
        } catch (IOException e) {
            e.printStackTrace();
        } catch (ModelNotFoundException e) {
            e.printStackTrace();
        } catch (MalformedModelException e) {
            e.printStackTrace();
        } catch (TranslateException e) {
            e.printStackTrace();
        }
    }

}

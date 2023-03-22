package test.paddle.ocr;

import ai.djl.modality.cv.Image;
import ai.djl.modality.cv.output.DetectedObjects;
import ai.djl.ndarray.NDList;
import ai.djl.translate.NoBatchifyTranslator;
import ai.djl.translate.TranslatorContext;

/**
 * ImageTranslator
 *
 * @Author tangsc
 * @Date: 2023/3/22
 */
public class ImageTranslator implements NoBatchifyTranslator<Image, DetectedObjects> {

    @Override
    public DetectedObjects processOutput(TranslatorContext translatorContext, NDList ndList) throws Exception {
        return null;
    }

    @Override
    public NDList processInput(TranslatorContext translatorContext, Image image) throws Exception {
        return null;
    }


}

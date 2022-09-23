package test.paddle.longasr;

import ai.djl.Device;
import ai.djl.inference.Predictor;
import ai.djl.ndarray.NDArray;
import ai.djl.ndarray.NDManager;
import ai.djl.repository.zoo.Criteria;
import ai.djl.repository.zoo.ZooModel;
import org.apache.commons.lang3.tuple.Pair;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import test.paddle.longasr.utils.AudioProcess;
import test.paddle.longasr.utils.SpeechRecognition;

/**
 * 预测短语音
 *
 * <p>https://github.com/yeyupiaoling/PaddlePaddle-DeepSpeech
 *
 * @author calvin
 * @mail 179209347@qq.com
 * @website www.aias.top
 */
public final class SpeechRecognitionExampleS {
    private static final Logger logger = LoggerFactory.getLogger(SpeechRecognitionExampleS.class);

    private SpeechRecognitionExampleS() {
    }

    public static void main(String[] args) throws Exception {

        NDManager manager = NDManager.newBaseManager(Device.cpu());
        NDArray audioFeature = AudioProcess.processUtterance(manager, "E:\\code\\Demo4Blogs\\djl-demo\\src\\main\\resources\\test.wav");
        // System.out.println(audioFeature.toDebugString(1000000000, 1000, 10, 1000));

        SpeechRecognition speakerEncoder = new SpeechRecognition();
        Criteria<NDArray, Pair> criteria = speakerEncoder.criteria();

        try (ZooModel<NDArray, Pair> model = criteria.loadModel();
             Predictor<NDArray, Pair> predictor = model.newPredictor()) {

            logger.info("input audio: {}", "src/test/resources/test.wav");

            Pair result = predictor.predict(audioFeature);
            logger.info("Score : " + result.getLeft());
            logger.info("Words : " + result.getRight());
        }
    }
}

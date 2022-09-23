package test.paddle.longasr.utils;

import ai.djl.ndarray.NDArray;
import ai.djl.repository.zoo.Criteria;
import ai.djl.training.util.ProgressBar;
import java.nio.file.Paths;
import org.apache.commons.lang3.tuple.Pair;

public class SpeechRecognition {
  public SpeechRecognition() {}

  public Criteria<NDArray, Pair> criteria() {
    Criteria<NDArray, Pair> criteria =
        Criteria.builder()
            .setTypes(NDArray.class, Pair.class)
//            .optModelUrls(
//                "https://aias-home.oss-cn-beijing.aliyuncs.com/models/speech_models/deep_speech.zip")
//            .optModelUrls("/Users/calvin/aias_projects/PaddlePaddle-DeepSpeech/models/infer/")
             .optModelPath(Paths.get("G:\\test\\ai-model\\deep_speech.zip"))
            .optTranslator(new AudioTranslator())
            .optEngine("PaddlePaddle") // Use PaddlePaddle engine
            
            .optProgress(new ProgressBar())
            .build();

    return criteria;
  }
}

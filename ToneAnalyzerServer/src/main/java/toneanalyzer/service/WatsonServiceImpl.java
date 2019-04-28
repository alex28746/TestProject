package toneanalyzer.service;

import com.google.gson.*;
import com.ibm.watson.tone_analyzer.v3.ToneAnalyzer;
import com.ibm.watson.tone_analyzer.v3.model.ToneOptions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import toneanalyzer.model.EmotionModel;

@Service
public class WatsonServiceImpl implements WatsonService {

    @Autowired
    private ToneAnalyzer toneAnalyzer;

    private static final String DOCUMENT_TONE = "document_tone";
    private static final String TONES = "tones";
    private static final int FIRST_ELEMENT = 0;

    @Override
    public EmotionModel getEmotion(String message) {
        ToneOptions toneOptions = new ToneOptions.Builder()
                .text(message)
                .build();

        String answer = toneAnalyzer.tone(toneOptions).execute().getResult().toString();
        return convertStringToModel(answer);
    }

    private EmotionModel convertStringToModel(String obj) {
        try {
            JsonParser parser = new JsonParser();
            JsonObject jsonObject = parser.parse(obj).getAsJsonObject();

            JsonObject document_tone = jsonObject.getAsJsonObject(DOCUMENT_TONE);
            JsonArray tones = document_tone.getAsJsonArray(TONES);

            JsonElement j = tones.get(FIRST_ELEMENT);

            Gson gson = new Gson();
            return gson.fromJson(j, EmotionModel.class);
        } catch (Exception e) {
            EmotionModel emotionModel = new EmotionModel();
            emotionModel.setDisplayName("unknown");
            return emotionModel;
            // TODO. catch при подключении левые сообщения летят. Обработать их.
            // TODO. Если настроение не распознано, то ничего не возвращает. Проверить этот ответ.
            // TODO. I am so sexy < это сообщение.
        }
    }
}

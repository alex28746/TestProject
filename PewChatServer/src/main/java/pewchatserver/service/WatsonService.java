package pewchatserver.service;

import pewchatserver.model.EmotionModel;

public interface WatsonService {
    EmotionModel getEmotion(String message);
}

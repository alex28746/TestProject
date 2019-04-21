package toneanalyzer.service;

import toneanalyzer.model.EmotionModel;

public interface WatsonService {
    EmotionModel getEmotion(String message);
}

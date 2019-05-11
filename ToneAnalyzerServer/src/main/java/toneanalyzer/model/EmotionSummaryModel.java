package toneanalyzer.model;

import java.util.*;

public class EmotionSummaryModel {
    private Integer analytical = 0;
    private Integer anger = 0;
    private Integer fear = 0;
    private Integer joy = 0;
    private Integer sadness = 0;
    private Integer tentative = 0;

    public void incrementAnalytical() {
        this.analytical++;
    }

    public void incrementAnger() {
        this.anger++;
    }

    public void incrementFear() {
        this.fear++;
    }

    public void incrementJoy() {
        this.joy++;
    }

    public void incrementSadness() {
        this.sadness++;
    }

    public void incrementTentative() {
        this.tentative++;
    }

    public Map<Emotion, Double> getAverage() {
        Map<Emotion, Double> result = new HashMap<>();
        List<Integer> emotionsAmount = Arrays.asList(this.analytical, this.anger, this.fear, this.joy, this.sadness, this.tentative);
        Double sum = emotionsAmount.stream().mapToDouble(Integer::intValue).sum();

        result.put(Emotion.ANALYTICAL, sum/this.analytical);
        result.put(Emotion.ANGER, sum/this.anger);
        result.put(Emotion.FEAR, sum/this.fear);
        result.put(Emotion.JOY, sum/this.joy);
        result.put(Emotion.SADNESS, sum/this.sadness);
        result.put(Emotion.TENTATIVE, sum/this.tentative);
        return result;
    }

    public Emotion getMax() {
        Map<Emotion, Integer> emotionIntegerMap = new HashMap<>();
        emotionIntegerMap.put(Emotion.ANALYTICAL, this.analytical);
        emotionIntegerMap.put(Emotion.ANGER, this.anger);
        emotionIntegerMap.put(Emotion.FEAR, this.fear);
        emotionIntegerMap.put(Emotion.JOY, this.joy);
        emotionIntegerMap.put(Emotion.SADNESS, this.sadness);
        emotionIntegerMap.put(Emotion.TENTATIVE, this.tentative);

        Integer maxEmotionValue = Collections.max(emotionIntegerMap.entrySet(), Comparator.comparingInt(Map.Entry::getValue)).getValue();
        Emotion maxEmotion = Collections.max(emotionIntegerMap.entrySet(), Comparator.comparingInt(Map.Entry::getValue)).getKey();

        if(maxEmotionValue > 0) {
            return maxEmotion;
        } else {
            return Emotion.UNKNOWN;
        }
    }

    @Override
    public String toString() {
        return "EmotionSummaryModel{" +
                "analytical=" + analytical +
                ", anger=" + anger +
                ", fear=" + fear +
                ", joy=" + joy +
                ", sadness=" + sadness +
                ", tentative=" + tentative +
                '}';
    }
}

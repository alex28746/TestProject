package toneanalyzer.model;

import com.google.gson.annotations.SerializedName;

public class EmotionModel {

    @SerializedName("score")
    private Double score;

    @SerializedName("tone_id")
    private String toneId;

    @SerializedName("tone_name")
    private String displayName;

    public Double getScore() {
        return score;
    }

    public void setScore(Double score) {
        this.score = score;
    }

    public String getToneId() {
        return toneId;
    }

    public void setToneId(String toneId) {
        this.toneId = toneId;
    }

    public String getDisplayName() {
        return displayName;
    }

    public void setDisplayName(String displayName) {
        this.displayName = displayName;
    }

    @Override
    public String toString() {
        return "EmotionModel{" +
                "score=" + score +
                ", toneId='" + toneId + '\'' +
                ", displayName='" + displayName + '\'' +
                '}';
    }
}

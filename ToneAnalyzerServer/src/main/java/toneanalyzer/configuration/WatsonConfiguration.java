package toneanalyzer.configuration;

import com.ibm.cloud.sdk.core.service.security.IamOptions;
import com.ibm.watson.tone_analyzer.v3.ToneAnalyzer;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class WatsonConfiguration {

    @Value("${watson.api-key}")
    private String API_KEY;

    @Value("${watson.version}")
    private String VERSION;

    @Value("${watson.end-point}")
    private String END_POINT;

    @Bean
    public ToneAnalyzer toneAnalyzer() {
        IamOptions options = new IamOptions.Builder()
                .apiKey(API_KEY)
                .build();

        ToneAnalyzer toneAnalyzer = new ToneAnalyzer(VERSION, options);
        toneAnalyzer.setEndPoint(END_POINT);
        return toneAnalyzer;
    }
}

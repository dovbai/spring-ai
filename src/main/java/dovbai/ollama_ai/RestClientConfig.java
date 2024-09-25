package dovbai.ollama_ai;

import org.springframework.context.annotation.Configuration;
import org.springframework.web.client.RestClient;

@Configuration
public class RestClientConfig {
    public RestClient.Builder restClientBuilder() {
        return RestClient.builder();
    }
}

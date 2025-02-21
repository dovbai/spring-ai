package dovbai.ollama_ai;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.*;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.client.RestTemplate;

import java.util.HashMap;
import java.util.Map;

@Service
public class OllamaService {

    private static final Logger logger = LoggerFactory.getLogger( OllamaService.class);

    @Value("${spring.ai.ollama.baseurl}")
    private String baseUrl;

    @Value("${spring.ai.ollama.chat.options.model}")
    private String model;

    private final RestTemplate restTemplate;

    @Autowired
    private ObjectMapper objectMapper;

    @Autowired
    public OllamaService( RestTemplate restTemplate) {
        this.restTemplate = restTemplate;
    }

    private record PromptInfo (String model, String prompt, boolean stream) {}



    // Format input data as a json string
    private String createBody( String model, String prompt, boolean stream ) {
        PromptInfo promptInfo = new PromptInfo(model, prompt, stream);

        ObjectMapper objectMapper = new ObjectMapper();

        String outString = null;
        try {
            outString = objectMapper.writeValueAsString(promptInfo);
            logger.info("Mapped PromptInfo object to : " + outString);
        } catch( JsonProcessingException e) {
            logger.error("Caught JsonProcessingExeption", e);
        }

        return outString;
    }

    private String getJsonField( String jsonString, String key ) {
        try {
            JsonNode root = objectMapper.readTree(jsonString);
            return root.path(key).asText();
        } catch (JsonProcessingException e) {
            throw new RuntimeException(e);
        }
    }

    private long countNewLines( String str ) {
        return str.chars().filter(ch->ch=='\n').count();
    }

    private String replaceNewLines( String str ) {
        return str.replace("\n", "\\\\n");
    }

    public ResponseEntity<QueryResponse> queryOllama( String prompt ) {

        logger.info("queryOllama: got prompt: " + prompt);
        long newLinesCount = countNewLines(prompt);
        if( newLinesCount > 0) {
            logger.info("Input prompt contains " + newLinesCount + " new linee characters");
            prompt = replaceNewLines(prompt);
            logger.info("Processed line: " + prompt);
        }

        String url = baseUrl + "/generate";

        // Prepare headers
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);

        String requestBody = createBody(model, prompt, false);
        logger.info("Request body: " + requestBody);

        HttpEntity<String> requestEntity =
                new HttpEntity<>(requestBody, headers);

        ResponseEntity<String> response =
                restTemplate.exchange(url, HttpMethod.POST, requestEntity, String.class);

        HttpStatusCode statusCode = response.getStatusCode();
        logger.info("Received response. status code: " + statusCode);

        if( statusCode != HttpStatus.OK) {
            //return "Error: status code: " + statusCode + " response body: " + response.getBody();
            return null;
        }

        String jsonResponse = response.getBody();

        String responseStr = getJsonField(jsonResponse, "response");

        QueryResponse queryResponse = new QueryResponse();
        queryResponse.setModel(model);
        queryResponse.setPrompt(prompt);
        queryResponse.setModelResponse(responseStr);

        return new ResponseEntity<QueryResponse>(queryResponse, HttpStatus.OK);
    }
}

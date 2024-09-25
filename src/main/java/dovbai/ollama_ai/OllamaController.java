package dovbai.ollama_ai;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@RestController
public class OllamaController {

    private static final Logger logger = LoggerFactory.getLogger( OllamaController.class);

    private final OllamaService ollamaService;

    public OllamaController( OllamaService ollamaService ) {
        this.ollamaService = ollamaService;
    }

    @PostMapping(value = "/query", consumes = "application/json" )
    public ResponseEntity<QueryResponse> queryOllama(@RequestBody Query query ) {
        logger.info("Endpoint: /query/ got query with prompt: " + query.prompt());
        return ollamaService.queryOllama(query.prompt());
    }

    @GetMapping("/test")
    public String greeting() {
        logger.info("At endpoint: /test" );
        return "Test is successful";
    }
}

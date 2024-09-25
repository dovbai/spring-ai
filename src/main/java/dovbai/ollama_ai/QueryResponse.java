package dovbai.ollama_ai;

public class QueryResponse {
    private String model;
    private String prompt;
    private String modelResponse;

    public void setModel( String model ) {
        this.model = model;
    }

    public String getModel() {return model;}

    public void setPrompt( String prompt ) {
        this.prompt = prompt;
    }

    public String getPrompt() {return prompt;}

    public void setModelResponse( String modelResponse ) {
        this.modelResponse = modelResponse;
    }

    public String getModelResponse() {return modelResponse;}
}

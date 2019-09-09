package br.ufg.fct.cep;

import java.io.IOException;
import java.net.URL;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.concurrent.TimeUnit;

import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ArrayNode;

import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

public class DataRioAPI {

    /**
     * HTTP Client (to consume the REST API)
     */
    private OkHttpClient httpClient;

    /**
     * JSON Mapper to deserialize the HTTP calls
     */
    private ObjectMapper jsonMapper;

    /**
     * API URL, where we fetch the data
     */
    private String apiURL;

    /**
     * How long should events be considered
     */
    private double delta;

    public DataRioAPI(String apiURL, double delta) {
        this.apiURL = apiURL;
        this.delta = delta;
        
        this.httpClient = new OkHttpClient.Builder()
                              .connectTimeout(10, TimeUnit.SECONDS)
                              .readTimeout(10, TimeUnit.SECONDS)
                              .retryOnConnectionFailure(true)
                              .build();

        this.jsonMapper = new ObjectMapper();
        jsonMapper.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);
    }

    public List<BusLocationUpdateEvent> getBusLocations() throws IOException {
        // Our return list
        List<BusLocationUpdateEvent> listBusUpdates = new ArrayList<BusLocationUpdateEvent>();

        // Do HTTP GET
        Request request = new Request.Builder()
                             .addHeader("accept", "application/json")
                             .url(apiURL)
                             .build();
        Response response = httpClient.newCall(request).execute();

        // Parse
        String rawJSON = response.body().string();
        JsonNode rawNode = jsonMapper.readValue(rawJSON, JsonNode.class);
        ArrayNode rawCategoriesArray = (ArrayNode) rawNode.get("DATA");

        // Rebuild
        Iterator<JsonNode> iterator = rawCategoriesArray.iterator();
        while (iterator.hasNext()) {
            listBusUpdates.add(new BusLocationUpdateEvent(iterator.next()));
        }
        return listBusUpdates;
    }

    public static void main(String[] args) {
        DataRioAPI api = new DataRioAPI("http://dadosabertos.rio.rj.gov.br/apiTransporte/apresentacao/rest/index.cfm/obterTodasPosicoes", 30);
        try {
            for (BusLocationUpdateEvent lu : api.getBusLocations()) {
                System.out.println("------------");
                System.out.println(lu);
            }
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }
}

package br.com.projeto.carsoncars.Services;

import java.io.IOException;
import java.util.List;
import java.util.Map;

import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.util.EntityUtils;

import org.springframework.stereotype.Service;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;

@Service
public class FipeService {

    private static final String BASE_URL = "https://parallelum.com.br/fipe/api/v1/carros/marcas/";
    private static final String SUBSCRIPTION_TOKEN = "eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9.eyJ1c2VySWQiOiI0OGMyZjhjMC1mYjNkLTRjYjMtOGE3ZS0xYzM4YmEzMGUxNTkiLCJlbWFpbCI6Im1hdGV1c2YuMjAyMkBhbHVub3MudXRmcHIuZWR1LmJyIiwic3RyaXBlU3Vic2NyaXB0aW9uSWQiOiJzdWJfMVBvRUxQQ1N2SXMwOHRJRTNDZVdxRDNBIiwiaWF0IjoxNzIzNzY5MzM5fQ.3tjVhF-ubRotZdjcRe4JZayiwu9m-a8LwYW73IyYh9Q"; // Adicione seu token aqui


    private HttpGet createRequestWithToken(String url) {
        HttpGet request = new HttpGet(url);
        request.addHeader("X-Subscription-Token", SUBSCRIPTION_TOKEN);
        request.addHeader("Content-Type", "application/json"); // Adicione mais headers se necessário
        return request;
    }

    public String getMarcas() throws IOException {
        try (CloseableHttpClient httpClient = HttpClients.createDefault()) {
            HttpGet request = createRequestWithToken(BASE_URL);  // Usa o método que adiciona o token
            try (CloseableHttpResponse response = httpClient.execute(request)) {
                return EntityUtils.toString(response.getEntity());
            }
        }
    }

    public String getModelos(String marcaId) throws IOException {
        try (CloseableHttpClient httpClient = HttpClients.createDefault()) {
            String url = BASE_URL + marcaId + "/modelos";
            HttpGet request = createRequestWithToken(url);  // Usa o método que adiciona o token
            try (CloseableHttpResponse response = httpClient.execute(request)) {
                return EntityUtils.toString(response.getEntity());
            }
        }
    }

    public String getAnos(String marcaId, String modeloId) throws IOException {
        try (CloseableHttpClient httpClient = HttpClients.createDefault()) {
            String url = BASE_URL + marcaId + "/modelos/" + modeloId + "/anos";
            HttpGet request = createRequestWithToken(url);  // Usa o método que adiciona o token
            try (CloseableHttpResponse response = httpClient.execute(request)) {
                return EntityUtils.toString(response.getEntity());
            }
        }
    }

    public String getFipe(String marcaId, String modeloId, String ano) throws IOException {
        try (CloseableHttpClient httpClient = HttpClients.createDefault()) {
            String url = BASE_URL + marcaId + "/modelos/" + modeloId + "/anos/" + ano;
            HttpGet request = createRequestWithToken(url);  // Usa o método que adiciona o token
            try (CloseableHttpResponse response = httpClient.execute(request)) {
                return EntityUtils.toString(response.getEntity());
            }
        }
    }

    // Outras funções continuam, sempre usando o método createRequestWithToken para incluir o token

    public String getMarcaIdByName(String nomeMarca) throws IOException {
        HttpGet request = createRequestWithToken(BASE_URL + "");  // Inclui o token aqui

        try (CloseableHttpClient httpClient = HttpClients.createDefault();
             CloseableHttpResponse response = httpClient.execute(request)) {

            String jsonResponse = EntityUtils.toString(response.getEntity());
            ObjectMapper mapper = new ObjectMapper();
            JsonNode rootNode = mapper.readTree(jsonResponse);

            if (rootNode.has("error")) {
                throw new IOException("API returned error: " + rootNode.get("error").asText());
            }

            List<Map<String, String>> marcas = mapper.convertValue(rootNode, new TypeReference<List<Map<String, String>>>() {});
            for (Map<String, String> marca : marcas) {
                if (marca.get("nome").equalsIgnoreCase(nomeMarca)) {
                    return marca.get("codigo");
                }
            }
            throw new IOException("Marca not found with name: " + nomeMarca);
        }
    }

    public String getModeloIdByName(String marcaId, String nomeModelo) throws IOException {
        String url = BASE_URL + marcaId + "/modelos";
        HttpGet request = createRequestWithToken(url);  // Inclui o token aqui

        try (CloseableHttpClient httpClient = HttpClients.createDefault();
             CloseableHttpResponse response = httpClient.execute(request)) {

            String jsonResponse = EntityUtils.toString(response.getEntity());
            ObjectMapper mapper = new ObjectMapper();
            JsonNode rootNode = mapper.readTree(jsonResponse);

            if (rootNode.has("error")) {
                throw new IOException("API returned error: " + rootNode.get("error").asText());
            }

            JsonNode modelosNode = rootNode.path("modelos");
            List<Map<String, String>> modelos = mapper.convertValue(modelosNode, new TypeReference<List<Map<String, String>>>() {});
            for (Map<String, String> modelo : modelos) {
                if (modelo.get("nome").equalsIgnoreCase(nomeModelo)) {
                    return modelo.get("codigo");
                }
            }
            throw new IOException("Modelo not found with name: " + nomeModelo);
        }
    }
}
   



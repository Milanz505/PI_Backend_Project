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

    public String getMarcas() throws IOException {
        try (CloseableHttpClient httpClient = HttpClients.createDefault()) {
            HttpGet request = new HttpGet(BASE_URL);
            try (CloseableHttpResponse response = httpClient.execute(request)) {
                return EntityUtils.toString(response.getEntity());
            }
        }
    }

    public String getModelos(String marcaId) throws IOException {
        try (CloseableHttpClient httpClient = HttpClients.createDefault()) {
            String url = BASE_URL + marcaId + "/modelos";
            HttpGet request = new HttpGet(url);
            try (CloseableHttpResponse response = httpClient.execute(request)) {
                return EntityUtils.toString(response.getEntity());
            }
        }
    }

    public String getAnos(String marcaId, String modeloId) throws IOException {
        try (CloseableHttpClient httpClient = HttpClients.createDefault()) {
            String url = BASE_URL + marcaId + "/modelos/" + modeloId + "/anos";
            HttpGet request = new HttpGet(url);
            try (CloseableHttpResponse response = httpClient.execute(request)) {
                return EntityUtils.toString(response.getEntity());
            }
        }
    }

    public String getFipe(String marcaId, String modeloId, String ano) throws IOException {
        try (CloseableHttpClient httpClient = HttpClients.createDefault()) {
            String url = BASE_URL + marcaId + "/modelos/" + modeloId + "/anos/" + ano;
            HttpGet request = new HttpGet(url);
            try (CloseableHttpResponse response = httpClient.execute(request)) {
                return EntityUtils.toString(response.getEntity());
            }
        }
    }

    public String getMarcaIdByName(String nomeMarca) throws IOException {
        String url = BASE_URL + "";  // Certifique-se de que esta é a URL correta
        HttpGet request = new HttpGet(url);

        try (CloseableHttpClient httpClient = HttpClients.createDefault();
             CloseableHttpResponse response = httpClient.execute(request)) {

            String jsonResponse = EntityUtils.toString(response.getEntity());

            // Verifique a resposta para entender o formato
            System.out.println("API Response: " + jsonResponse);

            ObjectMapper mapper = new ObjectMapper();

            // Ajuste o tipo de dados se necessário
            JsonNode rootNode = mapper.readTree(jsonResponse);

            // Verifique se o JSON contém uma chave de erro
            if (rootNode.has("error")) {
                throw new IOException("API returned error: " + rootNode.get("error").asText());
            }

            // Ajuste a deserialização conforme necessário
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
        String url = BASE_URL + marcaId + "/modelos";  // Certifique-se de que esta é a URL correta
        HttpGet request = new HttpGet(url);
    
        try (CloseableHttpClient httpClient = HttpClients.createDefault();
             CloseableHttpResponse response = httpClient.execute(request)) {
    
            String jsonResponse = EntityUtils.toString(response.getEntity());
    
            // Verifique a resposta para entender o formato
            System.out.println("API Response: " + jsonResponse);
    
            ObjectMapper mapper = new ObjectMapper();
    
            // Ajuste o tipo de dados se necessário
            JsonNode rootNode = mapper.readTree(jsonResponse);
    
            // Verifique se o JSON contém uma chave de erro
            if (rootNode.has("error")) {
                throw new IOException("API returned error: " + rootNode.get("error").asText());
            }
    
            // Ajuste a deserialização conforme necessário
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



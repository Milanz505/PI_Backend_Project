package br.com.projeto.carsoncars.Services;

import java.io.IOException;

import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.util.EntityUtils;

import org.springframework.stereotype.Service;

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
}

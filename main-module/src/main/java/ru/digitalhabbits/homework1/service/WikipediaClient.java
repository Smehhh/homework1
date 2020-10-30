package ru.digitalhabbits.homework1.service;

import com.google.gson.*;
import org.apache.http.HttpEntity;
import org.apache.http.client.config.CookieSpecs;
import org.apache.http.client.config.RequestConfig;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.utils.URIBuilder;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.util.EntityUtils;


import javax.annotation.Nonnull;
import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.HashMap;
import java.util.Map;
import java.util.stream.Collectors;

public class WikipediaClient {
    public static final String WIKIPEDIA_SEARCH_URL = "https://en.wikipedia.org/w/api.php";

    @Nonnull
    public String search(@Nonnull String searchString) {
        final URI uri = prepareSearchUrl(searchString);
        CloseableHttpClient httpClient = HttpClients.custom().setDefaultRequestConfig(RequestConfig.custom()
                .setCookieSpec(CookieSpecs.STANDARD).build())
                .build();
        HttpGet get = new HttpGet(uri);
        CloseableHttpResponse response = null;
        String result = "";
        try {
            response = httpClient.execute(get);
            HttpEntity entity = response.getEntity();
            if (entity != null) {
                String res = EntityUtils.toString(entity);
                JsonObject convertObject = new JsonParser().parse(res).getAsJsonObject();
                JsonObject query = convertObject.get("query").getAsJsonObject().get("pages").getAsJsonObject();
                Gson gson = new GsonBuilder().create();
                String index = gson.fromJson(query, HashMap.class).keySet().toArray()[0].toString();
                if (query.get(index).getAsJsonObject().get("extract") != null)
                    result = query.get(index).getAsJsonObject().get("extract").toString();
                else
                    System.out.println("Cant find this word");
            }
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            try {
                response.close();
                httpClient.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        return result;
    }

    @Nonnull
    private URI prepareSearchUrl(@Nonnull String searchString) {
        try {
            return new URIBuilder(WIKIPEDIA_SEARCH_URL)
                    .addParameter("action", "query")
                    .addParameter("format", "json")
                    .addParameter("titles", searchString)
                    .addParameter("prop", "extracts")
                    .addParameter("explaintext", "")
                    .build();
        } catch (URISyntaxException exception) {
            throw new RuntimeException(exception);
        }
    }

}

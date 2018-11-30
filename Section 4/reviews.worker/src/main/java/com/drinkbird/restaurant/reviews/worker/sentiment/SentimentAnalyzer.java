package com.drinkbird.restaurant.reviews.worker.sentiment;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ArrayNode;
import com.fasterxml.jackson.databind.node.ObjectNode;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.core.env.Environment;
import org.springframework.stereotype.Service;

import javax.net.ssl.HttpsURLConnection;
import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.Objects;

@Service
public class SentimentAnalyzer {
    private final URL sentimentApi;
    private final String cognitiveKey;
    private final ObjectMapper mapper;

    public SentimentAnalyzer(Environment env) throws MalformedURLException {
        this.sentimentApi = new URL(Objects.requireNonNull(
            env.getProperty("azure.cognitive.textAnalytics.sentimentApi")));
        this.cognitiveKey = Objects.requireNonNull(
            env.getProperty("azure.cognitive.key"));
        this.mapper = new ObjectMapper();
    }

    public SentimentResult analyze(String text) throws IOException {
        String requestJson = buildRequestJson(text);

        HttpsURLConnection connection = createConnection();
        makeRequest(requestJson, connection);

        return getResult(connection);
    }

    private String buildRequestJson(String text) {
        ObjectNode documentNode = mapper.createObjectNode();
        documentNode.put("language", "en");
        documentNode.put("id", "1");
        documentNode.put("text", text);

        ArrayNode documentsNode = mapper.createArrayNode();
        documentsNode.add(documentNode);

        ObjectNode rootNode = mapper.createObjectNode();
        rootNode.set("documents", documentsNode);

        String requestJson = rootNode.toString();
        logger.debug("Created cognitive services request: " + requestJson);
        return requestJson;
    }

    private HttpsURLConnection createConnection() throws IOException {
        HttpsURLConnection connection =
            (HttpsURLConnection) this.sentimentApi.openConnection();
        connection.setRequestMethod("POST");
        connection.setRequestProperty("Content-Type", "application/json");
        connection.setRequestProperty("Ocp-Apim-Subscription-Key", this.cognitiveKey);
        connection.setDoOutput(true);
        return connection;
    }

    private void makeRequest(String requestJson, HttpsURLConnection connection) throws IOException {
        byte[] requestBytes = requestJson.getBytes("UTF-8");
        DataOutputStream writer = new DataOutputStream(connection.getOutputStream());
        writer.write(requestBytes, 0, requestBytes.length);
        writer.flush();
        writer.close();
    }

    private SentimentResult getResult(HttpsURLConnection connection) throws IOException {
        StringBuilder response = new StringBuilder();
        BufferedReader input = new BufferedReader(
            new InputStreamReader(connection.getInputStream()));

        String line;
        while ((line = input.readLine()) != null) {
            response.append(line);
        }

        input.close();
        String responseJson = response.toString();

        logger.debug("Received cognitive services result: " + responseJson);
        return mapper.readValue(responseJson, SentimentResult.class);
    }

    private static Logger logger = LoggerFactory.getLogger(SentimentAnalyzer.class);
}

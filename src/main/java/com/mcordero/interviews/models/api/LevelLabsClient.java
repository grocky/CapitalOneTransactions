package com.mcordero.interviews.models.api;

import org.apache.http.HttpEntity;
import org.apache.http.HttpHeaders;
import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.ContentType;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.HttpClientBuilder;

import javax.json.Json;
import javax.json.JsonObject;
import javax.json.JsonReader;
import java.io.BufferedInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.UnsupportedEncodingException;
import java.util.List;
import java.util.stream.Collectors;

public class LevelLabsClient {

  private static final String INITIAL_TOKEN = "115D786878A5B25FB044E836D1612597";

  private final int uid;
  private final String apiToken;
  private String token = INITIAL_TOKEN;

  private HttpClient client;

  public LevelLabsClient(final int uid, final String apiToken) {
    this.uid = uid;
    this.apiToken = apiToken;
    HttpClientBuilder clientBuilder = HttpClientBuilder.create();
    this.client = clientBuilder.build();
  }

  public final List<Transaction> getAllTransactions() {
    HttpPost postRequest = new HttpPost("https://2016.api.levelmoney.com/api/v2/core/get-all-transactions");
    postRequest.setHeader(HttpHeaders.ACCEPT, ContentType.APPLICATION_JSON.getMimeType());
    postRequest.setHeader(HttpHeaders.CONTENT_TYPE, ContentType.APPLICATION_JSON.toString());

    TransactionsRequest req = new TransactionsRequest(this.uid, this.apiToken, this.token);
    HttpEntity requestBody = this.buildEntity(req);
    postRequest.setEntity(requestBody);

    JsonObject responseBody;

    try {
      HttpResponse response = this.client.execute(postRequest);
      responseBody = this.extractJsonBody(response);
    } catch (IOException e) {
      System.out.println(e.getMessage());
      responseBody = Json.createObjectBuilder()
        .add("transactions", Json.createArrayBuilder().build()).build();
    }

    return responseBody
      .getJsonArray("transactions")
      .stream()
      .map(t -> new Transaction((JsonObject) t))
      .collect(Collectors.toList());
  }

  private HttpEntity buildEntity(final Object body) {
    try {
      return new StringEntity(body.toString());
    } catch (UnsupportedEncodingException e) {
      System.out.println("Invalid request");
      throw new RuntimeException("Invalid json object constructed", e);
    }
  }

  private JsonObject extractJsonBody(final HttpResponse response) throws IOException {
    InputStream targetStream = new BufferedInputStream((response.getEntity().getContent()));
    JsonReader rdr = Json.createReader(targetStream);
    return rdr.readObject();
  }
}

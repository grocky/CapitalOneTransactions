package com.mcordero.interviews.models.api;

import org.apache.http.HttpEntity;
import org.apache.http.HttpHeaders;
import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.ContentType;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.HttpClientBuilder;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.util.List;

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

    TransactionResponse response;
    try {
      HttpResponse httpResponse = this.client.execute(postRequest);
      response = new TransactionResponse(httpResponse);
    } catch (IOException e) {
      System.out.println("Error sending http request: " + e.getMessage());
      response = new TransactionResponse();
    }

    return response.getTransactions();
  }

  private HttpEntity buildEntity(final Object body) {
    try {
      return new StringEntity(body.toString());
    } catch (UnsupportedEncodingException e) {
      System.out.println("Invalid request");
      throw new RuntimeException("Invalid json object constructed", e);
    }
  }

}

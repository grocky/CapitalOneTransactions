package com.mcordero.interviews.models.api;

import org.apache.http.HttpResponse;

import javax.json.Json;
import javax.json.JsonObject;
import javax.json.JsonReader;
import java.io.BufferedInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

public class TransactionResponse {
  private List<Transaction> transactions;

  TransactionResponse() {
    this.transactions = new ArrayList<>();
  }

  TransactionResponse(final HttpResponse response) {
    try {
      JsonObject body = this.extractJsonBody(response);
      this.transactions = body.getJsonArray("transactions")
        .stream()
        .map(t -> new Transaction((JsonObject) t))
        .collect(Collectors.toList());
    } catch (IOException e) {
      System.out.println("Unable to parse response: " + e.getMessage());
      this.transactions = new ArrayList<>();
    }
  }

  final List<Transaction> getTransactions() {
    return this.transactions;
  }

  private JsonObject extractJsonBody(final HttpResponse response) throws IOException {
    InputStream targetStream = new BufferedInputStream((response.getEntity().getContent()));
    JsonReader rdr = Json.createReader(targetStream);
    return rdr.readObject();
  }
}

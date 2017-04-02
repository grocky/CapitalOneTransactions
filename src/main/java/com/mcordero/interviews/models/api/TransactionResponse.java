package com.mcordero.interviews.models.api;

import org.apache.http.HttpResponse;

import javax.json.JsonObject;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

class TransactionResponse extends Response {
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

}

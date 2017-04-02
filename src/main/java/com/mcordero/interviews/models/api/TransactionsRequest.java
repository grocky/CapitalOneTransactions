package com.mcordero.interviews.models.api;

import javax.json.Json;
import javax.json.JsonObject;

public class TransactionsRequest {

  private int uid;
  private String token;
  private String apiToken;
  private boolean jsonStrictMode = true;
  private boolean jsonVerbostResponse = true;

  TransactionsRequest(final int uid, final String apiToken, final String token) {
    this.uid = uid;
    this.token = token;
    this.apiToken = apiToken;
  }

  TransactionsRequest(
    final int uid,
    final String token,
    final String apiToken,
    final boolean jsonStrictMode,
    final boolean jsonVerbostResponse
  ) {
    this(uid, apiToken, token);
    this.jsonStrictMode = jsonStrictMode;
    this.jsonVerbostResponse = jsonVerbostResponse;
  }

  public final JsonObject toJson() {
    JsonObject data = Json.createObjectBuilder()
      .add("uid", this.uid)
      .add("token", this.token)
      .add("api-token", this.apiToken)
      .add("json-strict-mode", this.jsonStrictMode)
      .add("json-verbose-response", this.jsonVerbostResponse)
      .build();

    return Json.createObjectBuilder()
      .add("args", data)
      .build();
  }

  @Override
  public final String toString() {
    return this.toJson().toString();
  }
}

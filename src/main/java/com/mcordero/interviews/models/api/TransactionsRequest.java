package com.mcordero.interviews.models.api;

import javax.json.Json;
import javax.json.JsonObject;

public class TransactionsRequest {
  private int uid;
  private String token;
  private String apiToken;
  private boolean jsonStrictMode;
  private boolean jsonVerbostResponse;

  public TransactionsRequest(
    final int uid,
    final String token,
    final String apiToken,
    final boolean jsonStrictMode,
    final boolean jsonVerbostResponse
  ) {
    this.uid = uid;
    this.token = token;
    this.apiToken = apiToken;
    this.jsonStrictMode = jsonStrictMode;
    this.jsonVerbostResponse = jsonVerbostResponse;
  }

  public final int getUid() {
    return this.uid;
  }

  public final String getToken() {
    return this.token;
  }

  public final String getApiToken() {
    return this.apiToken;
  }

  public final boolean isJsonStrictMode() {
    return this.jsonStrictMode;
  }

  public final boolean isJsonVerbostResponse() {
    return this.jsonVerbostResponse;
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

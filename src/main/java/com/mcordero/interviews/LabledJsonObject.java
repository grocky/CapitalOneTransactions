package com.mcordero.interviews;

import javax.json.Json;
import javax.json.JsonObject;

public class LabledJsonObject {

  String label;
  TransactionSummary summary;

  public LabledJsonObject(String label, TransactionSummary summary) {
    this.label = label;
    this.summary = summary;
  }

  public String getLabel() {
    return label;
  }

  public TransactionSummary getSummary() {
    return summary;
  }

  public JsonObject toJson() {
    return Json.createObjectBuilder()
      .add(this.label, this.summary.toJson())
      .build();
  }

  @Override
  public String toString() {
    return toJson().toString();
  }
}

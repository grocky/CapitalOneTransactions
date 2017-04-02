package com.mcordero.interviews;

import javax.json.Json;
import javax.json.JsonObject;

public class TransactionSummary {

  private double credits;
  private double debits;

  public TransactionSummary(double credits, double debits) {
    this.credits = credits;
    this.debits = debits;
  }

  public double getCredits() {
    return credits;
  }

  public double getDebits() {
    return debits;
  }

  public JsonObject toJson() {
    return Json.createObjectBuilder()
      .add("spent", this.debits)
      .add("income", this.credits)
      .build();
  }

}

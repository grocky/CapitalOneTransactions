package com.mcordero.interviews;

import javax.json.Json;
import javax.json.JsonObject;
import java.text.NumberFormat;

public class TransactionSummary {

  private double credits;
  private double debits;
  private NumberFormat formatter;

  public TransactionSummary(double credits, double debits) {
    this.credits = credits;
    this.debits = debits;
    this.formatter = NumberFormat.getCurrencyInstance();
  }

  public double getCredits() {
    return credits;
  }

  public double getDebits() {
    return debits;
  }

  public JsonObject toJson() {
    return Json.createObjectBuilder()
      .add("spent", this.formatter.format(this.debits))
      .add("income", this.formatter.format(this.credits))
      .build();
  }

}

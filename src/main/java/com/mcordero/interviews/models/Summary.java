package com.mcordero.interviews.models;

import javax.json.Json;
import javax.json.JsonObject;
import java.text.NumberFormat;

/**
 * A summary of credits and debits.
 */
public class Summary {

  private double credits;
  private double debits;
  private NumberFormat formatter;

  public Summary(final double credits, final double debits) {
    this.credits = credits;
    this.debits = debits;
    this.formatter = NumberFormat.getCurrencyInstance();
  }

  public final double getCredits() {
    return this.credits;
  }

  public final double getDebits() {
    return this.debits;
  }

  public final JsonObject toJson() {
    return Json.createObjectBuilder()
      .add("spent", this.formatter.format(this.debits))
      .add("income", this.formatter.format(this.credits))
      .build();
  }

}

package com.mcordero.interviews.models;

import javax.json.Json;
import javax.json.JsonObject;

/**
 * A labeled summary.
 */
public class LabeledSummary {

  private String label;
  private Summary summary;


  public LabeledSummary(final String label, final Summary summary) {
    this.label = label;
    this.summary = summary;
  }

  public final String getLabel() {
    return this.label;
  }

  public final Summary getSummary() {
    return this.summary;
  }

  public final JsonObject toJson() {
    return Json.createObjectBuilder()
      .add(this.label, this.summary.toJson())
      .build();
  }

  @Override
  public final String toString() {
    return this.toJson().toString();
  }
}

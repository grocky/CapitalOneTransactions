package com.mcordero.interviews.models.api;

import org.apache.http.HttpResponse;

import javax.json.JsonObject;
import java.io.IOException;

class LoginResponse extends Response {
  private String error;
  private int uid;
  private String token;
  private String onboardingStage;
  private String aggStatus;
  private boolean hasAccountsLinked;

  LoginResponse() {

  }

  LoginResponse(final HttpResponse response) {
    try {
      JsonObject json = this.extractJsonBody(response);
      this.error = json.getString("error");
      this.uid = json.getInt("uid");
      this.token = json.getString("token");
      this.onboardingStage = json.getString("onboarding-stage");
      this.aggStatus = json.getString("agg-status");
      this.hasAccountsLinked = json.getBoolean("has-accounts-linked");
    } catch (IOException e) {
      System.out.println("Unable to parse response: " + e.getMessage());
    }
  }

  final String getError() {
    return this.error;
  }

  final String getToken() {
    return this.token;
  }

  final int getUid() {
    return this.uid;
  }

  final String getOnboardingStage() {
    return this.onboardingStage;
  }

  final String getAggStatus() {
    return this.aggStatus;
  }

  final boolean isHasAccountsLinked() {
    return this.hasAccountsLinked;
  }

}

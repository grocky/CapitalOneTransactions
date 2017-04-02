package com.levelmoney.api.models;

import javax.json.JsonObject;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.TimeZone;

/**
 * Transaction from the API.
 */
public class Transaction {

  private static final int CENTOCENTS_CONVERSION_VALUE = 10000;

  private double amount;
  private boolean isPending;
  private Calendar aggregationTime;
  private Calendar clearDate;
  private String accountId;
  private String transactionId;
  private String rawMerchant;
  private String categorization;
  private String merchant;
  private Calendar transactionTime;

  public Transaction(final JsonObject jsonObject) {
    this.amount = jsonObject.getInt("amount") / CENTOCENTS_CONVERSION_VALUE;
    this.isPending = jsonObject.getBoolean("is-pending");
    this.aggregationTime = Calendar.getInstance();
    this.aggregationTime.setTimeInMillis((long) jsonObject.getInt("aggregation-time"));
    this.clearDate = Calendar.getInstance();
    this.clearDate.setTimeInMillis((long) jsonObject.getInt("clear-date"));
    this.accountId = jsonObject.getString("account-id");
    this.transactionId = jsonObject.getString("transaction-id");
    this.rawMerchant = jsonObject.getString("raw-merchant");
    this.categorization = jsonObject.getString("categorization");
    this.merchant = jsonObject.getString("merchant");

    SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSSXXX");
    TimeZone utc = TimeZone.getTimeZone("UTC");
    TimeZone.setDefault(utc);
    sdf.setTimeZone(utc);

    this.transactionTime = Calendar.getInstance();

    String time = "";

    try {
      time = jsonObject.getString("transaction-time");
      this.transactionTime.setTime(sdf.parse(time));
    } catch (ParseException e) {
      throw new RuntimeException("Bad transaction time: " + time, e);
    }
  }

  public final double getAmount() {
    return this.amount;
  }

  public final boolean isPending() {
    return this.isPending;
  }

  public final Calendar getAggregationTime() {
    return this.aggregationTime;
  }

  public final Calendar getClearDate() {
    return this.clearDate;
  }

  public final String getAccountId() {
    return this.accountId;
  }

  public final String getTransactionId() {
    return this.transactionId;
  }

  public final String getRawMerchant() {
    return this.rawMerchant;
  }

  public final String getCategorization() {
    return this.categorization;
  }

  public final String getMerchant() {
    return this.merchant;
  }

  public final Calendar getTransactionTime() {
    return this.transactionTime;
  }

}

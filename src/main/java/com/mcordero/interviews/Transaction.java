package com.mcordero.interviews;

import javax.json.JsonObject;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.TimeZone;

public class Transaction {

  private static int CENTOCENTS_CONVERSION_VALUE = 10000;

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

  Transaction(JsonObject jsonObject) {
    this.amount = jsonObject.getInt("amount") / CENTOCENTS_CONVERSION_VALUE;
    this.isPending = jsonObject.getBoolean("is-pending");
    this.aggregationTime = Calendar.getInstance();
    this.aggregationTime.setTimeInMillis((long)jsonObject.getInt("aggregation-time"));
    this.clearDate = Calendar.getInstance();
    this.clearDate.setTimeInMillis((long)jsonObject.getInt("clear-date"));
    this.accountId = jsonObject.getString("account-id");
    this.transactionId = jsonObject.getString("transaction-id");
    this.rawMerchant = jsonObject.getString("raw-merchant");
    this.categorization = jsonObject.getString("categorization");
    this.merchant = jsonObject.getString("merchant");

    SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSSXXX");
    TimeZone UTC = TimeZone.getTimeZone("UTC");
    TimeZone.setDefault(UTC);
    sdf.setTimeZone(UTC);

    this.transactionTime = Calendar.getInstance();

    String time = "";

    try {
      time = jsonObject.getString("transaction-time");
      this.transactionTime.setTime(sdf.parse(time));
    } catch (ParseException e) {
      throw new RuntimeException("Bad transaction time: " + time, e);
    }
  }

  public double getAmount() {
    return amount;
  }

  public boolean isPending() {
    return isPending;
  }

  public Calendar getAggregationTime() {
    return aggregationTime;
  }

  public Calendar getClearDate() {
    return clearDate;
  }

  public String getAccountId() {
    return accountId;
  }

  public String getTransactionId() {
    return transactionId;
  }

  public String getRawMerchant() {
    return rawMerchant;
  }

  public String getCategorization() {
    return categorization;
  }

  public String getMerchant() {
    return merchant;
  }

  public Calendar getTransactionTime() {
    return transactionTime;
  }

}

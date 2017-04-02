
package com.mcordero.interviews;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.InputStream;
import java.util.*;
import java.util.stream.Collectors;

import javax.json.Json;
import javax.json.JsonArray;
import javax.json.JsonObject;
import javax.json.JsonReader;

public class Main {

  public static void main (String[] args) {
    //Created file by running the curl command. --because could not get past API {error: no api-token}--
    // curl -H 'Accept: application/json' -H 'Content-Type: application/json' -X POST -d '{"args": {"uid": 1110590645,
    //"token": "115D786878A5B25FB044E836D1612597", "api-token": "AppTokenForInterview", "json-strict-mode": false,
    //"json-verbose-response": false}}' https://2016.api.levelmoney.com/api/v2/core/get-all-transactions > dataFile.txt

    // TODO: get from API
    File file = new File("./dataFile.json");

    try {
      InputStream targetStream = new FileInputStream(file);
      JsonReader rdr = Json.createReader(targetStream);
      JsonObject obj = rdr.readObject();
      JsonArray results = obj.getJsonArray("transactions");

      if (args.length != 0) {
        switch(args[0].toString()) {
          case "ignore-donuts":
            List<JsonObject> aList = results.getValuesAs(JsonObject.class);
            aList.stream().filter(j -> !j.get("merchant").toString().contentEquals("Krispy Kreme Donuts") || !j.get("merchant").toString().contentEquals("DUNKIN #336784"));
        }
      }

      Map<String, List<Transaction>> monthTransactionMap = buildMonthTransactionMap(results);

      List<LabledJsonObject> summaries = monthTransactionMap
        .entrySet()
        .stream()
        .map(s -> buildSummary(s.getKey(), s.getValue()))
        .collect(Collectors.toList());

      summaries.add(buildAverageSummary(summaries));

      summaries.forEach(System.out::println);

    } catch (FileNotFoundException e) {
      e.printStackTrace();
    }
  }

  private static LabledJsonObject buildSummary(String dateKey, List<Transaction> transactions) {
    double credits = transactions
      .stream()
      .filter(transaction -> transaction.getAmount() > 0)
      .mapToDouble(Transaction::getAmount)
      .sum();

    double debits = transactions
      .stream()
      .filter(transaction -> transaction.getAmount() < 0)
      .mapToDouble(Transaction::getAmount)
      .sum();

    TransactionSummary summary = new TransactionSummary(credits, debits);

    return new LabledJsonObject(dateKey, summary);
  }

  private static LabledJsonObject buildAverageSummary(List<LabledJsonObject> summaries) {
    OptionalDouble averageCredits = summaries
      .stream()
      .mapToDouble(summary -> summary.getSummary().getCredits())
      .average();

    OptionalDouble averageDebits = summaries
      .stream()
      .mapToDouble(summary -> summary.getSummary().getDebits())
      .average();

    TransactionSummary averageSummary = new TransactionSummary(averageCredits.orElse(0.0), averageDebits.orElse(0.0));

    return new LabledJsonObject("average", averageSummary);
  }

  private static Map<String, List<Transaction>> buildMonthTransactionMap(JsonArray results) {

    Map<String, List<Transaction>> monthlyTransactions = new HashMap<>();

    results.stream().map(obj -> (JsonObject) obj).forEach((JsonObject result) -> {

      Transaction transaction = new Transaction(result);

      Calendar transactionTime = transaction.getTransactionTime();

      String key = transactionTime.get(Calendar.YEAR) + "-" + transactionTime.get(Calendar.MONTH);

      if (!monthlyTransactions.containsKey(key)) {
        monthlyTransactions.put(key, new ArrayList<>());
      }

      monthlyTransactions.get(key).add(transaction);
    });

    return monthlyTransactions;
  }

}


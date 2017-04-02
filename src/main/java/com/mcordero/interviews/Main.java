
package com.mcordero.interviews;

import com.mcordero.interviews.models.LabeledSummary;
import com.mcordero.interviews.models.api.Transaction;
import com.mcordero.interviews.models.Summary;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.OptionalDouble;
import java.util.stream.Collectors;

import javax.json.Json;
import javax.json.JsonArray;
import javax.json.JsonObject;
import javax.json.JsonReader;

/**
 * Main class to execute solution.
 */
public final class Main {

  /**
   * Hide constructor for utility classes.
   */
  private Main() { }

  /**
   * Controller method to handle input and output.
   *
   * @param args Command line arguments
   */
  public static void main(final String[] args) {
    //Created file by running the curl command. --because could not get past API {error: no api-token}--
    // curl -H 'Accept: application/json' -H 'Content-Type: application/json' -X POST -d '{"args": {"uid": 1110590645,
    //"token": "115D786878A5B25FB044E836D1612597", "api-token": "AppTokenForInterview", "json-strict-mode": false,
    //"json-verbose-response": false}}' https://2016.api.levelmoney.com/api/v2/core/get-all-transactions > dataFile.txt
    File file = new File("./dataFile.json");

    try {
      InputStream targetStream = new FileInputStream(file);
      JsonReader rdr = Json.createReader(targetStream);
      JsonObject obj = rdr.readObject();
      JsonArray results = obj.getJsonArray("transactions");

      Map<String, List<Transaction>> monthTransactionMap = buildMonthTransactionMap(results);

      List<LabeledSummary> summaries = monthTransactionMap
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

  /**
   * Builds a summary for a month.
   *
   * @param dateKey The date key.
   * @param transactions The list of transactions.
   * @return A summary wrapped by a label.
   */
  private static LabeledSummary buildSummary(final String dateKey, final List<Transaction> transactions) {
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

    Summary summary = new Summary(credits, debits);

    return new LabeledSummary(dateKey, summary);
  }

  /**
   *
   * @param summaries A list of summaries
   * @return A summary wrapped by a label.
   */
  private static LabeledSummary buildAverageSummary(final List<LabeledSummary> summaries) {
    OptionalDouble averageCredits = summaries
      .stream()
      .mapToDouble(summary -> summary.getSummary().getCredits())
      .average();

    OptionalDouble averageDebits = summaries
      .stream()
      .mapToDouble(summary -> summary.getSummary().getDebits())
      .average();

    Summary averageSummary = new Summary(averageCredits.orElse(0.0), averageDebits.orElse(0.0));

    return new LabeledSummary("average", averageSummary);
  }

  /**
   *
   * @param results An array of json objects.
   * @return A map from a date to a list of transactions.
   */
  private static Map<String, List<Transaction>> buildMonthTransactionMap(final JsonArray results) {

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


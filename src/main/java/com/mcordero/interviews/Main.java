
package com.mcordero.interviews;

import com.mcordero.interviews.models.LabeledSummary;
import com.mcordero.interviews.models.api.LevelLabsClient;
import com.mcordero.interviews.models.api.Transaction;
import com.mcordero.interviews.models.Summary;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.OptionalDouble;
import java.util.stream.Collectors;

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

    LevelLabsClient client = new LevelLabsClient(1110590645, "AppTokenForInterview");
    List<Transaction> transactions = client.getAllTransactions();
    Map<String, List<Transaction>> monthTransactionMap = buildMonthTransactionMap(transactions);

    List<LabeledSummary> summaries = monthTransactionMap
      .entrySet()
      .stream()
      .map(s -> buildSummary(s.getKey(), s.getValue()))
      .collect(Collectors.toList());

    summaries.add(buildAverageSummary(summaries));

    summaries.forEach(System.out::println);

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
   * @param transactions An array of json objects.
   * @return A map from a date to a list of transactions.
   */
  private static Map<String, List<Transaction>> buildMonthTransactionMap(final List<Transaction> transactions) {

    Map<String, List<Transaction>> monthlyTransactions = new HashMap<>();

    transactions
      .forEach(transaction -> {
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


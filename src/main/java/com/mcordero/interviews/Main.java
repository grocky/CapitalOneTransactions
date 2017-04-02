
package com.mcordero.interviews;

import com.mcordero.interviews.models.LabeledSummary;

import java.util.List;

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

    TransactionsService transactionsService = new TransactionsService();
    List<LabeledSummary> summaries = transactionsService.generateTransactionSummary();

    summaries.forEach(System.out::println);
  }

}


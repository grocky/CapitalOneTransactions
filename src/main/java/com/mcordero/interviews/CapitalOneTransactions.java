
package com.mcordero.interviews;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.event.ActionEvent;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.InputStream;
import java.util.List;

import javax.json.Json;
import javax.json.JsonArray;
import javax.json.JsonObject;
import javax.json.JsonReader;
import javax.swing.AbstractAction;
import javax.swing.Action;
import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;

public class CapitalOneTransactions extends JFrame {



	/*
	 *
	 * Constructors
	 */

  public CapitalOneTransactions(){
    //setting up window
    setTitle("Transaction Data");
    setSize(WINDOW_WIDTH,WINDOW_HEIGHT);
    setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
    setLayout(new BorderLayout());
    getContentPane().setBackground(Color.WHITE);
    //setVisible(true);

    //setup data
    setObjArr(new Object[1][1]);

    //add the components to the JFrame window
    add(getTitlePanel(), BorderLayout.NORTH);
    add(getScrollPane(), BorderLayout.CENTER);
    add(getButtonPanel(),BorderLayout.SOUTH);
  }

  public CapitalOneTransactions(Object [][] obj){
    //setting up window
    setTitle("Capital One Transaction Information");
    setSize(WINDOW_WIDTH,WINDOW_HEIGHT);
    setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
    setLayout(new BorderLayout());
    getContentPane().setBackground(Color.WHITE);

    //setup data
    setObjArr(obj);

    //add the components to the JFrame window
    add(getTitlePanel(), BorderLayout.NORTH);
    add(getScrollPane(), BorderLayout.CENTER);
    add(getButtonPanel(),BorderLayout.SOUTH);

    //reopen
    setVisible(true);
  }

	/*
	 *
	 * Attributes
	 */

  private static final long serialVersionUID = -4842557053238840941L;
  private int WINDOW_WIDTH = 2000;
  private int WINDOW_HEIGHT = 500;
  private String year = "";		//UI select items drop down for data filtering by year
  private JScrollPane scrollPane;
  private Object [][] objArray;

	/*
	 *
	 * Accessors
	 */

  public Object[][] getObjArray() {
    return objArray;
  }

  public void setObjArr(Object[][] objArray) {
    this.objArray = objArray;
  }

  public JScrollPane getScrollPane() {
    scrollPane = new JScrollPane();
    //scrollPane.getColumnHeader().setFont(new Font(String.valueOf(Font.PLAIN), Font.BOLD, 12));
    scrollPane.setBackground(Color.WHITE);
    scrollPane.setViewportView(getDataTable());
    scrollPane.setViewportBorder(BorderFactory.createLineBorder(Color.BLACK));
    scrollPane.setBorder(BorderFactory.createLineBorder(Color.BLACK));
    return scrollPane;
  }

  public void setScrollPane(JScrollPane scrollPane) {
    this.scrollPane = scrollPane;
  }


	/*
	 *
	 * Private
	 */

  private JTable getDataTable(){
    Object [] columnNames = {"amount","is-pending","aggregation-time","account-id","clear-date",
      "transaction-id","raw-merchant","categorization","merchant","transaction-time"};
    Object [] [] rowData = getObjArray();
    JTable dataTable = new JTable(rowData,columnNames);
    dataTable.setGridColor(Color.BLACK);
    dataTable.setShowGrid(true);
    dataTable.setAutoResizeMode(JTable.AUTO_RESIZE_ALL_COLUMNS);
    return dataTable;
  }

  private JPanel getTitlePanel(){
    JPanel titlePanel = new JPanel();
    JComboBox<String> comboBox = new JComboBox<String>(new String[]{"2014", "2015", "2016", "2017"});
    comboBox.setAlignmentX(CENTER_ALIGNMENT);
    comboBox.addItemListener(i -> year = i.getItem().toString());
    titlePanel.add(comboBox);
    titlePanel.setBorder(BorderFactory.createEtchedBorder());
    titlePanel.setBackground(Color.WHITE);
    titlePanel.setVisible(true);
    return titlePanel;
  }

  private JPanel getButtonPanel(){
    JPanel buttonPanel = new JPanel();
    buttonPanel.setBorder(BorderFactory.createLineBorder(Color.BLACK));
    buttonPanel.setBackground(Color.WHITE);

    //Add Refresh Button
    Action refresh = new RefreshAction();
    JButton refreshButton = new JButton(refresh);
    refreshButton.setText("Refresh");

    //Create ignore donuts button
    Action ignoreDonuts = new ignoreDonutsAction();
    JButton ignoreDonutsButton = new JButton(ignoreDonuts);
    ignoreDonutsButton.setText("Ignore Donuts");

    //Create ignore credit card button
    Action ignoreCreditCard  = new ignoreCreditCardAction();
    JButton ignoreCreditCardButton = new JButton(ignoreCreditCard);
    ignoreCreditCardButton.setText("Ignore Credit Card");

    //Add Buttons
    buttonPanel.add(refreshButton);
    buttonPanel.add(ignoreDonutsButton);
    buttonPanel.add(ignoreCreditCardButton);
    return buttonPanel;
  }

  //Unfortunately @FunctionalInterface was not implemented on the Action class, so no lambda function available
  private class RefreshAction extends AbstractAction{
    private static final long serialVersionUID = -8404165432612315832L;
    @Override
    public void actionPerformed(ActionEvent e) {
      setVisible(false);		//updates model; there's probably a more correct way, but this works.
      String [] args = {""};
      CapitalOneTransactions.main(args);
    }
  }

  private class ignoreDonutsAction extends AbstractAction{
    private static final long serialVersionUID = -8563951402555496412L;
    @Override
    public void actionPerformed(ActionEvent e) {
      setVisible(false);		//updates model; there's probably a more correct way, but this works.
      String [] args = new String[]{"ignore-donuts"};
      CapitalOneTransactions.main(args);
    }
  }

  private class ignoreCreditCardAction extends AbstractAction{
    private static final long serialVersionUID = -3853613441334408783L;
    @Override
    public void actionPerformed(ActionEvent e) {
      setVisible(false);		//updates model; there's probably a more correct way, but this works.
      String [] args = new String[]{"ignore-credit-cards"};
      CapitalOneTransactions.main(args);
    }
  }

	/*
	 *
	 * Public
	 */

  public static void main (String[] args){
    //Created file by running the curl command. --because could not get past API {error: no api-token}--
    // curl -H 'Accept: application/json' -H 'Content-Type: application/json' -X POST -d '{"args": {"uid": 1110590645,
    //"token": "115D786878A5B25FB044E836D1612597", "api-token": "AppTokenForInterview", "json-strict-mode": false,
    //"json-verbose-response": false}}' https://2016.api.levelmoney.com/api/v2/core/get-all-transactions > dataFile.txt
    File file = new File("./dataFile.txt");
    try {
      InputStream targetStream = new FileInputStream(file);
      JsonReader rdr = Json.createReader(targetStream);
      JsonObject obj = rdr.readObject();
      JsonArray results = obj.getJsonArray("transactions");
      if(args.length != 0){
        switch(args[0].toString()){
          case "ignore-donuts":
            List<JsonObject> aList = results.getValuesAs(JsonObject.class);
            aList.stream().filter(j -> !j.get("merchant").toString().contentEquals("Krispy Kreme Donuts") || !j.get("merchant").toString().contentEquals("DUNKIN #336784"));
            //"donuts" successfully filtered but object conversion is difficult
            //Iterator<JsonObject> itr = aList.iterator();
            JsonArray b;
            //results.addAll(aList); could not manipulate Json api
            //aList.forEach(j -> b.add(j));

            //case "ignore-credit-cards": results.removeIf(j -> j.getValueType().equals(other) ;break;
          default:;
        }
      }
      Object [] [] objArr = new Object [results.size()][10];		//hard code
      for (int i=0; i< results.size()-1; i++) {
        objArr[i][0] = results.getJsonObject(i).get("amount");
        objArr[i][1] = results.getJsonObject(i).get("is-pending");
        objArr[i][2] = results.getJsonObject(i).get("aggregation-time");
        objArr[i][3] = results.getJsonObject(i).get("account-id");
        objArr[i][4] = results.getJsonObject(i).get("clear-date");
        objArr[i][5] = results.getJsonObject(i).get("transaction-id");
        objArr[i][6] = results.getJsonObject(i).get("raw-merchant");
        objArr[i][7] = results.getJsonObject(i).get("categorization");
        objArr[i][8] = results.getJsonObject(i).get("merchant");
        objArr[i][9] = results.getJsonObject(i).get("transaction-time");
      }
      CapitalOneTransactions cout = new CapitalOneTransactions(objArr);
      cout.setVisible(true);
    } catch (FileNotFoundException e) {
      e.printStackTrace();
    }
  }

}


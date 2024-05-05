import java.util.*;

import java.awt.*;
import javax.swing.*;
import javax.swing.border.Border;

import java.io.*;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.LocalDateTime;
import java.time.ZoneOffset;

import org.jfree.chart.JFreeChart; 
import org.jfree.chart.ChartFactory; 
import org.jfree.chart.ChartUtilities; 
import org.jfree.chart.plot.PlotOrientation;
import org.jfree.data.category.DefaultCategoryDataset;
import org.jfree.chart.ChartFactory;
import org.jfree.chart.ChartPanel;
import org.jfree.chart.JFreeChart;
import org.jfree.data.xy.DefaultXYDataset;
import org.jfree.data.xy.XYSeries;
import org.jfree.ui.ApplicationFrame;
import org.jfree.ui.RefineryUtilities;

public class Graph extends PopupWindow {
    public static record Vector3<T>(T x, T y, T z) { }

    private JPanel graphImageBox;

    Color background = new Color(235,240,245);
    Random rand = new Random();

    public String item;

    private static int secondsInDay = 86400;
    private static int secondsInHour = 3600;

    private String generateGraphJPEG(long timestampFrom, long timestampTo) {
        ArrayList<orders.Tuple<Integer, Long>> orderKeysAndTimestamps = new orders().getOrders(timestampFrom, timestampTo);
        
        if (orderKeysAndTimestamps.size() <= 0) return "";

        HashMap<Integer, ArrayList<String>> orderKeysAndItemNames = new orders().getOrdersInfo(orderKeysAndTimestamps);

        // calculate the number of steps and the step sized based on how big the time range is 
        long stepSize = (timestampTo - timestampFrom) > secondsInDay * 5 ? secondsInDay : secondsInHour;
        int steps = (int)((timestampTo - timestampFrom) / stepSize) + 1;


        int amounts[];
        amounts = new int[steps];

        for (int i = 0; i < steps; i++) {
            amounts[i] = 0;
        }

        for (orders.Tuple<Integer, Long> pair : orderKeysAndTimestamps) {
            if (orderKeysAndItemNames.get(pair._1()) == null) continue;

            for (String s : orderKeysAndItemNames.get(pair._1())) {
                if (item.equals(s)) {
                    amounts[(int)((pair._2() - timestampFrom) / stepSize)]++;
                }
            }
        }

        // return scatterGraph(amounts,steps);
        return lineGraph(amounts, steps);
    }

    private String scatterGraph(int amounts[], int steps) {
        String path = "bin/" + item + "graph" + rand.nextInt(1000) + ".jpeg";

        
        DefaultXYDataset chart = new DefaultXYDataset();

        double[] xData = new double[steps];
        double[] yData = new double[steps];

        // Populate x and y data arrays
        for (int i = 0; i < steps; i++) {
            xData[i] = i;
            yData[i] = amounts[i];
        }

        double[][] data = { xData, yData };

        chart.addSeries("sales", data);

        JFreeChart lineChartObject = ChartFactory.createScatterPlot("", "Time", item, chart, PlotOrientation.VERTICAL, true, true, false);

        int imgWidth = width-20;    /* Width of the image */
        int imgHeight = height - 100;   /* Height of the image */ 
        File lineChart = new File(path); 
        try {
            ChartUtilities.saveChartAsJPEG(lineChart ,lineChartObject, imgWidth ,imgHeight);
        }
        catch (Exception e) {
            e.printStackTrace();
        }


        return path;
    }

    private String lineGraph(int amounts[], int steps) {
        String path = "bin/" + item + "graph" + rand.nextInt(1000) + ".jpeg";

        
        DefaultCategoryDataset chart = new DefaultCategoryDataset();

        for (int i = 0; i < steps; i++) {

            chart.addValue(amounts[i],"sales",Integer.toString(i));
        }

        JFreeChart lineChartObject = ChartFactory.createLineChart(
            "","Time",
            item,
            chart,PlotOrientation.VERTICAL,
            true,true,false);

        int imgWidth = width-20;    /* Width of the image */
        int imgHeight = height - 100;   /* Height of the image */ 
        File lineChart = new File(path); 
        try {
            ChartUtilities.saveChartAsJPEG(lineChart ,lineChartObject, imgWidth ,imgHeight);
        }
        catch (Exception e) {
            e.printStackTrace();
        }


        return path;
    }

    private boolean handleDateInputs(long timestampFrom, long timestampTo) {
        if (timestampFrom >= timestampTo) {
            System.out.println("error: start date must be before end date");
            showError("Start date must be before end date");
            return false;
        }

        graphImageBox.removeAll();

        JLabel loadingLabel = new JLabel("LOADING DATA");
        loadingLabel.setHorizontalAlignment(SwingConstants.CENTER);
        graphImageBox.add(loadingLabel,BorderLayout.CENTER);

        repaint();

        new Thread(new Runnable() {
            @Override public void run() {
                // do stuff in this thread
                String pathToGraphImg = generateGraphJPEG(timestampFrom, timestampTo);
        
                graphImageBox.removeAll();
                if (pathToGraphImg.equals("")) {
                    showError("Given time range has no sales for this item");
                    return;
                }
                ImageIcon graph = new ImageIcon(pathToGraphImg);
                JLabel graphImage = new JLabel(graph);
                graphImageBox.add(graphImage);

                repaint();
            }
        }).start();
        
        return true;
    }

    Graph(String name) {
        super();

        item = name;

        frame.setName(name + " Graph");

        panel.setBackground(background);


        // panel where the image of the graph will be displayed
        graphImageBox = new JPanel(new BorderLayout());
        graphImageBox.setBounds(10,10,width-20,height - 100);

        // Create a black line border
        Border blackLineBorder = BorderFactory.createLineBorder(new Color(155,165,185),3);
        graphImageBox.setBorder(blackLineBorder);


        // panel for the input
        JPanel inputBox = new JPanel(new GridLayout(1,3));
        inputBox.setBounds(10,height - 80,width - 80,40);

        inputBox.setBorder(blackLineBorder);

        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");

        JFormattedTextField dateField = new JFormattedTextField(dateFormat);
        dateField.setColumns(10); // Set the width of the field
        dateField.setText("2023-10-01"); // Set default text
        dateField.setFont(font.deriveFont(Font.PLAIN, 15));

        JFormattedTextField dateField2 = new JFormattedTextField(dateFormat);
        dateField2.setColumns(10); // Set the width of the field
        dateField2.setText("2023-10-16"); // Set default text
        dateField2.setFont(font.deriveFont(Font.PLAIN, 15));


        JButton submitButton = new JButton("Submit");
        submitButton.setBounds(width-70,height - 80,60,40);
        submitButton.setFont(font.deriveFont(Font.PLAIN, 14));
        submitButton.addActionListener(e -> {
            try {
                // Parse the date input from the field
                Date firstDate = dateFormat.parse(dateField.getText());
                Date secondDate = dateFormat.parse(dateField2.getText());

                handleDateInputs(firstDate.getTime() / 1000, secondDate.getTime() / 1000);
            } catch (ParseException ex) {
                System.err.println("Invalid date format");
                showError("Invalid date format. Use yyyy-MM-dd");
            }
        });

        JPanel dateFrom = new JPanel();
        dateFrom.setLayout(new BoxLayout(dateFrom, BoxLayout.LINE_AXIS));
        JLabel l1 = new JLabel("Start Date:");
        l1.setFont(font.deriveFont(Font.PLAIN, 16));
        dateFrom.add(l1);
        dateFrom.add(dateField);
        dateFrom.setFont(font.deriveFont(Font.PLAIN, 16));

        JPanel dateTo = new JPanel();
        dateTo.setLayout(new BoxLayout(dateTo, BoxLayout.LINE_AXIS));
        JLabel l2 = new JLabel("End Date:");
        l2.setFont(font.deriveFont(Font.PLAIN, 16));
        dateTo.add(l2);
        dateTo.add(dateField2);

        JPanel emptyBox = new JPanel();
        // emptyBox.setBackground(Color.white);

        inputBox.add(dateFrom);
        inputBox.add(emptyBox);
        inputBox.add(dateTo);
        // inputBox.add(submitButton);

        panel.add(inputBox);
        panel.add(submitButton);
        panel.add(graphImageBox);

        repaint();
    }
}
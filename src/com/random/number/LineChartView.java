package com.random.number;

import java.awt.BorderLayout;
import java.awt.EventQueue;
import java.time.LocalDateTime;
import java.util.Date;

import javax.swing.JFrame;
import javax.swing.JPanel;

import org.jfree.chart.ChartFactory;
import org.jfree.chart.ChartPanel;
import org.jfree.chart.JFreeChart;
import org.jfree.data.category.CategoryDataset;
import org.jfree.data.category.DefaultCategoryDataset;

public class LineChartView extends JFrame {

	private String year;
	private String month;
	
	public LineChartView(String year, String month) {
		 super("Line Chart for Memorize");
		 this.year = year;
		 this.month = month;
		 
		 JPanel chartPanel = createChartPanel();
		 add(chartPanel, BorderLayout.CENTER);
	 
		 setVisible(true);
		 setSize(800, 680);
		 //setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		 setLocationRelativeTo(null);
	}
	
	public LineChartView() {
        super("Line Chart for Memorize");
 
        JPanel chartPanel = createChartPanel();
        add(chartPanel, BorderLayout.CENTER);
 
        setSize(800, 680);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);
    }
 
    private JPanel createChartPanel() {
    	String selectedDate = "("+month+"/"+year+")";
    	String chartTitle = "Number Memorize Trends "+selectedDate;
        String categoryAxisLabel = "Date(dd)";
        String valueAxisLabel = "Time(s)";
     
        CategoryDataset dataset = createDataset();
     
        JFreeChart chart = ChartFactory.createLineChart(chartTitle,
                categoryAxisLabel, valueAxisLabel, dataset);
     
        return new ChartPanel(chart);
    }
 
    private CategoryDataset createDataset() {
    	DefaultCategoryDataset dataset = new DefaultCategoryDataset();
        String series1 = "Number Memorize";
        /*String series2 = "PHP";
        String series3 = "C++";
        String series4 = "C#";*/
     
        dataset.addValue(150, series1, "01");
        dataset.addValue(140, series1, "02");
        dataset.addValue(120, series1, "03");
        dataset.addValue(115, series1, "04");        
        dataset.addValue(150, series1, "05");
        dataset.addValue(140, series1, "06");
        dataset.addValue(120, series1, "07");
        dataset.addValue(115, series1, "08");
        dataset.addValue(125, series1, "09");
        dataset.addValue(110, series1, "10");
        dataset.addValue(100, series1, "11");
        dataset.addValue(100, series1, "12");
        dataset.addValue(150, series1, "13");
        dataset.addValue(140, series1, "14");
        dataset.addValue(120, series1, "15");
        dataset.addValue(115, series1, "16");
        dataset.addValue(125, series1, "17");
        dataset.addValue(110, series1, "18");
        dataset.addValue(100, series1, "19");
        dataset.addValue(100, series1, "20");
        dataset.addValue(100, series1, "21");
        dataset.addValue(100, series1, "22");
        dataset.addValue(150, series1, "23");
        dataset.addValue(140, series1, "24");
        dataset.addValue(120, series1, "25");
        dataset.addValue(115, series1, "26");
        dataset.addValue(125, series1, "27");
        dataset.addValue(110, series1, "28");
        dataset.addValue(100, series1, "29");
        dataset.addValue(100, series1, "30");
        dataset.addValue(100, series1, "31");
     
        /*dataset.addValue(4.0, series2, "2005");
        dataset.addValue(4.2, series2, "2006");
        dataset.addValue(3.8, series2, "2007");
        dataset.addValue(3.6, series2, "2008");
        dataset.addValue(3.4, series2, "2009");
        dataset.addValue(3.4, series2, "2010");
        dataset.addValue(3.3, series2, "2011");
        dataset.addValue(3.1, series2, "2012");
        dataset.addValue(3.2, series2, "2013");
     
        dataset.addValue(3.6, series3, "2005");
        dataset.addValue(3.4, series3, "2006");
        dataset.addValue(3.5, series3, "2007");
        dataset.addValue(3.2, series3, "2008");
        dataset.addValue(3.2, series3, "2009");
        dataset.addValue(3.0, series3, "2010");
        dataset.addValue(2.8, series3, "2011");
        dataset.addValue(2.8, series3, "2012");
        dataset.addValue(2.6, series3, "2013");
     
        dataset.addValue(3.2, series4, "2005");
        dataset.addValue(3.2, series4, "2006");
        dataset.addValue(3.0, series4, "2007");
        dataset.addValue(3.0, series4, "2008");
        dataset.addValue(2.8, series4, "2009");
        dataset.addValue(2.7, series4, "2010");
        dataset.addValue(2.6, series4, "2011");
        dataset.addValue(2.6, series4, "2012");
        dataset.addValue(2.4, series4, "2013");*/
     
        return dataset;
    }
 
 
    public static void main(String[] args) {
    	Date today = new Date();
    	 
    	
    	LocalDateTime date = LocalDateTime.now();    	    	
    	String year = String.valueOf(date.getYear());
    	String month = String.valueOf(date.getMonthValue());
    	
    	String str = String.format("%tm/%tY", date,date);   	 
   	 	System.out.println(str);
   	 	
    	EventQueue.invokeLater(new Runnable() {
			public void run() {
				new LineChartView(year, month).setVisible(true);
			}
		});    
    }

}

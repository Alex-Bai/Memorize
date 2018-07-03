package com.random.number;

import java.awt.BorderLayout;
import java.awt.EventQueue;

import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.SwingUtilities;

import org.jfree.chart.ChartFactory;
import org.jfree.chart.ChartPanel;
import org.jfree.chart.JFreeChart;
import org.jfree.data.category.CategoryDataset;
import org.jfree.data.category.DefaultCategoryDataset;

public class Test extends JFrame {

	public Test() {
        super("Line Chart Example with JFreechart");
 
        JPanel chartPanel = createChartPanel();
        add(chartPanel, BorderLayout.CENTER);
 
        setSize(640, 480);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);
    }
 
    private JPanel createChartPanel() {
    	String chartTitle = "Programming Languages Trends";
        String categoryAxisLabel = "Interest over time";
        String valueAxisLabel = "Popularity";
     
        CategoryDataset dataset = createDataset();
     
        JFreeChart chart = ChartFactory.createLineChart(chartTitle,
                categoryAxisLabel, valueAxisLabel, dataset);
     
        return new ChartPanel(chart);
    }
 
    private CategoryDataset createDataset() {
    	DefaultCategoryDataset dataset = new DefaultCategoryDataset();
        String series1 = "Java";
        String series2 = "PHP";
        String series3 = "C++";
        String series4 = "C#";
     
        dataset.addValue(5.0, series1, "2005");
        dataset.addValue(4.8, series1, "2006");
        dataset.addValue(4.5, series1, "2007");
        dataset.addValue(4.3, series1, "2008");
        dataset.addValue(4.0, series1, "2009");
        dataset.addValue(4.1, series1, "2010");
        dataset.addValue(4.2, series1, "2011");
        dataset.addValue(4.2, series1, "2012");
        dataset.addValue(4.0, series1, "2013");
     
        dataset.addValue(4.0, series2, "2005");
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
        dataset.addValue(2.4, series4, "2013");
     
        return dataset;
    }
 
 
    public static void main(String[] args) {
    	System.out.println("start...");
    	EventQueue.invokeLater(new Runnable() {
			public void run() {
				new Test().setVisible(true);
			}
		});    
    }

}

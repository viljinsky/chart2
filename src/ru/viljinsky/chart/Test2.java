/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package ru.viljinsky.chart;

/**
 *
 * @author vadik
 */
import java.awt.*;
import java.awt.event.ActionEvent;
import javax.swing.*;

public class Test2  extends Chart{
    static ChartSeries series1,series2,series3;
    JPanel statusBar;
    JLabel statusLabel;
    JMenuBar menuBar;

    @Override
    protected void onBarClick(ChartElement bar) {
        statusLabel.setText(bar.toString());
    }
    
    
    public Test2(){
        series1 = ChartSeries.createSeries(SeriesType.LINE_CHART, "Пример1", Color.yellow);
        series1.addValue(1,1);
        series1.addValue(2,2);
        series1.addValue(3,3);
        series1.addValue(4,4);
        series1.addValue(5,5);
        series1.addValue(6,6);
        series1.addValue(7,12);
        series1.addValue(8,1);
        series1.addValue(10,1);
        series1.rebuild();
        
        series2 = ChartSeries.createSeries(SeriesType.BAR_CHART,"Пример2", new Color(125,255,255));
        series2.addValue(0,8);
        series2.addValue(2,7);
        series2.addValue(3,6);
        series2.addValue(7,5);
        series2.addValue(8,4);
        series2.addValue(9,2);
        series2.rebuild();
        
        series3 = ChartSeries.createSeries(SeriesType.BAR_CHART,"Пример3",new Color(225,125,225));
        series3.addValue(0,8);
        series3.addValue(1,7);
        series3.addValue(9,6);
        series3.addValue(10,5);
        series3.addValue(2,4);
        series3.addValue(3,1);
        series3.rebuild();
        
        statusBar = new JPanel(new FlowLayout(FlowLayout.LEFT));
        statusLabel = new JLabel("status bar");
        statusBar.add(statusLabel);
        
        menuBar = new JMenuBar();
        JMenu menu = new JMenu("File");
        JMenuItem menuItem;
        
        for (String command:new String[]{"autorange","exit","bar","line"}){
            menuItem = new JMenuItem(new AbstractAction(command) {
                
                @Override
                public void actionPerformed(ActionEvent e) {
                    doCommand(e.getActionCommand());
                }
            });
            menu.add(menuItem);
        }
        menuBar.add(menu);
        
    }
    
    public void doCommand(String command){
        switch (command){
            case "exit":
                System.exit(0);
                break;
            case "autorange":
                autoRange();
                updateUI();
                break;
            case "bar":
                setSeriesType(SeriesType.BAR_CHART);
            break;
            case "line":
                setSeriesType(SeriesType.LINE_CHART);
                break;
        }
    }
    
    public void setSeriesType(SeriesType type){
        for (ChartSeries series:seriesList){
            series.seriesType=type;
            series.rebuild();
        }
        updateUI();
    }
    public static void createAndShowGUI(){
        Test2 chart = new Test2();
        chart.addSeries(series1);
        chart.addSeries(series2);
        chart.addSeries(series3);
        chart.autoRange();
        int x1,x2;
        x1 = chart.getXAxis().minValue-1;
        x2 = chart.getXAxis().maxValue+1;
        chart.getXAxis().setRange(x1, x2);
        
        JFrame frame = new JFrame("Chart demo");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        
        JPanel panel = new JPanel(new BorderLayout());
        panel.add(chart);
        panel.add(chart.statusBar,BorderLayout.PAGE_END);
                
        
        frame.setContentPane(panel);
        frame.setJMenuBar(chart.menuBar);
        frame.pack();
        frame.setVisible(true);
    }
    public static void main(String[] args){
        SwingUtilities.invokeLater(new Runnable() {

            @Override
            public void run() {
                createAndShowGUI();
            }
        });
    }
    
}

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
import javax.swing.table.AbstractTableModel;

class AxisControl extends JPanel{
    public AxisControl(){
        setLayout(new BoxLayout(this,BoxLayout.Y_AXIS));
        setAlignmentY(TOP_ALIGNMENT);
        
        Box box;
        String btnName[] = {"button1","button2","button3","button4"};
        
        for (String name:btnName){
            box = Box.createHorizontalBox();
            box.add(new JLabel(name));
            box.add(Box.createHorizontalGlue());
            box.add(new JButton(name));

            box.setAlignmentY(TOP_ALIGNMENT);
            box.setAlignmentX(LEFT_ALIGNMENT);
            add(box);
            add(Box.createVerticalStrut(10));
        }
        
        
        
        setBorder( BorderFactory.createEmptyBorder(10, 10, 10,10));
        
        
    }
}


class DataPanel extends JPanel{
    JTable table;
    
    class DataModel extends AbstractTableModel{
        ChartSeries series;

        public DataModel(ChartSeries series){
            this.series=series;
        }
        
        @Override
        public int getRowCount() {
            return series.getElementCount();
        }

        @Override
        public int getColumnCount() {
            return 2;
        }

        @Override
        public Object getValueAt(int rowIndex, int columnIndex) {
            ChartElement bar = series.getElement(rowIndex);
            switch (columnIndex){
                case 1:
                    return  bar.position;
                case 2:
                    return  bar.getValue();
                    default:
                        return null;
            }
        }
    }
    
    public DataPanel(){
        setLayout(new BorderLayout());
        table = new JTable(10,2);
        add(new JScrollPane(table));
    }
    
    public void setSeries(ChartSeries series){
        System.out.println(series);
        table.setModel(new DataModel(series));
    }
}

class ControlPanel extends JPanel{
    JTabbedPane tabbedPane = new JTabbedPane();
    ChartSeries series;
    DataPanel dataPanel;
    
    public ControlPanel(){
        super(new BorderLayout());
        dataPanel = new DataPanel();
        tabbedPane.addTab("XAxis", new AxisControl());
        tabbedPane.addTab("YAxis", new AxisControl());
        tabbedPane.addTab("Series", new JPanel());
        tabbedPane.addTab("Data",dataPanel);
        add(tabbedPane);
    }
    
    public void setSeries(ChartSeries series){
        this.series = series;
        dataPanel.setSeries(series);
    }
}

public class Test extends JFrame{
    Chart3 chart;
    ControlPanel controlPanel;
    JToolBar toolbar;
    public Test(){
        super("Test");
        initComponents(getContentPane());
    }
    
    class Chart3 extends Chart{

        @Override
        protected void onBarClick(ChartElement bar) {
            ChartSeries series = bar.series;
            controlPanel.setSeries(series);
        }
    }
    
    public void initComponents(Container content){
        chart = new Chart3();
        chart.getXAxis().setRange(-2, 12);
        chart.getYAxis().setRange(-2,10);
        controlPanel = new ControlPanel();
        JSplitPane splitPane = new JSplitPane(JSplitPane.HORIZONTAL_SPLIT);
        splitPane.setLeftComponent(controlPanel);
        splitPane.setRightComponent(chart);
        splitPane.setDividerLocation(200);
        
        content.add(splitPane);
        toolbar = new JToolBar();
        for (String s:new String[]{"series1","series2","series3","clear"}){
            toolbar.add(new AbstractAction(s) {

                @Override
                public void actionPerformed(ActionEvent e) {
                    doCommand(e.getActionCommand());
                }
            });
        }
        content.add(toolbar,BorderLayout.PAGE_START);
    }
    
    
    public void doCommand(String command){
        switch (command){
            case "series1":
//                chart.addSeries(Chart.series1);
                break;
            case "series2":
//                chart.addSeries(Chart.series2);
                break;
            case "series3":
//                chart.addSeries(Chart.series3);
                break;
            case "clear":
//                chart.clear();
                break;
        }
        chart.updateUI();
    }
    public static void createAndShow(){
        Test frame = new Test();
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.pack();
        frame.setVisible(true);
    }
    
    public static void main(String[] args){
        createAndShow();
    }
    
}

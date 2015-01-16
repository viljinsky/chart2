/*
 * Copyright (C) 2015 vadik
 *
 * This program is free software; you can redistribute it and/or
 * modify it under the terms of the GNU General Public License
 * as published by the Free Software Foundation; either version 2
 * of the License, or (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with this program; if not, write to the Free Software
 * Foundation, Inc., 59 Temple Place - Suite 330, Boston, MA  02111-1307, USA.
 */

package ru.viljinsky.chart;

/**
 *
 * @author vadik
 */
import java.awt.*;
import java.awt.event.ActionEvent;
import java.util.ArrayList;
import javax.swing.*;
import javax.swing.event.TableModelEvent;
import javax.swing.event.TableModelListener;
import javax.swing.table.AbstractTableModel;

class DataModel extends AbstractTableModel{
    java.util.List<Object[]> data;
    Integer columnCount;
    String[] columns ;

    public DataModel(){
        data = new ArrayList<>();
        columnCount = 0;
        columns= new String[0];
    }
    
    @Override
    public int getRowCount() {
        return data.size();
    }

    @Override
    public int getColumnCount() {
        return columnCount;
    }

    @Override
    public Object getValueAt(int rowIndex, int columnIndex) {
        Object[] rowset = data.get(rowIndex);
        return rowset[columnIndex];
    }

    @Override
    public String getColumnName(int column) {
        return columns[column];
    }

    @Override
    public void setValueAt(Object aValue, int rowIndex, int columnIndex) {
        Object[] rowset = data.get(rowIndex);
        rowset[columnIndex]=aValue;
        fireTableDataChanged();
    }

    @Override
    public boolean isCellEditable(int rowIndex, int columnIndex) {
       return columnIndex==2;
    }
    
    
    
}

public class Test5  extends JFrame{
    Chart chart;
    JTable table;
    JMenuBar menuBar;
    JToolBar toolBar;
    SeriesType seriesType = SeriesType.LINE_CHART;
    static final String[] sessions = {"Зима","Весна","Лето","Осень"};
    static final Color[] sessionColor= new Color[]{Color.cyan,Color.green,Color.yellow,Color.pink,Color.cyan};
    

    
    class TestAction extends AbstractAction{
        
        public TestAction(String command){
            super(command);
        }
        
        @Override
        public void actionPerformed(ActionEvent e) {
            doCommand(e.getActionCommand());
        }
    }
    
    public Test5(){
        JPanel panel = new JPanel(new BorderLayout());
        panel.setPreferredSize(new Dimension(800,600));
        
        chart = new Chart();
        chart.setCaption("Температура по сесознам");
        chart.getYAxis().minorTick=5;
        chart.getXAxis().setCaption("год");
        
        table = new JTable(1,2);
        JSplitPane splitPane = new JSplitPane(JSplitPane.HORIZONTAL_SPLIT);
        splitPane.setRightComponent(chart);
        splitPane.setLeftComponent(new JScrollPane(table));
        splitPane.setResizeWeight(0.3);
        
        menuBar = new JMenuBar();
        JMenu menu ;
        JMenuItem menuItem;
        menu= new JMenu("File");
        
        menuItem = new JMenuItem(new TestAction("open"));
        menu.add(menuItem);
        
        menuItem = new JMenuItem(new TestAction("exit"));
        menu.add(menuItem);
        menuBar.add(menu);
        
        
        menu = new JMenu("view");
        for (SeriesType type:SeriesType.values()){
            menuItem = new JMenuItem(new TestAction(type.name()));
            menu.add(menuItem);
        }
        menuBar.add(menu);
        
        menu = new JMenu("tool");
        menuItem = new JMenuItem(new TestAction("auto"));
        menu.add(menuItem);
        
        menuItem = new JMenuItem(new TestAction("auto2"));
        menu.add(menuItem);
        
        menuBar.add(menu);
        
        toolBar = new JToolBar();
        for (SeriesType type:SeriesType.values()){
            toolBar.add(new TestAction(type.name()));
        }
        toolBar.addSeparator();
        for (int i=0;i<sessions.length;i++){
            toolBar.add(new TestAction(sessions[i]));
        }

        setJMenuBar(menuBar);
        
        panel.add(splitPane);
        panel.add(toolBar,BorderLayout.PAGE_START);
        
        setContentPane(panel);
        open();
    }
    
    public void doCommand(String command){
        ChartSeries series;
        switch (command){
            case "exit":
                System.exit(0);
                break;
            case "open":
                readData();
                break;
                
            case "auto":
                chart.autoRange();
                chart.updateUI();
                break;
            case "auto2":
                chart.autoRang2();
                chart.updateUI();
                break;
            case "BAR_CHART":case "LINE_CHART":case "AREA_CHART":case "STACKED":
                seriesType = SeriesType.valueOf(command);
                readData();
                if (command.equals("STACKED")){
                    chart.autoRang2();
                }
                break;
            case "Зима":case "Весна":case "Лето":case "Осень":
                series=chart.findSeries(command);
                if (series!=null){
                    series.setVisible(!series.isVisible());
                    chart.updateUI();
                }
                break;
            default:
                System.out.println(command);
        }
    }
    
    private ChartSeries createSeries( Integer session){
        DataModel model = (DataModel)table.getModel();
        ChartSeries series = ChartSeries.createSeries(seriesType, sessions[session], sessionColor[session]);
        for (Object[] rowset:model.data){
            if (rowset[1].equals(session)){
                try{
                    series.addValue((Integer)rowset[0], rowset[2]);
                } catch (Exception e){
                    System.out.println(rowset[1].toString()+" "+rowset[2].toString()+" "+rowset[2].getClass().getName());
                }
            }
        }
        series.rebuild();
        return series;
    }
    
    public void readData(){
        chart.clear();
        ChartSeries series;
        for (int i=0;i<sessions.length;i++){
            series = createSeries(i);
//            series.rebuild();
            chart.addSeries(series);
        }
        chart.autoRange();
    //    chart.autoRang2();
        chart.updateUI();
    }
    
    public void open(){
//        String text = "2001=10.0;2002=10.2;2003=10.4;2004=10.5;2005=10.6";
        DataModel model = new DataModel();
        model.columnCount=3;
        model.columns=new String[]{"Year","Sisson","Temperature"};

        Float tenperature;
        for (int year=2001;year<2015;year++){
            for (int session=0;session<sessions.length;session++){
                switch (session){
                    case 0:
                        tenperature =new Float(-7.0+Math.random()*10);
                        break;
                    case 1:
                        tenperature =new Float(0.0+Math.random()*10);
                        break;
                    case 2:
                        tenperature =new Float(+7.0+Math.random()*10);
                        break;
                    case 3:
                        tenperature =new Float(0.0+Math.random()*10);
                        break;
                    default:
                        tenperature = new Float(3.3);
                }
                model.data.add(new Object[]{year,session,tenperature});
            }
        }

        table.setModel(model);
        model.addTableModelListener(new TableModelListener() {

            @Override
            public void tableChanged(TableModelEvent e) {
                readData();
            }
        });
    }
    
    public static void createAndShow(){
        JFrame frame = new Test5();
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.pack();
        frame.setVisible(true);
        
    }
    
    public static void main(String[] args){
        createAndShow();
    }
    
}

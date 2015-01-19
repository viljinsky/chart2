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

package ru.viljinsky.simples;

/**
 *
 * @author vadik
 */
import java.awt.*;
import java.awt.event.*;
import javax.swing.*;
import ru.viljinsky.chart.*;
        
public class Simple1 extends Chart implements ActionListener{
    JMenuBar menuBar;
    String data = "2001=10.2;2002=12.5;2003=18.3;2004=8.7";
    
    public Simple1(){
        menuBar = new JMenuBar();
        JMenu menu;
        JMenuItem menuItem;
        
        menu= new JMenu("File");
        
            menuItem = new JMenuItem("add");
            menuItem.addActionListener(this);
            menu.add(menuItem);
       menu.addSeparator();
        
        for (SeriesType type:SeriesType.values()){
            menuItem = new JMenuItem(type.name());
            menuItem.addActionListener(this);
            menu.add(menuItem);
        }
        
        menuBar.add(menu);
        
        openChart(SeriesType.AREA_CHART);
        
    }
    
    
    
    public void openChart(SeriesType type){
        ChartSeries series;
        series = ChartSeries.createSeries(type, "series3", Color.ORANGE);
        series.setData(data);
        addSeries(series);
    }
    
    public void doCommand(String command){
        System.out.println(command);
        ChartSeries series;
        SeriesType type;
        switch (command){
            case "BAR_CHART":case "LINE_CHART":case "AREA_CHART": case "STACKED":
                ChartElement e = getSelectedElement();
                if (e==null) return;
                series = e.getSeries();
                getSeries().remove(series);
                
                type = SeriesType.valueOf(command);
                openChart(type);
                break;
            case "add":    
                series = ChartSeries.createSeries(SeriesType.BAR_CHART, "Series1", Color.MAGENTA);
                series.setData(data);
                addSeries(series);
                break;
        }
        updateUI();
    }
    
    public static void createAndShow(){
        Simple1 chart = new Simple1();
        JFrame frame = new JFrame();
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setContentPane(chart);
        
        frame.setJMenuBar(chart.menuBar);
        
        frame.pack();
        frame.setVisible(true);
    }
    
    public static void main(String[] args){
        createAndShow();
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        doCommand(e.getActionCommand());
    }
    
}

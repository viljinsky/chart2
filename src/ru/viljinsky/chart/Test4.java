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
import java.awt.event.*;
import java.net.URL;
import javax.swing.*;

public class Test4 extends Chart{
    JMenuBar menuBar;
    JToolBar toolBar;
    ImageIcon iconBar;
    ImageIcon iconPlot;
    ImageIcon iconArea;
    
    class A extends AbstractAction{

        public A(String caption,ImageIcon icon){
            super(caption,icon);
            putValue(AbstractAction.ACTION_COMMAND_KEY,caption);
        }
        @Override
        public void actionPerformed(ActionEvent e) {
            doCommand(e.getActionCommand());
        }
    }
    
    public Test4(){
        
        iconBar = createImage("bar.png");
        iconPlot = createImage("plot.png");
        iconArea = createImage("area.png");
        
        setCaption( "Времена года");
        xAxis.setCaption("годы");
        yAxis.setCaption("t,0C");
        yAxis.minorTick = 5;
        String[] commands={"Show3D","horizontalgrid","werticalgrid","bar","line","area"};
        menuBar = new JMenuBar();
        JMenu menu = new JMenu("View");
        JMenuItem menuItem;
        for (String s:commands){
            menuItem=new JMenuItem(new AbstractAction(s) {
                
                @Override
                public void actionPerformed(ActionEvent e) {
                    doCommand(e.getActionCommand());
                }
            });
            menu.add(menuItem);
        }
        menuBar.add(menu);
//        menuBar.add(getSeriesMenu());
       
        toolBar = new JToolBar();
        for (String s:new String[]{"bar","line","area"}){
            toolBar.add(new A(s,iconPlot)) ;
                
        }
        
    }
    
    public ImageIcon createImage(String name){
        
        URL url = getClass().getResource("../images/"+name);
        if (url!=null){
            ImageIcon result = new ImageIcon(url);
            return result;
        }
        System.err.println(name+" - not found \n"+url);
        return null;
    }
    
    public void doCommand(String command){
        switch (command){
            case "line":
                open(SeriesType.LINE_CHART);
                break;
            case "bar":
                open(SeriesType.BAR_CHART);
                break;
            case "area":
                open(SeriesType.AREA_CHART);
                break;
        }
    }
    
    public void open(SeriesType type){
        clear();
        
        ChartSeries[] series ={
            ChartSeries.createSeries(type, "Зима", Color.blue),
            ChartSeries.createSeries(type, "Весна", Color.green),
            ChartSeries.createSeries(type, "Лето", Color.yellow),
            ChartSeries.createSeries(type, "Осень", Color.pink)
        };
                
        String[] data = {
            "2009=-5;2010=-5.2;2011=-8.3;2012=-2.3;2013=-3.2;2014=-3.9",
            "2009=-1.0;2010=2.5;2011=2.3;2012=-0.8;2013=-0.2;2014=1.2",
            "2009=15.3;2010=19.2;2011=17.3;2012=12.3;2013=14.2;2014=12.9",
            "2009=7;2010=6;2011=9;2012=5;2013=3;2014=5"
        };
        
        int x;float v;
        String[] d;
        for (int i=0;i<4;i++){
            d = data[i].split(";");
            for (String s: d ){
                try{
                    x=Integer.valueOf(s.split("=")[0]);
                    v=Float.valueOf(s.split("=")[1]);
                    series[i].addValue(x, v);
                } catch (Exception e){
                    System.out.println("-->"+s+"\n"+e.getMessage());
                }
            };
            series[i].rebuild();
            addSeries(series[i]);
        }
        
        
        autoRange();
        updateUI();
        menuBar.add(getSeriesMenu());

        
    }
    
    public static void createAndShow(){
        Test4 chart = new Test4();
        JFrame frame = new JFrame("Test4");
        frame.setTitle("Chart _'"+chart.getCaption()+"'");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        Container content = frame.getContentPane();
        content.add(chart);
        frame.getContentPane().add(chart.toolBar,BorderLayout.PAGE_START);
        frame.setJMenuBar(chart.menuBar);
        frame.pack();
        frame.setVisible(true);
        chart.open(SeriesType.AREA_CHART);
    }
    
    public static void main(String[] args){
        createAndShow();
    }
}

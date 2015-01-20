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

import java.awt.Color;
import javax.swing.JFrame;

/**
 *
 * @author vadik
 */
public class Test3 extends Chart{
    public Test3(){
        setCaption("Средняя температура");
        Object[][] data = {{2005,"1"},{2006,"2.7"},{2007,"2"},{2008,"3.3"},{2009,"4"}};
        Object[][] data1 = {{2005,"3"},{2006,"2.7"},{2007,"7"},{2008,"6.2"},{2009,"1"}};
        
        ChartSeries series1 = ChartSeries.createSeries(SeriesType.BAR_CHART, "Лето", Color.yellow);
        for (Object[] d:data){
            series1.addValue((Integer)d[0],Double.valueOf((String)d[1]));
        }
        series1.rebuild();
        
        ChartSeries series2 = ChartSeries.createSeries(SeriesType.BAR_CHART, "Зима", Color.green);
        for (Object[] d:data1){
            series2.addValue((Integer)d[0],Double.valueOf((String)d[1]));
        }
        series2.rebuild();
        
        
        
        addSeries(series1);
        addSeries(series2);
        autoRange();
    }

    @Override
    protected void onBarClick(ChartElement bar) {
        System.out.println(bar);
    }
    
    
    public static void main(String[] args){
        JFrame frame = new JFrame("Test3");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setContentPane(new Test3());
        frame.pack();
        frame.setVisible(true);
        
    }
    
}

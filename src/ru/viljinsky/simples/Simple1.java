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
import javax.swing.*;
import ru.viljinsky.chart.*;
        
public class Simple1 extends Chart{
    public Simple1(){
        String data = "2001=10.2;2002=12.5;2003=18.3;2004=8.7";
        ChartSeries series = ChartSeries.createSeries(SeriesType.BAR_CHART, "Series1", Color.yellow);
        series.setData(data);
        addSeries(series);
    }
    
    public static void createAndShow(){
        JFrame frame = new JFrame();
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setContentPane(new Simple1());
        frame.pack();
        frame.setVisible(true);
    }
    
    public static void main(String[] args){
        createAndShow();
    }
    
}

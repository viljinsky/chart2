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
        
public class Simple2 extends Chart{
    public static JMenuBar menuBar = new JMenuBar();
    public Simple2(){
        createSeries(LINE,Color.CYAN).setData("2001=10;2002=20;2003=12");
        createSeries(BAR,Color.MAGENTA).setData("2001=11;2002=19;2003=12.2");
        autoRange();
        menuBar.add(getViewMenu());
        menuBar.add(getSeriesMenu());
        menuBar.add(getSeriesTypeMenu());
    }
    
    public static void main(String[] args){
        JFrame frame = new JFrame("Simple2");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setContentPane(new Simple2());
        frame.setJMenuBar(menuBar);
        frame.pack();
        frame.setVisible(true);
    }
}

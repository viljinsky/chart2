/*
 * Copyright (C) 2015 vadim iljinsky
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
import java.awt.Font;
import java.awt.Graphics;
import java.awt.Rectangle;

/**
 * Легенда диаграммы
 * @author vadik
 */
public class ChartLegend {
    Font chartFont;
    Integer fontHeight;
    Color backGround;
    Color foreGround;
    public Chart chart;
    boolean visible = true;

    public boolean isVisible() {
        return visible;
    }

    public void setVisible(boolean visible) {
        this.visible = visible;
    }

    public ChartLegend(Chart chart) {
        this.chart = chart;
        backGround = Color.WHITE;
        foreGround = Color.BLACK;
        fontHeight = 12;
        chartFont = new Font("courier", Font.PLAIN, fontHeight);
    }

    public void draw(Graphics g, Rectangle rect) {
        if (chart.getSeriesCount()>0){
        
            rect.height=(fontHeight+4)*chart.getSeriesCount();
        }
        
        g.setFont(chartFont);
        g.setColor(backGround);
        g.fillRect(rect.x, rect.y, rect.width, rect.height);
        g.setColor(foreGround);
        g.drawRect(rect.x, rect.y, rect.width, rect.height);
        int h = fontHeight;//rect.height / chart.getSeriesCount();
        Rectangle r1 = rect;
        Rectangle r2 = new Rectangle();
        r1.height = h;
        for (ChartSeries series : chart.getSeries()) {
            r2.x = r1.x + 4;
            r2.y = r1.y + 4;
            r2.height = h - 2;
            r2.width = h - 2;
            g.setColor(series.getColor());
            g.fillRect(r2.x, r2.y, r2.width, r2.height);
            g.setColor(foreGround);
            g.drawRect(r2.x, r2.y, r2.width, r2.height);
            g.drawString(series.getCaption(), r1.x + r2.width + 8, r1.y + fontHeight);
            r1.y += h;
        }
    }
    
}

/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
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

    public ChartLegend(Chart chart) {
        this.chart = chart;
        backGround = Color.WHITE;
        foreGround = Color.BLACK;
        fontHeight = 12;
        chartFont = new Font("courier", Font.PLAIN, fontHeight);
    }

    public void draw(Graphics g, Rectangle rect) {
        rect.height=(fontHeight+4)*chart.getSeriesCount();
        
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

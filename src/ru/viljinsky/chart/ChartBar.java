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
import java.awt.Graphics;
import java.awt.Point;
import java.awt.Polygon;
import java.awt.Rectangle;

/**
 * Базовый элемент для  графиков
 * @author vadik
 */

abstract class ChartElement{
    ChartSeries series;
    Integer position;
    Rectangle bounds=null;
    Object value;
    
    public ChartElement(ChartSeries series, Integer position) {
        this.series = series;
        this.position = position;
    }
    
    public boolean hitTest(int x, int y) {
        if (bounds!=null)
            return bounds.contains(x, y);
        else 
            return false;
    }

    public void setBounds(Rectangle bounds) {
        this.bounds = bounds;
    }
    
    public abstract void draw(Graphics g);
    
    public Integer getValueK(Float k){
        return series.getYValueK(position, k);
    }
    
    public Integer getValue() {
        return series.getYValue(position);
    }
    
    public Integer getValue(Float k){
        return series.getYValueK(position, k);
    }
    
    @Override
    public String toString(){
        return (series!=null?series.name+" ":"")+position+" "+value.toString();
    }
    }

class DefaultChartElement extends ChartElement{

    public DefaultChartElement(ChartSeries series, Integer position) {
        super(series, position);
    }

    @Override
    public void draw(Graphics g) {
        Point p = series.getElementPoint(this);
        bounds = new Rectangle(p.x-5,p.y-5,10,10);
        g.setColor(series.color);
        g.fillRect(p.x-5, p.y-5, 10, 10);
        g.setColor(Color.black);
        g.drawRect(p.x-5, p.y-5, 10,10);
    }
}

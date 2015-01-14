/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package ru.viljinsky.chart;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.Polygon;
import java.awt.Rectangle;

/**
 *
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
}

class ChartPoint extends ChartElement{

    public ChartPoint(ChartSeries series, Integer position) {
        super(series, position);
    }

    @Override
    public void draw(Graphics g) {
        int x,y,w,h;
        x=bounds.x;
        y=bounds.y;
        w=bounds.width;
        h=bounds.height;
        g.setColor(series.getColor());
        g.fillOval(x,y,w,h);
        g.setColor(Color.black);
        g.drawOval(x,y,w,h);
        
        g.drawString(value.toString(), x, y);
    }
}

class ChartBar extends ChartElement{

    public ChartBar(ChartSeries series, Integer position) {
        super(series, position);
    }

    @Override
    public void draw(Graphics g) {
        int x,y,w,h;
        x  = bounds.x;
        y  = bounds.y;
        h  = bounds.height;
        w  = bounds.width;
        
        g.setColor(series.getColor());
        g.fillRect(x,y,w,h);
        // 3d
        g.setColor(Color.gray);
        g.drawLine(x, y, x+w, y);
        g.drawLine(x+w, y, x+w, y+h);
        
        Polygon p = new Polygon(new int[]{x,x+10,x+10+w,x+10+w,x+w,x+w,x},
                                new int[]{y,y-10,y-10,y-10+h,y+h,y,y},
                                7 );
        g.setColor(series.getColor());
        g.fillPolygon(p);
        g.setColor(Color.gray);
        g.drawPolygon(p);
    }

    

    @Override
    public String toString() {
        return "bar xValue:" + position + " yValue:" + value + " series:" + series.getCaption();
    }
    
}

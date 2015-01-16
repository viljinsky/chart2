/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
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

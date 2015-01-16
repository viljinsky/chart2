/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package ru.viljinsky.chart;

import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Point;
import java.awt.Graphics;
import java.awt.Rectangle;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Set;
import java.util.List;
import java.util.ArrayList;
import javax.swing.AbstractAction;
import java.awt.Graphics2D;

/**
 *
 * @author vadik
 */
enum SeriesType{
    BAR_CHART,
    LINE_CHART,
    AREA_CHART,
    STACKED
}

abstract class ChartSeries{
    Chart chart = null;
    protected String name = "Noname";
    protected Color color = Color.pink;
    protected HashMap<Integer, Object> data = new HashMap<>();
    protected List<ChartElement> elements = new ArrayList<>();
    protected SeriesType seriesType;
    
    public abstract void draw(Graphics g);
    public abstract ChartElement createElement(Integer id);
    
    
    public Point getElementPoint(Integer xPosition){
        Integer x,y;
        Float kx,ky;
        Rectangle r = chart.getWorkArea();

        ChartElement element = findElement(xPosition);
        if (element == null) return null;
        
        ky = new Float(r.height/(chart.yAxis.maxValue-chart.yAxis.minValue));
        kx = new Float(r.width/(chart.xAxis.maxValue-chart.xAxis.minValue));
        y = r.y+r.height - element.getValueK(ky) + Math.round(chart.yAxis.minValue*ky);
        x = r.x+ Math.round((xPosition-chart.xAxis.minValue)*kx);

        return new Point(x,y);
    }
    

    public ChartSeries(String name, Color color) {
        this.color = color;
        this.name = name;
    }
    
    public String getCaption(){
        return name;
    }
    
    public Color getColor(){
        return color;
    }
    /**
     * Количество элементов в серии
     * @return Количество элементов в серии
     */
    public Integer getElementCount(){
        return data.size();
    }
    
    /**
     * Список всех элементов серии
     * @return Список всех элементов серии
     */
    public List<ChartElement> getElements(){
        return elements;
    }
    
    /**
     * Поиск элемента серии по значению X
     * @param xValue - значение элемента по оси X
     * @return  если элемент существует - найденный ChartElement,
     *  в противном случае null
     */
    public ChartElement findElement(Integer xValue) {
        for (ChartElement element : elements) {
            if (element.position.equals(xValue)) {
                return element;
            }
        }
        return null;
    }

    /**
     * Получение элемента по порядковому номеру
     * @param index номер элемента по порядку в списке
     * @return ChartElement
     */
    public ChartElement getElement(Integer index){
        return elements.get(index);
    }
    
    public Integer getMinX() {
        Integer result = Integer.MAX_VALUE;
        for (Integer n : data.keySet()) {
            if (n < result) {
                result = n;
            }
        }
        return result;
    }
    
    public Integer getMaxX() {
        Integer result = Integer.MIN_VALUE;
        for (Integer n : data.keySet()) {
            if (n > result) {
                result = n;
            }
        }
        return result;
    }

    public Integer getMaxY() {
        Integer result = Integer.MIN_VALUE;
        Integer value;
        for (Integer n : data.keySet()) {
            value = getYValue(n);
            if (value > result) {
                result = value;
            }
        }
        return result;
    }

    public Integer getMinY() {
        Integer result = Integer.MAX_VALUE;
        Integer value;
        for (Integer n : data.keySet()) {
            value = getYValue(n);
            if (value < result) {
                result = value;
            }
        }
        return result;
    }

    public Integer getYValue(Integer xValue) {
        Object v = data.get(xValue);
        if (v != null) {
            if (v instanceof Integer) {
                return (Integer) v;
            } else if (v instanceof Long) {
                return ((Long) v).intValue();
            } else if (v instanceof Float) {
                return Math.round((Float) v);
            } else if (v instanceof Double) {
                return Math.round(new Float((Double) v));
            }
        }
        return null;
    }
    /**
     * Получение значение с коэффициентом
     * @param xValue
     * @param k
     * @return 
     */
    public Integer getYValueK(Integer xValue,Float k) {
        Object v = data.get(xValue);
        if (v != null) {
            if (v instanceof Integer) {
                return Math.round((Integer) v * k);
            } else if (v instanceof Long) {
                return  Math.round(((Long) v ).intValue()*k);
            } else if (v instanceof Float) {
                return Math.round((Float) v * k);
            } else if (v instanceof Double) {
                return Math.round(new Float((Double) v) * k);
            }
        }
        return null;
    }
    

    
    public void addValue(Integer xValue, Object yValue) {
        if (yValue instanceof String){
            Float f = Float.parseFloat((String)yValue);
            data.put(xValue, f);            
        } else 
            data.put(xValue, yValue);
    }

    
    public void rebuild() {
        elements = new ArrayList<>();
        Set<Integer> keySet = data.keySet();
        ChartElement element;
        int id;
        for (Iterator it = keySet.iterator(); it.hasNext();) {
            id = (Integer) it.next();
            element  = createElement(id);
            element.value = data.get(id);
            elements.add(element);
        }
    }

    public void setData(HashMap<Integer, Object> data) {
        this.data = new HashMap<>();
        for (Integer key : data.keySet()) {
            this.data.put(key, data.get(key));
        }
        rebuild();
    }


    /**
     * Создание экземпляра случайных данных с указанным количеством элементов
     * @param count колличество элементов
     * @return сгенерированный набор данных
     */
    public static HashMap<Integer, Object> createData(Integer count) {
        HashMap<Integer, Object> result = new HashMap<>();
        for (int i = 0; i < count; i++) {
            result.put(i, Math.random() * 100);
        }
        return result;
    }
    
    
    
    public static ChartSeries createSeries(SeriesType seriesType,String caption,Color color){
        ChartSeries series;
        switch (seriesType){
            case BAR_CHART:
                series = new ChartBarSeries(caption, color);             
                break;
            case LINE_CHART:
                series = new ChartLineSeries(caption,color);
                break;
            case AREA_CHART:
                series=new ChartAreaSeries(caption, color);
                break;
            case STACKED:
                series= new StackedSeries(caption,color);
                break;
            default:
                return null;
        }
        series.seriesType=seriesType;
        return series;
    }
}


class ChartLineSeries extends ChartSeries{

    public ChartLineSeries(String name, Color color) {
        super(name, color);
    }
    
    @Override
    public String toString(){
        return "LineSeries "+name+" "+color;
    }

    @Override
    public void draw(Graphics g) {
        int xPosition;
        Rectangle r = chart.getWorkArea();
        int xMinValue,xMaxValue,yMinValue,yMaxValue;
        xMinValue = chart.xAxis.minValue;
        xMaxValue = chart.xAxis.maxValue;
        yMinValue=chart.yAxis.minValue;
        yMaxValue = chart.yAxis.maxValue;
        
        float kx= r.width/(xMaxValue-xMinValue);
        float ky = r.height/(yMaxValue - yMinValue);
        
        int x,y;
        
        chart.xAxis.begin();
        while (chart.xAxis.hasNext()){
            xPosition = chart.xAxis.next();
            ChartPoint element = (ChartPoint)findElement(xPosition);
            if (element!=null){
                x=r.x + Math.round((xPosition-xMinValue) * kx);
                y=r.y + r.height;
                y-= Math.round((element.getValue() - chart.yAxis.minValue)*ky);
                element.bounds = new Rectangle(x-5, y-5, 10, 10);
                element.draw(g);
//                g.setColor(element.series.color);
//                g.fillOval(x-5, y-5, 10, 10);
//                g.setColor(Color.black);
//                g.drawOval(x-5, y-5, 10, 10);
                
            }
        }
    }

    @Override
    public ChartElement createElement(Integer id) {
        return new ChartPoint(this, id);
    }
}

class ChartAreaSeries extends ChartSeries{

    public ChartAreaSeries(String name, Color color) {
        super(name, color);
    }
    
    @Override
    public void draw(Graphics g2) {
        Graphics2D g = (Graphics2D)g2;
        g.setStroke(new BasicStroke(4));
        
        chart.xAxis.begin();
        Integer xPosition,yPosition;
        
        int x,y;
        Integer lastX=null,lastY=null;
        ChartElement el ;
        Rectangle r = chart.getWorkArea();
        float kx,ky;
        kx = r.width/(chart.xAxis.maxValue-chart.xAxis.minValue);
        ky = r.height/(chart.yAxis.maxValue-chart.yAxis.minValue);
        
        while (chart.xAxis.hasNext()){
            xPosition=chart.xAxis.next();
            el =  findElement(xPosition);
            if (el==null) continue;
            
            y = r.y+r.height - Math.round((el.getValue()-chart.yAxis.minValue)*ky);
            x = r.x + Math.round(kx*(xPosition - chart.xAxis.minValue));
            
            g.setColor(color);
            g.fillRect(x-5, y-5, 10, 10);
            
           
            if (lastX!=null){
                g.drawLine(lastX, lastY, x, y);
            }
            lastX=x;
            lastY=y;
        }
        g.setStroke(new BasicStroke(1));
    }

    @Override
    public ChartElement createElement(Integer id) {
        return new ChartPoint(this, id);
    }
    
}

class ChartBarSeries extends ChartSeries{

    public ChartBarSeries(String name, Color color) {
        super(name, color);
    }
    
    @Override
    public String toString(){
        return "BarSeries "+name+" "+color;
    }


    private Integer getOffset(){
        Integer result = 0;
        ChartSeries series;
        for (Integer i=0;i<chart.getSeriesCount();i++){
            series = chart.getSeries().get(i);
            if (series == this)
                return result;
            if (seriesType.equals(series.seriesType)){
                result +=20;
            }
        }
        return result;
    }
    
    @Override
    public void draw(Graphics g) {
        if (chart==null){
            return ;
        };
        
        int minYValue = chart.yAxis.minValue;
        int maxYValue = chart.yAxis.maxValue;
        
        Rectangle rect = chart.getWorkArea();
        
        Integer xValue,yValue,yValue0 ;
        ChartElement bar;
        
        int xOffset = getOffset();
        
        float f = rect.height/(maxYValue-minYValue);
        
        yValue0 = Math.round(-minYValue*f);
        chart.xAxis.begin();
        while (chart.xAxis.hasNext()){
            xValue = chart.xAxis.next();
            bar = (ChartBar)findElement(xValue);
            if (bar!=null){
                
                yValue = bar.getValueK(f) - Math.round(minYValue*f);
                                                              
                int x=chart.getBarCenter(rect, bar.position);
                int n = chart.getSeriesCount();
                int x1,y1,w,h;
                x1 = x+xOffset-(n*20/2);
                w=20;
                
                y1=rect.y+rect.height-yValue;
                
                if (yValue>=0)
                    h=(yValue0>0?yValue-yValue0:yValue);
                else
                    h=(yValue0>0?yValue0:yValue);
                
                bar.setBounds(new Rectangle(x1,y1,w,h));
                bar.draw(g);
                
                if (yValue0>0){
                    y1 = rect.y+rect.height-yValue0;
                    g.drawLine(x1, y1, x1+w, y1);
                }
            }
        }
    }

    @Override
    public ChartElement createElement(Integer id) {
        return new ChartBar(this, id);
    }
    
}

class StackedSeries extends ChartSeries{

    public StackedSeries(String name, Color color) {
        super(name, color);
    }

    @Override
    public void draw(Graphics g) {
        Integer xValue;
        Point p;
        ChartElement element ;
        chart.xAxis.begin();
        while (chart.xAxis.hasNext()){
            xValue = chart.xAxis.next();
            element = findElement(xValue);
            if (element!=null){
                element.draw(g);
            }
        }
    }

    @Override
    public ChartElement createElement(Integer id) {
        return new DefaultChartElement(this,id);
    }
}

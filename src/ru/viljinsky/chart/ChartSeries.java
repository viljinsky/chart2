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
import java.awt.Polygon;

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
    protected boolean visible = true;
    protected boolean threeD = false;

    public boolean isVisible() {
        return visible;
    }

    public void setVisible(boolean visible) {
        this.visible = visible;
    }
    

    /**
     * Возвращает соличество серий тогоже типа
     * @return 
     */
    public Integer getSeriesCount(){
        Integer result = 0;
        for (ChartSeries series:chart.getSeries()){
            if (series.seriesType== this.seriesType){
                result+=1;
            }
        }
        return result;
    }
    
    public Integer getOffset(){
        Integer  result = 0;
        for (ChartSeries series:chart.seriesList){
            if (series==this){
                return result;
            }
            result+=20;
        }
        return result;
    }
    
    
    public abstract void draw(Graphics g);
    
    
    /**
     * Метод создаёт элемент серии по умолчанию DefaultChartElement
     * @param id
     * @return 
     */
    protected  ChartElement createElement(Integer id){
        return new DefaultChartElement(this,id);
    };
    
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
     * Поис точки соответсвующей значению элемента на чарте
     * @param element ChartElement;
     * @return Point точка на чарте
     */
    public Point getElementPoint(ChartElement element){
        int x,y;
        float kx,ky;
        Rectangle r = chart.getWorkArea();
        Object v = data.get(element.position);
        if (v==null){
            return null;
        }
        ky = chart.getkY();
        kx = chart.getkX();
        y = r.y+r.height - element.getValueK(ky) + Math.round(chart.yAxis.minValue*ky);
        x = r.x+ Math.round((element.position-chart.xAxis.minValue)*kx);
        
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
    
    /**
     * Добавление элемента в серию
     * @param xValue
     * @param yValue 
     */
    public void addValue(Integer xValue, Object yValue) {
        if (yValue instanceof String){
            Float f = Float.parseFloat((String)yValue);
            data.put(xValue, f);            
        } else 
            data.put(xValue, yValue);
    }

    /**
     * Удаление елемента из серии
     * @param xValue 
     */
    public void removeValue(Integer xValue){
        
    }
    

    public void setData(HashMap<Integer, Object> data) {
        this.data = new HashMap<>();
        for (Integer key : data.keySet()) {
            this.data.put(key, data.get(key));
        }
        rebuild();
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
                series = new BarSeries(caption, color);             
                break;
            case LINE_CHART:
                series = new LineSeries(caption,color);
                break;
            case AREA_CHART:
                series=new AreaSeries(caption, color);
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


class LineSeries extends ChartSeries{

    public LineSeries(String name, Color color) {
        super(name, color);
    }
    
    @Override
    public void draw(Graphics g2) {
        Graphics2D g = (Graphics2D)g2;
        g.setStroke(new BasicStroke(4));
        
        Integer xPosition;
        
        int x,y;
        Integer lastX=null,lastY=null;
        ChartElement element ;
        
        chart.xAxis.begin();
        while (chart.xAxis.hasNext()){
            xPosition=chart.xAxis.next();
            element =  findElement(xPosition);
            if (element!=null){
                Point p = getElementPoint(element);
            
            
                g.setColor(color);
                g.fillRect(p.x-5, p.y-5, 10, 10);


                if (lastX!=null){
                    g.drawLine(lastX, lastY, p.x, p.y);
                }
                lastX=p.x;
                lastY=p.y;
            }
        }
        g.setStroke(new BasicStroke(1));
    }

}

class BarSeries extends ChartSeries{

    public BarSeries(String name, Color color) {
        super(name, color);
    }
    
    
    public void drawElement(Graphics g,ChartElement element){
        Integer xOffset = getOffset();
        Integer height;
        Point p = getElementPoint(element);
        Rectangle bound;
        
        Float ky = chart.getkY();
        Rectangle r = chart.getWorkArea();
        
        int y2 = r.y+r.height + Math.round(chart.yAxis.minValue*ky);
        
        height = Math.abs(y2 - p.y);
        
        if (y2>p.y)
            bound = new Rectangle(p.x+xOffset,p.y,20,height);
        else
            bound =  new Rectangle(p.x+xOffset,y2,20,height);
        element.bounds=bound;
        g.setColor(color);
        g.fillRect(bound.x, bound.y, bound.width, bound.height);
        
        g.setColor(Color.lightGray);
        g.drawRect(bound.x, bound.y, bound.width, bound.height);
        

        Polygon pg = new Polygon(
                new int[]{bound.x,
                    bound.x+10,
                    bound.x+10+bound.width,
                    bound.x+10+bound.width,
                    bound.x+bound.width,
                    bound.x+bound.width,
                    bound.x},
                new int[]{
                    bound.y,
                    bound.y-10,
                    bound.y-10,
                    bound.y-10+bound.height,
                    bound.y+bound.height,
                    bound.y,
                    bound.y},
                7);

        g.setColor(color);
        g.fillPolygon(pg);
        g.setColor(Color.blue);
        g.drawPolygon(pg);
        
        
    }
    
    @Override
    public void draw(Graphics g) {
        ChartElement element;
        Integer xValue;
        chart.xAxis.begin();
        while (chart.xAxis.hasNext()){
            xValue = chart.xAxis.next();
            element = findElement(xValue);
            if (element!=null){
                drawElement(g,element);
            }
            
        }
    }

}

class AreaSeries extends ChartSeries{

    public AreaSeries(String name, Color color) {
        super(name, color);
    }

    @Override
    public void draw(Graphics g) {
        Integer xValue;
        Point p;
        Integer x1,y1,x2,y2;
        Integer lastX1=null,lastY1=null,lastX2=null,lastY2=null;
        Rectangle r = chart.getWorkArea();
        ChartElement element ;
        chart.xAxis.begin();
        while (chart.xAxis.hasNext()){
            xValue = chart.xAxis.next();
            element = findElement(xValue);
            if (element!=null){
                p = getElementPoint(element);
                element.draw(g);
                
                Float ky = chart.getkY();
                x1 = p.x;
                x2 = p.x;
                y1 = p.y;
                y2 =0;
                y2=r.y + r.height + Math.round(chart.yAxis.minValue*ky);
                g.drawLine(x1, y1,x2,y2);
                g.fillRect(x2-2, y2-2, 5, 5);
                      
                if (lastX1!=null){
                    g.setColor(color);
                    int[] px = {lastX1,x1,x2,lastX2,lastX1};
                    int[] py = {lastY1,y1,y2,lastY2,lastY1};
                    g.fillPolygon(px, py, 5);
                    g.setColor(Color.black);
                    g.drawPolyline(px, py, 5);
//                    g.fillRect(lastX1, lastY1, x1-lastX2,lastY2-lastY1);
                }
                
                
                lastX1=x1;
                lastY1=y1;
                lastX2= x2;
                lastY2= y2;
            }
        }
    }

}

class StackedSeries extends ChartSeries{

    public StackedSeries(String name, Color color) {
        super(name, color);
    }
    
    public void drawElement(Graphics g,ChartElement element){
        
        int xOffset = getOffset();
        int yOffset = 0;
        
        if (chart.lastValues.get(element.position)!=null){
            yOffset = chart.lastValues.get(element.position);
        }
        
        Float kx,ky;
        kx=chart.getkX();
        ky=chart.getkY();
        Point p = getElementPoint(element);
        Integer height;
        
        Rectangle r = chart.getWorkArea();
        
        int y2 = r.y+r.height + Math.round( chart.yAxis.minValue*ky);
        height = Math.abs(y2-p.y);

        Rectangle bound; 
        if (p.y<y2)
            bound  = new Rectangle(p.x-10, p.y - yOffset , 20,height );
        else 
            bound  = new Rectangle(p.x-10, y2-yOffset, 20, height);
        element.bounds=bound;
        
        chart.lastValues.put(element.position, yOffset + bound.height);
        
        g.setColor(color);
        g.fillRect(bound.x,bound.y,bound.width,bound.height);
        g.setColor(Color.lightGray);
        g.drawRect(bound.x,bound.y, bound.width, bound.height);
         
        
        g.setColor(Color.black);
        g.drawRect(p.x-2,y2-2,5,5);
        
    }
    
    
    @Override
    public void draw(Graphics g) {
        Integer xValue;
        ChartElement element;
        chart.xAxis.begin();
        while (chart.xAxis.hasNext()){
            xValue = chart.xAxis.next();
            element= findElement(xValue);
            if (element!=null){
                drawElement(g, element);
            }
        }
        System.out.println(chart.lastValues);
    }
}


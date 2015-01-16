/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package ru.viljinsky.chart;

/**
 *
 * @author vadik
 */
// import com.sun.jmx.snmp.BerDecoder;
import java.util.List;
import java.util.ArrayList;
import java.util.HashMap;
import java.awt.*;
import java.awt.event.*;
import javax.swing.*;

/**
 * Панель для отображения графиков
 * Chart chart = new Chart();
 * chart.setCaption("Средняя температура")
 * chart.addSeries(series1);
 * chart.addSeries(series2);
 * chart.addSeries(series3);
 * cahrt.update();
 * @author vadik
 */
public class Chart extends JPanel{
    String caption="Chart demo";
    Font defaultFont = new Font("courier",Font.PLAIN,12);
    Font captionFont = new Font("courier", Font.BOLD, 24);
    ChartAxis xAxis;
    ChartAxis yAxis;
    List<ChartSeries> seriesList = new ArrayList<>();
    ChartLegend legent;
    HashMap<Integer, Integer> lastValues; // Для стеков
    
    private float kX;
    private float kY;

    public float getkX() {
        return kX;
    }

    public float getkY() {
        return kY;
    }

    public void setCaption(String caption){
        this.caption=caption;
    }
    
    /**
     *
     */
    public void clear(){
        seriesList.clear();
    }
    
    public List<ChartSeries> getSeries(){
        return seriesList;
    }
    
    
    /**
     * Добавление серии в диаграмму
     * @param series
     */
    public void addSeries(ChartSeries series){
        Chart chart = series.chart;
        if (chart!=null){
            chart.removeSeries(series);
        }
        series.chart=this;
        seriesList.add(series);
        
    }
    
    public void removeSeries(ChartSeries series){
        seriesList.remove(series);
        series.chart=null;
    }
    
    /**
     * Колличество серий в диаграмме
     * @return Колличество серий в диаграмме
     */
    public int getSeriesCount(){
        return seriesList.size();
    }
    
    /**
     *
     * @return
     */
    public ChartAxis getXAxis(){
        return xAxis;
    }
    
    /**
     *
     * @return
     */
    public ChartAxis getYAxis(){
        return yAxis;
    }

    /**
     *
     */
    public Chart(){
        setPreferredSize(new Dimension(800,600));
        legent = new ChartLegend(this);
        xAxis = new ChartAxis(ChartAxis.X_AXIS);
        xAxis.caption="X,mm";
        yAxis = new ChartAxis(ChartAxis.Y_AXIS);   
        yAxis.caption="Y";
        addMouseListener(new MouseAdapter() {

            @Override
            public void mousePressed(MouseEvent e) {
                int x,y;
                x=e.getX();y=e.getY();
                ChartElement bar = hitTest(x,y);
                if (bar!=null){
                    onBarClick(bar);
                }
            }
        });
    }
    
    
    public ChartSeries findSeries(String seriesName){
        for (ChartSeries series:seriesList){
            if (series.name.equals(seriesName)){
                return series;
            }
        }
        return null;
    }
    /**
     * Проверка клика мыши по бар-диаграмма
     * @param x позиция мыши
     * @param y позиция мыши
     * @return бар если таковоё есть, в противном случаее null
     */
    protected ChartElement hitTest(int x,int y){
        ChartSeries series;
        for (int i=seriesList.size()-1;i>=0;i--){
            series = seriesList.get(i);
            for (ChartElement bar:series.getElements())
                if (bar.hitTest(x, y))
                    return bar;
        }
        return null;
    }
    
    /**
     * событие клика по диаграмме если клик пришёлся на бар
     * @param bar по которому сделан клик.
     */
    protected void onBarClick(ChartElement bar){
        System.out.println(bar.toString());
    }
    
    
    public Rectangle getWorkArea(){
        Integer LEFT_MARGIN = 40;
        Integer TOP_MARGIN = 10;
        Integer RIGHT_MARGIN = 10;
        Integer BOTTON_MARGINE = 40;
        return  new Rectangle(LEFT_MARGIN,
                              TOP_MARGIN,
                              getWidth()-LEFT_MARGIN-RIGHT_MARGIN,
                              getHeight()-TOP_MARGIN-BOTTON_MARGINE);
    }
    
    
    /**
     * Находит позицию значения по оси Х
     * @param rect рабочая область диаграммы
     * @param xValue
     * @return занчение Х на диаграмме для значения Х оси 
     */
    public Integer getBarCenter(Rectangle rect,Integer xValue){
        float k = rect.width/(xAxis.maxValue-xAxis.minValue);
        return  rect.x + Math.round((xValue-xAxis.minValue) * k);
    }
    
    /**
     * Прорисовка осей координат и сетки
     * @param g контекст графики
     * @param rect рабочая область
     */
    public void drawAxis(Graphics g,Rectangle rect){
        
        Font f = new Font("courier",Font.PLAIN,14);
        
        g.setFont(f);
        
        int fh = g.getFontMetrics(f).getHeight();
        int fw;
        String sValue;
        
        Integer value;
        int x1,y1,x2,y2;
        
        xAxis.begin();
        while (xAxis.hasNext()){
            value = xAxis.next();
            x1=rect.x + Math.round((value-xAxis.minValue) * kX);
            x2=x1;
            y1=rect.y;y2=rect.y+rect.height;
            g.setColor(Color.lightGray);
            g.drawLine(x1, y1, x2, y2);
            // Метки
            if (!value.equals(xAxis.maxValue) && !value.equals(xAxis.minValue)){
                g.setColor(Color.black);
                sValue = value.toString();
                fw = g.getFontMetrics().stringWidth(sValue);
                g.drawString(sValue, x1- fw/2, y2+fh);
            }
        }
        // Подпись по оси Х
        sValue = xAxis.caption;
        x1=rect.x+rect.width-g.getFontMetrics().stringWidth(sValue);
        y1=rect.y+rect.height;
        g.setColor(Color.black);
        g.drawString(sValue, x1, rect.y+rect.height+fh);
        
        //----------------------------------------------------------------------
        
        yAxis.begin();
        while (yAxis.hasNext()){
            value = yAxis.next();
            x1=rect.x;
            x2=rect.x+rect.width;
            y1=rect.y+rect.height- Math.round((value-yAxis.minValue)*kY);
            y2=y1;
            g.setColor(Color.lightGray);
            g.drawLine(x1, y1, x2, y2);
            
            if (!value.equals(yAxis.maxValue) && !value.equals(yAxis.minValue)){
                if (value % yAxis.minorTick ==0 ){
                    g.setColor(Color.black);
                    sValue = value.toString();
                    fw = g.getFontMetrics().stringWidth(value.toString());
                    g.drawString(sValue, x1-fw-2, y1+fh/2);
                }
            }
        }
        // подпись по оси Y
        sValue = yAxis.caption;
        x1=rect.x - g.getFontMetrics().stringWidth(sValue)-4;
        y1=rect.y+fh/2;
        g.setColor(Color.black);
        g.drawString(sValue, x1, y1);
    }
    
    @Override
    public void paint(Graphics g){
        Integer LEFT_MARGIN = 40;
        Integer TOP_MARGIN = 10;
        Integer RIGHT_MARGIN = 10;
        Integer BOTTON_MARGINE = 40;
        
        super.paint(g);
        g.setFont(defaultFont);
        Rectangle r ; // Рабочая область диаграммы
        r = new Rectangle(LEFT_MARGIN,TOP_MARGIN,getWidth()-LEFT_MARGIN-RIGHT_MARGIN,getHeight()-TOP_MARGIN-BOTTON_MARGINE);
        
        g.setColor(Color.white);
        g.fillRect(r.x, r.y, r.width,r.height);
        g.setColor(Color.gray);
        g.drawRect(r.x,r.y,r.width, r.height);
        
        kY = new Float(r.height/(yAxis.maxValue-yAxis.minValue));
        kX = new Float(r.width/(xAxis.maxValue-xAxis.minValue));
        
        drawAxis(g, r);
        
        lastValues = new HashMap<>();
        for (ChartSeries series:seriesList){
            if (series.isVisible())
                series.draw(g);
        }
        
        int x,y;
        g.setFont(captionFont);
        g.setColor(Color.black);
        x = r.x+r.width/2 - g.getFontMetrics().stringWidth(caption)/2;
        y = r.y+ g.getFontMetrics().getHeight();
        g.drawString(caption, x,y);
        
        Rectangle r2 = new Rectangle();
        r2.y= 30;
        r2.height= 50;
        r2.x = r.x+r.width- 120;
        r2.width = 120;
        legent.draw(g, r2);
        
    }
    
    public void autoRange(){
        Integer minValue = Integer.MAX_VALUE;
        Integer maxValue = Integer.MIN_VALUE;
        for (ChartSeries series:seriesList){
            if (series.getMinX()<minValue) minValue=series.getMinX();
            if (series.getMaxX()>maxValue) maxValue=series.getMaxX();
        }
        xAxis.setRange(minValue-1, maxValue+1);
        
        minValue = Integer.MAX_VALUE;
        maxValue = Integer.MIN_VALUE;
        for (ChartSeries series:seriesList){
            if (series.getMinY()<minValue) minValue=series.getMinY();
            if (series.getMaxY()>maxValue) maxValue=series.getMaxY();
        }
        yAxis.setRange(minValue-1, maxValue+1);
    }
    
    public void autoRang2(){
        Integer minY,maxY;
        Integer xValue;
        Integer yValue;
        HashMap<Integer,Integer> yValues= new HashMap<>();
        ChartElement element;
        xAxis.begin();
        while (xAxis.hasNext()){
            xValue = xAxis.next();
            yValue=0;
            for (ChartSeries series:seriesList){
                element = series.findElement(xValue);
                if (element!=null){
                    yValue+=element.getValue();
                }
            }
            yValues.put(xValue, yValue);
        }
        minY=Integer.MAX_VALUE;
        maxY=Integer.MIN_VALUE;
        for (Integer n:yValues.keySet()){
            if (yValues.get(n)<minY) minY=yValues.get(n);
            if (yValues.get(n)>maxY) maxY=yValues.get(n);
        }
        System.out.println(yValues);
        System.out.println(minY+" "+maxY);
        yAxis.setRange(minY, maxY);
    }
    
}

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
    public static final int BAR = 1;
    public static final int LINE = 2;
    public static final int AREA = 3;
    public static final int STACK = 4;
    
    ChartCaption chartCaption;
    Font defaultFont = new Font("courier",Font.PLAIN,12);
    Font captionFont = new Font("courier", Font.BOLD, 24);
    ChartAxis xAxis; /** Ось оординат */
    ChartAxis yAxis; /** Ось абсцис */
    List<ChartSeries> seriesList = new ArrayList<>();
    ChartLegend legent;
    HashMap<Integer, Integer> lastValues; // Для стеков
    ChartElement selectedElement =null;
    ViewMenu viewMenu;
    
    public ChartSeries createSeries(Integer sType,Color color){
        
        SeriesType type ;
        switch (sType){
                case BAR : type = SeriesType.BAR_CHART;
                    break;
                case AREA: type = SeriesType.AREA_CHART;
                    break;
                case LINE: type = SeriesType.LINE_CHART;
                    break;
                case STACK: type = SeriesType.STACKED;
                    break;
                default:
                    type = SeriesType.BAR_CHART;
                    
        };
        ChartSeries result = ChartSeries.createSeries(type, type.name(), color);
        addSeries(result);
        return result;
    }
    
    abstract class ChartMenu{
        Action[] actions;
        class ChartAction extends AbstractAction{

            public ChartAction(String command){
                super(command);
            }

            @Override
            public void actionPerformed(ActionEvent e) {
                doCommand(e.getActionCommand());
                updateActionList();
            }
        }
        public ChartMenu(String[] commands){
            actions = new Action[commands.length];
            for (int i=0;i<actions.length;i++){
                actions[i]=new ChartAction(commands[i]);
            }
        }
        
        public abstract void doCommand(String command);
        
        protected void updateActionList(){
            for (Action action:actions){
                updateAction(action);
            }
        }
        
        public abstract void updateAction(Action action);
        
    }
    
    class ViewMenu extends ChartMenu{
    
        public ViewMenu(){
            super(new String[]{"xaxis","yaxis","legent","caption"});
        }
        
        @Override
        public void doCommand(String command){
            System.out.println(command);
            switch (command){
                case "xaxis":
                    getXAxis().setVisible(!getXAxis().isVisible());
                    break;
                case "yaxis":
                    getYAxis().setVisible(!getYAxis().isVisible());
                    break;
                case "legent":
                    legent.setVisible(!legent.visible);
                    break;
                case "caption":
                    chartCaption.setVisible(!chartCaption.isVisible());
                    break;
            }
            updateUI();
        }

        @Override
        public void updateAction(Action action) {
            System.out.println(action.getValue(AbstractAction.NAME));//action.getValue(AbstractAction.ACTION_COMMAND_KEY));
        }
        
    }
    

    //-------------------------------------------------------------------------
    public String getCaption(){
        return chartCaption.text;
    }
    class ChartCaption{

        public boolean isVisible() {
            return visible;
        }

        public void setVisible(boolean visible) {
            this.visible = visible;
        }
        boolean visible = true;
        String text = "Chart" ;
        Rectangle bound;
        Color background = Color.yellow;
        Color foreground = Color.black;
        Font font = new Font("courier",Font.BOLD,24);//new Font("courier", Font.BOLD, 24)
        public ChartCaption(String text){
            this.text=text;
        }
        public void draw(Rectangle  r ,Graphics g){
            int w,h ;
            g.setFont(font);
            w= g.getFontMetrics().stringWidth(text);
            h= g.getFont().getSize();
            bound = new Rectangle(r.x,r.y+5,w,h);
            
            int x,y;
            x= bound.x+(r.width-w)/2;
            y= bound.y;
            g.setColor(background);
            g.fillRect(x,y,w,h);
            
            g.setColor(foreground);
            g.drawString(text,x, y+h-5);
        }
    }
    public JMenu getViewMenu(){
        JMenu result = new JMenu("view");
        for (Action action:viewMenu.actions){
            result.add(action);
        }
        return result;
        
    }
    
    //--------------------------------------------------------------------------
    class SeriesMenuItem extends JCheckBoxMenuItem implements ActionListener{
        ChartSeries series;
        public SeriesMenuItem(ChartSeries series){
            super(series.name);
            this.series=series;
            addActionListener(this);
        }

        @Override
        public void actionPerformed(ActionEvent e) {           
            series.setVisible(this.getState());
            Chart.this.updateUI();
        }
    }
    
    
    public JMenu getSeriesMenu(){
        JMenu result = new JMenu("series");
        SeriesMenuItem  menuItem;
        for (ChartSeries series:getSeries()){
            menuItem = new SeriesMenuItem(series);
            menuItem.setState(true);
            result.add(menuItem);
        }
        return result;
    }

    
//------------------------------------------------------------  
    ButtonGroup group = new ButtonGroup();
    
    class SeriesTypeMenuItem extends JRadioButtonMenuItem implements ActionListener{
        SeriesType type;
        public SeriesTypeMenuItem(SeriesType type){
            super(type.name());
            this.type=type;
            addActionListener(this);
        }

        @Override
        public void actionPerformed(ActionEvent e) {
            
            System.out.println(e.getActionCommand());
        }
    }
    
    public JMenu getSeriesTypeMenu(){
        JMenu result = new JMenu("seriesType");
        SeriesTypeMenuItem menuItem;
        for (SeriesType type:SeriesType.values()){
            menuItem = new SeriesTypeMenuItem(type);
            menuItem.setSelected(true);
            group.add(menuItem);
            result.add(menuItem);
        }
        
        return result;
    }
    
//-----------------------------------------------------
    
    
    public ChartElement getSelectedElement() {
        return selectedElement;
    }

    
    private float kX;
    private float kY;

    public float getkX() {
        return kX;
    }

    public float getkY() {
        return kY;
    }

    public void setCaption(String caption){
        chartCaption.text=caption;
//        this.caption=caption;
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
       
        autoRange();
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
        chartCaption = new ChartCaption("Пример1");
        viewMenu = new ViewMenu();
        addMouseListener(new MouseAdapter() {

            @Override
            public void mousePressed(MouseEvent e) {
                int x,y;
                x=e.getX();y=e.getY();
                selectedElement = hitTest(x,y);
                if (selectedElement!=null){
                    onBarClick(selectedElement);
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
        
        int fh = g.getFont().getSize();// g.getFontMetrics(f).getHeight();
        int fw;
        String sValue;
        
        Integer value;
        int x1,y1,x2,y2;
        
        if (xAxis.isVisible()){
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
        }
        
        //----------------------------------------------------------------------
        
        if (yAxis.isVisible()){
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
        r = new Rectangle(
                LEFT_MARGIN,
                TOP_MARGIN,
                getWidth()-LEFT_MARGIN-RIGHT_MARGIN,
                getHeight()-TOP_MARGIN-BOTTON_MARGINE);
        
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
        
        if (chartCaption.isVisible())
            chartCaption.draw(r,g);
        
        if (legent.isVisible()){
            Rectangle r2 = new Rectangle();
            r2.y= 30;
            r2.height= 50;
            r2.x = r.x+r.width- 120;
            r2.width = 120;
            legent.draw(g, r2);
        }
        
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

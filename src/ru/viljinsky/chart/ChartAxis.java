/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package ru.viljinsky.chart;

/**
 * Ось диаграммы. В зависимости от типа - абсцис или оординат
 * @author vadik
 */
public class ChartAxis {
    
    public static final Integer X_AXIS = 0;
    public static final Integer Y_AXIS = 1;
    
    
    protected Integer minValue = 0;
    protected Integer maxValue = 10;
    Integer majorTick = 1;
    Integer minorTick = 1;
    /** тип оси абсцис или оординат */
    Integer axisType = X_AXIS;
    protected Integer value = 0;
    boolean autoRange = true;
    /** Заголовок оси*/
    String caption;

    public void setCaption(String caption){
        this.caption=caption;
    }
    public ChartAxis(Integer axisType) {
        this.axisType = axisType;
    }

    public void setRange(Integer minValue, Integer maxValue) {
        this.minValue = minValue;
        this.maxValue = maxValue;
    }

    public void setMinValue(Integer value) {
        minValue = value;
    }

    public void setMaxValue(Integer value) {
        maxValue = value;
    }

    /** Начало просмотра значений*/
    public void begin() {
        value = minValue - majorTick;
    }
    
    /** проверка наличия следующего значения */
    public boolean hasNext() {
        return value < maxValue;
    }

    /** получение следующего значения */
    public Integer next() {
        value += majorTick;
        return value;
    }

    public void setAutoRange(boolean value) {
        this.autoRange = value;
    }

    public boolean isAutoRange() {
        return autoRange;
    }
    
}

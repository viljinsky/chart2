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
    boolean visible = true;

    public boolean isVisible() {
        return visible;
    }

    public void setVisible(boolean visible) {
        this.visible = visible;
    }

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

package com.example.tecnicas_reto11;

import javafx.beans.property.SimpleDoubleProperty;
import javafx.beans.property.SimpleStringProperty;

public class Movimiento {

    private SimpleStringProperty date;
    private SimpleDoubleProperty open;
    private SimpleDoubleProperty high;
    private SimpleDoubleProperty low;
    private SimpleDoubleProperty close;
    private SimpleDoubleProperty adjClose;
    private SimpleDoubleProperty volume;

    public Movimiento(String date,double open,double high ,double low ,double close ,double adjClose ,double volume ){
        this.date = new SimpleStringProperty(date);
        this.open = new SimpleDoubleProperty(open);
        this.high = new SimpleDoubleProperty(high);
        this.low = new SimpleDoubleProperty(low);
        this.close = new SimpleDoubleProperty(close);
        this.adjClose = new SimpleDoubleProperty(adjClose);
        this.volume = new SimpleDoubleProperty(volume);
    }

    public double getHigh() {
        return high.get();
    }

    public double getLow() {
        return low.get();
    }

    public double getOpen() {
        return open.get();
    }

    public String getDate() {
        return date.get();
    }

    public double getAdjClose() {
        return adjClose.get();
    }

    public double getClose() {
        return close.get();
    }

    public double getVolume() {
        return volume.get();
    }


}

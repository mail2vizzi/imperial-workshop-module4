package com.vijay.module3.mock;

public class Camera {

    private final Sensor sensor;

    public Camera(Sensor sensor){
        this.sensor = sensor;
    }

    public void pressShutter() {
        // not implemented
    }

    public void powerOn() {
        // not implemented
        sensor.powerUp();
    }

    public void powerOff() {
       // not implemented
    }
}


package com.vijay.module3.mock;

import java.util.concurrent.atomic.AtomicBoolean;

public class Camera {

    private final Sensor sensor;
    private final MemoryCard memoryCard;
    private final AtomicBoolean isPowerOn = new AtomicBoolean(false);

    public Camera(Sensor sensor, MemoryCard memoryCard){
        this.sensor = sensor;
        this.memoryCard = memoryCard;
    }

    public void pressShutter() {
        if(isPowerOn.get()){
            memoryCard.write(sensor.readData());
        }
    }

    public void powerOn() {
        if(!isPowerOn.get()) {
            isPowerOn.set(true);
            sensor.powerUp();
        }
    }

    public void powerOff() {
        sensor.powerDown();
    }
}


package com.vijay.module3.mock;

import java.util.concurrent.atomic.AtomicBoolean;

public class Camera implements WriteListener {

    private final Sensor sensor;
    private final MemoryCard memoryCard;
    private final AtomicBoolean isPowerOn = new AtomicBoolean(false);
    private final AtomicBoolean isWritingOn = new AtomicBoolean(false);

    public Camera(Sensor sensor, MemoryCard memoryCard) {
        this.sensor = sensor;
        this.memoryCard = memoryCard;
    }

    public void pressShutter() {
        if (isPowerOn.get() && !isWritingOn.get()) {
            isWritingOn.set(true);
            memoryCard.write(sensor.readData(), this);
        }
    }

    public void powerOn() {
        if (!isPowerOn.get()) {
            isPowerOn.set(true);
            sensor.powerUp();
        }
    }

    public void powerOff() {
        if (isPowerOn.get()) {
            isPowerOn.set(false);
            if (!isWritingOn.get()) {
                sensor.powerDown();
            }
        }
    }

    @Override
    public void writeComplete() {
        if(isWritingOn.get()){
            isWritingOn.set(false);
        }
        if(!isPowerOn.get()){
            sensor.powerDown();
        }
    }
}


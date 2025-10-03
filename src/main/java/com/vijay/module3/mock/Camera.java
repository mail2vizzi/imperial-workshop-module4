package com.vijay.module3.mock;

import java.util.concurrent.atomic.AtomicBoolean;


/**
 * The Camera class coordinates interactions between a Sensor and a MemoryCard.
 * It powers the sensor on/off and manages the process of capturing data
 * and writing it to the memory card.
 * Implements WriteListener so it can be notified when data writing is complete.
 */
public class Camera implements WriteListener {

    private final Sensor sensor;
    private final MemoryCard memoryCard;
    private final AtomicBoolean isPowerOn = new AtomicBoolean(false);
    private final AtomicBoolean isWritingOn = new AtomicBoolean(false);

    public Camera(Sensor sensor, MemoryCard memoryCard) {
        this.sensor = sensor;
        this.memoryCard = memoryCard;
    }

    /**
     * Pressing the shutter will capture data from the sensor and
     * initiate writing it to the memory card, but only if:
     * - The camera is powered on
     * - The camera is not already writing data
     */
    public void pressShutter() {
        if (isPowerOn.get() && !isWritingOn.get()) {
            isWritingOn.set(true);
            memoryCard.write(sensor.readData(), this);
        }
    }

    /**
     * Powering on the camera:
     * - Sets the power flag to true
     * - Powers up the sensor (if not already on)
     */
    public void powerOn() {
        if (!isPowerOn.get()) {
            isPowerOn.set(true);
            sensor.powerUp();
        }
    }

    /**
     * Powering off the camera:
     * - Sets the power flag to false
     * - If no write is in progress, powers down the sensor immediately
     * - If writing is in progress, the sensor will stay powered on
     *   until the write completes (handled in writeComplete)
     */
    public void powerOff() {
        if (isPowerOn.get()) {
            isPowerOn.set(false);
            if (!isWritingOn.get()) {
                sensor.powerDown();
            }
        }
    }

    /**
     * Callback from the MemoryCard when writing is complete.
     * - Clears the writing flag
     * - If the camera was powered off while writing, powers down the sensor now
     */
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


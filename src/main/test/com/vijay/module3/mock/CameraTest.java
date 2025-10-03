package com.vijay.module3.mock;

import org.jmock.Expectations;
import org.jmock.Mockery;
import org.jmock.integration.junit4.JUnitRuleMockery;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;

public class CameraTest {

    @Rule
    public JUnitRuleMockery context = new JUnitRuleMockery();

    private Camera camera;

    private Sensor sensor;
    private MemoryCard memoryCard;

    @Before
    public void setup(){
        sensor = context.mock(Sensor.class);
        memoryCard = context.mock(MemoryCard.class);
        camera = new Camera(sensor, memoryCard);
    }

    @Test
    public void switchingTheCameraOnPowersUpTheSensor() {
        context.checking(new Expectations(){{
            exactly(1).of(sensor).powerUp();
        }});
        camera.powerOn();
        context.assertIsSatisfied();
    }

    @Test
    public void switchingTheCameraOffPowersDownTheSensor() {
        context.checking(new Expectations(){{
            exactly(1).of(sensor).powerDown();
        }});
        camera.powerOff();
        context.assertIsSatisfied();
    }

    @Test
    public void pressingTheShutterWhenThePowerIsOffDoesNothing(){
        //No action expected on pressShutter.
        camera.pressShutter();
        context.assertIsSatisfied();
    }

    @Test
    public void pressingShutterWithPowerOnCopiesDataToMemoryCard() {
        byte[] data = "Scene captured".getBytes();
        context.checking(new Expectations(){{
            oneOf(sensor).powerUp();
            oneOf(sensor).readData();
            will(returnValue(data));
            exactly(1).of(memoryCard).write(data);
        }});
        camera.powerOn();
        camera.pressShutter();
        context.assertIsSatisfied();
    }

    @Test
    public void switchingCameraOffDuringWriteDoesNotPowerDownSensor(){
        byte[] data = "Scene captured".getBytes();
        context.checking(new Expectations(){{
            oneOf(sensor).powerUp();
            oneOf(sensor).readData();
            will(returnValue(data));
            exactly(1).of(memoryCard).write(data);
        }});
        camera.powerOn();
        camera.pressShutter();
        camera.powerOff();
        context.assertIsSatisfied();
    }

}

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
    public void setup() {
        sensor = context.mock(Sensor.class);
        memoryCard = context.mock(MemoryCard.class);
        camera = new Camera(sensor, memoryCard);
    }

    @Test
    public void switchingTheCameraOnPowersUpTheSensor() {
        context.checking(new Expectations() {{
            exactly(1).of(sensor).powerUp();
        }});
        camera.powerOn();
        context.assertIsSatisfied();
    }

    @Test
    public void switchingTheCameraOffPowersDownTheSensor() {
        context.checking(new Expectations() {{
            oneOf(sensor).powerUp();
            exactly(1).of(sensor).powerDown();
        }});
        camera.powerOn();
        camera.powerOff();
        context.assertIsSatisfied();
    }

    @Test
    public void pressingTheShutterWhenThePowerIsOffDoesNothing() {
        //No action expected on pressShutter.
        camera.pressShutter();
        context.assertIsSatisfied();
    }

    @Test
    public void pressingShutterWithPowerOnCopiesDataToMemoryCard() {
        byte[] data = "Scene captured".getBytes();
        context.checking(new Expectations() {{
            oneOf(sensor).powerUp();
            oneOf(sensor).readData();
            will(returnValue(data));
            exactly(1).of(memoryCard).write(data, camera);
        }});
        camera.powerOn();
        camera.pressShutter();
        context.assertIsSatisfied();
    }

    @Test
    public void switchingCameraOffDuringWriteDoesNotPowerDownSensor() {
        byte[] data = "Beautiful scene captured".getBytes();
        context.checking(new Expectations() {{
            oneOf(sensor).powerUp();
            oneOf(sensor).readData();
            will(returnValue(data));
            exactly(1).of(memoryCard).write(data, camera);
            exactly(0).of(sensor).powerDown();
        }});
        camera.powerOn();
        camera.pressShutter();
        camera.powerOff();
        context.assertIsSatisfied();
    }

    @Test
    public void sensorPowersDownAfterWriteCompletesIfCameraIsOff() {
        byte[] data = "Wonderful scene captured".getBytes();
        WriteListener listener = camera;
        context.checking(new Expectations() {{
            oneOf(sensor).powerUp();
            oneOf(sensor).readData();
            will(returnValue(data));
            exactly(1).of(memoryCard).write(data, listener);
            exactly(1).of(sensor).powerDown();
        }});
        camera.powerOn();
        camera.pressShutter();
        camera.powerOff();
        //Event triggered manually upon write complete.
        camera.writeComplete();
        context.assertIsSatisfied();
    }
}

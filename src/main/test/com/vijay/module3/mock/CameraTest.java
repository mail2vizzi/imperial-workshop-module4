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
        camera = new Camera();
    }

    @Test
    public void switchingTheCameraOnPowersUpTheSensor() {

        context.checking(new Expectations(){{
            exactly(1).of(sensor).powerUp();
        }});
        camera.powerOn();
        context.assertIsSatisfied();
    }

}

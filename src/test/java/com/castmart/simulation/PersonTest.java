package com.castmart.simulation;

import org.jbox2d.dynamics.Body;
import org.jbox2d.dynamics.BodyType;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyFloat;
import static org.mockito.Mockito.when;

@RunWith(MockitoJUnitRunner.class)
public class PersonTest {

    @Mock
    SimulationEnvironment environment;

    @Mock
    Body body;

    Person person;
    @Before
    public void init() {
        when(environment.createCircleShapeOnWorld(any(BodyType.class), anyFloat(), anyFloat(), anyFloat()))
                .thenReturn(body);
        person = new Person(environment, BodyType.DYNAMIC, 0,0,1);
    }

    @Test
    public void testDefaultPersonHealthIsNotInfected() {
        Assert.assertEquals(Health.NOT_INFECTED, person.getPersonHealth());
    }

    @Test
    public void infectPerson_when_NOT_INFECTED_and_INFECTED_healthEvent() {
        // Assert person is not infected.
        Assert.assertEquals(Health.NOT_INFECTED, person.getPersonHealth());
        person.healthEvent(Health.INFECTED);
        Assert.assertEquals(Health.INFECTED, person.getPersonHealth());
    }

    @Test
    public void notInfectPerson_when_NOT_INFECTED_and_RECOVERED_healthEvent() {
        Assert.assertEquals(Health.NOT_INFECTED, person.getPersonHealth());
        person.healthEvent(Health.RECOVERED);
        Assert.assertEquals(Health.NOT_INFECTED, person.getPersonHealth());
    }

    @Test
    public void doNotInfectPerson_when_Recovered_and_INFECTED_healthEvent() {
        // Assert person is not infected.
        Assert.assertEquals(Health.NOT_INFECTED, person.getPersonHealth());
        person.healthEvent(Health.INFECTED);
        Assert.assertEquals(Health.INFECTED, person.getPersonHealth());
        try {
            Thread.sleep(Person.RECOVER_TIME);
        } catch (Exception e){

        }
        person.checkIfRecover(System.currentTimeMillis());
        Assert.assertEquals(Health.RECOVERED, person.getPersonHealth());
    }
}
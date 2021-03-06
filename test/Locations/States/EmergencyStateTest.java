/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Locations.States;

import People.Keycard;
import People.Role;
import static People.Role.EMERGENCYRESPONDER;
import static People.Role.STUDENT;
import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;
import static org.junit.Assert.*;

/**
 *
 * @author Student
 */
public class EmergencyStateTest {
    
    EmergencyState s1;
    Keycard c1, c2, c3;
    
    public EmergencyStateTest() {
    }
    
    @BeforeClass
    public static void setUpClass() {
    }
    
    @AfterClass
    public static void tearDownClass() {
    }
    
    @Before
    public void setUp() {
        s1 = new EmergencyState();
        c1 = new Keycard(new Role[] { STUDENT }, "Dave", "0006");
        c2 = new Keycard(new Role[] { EMERGENCYRESPONDER }, "Fireman", "0007");
        c3 = new Keycard(new Role[] { STUDENT, EMERGENCYRESPONDER }, "Fireman", "0008");
    }
    
    @After
    public void tearDown() {
    }

    @Test
    public void testAccessRequest() {
        System.out.println("Testing AccessRequest()");
        
        assertEquals(false, s1.AccessRequest(c1));
        assertEquals(true, s1.AccessRequest(c2));
        assertEquals(true, s1.AccessRequest(c3));
    }
    
}

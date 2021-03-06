/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package People;

import static People.Role.*;
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
public class RoleTest {
    
    public RoleTest() {
    }
    
    @BeforeClass
    public static void setUpClass() {
    }
    
    @AfterClass
    public static void tearDownClass() {
    }
    
    @Before
    public void setUp() {
    }
    
    @After
    public void tearDown() {
    }

    @Test
    public void testValues() {
    }

    @Test
    public void testValueOf() {
    }

    @Test
    public void testHasTimeAccess() {
        System.out.println("Testing HasTimeAccess()\n(Note that test may fail"
                + " at certain times of the day)");
        
        assertEquals(true, VISITOR.HasTimeAccess());
        assertEquals(true, STAFFMEMBER.HasTimeAccess());
        assertEquals(true, SECURITY.HasTimeAccess());
    }

    @Test
    public void testGetName() {
        System.out.println("Testing GetName()");
        
        assertEquals("Visitor", VISITOR.GetName());
        assertEquals("Staff Member", STAFFMEMBER.GetName());
        assertEquals("Security", SECURITY.GetName());
    }

    @Test
    public void testGetPluralName() {
        System.out.println("Testing GetPluralName()");
        
        assertEquals("Visitors", VISITOR.GetPluralName());
        assertEquals("Staff Members", STAFFMEMBER.GetPluralName());
        assertEquals("Security", SECURITY.GetPluralName());
    }
    
}

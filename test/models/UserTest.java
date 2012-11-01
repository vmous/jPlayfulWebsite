package models;

import junit.framework.TestCase;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import static play.test.Helpers.*;

public class UserTest extends TestCase {
    @Override
    @Before
    public void setUp() throws Exception {
        start(fakeApplication(inMemoryDatabase()));
        super.setUp();
    }

    @Override
    @After
    public void tearDown() throws Exception {
        super.tearDown();
    }

    @Test
    public void testit() {
        // Count user
        assertEquals(3, User.find.findRowCount());

        // Authenticate users
        assertNotNull(User.authenticate("bob@example.com", "secret"));
        assertNotNull(User.authenticate("jane@example.com", "secret"));
        assertNotNull(User.authenticate("jeff@example.com", "secret"));

        assertNull(User.authenticate("bob@example.com", "badpassword"));
        assertNull(User.authenticate("jane@example.com", "badpassword"));
        assertNull(User.authenticate("jeff@example.com", "badpassword"));

        assertNull(User.authenticate("tom@example.com", "secret"));
    }
}

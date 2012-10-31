package models;

import junit.framework.TestCase;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import play.test.*;

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
        new User("vassilis@mailserver.gr", "Vassilis", "Moustakas", "secret").save();
        assertNotNull(User.authenticate("vassilis@mailserver.gr", "secret"));
        assertNull(User.authenticate("foo@mailserver.gr", "badpassword"));
        assertNull(User.authenticate("bar@mailserver.gr", "secret"));
    }
}

package controllers;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import com.google.common.collect.ImmutableMap;

import junit.framework.TestCase;

import play.mvc.Result;
import static play.test.Helpers.*;

public class SignInTest extends TestCase {
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
    public void testIt() {
        Result res = null;

        // -- Authenticate success

        res = callAction(
                controllers.routes.ref.Application.authenticate(),
                fakeRequest().withFormUrlEncodedBody(ImmutableMap.of("email", "bob@example.com", "password", "secret"))
        );
        assertEquals(303, status(res));
        assertEquals("bob@example.com", session(res).get("email"));

        // -- Authenticate failure

        res = callAction(
                controllers.routes.ref.Application.authenticate(),
                fakeRequest().withFormUrlEncodedBody(ImmutableMap.of("email", "bob@example.com", "password", "badpassword"))
        );
        assertEquals(400, status(res));
        assertNull(session(res).get("email"));

        res = callAction(
                controllers.routes.ref.Application.authenticate(),
                fakeRequest().withFormUrlEncodedBody(ImmutableMap.of("email", "unknown@example.com", "password", "secret"))
        );
        assertEquals(400, status(res));
        assertNull(session(res).get("email"));

        // -- Authenticator authenticate

        res = callAction(
                controllers.routes.ref.Application.index(),
                fakeRequest().withSession("email", "bob@example.com")
        );
        assertEquals(200, status(res));

        // -- Authenticator not authenticate

        res = callAction(
                controllers.routes.ref.Application.index(),
                fakeRequest()
        );
        assertEquals(303, status(res));
        assertEquals("/signin", header("Location", res));
    }
}

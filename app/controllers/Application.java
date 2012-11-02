package controllers;

import models.User;
import play.*;
import play.data.Form;
import play.mvc.*;

import views.html.*;

/**
 * The controller class for the application action methods.
 *
 * @author billy
 *
 */
public class Application extends Controller {
    // -- Home

    /**
     * <p>The index / home page action.</p>
     *
     * <p>
     * Annotated with the {@link Secured} authenticator.
     * </p>
     *
     * @return
     */
    @Security.Authenticated(Secured.class)
    public static Result index() {
        return ok(index.render("Your new application is ready.", User.find.byId(request().username())));
    }

    // -- Sign-in / Log-in

    /**
     * The inner class that backs the sign-in page.
     *
     * @author billy
     *
     */
    public static class SignIn {
        /**
         * The e-mail to be authenticated.
         */
        public String email;

        /**
         * The password with which to authenticate the e-mail.
         */
        public String password;

        /**
         * <p>Validates the email and the password given.</p>
         *
         * <p>
         * Uses the {@link models.User.authenticate()} helper method to
         * authenticate the information given.
         * </p>
         *
         * @return A {@code null} value if the validation passes or a
         *         {@link String} with an error message, if it fails.
         */
        public String validate() {
            String ret = null;
            if (User.authenticate(email, password) == null) {
                ret = "Invalid user or password";
            }

            return ret;
        }
    }

    /**
     * <p>The sign-in action.</p>
     *
     * <p>
     * Constructs a {@code 200 OK} HTTP response, containing
     * {@code app/views/signin.scala.html} as a body.
     * </p>
     *
     * @return A {@code 200 OK} HTTP {@link Result}.
     */
    public static Result signIn() {
        return ok(
                signin.render(form(SignIn.class))
        );
    }

    /**
     * <p>The authentication action.</p>
     *
     * <p>
     * Checks the submitted form for errors. If no errors were found it
     * constructs a {@code 303 SEE_OTHER} HTTP response pointing to
     * {@link Application.index()} action. In case of errors it prepares a
     * {@code 400 BAD_REQUEST} HTTP response with
     * {@code app/views/signin.scala.html} as body.
     * </p>
     *
     * @return A {@code 303 SEE_OTHER} HTTP {@link Result} if the request has
     *         no errors; a {@code 400 BAD_REQUEST} HTTP {@link Result}
     *         otherwise.
     */
    public static Result authenticate() {
        Result res = null;
        // Create a form filled with the requested data.
        Form<SignIn> form = form(SignIn.class).bindFromRequest();

        if (form.hasErrors()) {
            res = badRequest(signin.render(form));
        }
        else {
            session().clear();
            session("email", form.get().email);
            res = redirect(
                    routes.Application.index()
            );
        }
        return res;
    }

    // -- Sign-out / Log-out

    /**
     * <p>The sign-out action.</p>
     *
     * <p>
     * Cleans up the session and adds a success message to the flash scope.
     * </p>
     *
     * @return A {@code 303 SEE_OTHER} HTTP {@link Result} pointing to
     * {@link Application.signIn()} action.
     */
    public static Result signOut() {
        session().clear();
        flash("success", "You've successfully signed out.");

        return redirect(
                routes.Application.signIn()
        );
    }
}
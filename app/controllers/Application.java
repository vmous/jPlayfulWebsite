package controllers;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

import javax.persistence.Id;

import com.feth.play.module.pa.PlayAuthenticate;
import com.feth.play.module.pa.providers.password.UsernamePasswordAuthProvider;
import com.feth.play.module.pa.user.AuthUser;

import be.objectify.deadbolt.java.actions.Group;
import be.objectify.deadbolt.java.actions.Restrict;

import models.Evaluation;
import models.User;
import play.*;
import play.api.data.validation.ValidationError;
import play.data.Form;
import static play.data.Form.*;
import play.data.validation.Constraints.Email;
import play.data.validation.Constraints.Required;
import play.mvc.*;
import play.mvc.Http.HeaderNames;
import play.mvc.Http.Session;
import providers.PlayfulLoginUsernamePasswordAuthUser;
import providers.PlayfulUsernamePasswordAuthProvider;
import providers.PlayfulUsernamePasswordAuthProvider.PlayfulSignIn;
import providers.PlayfulUsernamePasswordAuthProvider.PlayfulSignUp;
import scala.App;

import views.html.*;
import views.html.defaultpages.todo;

/**
 * The controller class for the application action methods.
 *
 * @author billy
 */
public class Application extends Controller {

    public static final String FLASH_MESSAGE_KEY = "message";
    public static final String FLASH_ERROR_KEY = "error";
    public static final String USER_ROLE = "user";

    // -- Welcome

    /**
     * <p>The welcome page action.</p>
     *
     * <p>
     * Constructs a {@code 200 OK} HTTP response, containing
     * {@code app/views/welcome.scala.html} as a body.
     * </p>
     *
     * @return A {@code 200 OK} HTTP {@link Result}.
     */
    public static Result welcome() {
        return ok(welcome.render());
    }

    // -- Home

    /**
     * <p>The index / home page action.</p>
     *
     * <p>
     * Constructs a {@code 200 OK} HTTP response, containing
     * {@code app/views/index.scala.html} as a body.
     * </p>
     *
     * <p>
     * Annotated with Deadbolt authorization.
     * </p>
     *
     * @return A {@code 200 OK} HTTP {@link Result}.
     */
    @Restrict(@Group(Application.USER_ROLE))
    public static Result index() {
        User currentUser = getCurrentUser(session());

        return ok(index.render(currentUser));
    }

    public static User getCurrentUser(Session session) {
        AuthUser authUser = PlayAuthenticate.getUser(session);

        return User.findByAuthUserIdentity(authUser);
    }

//    // -- Sign-up

    /**
     * <p>The sign-up action.</p>
     *
     * <p>
     * Constructs a {@code 200 OK} HTTP response, containing
     * {@code app/views/signup.scala.html} as a body.
     * </p>
     *
     * @return A {@code 200 OK} HTTP {@link Result}.
     */
    public static Result signUp() {

        return ok(
                signup.render(PlayfulUsernamePasswordAuthProvider.SIGNUP_FORM)
        );
    }


    /**
     * @return
     */
    public static Result doSignUp() {
        // Web page should not be cached.
        com.feth.play.module.pa.controllers.Authenticate.noCache(response());

        Result res = null;
        Logger.info("AAAAAAAAAAAAAAAAAAAAA");
        Form<PlayfulSignUp> filledForm = PlayfulUsernamePasswordAuthProvider.SIGNUP_FORM
                .bindFromRequest();
        if (filledForm.hasErrors()) {
            // User did not fill everything properly
            Logger.info("BBBBBBBBBBBBBBBBBBBB");
            res = badRequest(signup.render(filledForm));
        } else {
            Logger.info("CCCCCCCCCCCCCCCCCCCCC");
            // Everything was filled
            // do something with your part of the form before handling the user
            // signup
            res = UsernamePasswordAuthProvider.handleSignup(ctx());
        }

        return res;
    }

//    /**
//     * <p>The registration action.</p>
//     *
//     * <p>
//     * Checks the submitted form for errors. If no errors were found it
//     * constructs a {@code 303 SEE_OTHER} HTTP response pointing to
//     * {@link Application.index()} action. In case of errors it prepares a
//     * {@code 400 BAD_REQUEST} HTTP response with
//     * {@code app/views/signup.scala.html} as body.
//     * </p>
//     *
//     * @return A {@code 303 SEE_OTHER} HTTP {@link Result} if the request has
//     *         no errors; a {@code 400 BAD_REQUEST} HTTP {@link Result}
//     *         otherwise.
//     */
//    public static Result register() {
//        Result res = null;
//        Form<User> form = form(User.class).bindFromRequest();
//
//        // Check if e-mail confirmation is successful
//        if(!form.field("email").value().equals(form.field("confirm-email").value())) {
//            form.reject("confirm-email", "Emails should match");
//        }
//
//        // Check if password confirmation is successful
//        if(!form.field("password").value().equals(form.field("confirm-password").value())) {
//            form.reject("confirm-password", "Passwords should match");
//        }
//
//        if(form.hasErrors()) {
//            res = badRequest(signup.render(form));
//        }
//        else {
//            User user = form.get();
//            user.save();
//            session().clear();
//            session("email", form.get().email);
//            res = redirect(routes.Application.index());
//        }
//
//        return res;
//    }
//
    // -- Sign-in / Log-in
//
//    /**
//     * The inner class that backs the sign-in page.
//     *
//     * @author billy
//     *
//     */
//    public static class SignIn {
//        /**
//         * The e-mail to be authenticated.
//         */
//        @Required(message = "Please enter your e-mail")
//        public String email;
//
//        /**
//         * The password with which to authenticate the e-mail.
//         */
//        @Required(message = "Please enter your password")
//        public String password;
//
//        /**
//         * <p>Validates the email and the password given.</p>
//         *
//         * <p>
//         * Uses the {@link models.User.authenticate()} helper method to
//         * authenticate the information given.
//         * </p>
//         *
//         * @return A {@code null} value if the validation passes or a
//         *         {@link String} with an error message, if it fails.
//         */
//        public String validate() {
//            String ret = null;
//            if (User.authenticate(email, password) == null) {
//                ret = "Invalid user or password";
//            }
//
//            return ret;
//        }
//    }

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
                signin.render(PlayfulUsernamePasswordAuthProvider.SIGNIN_FORM)
        );
    }

    /**
     * <p>The sign-in action.</p>
     *
     * <p>
     * Checks the submitted form for errors. If no errors are found it tries to
     * authenticate the user. In case of errors it prepares a
     * {@code 400 BAD_REQUEST} HTTP response and renders the sign-in form
     * again.
     * </p>
     *
     * @return
     *     ...
     */
    public static Result doSignIn() {
        // Web page should not be cached.
        com.feth.play.module.pa.controllers.Authenticate.noCache(response());

        Result res = null;
        // Create a form filled with the requested data.
        Form<PlayfulSignIn> form = PlayfulUsernamePasswordAuthProvider.SIGNIN_FORM.bindFromRequest();

        if (form.hasErrors()) {
            res = badRequest(signin.render(form));
        }
        else {
//            session().clear();
//            session("email", form.get().email);
            res = UsernamePasswordAuthProvider.handleLogin(ctx());
        }

        return res;
    }


//    // -- Sign-out / Log-out
//
//    /**
//     * <p>The sign-out action.</p>
//     *
//     * <p>
//     * Cleans up the session and adds a success message to the flash scope.
//     * </p>
//     *
//     * @return A {@code 303 SEE_OTHER} HTTP {@link Result} pointing to
//     * {@link Application.signIn()} action.
//     */
//    public static Result signOut() {
//        session().clear();
//        flash("success", "You've successfully signed out");
//
//        return redirect(routes.Application.signIn());
//    }


    // -- Profile


    @Restrict(@Group(Application.USER_ROLE))
    public static Result profile() {
        final User localUser = getCurrentUser(session());
        return ok(profile.render(localUser));
    }


//    // -- Evaluation

    /**
     * <p>The evaluation action.</p>
     *
     * <p>
     * Constructs a {@code 200 OK} HTTP response, containing
     * {@code app/views/evaluation.scala.html} as a body.
     * </p>
     *
     * <p>
     * Annotated with the {@link Secured} authenticator.
     * </p>
     *
     * @return A {@code 200 OK} HTTP {@link Result}.
     */
    @Restrict(@Group(Application.USER_ROLE))
    public static Result evaluation() {
        return ok(evaluation.render(form(Evaluation.class)));
    }

    /**
     * <p>The evaluation submission action.</p>
     *
     * @return A {@code 303 SEE_OTHER} HTTP {@link Result}.
     */
    public static Result doEvaluation() {
        Result res = null;
        Form<Evaluation> form = form(Evaluation.class).bindFromRequest();

        if (form.hasErrors()) {
            res = badRequest(evaluation.render(form));
        }
        else {
            Evaluation evaluation = form.get();
            evaluation.save();
            flash("success", "Thank you for your time. Your evaluation has been successfully saved.");
            res =  redirect(routes.Application.index());
        }

        return res;
    }

    /**
     * <p>The set language action.</p>
     *
     * @param code
     *     The language to be set.
     *
     * @return
     *     A {@code 303 SEE_OTHER} HTTP {@link Result}. Always redirects to
     *     the referer.
     */
    public static Result setLanguage(String code) {
        Result res = null;

        changeLang(code);

        String[] refererList = request().headers().get(HeaderNames.REFERER);
        String referer = (refererList.length != 0 ? refererList[0] : null);

        if (referer != null) {
            res = redirect(referer);
        }
        else {
            res = redirect(routes.Application.welcome());
        }

        return res;
    }

    public static String formatTimestamp(final long t) {
        return new SimpleDateFormat("yyyy-dd-MM HH:mm:ss").format(new Date(t));
    }

}
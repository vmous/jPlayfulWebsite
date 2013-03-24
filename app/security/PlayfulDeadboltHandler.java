package security;

import models.User;

import com.feth.play.module.pa.PlayAuthenticate;
import com.feth.play.module.pa.user.AuthUserIdentity;

import controllers.Application;

import play.mvc.Http.Context;
import play.mvc.Result;
import be.objectify.deadbolt.core.models.Subject;
import be.objectify.deadbolt.java.AbstractDeadboltHandler;
import be.objectify.deadbolt.java.DynamicResourceHandler;

public class PlayfulDeadboltHandler extends AbstractDeadboltHandler {

    @Override
    public Result beforeAuthCheck(Context context) {
        Result res = null;
        String strUrl = null;
        if (!PlayAuthenticate.isLoggedIn(context.session())) {
            // Calling this function will redirect your visitor to the page
            // whose requested led to the login page; else the user will be
            // redirected to the page defined by your resolver.
            strUrl = new String(PlayAuthenticate.storeOriginalUrl(context));
            context.flash().put(Application.FLASH_ERROR_KEY,
                    "You need to sign-in first in order to view '" +
                    strUrl + "'");

            res = redirect(PlayAuthenticate.getResolver().login());
        }

        return res;
    }


    @Override
    public DynamicResourceHandler getDynamicResourceHandler(Context context) {
        return null;
    }


    @Override
    public Subject getSubject(Context context) {
        AuthUserIdentity id = PlayAuthenticate.getUser(context);
        // Caching might be a good idea here.
        return User.findByAuthUserIdentity(id);
    }


    @Override
    public Result onAuthFailure(Context context, String content) {
        // if the user has a cookie with a valid user and the local user has
        // been deactivated/deleted in between, it is possible that this gets
        // shown. You might want to consider to sign the user out in this case.
        return forbidden("Forbidden");
    }

}

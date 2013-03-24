package service;

import models.User;
import play.Application;

import com.feth.play.module.pa.user.AuthUser;
import com.feth.play.module.pa.user.AuthUserIdentity;
import com.feth.play.module.pa.service.UserServicePlugin;

public class PlayfulUserServicePlugin extends UserServicePlugin {

    public PlayfulUserServicePlugin(Application app) {
        super(app);
    }

    @Override
    public Object save(AuthUser authUser) {
        Object obj = null;
        // Check if the user is linked
        boolean isLinked = User.existsByAuthUserIdentity(authUser);
        if (!isLinked) {
            obj = User.create(authUser).id;
        }

        return obj;
    }

    @Override
    public Object getLocalIdentity(AuthUserIdentity identity) {
        Object obj = null;
        // For production: Caching might be a good idea here...
        // ...and dont forget to sync the cache when users get deactivated/deleted
        User u = User.findByAuthUserIdentity(identity);
        if(u != null) {
            obj = u.id;
        }

        return obj;
    }

    @Override
    public AuthUser merge(AuthUser newUser, AuthUser oldUser) {
        if (!oldUser.equals(newUser)) {
            User.merge(oldUser, newUser);
        }
        return oldUser;
    }

    @Override
    public AuthUser link(AuthUser oldUser, AuthUser newUser) {
        User.addLinkedAccount(oldUser, newUser);
        return newUser;
    }

    @Override
    public AuthUser update(AuthUser knownUser) {
        // User logged in again, bump last login date
        User.setLastLoginDate(knownUser);
        return knownUser;
    }

}

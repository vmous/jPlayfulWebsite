package models;

import java.util.Arrays;
import java.util.Collections;
import java.util.Date;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.ManyToMany;
import javax.persistence.Table;

import com.avaje.ebean.Ebean;
import com.avaje.ebean.ExpressionList;
import com.feth.play.module.pa.providers.password.UsernamePasswordAuthUser;
import com.feth.play.module.pa.user.AuthUser;
import com.feth.play.module.pa.user.AuthUserIdentity;
import com.feth.play.module.pa.user.EmailIdentity;
import com.feth.play.module.pa.user.FirstLastNameIdentity;
import com.feth.play.module.pa.user.NameIdentity;

import be.objectify.deadbolt.core.models.Permission;
import be.objectify.deadbolt.core.models.Role;
import be.objectify.deadbolt.core.models.Subject;

import play.data.format.Formats.NonEmpty;
import play.data.validation.Constraints.Email;
import play.data.validation.Constraints.MaxLength;
import play.data.validation.Constraints.MinLength;
import play.data.validation.Constraints.Required;
import play.data.format.Formats;
import play.db.ebean.Model;

/**
 * Class representing the application user.
 *
 * @author billy
 */
@Entity
@Table(name = "users")
public class User extends Model implements Subject {

    /**
     * Default serial version id.
     */
    private static final long serialVersionUID = 1L;


    /**
     * The identification number of the user.
     */
    @Id
    public Long id;


    /**
     * The user's email.
     */
    @Email(message = "Please enter a valid e-mail address")
    @NonEmpty
    @Required(message = "Please enter your e-mail")
    // if you make this unique, keep in mind that users *must* merge/link their
    // accounts then on signup with additional providers
//    @Column(unique = true)
    public String email;


    /**
     * The user's name.
     */
    public String name;


    /**
     * The user's first name.
     */
    public String firstName;


    /**
     * The user's last name.
     */
    public String lastName;


    /**
     * The user's last login date and time.
     */
    @Formats.DateTime(pattern = "yyyy-MM-dd HH:mm:ss")
    public Date lastLogin;


    /**
     * Denotes whether the user is active or not.
     */
    public boolean active;


    /**
     * Denotes whether the user's email has been validated or not.
     */
    public boolean emailValidated;


    /**
     * A list of security roles assigned to the user.
     */
    @ManyToMany
    public List<SecurityRole> roles;


    /**
     * A list of linked accounts connected to the user.
     */
    @ManyToMany(cascade = CascadeType.ALL)
    public List<LinkedAccount> linkedAccounts;


    /**
     * A list of the user's permissions.
     */
    @ManyToMany
    public List<UserPermission> permissions;


    /* (non-Javadoc)
     * @see be.objectify.deadbolt.core.models.Subject#getIdentifier()
     */
    @Override
    public String getIdentifier() {
        return Long.toString(id);
    }


    /* (non-Javadoc)
     * @see be.objectify.deadbolt.core.models.Subject#getPermissions()
     */
    @Override
    public List<? extends Permission> getPermissions() {
        return permissions;
    }


    /* (non-Javadoc)
     * @see be.objectify.deadbolt.core.models.Subject#getRoles()
     */
    @Override
    public List<? extends Role> getRoles() {
        // TODO Auto-generated method stub
        return roles;
    }


    // -- Queries


    /**
     * A field to programatically make queries for the user.
     */
    public static Finder<Long, User> find = new Finder<Long, User>(Long.class, User.class);


    public static boolean existsByAuthUserIdentity(final AuthUserIdentity identity) {
        final ExpressionList<User> exp;

        if (identity instanceof UsernamePasswordAuthUser) {
            exp = getUsernamePasswordAuthUserFind((UsernamePasswordAuthUser) identity);
        }
        else {
            exp = getAuthUserFind(identity);
        }
        return exp.findRowCount() > 0;
    }


    public static User findByAuthUserIdentity(final AuthUserIdentity identity) {
        if (identity == null) {
            return null;
        }

        if (identity instanceof UsernamePasswordAuthUser) {
            return findByUsernamePasswordIdentity((UsernamePasswordAuthUser) identity);
        } else {
            return getAuthUserFind(identity).findUnique();
        }
    }


    public static User findByUsernamePasswordIdentity(final UsernamePasswordAuthUser identity) {
        return getUsernamePasswordAuthUserFind(identity).findUnique();
    }


    // -- Private


    private static ExpressionList<User> getUsernamePasswordAuthUserFind(final UsernamePasswordAuthUser identity) {
        return getEmailUserFind(identity.getEmail()).eq("linkedAccounts.providerKey", identity.getProvider());
    }


    /**
     * Finds the user that is active and matches given identity's provider user
     * id and provider key.
     *
     * @param identity
     *     The identity to be found.
     *
     * @return
     */
    private static ExpressionList<User> getAuthUserFind(final AuthUserIdentity identity) {
        return find.where()
                .eq("active", true)
                .eq("linkedAccounts.providerUserId", identity.getId())
                .eq("linkedAccounts.providerKey", identity.getProvider());
    }


    private static ExpressionList<User> getEmailUserFind(final String email) {
        return find.where().eq("active", true).eq("email", email);
    }


    public void merge(final User otherUser) {
        for (final LinkedAccount acc : otherUser.linkedAccounts) {
            this.linkedAccounts.add(LinkedAccount.create(acc));
        }
        // do all other merging stuff here - like resources, etc.

        // deactivate the merged user that got added to this one
        otherUser.active = false;
        Ebean.save(Arrays.asList(new User[] { otherUser, this }));
    }


    public static User create(final AuthUser authUser) {
        final User user = new User();
        user.roles = Collections.singletonList(SecurityRole.findByRoleName(controllers.Application.USER_ROLE));
        // user.permissions = new ArrayList<UserPermission>();
        // user.permissions.add(UserPermission.findByValue("printers.edit"));
        user.active = true;
        user.lastLogin = new Date();
        user.linkedAccounts = Collections.singletonList(LinkedAccount.create(authUser));

        if (authUser instanceof EmailIdentity) {
            final EmailIdentity identity = (EmailIdentity) authUser;
            // Remember, even when getting them from FB & Co., emails should be
            // verified within the application as a security breach there might
            // break your security as well!
            user.email = identity.getEmail();
            user.emailValidated = false;
        }

        if (authUser instanceof NameIdentity) {
            final NameIdentity identity = (NameIdentity) authUser;
            final String name = identity.getName();
            if (name != null) {
                user.name = name;
            }
        }

        if (authUser instanceof FirstLastNameIdentity) {
            final FirstLastNameIdentity identity = (FirstLastNameIdentity) authUser;
            final String firstName = identity.getFirstName();
            final String lastName = identity.getLastName();
            if (firstName != null) {
                user.firstName = firstName;
            }

            if (lastName != null) {
                user.lastName = lastName;
            }
        }

        user.save();
        user.saveManyToManyAssociations("roles");
        // user.saveManyToManyAssociations("permissions");
        return user;
    }


    public static void merge(final AuthUser oldUser, final AuthUser newUser) {
        User.findByAuthUserIdentity(oldUser).merge(User.findByAuthUserIdentity(newUser));
    }


    public Set<String> getProviders() {
        final Set<String> providerKeys = new HashSet<String>(
        linkedAccounts.size());
        for (final LinkedAccount acc : linkedAccounts) {
            providerKeys.add(acc.providerKey);
        }
        return providerKeys;
    }


    public static void addLinkedAccount(final AuthUser oldUser,
        final AuthUser newUser) {
        final User u = User.findByAuthUserIdentity(oldUser);
        u.linkedAccounts.add(LinkedAccount.create(newUser));
        u.save();
    }


    public static void setLastLoginDate(final AuthUser knownUser) {
        final User u = User.findByAuthUserIdentity(knownUser);
        u.lastLogin = new Date();
        u.save();
    }


    public static User findByEmail(final String email) {
        return getEmailUserFind(email).findUnique();
    }


    public LinkedAccount getAccountByProvider(final String providerKey) {
        return LinkedAccount.findByProviderKey(this, providerKey);
    }


    public static void verify(final User unverified) {
        // You might want to wrap this into a transaction
        unverified.emailValidated = true;
        unverified.save();
        TokenAction.deleteByUser(unverified, models.TokenAction.Type.EMAIL_VERIFICATION);
    }


    public void changePassword(final UsernamePasswordAuthUser authUser,
        final boolean create) {
        LinkedAccount a = this.getAccountByProvider(authUser.getProvider());
        if (a == null) {
            if (create) {
                a = LinkedAccount.create(authUser);
                a.user = this;
            }
            else {
                throw new RuntimeException("Account not enabled for password usage");
            }
        }
        a.providerUserId = authUser.getHashedPassword();
        a.save();
    }


    public void resetPassword(final UsernamePasswordAuthUser authUser,
        final boolean create) {
        // You might want to wrap this into a transaction
        this.changePassword(authUser, create);
        TokenAction.deleteByUser(this, models.TokenAction.Type.PASSWORD_RESET);
    }
}

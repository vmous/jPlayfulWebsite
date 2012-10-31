package models;

import javax.persistence.Entity;
import javax.persistence.Id;

import play.db.ebean.Model;

/**
 * <p>
 * Class representing the application user.
 * </p>
 *
 * <p>
 * <strong>Warning</strong>: Passwords are manipulated in clear text. This is,
 * obviously a very bad practice, but for the time being we leave it as it is.
 * </p>
 *
 * <p>
 * TODO: A better approach would be to hash the password before storing it,
 * and then hash it before using it.
 * </p>
 *
 * @author billy
 *
 */
@Entity
public class User extends Model {
    /**
     * The user's email.
     */
    @Id
    public String email;

    /**
     * The user's first name.
     */
    public String name;

    /**
     * The user's last name.
     */
    public String surname;

    /**
     * The user's password.
     */
    public String password;

    /**
     * A field to be user to programatically make queries.
     */
    public static Finder<String, User> find = new Finder<String, User>(String.class, User.class);

    /**
     * Constructor
     *
     * @param email     The email address of the user. This will serve as a
     *                  username and id for the the application
     * @param name      The name of the user.
     * @param surname   The surname of the user.
     * @param password  The password of the user.
     */
    public User(String email, String name, String surname, String password) {
        this.email = email;
        this.name = name;
        this.surname = surname;
        this.password = password;
    }

    /**
     * A helper method for user authentication.
     *
     * @param email     The email to be authenticated.
     * @param password  The password.
     * @return  A {@link User} object representing the corresponding user if
     *          the authentication was successful; {@code null} otherwise.
     */
    public static User authenticate(String email, String password) {
        return find.where().eq("email", email).eq("password", password).findUnique();
    }

}

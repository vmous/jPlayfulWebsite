package providers;

import providers.PlayfulUsernamePasswordAuthProvider.PlayfulSignUp;

import com.feth.play.module.pa.providers.password.UsernamePasswordAuthUser;
import com.feth.play.module.pa.user.NameIdentity;

public class PlayfulUsernamePasswordAuthUser extends UsernamePasswordAuthUser implements NameIdentity {

	private static final long serialVersionUID = 1L;

	private final String name;

	public PlayfulUsernamePasswordAuthUser(final PlayfulSignUp signup) {
		super(signup.password, signup.email);
		this.name = signup.name;
	}

	/**
	 * Used for password reset only - do not use this to sign up a user!
	 *
	 * @param password
	 *     ...
	 */
	public PlayfulUsernamePasswordAuthUser(final String password) {
		super(password, null);
		name = null;
	}

	@Override
	public String getName() {
		return name;
	}

}

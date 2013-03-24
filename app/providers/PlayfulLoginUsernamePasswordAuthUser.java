package providers;

import com.feth.play.module.pa.providers.password.DefaultUsernamePasswordAuthUser;

public class PlayfulLoginUsernamePasswordAuthUser extends DefaultUsernamePasswordAuthUser {

	private static final long serialVersionUID = 1L;

	/**
	 * The session timeout in seconds. It defaults to two weeks.
	 */
	final static long SESSION_TIMEOUT = 24 * 14 * 3600;

	private final long expiration;

	/**
	 * For logging the user in automatically
	 *
	 * @param email
	 */
	public PlayfulLoginUsernamePasswordAuthUser(String email) {
		this(null, email);
	}

	public PlayfulLoginUsernamePasswordAuthUser(String clearPassword, String email) {
		super(clearPassword, email);

		expiration = System.currentTimeMillis() + 1000 * SESSION_TIMEOUT;
	}

	@Override
	public long expires() {
		return expiration;
	}

}

package engine.util;
import java.util.MissingResourceException;
import java.util.ResourceBundle;

public class MessagesErros {
	private static final String BUNDLE_NAME = "messages_err"; //$NON-NLS-1$

	private static final ResourceBundle RESOURCE_BUNDLE = ResourceBundle
			.getBundle(BUNDLE_NAME);

	private MessagesErros() {
	}

	public static String getString(String key) {
		try {
			return RESOURCE_BUNDLE.getString(key);
		} catch (MissingResourceException e) {
			return '!' + key + '!';
		}
	}
}

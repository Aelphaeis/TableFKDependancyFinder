package database.utilities;

import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.util.Objects;

public class Reflector {
	
	public static Class<?> loadClass(String className) {
		try {
			return Class.forName(className);
		}
		catch (ClassNotFoundException e) {
			String err = "Unexpected exception loading class [%s]";
			throw new ReflectorException(String.format(err, className), e);
		}
	}
	
	public static <T> T load(String name, IOFunction<T> f) {
		ClassLoader loader = ClassLoader.getSystemClassLoader();
		URL url = loader.getResource(Objects.requireNonNull(name));
		if (url == null) {
			String fmt = "[%s] is not a resource";
			throw new IllegalArgumentException(String.format(fmt, name));
		}
		
		try (InputStream stream = url.openStream()) {
			return f.apply(stream);
		}
		catch (IOException e) {
			String fmt = "error processing stream [%s] at [%s]";
			String err = String.format(fmt, name, url);
			throw new IllegalArgumentException(err, e);
		}
	}
	
	@FunctionalInterface
	public interface IOFunction<T> {
		
		T apply(InputStream t) throws IOException;
	}
	
	public static class ReflectorException extends RuntimeException {
		
		private static final long serialVersionUID = 909384213793458361L;
		
		public ReflectorException(String message, Throwable cause) {
			super(message, cause);
		}
	}
	
	private Reflector() {}
}

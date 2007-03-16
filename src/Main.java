import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.List;
import java.util.Properties;


public class Main {
	
	public static void main(String[] args) {
		InputStream in = loadPropertiesFile();

		Properties props = loadProperties(in);
		
		MainClassLoader loader = initializeClassLoader(props);
		
		Class clazz = loadMainClass(props, loader);
		
		Method method = findMainMethod(props, clazz);
		
		callMainMethod(args, method);		
	}

	private static void callMainMethod(String[] args, Method method) {
		try {
			method.invoke(null, new Object[] { args } );
		} catch (IllegalArgumentException e) {
			e.printStackTrace();
			System.exit(0);
		} catch (IllegalAccessException e) {
			e.printStackTrace();
			System.exit(0);
		} catch (InvocationTargetException e) {
			e.printStackTrace();
			System.exit(0);
		}
	}

	private static Method findMainMethod(Properties props, Class clazz) {
		String mainMethod = props.getProperty("method");
		
		if (mainMethod == null) {
			System.out.println("main class property not found");
			System.exit(0);
		}
		
		Method method = null;
		try {
			method = clazz.getDeclaredMethod(mainMethod, new Class[] { String[].class });
		} catch (SecurityException e) {
			e.printStackTrace();
			System.exit(0);
		} catch (NoSuchMethodException e) {
			e.printStackTrace();
			System.exit(0);
		}
		return method;
	}

	private static Class loadMainClass(Properties props, MainClassLoader loader) {
		String mainClass = props.getProperty("class");
		
		if (mainClass == null) {
			System.out.println("main class property not found");
			System.exit(0);
		}
		
		Class clazz = null;
		try {
			clazz = loader.loadClass(mainClass);
		} catch (ClassNotFoundException e) {
			e.printStackTrace();
			System.exit(0);
		}
		return clazz;
	}

	private static MainClassLoader initializeClassLoader(Properties props) {
		String classpath = props.getProperty("classpath");
		
		if (classpath == null) {
			System.out.println("classpath property not found");
			System.exit(0);
		}
		
		String basedir = System.getProperty("user.dir");
		
		List<File> files = new ArrayList<File>();
		String[] classpathArray = classpath.split(";");
		for (int i = 0; i < classpathArray.length; i++) {
			files.add(new File(basedir + classpathArray[i]));
		}
		
		File[] fileArray = new File[files.size()];
		files.toArray(fileArray);
		MainClassLoader loader = new MainClassLoader(fileArray);
		return loader;
	}

	private static Properties loadProperties(InputStream in) {
		Properties props = new Properties();
		try {
			props.load(in);
		} catch (IOException e) {
			e.printStackTrace();
			System.exit(0);
		}
		return props;
	}

	private static InputStream loadPropertiesFile() {
		InputStream in = Main.class.getResourceAsStream("/boot.properties");
		
		if (in == null) {
			System.out.println("boot properties not found");
			System.exit(0);
		}
		return in;
	}

}

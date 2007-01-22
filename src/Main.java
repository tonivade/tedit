import java.io.File;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;


public class Main {
	
	public static void main(String[] args) {
		
		String baseDir = System.getProperty("user.dir");
		
		File[] files = new File[] { new File(baseDir + "/main"), new File(baseDir + "./lib") };
		
		MainClassLoader loader = new MainClassLoader(files);
		
		Class clazz = null;
		try {
			clazz = loader.loadClass("tk.tomby.tedit.Main");
		} catch (ClassNotFoundException e) {
			e.printStackTrace();
			System.exit(0);
		}
		
		Method method = null;
		try {
			method = clazz.getDeclaredMethod("main", new Class[] { String[].class });
		} catch (SecurityException e) {
			e.printStackTrace();
			System.exit(0);
		} catch (NoSuchMethodException e) {
			e.printStackTrace();
			System.exit(0);
		}
		
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

}

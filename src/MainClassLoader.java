

import java.io.File;
import java.io.FilenameFilter;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLClassLoader;
import java.net.URLStreamHandlerFactory;
import java.util.ArrayList;
import java.util.List;

public class MainClassLoader extends URLClassLoader {

	public MainClassLoader(File[] libs) {
		super(getClasspath(libs));
	}

	public MainClassLoader(URL[] urls, ClassLoader parent) {
		super(urls, parent);
		// TODO Auto-generated constructor stub
	}
	
	public MainClassLoader(URL[] urls, ClassLoader parent, URLStreamHandlerFactory factory) {
		super(urls, parent, factory);
		// TODO Auto-generated constructor stub
	}
	
	private static URL[] getClasspath(File[] libs) {
		List<URL> urls = new ArrayList<URL>();
		for (int i = 0; i < libs.length; i++) {
			File file = libs[i];
			if (file.isDirectory()) {
				addDirectoryURL(urls, file);
			} else {
				addFileURL(urls, file);
			}
		}
		
		System.out.println("classpath: " + urls);
		
		URL[] array = new URL[urls.size()];
		urls.toArray(array);
		return array;
	}
	
	private static void addDirectoryURL(List<URL> urls, File file) {
		if (file != null) {
			File[] files = file.listFiles(new FilenameFilter() {
				public boolean accept(File dir, String name) {
					return name.endsWith(".jar");
				}
			});
			
			if (files.length == 0) {
				// el directorio está vacio
				addFileURL(urls, file);
			} else {
				for (int i = 0; i < files.length; i++) {
					addFileURL(urls, files[i]);
				}
			}
		}
	}

	private static void addFileURL(List<URL> urls, File file) {
		if (file != null) {
			try {
				urls.add(new URL("file:" + file.getAbsolutePath()));
			} catch (MalformedURLException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
	}

}

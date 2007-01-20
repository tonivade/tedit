package tk.tomby.tedit.plugins;

import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLClassLoader;
import java.net.URLStreamHandlerFactory;
import java.util.ArrayList;
import java.util.List;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import tk.tomby.tedit.plugins.IPluginDescriptor.IPluginLibrary;

public class PluginClassLoader extends URLClassLoader {
	
	private static final Log log = LogFactory.getLog(PluginClassLoader.class);

	public PluginClassLoader(IPluginDescriptor descriptor) {
		super(getClasspath(descriptor));
	}

	public PluginClassLoader(IPluginDescriptor descriptor, ClassLoader parent) {
		super(getClasspath(descriptor), parent);
	}
	
	public PluginClassLoader(IPluginDescriptor descriptor, ClassLoader parent, URLStreamHandlerFactory factory) {
		super(getClasspath(descriptor), parent, factory);
	}
	
	private static URL[] getClasspath(IPluginDescriptor descriptor) {
		List<URL> list = new ArrayList<URL>();
		
		addURL(list, "", descriptor.getPluginPath());
		
		for (IPluginLibrary lib : descriptor.getLibraries()) {
			addURL(list, PluginLoader.getBaseDir() + "/", lib.getPath());
		}
		
		if (log.isDebugEnabled()) {
			log.debug("classpath: " + list);
		}
		
		URL[] urls = new URL[list.size()];
		list.toArray(urls);
		return urls;
	}
	
	private static void addURL(List<URL> list, String base, String path) {
		try {
			if (path != null) {
				URL url = new URL("file://" + base + path);
				list.add(url);
			}
		} catch (MalformedURLException e) {
			log.warn("invalid url", e);
		}
	}

}

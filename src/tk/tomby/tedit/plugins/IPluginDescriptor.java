package tk.tomby.tedit.plugins;

import java.util.Collection;

/**
 * Plugin information needed for initialization
 * 
 * @author tomby
 */
public interface IPluginDescriptor {
	
	public static final String PLUGIN_WORKSPACE_TYPE = "workspace";
	
	public static final String PLUGIN_STATUS_TYPE = "status";
	
	public static final String PLUGIN_LAF_TYPE = "laf";
	
	/**
	 * 
	 * @return
	 */
	public abstract String getPluginName();
	
	/**
	 * 
	 * @return
	 */
	public abstract String getPluginType();
	
	/**
	 * 
	 * @return
	 */
	public abstract String getPluginClass();
	
	/**
	 * 
	 * @return
	 */
	public abstract String getPluginPath();
	
	/**
	 * 
	 * @param path
	 */
	public abstract void setPluginPath(String path);
	
	/**
	 * 
	 * @return
	 */
	public abstract Collection<IPluginLibrary> getLibraries();
	
	/**
	 * 
	 * @param name
	 * @return
	 */
	public abstract IPluginLibrary getLibrary(String name);
	
	/**
	 * 
	 * @return
	 */
	public abstract String getPreferences();
	
	/**
	 * 
	 * @return
	 */
	public abstract String getResources();
	
	/**
	 * 
	 * @author tomby
	 */
	public interface IPluginLibrary {
		
		/**
		 * 
		 * @return
		 */
		public abstract String getName();
		
		/**
		 * 
		 * @return
		 */
		public abstract String getPath();
		
	}

}

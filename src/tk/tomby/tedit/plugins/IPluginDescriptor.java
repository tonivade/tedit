package tk.tomby.tedit.plugins;

import java.util.Collection;

public interface IPluginDescriptor {
	
	public abstract String getPluginName();
	
	public abstract String getPluginType();
	
	public abstract String getPluginClass();
	
	public abstract String getPluginPath();
	
	public abstract void setPluginPath(String path);
	
	public abstract Collection<IPluginLibrary> getLibraries();
	
	public interface IPluginLibrary {
		
		public abstract String getName();
		
		public abstract String getPath();
		
	}

}

package studentlottery.standard.pluginloading;

import java.io.File;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLClassLoader;
import java.util.ArrayList;

import com.deb.lib.program.ProgramFs;

import studentlottery.plugin.IPlugin;


public class PluginLoader {
	
	File pluginFolder;
	ArrayList<IPlugin> plugins = new ArrayList<IPlugin>();
	boolean hasUnverified = false;
	URLClassLoader cl;
	
	public PluginLoader(File folder) {
		if (folder.isDirectory())	this.pluginFolder = folder;
		else this.pluginFolder = ProgramFs.getProgramFile("Plugins");
		this.pluginFolder.mkdirs();
		try {
			this.cl = new URLClassLoader(new URL[]{pluginFolder.toURI().toURL()});
		} catch (MalformedURLException e) {
			e.printStackTrace();
		}
		this.update();
	}
	
	public void update() {
		this.plugins = new ArrayList<IPlugin>();
		this.hasUnverified = false;
		
		File[] all = this.pluginFolder.listFiles();
		
		for(int i = 0; i < all.length; i++) {
			if(all[i].isDirectory()) {
				File pluginF = all[i];
				if(!this.isVerified(pluginF)) this.hasUnverified = true;
				this.loadPluginClasses(pluginF);//NOTE: check for duplicates
			}
		}
	}
	
	public boolean isVerified(File pluginF) {
		File sHash = new File(pluginF.getAbsolutePath()+"/ehash");
		
		if(sHash.isFile()) {
			//NOTE: add checks
			
		}
		
		return false;
	}
	
	void loadPluginClasses(File pluginF) {
		File[] pFiles = pluginF.listFiles();
		
		for(int i = 0; i < pFiles.length; i++) {
			try {
				if(pFiles[i].isDirectory()) this.loadPluginClasses(pFiles[i]);
				Class c = cl.loadClass(pluginF.getName()+"."+this.removeDotClass(pFiles[i].getName()));
				if (IPlugin.class.isAssignableFrom(c)) {
					plugins.add((IPlugin) c.newInstance());
				}
			} catch (Exception e) {
			}
		}
	}
	
	String removeDotClass(String s) {
		if(s.endsWith(".class")) return s.substring(0, s.length()-6);
		return s;
	}
	
	public ArrayList<IPlugin> getPlugins() {
		return this.plugins;
	}
}

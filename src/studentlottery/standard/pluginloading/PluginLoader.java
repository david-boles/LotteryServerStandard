package studentlottery.standard.pluginloading;

import java.io.DataInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.util.ArrayList;

import com.deb.lib.program.ProgramFs;

import studentlottery.plugin.IPlugin;


public class PluginLoader extends ClassLoader {
	
	File pluginFolder;
	ArrayList<IPlugin> plugins = new ArrayList<IPlugin>();
	boolean hasUnverified = false;
	
	public PluginLoader(File folder) {
		if (folder.isDirectory())	this.pluginFolder = folder;
		else this.pluginFolder = ProgramFs.getProgramFile("Plugins");
		this.pluginFolder.mkdirs();
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
				this.loadPlugin(pluginF);
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
	
	void loadPlugin(File pluginF) {
		
	}
	
	void Class loadClass(File newClass) {
		String classname = newClass.getName();
		
		try {
	        Class c = findLoadedClass(classname);

	        // After this method loads a class, it will be called again to
	        // load the superclasses. Since these may be system classes, we've
	        // got to be able to load those too. So try to load the class as
	        // a system class (i.e. from the CLASSPATH) and ignore any errors
	        if (c == null) {
	          try { c = findSystemClass(classname); }
	          catch (Exception ex) {}
	        }

	        // If the class wasn't found by either of the above attempts, then
	        // try to load it from a file in (or beneath) the directory
	        // specified when this ClassLoader object was created. Form the
	        // filename by replacing all dots in the class name with
	        // (platform-independent) file separators and by adding the ".class" extension.
	        if (c == null) {

	          // Get the length of the class file, allocate an array of bytes for
	          // it, and read it in all at once.
	          int length = (int) newClass.length();
	          byte[] classbytes = new byte[length];
	          DataInputStream in = new DataInputStream(new FileInputStream(newClass));
	          in.readFully(classbytes);
	          in.close();

	          // Now call an inherited method to convert those bytes into a Class
	          c = defineClass(classname, classbytes, 0, length);
	        }

	        // And we're done. Return the Class object we've loaded.
	        return c;
	      }
	      // If anything goes wrong, throw a ClassNotFoundException error
	      catch (Exception ex) { throw new ClassNotFoundException(ex.toString()); }
	    }
	}
}

package studentlottery.standard.start;

import com.deb.lib.program.ProgramFs;

import studentlottery.standard.pluginloading.PluginLoader;

public class Test {

	public static void main(String[] args) {
		PluginLoader pl = new PluginLoader(ProgramFs.getProgramFile("Plugins/"));
		System.out.println(pl.getPlugins());
		pl.getPlugins().get(0).eventOccured(null, null, null);

	}

}

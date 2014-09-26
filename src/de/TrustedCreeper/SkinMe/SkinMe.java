package de.TrustedCreeper.SkinMe;

import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.URL;
import java.util.HashMap;

import net.citizensnpcs.api.npc.NPC;

import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.plugin.java.JavaPlugin;
import org.mcstats.metrics.Metrics;

public class SkinMe extends JavaPlugin {
	@Override
	public void onEnable() {
		registerEvents();
		loadConfig();
		SkinMeCommands command = new SkinMeCommands(this);
		getCommand("skinme").setExecutor(command);
		
		if(getInstance().getConfig().getBoolean("config.use_metrics")) {
			try {
			    Metrics metrics = new Metrics(this);
			    metrics.start();
			    Bukkit.getConsoleSender().sendMessage(getPrefix() + "§2Plugin Metrics enabled!");
			} catch (IOException e) {
				Bukkit.getConsoleSender().sendMessage(getPrefix() + "§cError: Cant enable Plugin Metrics!");
				e.printStackTrace();
			}
		} else {
			Bukkit.getConsoleSender().sendMessage(getPrefix() + "§cPlugin Metrics are disabled!");
		}
		
		try {
			if(getServer().getPluginManager().getPlugin("Citizens").getName().equals("Citizens")) {
				existCitizens = true;
			} else {
				Bukkit.getConsoleSender().sendMessage(getPrefix() + "§cDisabled preview feature, because Citizens isnt installed!");
			}
		} catch (Exception e) {
			Bukkit.getConsoleSender().sendMessage(getPrefix() + "§cDisabled preview feature, because Citizens isnt installed!");
		}
		
	}
	
	@Override
	public void onDisable() {
		if(!npcs.isEmpty()) {
			for(NPC npc : npcs.values()) {
				npc.destroy();
			}
		}
	}
	
	
	public void onReload() {
		if(!npcs.isEmpty()) {
			for(NPC npc : npcs.values()) {
				npc.destroy();
			}
		}
	}
	
	private SkinMeEventHandler events;
	public void registerEvents() {
		this.events = new SkinMeEventHandler(this);
	}
	
	private final HashMap<String, NPC> npcs = new HashMap<String, NPC>();
	
	public void addNPC(Player p, NPC npc) {
		npcs.put(p.getName(), npc);
	}
	
	public void removeNPC(Player p) {
		if(npcs.containsKey(p.getName())) {
			npcs.get(p.getName()).destroy();
			npcs.remove(p.getName());
		}
	}
	
	public boolean hasNPC(Player p) {
		return npcs.containsKey(p.getName());
	}
	
	File config = new File("plugins/SkinMe/config.yml");
	public void loadConfig() {
		//if(!config.exists()) {
			getInstance().getConfig().addDefault("config.allow_sneak_rightclick", true);
			getInstance().getConfig().addDefault("config.allow_shortlink", true);
			getInstance().getConfig().addDefault("config.use_metrics", true);
			getInstance().getConfig().addDefault("config.show_info", true);
			getInstance().getConfig().addDefault("config.preview_length_in_seconds", 10);
			for(Messages message : Messages.values())
				getInstance().getConfig().addDefault(message.getConfigPath(), message.getDefault());
			getInstance().getConfig().options().copyDefaults(true);
			getInstance().saveConfig();
		//}
	}
	
	public String getPrefix() {
		return Messages.PREFIX.getString() + " ";
	}
	
	public static SkinMe getInstance() {
		return (SkinMe) Bukkit.getPluginManager().getPlugin("SkinMe");
	}
	
	public boolean existAccount(String player) {
		try {
			BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(new URL("https://www.minecraft.net/haspaid.jsp?user=" + player).openStream()));
			String line = bufferedReader.readLine();
			if(line.contains("true")) return true;
		} catch (Exception e) {e.printStackTrace();}
		return false;
	}
	
	private boolean existCitizens = false;
	
	public boolean isCitizensEnabled() { return existCitizens; }
	 
	public String createShortlink(String url) {
		try {
			BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(new URL("http://tinyurl.com/api-create.php?url=" + url).openStream()));
			String line = bufferedReader.readLine();
			return line;
		} catch (Exception e) {e.printStackTrace();}
		return null;
	}
	
	public String createURLFromSkinURL(String url) {
		String returnURL = "https://www.minecraft.net/skin/remote.jsp?url=" + url;
		if(getInstance().getConfig().getBoolean("config.allow_shortlink")) returnURL = createShortlink(returnURL);
		return returnURL;
	}
	
	public String getURL(String player) {
		String url = "https://www.minecraft.net/skin/remote.jsp?url=http://s3.amazonaws.com/MinecraftSkins/char.png";
		if(existAccount(player)) url = "https://www.minecraft.net/skin/remote.jsp?url=http://s3.amazonaws.com/MinecraftSkins/" + player + ".png";
		if(getInstance().getConfig().getBoolean("config.allow_shortlink")) url = createShortlink(url);
		return url;
	}
}




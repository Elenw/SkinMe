package de.TrustedCreeper.SkinMe;

import org.bukkit.ChatColor;

public enum Messages {
	PREFIX("messages.prefix", "&6[&5Skin&cMe&6]"),
	NO_PERMISSION("messages.no_permission", "&cYou dont have permission!"),
	ACCOUNT_DOESNT_EXIST("messages.account_doesnt_exist", "&cThis player does not have a valid account! Cracked perhaps?"),
	CHANGE_SKIN("messages.change_skin", "&2To get %player%'s skin open this url: &6%link%"),
	CHANGE_SKIN_FROM_URL("messages.change_skin_from_url", "&2To change your skin open this url: &6%link%"),
	USAGE("messages.usage", "&cUsage: /skinme [view] <playername/url>"),
	CITIZENS_DOESNT_EXIST("messages.citizens", "&cThis feature is disabled because Citizens plugin is missing!"),
	ALREADY_VIEWING("messages.already_viewing", "&cYou already view a skin!"),
	CONFIG_RELOADED("messages.config_reloaded", "&2Config has been reloaded!"),
	IS_NPC("messages.is_npc", "&cThis is a NPC not a player!"),
	PNG_NOT_FOUND("messages.png_not_found", "&cYour skin file has to be a PNG picture!");
	
	String configpath;
	String defaultmessage;
	
	
	Messages(String configpath, String defaultmessage) {
		this.configpath = configpath;
		this.defaultmessage = defaultmessage;
	}
	
	public String getConfigPath() {
		return configpath;
	}
	
	public String getDefault() {
		return defaultmessage;
	}
	
	public void set(String setTo) {
		SkinMe.getInstance().getConfig().set(getConfigPath(), ChatColor.stripColor(setTo));
		SkinMe.getInstance().saveConfig();
	}
	
	public String getString() {
		return ChatColor.translateAlternateColorCodes('&', SkinMe.getInstance().getConfig().getString(getConfigPath()));
	}
	
	public String getChangeSkinMessage(String player, String link) {
		String message = Messages.CHANGE_SKIN.getString();
		if(message.contains("%link%")) message = message.replace("%link%", link);
		if(message.contains("%player%")) message = message.replace("%player%", player);
		return message;
	}
	
	public String getChangeSkinFromURLMessage(String link) {
		String message = Messages.CHANGE_SKIN_FROM_URL.getString();
		if(message.contains("%link%")) message = message.replace("%link%", link);
		return message;
	}
}

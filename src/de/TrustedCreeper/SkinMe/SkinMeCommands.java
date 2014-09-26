package de.TrustedCreeper.SkinMe;

import net.citizensnpcs.api.CitizensAPI;
import net.citizensnpcs.api.npc.NPC;

import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Player;

public class SkinMeCommands implements CommandExecutor {

	private final SkinMe plugin;

	public SkinMeCommands(SkinMe plugin) {
		this.plugin = plugin;
	}


	@Override
	public boolean onCommand(CommandSender sender, Command command,
			String label, String[] args) {
		if(command.getName().equalsIgnoreCase("skinme")) {
			if(!(sender instanceof Player)) return false;
			final Player p = (Player) sender;
			if(args.length == 0) {
				if(plugin.getConfig().getBoolean("config.show_info")) {
					if(p.hasPermission("skinme.info")) {
						p.sendMessage(SkinMe.getInstance().getPrefix() + "§2Plugin name: §aSkinMe");
						p.sendMessage(SkinMe.getInstance().getPrefix() + "§2Developed by: §aTrustedCreeper");
						p.sendMessage(SkinMe.getInstance().getPrefix() + "§2Version: §a1.3.1");
						p.sendMessage(SkinMe.getInstance().getPrefix() + "§2Download: §ahttp://TrustedCreeper.de/?u=SkinMe");
						p.sendMessage(SkinMe.getInstance().getPrefix() + "§2Usage: §a/skinme [view] <Player/URL>");
					} else {
						p.sendMessage(SkinMe.getInstance().getPrefix() + Messages.NO_PERMISSION.getString());
					}
				}
				return true;
			}
			
				if(args.length == 1) {
					if(args[0].equalsIgnoreCase("reload") || args[0].equalsIgnoreCase("rl")) {
						if(p.hasPermission("skinme.reload")) {
							plugin.reloadConfig();
							p.sendMessage(SkinMe.getInstance().getPrefix() + Messages.CONFIG_RELOADED.getString());
						} else {
							p.sendMessage(SkinMe.getInstance().getPrefix() + Messages.NO_PERMISSION.getString());
						}
					} else {
						if(p.hasPermission("skinme.command")) {
							//Change Skin via URL
							if(args[0].toLowerCase().startsWith("http://") || args[0].toLowerCase().startsWith("https://")) {
								if(args[0].toLowerCase().endsWith(".png")) {
									p.sendMessage(SkinMe.getInstance().getPrefix() + Messages.CHANGE_SKIN_FROM_URL.getChangeSkinFromURLMessage(SkinMe.getInstance().createURLFromSkinURL(args[0])));
								} else {
									p.sendMessage(SkinMe.getInstance().getPrefix() + Messages.PNG_NOT_FOUND.getString());
								}
								return true;
							}
							
							//Change Skin via Playername
							if(SkinMe.getInstance().existAccount(args[0])) {
								p.sendMessage(SkinMe.getInstance().getPrefix() + Messages.CHANGE_SKIN.getChangeSkinMessage(args[0], SkinMe.getInstance().getURL(args[0])));
							} else {
								p.sendMessage(SkinMe.getInstance().getPrefix() + Messages.ACCOUNT_DOESNT_EXIST.getString());
							}
						} else {
							p.sendMessage(SkinMe.getInstance().getPrefix() + Messages.NO_PERMISSION.getString());
						}
					}
				} else if(args.length == 2) {
					
					if(args[0].equalsIgnoreCase("view") || args[0].equalsIgnoreCase("preview") || args[0].equalsIgnoreCase("show")) {
						if(p.hasPermission("skinme.view")) {
							if(!plugin.isCitizensEnabled()) {
								p.sendMessage(SkinMe.getInstance().getPrefix() + Messages.CITIZENS_DOESNT_EXIST.getString());
								return true;
							}
							if(plugin.hasNPC(p)) {
								p.sendMessage(SkinMe.getInstance().getPrefix() + Messages.ALREADY_VIEWING.getString());
								return true;
							}
							if(SkinMe.getInstance().existAccount(args[1])) {
								int seconds = plugin.getConfig().getInt("config.preview_length_in_seconds");
								
								final NPC npc = CitizensAPI.getNPCRegistry().createNPC(EntityType.PLAYER, args[1]);
								plugin.addNPC(p, npc);
								npc.spawn(p.getLocation());
								npc.faceLocation(p.getLocation());
								npc.setProtected(true);
								Bukkit.getScheduler().scheduleSyncDelayedTask(plugin, new Runnable() {
									@Override
									public void run() {
										plugin.removeNPC(p);
									}
								}, seconds * 20);
							} else {
								p.sendMessage(SkinMe.getInstance().getPrefix() + Messages.ACCOUNT_DOESNT_EXIST.getString());
							}
							return true;
						} else {
							p.sendMessage(SkinMe.getInstance().getPrefix() + Messages.NO_PERMISSION.getString());
						}
						
						
					} else {
						p.sendMessage(SkinMe.getInstance().getPrefix() + Messages.USAGE.getString());
					}
				} else {
					p.sendMessage(SkinMe.getInstance().getPrefix() + Messages.USAGE.getString());
				}
			
			
			return true;
		}
		return false;
	}
}
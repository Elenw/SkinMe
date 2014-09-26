package de.TrustedCreeper.SkinMe;

import org.bukkit.ChatColor;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerInteractEntityEvent;


public class SkinMeEventHandler implements Listener {

	
	private final SkinMe plugin;

	public SkinMeEventHandler(SkinMe plugin) {
		this.plugin = plugin;
		plugin.getServer().getPluginManager().registerEvents(this, plugin);
	}
	
	@EventHandler
	public void onPlayerClickPlayer(PlayerInteractEntityEvent e) {
		if(e.getPlayer().equals(null)) return;
		if(e.getRightClicked().equals(null)) return;
		if(e.getRightClicked() instanceof Player) {
			Player clicked = (Player) e.getRightClicked();
			Player p = e.getPlayer();
			if(p.isSneaking()) {
				if(SkinMe.getInstance().getConfig().getBoolean("config.allow_sneak_rightclick")) {
					if(p.hasPermission("skinme.event")) {
						if(e.getRightClicked().hasMetadata("NPC")) {
							p.sendMessage(SkinMe.getInstance().getPrefix() + Messages.IS_NPC.getString());
							return;
						}
						String name = ChatColor.stripColor(clicked.getName());
						if(SkinMe.getInstance().existAccount(name)) {
							p.sendMessage(SkinMe.getInstance().getPrefix() + Messages.CHANGE_SKIN.getChangeSkinMessage(name, SkinMe.getInstance().getURL(name)));
						} else {
							p.sendMessage(SkinMe.getInstance().getPrefix() + Messages.ACCOUNT_DOESNT_EXIST.getString());
						}
					} else {
						p.sendMessage(SkinMe.getInstance().getPrefix() + Messages.NO_PERMISSION.getString());
					}
				}
			}
		}
	}
}

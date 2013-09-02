package com.github.lyokofirelyte.Administratum;

import java.util.List;
import java.util.logging.Level;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerCommandPreprocessEvent;

public class ChatChannels implements Listener{

	AdministratumMain plugin;
	public ChatChannels(AdministratumMain plugin){
		this.plugin = plugin;
	}
	
	
	
	@EventHandler(priority = EventPriority.HIGH)
	public void onCommandStuffThingsDinosaur(PlayerCommandPreprocessEvent event) {
	
	if (plugin.config.getBoolean("useChatChannels")){
		
	
	 List <String> channels = plugin.channels.getStringList("AliasList");
	 String arr[] = event.getMessage().split(" ", 2);
	 String firstWord = arr[0];
	 
	 	for (String waffles : channels){
	 		
	 		if (event.getMessage().startsWith("/" + waffles) && firstWord.equalsIgnoreCase("/" + waffles)){
	 			event.setCancelled(true);
	 			plugin.datacore.set("users." + event.getPlayer().getName() + ".aliasCheck", firstWord);
	 			channelPrompt(event.getPlayer().getName(), arr[1]);
	 		}
	 	}
	}
}
	
	public void channelPrompt(String tod, String message){
		
		String alias = plugin.datacore.getString("users." + tod + ".aliasCheck");
		String channel = plugin.channels.getString("Aliases." + alias);
		List <String> users = plugin.channels.getStringList("Channels." + channel + ".Users");  
		String format = plugin.channels.getString("Channels." + channel + ".Format");
		String prefix = plugin.channels.getString("users." + tod + ".Prefix");
		String suffix = plugin.channels.getString("users." + tod + ".Suffix");
		String menuHeader = plugin.styles.getString("Themes.Headers.Menus");
		
			if (!users.contains(tod)){
				Bukkit.getPlayer(tod).sendMessage(AS(menuHeader + "You are not a part of that channel!"));
				return;
			}
			
		if (prefix == null){
			plugin.channels.set("users." + tod + ".Prefix", "&7Prefix");
		}
		
		if (suffix == null){
			plugin.channels.set("users." + tod + ".Suffix", "&7Suffix");
		}
		
		if (plugin.config.getBoolean("channelConsoleLog")){
			plugin.getLogger().log(Level.INFO, tod + " issued server command /" + alias + " " + message);
		}
			for (String badTouch : users){		
				if (Bukkit.getOfflinePlayer(badTouch).isOnline()){
				Bukkit.getPlayer(badTouch).sendMessage(AS((format).replaceAll("%suffix", suffix).replaceAll("%prefix", prefix).replaceAll("%player", Bukkit.getPlayer
				(tod).getDisplayName()).replaceAll("%name", Bukkit.getPlayer(tod).getName()).replaceAll("%channel", channel).replaceAll("%message", message)));
			}
	}			}
	

	public String AS(String DecorativeToasterCozy){
		
		String FlutterShysShed = ChatColor.translateAlternateColorCodes('&', DecorativeToasterCozy);
		return FlutterShysShed;
		
	}

}

package com.github.lyokofirelyte.Administratum;

import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.event.Listener;

public class ChatChannels implements CommandExecutor, Listener{

	AdministratumMain plugin;
	public ChatChannels(AdministratumMain plugin){
		this.plugin = plugin;
	}
	
	
	
	
	
	
	
	@Override 
	public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {
		
		String menuHeader = plugin.styles.getString("Themes.Headers.Menus");
	
		switch(args.length){
		
		case 0: 

			sender.sendMessage(AS(menuHeader + "I don't understand. Please see /a help chat."));
			break;
			
		
		
		
		
		
		
		}
		
		
		
		
		
		return true;
	}
	
	
	public String AS(String DecorativeToasterCozy){
		
		String FlutterShysShed = ChatColor.translateAlternateColorCodes('&', DecorativeToasterCozy);
		return FlutterShysShed;
		
	}

}

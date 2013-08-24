package com.github.lyokofirelyte.Administratum;

import java.util.List;
import java.util.Random;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerKickEvent;
import org.bukkit.event.player.PlayerLoginEvent;
import org.bukkit.event.player.PlayerQuitEvent;


public class JoinListen implements Listener {
	
	AdministratumMain plugin;
	public JoinListen(AdministratumMain plugin){
		this.plugin = plugin;

	}
	

	




	@EventHandler(priority = EventPriority.NORMAL)
	public void onPlayerLogin(PlayerLoginEvent event) {
		Player p = event.getPlayer();
		List <String> registered = plugin.datacore.getStringList("RegisteredPlayers");
		
		if (!registered.contains(event.getPlayer().getName())){
			
			registered.add(event.getPlayer().getName());
			plugin.datacore.set("RegisteredPlayers", registered);
			
		}
		
		if (plugin.datacore.getBoolean("Registered." + p.getName()) == false){
			plugin.datacore.set("Registered." + p.getName(), true);
		}
		
		plugin.saveYamls();
	}
	
	
	
	@EventHandler(priority = EventPriority.NORMAL)
	public void onPlayerJoin(final PlayerJoinEvent event) {
		Player p = event.getPlayer();
	if (plugin.datacore.getBoolean("Banned." + p.getName()) == true) {
		String reason = plugin.datacore.getString("BanReason." + p.getName());
		p.kickPlayer(reason);
		
	}		
	
	if (plugin.config.getBoolean("useJoinMessages")){
		
	event.setJoinMessage(null);
	
	List <String> joinList = plugin.styles.getStringList("Themes.Events.LoginMessages");
	Random rand = new Random();
	int randomNumber = rand.nextInt(joinList.size());
	final String message = joinList.get(randomNumber);
	Bukkit.getServer().getScheduler().scheduleSyncDelayedTask(plugin,
			new Runnable(){ public void run(){
				Bukkit.broadcastMessage(ChatColor.translateAlternateColorCodes('&', message).replaceAll("%player", event.getPlayer().getDisplayName()));
			}}, 5);
	
	
}
	
	}
	
	
	@EventHandler(priority = EventPriority.NORMAL)
	public void onPlayerDepart(final PlayerQuitEvent event) {
		
	if (plugin.datacore.getBoolean("users." + event.getPlayer().getName() + ".frozen")){
		  Bukkit.getServer().getScheduler().cancelTask(plugin.datacore.getInt("users." + event.getPlayer().getName() + ".Task"));
			plugin.datacore.set("users." + event.getPlayer().getName() + ".HasTask", false);
	}
	
	if (plugin.config.getBoolean("useQuitMessages")){
		
	event.setQuitMessage(null);
	
	List <String> quitList = plugin.styles.getStringList("Themes.Events.QuitMessages");
	Random rand = new Random();
	int randomNumber = rand.nextInt(quitList.size());
	final String message = quitList.get(randomNumber);
	Bukkit.getServer().getScheduler().scheduleSyncDelayedTask(plugin,
			new Runnable(){ public void run(){
				Bukkit.broadcastMessage(ChatColor.translateAlternateColorCodes('&', message).replaceAll("%player", event.getPlayer().getDisplayName()));
			}}, 5);
	
	
}
	
	}
	
	@EventHandler(priority = EventPriority.NORMAL)
	public void onPlayerKick(final PlayerKickEvent event) {
	
	if (plugin.config.getBoolean("useQuitMessages")){
		
	event.setLeaveMessage(null);
	
	}
	
	}
	
}
package com.github.lyokofirelyte.Administratum;




import java.util.List;

import com.github.lyokofirelyte.Administratum.AdministratumMain;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Effect;
import org.bukkit.Location;
import org.bukkit.World;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.event.block.BlockPlaceEvent;
import org.bukkit.event.player.AsyncPlayerChatEvent;
import org.bukkit.event.player.PlayerMoveEvent;
import org.bukkit.scheduler.BukkitRunnable;





public class CommandEx implements CommandExecutor, Listener {

AdministratumMain plugin;
public String italic = ChatColor.ITALIC + "";
public String aprefix = ChatColor.RED + "Administratum " + ChatColor.DARK_RED + "// " + ChatColor.RED;
public String aprefix2 = ChatColor.RED + "Administratum " + ChatColor.WHITE + "// " + ChatColor.RED;
int taskID;

public CommandEx(AdministratumMain plugin){
	this.plugin = plugin;

}











@EventHandler(priority = EventPriority.MONITOR)
public void onPlayerMove(PlayerMoveEvent event) {

if (plugin.datacore.getBoolean("users." + event.getPlayer().getName() + ".frozen") == true){
	
    Location from = event.getFrom();
    double xfrom = event.getFrom().getX();
    double yfrom = event.getFrom().getY();
    double zfrom = event.getFrom().getZ();
    double xto = event.getTo().getX();
    double yto = event.getTo().getY();
    double zto = event.getTo().getZ();
    if (!(xfrom == xto && yfrom == yto && zfrom == zto)) {
    event.getPlayer().teleport(from);
    }
}


if (plugin.datacore.getBoolean("events.evac") == true && plugin.config.getBoolean("evacExempt." + event.getPlayer().getName()) == false){
	
    Location from = event.getFrom();
    double xfrom = event.getFrom().getX();
    double yfrom = event.getFrom().getY();
    double zfrom = event.getFrom().getZ();
    double xto = event.getTo().getX();
    double yto = event.getTo().getY();
    double zto = event.getTo().getZ();
    if (!(xfrom == xto && yfrom == yto && zfrom == zto)) {
    event.getPlayer().teleport(from);
    }
}

}




@EventHandler(priority = EventPriority.HIGH)
public void onBlockBreak(BlockBreakEvent event) {


	
if (plugin.datacore.getBoolean("users." + event.getPlayer().getName() + ".restricted") == true) {
	
	event.setCancelled(true);
	event.getPlayer().sendMessage(aprefix2 + "You were restricted from breaking blocks!");
	
}

if (plugin.datacore.getBoolean("events.evac") == true && plugin.config.getBoolean("evacExempt." + event.getPlayer().getName()) == false){
	
	event.setCancelled(true);	
}
}


@EventHandler(priority = EventPriority.HIGH)
public void onBlockPlace(BlockPlaceEvent event) {


	
if (plugin.datacore.getBoolean("users." + event.getPlayer().getName() + ".restricted") == true) {
	
	event.setCancelled(true);
	event.getPlayer().sendMessage(aprefix2 + "You were restricted from placing blocks!");
	
}

if (plugin.datacore.getBoolean("events.evac") == true && plugin.config.getBoolean("evacExempt." + event.getPlayer().getName()) == false){
	
	event.setCancelled(true);	
}

}







@EventHandler(priority = EventPriority.NORMAL)
public void onCommandPreprocess(AsyncPlayerChatEvent event) { 
	
	
String automaticFormat = plugin.styles.getString("Themes.Warnings.Automatic");
String automaticActionFormat = plugin.styles.getString("Themes.Warnings.AutomaticAction");

	
	if (event.isCancelled() == true){
		
		return;
	}
	
    Player p = event.getPlayer();
    String sentence = event.getMessage();
	List<String> filters = plugin.config.getStringList("filters.filter");
	List<String> noWarn = plugin.config.getStringList("filters.noWarn");
	String filtered = plugin.config.getString("filters.defaultReplace");
	Player player = event.getPlayer();
	Boolean muted = plugin.datacore.getBoolean("users." + player.getName() + ".muted");
	
	
	if (muted == true){
		
		event.setCancelled(true);
		player.sendMessage(aprefix2 + "You are currently silenced.");
		return;
		
	}
	


	if (plugin.datacore.getBoolean("events.evac") == true && plugin.config.getBoolean("evacExempt." + event.getPlayer().getName()) == false){
		
		event.setCancelled(true);
		player.sendMessage(aprefix2 + "A global freeze is in place. Please stay calm while the issue is investigated.");
		return;
	}

	
	for(String word2 : noWarn){
		
        if (event.getMessage().toLowerCase().contains(word2) && plugin.config.getBoolean("filters.usingfilters") == true && plugin.config.getBoolean("exempt." + p.getName()) == false)
        { 
        	
        	  String sentenceEdit = event.getMessage().toLowerCase();
         	  String oldWord = word2.toLowerCase();
        	  
        	  if(plugin.config.getString("filters.customFilters." + word2) == null){
        		  
        		  String newWord = filtered;

            	  String newSentence = replaceWordsInSentence(sentenceEdit, oldWord, newWord);
            	  

            		
                  event.setMessage(newSentence);
                  return;
                  
        	  } else {
        		  
        		  String newWord = plugin.config.getString("filters.customFilters." + word2);

            	  String newSentence = replaceWordsInSentence(sentenceEdit, oldWord, newWord);
            	  

          		
                  event.setMessage(newSentence);
                  return;
        	  }	
        
		
	}
	}

	for(String word : filters){
              if (event.getMessage().toLowerCase().contains(word) && plugin.config.getBoolean("filters.usingfilters") == true && plugin.config.getBoolean("exempt." + p.getName()) == false)
              { 
            	  String oldWord = word.toLowerCase();
            	  String sentenceEdit = event.getMessage().toLowerCase();
            	  
            	  if(plugin.config.getString("filters.customFilters." + word) == null){
            		  
            		  String newWord = filtered;

                	  String newSentence = replaceWordsInSentence(sentenceEdit, oldWord, newWord);
                	  

                	  
                      event.setMessage(newSentence);
                      
            	  } else {
            		  
            		  String newWord = plugin.config.getString("filters.customFilters." + word);

                	  String newSentence = replaceWordsInSentence(sentenceEdit, oldWord, newWord);
                	  
                	  
                      event.setMessage(newSentence);
            		  
            	  }
            		    
            	  
            	        
                      if (plugin.config.getBoolean("automatic_warnings") == true){
                    	  

                      Integer automatics = plugin.datacore.getInt("users." + p.getName() + ".Warnings.Automatics.Total");
                      Integer automaticsUpdated = automatics + 1;
                      Integer overall = plugin.datacore.getInt("users." + p.getName() + ".Warnings.Overall");
                      Integer overallUpdated = overall + 1;

                      
                      if (plugin.config.getString("warning_levels." + automaticsUpdated) == null){
                    	  
                          String action = plugin.config.getString("warning_levels.fallback");
                      
                          plugin.datacore.set("users." + p.getName() + ".Warnings.Overall", overallUpdated);
                    	  plugin.datacore.set("users." + p.getName() + ".Warnings.Automatics.Total", automaticsUpdated);
                    	  plugin.datacore.set("users." + p.getName() + ".Warnings.Automatics.Reason" + automaticsUpdated, sentence);
                    	  plugin.datacore.set("users." + p.getName() + ".Warnings.Automatics.Auth" + automaticsUpdated, "system");
                    	  
                      
                      
                      
                      
                    	  
                          if (action.equalsIgnoreCase("warning")) {
                        	  
                        	  plugin.datacore.set("users." + p.getName() + ".Warnings.Automatics.Action" + automaticsUpdated, "warning");
                        	  
                          }
                          
                          if (action.equalsIgnoreCase("mute")) {
                        	  
                        	  plugin.datacore.set("users." + p.getName() + ".Warnings.Automatics.Action" + automaticsUpdated, "mute");
                        	  plugin.datacore.set("users." + p.getName() + ".muted", true);
                        	  
                          }
                          
                          if (action.equalsIgnoreCase("freeze")) {
                        	  
                        	  plugin.datacore.set("users." + p.getName() + ".Warnings.Automatics.Action" + automaticsUpdated, "freeze");
                        	  plugin.datacore.set("users." + p.getName() + ".frozen", true);
                        	  
                          }
                          
                          if (action.equalsIgnoreCase("restrict")) {
                        	  
                        	  plugin.datacore.set("users." + p.getName() + ".Warnings.Automatics.Action" + automaticsUpdated, "restriction");
                        	  plugin.datacore.set("users." + p.getName() + ".restricted", true);
                        	  
                          }
                          
                          if (action.equalsIgnoreCase("ban")) {
                        	  
                        	  plugin.datacore.set("users." + p.getName() + ".Warnings.Automatics.Action" + automaticsUpdated, "ban");
                        	  plugin.datacore.set("Banned." + p.getName(), true);
                        	  plugin.datacore.set("BanReason." + p.getName(), "Max warning limit reached!");
                        	  
                          }
                          
                          if (action.equalsIgnoreCase("kick")) {
                        	  
                        	  plugin.datacore.set("users." + p.getName() + ".Warnings.Automatics.Action" + automaticsUpdated, "kick");
                      	      String reason = ChatColor.GOLD + italic + "Swearing";
                    	 	  event.getPlayer().kickPlayer(reason + ChatColor.WHITE + " (" + ChatColor.RED + "System" + ChatColor.WHITE + ")");
                        	  
                          }
                          
                   
                          String automaticsUpdatedString = "" + automaticsUpdated;
                          Bukkit.broadcastMessage(ChatColor.translateAlternateColorCodes('&', automaticFormat).replaceAll("%name", event.getPlayer().getName()).replaceAll("%player", event.getPlayer().getDisplayName()).replaceAll("%warning", automaticsUpdatedString));
                          Bukkit.broadcastMessage(ChatColor.translateAlternateColorCodes('&', automaticActionFormat.replaceAll("%action", action)));
                                 return;
                         
                      
                      } 
                      
                      
                      String action = plugin.config.getString("warning_levels." + automaticsUpdated);
                	  plugin.datacore.set("users." + p.getName() + ".Warnings.Automatics.Total", automaticsUpdated);
                	  plugin.datacore.set("users." + p.getName() + ".Warnings.Automatics.Reason" + automaticsUpdated, sentence);
                	  plugin.datacore.set("users." + p.getName() + ".Warnings.Automatics.Auth" + automaticsUpdated, "system");
                	  plugin.datacore.set("users." + p.getName() + ".Warnings.Overall", overallUpdated);
                  
                  
                  
                  
                	  
                      if (action.equalsIgnoreCase("warning")) {
                    	  
                    	  plugin.datacore.set("users." + p.getName() + ".Warnings.Automatics.Action" + automaticsUpdated, "warning");
                    	  
                      }
                      
                      if (action.equalsIgnoreCase("mute")) {
                    	  
                    	  plugin.datacore.set("users." + p.getName() + ".Warnings.Automatics.Action" + automaticsUpdated, "mute");
                    	  plugin.datacore.set("users." + p.getName() + ".muted", true);
                    	  
                      }
                      
                      if (action.equalsIgnoreCase("ban")) {
                    	  
                    	  plugin.datacore.set("users." + p.getName() + ".Warnings.Automatics.Action" + automaticsUpdated, "ban");
                    	  plugin.datacore.set("Banned." + p.getName(), true);
                    	  plugin.datacore.set("BanReason." + p.getName(), "Max warning limit reached!");
                    	  
                      }
                      
                      if (action.equalsIgnoreCase("kick")) {
                    	  
                    	  plugin.datacore.set("users." + p.getName() + ".Warnings.Automatics.Action" + automaticsUpdated, "kick");
                  	      String reason = ChatColor.GOLD + italic + "Swearing";
                	 	  event.getPlayer().kickPlayer(reason + ChatColor.WHITE + " (" + ChatColor.RED + "System" + ChatColor.WHITE + ")");
                    	  
                      }
                      
                      if (action.equalsIgnoreCase("freeze")) {
                    	  
                    	  plugin.datacore.set("users." + p.getName() + ".Warnings.Automatics.Action" + automaticsUpdated, "freeze");
                    	  plugin.datacore.set("users." + p.getName() + ".frozen", true);
                    	  
                      }
                      
                      if (action.equalsIgnoreCase("restrict")) {
                    	  
                    	  plugin.datacore.set("users." + p.getName() + ".Warnings.Automatics.Action" + automaticsUpdated, "restriction");
                    	  plugin.datacore.set("users." + p.getName() + ".restricted", true);
                    	  
                      }
                      
              String automaticsUpdatedString = "" + automaticsUpdated;
              Bukkit.broadcastMessage(ChatColor.translateAlternateColorCodes('&', automaticFormat).replaceAll("%name", event.getPlayer().getName()).replaceAll("%player", event.getPlayer().getDisplayName()).replaceAll("%warning", automaticsUpdatedString)); 
              Bukkit.broadcastMessage(ChatColor.translateAlternateColorCodes('&', automaticActionFormat.replaceAll("%action", action)));
                     return;
                     
                      }

                      Integer automatics = plugin.datacore.getInt("users." + p.getName() + ".Warnings.Automatics.Total");
                      Integer automaticsUpdated = automatics + 1;
                      Integer overall = plugin.datacore.getInt("users." + p.getName() + ".Warnings.Overall");
                      Integer overallUpdated = overall + 1;
                      
                        	  
                        	  plugin.datacore.set("users." + p.getName() + ".Warnings.Automatics.Total", automaticsUpdated);
                        	  plugin.datacore.set("users." + p.getName() + ".Warnings.Automatics.Reason" + automaticsUpdated, sentence);
                        	  plugin.datacore.set("users." + p.getName() + ".Warnings.Automatics.Auth" + automaticsUpdated, "system");
                        	  plugin.datacore.set("users." + p.getName() + ".Warnings.Overall", overallUpdated);
                        	  
                        	  
                      return;
                   
                    	  
                      }
                      
                      
              }
	
                      
	if (plugin.config.getBoolean("filters.denyCaps") == true){
		
		if (isAllUpperCase(sentence) == true){
			
		String newSentence2 = sentence.toLowerCase();
		event.setMessage(newSentence2);
		return;
			
		}
		
	    }  
            	
            		  
            	  }
            	  
              
	
	



public static boolean isAllUpperCase(String cs) {
            if (cs == null) {
                return false;
            }
            int sz = cs.length();
            for (int i = 0; i < sz; i++) {
                if (Character.isUpperCase(cs.charAt(i)) == false) {
                    return false;
                }
            }
           return true;
        }










private String replaceWordsInSentence(String sentence, String oldWord, String newWord) {  
    return sentence.replaceAll("\\b" + oldWord + "\\b", newWord);  
}  

@SuppressWarnings("unused")
@Override

public boolean onCommand(final CommandSender sender, Command cmd, String label, final String[] args) {
	
String kickDisplay = plugin.styles.getString("Themes.Warnings.Kick");
String generalDisplay = plugin.styles.getString("Themes.Warnings.General");
String muteDisplay = plugin.styles.getString("Themes.Warnings.Mute");
String unmuteDisplay = plugin.styles.getString("Themes.Warnings.UnMute");
String banDisplay = plugin.styles.getString("Themes.Warnings.Ban");
String unbanDisplay = plugin.styles.getString("Themes.Warnings.UnBan");
String freezeDisplay = plugin.styles.getString("Themes.Warnings.Freeze");
String unfreezeDisplay = plugin.styles.getString("Themes.Warnings.UnFreeze");
String restrictDisplay = plugin.styles.getString("Themes.Warnings.Restrict");
String unrestrictDisplay = plugin.styles.getString("Themes.Warnings.UnRestrict");
String evacOnDisplay = plugin.styles.getString("Themes.Events.EvacOn");
String evacOffDisplay = plugin.styles.getString("Themes.Events.EvacOff");
String logoffDisplay = plugin.styles.getString("Themes.Events.Logoff");
String menuHeader = plugin.styles.getString("Themes.Headers.Menus");
	


	if(cmd.getName().equalsIgnoreCase("evac") || cmd.getName().equalsIgnoreCase("aevac")) {
		
		if (plugin.datacore.getBoolean("events.evac") == true){
			
			plugin.datacore.set("events.evac", false);
			
			if (sender instanceof Player){
			Player p = (Player) sender;
			Bukkit.broadcastMessage(ChatColor.translateAlternateColorCodes('&', evacOffDisplay).replaceAll("%player", p.getDisplayName()).replaceAll("%name", p.getName())); 
			return true;
			}
			
			Bukkit.broadcastMessage(ChatColor.translateAlternateColorCodes('&', evacOffDisplay).replaceAll("%player", "console"));
			return true;
			
		}
		
		plugin.datacore.set("events.evac", true);
		
		if (sender instanceof Player){
		Player p = (Player) sender;
		Bukkit.broadcastMessage(ChatColor.translateAlternateColorCodes('&', evacOnDisplay).replaceAll("%player", p.getDisplayName()).replaceAll("%name", p.getName())); 
		return true;
		}
		
		Bukkit.broadcastMessage(ChatColor.translateAlternateColorCodes('&', evacOnDisplay).replaceAll("%player", "console"));
		return true;
		
	}

	    if(cmd.getName().equalsIgnoreCase("logoff") || cmd.getName().equalsIgnoreCase("alogoff")) {
	    	
	    	if (sender instanceof Player == false){
	    		
	    		sender.sendMessage(ChatColor.translateAlternateColorCodes('&', menuHeader) + "What the hell are you trying to do?");
	    		return true;
	    		
	    	}
	    	
	    	Player v = (Player) sender;
	        v.kickPlayer(ChatColor.translateAlternateColorCodes('&', logoffDisplay));
	    }
	
  if(cmd.getName().equalsIgnoreCase("aunfreeze") || cmd.getName().equalsIgnoreCase("unfreeze") ) {
	  
	  if (sender instanceof Player){
		  

		  
		  if (args.length != 1){
			  
			  sender.sendMessage(ChatColor.translateAlternateColorCodes('&', menuHeader) + "Correct usage is /unfreeze <player>");
			  return true;
		  }
		  
		  Player p = Bukkit.getPlayer(args[0]);
		  Player s = (Player) sender;
		  
		  if (p == null){
			  
			  sender.sendMessage(ChatColor.translateAlternateColorCodes('&', menuHeader) + "That player is not online.");
			  return true;
			  
		  }
		  
		  
		  Boolean frozen = plugin.datacore.getBoolean("users." + args[0] + ".frozen");
		  
		  
		  if (frozen == false){
			  
			  sender.sendMessage(ChatColor.translateAlternateColorCodes('&', menuHeader) + "That player is not frozen!");
			  return true;
		  }
		  
		  	if (plugin.config.getBoolean("freezeEffect")){
		  		
		  		Bukkit.getServer().getScheduler().cancelTask(plugin.datacore.getInt("users." + Bukkit.getPlayer(args[0]).getName() + ".Task"));
				plugin.datacore.set("users." + Bukkit.getPlayer(args[0]).getName() + ".HasTask", false);
		  		
		  	}
		  
		  plugin.datacore.set("users." + args[0] + ".frozen", false);
		  Bukkit.broadcastMessage(ChatColor.translateAlternateColorCodes('&', unfreezeDisplay).replaceAll("%auth", s.getDisplayName()).replaceAll("%afull", s.getName()).replaceAll("%player", p.getDisplayName()).replaceAll("%name", p.getName()));
		  return true;
	  }
	  
	  Player p = Bukkit.getPlayer(args[0]);
	  
	  if (args.length != 1){
		  
		  sender.sendMessage(ChatColor.translateAlternateColorCodes('&', menuHeader) + "Correct usage is /unfreeze <player>");
		  return true;
	  }
	  
	  if (p == null){
		  
		  sender.sendMessage(ChatColor.translateAlternateColorCodes('&', menuHeader) + "That player is not online.");
		  return true;
		  
	  }
	  
	  
	  Boolean frozen = plugin.datacore.getBoolean("users." + args[0] + ".frozen");
	  
	  if (frozen == false){
		  
		  sender.sendMessage(ChatColor.translateAlternateColorCodes('&', menuHeader) + "That player is not frozen!");
		  return true;
	  }
	  
	  	if (plugin.config.getBoolean("freezeEffect")){
	  		
	  		Bukkit.getServer().getScheduler().cancelTask(plugin.datacore.getInt("users." + Bukkit.getPlayer(args[0]).getName() + ".Task"));
			plugin.datacore.set("users." + Bukkit.getPlayer(args[0]).getName() + ".HasTask", false);
	  		
	  	}
	  	
	  plugin.datacore.set("users." + args[0] + ".frozen", false);
	  Bukkit.broadcastMessage(ChatColor.translateAlternateColorCodes('&', unfreezeDisplay).replaceAll("%auth", "console").replaceAll("%player", p.getDisplayName()));
	  return true;
	  
	  
	  
  }

	if(cmd.getName().equalsIgnoreCase("afreeze") || cmd.getName().equalsIgnoreCase("freeze") ) {
		
		
		if (sender instanceof Player){
			

		
		
		if (args.length < 2){
			
			sender.sendMessage(ChatColor.translateAlternateColorCodes('&', menuHeader) + "Correct usage is /freeze <player> <reason>");
			return true;
		}
		
		
		Player p = (Player) sender;
		Player p2 = Bukkit.getPlayer(args[0]);
		String reason = ChatColor.GOLD + italic + createString(args, 1);
		
		if (p2 == null){
			
			sender.sendMessage(ChatColor.translateAlternateColorCodes('&', menuHeader) + "That player is not online! You must really hate them.");
			return true;
			
		}
		
		int overall_warnings = plugin.datacore.getInt("users." + args[0] + "." + "Warnings.Overall");
		int freeze_warnings = plugin.datacore.getInt("users." + args[0] + "." + "Warnings.Freezes.Total");
		
		int overall_warnings2 = (overall_warnings + 1);
		int freeze_warnings2 = (freeze_warnings + 1);
		
		plugin.datacore.set("users." + p2.getName() + ".frozen", true);
		plugin.datacore.set("users." + p2.getName() + "." + "Warnings.Overall", overall_warnings2);
		plugin.datacore.set("users." + p2.getName() + "." + "Warnings.Freezes.Total", freeze_warnings2);
		plugin.datacore.set("users." + p2.getName() + "." + "Warnings.Freezes.Reason" + freeze_warnings2, reason);
		plugin.datacore.set("users." + p2.getName() + "." + "Warnings.Freezes.Auth" + freeze_warnings2, p.getDisplayName());
		
			if (plugin.config.getBoolean("freezeEffect")){
				
				
				taskID = Bukkit.getServer().getScheduler().scheduleSyncRepeatingTask(this.plugin, new BukkitRunnable() {
					
					public void run() {
						
						World world = ((Player) Bukkit.getPlayer(args[0])).getWorld();
						Location loc = Bukkit.getPlayer(args[0]).getLocation();
						Location l = new Location(loc.getWorld(), loc.getX(), loc.getY(), loc.getZ());
						Location l2 = new Location(loc.getWorld(), loc.getX(), loc.getY()+1, loc.getZ());
						world.playEffect(l, Effect.SMOKE, 0); 
						world.playEffect(l2, Effect.SMOKE, 0);
						world.playEffect(l2, Effect.ENDER_SIGNAL, 0);
						int thisTask = getTaskID();
							if (plugin.datacore.getBoolean("users." + Bukkit.getPlayer(args[0]).getName() + ".HasTask") == false){
								plugin.datacore.set("users." + Bukkit.getPlayer(args[0]).getName() + ".Task", thisTask);
								plugin.datacore.set("users." + Bukkit.getPlayer(args[0]).getName() + ".HasTask", true);
							}
					    }
					}, 0L, 5L);
				
			}
		Bukkit.broadcastMessage(ChatColor.translateAlternateColorCodes('&', freezeDisplay).replaceAll("%auth", p.getDisplayName()).replaceAll("%player", p2.getDisplayName()).replaceAll("%reason", reason).replaceAll("%name", p2.getName()));
        return true;
		}
		
		
		
		Player p2 = Bukkit.getPlayer(args[0]);
		String reason = ChatColor.GOLD + italic + createString(args, 1);
		
		
		if (args.length < 2){
			
			sender.sendMessage(ChatColor.translateAlternateColorCodes('&', menuHeader) + "Correct usage is /freeze <player> <reason>");
			return true;
		}
		
		if (p2 == null){
			
			sender.sendMessage(ChatColor.translateAlternateColorCodes('&', menuHeader) + "That player is not online! You must really hate them.");
			return true;
			
		}
		
		int overall_warnings = plugin.datacore.getInt("users." + args[0] + "." + "Warnings.Overall");
		int freeze_warnings = plugin.datacore.getInt("users." + args[0] + "." + "Warnings.Freezes.Total");
		
		int overall_warnings2 = (overall_warnings + 1);
		int freeze_warnings2 = (freeze_warnings + 1);
		
		plugin.datacore.set("users." + p2.getName() + ".frozen", true);
		plugin.datacore.set("users." + p2.getName() + "." + "Warnings.Overall", overall_warnings2);
		plugin.datacore.set("users." + p2.getName() + "." + "Warnings.Freezes.Total", freeze_warnings2);
		plugin.datacore.set("users." + p2.getName() + "." + "Warnings.Freezes.Reason" + freeze_warnings2, reason);
		plugin.datacore.set("users." + p2.getName() + "." + "Warnings.Freezes.Auth" + freeze_warnings2, "console");
		
		if (plugin.config.getBoolean("freezeEffect")){
			
			
			taskID = Bukkit.getServer().getScheduler().scheduleSyncRepeatingTask(this.plugin, new BukkitRunnable() {
				
				public void run() {
					
					World world = ((Player) Bukkit.getPlayer(args[0])).getWorld();
					Location loc = Bukkit.getPlayer(args[0]).getLocation();
					Location l = new Location(loc.getWorld(), loc.getX(), loc.getY(), loc.getZ());
					Location l2 = new Location(loc.getWorld(), loc.getX(), loc.getY()+1, loc.getZ());
					Location l3 = new Location(loc.getWorld(), loc.getX(), loc.getY()-1, loc.getZ());
					world.playEffect(l, Effect.SMOKE, 0); 
					world.playEffect(l2, Effect.SMOKE, 0);
					world.playEffect(l2, Effect.ENDER_SIGNAL, 0);
					int thisTask = getTaskID();
						if (plugin.datacore.getBoolean("users." + Bukkit.getPlayer(args[0]).getName() + ".HasTask") == false){
							plugin.datacore.set("users." + Bukkit.getPlayer(args[0]).getName() + ".Task", thisTask);
							plugin.datacore.set("users." + Bukkit.getPlayer(args[0]).getName() + ".HasTask", true);
						}
				    }
				}, 0L, 5L);
			
		}
		
		Bukkit.broadcastMessage(ChatColor.translateAlternateColorCodes('&', freezeDisplay).replaceAll("%auth", "console").replaceAll("%player", p2.getDisplayName()).replaceAll("%reason", reason).replaceAll("%name", p2.getName()));
        return true;
	}
	
	if(cmd.getName().equalsIgnoreCase("restrict")) {
		

		
		if (args.length < 2){
			
			sender.sendMessage(ChatColor.translateAlternateColorCodes('&', menuHeader) + "Correct usage is /restrict <player> <reason>");
			return true;
			
		}
		
		if (Bukkit.getPlayer(args[0]) == null){
			
			sender.sendMessage(ChatColor.translateAlternateColorCodes('&', menuHeader) + "That player does not exist or is offline. You must really hate them...");
			return true;
			
		}
		
		if (plugin.datacore.getBoolean("users." + args[0] + ".restricted") == true){
			
			sender.sendMessage(ChatColor.translateAlternateColorCodes('&', menuHeader) + "That user is already restricted!");
			return true;
			
		}
		
            Player p = Bukkit.getPlayer(args[0]);
			plugin.datacore.set("users." + args[0] + ".restricted", true);
			String reason = ChatColor.GOLD + italic + createString(args, 1);
			
			int overall_warnings = plugin.datacore.getInt("users." + args[0] + "." + "Warnings.Overall");
			int restriction_warnings = plugin.datacore.getInt("users." + args[0] + "." + "Warnings.Restrictions.Total");
			
			int overall_warnings2 = (overall_warnings + 1);
			int restriction_warnings2 = (restriction_warnings + 1);

			plugin.datacore.set("users." + p.getName() + "." + "Warnings.Overall", overall_warnings2);
			plugin.datacore.set("users." + p.getName() + "." + "Warnings.Restrictions.Total", restriction_warnings2);
			plugin.datacore.set("users." + p.getName() + "." + "Warnings.Restrictions.Reason" + restriction_warnings2, reason);
			
			if (sender instanceof Player){
				
            Player p2 = (Player) sender;
			plugin.datacore.set("users." + p.getName() + "." + "Warnings.Restrictions.Auth" + restriction_warnings2, p2.getDisplayName());
			Bukkit.broadcastMessage(ChatColor.translateAlternateColorCodes('&', restrictDisplay).replaceAll("%auth", p2.getDisplayName()).replaceAll("%afull", p2.getName()).replaceAll("%player", p.getDisplayName()).replaceAll("%reason", reason).replaceAll("%name", p.getName()));
		    return true;
		    
			}
			Bukkit.broadcastMessage(ChatColor.translateAlternateColorCodes('&', restrictDisplay).replaceAll("%auth", "console").replaceAll("%player", p.getDisplayName()).replaceAll("%reason", reason).replaceAll("%name", p.getName()));
			 plugin.datacore.set("users." + p.getName() + "." + "Warnings.Restrictions.Auth" + restriction_warnings2, "console");
			}

	
	if(cmd.getName().equalsIgnoreCase("unrestrict")) {
		

		
		if (args.length != 1){
			
			sender.sendMessage(ChatColor.translateAlternateColorCodes('&', menuHeader) + "Correct usage is /unrestrict <player>");
			return true;
			
		}
		
		if (Bukkit.getPlayer(args[0]) == null){
			
			sender.sendMessage(ChatColor.translateAlternateColorCodes('&', menuHeader) + "That player does not exist or is offline. You must really hate them...");
			return true;
			
		}
		
		if (plugin.datacore.getBoolean("users." + args[0] + ".restricted") == false){
			
			sender.sendMessage(ChatColor.translateAlternateColorCodes('&', menuHeader) + "That user is not currently restricted!");
			return true;
			
		}
            Player p = Bukkit.getPlayer(args[0]);
			plugin.datacore.set("users." + args[0] + ".restricted", false);

			if (sender instanceof Player){
				
				
	            Player p2 = (Player) sender;
	            Bukkit.broadcastMessage(ChatColor.translateAlternateColorCodes('&', unrestrictDisplay).replaceAll("%auth", p2.getDisplayName()).replaceAll("%afull", p2.getName()).replaceAll("%player", p.getDisplayName()).replaceAll("%name", p.getName()));
			    return true;
				}
				

			Bukkit.broadcastMessage(ChatColor.translateAlternateColorCodes('&', unrestrictDisplay).replaceAll("%auth", "console").replaceAll("%player", p.getDisplayName()).replaceAll("%name", p.getName()));

				}
	
	
	if(cmd.getName().equalsIgnoreCase("aban")) {
		
		if (sender instanceof Player) {
			
			if (args.length < 2){
			
				sender.sendMessage(ChatColor.translateAlternateColorCodes('&', menuHeader) + "Correct usage: /aban <player> <reason>");
				return true;
			}
				
				
			Player p = (Player) sender;
			Player banned = Bukkit.getPlayer(args[0]);
			
			if (plugin.datacore.getBoolean("Registered." + args[0]) == false){
				
				sender.sendMessage(ChatColor.translateAlternateColorCodes('&', menuHeader) + "That player has never logged in before!");
				return true;
				
			}
			
		    plugin.datacore.set("Banned." + args[0], true);
		   
		    String reason = ChatColor.GOLD + italic + createString(args, 1);
		    
			int overall_warnings = plugin.datacore.getInt("users." + args[0] + "." + "Warnings.Overall");
			int ban_warnings = plugin.datacore.getInt("users." + args[0] + "." + "Warnings.Bans.Total");
			
			int overall_warnings2 = (overall_warnings + 1);
			int ban_warnings2 = (ban_warnings + 1);
			plugin.datacore.set("users." + args[0] + "." + "Warnings.Overall", overall_warnings2);
			plugin.datacore.set("users." + args[0] + "." + "Warnings.Bans.Total", ban_warnings2);
			plugin.datacore.set("users." + args[0] + "." + "Warnings.Bans.Reason" + ban_warnings2, reason);
			plugin.datacore.set("BanReason." + args[0], reason);
			plugin.datacore.set("users." + args[0] + "." + "Warnings.Bans.Auth" + ban_warnings2, p.getDisplayName());
		    banned.kickPlayer(ChatColor.RED + "Banned by " + p.getDisplayName() + ChatColor.WHITE + " (" + reason + ChatColor.WHITE + ")");
		    Bukkit.broadcastMessage(ChatColor.translateAlternateColorCodes('&', banDisplay).replaceAll("%auth", p.getDisplayName()).replaceAll("%afull", p.getName()).replaceAll("%player", banned.getDisplayName()).replaceAll("%reason", reason).replaceAll("%name", banned.getName()));
		
		    
		} else {
			
			if (args.length < 2){
				
				sender.sendMessage(ChatColor.translateAlternateColorCodes('&', menuHeader) + "Correct usage: /ban <player> <reason>");
				return true;
			}
				
				
			Player p = (Player) sender;
			Player banned = Bukkit.getPlayer(args[0]);
			
			if (plugin.datacore.getBoolean("Registered." + args[0]) == false){
				
				sender.sendMessage(ChatColor.translateAlternateColorCodes('&', menuHeader) + "That player has never logged in before!");
				return true;
				
			}
			
		    plugin.datacore.set("Banned." + args[0], true);
		   
		    String reason = ChatColor.GOLD + italic + createString(args, 1);
		    
			int overall_warnings = plugin.datacore.getInt("users." + args[0] + "." + "Warnings.Overall");
			int ban_warnings = plugin.datacore.getInt("users." + args[0] + "." + "Warnings.Bans.Total");
			
			int overall_warnings2 = (overall_warnings + 1);
			int ban_warnings2 = (ban_warnings + 1);
			plugin.datacore.set("users." + args[0] + "." + "Warnings.Overall", overall_warnings2);
			plugin.datacore.set("users." + args[0] + "." + "Warnings.Bans.Total", ban_warnings2);
			plugin.datacore.set("users." + args[0] + "." + "Warnings.Bans.Reason" + ban_warnings2, reason);
			plugin.datacore.set("BanReason." + args[0], reason);
			plugin.datacore.set("users." + args[0] + "." + "Warnings.Bans.Auth" + ban_warnings2, p.getDisplayName());
		    banned.kickPlayer(ChatColor.RED + "Banned by console" + ChatColor.WHITE + " (" + reason + ChatColor.WHITE + ")");
		    Bukkit.broadcastMessage(ChatColor.translateAlternateColorCodes('&', banDisplay).replaceAll("%auth", "console").replaceAll("%player", p.getDisplayName()).replaceAll("%reason", reason).replaceAll("%name", p.getName()));

			
		}
	}
	
	
	
if(cmd.getName().equalsIgnoreCase("amute") || cmd.getName().equalsIgnoreCase("mute")) {
	
	if (sender instanceof Player) {
		
		if (args.length < 2){
		
			sender.sendMessage(ChatColor.translateAlternateColorCodes('&', menuHeader) + "Correct usage: /mute <player> <reason>");
			return true;
		}
			
			
		Player p = (Player) sender;
		Player muted = Bukkit.getPlayer(args[0]);
		
		if (muted == null) {
			
			sender.sendMessage(ChatColor.translateAlternateColorCodes('&', menuHeader) + "That player is not currently online, therefore, they are already silenced!");
			return true;
			
		}
		
	    plugin.datacore.set("users." + args[0] + ".muted", true);
	    String reason = ChatColor.GOLD + italic + createString(args, 1);
	    
		int overall_warnings = plugin.datacore.getInt("users." + args[0] + "." + "Warnings.Overall");
		int mute_warnings = plugin.datacore.getInt("users." + args[0] + "." + "Warnings.Mutes.Total");
		
		int overall_warnings2 = (overall_warnings + 1);
		int kick_warnings2 = (mute_warnings + 1);
		plugin.datacore.set("users." + args[0] + "." + "Warnings.Overall", overall_warnings2);
		plugin.datacore.set("users." + args[0] + "." + "Warnings.Mutes.Total", kick_warnings2);
		plugin.datacore.set("users." + args[0] + "." + "Warnings.Mutes.Reason" + kick_warnings2, reason);
		plugin.datacore.set("users." + args[0] + "." + "Warnings.Mutes.Auth" + kick_warnings2, p.getDisplayName());
	    
		 Bukkit.broadcastMessage(ChatColor.translateAlternateColorCodes('&', muteDisplay).replaceAll("%auth", p.getDisplayName()).replaceAll("%afull", p.getName()).replaceAll("%player", muted.getDisplayName()).replaceAll("%reason", reason).replaceAll("%name", muted.getName()));
	
	} else {
		
		if (args.length < 2){
			
			sender.sendMessage(ChatColor.translateAlternateColorCodes('&', menuHeader) + "Correct usage: /mute <player> <reason>");
			return true;
		}
			
			
		Player muted = Bukkit.getPlayer(args[0]);
		
		if (muted == null) {
			
			sender.sendMessage(ChatColor.translateAlternateColorCodes('&', menuHeader) + "That player is not currently online, therefore, they are already silenced!");
			return true;
			
		}
		
	    plugin.datacore.set("users." + args[0] + ".muted", true);
	    String reason = ChatColor.GOLD + italic + createString(args, 1);
	    
		int overall_warnings = plugin.datacore.getInt("users." + args[0] + "." + "Warnings.Overall");
		int mute_warnings = plugin.datacore.getInt("users." + args[0] + "." + "Warnings.Mutes.Total");
		
		int overall_warnings2 = (overall_warnings + 1);
		int kick_warnings2 = (mute_warnings + 1);
		plugin.datacore.set("users." + args[0] + "." + "Warnings.Overall", overall_warnings2);
		plugin.datacore.set("users." + args[0] + "." + "Warnings.Mutes.Total", kick_warnings2);
		plugin.datacore.set("users." + args[0] + "." + "Warnings.Mutes.Reason" + kick_warnings2, reason);
		plugin.datacore.set("users." + args[0] + "." + "Warnings.Mutes.Auth" + kick_warnings2, "console");
	    
		 Bukkit.broadcastMessage(ChatColor.translateAlternateColorCodes('&', muteDisplay).replaceAll("%auth", "console").replaceAll("%player", muted.getDisplayName()).replaceAll("%reason", reason).replaceAll("%name", muted.getName()));

		
	}
}

if(cmd.getName().equalsIgnoreCase("awarn") || cmd.getName().equalsIgnoreCase("warn")) {
	
	
	if (args.length < 2) {
		
		sender.sendMessage(ChatColor.translateAlternateColorCodes('&', menuHeader) + "Currect usage is /warn <player> <reason>");
		return true;
	}
	
	Player p = Bukkit.getPlayer(args[0]);
	Player p2 = (Player) sender;
	
	if (p == null){
		
		sender.sendMessage(ChatColor.translateAlternateColorCodes('&', menuHeader) + "That player is currently offline. Did you spell it right?");
		return true;
		
	}
	
	
	if (plugin.datacore.getBoolean("Registered." + p.getName()) == false) {
		
		sender.sendMessage(ChatColor.translateAlternateColorCodes('&', menuHeader) + "That player has never logged into this server before. Did you spell it right?");
		return true;
		
	}
	
	Integer general_warnings = plugin.datacore.getInt("users." + args[0] + ".Warnings.Generals.Total");
	Integer general_warnings2 = general_warnings + 1;
	Integer overall_warnings = plugin.datacore.getInt("users." + args[0] + ".Warnings.Overall");
	Integer overall_warnings2 = overall_warnings + 1;
	
	plugin.datacore.set("users." + args[0] + ".Warnings.Generals.Total", general_warnings2);
	plugin.datacore.set("users." + args[0] + ".Warnings.Overall", overall_warnings2);
	String message = ChatColor.GOLD + italic + createString(args, 1); 
	plugin.datacore.set("users." + args[0] + ".Warnings.Generals.Reason" + general_warnings2, message);
	
	if (sender instanceof Player){
	plugin.datacore.set("users." + args[0] + ".Warnings.Generals.Auth" + general_warnings2, p.getDisplayName());
	 Bukkit.broadcastMessage(ChatColor.translateAlternateColorCodes('&', generalDisplay).replaceAll("%auth", p2.getDisplayName()).replaceAll("%afull", p2.getName()).replaceAll("%player", p.getDisplayName()).replaceAll("%name", p.getName()).replaceAll("%reason", message));
	return true;
	}
	plugin.datacore.set("users." + args[0] + ".Warnings.Generals.Auth" + general_warnings2, "console");
	Bukkit.broadcastMessage(ChatColor.translateAlternateColorCodes('&', generalDisplay).replaceAll("%auth", "console").replaceAll("%player", p.getDisplayName()).replaceAll("%name", p.getName()).replaceAll("%reason", message));
	
}

if(cmd.getName().equalsIgnoreCase("aunmute") || cmd.getName().equalsIgnoreCase("unmute")) {
	
	if (args.length < 1) {
		
		sender.sendMessage(ChatColor.translateAlternateColorCodes('&', menuHeader) + "Currect usage is /unmute <player>");
		return true;
	}
	
	if (plugin.datacore.getBoolean("users." + args[0] + ".muted") == false) {
		
		sender.sendMessage(ChatColor.translateAlternateColorCodes('&', menuHeader) + "That user is not currently muted...Did you spell thier name correctly?");
		return true;
		
	}
	
	Player mutedPlayer = Bukkit.getPlayer(args[0]); 
	Player unmuter = (Player) sender;
	
	if (mutedPlayer == null) {
		
		sender.sendMessage(ChatColor.translateAlternateColorCodes('&', menuHeader) + "That player is currently offline, but the system will unmute them anyway.");
	}
	
	plugin.datacore.set("users." + args[0] + ".muted", null);
	if (sender instanceof Player) {
	Bukkit.broadcastMessage(ChatColor.translateAlternateColorCodes('&', unmuteDisplay).replaceAll("%auth", unmuter.getDisplayName()).replaceAll("%afull", unmuter.getName()).replaceAll("%player", mutedPlayer.getDisplayName()).replaceAll("%name", mutedPlayer.getName()));
	return true;
	}
	Bukkit.broadcastMessage(ChatColor.translateAlternateColorCodes('&', unmuteDisplay).replaceAll("%auth", "console").replaceAll("%player", mutedPlayer.getDisplayName()).replaceAll("%name", mutedPlayer.getName()));
}

if(cmd.getName().equalsIgnoreCase("aunban")) {
	
	if (args.length < 1) {
		
		sender.sendMessage(ChatColor.translateAlternateColorCodes('&', menuHeader) + "Currect usage is /unban <player>");
		return true;
	}
	
	if (plugin.datacore.getBoolean("Banned." + args[0]) == false) {
		
		sender.sendMessage(ChatColor.translateAlternateColorCodes('&', menuHeader) + "That user is not currently banned...Did you spell thier name correctly?");
		return true;
		
	}
	


	
	plugin.datacore.set("Banned." + args[0], null);
	plugin.datacore.set("BanReason." + args[0], null);
	
	if (sender instanceof Player) {
	Player unbanner = (Player) sender;
	Bukkit.broadcastMessage(ChatColor.translateAlternateColorCodes('&', unbanDisplay).replaceAll("%auth", unbanner.getDisplayName()).replaceAll("%player", Bukkit.getPlayer(args[0]).getName()));
	return true;
	}
	
	Bukkit.broadcastMessage(ChatColor.translateAlternateColorCodes('&', unbanDisplay).replaceAll("%auth", "console").replaceAll("%player", Bukkit.getPlayer(args[0]).getName()));
	return true;
}

if(cmd.getName().equalsIgnoreCase("akick") || cmd.getName().equalsIgnoreCase("kick")) {

	if(sender instanceof Player){
    Player p = (Player) sender;
    if(p.hasPermission("a.kick")){
	if(args.length > 0){
		Player k = Bukkit.getPlayer(args[0]);
		if (k == null){
			sender.sendMessage(ChatColor.translateAlternateColorCodes('&', menuHeader) + "That player is currently offline. You must really hate them.");
	}else if(args.length == 1){
		int overall_warnings = plugin.datacore.getInt("users." + args[0] + "." + "Warnings.Overall");
		int kick_warnings = plugin.datacore.getInt("users." + args[0] + "." + "Warnings.Kicks.Total");
		String reason = ChatColor.GOLD + italic + "No reason specified. #rude";
		k.kickPlayer(reason + ChatColor.WHITE + " (" + p.getDisplayName() + ChatColor.WHITE + ")");
		int overall_warnings2 = (overall_warnings + 1);
		int kick_warnings2 = (kick_warnings + 1);
		plugin.datacore.set("users." + args[0] + "." + "Warnings.Overall", overall_warnings2);
		plugin.datacore.set("users." + args[0] + "." + "Warnings.Kicks.Total", kick_warnings2);
		plugin.datacore.set("users." + args[0] + "." + "Warnings.Kicks.Reason" + kick_warnings2, reason);
		plugin.datacore.set("users." + args[0] + "." + "Warnings.Kicks.Auth" + kick_warnings2, p.getDisplayName());
		Bukkit.broadcastMessage(ChatColor.translateAlternateColorCodes('&', kickDisplay).replaceAll("%auth", p.getDisplayName()).replaceAll("%afull", p.getName()).replaceAll("%player", k.getDisplayName()).replaceAll("%reason", reason).replaceAll("%name", k.getName()));
	}else if(args.length > 1){
		int overall_warnings = plugin.datacore.getInt("users." + args[0] + "." + "Warnings.Overall");
		int kick_warnings = plugin.datacore.getInt("users." + args[0] + "." + "Warnings.Kicks.Total");
		String reason = ChatColor.GOLD + italic + createString(args, 1);
		k.kickPlayer(reason + ChatColor.WHITE + " (" + p.getDisplayName() + ChatColor.WHITE + ")");
		int overall_warnings2 = (overall_warnings + 1);
		int kick_warnings2 = (kick_warnings + 1);
		plugin.datacore.set("users." + args[0] + "." + "Warnings.Overall", overall_warnings2);
		plugin.datacore.set("users." + args[0] + "." + "Warnings.Kicks.Total", kick_warnings2);
		plugin.datacore.set("users." + args[0] + "." + "Warnings.Kicks.Reason" + kick_warnings2, reason);
		plugin.datacore.set("users." + args[0] + "." + "Warnings.Kicks.Auth" + kick_warnings2, p.getDisplayName());
		Bukkit.broadcastMessage(ChatColor.translateAlternateColorCodes('&', kickDisplay).replaceAll("%auth", p.getDisplayName()).replaceAll("%afull", p.getName()).replaceAll("%player", k.getDisplayName()).replaceAll("%reason", reason).replaceAll("%name", k.getName()));
	}
	
	}else{
		sender.sendMessage(ChatColor.translateAlternateColorCodes('&', menuHeader) + "Try /kick <player> <reason> instead.");
	}
    }
}else{
	if(args.length > 0){ 
		Player k = Bukkit.getPlayer(args[0]);
	if (k == null){
		sender.sendMessage(ChatColor.translateAlternateColorCodes('&', menuHeader) + "That player is currently offline. You must really hate them.");
	}else if(args.length == 1){
		int overall_warnings = plugin.datacore.getInt("users." + args[0] + "." + "Warnings.Overall");
		int kick_warnings = plugin.datacore.getInt("users." + args[0] + "." + "Warnings.Kicks.Total");
		String reason = ChatColor.GOLD + italic + "No reason specified. #rude";
		k.kickPlayer(reason + ChatColor.WHITE + " (console)");
		int overall_warnings2 = (overall_warnings + 1);
		int kick_warnings2 = (kick_warnings + 1);
		plugin.datacore.set("users." + args[0] + "." + "Warnings.Overall", overall_warnings2);
		plugin.datacore.set("users." + args[0] + "." + "Warnings.Kicks.Total", kick_warnings2);
		plugin.datacore.set("users." + args[0] + "." + "Warnings.Kicks.Reason" + kick_warnings2, reason);
		plugin.datacore.set("users." + args[0] + "." + "Warnings.Kicks.Auth" + kick_warnings2, "console");
		Bukkit.broadcastMessage(ChatColor.translateAlternateColorCodes('&', kickDisplay).replaceAll("%auth", "console").replaceAll("%player", k.getDisplayName()).replaceAll("%reason", reason).replaceAll("%name", k.getName()));
	}else if(args.length > 1){
		int overall_warnings = plugin.datacore.getInt("users." + args[0] + "." + "Warnings.Overall");
		int kick_warnings = plugin.datacore.getInt("users." + args[0] + "." + "Warnings.Kicks.Total");
		String reason = ChatColor.GOLD + italic + createString(args, 1);
		k.kickPlayer(reason + ChatColor.WHITE + " (console)");
		int overall_warnings2 = (overall_warnings + 1);
		int kick_warnings2 = (kick_warnings + 1);
		plugin.datacore.set("users." + args[0] + "." + "Warnings.Overall", overall_warnings2);
		plugin.datacore.set("users." + args[0] + "." + "Warnings.Kicks.Total", kick_warnings2);
		plugin.datacore.set("users." + args[0] + "." + "Warnings.Kicks.Reason" + kick_warnings2, reason);
		plugin.datacore.set("users." + args[0] + "." + "Warnings.Kicks.Auth" + kick_warnings2, "console");
		Bukkit.broadcastMessage(ChatColor.translateAlternateColorCodes('&', kickDisplay).replaceAll("%auth", "console").replaceAll("%player", k.getDisplayName()).replaceAll("%reason", reason).replaceAll("%name", k.getName()));
	}
	
	}else{
		sender.sendMessage(ChatColor.translateAlternateColorCodes('&', menuHeader) + "Try /kick <player> <reason> instead.");
	}
	

}
}
return true;
}


private int getTaskID(){
	
	return taskID;
	
}



private String createString(String[] args, int i) {
	
	StringBuilder sb = new StringBuilder();
	for(i = 1; i < args.length; i++)
	{
		if(i != args.length && i != 1)
		{
			sb.append(" ");
		}
		sb.append(args[i]);
	}
	String message = sb.toString();
	return message;
}



}

package com.github.lyokofirelyte.Administratum;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.Arrays;
import java.util.List;
import java.util.logging.Level;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.plugin.Plugin;
import org.bukkit.plugin.PluginManager;
import org.bukkit.plugin.java.JavaPlugin;

import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.entity.Player;

public class AdministratumMain extends JavaPlugin implements CommandExecutor {
	
	public AdministratumMain()
	{
		
	}

	File configFile;
	File datacoreFile;
	File stylesFile;
	FileConfiguration config;
	FileConfiguration datacore;
	FileConfiguration styles;
	
	public String italic = ChatColor.ITALIC + "";

@Override
public void onEnable(){


	PluginManager pm = getServer().getPluginManager();
	pm.registerEvents(new CommandEx(this), this);
	pm.registerEvents(new JoinListen(this), this);
	getCommand("amute").setExecutor(new CommandEx(this));
	getCommand("akick").setExecutor(new CommandEx(this));
	getCommand("warn").setExecutor(new CommandEx(this));
	getCommand("awarn").setExecutor(new CommandEx(this));
	getCommand("kick").setExecutor(new CommandEx(this));
	getCommand("mute").setExecutor(new CommandEx(this));
	getCommand("aunmute").setExecutor(new CommandEx(this));
	getCommand("unmute").setExecutor(new CommandEx(this));
	getCommand("aunban").setExecutor(new CommandEx(this));
	getCommand("aban").setExecutor(new CommandEx(this));
	getCommand("restrict").setExecutor(new CommandEx(this));
	getCommand("unrestrict").setExecutor(new CommandEx(this));
	getCommand("arestrict").setExecutor(new CommandEx(this));
	getCommand("aunrestrict").setExecutor(new CommandEx(this));
	getCommand("freeze").setExecutor(new CommandEx(this));
	getCommand("afreeze").setExecutor(new CommandEx(this));
	getCommand("unfreeze").setExecutor(new CommandEx(this));
	getCommand("aunfreeze").setExecutor(new CommandEx(this));
	getCommand("logoff").setExecutor(new CommandEx(this));
	getCommand("alogoff").setExecutor(new CommandEx(this));
	getCommand("evac").setExecutor(new CommandEx(this));
	getCommand("aevac").setExecutor(new CommandEx(this));
	getCommand("a").setExecutor(this);
	getCommand("watchlist").setExecutor(this);
	getCommand("awatchlist").setExecutor(this);
	Plugin herochat = getServer().getPluginManager().getPlugin("Herochat");
	Plugin mcbans = getServer().getPluginManager().getPlugin("MCBans");
	configFile = new File(getDataFolder(), "config.yml");
	datacoreFile = new File(getDataFolder(), "datacore.yml");
	stylesFile = new File(getDataFolder(), "styles.yml");

	try {
		
		firstRun();
	} catch (Exception e) {
		e.printStackTrace();
	}
	
	config = new YamlConfiguration();
	datacore = new YamlConfiguration();
	styles = new YamlConfiguration();
	loadYamls();
	
	String bootUp = styles.getString("Themes.Console.BootUp");
	
	updateCheck();

	getLogger().info(bootUp);

	if (herochat != null) {
		getLogger().log(Level.INFO, "You are using Herochat! Make sure you are not using both sets of chat filters!");
	}
	if (mcbans != null) {
		getLogger().log(Level.INFO, "You are using MCBans! This will override kicks and bans unless you use the /a variant of the command!");
	}
	
	getLogger().log(Level.INFO, "Thank you for downloading and using my plugin! It means a lot to me.");
}

List <String> helpGlobal = Arrays.asList("&cADMINISTRATUM: COMPLETE. AUTHORITATIVE. CONTROL.", 
									"&4.....&6g&4..&6l&4..&6o&4..&6b&4..&6a&4..&6l&4.....&6h&4..&6e&4..&6l&4..&6p&4.....",
									"&8| &c/a help selection &f// &cHelp menu for selection & lookups.",
									"&8| &c/a help actions &f// &cHelp menu for authoritative actions.",
									"&8| &c/a help filters &f// &cChat filter configuration help.",
									"&8| &c/a help channels &f// &cChat channel configuration options.",
									"&8| &c/a help watchlist &f// &cWatchlist management.",
									"&8| &c/a help events &f// &cJoin / Quit message help.",
									"&8| &c/a help plugin &f// &cUnder-the-hood plugin options.",
									"&8| &c&o/a help search <query> &f&o// &c&oSearch the Administratum database for help.");

List <String> helpSelection = Arrays.asList("&cAdministratum &4// &e&oSelection & Lookups",
									"&8| &3/a sel <player> &9// &3Select <player> to lookup info on.",
									"&8| &3/a s <player> &9// &3Select yourself as the selected player.",
									"&8| &3/a k <player> &9// &3Kick lookup on selected player.",
									"&8| &3/a m <player> &9// &3Mute lookup on selected player.",
									"&8| &3/a g <player> &9// &3General lookup on selected player.",
									"&8| &3/a a <player> &9// &3Automatic Action lookup on selected player.",
									"&8| &3/a f <player> &9// &3Freeze lookup on selected player.",
									"&8| &3/a r <player> &9// &3Restriction lookup on selected player.");

List <String> helpActions = Arrays.asList("&cAdministratum &4// &e&oAuthoritative Actions",
									"&8| &3/aban <player> <reason> &9// &3Ban someone.",
									"&8| &3/aunban <player> &9// &3Unban someone.",
									"&8| &3/mute <player> <reason> &9// &3Perma-mute someone.",	
									"&8| &3/unmute <player> &9// &3Un-mute someone.",
									"&8| &3/freeze <player> <reason> &9// &3Freeze someone.",
									"&8| &3/unfreeze <player> &9// &3Un-freeze someone.",
									"&8| &3/warn <player> <reason> &f// &3Broadcast & record a general warning.",
									"&8| &3/restrict <player> &9// &3Prevent <player> from breaking/placing blocks.",
									"&8| &3/unrestrict <player> &9// &3Allow <player> to break/place blocks again.",
									"&8| &3/evac &9// &3Freeze, mute, and restrict all players that are not on the evacExempt list.",
									"&8| &3/logoff &9// &3Logs you out of the server. You'll be 27% cooler if you use this command.",
									"&8&oAdd 'a' in front of any of the above commands to override another plugin...");

List <String> helpFilters = Arrays.asList("&cAdministratum &4// &e&oFilter Management",
									"&8| &3/a filter add <word> <new phrase> &9// &3Add a filter to the config.",
									"&8| &3/a filter remove <word> &9// &3Remove a filter.",
									"&8| &3/a nowarn <word> &9// &3Keep the filter but turn off warnings for <word>.",
									"&8| &3/a warn <word> &9// &3Turn warnings back on for <word>.",
									"&8| &3/a ex <player> &9// &3Add <player> to the exemption list for automatic warnings.",
									"&8| &3/a -ex <player> &9// &3Remove <player> from the exemption list.");

List <String> helpChannels = Arrays.asList("&cAdministratum &4// &e&oChannel Configuration");


List <String> helpWatchlist = Arrays.asList("&cAdministratum &4// &e&oWatchList",
									"&8| &3/watchlist add <player> <reason> &9// &3Add <player> to the watchlist.",
									"&8| &3/watchlist rem <player> &9// &3Remove <player> from the watchlist.",
									"&8| &3/watchlist edit <player> <new reason> &9// &3Edit the reason for <player> in the watchlist.",
									"&8| &3/watchlist toggle &9// &3Toggle the on-join notification option.",
									"&8| &3/watchlist view &9// &3View the watchlist.",
									"&8&oAdd 'a' in front of any of the above commands to override another plugin...");

List <String> helpEvents = Arrays.asList("&cAdministratum &4// &e&oEvent Configuration (Requires values set TRUE in config)",
									"&8| &3/a join add <phrase> &9// &3Add a new login message into the random rotation.",
									"&8| &3/a join remove <phrase> &9// &3Remove a login message from the random rotation.",
									"&8| &3/a join list <phrase> &9// &3View the login messages.",
									"&8| &3/a quit add <phrase> &9// &3Add a new quit message into the random rotation.",
									"&8| &3/a quit remove <phrase> &9// &3Remove a quit message from the random rotation.",
									"&8| &3/a quit list <phrase> &9// &3View the quit messages.");
		
List <String> helpPlugin = Arrays.asList("&cAdministratum &4// &e&oPlugin Options",
									"&8| &3/a save &9// &3Save config, datacore, and style sheet.",
									"&8| &3/a reload &9// &3Reload config, datacore, and style sheet.",
									"&8| &3/a disable &9// &3Disable the plugin, making it appear red in your plugin list.",
									"&8| &3/a restart &9// &3Restart the plugin as if you reloaded it. *Debugging use only.",
									"&8| &3/a version &9// &3What version of the plugin are you running?");


public String AS(String DecorativeToasterCozy){
	
	String FlutterShysShed = ChatColor.translateAlternateColorCodes('&', DecorativeToasterCozy);
	return FlutterShysShed;
	
}


@Override 

public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {
	

	switch (cmd.getName()){

		case "watchlist": case "awatchlist":

			String watchListAddDisplay = styles.getString("Themes.Events.WatchListAdd");
			String watchListRemDisplay = styles.getString("Themes.Events.WatchListRem");
			String watchListEditDisplay = styles.getString("Themes.Events.WatchListEdit");
			String menuHeader2 = styles.getString("Themes.Headers.Menus");
			
			switch (args.length){
			
				case 0: 
					sender.sendMessage(ChatColor.translateAlternateColorCodes('&', menuHeader2) + " Let's try /watchlist add <player> <reason>, /watchlist rem <player>, or /watchlist view instead.");
					break;
					
				case 1: case 2:
					
					if (args[0].equalsIgnoreCase("toggle")){
						
						if (config.getBoolean("WatchListNotify")){
							config.set("WatchListNotify", false);
							sender.sendMessage(AS(menuHeader2 + "Staff will no longer receive notifications when users on the watchlist log in."));
							break;
						} else {
							config.set("WatchListNotify", true);
							sender.sendMessage(AS(menuHeader2 + "Staff will receive notifications when users on the watchlist log in."));
							break;
						}
						
					}
					
					if (args[0].equalsIgnoreCase("view")){
						
						List <String> watchList = datacore.getStringList("WatchList");
						int x = 0;
							if (watchList.size() == 0){
								sender.sendMessage(AS(menuHeader2 + "The watchlist is empty!"));
								break;
							}
							
						sender.sendMessage(ChatColor.translateAlternateColorCodes('&', menuHeader2 + "&3WatchList &f(&3ID &4// &c&oPlayer &4// &6Reason &4// &cAuth&f)"));
						
							for (String currentList : watchList){
								x++;
								
								String message = datacore.getString("Users." + currentList + ".WatchListReason");
								String auth = datacore.getString("Users." + currentList + ".WatchListAuth");
								sender.sendMessage(AS("&3" + x + " &4// &c&o" + currentList + " &4// &6" + message + " &4// " + auth));
							}
							
					} 
					
					
					else if (args[0].equalsIgnoreCase("rem")){
						
						List <String> watchList = datacore.getStringList("WatchList");
							if (!watchList.contains(args[1])){
							
								sender.sendMessage(AS(menuHeader2 + "That player is not on the watchlist."));
								break;
							}
							
						Player player2 = (Player) sender;
						watchList.remove(args[1]);
						datacore.set("Users." + args[1] + ".WatchListReason", null);
						datacore.set("Users." + args[1] + ".WatchListAuth", null);
						datacore.set("WatchList", watchList);
						sender.sendMessage(AS(menuHeader2 + "Removed &4&o" + args[1] + " &cfrom the watchlist!"));
							for (Player currentPlayer : Bukkit.getOnlinePlayers()){
							
								if (currentPlayer.hasPermission("administratum.watchlist")){

									currentPlayer.sendMessage(AS(watchListRemDisplay.replaceAll("%player", args[1]).replaceAll("%auth", player2.getDisplayName()).replaceAll("%name", args[1]).replaceAll("%afull", player2.getName())));
								
								}
							
							}
						saveYamls();
						break;
					
				
					} else {
						
						sender.sendMessage(ChatColor.translateAlternateColorCodes('&', menuHeader2) + " Let's try /watchlist add <player> <reason>, /watchlist rem <player>, or /watchlist view instead.");
					}
					
					break;
					
				default:

					
					if (args[0].equalsIgnoreCase("add")){
						
						Player player2 = (Player) sender;
						List <String> watchList = datacore.getStringList("WatchList");
							if (watchList.contains(args[1])){
								
								sender.sendMessage(AS(menuHeader2 + "That player is already on the watchlist."));
								break;
							}
						String message = createString(args, 2);
						watchList.add(args[1]);
						datacore.set("Users." + args[1] + ".WatchListReason", message);
						datacore.set("Users." + args[1] + ".WatchListAuth", player2.getDisplayName());
						datacore.set("WatchList", watchList);
						sender.sendMessage(AS(menuHeader2 + "Added &4&o" + args[1] + " &cto the watchlist!"));
						
						for (Player currentPlayer : Bukkit.getOnlinePlayers()){
							
							if (currentPlayer.hasPermission("administratum.watchlist")){

								currentPlayer.sendMessage(AS(watchListAddDisplay.replaceAll("%player", args[1]).replaceAll("%auth", player2.getDisplayName()).replaceAll("%name", args[1]).replaceAll("%afull", player2.getName())));
								
							}
							
						}
						saveYamls();
						break;
					}
					
					if (args[0].equalsIgnoreCase("edit")){
						
						Player player2 = (Player) sender;
						List <String> watchList = datacore.getStringList("WatchList");
							if (!watchList.contains(args[1])){
								
								sender.sendMessage(AS(menuHeader2 + "That player is not on the watchlist."));
								break;
							}
						String message = createString(args, 2);
						datacore.set("Users." + args[1] + ".WatchListReason", message);
						sender.sendMessage(AS(menuHeader2 + "Edited &4&o" + args[1] + " &cin the watchlist!"));
						
						for (Player currentPlayer : Bukkit.getOnlinePlayers()){
							
							if (currentPlayer.hasPermission("administratum.watchlist")){
								
								currentPlayer.sendMessage(AS(watchListEditDisplay.replaceAll("%player", args[1]).replaceAll("%auth", player2.getDisplayName()).replaceAll("%name", args[1]).replaceAll("%afull", player2.getName())));
								
							}
							
						}
						saveYamls();
						break;
					}

					
					break;

			}
			
		break;
	
		case "a":	

			String menuHeader = styles.getString("Themes.Headers.Menus");
			String selectionColor1 = styles.getString("Themes.Headers.SelectionColors.Color1");
			String selectionColor2 = styles.getString("Themes.Headers.SelectionColors.Color2");
			String selectionColor3 = styles.getString("Themes.Headers.SelectionColors.Color3");
			
			switch (args.length){
			
				default: case 0: 
					
					for (String rainbowDash : helpGlobal){
					sender.sendMessage(AS(rainbowDash));
					}
					break;
					
				case 1:
					
					switch (args[0].toLowerCase()){
					
						default: case "help": case "?":
							
							for (String rainbowDash : helpGlobal){
								sender.sendMessage(AS(rainbowDash));
								}
							break;
												
						case "reload":
							
							loadYamls();
							sender.sendMessage(ChatColor.translateAlternateColorCodes('&', menuHeader) + " Config and data reloaded!");
							break;
							
						case "save":
							
							saveYamls();
							sender.sendMessage(ChatColor.translateAlternateColorCodes('&', menuHeader) + " Config and data saved!");
							break;
							
						case "ex":
							
							sender.sendMessage(ChatColor.translateAlternateColorCodes('&', menuHeader) + " Correct usage is /a ex <player>.");
							break;
							
						case "-ex":
							
							sender.sendMessage(ChatColor.translateAlternateColorCodes('&', menuHeader) + " Correct usage is /a -ex <player>.");
							break;
							
						case "filter":
							
							sender.sendMessage(ChatColor.translateAlternateColorCodes('&', menuHeader) + " Correct usage is /a filter add <word> <new word> or /a filter remove <word>.");
							break;
							
						case "version":
							
							sender.sendMessage(ChatColor.translateAlternateColorCodes('&', menuHeader) + " How the hell should I know? You're the one who downloaded the damn thing.");
							break;
							
						case "nowarn":
							
							sender.sendMessage(ChatColor.translateAlternateColorCodes('&', menuHeader) + " Correct usage is /a nowarn <word>");
							break;
							
						case "warn":
							
							sender.sendMessage(ChatColor.translateAlternateColorCodes('&', menuHeader) + " Correct usage is /a warn <word>");
							break;
							
						case "restart":
	
							sender.sendMessage(ChatColor.translateAlternateColorCodes('&', menuHeader) + " Killed systems and saved YAMLS...");
							Bukkit.getPluginManager().disablePlugin(this);
							sender.sendMessage(ChatColor.translateAlternateColorCodes('&', menuHeader) + " Loaded plugin and read YAMLS!");
							Bukkit.getPluginManager().enablePlugin(this);
							break;
							
						case "sel":
							
							sender.sendMessage(ChatColor.translateAlternateColorCodes('&', menuHeader) + " Correct usage is /a sel <player>.");
							break;
							
						case "s":
							
							datacore.set("temp." + sender.getName() + "." + "selection", sender.getName());
							String selectedPlayer = datacore.getString("temp." + sender.getName() + "." + "selection");
							int selectedOverall = datacore.getInt("users." + selectedPlayer + "." + "Warnings.Overall");
							int selectedKicks = datacore.getInt("users." + selectedPlayer + "." + "Warnings.Kicks.Total");
							int selectedMutes = datacore.getInt("users." + selectedPlayer + "." + "Warnings.Mutes.Total");
							int selectedGenerals = datacore.getInt("users." + selectedPlayer + "." + "Warnings.Generals.Total");
							int selectedAutomatics = datacore.getInt("users." + selectedPlayer + "." + "Warnings.Automatics.Total");
							int selectedBans = datacore.getInt("users." + selectedPlayer + "." + "Warnings.Bans.Total");
							int selectedFreezes = datacore.getInt("users." + selectedPlayer + "." + "Warnings.Freezes.Total");
							int selectedRestrictions = datacore.getInt("users." + selectedPlayer + "." + "Warnings.Restrictions.Total");
							

							sender.sendMessage(new String[] {
						
							ChatColor.translateAlternateColorCodes('&', menuHeader) + "Global Dashboard of Yourself",
							ChatColor.RED + "..............................",
							ChatColor.GRAY + "| " + "§" + selectionColor1 + "Overall Warnings" + ChatColor.WHITE + ": " + "§" + selectionColor1 + selectedOverall,
							ChatColor.GRAY + "| " + "§" + selectionColor2 + "Automatic Actions" + ChatColor.WHITE + ": " + "§" + selectionColor2 + selectedAutomatics,
							ChatColor.GRAY + "| " + "§" + selectionColor2 + "General Warns" + ChatColor.WHITE + ": " + "§" + selectionColor2 + selectedGenerals,
							ChatColor.GRAY + "| " + "§" + selectionColor2 + "Mutes" + ChatColor.WHITE + ": " + "§" + selectionColor2 + selectedMutes,
							ChatColor.GRAY + "| " + "§" + selectionColor2 + "Kicks" + ChatColor.WHITE + ": " + "§" + selectionColor2 + selectedKicks,
							ChatColor.GRAY + "| " + "§" + selectionColor2 + "Freezes" + ChatColor.WHITE + ": " + "§" + selectionColor2 + selectedFreezes,
							ChatColor.GRAY + "| " + "§" + selectionColor2 + "Restrictions" + ChatColor.WHITE + ": " + "§" + selectionColor2 + selectedRestrictions,
							ChatColor.GRAY + "| " + "§" + selectionColor2 + "Bans" + ChatColor.WHITE + ": " + "§" + selectionColor2 + selectedBans,
							"§" + selectionColor3 + italic + "Lookup" + ChatColor.WHITE + ": " + "§" + selectionColor3 + "/a [a] [g] [m] [k] [f] [r] [b]"});
							break;
							
						case "a":
							
							datacore.set("temp." + sender.getName() + ".typeSelection", "Automatics");
							checkSelection(sender);
							break;
							
						case "g":
							
							datacore.set("temp." + sender.getName() + ".typeSelection", "Generals");
							checkSelection(sender);
							break;

						case "m":
							
							datacore.set("temp." + sender.getName() + ".typeSelection", "Mutes");
							checkSelection(sender);
							break;
							
						case "k":
							
							datacore.set("temp." + sender.getName() + ".typeSelection", "Kicks");
							checkSelection(sender);
							break;
							
						case "f":
							
							datacore.set("temp." + sender.getName() + ".typeSelection", "Freezes");
							checkSelection(sender);
							break;
							
						case "r":
							
							datacore.set("temp." + sender.getName() + ".typeSelection", "Restrictions");
							checkSelection(sender);
							break;
							
						case "b":
							
							datacore.set("temp." + sender.getName() + ".typeSelection", "Bans");
							checkSelection(sender);
							break;
							
						case "join":
							
							sender.sendMessage(ChatColor.translateAlternateColorCodes('&', menuHeader) + "Correct usage is /a join add <message> or /a join remove <message>.");
							break;
							
						case "quit": 
							
							sender.sendMessage(ChatColor.translateAlternateColorCodes('&', menuHeader) + "Correct usage is /a quit add <message> or /a quit remove <message>.");
							break;
							


					}
					
				break;
				
				case 2:
					
					switch (args[0].toLowerCase()){
					
						default:
							
							for (String rainbowDash : helpGlobal){
								sender.sendMessage(AS(rainbowDash));
								}
							break;
							
						case "help":

							switch (args[1].toLowerCase()){
							
							case "selection":
								for (String appleJack : helpSelection){
									sender.sendMessage(AS(appleJack));
									}
								break;
								
							case "actions":
								for (String twilightSparkle : helpActions){
									sender.sendMessage(AS(twilightSparkle));
									}
								break;
								
							case "filters":
								for (String pinkiePie : helpFilters){
									sender.sendMessage(AS(pinkiePie));
									}
								break;
								
							case "channels":
								for (String rainbowDash : helpChannels){
									sender.sendMessage(AS(rainbowDash));
									}
								break;
								
							case "watchlist":
								for (String rarity : helpWatchlist){
									sender.sendMessage(AS(rarity));
									}
								break;
								
							case "events":
								for (String flutterShy : helpEvents){
									sender.sendMessage(AS(flutterShy));
									}
								break;
								
							case "plugin":
								for (String spikeTheDragon : helpPlugin){
									sender.sendMessage(AS(spikeTheDragon));
									}
								break;
								
							case "search":		
								sender.sendMessage(AS(menuHeader + "Please type /a help search <query>."));						
								break;
							
							}
							
						break;
							
						case "quit":
							
							if (args[1].equalsIgnoreCase("list")){
								
								List <String> quitList = styles.getStringList("Themes.Events.QuitMessages");
								
								for (String currentQuit : quitList){
									
									sender.sendMessage(ChatColor.translateAlternateColorCodes('&', currentQuit));
									
								}
								
							break;
							
							} else {
								
								sender.sendMessage(ChatColor.translateAlternateColorCodes('&', menuHeader) + "Correct usage is /a quit add <message> or /a quit remove <message>.");
								break;
							}
							
						case "join":
							
							if (args[1].equalsIgnoreCase("list")){
								
								List <String> loginList = styles.getStringList("Themes.Events.LoginMessages");
								
								for (String currentLogin : loginList){
									
									sender.sendMessage(ChatColor.translateAlternateColorCodes('&', currentLogin));
									
								}
								
							break;
							
							} else {
								
								sender.sendMessage(ChatColor.translateAlternateColorCodes('&', menuHeader) + "Correct usage is /a join add <message> or /a join remove <message>.");
								break;
							}
					
						case "sel":
				
							List<String> registered = datacore.getStringList("RegisteredPlayers");
							int x = 0;
							
							for (String lookup : registered){
								
								if (lookup.contains(args[1])){
									
									datacore.set("temp." + sender.getName() + "." + "selection", lookup);
									x++;
									
								}
							
							}
							
							if (x == 0){
								
								ChatColor.translateAlternateColorCodes('&', menuHeader + "Administratum could not located anyone similar to that player. Try again.");
								break;
							}

							String selectedPlayer = datacore.getString("temp." + sender.getName() + "." + "selection");
							int selectedOverall = datacore.getInt("users." + selectedPlayer + "." + "Warnings.Overall");
							int selectedKicks = datacore.getInt("users." + selectedPlayer + "." + "Warnings.Kicks.Total");
							int selectedMutes = datacore.getInt("users." + selectedPlayer + "." + "Warnings.Mutes.Total");
							int selectedGenerals = datacore.getInt("users." + selectedPlayer + "." + "Warnings.Generals.Total");
							int selectedAutomatics = datacore.getInt("users." + selectedPlayer + "." + "Warnings.Automatics.Total");
							int selectedBans = datacore.getInt("users." + selectedPlayer + "." + "Warnings.Bans.Total");
							int selectedFreezes = datacore.getInt("users." + selectedPlayer + "." + "Warnings.Freezes.Total");
							int selectedRestrictions = datacore.getInt("users." + selectedPlayer + "." + "Warnings.Restrictions.Total");
							

							sender.sendMessage(new String[] {
						
							ChatColor.translateAlternateColorCodes('&', menuHeader) + "Global Dashboard of " + Bukkit.getPlayer(args[1]).getName(),
							ChatColor.RED + "..............................",
							ChatColor.GRAY + "| " + "§" + selectionColor1 + "Overall Warnings" + ChatColor.WHITE + ": " + "§" + selectionColor1 + selectedOverall,
							ChatColor.GRAY + "| " + "§" + selectionColor2 + "Automatic Actions" + ChatColor.WHITE + ": " + "§" + selectionColor2 + selectedAutomatics,
							ChatColor.GRAY + "| " + "§" + selectionColor2 + "General Warns" + ChatColor.WHITE + ": " + "§" + selectionColor2 + selectedGenerals,
							ChatColor.GRAY + "| " + "§" + selectionColor2 + "Mutes" + ChatColor.WHITE + ": " + "§" + selectionColor2 + selectedMutes,
							ChatColor.GRAY + "| " + "§" + selectionColor2 + "Kicks" + ChatColor.WHITE + ": " + "§" + selectionColor2 + selectedKicks,
							ChatColor.GRAY + "| " + "§" + selectionColor2 + "Freezes" + ChatColor.WHITE + ": " + "§" + selectionColor2 + selectedFreezes,
							ChatColor.GRAY + "| " + "§" + selectionColor2 + "Restrictions" + ChatColor.WHITE + ": " + "§" + selectionColor2 + selectedRestrictions,
							ChatColor.GRAY + "| " + "§" + selectionColor2 + "Bans" + ChatColor.WHITE + ": " + "§" + selectionColor2 + selectedBans,
							"§" + selectionColor3 + italic + "Lookup" + ChatColor.WHITE + ": " + "§" + selectionColor3 + "/a [a] [g] [m] [k] [f] [r] [b]"});
							break;
							
						case "ex":
							
							if (datacore.getBoolean("Registered." + args[1]) == false){
							
								sender.sendMessage(ChatColor.translateAlternateColorCodes('&', menuHeader) + "That person has never logged into this server.");
								break;
							}
							
							if (config.getBoolean("exempt." + args[1]) == true){
								
								sender.sendMessage(ChatColor.translateAlternateColorCodes('&', menuHeader) + "That person was already added to the list.");
								break;
							}
							
							config.set("exempt." + args[1], true);
							sender.sendMessage(ChatColor.translateAlternateColorCodes('&', menuHeader) + "Success! Added " + args[1] + " to the exemption list.");
							saveYamls();
							break;
							
						case "-ex":
							
							if (datacore.getBoolean("Registered." + args[1]) == false){
								
								sender.sendMessage(ChatColor.translateAlternateColorCodes('&', menuHeader) + "That person has never logged into this server.");
								break;
							}
							
							if (config.getBoolean("exempt." + args[1]) == false){
								
								sender.sendMessage(ChatColor.translateAlternateColorCodes('&', menuHeader) + "That person was already removed from the list.");
								break;
							}
							
							config.set("exempt." + args[1], false);
							sender.sendMessage(ChatColor.translateAlternateColorCodes('&', menuHeader) + "Success! Removed " + args[1] + " from the exemption list.");
							saveYamls();
							break;
							
						case "filter":
							
							sender.sendMessage(ChatColor.translateAlternateColorCodes('&', menuHeader) + " Correct usage is /a filter add <word> <new word> or /a filter remove <word>.");
							break;
							
						case "warn":
					
							List<String> currentList = (config.getStringList("filters.noWarn"));
							currentList.remove(args[1]);
							config.set("filters.noWarn", currentList);
							saveYamls();
							sender.sendMessage(ChatColor.translateAlternateColorCodes('&', menuHeader) + "The word " + args[1] + " will be filtered AND issue warnings.");
							break;
							
						case "nowarn":
							
							currentList = (config.getStringList("filters.noWarn"));
							currentList.add(args[1]);
							config.set("filters.noWarn", currentList);
							saveYamls();
							sender.sendMessage(ChatColor.translateAlternateColorCodes('&', menuHeader) + "The word " + args[1] + " will be filtered but no warnings will be issued.");
							break;
							
						case "a":
							
							datacore.set("temp." + sender.getName() + ".typeSelection", "Automatics");
							checkSelectionSpecific(sender, args);
							break;
							
						case "g":
							
							datacore.set("temp." + sender.getName() + ".typeSelection", "Generals");
							checkSelectionSpecific(sender, args);
							break;

						case "m":
							
							datacore.set("temp." + sender.getName() + ".typeSelection", "Mutes");
							checkSelectionSpecific(sender, args);
							break;
							
						case "k":
							
							datacore.set("temp." + sender.getName() + ".typeSelection", "Kicks");
							checkSelectionSpecific(sender, args);
							break;
							
						case "f":
							
							datacore.set("temp." + sender.getName() + ".typeSelection", "Freezes");
							checkSelectionSpecific(sender, args);
							break;
							
						case "r":
							
							datacore.set("temp." + sender.getName() + ".typeSelection", "Restrictions");
							checkSelectionSpecific(sender, args);
							break;
							
						case "b":
							
							datacore.set("temp." + sender.getName() + ".typeSelection", "Bans");
							checkSelectionSpecific(sender, args);
							break;
					}
					
				break;
				
				case 3: case 4: case 5: case 6: case 7: case 8: case 9: case 10: case 11: case 12: case 13: case 14: case 15: case 16: case 17: case 18: case 19: case 20:
					
					switch (args[0].toLowerCase()){
					
						default:
							
							for (String rainbowDash : helpGlobal){
								sender.sendMessage(AS(rainbowDash));
								}
							break;
							
						case "help":
							
							if (args[1].equalsIgnoreCase("search")){
								
								int results = 0;
								
								String searchQuery = createString(args, 3);
								
									for (String discord : helpSelection){
										if (discord.toLowerCase().contains(searchQuery.toLowerCase())){
											sender.sendMessage(AS(discord));
											results++;
										}
									}
									
									for (String princessCelestia : helpActions){
										if (princessCelestia.toLowerCase().contains(searchQuery.toLowerCase())){
											sender.sendMessage(AS(princessCelestia));
											results++;
										}
									}
									
									for (String princessLuna : helpFilters){
										if (princessLuna.toLowerCase().contains(searchQuery.toLowerCase())){
											sender.sendMessage(AS(princessLuna));
											results++;
										}
									}
									
									for (String mrsFrizzle : helpChannels){
										if (mrsFrizzle.toLowerCase().contains(searchQuery.toLowerCase())){
											sender.sendMessage(AS(mrsFrizzle));
											results++;
										}
									}
									
									for (String trixie : helpWatchlist){
										if (trixie.toLowerCase().contains((searchQuery.toLowerCase()))){
											sender.sendMessage(AS(trixie));
											results++;
										}
									}
									
									for (String cutieMarkCrusaders : helpEvents){
										if (cutieMarkCrusaders.toLowerCase().contains(searchQuery.toLowerCase())){
											sender.sendMessage(AS(cutieMarkCrusaders));
											results++;
										}
									}
									
									for (String rainbowFactory : helpPlugin){
										if (rainbowFactory.toLowerCase().contains(searchQuery.toLowerCase())){
											sender.sendMessage(AS(rainbowFactory));
											results++;
										}
									}
									
									sender.sendMessage(AS("&8&oWe found " + results + " &8&oresults for this search."));
									break;
											
							} else {
								for (String rainbowDash : helpGlobal){
									sender.sendMessage(AS(rainbowDash));
									}
								break;
							}

						case "filter":
							
							if (args[1].equalsIgnoreCase("add")){
								
								String newPhrase = createString(args, 3);
								String oldWord = args[2];
								List<String> currentOld = (config.getStringList("filters.filter"));
								currentOld.add(oldWord);
								config.set("filters.filter",currentOld);
								config.set("filters.customFilters." + oldWord, newPhrase);
								sender.sendMessage(ChatColor.translateAlternateColorCodes('&', menuHeader) + "The word " + oldWord + " will now be filtered to " + newPhrase + ".");
								saveYamls();
								break;
							}
							
							if (args[1].equalsIgnoreCase("remove")){
								
								String oldWord = args[2];
								List<String> currentOld = (config.getStringList("filters.filter"));
								currentOld.remove(oldWord);
								config.set("filters.filter",currentOld);
								config.set("filters.customFilters." + oldWord, null);
								sender.sendMessage(ChatColor.translateAlternateColorCodes('&', menuHeader) + "The word " + oldWord + " will no longer be filtered.");
								saveYamls();
							    break;
							}
						
						break;
							
						case "join":
							
							List <String> joinList = styles.getStringList("Themes.Events.LoginMessages");
							
							
							if (args[1].equalsIgnoreCase("add")){
								
								if (joinList.contains(args[2])){
									
									sender.sendMessage(ChatColor.translateAlternateColorCodes('&', menuHeader) + "That phrase is already on the join list!");
									break;
								
								}
								
								joinList.add(args[2]);
								styles.set("Themes.Events.LoginMessages", joinList);
								sender.sendMessage(ChatColor.translateAlternateColorCodes('&', menuHeader) + "Added " + args[2] + " to the join message list!");
								break;
								
							}
							
							if (args[1].equalsIgnoreCase("remove")){
								
								if (!joinList.contains(args[2])){
									
									sender.sendMessage(ChatColor.translateAlternateColorCodes('&', menuHeader) + "That phrase is already removed from the join list!");
									break;
								
								}
								
								joinList.remove(args[2]);
								styles.set("Themes.Events.LoginMessages", joinList);
								sender.sendMessage(ChatColor.translateAlternateColorCodes('&', menuHeader) + "Removed " + args[2] + " to the join message list!");
								break;
								
							}
							
							if (args[1].equalsIgnoreCase("list")){
								
								for (String currentJoin : joinList){
									
									sender.sendMessage(ChatColor.translateAlternateColorCodes('&', currentJoin));
									
								}
								
							break;
							}
							
						break;
							
						case "quit":
							
							List <String> quitList = styles.getStringList("Themes.Events.QuitMessages");
							
							
							if (args[1].equalsIgnoreCase("add")){
								
								if (quitList.contains(args[2])){
									
									sender.sendMessage(ChatColor.translateAlternateColorCodes('&', menuHeader) + "That phrase is already on the quit list!");
									break;
								
								}
								
								quitList.add(args[2]);
								styles.set("Themes.Events.QuitMessages", quitList);
								sender.sendMessage(ChatColor.translateAlternateColorCodes('&', menuHeader) + "Added " + args[2] + " to the quit message list!");
								break;
								
							}
							
							if (args[1].equalsIgnoreCase("remove")){
								
								if (!quitList.contains(args[2])){
									
									sender.sendMessage(ChatColor.translateAlternateColorCodes('&', menuHeader) + "That phrase is already removed from the quit list!");
									break;
								
								}
								
								quitList.remove(args[2]);
								styles.set("Themes.Events.QuitMessages", quitList);
								sender.sendMessage(ChatColor.translateAlternateColorCodes('&', menuHeader) + "Removed " + args[2] + " to the quit message list!");
								break;
								
							}
							

					break;
	
					}
			break;
			
			}
	break;
	
	}
return true;
}

	public static boolean isInteger(String s) {
   	    try { 
   	        Integer.parseInt(s); 
   	    } catch(NumberFormatException e) { 
   	        return false; 
   	    }

   	    return true;
   	}

private void firstRun() throws Exception {
    if(!configFile.exists()){
        configFile.getParentFile().mkdirs();
        copy(getResource("config.yml"), configFile);
    }
    if(!datacoreFile.exists()){
    	datacoreFile.getParentFile().mkdirs();
        copy(getResource("datacore.yml"), datacoreFile);
    }
    if(!stylesFile.exists()){
    	stylesFile.getParentFile().mkdirs();
        copy(getResource("styles.yml"), stylesFile);
    }

}

private void copy(InputStream in, File file) {
    try {
        OutputStream out = new FileOutputStream(file);
        byte[] buf = new byte[1024];
        int len;
        while((len=in.read(buf))>0){
            out.write(buf,0,len);
        }
        out.close();
        in.close();
    } catch (Exception e) {
        e.printStackTrace();
    }
    
}

public void saveYamls() {

    try {
        config.save(configFile);
        datacore.save(datacoreFile);
        styles.save(stylesFile);
    } catch (IOException e) {
        e.printStackTrace();
    }
}


public void loadYamls() {
    try {
        config.load(configFile);
        datacore.load(datacoreFile);
        styles.load(stylesFile);
    } catch (Exception e) {
        e.printStackTrace();
    }
}
    
public void onDisable() {
	
	String shutDown = styles.getString("Themes.Console.ShutDown");
	getLogger().info(shutDown);
	saveYamls();

}

public String createString(String[] args, int x)

{
    StringBuilder sb = new StringBuilder();
    for(int i = x; i < args.length; i++)
    {
        if(i != x && i != args.length)
        {
            sb.append(" ");
        }
        sb.append(args[i]);
    }
    return sb.toString();
}

public void checkSelectionSpecific(CommandSender sender, String[] args){
	

String menuHeader = styles.getString("Themes.Headers.Menus");
String selectionColor1 = styles.getString("Themes.Headers.SelectionColors.Color1");
String selectionColor2 = styles.getString("Themes.Headers.SelectionColors.Color2");
String lookupColor = styles.getString("Themes.Headers.LookupColor");
String lookupDivider = styles.getString("Themes.Headers.LookupDivider");
	

	 String selectedPlayer = datacore.getString("temp." + sender.getName() + "." + "selection");
	 String selectionType = datacore.getString("temp." + sender.getName() + ".typeSelection");
	 int selectedTotals = datacore.getInt("users." + selectedPlayer + "." + "Warnings." + selectionType + ".Total");
	 
  	 if(isInteger(args[1])){
  		  
     int generalRequest = Integer.parseInt(args[1]);
  	  
  	  
	 if (selectedTotals >= generalRequest ){
	
	sender.sendMessage(new String[] {
	"§" + lookupColor + "Viewing " + selectionType + " " + args[1] + "§" + lookupDivider + "// " + "§" + lookupColor + selectedPlayer,
	ChatColor.GRAY + "| " + "§" + selectionColor2 + "Auth" + ChatColor.WHITE + ": " + "§" + selectionColor1 + datacore.getString("users." + selectedPlayer + "." + "Warnings." + selectionType + ".Auth" + args[1]),
	ChatColor.GRAY + "| " + "§" + selectionColor2 + "Reason" + ChatColor.WHITE + ": " + "§" + selectionColor1 + datacore.getString("users." + selectedPlayer + "." + "Warnings." + selectionType + ".Reason" + args[1]),
	ChatColor.WHITE + "|"});
	
	 }else{
		 
		 sender.sendMessage(ChatColor.translateAlternateColorCodes('&', menuHeader) + "That player does not have that many warnings, or you for some reason put in a letter instead of a number.");
		 return;
		  
	 }
   }
}

public void checkSelection(CommandSender sender){
	

String selectionColor1 = styles.getString("Themes.Headers.SelectionColors.Color1");
String selectionColor2 = styles.getString("Themes.Headers.SelectionColors.Color2");
String lookupColor = styles.getString("Themes.Headers.LookupColor");
String lookupDivider = styles.getString("Themes.Headers.LookupDivider");
	
	String selectedPlayer = datacore.getString("temp." + sender.getName() + "." + "selection");
	String selectionType = datacore.getString("temp." + sender.getName() + ".typeSelection");
	int selectedLookup = datacore.getInt("users." + selectedPlayer + "." + "Warnings." + selectionType + ".Total");
    sender.sendMessage("§" + lookupColor + selectionType + " " + "§" + lookupDivider + "// " + "§" + lookupColor + selectedPlayer);
    
    switch (selectedLookup) {
	
	case 1: case 2: case 3: case 4:
		
		int z = 1;
		int x = selectedLookup;
		while(z < 5 && x > 0){
	  
		sender.sendMessage(new String[] {
		ChatColor.GRAY + "| " + "§" + selectionColor2 + "Auth" + ChatColor.WHITE + ": " + "§" + selectionColor1 + datacore.getString("users." + selectedPlayer + "." + "Warnings." + selectionType + ".Auth" + z),
		ChatColor.GRAY + "| " + "§" + selectionColor2 + "Reason" + ChatColor.WHITE + ": " + "§" + selectionColor1 + datacore.getString("users." + selectedPlayer + "." + "Warnings." + selectionType + ".Reason" + z),
		ChatColor.WHITE + "|"
		});
		z++;
		x--;
		}
		sender.sendMessage(ChatColor.GRAY + italic + "Want more? Type /a <letter> <number> to show a specific action.");
		break;
		
	case 5: case 6: case 7: case 8:
		
		int z3 = 5;
		int x3 = selectedLookup;
		while(z3 < 9 && x3 >= 5){
			
		sender.sendMessage(new String[] {
		ChatColor.GRAY + "| " + "§" + selectionColor2 + "Auth" + ChatColor.WHITE + ": " + "§" + selectionColor1 + datacore.getString("users." + selectedPlayer + "." + "Warnings." + selectionType + ".Auth" + z3),
		ChatColor.GRAY + "| " + "§" + selectionColor2 + "Reason" + ChatColor.WHITE + ": " + "§" + selectionColor1 + datacore.getString("users." + selectedPlayer + "." + "Warnings." + selectionType + ".Reason" + z3),
		ChatColor.WHITE + "|"
		});
		z3++;
		x3--;
		}
		sender.sendMessage(ChatColor.GRAY + italic + "Want more? Type /a <letter> <number> to show a specific action.");
		break;
		
	default:
		
		int z2 = 1;
		int x2 = selectedLookup;
		while(z2 < 5 && x2 > 0){			  
		sender.sendMessage(new String[] {
		ChatColor.GRAY + "| " + "§" + selectionColor2 + "Auth" + ChatColor.WHITE + ": " + "§" + selectionColor1 + datacore.getString("users." + selectedPlayer + "." + "Warnings." + selectionType + ".Auth" + z2),
		ChatColor.GRAY + "| " + "§" + selectionColor2 + "Reason" + ChatColor.WHITE + ": " + "§" + selectionColor1 + datacore.getString("users." + selectedPlayer + "." + "Warnings." + selectionType + ".Reason" + z2),
		ChatColor.WHITE + "|"
		});
		z2++;
		x2--;	
		}			
		sender.sendMessage(ChatColor.GRAY + italic + "Want more? Type /a <letter> <number> to show a specific action.");
		break;

    }
	
	
	
}

public void updateCheck(){
	
	if (config.getString("WatchListNotify") == null){
		config.set("WatchListNotify", true);	
	}
	if (config.getString("UseJoinMessages") == null){
		config.set("UseJoinMessages", false);	
	}
	if (config.getString("UseQuitMessages") == null){
		config.set("UseQuitMessages", false);	
	}
	if (config.getString("freezeEffect") == null){
		config.set("freezeEffect", false);	
	}
	if (styles.getString("Themes.Events.WatchListAdd") == null){
		styles.set("Themes.Events.WatchListAdd", "&cAdministratum &4// &c%player &cwas added to the Administratum watchlist by %auth");	
	}
	if (styles.getString("Themes.Events.WatchListRem") == null){
		styles.set("Themes.Events.WatchListRem", "&cAdministratum &4// &c%player &cwas removed from the Administratum watchlist by %auth");	
	}
	if (styles.getString("Themes.Events.WatchListEdit") == null){
		styles.set("Themes.Events.WatchListEdit", "&cAdministratum &4// &c%player &cwas edited in the Administratum watchlist by %auth");	
	}
	if (styles.getString("Themes.Events.WatchListLogin") == null){
		styles.set("Themes.Events.WatchListLogin", "&cAdministratum &4// &c%player &cis on the Administratum watchlist!");	
	}
	
	saveYamls();
	
}


}


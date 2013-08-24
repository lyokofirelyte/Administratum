package com.github.lyokofirelyte.Administratum;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
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

	
	getLogger().info(bootUp);

	if (herochat != null) {
		getLogger().log(Level.INFO, "You are using Herochat! Make sure you are not using both sets of chat filters!");
	}
	if (mcbans != null) {
		getLogger().log(Level.INFO, "You are using MCBans! This will override kicks and bans unless you use the /a variant of the command!");
	}
	
	getLogger().log(Level.INFO, "Thank you for downloading and using my plugin! It means a lot to me.");
}

String[] help1 = new String[] {
		
	    ChatColor.RED + "ADMINISTRATUM: COMPLETE. AUTHORITATIVE. CONTROL.",
	    ChatColor.DARK_RED + "..............................",
	    ChatColor.GRAY + "| " + ChatColor.RED + "/a sel <player>" + ChatColor.WHITE + " // " + ChatColor.RED + "Select <player> to lookup info on.",
	    ChatColor.GRAY + "| " + ChatColor.RED + "/a k" + ChatColor.WHITE + " // " + ChatColor.RED + "Kick lookup on selected player.",
	    ChatColor.GRAY + "| " + ChatColor.RED + "/a m" + ChatColor.WHITE + " // " + ChatColor.RED + "Mute lookup on selected player.",
	    ChatColor.GRAY + "| " + ChatColor.RED + "/a g" + ChatColor.WHITE + " // " + ChatColor.RED + "General Warning lookup on selected player.",
	    ChatColor.GRAY + "| " + ChatColor.RED + "/a a" + ChatColor.WHITE + " // " + ChatColor.RED + "Automatic Action lookup on selected player.",
	    ChatColor.GRAY + "| " + ChatColor.RED + "/a f" + ChatColor.WHITE + " // " + ChatColor.RED + "Freeze lookup on selected player.",
	    ChatColor.GRAY + "| " + ChatColor.RED + "/a r" + ChatColor.WHITE + " // " + ChatColor.RED + "Restriction lookup on selected player.",
	    ChatColor.GRAY + "| " + ChatColor.RED + "/a s" + ChatColor.WHITE + " // " + ChatColor.RED + "Check your own status without selecting yourself.",
	    ChatColor.GRAY + "| " + ChatColor.RED + "/a ex <player>" + ChatColor.WHITE + " // " + ChatColor.RED + "Add <player> to the automatic warning exemption list.",
	    ChatColor.GRAY + "| " + ChatColor.RED + "/a -ex <player>" + ChatColor.WHITE + " // " + ChatColor.RED + "Remove <player> from the automatic warning exemption list.",
	    ChatColor.GRAY + italic + "More help @ /a help2"
				
		};

String[] help2 = new String[] {
		ChatColor.GRAY + "| " + ChatColor.RED + "/aban <player> <reason>" + ChatColor.WHITE + " // " + ChatColor.RED + "Issue an Administratum ban.",
		ChatColor.GRAY + "| " + ChatColor.RED + "/aunban <player>" + ChatColor.WHITE + " // " + ChatColor.RED + "Unban someone.",
		ChatColor.GRAY + "| " + ChatColor.RED + "/mute <player> <reason>" + ChatColor.WHITE + " // " + ChatColor.RED + "Perma-mute someone.",	
		ChatColor.GRAY + "| " + ChatColor.RED + "/unmute <player>" + ChatColor.WHITE + " // " + ChatColor.RED + "Un-mute someone.",
		ChatColor.GRAY + "| " + ChatColor.RED + "/freeze <player> <reason>" + ChatColor.WHITE + " // " + ChatColor.RED + "Freeze someone.",
		ChatColor.GRAY + "| " + ChatColor.RED + "/unfreeze <player>" + ChatColor.WHITE + " // " + ChatColor.RED + "Un-freeze someone.",
		ChatColor.GRAY + "| " + ChatColor.RED + "/warn <player> <reason>" + ChatColor.WHITE + " // " + ChatColor.RED + "Broadcast & record a general warning.",
		ChatColor.GRAY + "| " + ChatColor.RED + "/restrict <player>" + ChatColor.WHITE + " // " + ChatColor.RED + "Prevent <player> from breaking/placing blocks.",
		ChatColor.GRAY + "| " + ChatColor.RED + "/unrestrict <player>" + ChatColor.WHITE + " // " + ChatColor.RED + "Allow <player> to break/place blocks again.",
		ChatColor.GRAY + "| " + ChatColor.RED + "/evac" + ChatColor.WHITE + " // " + ChatColor.RED + "Freeze, mute, and restrict all players that are not on the evacExempt list.",
		ChatColor.GRAY + italic + "Add 'a' in front of any of the commands to override another plugin...",
		ChatColor.GRAY + italic + "More help @ /a help3"
				
		};

String[] help3 = new String[] {
		ChatColor.GRAY + "| " + ChatColor.RED + "/a filter add <word> <filtered>" + ChatColor.WHITE + " // " + ChatColor.RED + "Add a filter to the config.",
		ChatColor.GRAY + "| " + ChatColor.RED + "/a filter remove <word>" + ChatColor.WHITE + " // " + ChatColor.RED + "Remove a filter.",
		ChatColor.GRAY + "| " + ChatColor.RED + "/a nowarn <word>" + ChatColor.WHITE + " // " + ChatColor.RED + "Keep the filter but turn off warnings.",
		ChatColor.GRAY + "| " + ChatColor.RED + "/a warn <word>" + ChatColor.WHITE + " // " + ChatColor.RED + "Turn back on automatic warnings for <word>.",
		ChatColor.GRAY + "| " + ChatColor.RED + "/a save" + ChatColor.WHITE + " // " + ChatColor.RED + "Save config.",
		ChatColor.GRAY + "| " + ChatColor.RED + "/a reload" + ChatColor.WHITE + " // " + ChatColor.RED + "Reload config from file.",
		ChatColor.GRAY + "| " + ChatColor.RED + "/a disable" + ChatColor.WHITE + " // " + ChatColor.RED + "Disable the plugin, making it appear red on your plugin list.",
		ChatColor.GRAY + "| " + ChatColor.RED + "/a restart" + ChatColor.WHITE + " // " + ChatColor.RED + "Disable and re-enable the plugin.",
		ChatColor.GRAY + "| " + ChatColor.RED + "/a join add <phrase>" + ChatColor.WHITE + " // " + ChatColor.RED + "Add a new login message!",
		ChatColor.GRAY + "| " + ChatColor.RED + "/a join remove <phrase>" + ChatColor.WHITE + " // " + ChatColor.RED + "Remove a login message!",
		ChatColor.GRAY + "| " + ChatColor.RED + "/a join list" + ChatColor.WHITE + " // " + ChatColor.RED + "List your login messages.",
		ChatColor.GRAY + "| " + ChatColor.RED + "/a quit add <phrase>" + ChatColor.WHITE + " // " + ChatColor.RED + "Add a new logoff message!",
		ChatColor.GRAY + "| " + ChatColor.RED + "/a quit remove <phrase>" + ChatColor.WHITE + " // " + ChatColor.RED + "Remove a logoff message!",
		ChatColor.GRAY + "| " + ChatColor.RED + "/a quit list" + ChatColor.WHITE + " // " + ChatColor.RED + "List your logoff messages.",
		ChatColor.GRAY + italic + "Remember to suggest new features and report bugs on the Administratum Bukkit page!"
				
		};


@Override 

public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {
	

	switch (cmd.getName()){
	
		case "a":		
			

			String menuHeader = styles.getString("Themes.Headers.Menus");
			String selectionColor1 = styles.getString("Themes.Headers.SelectionColors.Color1");
			String selectionColor2 = styles.getString("Themes.Headers.SelectionColors.Color2");
			String selectionColor3 = styles.getString("Themes.Headers.SelectionColors.Color3");
			
			switch (args.length){
			
				default: case 0: 
					
					sender.sendMessage(help1);
					break;
					
				case 1:
					
					switch (args[0].toLowerCase()){
					
						default: case "help": case "?":
							
							sender.sendMessage(help1);
							break;
						
						case "help2":
							
							sender.sendMessage(help2);
							break;
							
						case "help3":
							
							sender.sendMessage(help3);
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
							ChatColor.GRAY + "| " + "�" + selectionColor1 + "Overall Warnings" + ChatColor.WHITE + ": " + "�" + selectionColor1 + selectedOverall,
							ChatColor.GRAY + "| " + "�" + selectionColor2 + "Automatic Actions" + ChatColor.WHITE + ": " + "�" + selectionColor2 + selectedAutomatics,
							ChatColor.GRAY + "| " + "�" + selectionColor2 + "General Warns" + ChatColor.WHITE + ": " + "�" + selectionColor2 + selectedGenerals,
							ChatColor.GRAY + "| " + "�" + selectionColor2 + "Mutes" + ChatColor.WHITE + ": " + "�" + selectionColor2 + selectedMutes,
							ChatColor.GRAY + "| " + "�" + selectionColor2 + "Kicks" + ChatColor.WHITE + ": " + "�" + selectionColor2 + selectedKicks,
							ChatColor.GRAY + "| " + "�" + selectionColor2 + "Freezes" + ChatColor.WHITE + ": " + "�" + selectionColor2 + selectedFreezes,
							ChatColor.GRAY + "| " + "�" + selectionColor2 + "Restrictions" + ChatColor.WHITE + ": " + "�" + selectionColor2 + selectedRestrictions,
							ChatColor.GRAY + "| " + "�" + selectionColor2 + "Bans" + ChatColor.WHITE + ": " + "�" + selectionColor2 + selectedBans,
							"�" + selectionColor3 + italic + "Lookup" + ChatColor.WHITE + ": " + "�" + selectionColor3 + "/a [a] [g] [m] [k] [f] [r] [b]"});
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
							
							sender.sendMessage(help1);
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
							ChatColor.GRAY + "| " + "�" + selectionColor1 + "Overall Warnings" + ChatColor.WHITE + ": " + "�" + selectionColor1 + selectedOverall,
							ChatColor.GRAY + "| " + "�" + selectionColor2 + "Automatic Actions" + ChatColor.WHITE + ": " + "�" + selectionColor2 + selectedAutomatics,
							ChatColor.GRAY + "| " + "�" + selectionColor2 + "General Warns" + ChatColor.WHITE + ": " + "�" + selectionColor2 + selectedGenerals,
							ChatColor.GRAY + "| " + "�" + selectionColor2 + "Mutes" + ChatColor.WHITE + ": " + "�" + selectionColor2 + selectedMutes,
							ChatColor.GRAY + "| " + "�" + selectionColor2 + "Kicks" + ChatColor.WHITE + ": " + "�" + selectionColor2 + selectedKicks,
							ChatColor.GRAY + "| " + "�" + selectionColor2 + "Freezes" + ChatColor.WHITE + ": " + "�" + selectionColor2 + selectedFreezes,
							ChatColor.GRAY + "| " + "�" + selectionColor2 + "Restrictions" + ChatColor.WHITE + ": " + "�" + selectionColor2 + selectedRestrictions,
							ChatColor.GRAY + "| " + "�" + selectionColor2 + "Bans" + ChatColor.WHITE + ": " + "�" + selectionColor2 + selectedBans,
							"�" + selectionColor3 + italic + "Lookup" + ChatColor.WHITE + ": " + "�" + selectionColor3 + "/a [a] [g] [m] [k] [f] [r] [b]"});
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
							
							sender.sendMessage(help1);
							break;
					
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
	"�" + lookupColor + "Viewing " + selectionType + " " + args[1] + "�" + lookupDivider + "// " + "�" + lookupColor + selectedPlayer,
	ChatColor.GRAY + "| " + "�" + selectionColor2 + "Auth" + ChatColor.WHITE + ": " + "�" + selectionColor1 + datacore.getString("users." + selectedPlayer + "." + "Warnings." + selectionType + ".Auth" + args[1]),
	ChatColor.GRAY + "| " + "�" + selectionColor2 + "Reason" + ChatColor.WHITE + ": " + "�" + selectionColor1 + datacore.getString("users." + selectedPlayer + "." + "Warnings." + selectionType + ".Reason" + args[1]),
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
    sender.sendMessage("�" + lookupColor + selectionType + " " + "�" + lookupDivider + "// " + "�" + lookupColor + selectedPlayer);
    
    switch (selectedLookup) {
	
	case 1: case 2: case 3: case 4:
		
		int z = 1;
		int x = selectedLookup;
		while(z < 5 && x > 0){
	  
		sender.sendMessage(new String[] {
		ChatColor.GRAY + "| " + "�" + selectionColor2 + "Auth" + ChatColor.WHITE + ": " + "�" + selectionColor1 + datacore.getString("users." + selectedPlayer + "." + "Warnings." + selectionType + ".Auth" + z),
		ChatColor.GRAY + "| " + "�" + selectionColor2 + "Reason" + ChatColor.WHITE + ": " + "�" + selectionColor1 + datacore.getString("users." + selectedPlayer + "." + "Warnings." + selectionType + ".Reason" + z),
		ChatColor.WHITE + "|"
		});
		z++;
		x--;
		}
		break;
		
	case 5: case 6: case 7: case 8:
		
		int z3 = 5;
		int x3 = selectedLookup;
		while(z3 < 9 && x3 >= 5){
			
		sender.sendMessage(new String[] {
		ChatColor.GRAY + "| " + "�" + selectionColor2 + "Auth" + ChatColor.WHITE + ": " + "�" + selectionColor1 + datacore.getString("users." + selectedPlayer + "." + "Warnings." + selectionType + ".Auth" + z3),
		ChatColor.GRAY + "| " + "�" + selectionColor2 + "Reason" + ChatColor.WHITE + ": " + "�" + selectionColor1 + datacore.getString("users." + selectedPlayer + "." + "Warnings." + selectionType + ".Reason" + z3),
		ChatColor.WHITE + "|"
		});
		z3++;
		x3--;
		}
		break;
		
	default:
		
		int z2 = 1;
		int x2 = selectedLookup;
		while(z2 < 5 && x2 > 0){			  
		sender.sendMessage(new String[] {
		ChatColor.GRAY + "| " + "�" + selectionColor2 + "Auth" + ChatColor.WHITE + ": " + "�" + selectionColor1 + datacore.getString("users." + selectedPlayer + "." + "Warnings." + selectionType + ".Auth" + z2),
		ChatColor.GRAY + "| " + "�" + selectionColor2 + "Reason" + ChatColor.WHITE + ": " + "�" + selectionColor1 + datacore.getString("users." + selectedPlayer + "." + "Warnings." + selectionType + ".Reason" + z2),
		ChatColor.WHITE + "|"
		});
		z2++;
		x2--;	
		}			
		sender.sendMessage(ChatColor.GRAY + italic + "Next page? /a <type>2");
		break;

    }
	
	
	
}


}


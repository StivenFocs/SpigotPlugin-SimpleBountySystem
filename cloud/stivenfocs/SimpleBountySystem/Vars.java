package cloud.stivenfocs.SimpleBountySystem;

import com.google.gson.JsonParser;
import me.clip.placeholderapi.PlaceholderAPI;
import net.milkbowl.vault.economy.Economy;
import net.milkbowl.vault.economy.EconomyResponse;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Sound;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.command.SimpleCommandMap;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.entity.Player;

import java.io.File;
import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.List;

public class Vars {

    public static Loader plugin;
    public FileConfiguration getConfig() {
        return plugin.getConfig();
    }

    public File dataFile = new File(plugin.getDataFolder() + "/data.yml");
    public FileConfiguration dataConfig;

    static Vars vars;
    public static Vars getVars() {
        if (vars == null) vars = new Vars();
        return vars;
    }

    private static Field bukkitCommandMap = null;

    static {
        try {
            bukkitCommandMap = Bukkit.getServer().getClass().getDeclaredField("commandMap");
            bukkitCommandMap.setAccessible(true);
        } catch (NoSuchFieldException noSuchFieldException) {
            noSuchFieldException.printStackTrace();
        }
    }

    public static Economy econ;

    ///////////////////////////

    public static Integer bounty_increment_every_kills = 5;
    public static Double bounty_increment_amount = 50.0;
    public static List<String> killer_actions = new ArrayList<>();
    public static List<String> killed_actions = new ArrayList<>();
    public static String bounty_placeholder = "";
    public static Boolean disable_in_duels = true;

    public static String prefix = "";
    public static String configuration_reloaded = "";
    public static String an_error_occurred = "";
    public static String no_permission = "";
    public static String unknown_subcommand = "";
    public static String bounty_placed = "";
    public static String bounty_updated = "";
    public static String incomplete_command = "";
    public static String player_not_found = "";
    public static String integer_needed = "";
    public static String bounty_set = "";
    public static List<String> help_admin = new ArrayList<>();
    public static List<String> help_user = new ArrayList<>();

    ///////////////////////////

    public boolean reloadVars() {
        try {
            plugin.reloadConfig();

            getConfig().options().header("Developed with LOV by StivenFocs");
            getConfig().options().copyDefaults(true);

            getConfig().addDefault("options.bounty_increment_every_kills", 5);
            getConfig().addDefault("options.bounty_increment_amount", 50.0);
            List<String> new_killer_actions = new ArrayList<>();
            new_killer_actions.add("broadcast:&f%player_name% &7claimed the bounty on &f%killed_name% &7with the value of &a$%bounty_amount%");
            new_killer_actions.add("money:%bounty_amount%");
            getConfig().addDefault("options.killer_actions", new_killer_actions);
            List<String> new_killed_actions = new ArrayList<>();
            getConfig().addDefault("options.killed_actions", new_killed_actions);
            getConfig().addDefault("options.bounty_placeholder", "&2$%bounty_amount%");

            getConfig().addDefault("messages.prefix", "");
            getConfig().addDefault("messages.configuration_reloaded", "&aConfiguration successfully reloaded");
            getConfig().addDefault("messages.an_error_occurred", "&cAn error occurred while doing this task...");
            getConfig().addDefault("messages.no_permission", "&cYou're not permitted to do this.");
            getConfig().addDefault("messages.unknown_subcommand", "&cUnknown subcommand! use &4/sbs &cfor the subcommands list.");
            getConfig().addDefault("messages.bounty_placed", "&7A bounty of &a$%bounty_amount% &7has been placed on &f%player_name%");
            getConfig().addDefault("messages.bounty_updated", "&7The bounty on &f%player_name% &7has been raised up to &a$%bounty_amount%");
            getConfig().addDefault("messages.incomplete_command", "&cIncomplete command! type &4/sbs &cfor the subcommands list.");
            getConfig().addDefault("messages.player_not_found", "&cNo player found with the name &f%player_name%");
            getConfig().addDefault("messages.integer_needed", "&cAn integer number is needed!");
            getConfig().addDefault("messages.bounty_set", "&eyou applied a bounty of &f%bounty_amount% &e to &f%player_name%");
            List<String> new_help_admin = new ArrayList<>();
            new_help_admin.add("&8&m*=======================*");
            new_help_admin.add("&7* &6&lBounty&e&lSystem &7%version%");
            new_help_admin.add("");
            new_help_admin.add("&7* /sbs reload &8&m|&7 Reload the whole configuration");
            new_help_admin.add("&7* /sbs setbounty <playername> <amount> &8&m|&7 set a player bounty");
            new_help_admin.add("");
            new_help_admin.add("&8&m*=======================*");
            getConfig().addDefault("messages.help_admin", new_help_admin);
            List<String> new_help_user = new ArrayList<>();
            new_help_user.add("&8&m*=======================*");
            new_help_user.add("&7* &6&lBounty&e&lSystem");
            new_help_user.add("");
            new_help_user.add("&7* This plugin allows you to");
            new_help_user.add("&7* gain prizes and money at");
            new_help_user.add("&7* every player kill.");
            new_help_user.add("");
            new_help_user.add("&8&m*=======================*");
            getConfig().addDefault("messages.help_user", new_help_user);

            plugin.saveConfig();
            plugin.reloadConfig();

            reloadDataConfig();

            dataConfig.options().header("Developed with LOV by StivenFocs");

            saveDataConfig();
            reloadDataConfig();

            disable_in_duels = getConfig().getBoolean("options.disable_in_duels", true);

            bounty_increment_every_kills = getConfig().getInt("options.bounty_increment_every_kills", 5);
            bounty_increment_amount = getConfig().getDouble("options.bounty_increment_amount", 50.0);
            killer_actions = getConfig().getStringList("options.killer_actions");
            killed_actions = getConfig().getStringList("options.killed_actions");
            bounty_placeholder = getConfig().getString("options.bounty_placeholder", "&2$%bounty_amount%");

            prefix = getConfig().getString("messages.prefix", "");
            configuration_reloaded = getConfig().getString("messages.configuration_reloaded", "&aConfiguration successfully reloaded");
            an_error_occurred = getConfig().getString("messages.an_error_occurred", "&cAn error occurred while doing this task...");
            no_permission = getConfig().getString("messages.no_permission", "&cYou're not permitted to do this.");
            unknown_subcommand = getConfig().getString("messages.unknown_subcommand", "&cUnknown subcommand! use &4/sbs &cfor the subcommands list.");
            bounty_placed = getConfig().getString("messages.bounty_placed", "&7A bounty of &a$%bounty_amount% &7has been placed on &f%player_name%");
            bounty_updated = getConfig().getString("messages.bounty_updated", "&7The bounty on &f%player_name% &7has been raised up to &a$%bounty_amount%");
            incomplete_command = getConfig().getString("messages.incomplete_command", "&cIncomplete command! type &4/sbs &cfor the subcommands list.");
            player_not_found = getConfig().getString("messages.player_not_found", "&cNo player found with the name &f%player_name%");
            integer_needed = getConfig().getString("messages.integer_needed", "&cAn integer number is needed!");
            bounty_set = getConfig().getString("messages.bounty_set", "&eyou applied a bounty of &f%bounty_amount% &e to &f%player_name%");
            help_admin = getConfig().getStringList("messages.help_admin");
            help_user = getConfig().getStringList("messages.help_user");

            plugin.getLogger().info("Configuration successfully reloaded");
            return true;
        } catch (Exception ex) {
            plugin.getLogger().severe("Couldn't load the whole configuration! disabling the pluùgin.");
            ex.printStackTrace();
            Bukkit.getPluginManager().disablePlugin(plugin);
            return false;
        }
    }

    public void saveDataConfig() {
        if (!dataFile.exists()) {
            try {
                if (!dataFile.createNewFile()) {
                    plugin.getLogger().severe("Unable to generate the data configuration!!");
                }
            } catch (Exception ex) {
                plugin.getLogger().severe("Unable to generate the data configuration!!");
                ex.printStackTrace();
            }
            dataConfig = YamlConfiguration.loadConfiguration(dataFile);
        }

        try {
            dataConfig.save(dataFile);
        } catch (Exception ex) {
            plugin.getLogger().severe("Unable to save the data configuration!!");
            ex.printStackTrace();
        }
    }

    public void reloadDataConfig() {
        if (!dataFile.exists()) {
            try {
                if (!dataFile.createNewFile()) {
                    plugin.getLogger().severe("Unable to generate the data configuration!!");
                }
            } catch (Exception ex) {
                plugin.getLogger().severe("Unable to generate the data configuration!!");
                ex.printStackTrace();
            }
        }

        dataConfig = YamlConfiguration.loadConfiguration(dataFile);
    }

    ///////////////////////////

    public static boolean hasAdminPermissions(CommandSender user) {
        return user.hasPermission("simplebountysystem.admin");
    }

    public static String setPlaceholders(String text, CommandSender user) {
        text = text.replace("%version%", plugin.getDescription().getVersion());
        text = text.replace("%name%", user.getName());
        if (user instanceof Player) {
            Player p = (Player) user;
            PlayerStats p_stats = new PlayerStats(p.getUniqueId());

            text = text.replace("%displayname%", p.getDisplayName());
            text = text.replace("%kills%", String.valueOf(p_stats.getKills()));
            text = text.replace("%bounty%", String.valueOf(p_stats.getBounty()));
        }

        return text;
    }

    ///////////////////////////

    public static void sendString(String text, CommandSender user) {
        if (text.length() > 0) {
            text = prefix + text;
            if (Vars.plugin.getConfig().getString(text) != null) text = Vars.plugin.getConfig().getString(text);
            for (String line : text.split("/n")) {
                line = setPlaceholders(line, user);
                if (user instanceof Player && Bukkit.getPluginManager().getPlugin("PlaceholderAPI") != null)
                    line = PlaceholderAPI.setPlaceholders((Player) user, line);

                if (isValidJson(line) && user instanceof Player) {
                    Bukkit.dispatchCommand(Bukkit.getConsoleSender(), "tellraw " + user.getName() + " " + line);
                }

                user.sendMessage(ChatColor.translateAlternateColorCodes('&', line));
            }
        }
    }

    public static void sendStringList(List<String> string_list, CommandSender user) {
        for (String line_ : string_list) {
            line_ = prefix + line_;
            if (line_.length() > 0 && Vars.plugin.getConfig().getString(line_) != null) line_ = Vars.plugin.getConfig().getString(line_);
            for (String line : line_.split("/n")) {
                line = setPlaceholders(line, user);
                if (user instanceof Player && Bukkit.getPluginManager().getPlugin("PlaceholderAPI") != null)
                    line = PlaceholderAPI.setPlaceholders((Player) user, line);

                if (isValidJson(line) && user instanceof Player) {
                    Bukkit.dispatchCommand(Bukkit.getConsoleSender(), "tellraw " + user.getName() + " " + line);
                }

                user.sendMessage(ChatColor.translateAlternateColorCodes('&', line));
            }
        }
    }

    public static boolean isValidJson(String json) {
        try {
            return new JsonParser().parse(json).getAsJsonObject() != null;
        } catch (Throwable ignored) {}

        try {
            return new JsonParser().parse(json).getAsJsonArray() != null;
        } catch (Throwable ignored) {}

        return false;
    }

    public static void prizePlayer(Player p, Player killed, Double bounty_amount) {
        for (String action : Vars.killer_actions) {
            try {
                action = action.replace("%player_name%", p.getName()).replace("%player_displayname%", p.getDisplayName()).replace("%player_uuid%", p.getUniqueId().toString()).replace("%world%", p.getWorld().getName()).replace("%x%", String.valueOf(p.getLocation().getX())).replace("%y%", String.valueOf(p.getLocation().getY())).replace("%z%", String.valueOf(p.getLocation().getZ())).replace("%killed_name%", killed.getName()).replace("%killed_displayname%", killed.getDisplayName()).replace("%killed_uuid%", killed.getUniqueId().toString()).replace("%killed_world%", killed.getWorld().getName()).replace("%killed_x%", String.valueOf(killed.getLocation().getX())).replace("%killed_y%", String.valueOf(killed.getLocation().getY())).replace("%killed_z%", String.valueOf(killed.getLocation().getZ())).replace("%bounty_amount%", String.valueOf(bounty_amount));
                if (Bukkit.getPluginManager().getPlugin("PlaceholderAPI") != null) {
                    action = PlaceholderAPI.setPlaceholders(p, action);
                }

                if (action.startsWith("tell:")) {
                    p.sendMessage(ChatColor.translateAlternateColorCodes('&', action.replaceAll("tell:", "")));
                } else if (action.startsWith("broadcast:")) {
                    Bukkit.broadcastMessage(ChatColor.translateAlternateColorCodes('&', action.replaceAll("broadcast:", "")));
                } else if (action.startsWith("sudo:")) {
                    action = action.replace("sudo:", "");
                    if (Vars.isBukkitCommand(action)) {
                        p.performCommand(action);
                    } else {
                        p.chat("/" + action);
                    }
                } else if (action.startsWith("permitted_sudo:")) {
                    action = action.replace("permitted_sudo:", "");
                    boolean op = p.isOp();

                    p.setOp(true);
                    if (Vars.isBukkitCommand(action)) {
                        p.performCommand(action);
                    } else {
                        p.chat("/" + action);
                    }
                    p.setOp(op);
                } else if (action.startsWith("sound:")) {
                    action = action.replace(" ","");
                    String[] sound = action.replace("sound:", "").split(",");
                    p.playSound(p.getLocation(), Sound.valueOf(sound[0].toUpperCase()), Integer.parseInt(sound[1]), Integer.parseInt(sound[2]));
                } else if (action.startsWith("money:")) {
                    double money_amount = Double.parseDouble(action.replace("money:", ""));
                    EconomyResponse economy_response = Vars.econ.depositPlayer(p, money_amount);
                    if (!economy_response.transactionSuccess()) {
                        plugin.getLogger().warning("Could not success a transaction for " + p.getName() + " of " + money_amount + " : " + economy_response.errorMessage);
                    }
                } else {
                    Bukkit.dispatchCommand(Bukkit.getConsoleSender(), action);
                }
            } catch (Exception ex) {
                Vars.plugin.getLogger().warning("Unable to run the killer_action: " + action + " Reason: " + ex.getMessage());
            }
        }
    }

    public static void unprizePlayer(Player p, Player killer, Double bounty_amount) {
        for (String action : Vars.killed_actions) {
            try {
                action = action.replace("%player_name%", p.getName()).replace("%player_displayname%", p.getDisplayName()).replace("%player_uuid%", p.getUniqueId().toString()).replace("%world%", p.getWorld().getName()).replace("%x%", String.valueOf(p.getLocation().getX())).replace("%y%", String.valueOf(p.getLocation().getY())).replace("%z%", String.valueOf(p.getLocation().getZ())).replace("%killer_name%", killer.getName()).replace("%killer_displayname%", killer.getDisplayName()).replace("%killer_uuid%", killer.getUniqueId().toString()).replace("%killer_world%", killer.getWorld().getName()).replace("%killer_x%", String.valueOf(killer.getLocation().getX())).replace("%killer_y%", String.valueOf(killer.getLocation().getY())).replace("%killer_z%", String.valueOf(killer.getLocation().getZ())).replace("%bounty_amount%", String.valueOf(bounty_amount));
                if (Bukkit.getPluginManager().getPlugin("PlaceholderAPI") != null) {
                    action = PlaceholderAPI.setPlaceholders(p, action);
                }

                if (action.startsWith("tell:")) {
                    p.sendMessage(ChatColor.translateAlternateColorCodes('&', action.replaceAll("tell:", "")));
                } else if (action.startsWith("broadcast:")) {
                    Bukkit.broadcastMessage(ChatColor.translateAlternateColorCodes('&', action.replaceAll("broadcast:", "")));
                } else if (action.startsWith("sudo:")) {
                    action = action.replace("sudo:", "");
                    if (Vars.isBukkitCommand(action)) {
                        p.performCommand(action);
                    } else {
                        p.chat("/" + action);
                    }
                } else if (action.startsWith("permitted_sudo:")) {
                    action = action.replace("permitted_sudo:", "");
                    boolean op = p.isOp();

                    p.setOp(true);
                    if (Vars.isBukkitCommand(action)) {
                        p.performCommand(action);
                    } else {
                        p.chat("/" + action);
                    }
                    p.setOp(op);
                } else if (action.startsWith("sound:")) {
                    action = action.replace(" ","");
                    String[] sound = action.replace("sound:", "").split(",");
                    p.playSound(p.getLocation(), Sound.valueOf(sound[0].toUpperCase()), Integer.parseInt(sound[1]), Integer.parseInt(sound[2]));
                } else if (action.startsWith("money:")) {
                    double money_amount = Double.parseDouble(action.replace("money:", ""));
                    EconomyResponse economy_response = Vars.econ.depositPlayer(p, money_amount);
                    if (!economy_response.transactionSuccess()) {
                        plugin.getLogger().warning("Could not success a transaction for " + p.getName() + " of " + money_amount + " : " + economy_response.errorMessage);
                    }
                } else {
                    Bukkit.dispatchCommand(Bukkit.getConsoleSender(), action);
                }
            } catch (Exception ex) {
                Vars.plugin.getLogger().warning("Unable to run the killed_action: " + action + " Reason: " + ex.getMessage());
            }
        }
    }

    public static boolean isDigit(String text) {
        try {
            Integer.parseInt(text);
            return true;
        } catch (Exception ignored) {
        }
        return false;
    }

    public static boolean isDoubleDigit(String text) {
        try {
            Double.parseDouble(text);
            return true;
        } catch (Exception ignored) {
        }
        return false;
    }

    public static boolean isBukkitCommand(String paramString) {
        paramString = paramString.split(" ")[0];
        try {
            SimpleCommandMap simpleCommandMap = (SimpleCommandMap) bukkitCommandMap.get(Bukkit.getServer());
            for (Command command : simpleCommandMap.getCommands()) {
                if (command.getName().equalsIgnoreCase(paramString) || command.getAliases().contains(paramString))
                    return true;
            }
        } catch (IllegalAccessException ex) {
            Vars.plugin.getLogger().warning("An exception occurred while trying to retrieve and/or use the commandsMap");
            ex.printStackTrace();
        }
        return false;
    }

}

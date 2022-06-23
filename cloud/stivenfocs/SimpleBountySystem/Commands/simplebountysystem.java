package cloud.stivenfocs.SimpleBountySystem.Commands;

import cloud.stivenfocs.SimpleBountySystem.Loader;
import cloud.stivenfocs.SimpleBountySystem.PlayerStats;
import cloud.stivenfocs.SimpleBountySystem.Vars;
import org.bukkit.Bukkit;
import org.bukkit.command.*;
import org.bukkit.entity.Player;

import java.util.ArrayList;
import java.util.List;

public class simplebountysystem implements CommandExecutor, TabCompleter {

    private final Loader plugin;

    public simplebountysystem(Loader plugin) {
        this.plugin = plugin;
    }

    ///////////////////////////

    @Override
    public boolean onCommand(CommandSender sender, Command cmd, String s, String[] args) {
        if (args.length == 0) {
            if (Vars.hasAdminPermissions(sender)) {
                Vars.sendStringList(Vars.help_admin, sender);
            } else {
                Vars.sendStringList(Vars.help_user, sender);
            }
        } else {
            if (args[0].equalsIgnoreCase("reload")) {
                if (Vars.hasAdminPermissions(sender)) {
                    Vars vars = Vars.getVars();
                    if (sender instanceof ConsoleCommandSender) {
                        vars.reloadVars();
                    } else {
                        if (vars.reloadVars()) {
                            Vars.sendString(Vars.configuration_reloaded, sender);
                        } else {
                            Vars.sendString(Vars.an_error_occurred, sender);
                        }
                    }
                } else {
                    Vars.sendString(Vars.no_permission, sender);
                }
                return true;
            }
            if (args[0].equalsIgnoreCase("setbounty")) {
                if (Vars.hasAdminPermissions(sender)) {
                    if (args.length > 1) {
                        Player p = Bukkit.getPlayerExact(args[1]);
                        if (p != null) {
                            if (args.length > 2) {
                                if (Vars.isDoubleDigit(args[2])) {
                                    PlayerStats p_stats = new PlayerStats(p.getUniqueId());
                                    Double bounty_amount = Double.parseDouble(args[2]);
                                    p_stats.setBounty(bounty_amount);
                                    Vars.sendString(Vars.bounty_set.replace("%player_name%", p.getName()).replace("%player_displayname%", p.getDisplayName()).replace("%player_uuid%", String.valueOf(p.getUniqueId())).replace("%bounty_amount%", String.valueOf(bounty_amount)), sender);
                                } else Vars.sendString(Vars.integer_needed, sender);
                            } else  Vars.sendString(Vars.incomplete_command, sender);
                        } else Vars.sendString(Vars.player_not_found.replace("%player_name%", args[1]), sender);
                    } else Vars.sendString(Vars.incomplete_command, sender);
                } else Vars.sendString(Vars.no_permission, sender);
                return true;
            }

            Vars.sendString(Vars.unknown_subcommand, sender);
        }
        return false;
    }

    @Override
    public List<String> onTabComplete(CommandSender sender, Command cmd, String s, String[] args) {
        List<String> su = new ArrayList<>();

        if (args.length == 1) {
            if (args[0].equals("")) {
                if (Vars.hasAdminPermissions(sender)) {
                    su.add("reload");
                    su.add("setbounty");
                }
            } else {
                if (Vars.hasAdminPermissions(sender)) {
                    if ("reload".startsWith(args[0].toLowerCase())) {
                        su.add("reload");
                    }
                    if ("setbounty".startsWith(args[0].toLowerCase())) {
                        su.add("setbounty");
                    }
                }
            }
        }
        if (args.length == 2) {
            if (args[1].equals("")) {
                if (args[0].equalsIgnoreCase("setbounty")) {
                    for (Player p : Bukkit.getOnlinePlayers()) {
                        su.add(p.getName());
                    }
                }
            } else {
                if (args[0].equalsIgnoreCase("setbounty")) {
                    for (Player p : Bukkit.getOnlinePlayers()) {
                        if (p.getName().toLowerCase().startsWith(args[1].toLowerCase())) {
                            su.add(p.getName());
                        }
                    }
                }
            }
        }

        return su;
    }
}

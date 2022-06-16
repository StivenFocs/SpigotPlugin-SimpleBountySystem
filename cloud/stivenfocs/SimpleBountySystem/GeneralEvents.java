package cloud.stivenfocs.SimpleBountySystem;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.PlayerDeathEvent;

import java.util.UUID;

public class GeneralEvents implements Listener {

    private final Loader plugin;

    public GeneralEvents(Loader plugin) {
        this.plugin = plugin;
    }

    ///////////////////////////

    @EventHandler
    public void playerDeath(PlayerDeathEvent event) {
        Player p = event.getEntity();
        if (p.hasPermission("simplebountysystem.bounty")) {
            UUID p_UUID = p.getUniqueId();
            PlayerStats p_stats = new PlayerStats(p_UUID);

            if (p.getKiller() != null) {
                Player p_killer = p.getKiller();
                UUID p_killer_UUID = p_killer.getUniqueId();
                PlayerStats p_killer_stats = new PlayerStats(p_killer_UUID);

                p_killer_stats.setKills(p_killer_stats.getKills() + 1);
                if ((p_killer_stats.getKills() % Vars.bounty_increment_every_kills) == 0) {
                    p_killer_stats.setBounty(p_killer_stats.getBounty() + Vars.bounty_increment_amount);

                    if (p_killer_stats.getKills() == Vars.bounty_increment_every_kills) {
                        Bukkit.broadcastMessage(ChatColor.translateAlternateColorCodes('&', Vars.bounty_placed.replace("%player_name%", p_killer.getName()).replace("%player_displayname%", p_killer.getDisplayName()).replace("%player_uuid%", String.valueOf(p_killer.getUniqueId())).replace("%bounty_amount%", String.valueOf(p_killer_stats.getBounty()))));
                    } else {
                        Bukkit.broadcastMessage(ChatColor.translateAlternateColorCodes('&', Vars.bounty_updated.replace("%player_name%", p_killer.getName()).replace("%player_displayname%", p_killer.getDisplayName()).replace("%player_uuid%", String.valueOf(p_killer.getUniqueId())).replace("%bounty_amount%", String.valueOf(p_killer_stats.getBounty()))));
                    }
                }

                if (p_stats.getBounty() > 0) {
                    p_killer_stats.prizePlayer(p_stats.getBounty(), p);
                }

                p_stats.reset();
                p_stats.unprizePlayer(p_stats.getBounty(), p.getKiller());
            }
        }
    }

}

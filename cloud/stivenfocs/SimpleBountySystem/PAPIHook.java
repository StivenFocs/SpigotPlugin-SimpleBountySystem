package cloud.stivenfocs.SimpleBountySystem;

import me.clip.placeholderapi.expansion.PlaceholderExpansion;
import org.bukkit.OfflinePlayer;
import org.bukkit.entity.Player;

public class PAPIHook extends PlaceholderExpansion {

   private final Loader plugin;

   public PAPIHook(Loader plugin) {
     this.plugin = plugin;
   }

    ///////////////////////////

   public String getIdentifier() {
     return plugin.getName();
   }

   public String getAuthor() {
     return plugin.getDescription().getAuthors().get(0);
   }

   public String getVersion() {
     return plugin.getDescription().getVersion();
   }

   public boolean persist() {
     return true;
   }

   public String onRequest(OfflinePlayer player, String params) {
     if (player != null && player.isOnline()) return onPlaceholderRequest(player.getPlayer(), params);
     return null;
   }

   public String onPlaceholderRequest(Player p, String identifier) {
       PlayerStats p_stats = new PlayerStats(p.getUniqueId());
    if (identifier.equals("kills")) return String.valueOf(p_stats.getKills());
    if (identifier.equals("bounty")) {
        if (p_stats.getBounty() > 0) {
            if (p_stats.getBounty().intValue() == p_stats.getBounty()) {
                return Vars.bounty_placeholder.replace("%bounty_amount%", String.valueOf(p_stats.getBounty().intValue()));
            } else return Vars.bounty_placeholder.replace("%bounty_amount%", String.valueOf(p_stats.getBounty()));
        } else {
            return "";
        }
    }
    return null;
   }
 }
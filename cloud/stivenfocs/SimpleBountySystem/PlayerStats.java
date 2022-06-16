package cloud.stivenfocs.SimpleBountySystem;

import org.bukkit.Bukkit;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.entity.Player;

import java.util.UUID;

public class PlayerStats {

    public final UUID pUID;

    public PlayerStats(UUID pUID) {
        this.pUID = pUID;
    }

    ///////////////////////////

    public void prizePlayer(Double bounty_amount, Player killed) {
        Vars.prizePlayer(Bukkit.getPlayer(pUID), killed, bounty_amount);
    }

    public void unprizePlayer(Double bounty_amount, Player killer) {
        Vars.unprizePlayer(Bukkit.getPlayer(pUID), killer, bounty_amount);
    }

    public Integer getKills() {
        addDefault();
        return Vars.getVars().dataConfig.getInt(pUID + ".kills");
    }

    public Double getBounty() {
        addDefault();
        return Vars.getVars().dataConfig.getDouble(pUID + ".bounty");
    }

    public void setKills(Integer new_kills) {
        Vars.getVars().dataConfig.set(pUID + ".kills", new_kills);
    }

    public void setBounty(Double new_bounty) {
        Vars.getVars().dataConfig.set(pUID + ".bounty", new_bounty);
    }

    ///////////////////////////

    public void addDefault() {
        Vars vars = Vars.getVars();
        FileConfiguration dataConfig = vars.dataConfig;
        if (dataConfig.get(pUID + ".kills") == null) {
            dataConfig.set(pUID + ".kills", 0);

            vars.saveDataConfig();
            vars.reloadDataConfig();
        }
        if (dataConfig.get(pUID + ".bounty") == null) {
            dataConfig.set(pUID + ".bounty", 0.0);

            vars.saveDataConfig();
            vars.reloadDataConfig();
        }
    }

    public void reset() {
        Vars vars = Vars.getVars();
        FileConfiguration dataConfig = vars.dataConfig;

        dataConfig.set(pUID + ".kills", 0);
        dataConfig.set(pUID + ".bounty", 0.0);

        vars.saveDataConfig();
        vars.reloadDataConfig();
    }

}

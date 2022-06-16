package cloud.stivenfocs.SimpleBountySystem;

import cloud.stivenfocs.SimpleBountySystem.Commands.simplebountysystem;
import net.milkbowl.vault.economy.Economy;
import org.bukkit.Bukkit;
import org.bukkit.plugin.RegisteredServiceProvider;
import org.bukkit.plugin.java.JavaPlugin;

public class Loader extends JavaPlugin {

    public void onEnable() {
        Vars.plugin = this;
        Vars vars = Vars.getVars();
        vars.reloadVars();

        getCommand("simplebountysystem").setExecutor(new simplebountysystem(this));
        getCommand("simplebountysystem").setTabCompleter(new simplebountysystem(this));

        Bukkit.getPluginManager().registerEvents(new GeneralEvents(this), this);

        if (Bukkit.getPluginManager().getPlugin("Vault") != null) {
            getLogger().info("Vault found and initialized, please make sure to install an economy plugin also.");
            setupEconomy();
        } else {
            getLogger().info("No vault found, all economy utilities has been disabled.");
        }

        if (Bukkit.getPluginManager().getPlugin("PlaceholderAPI") != null) {
            (new PAPIHook(this)).register();
        }
    }

    public void setupEconomy() {
        RegisteredServiceProvider<Economy> rsp = getServer().getServicesManager().getRegistration(Economy.class);
        if (rsp != null) {
            Vars.econ = rsp.getProvider();
        }
    }

}

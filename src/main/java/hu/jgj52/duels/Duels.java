package hu.jgj52.duels;

import hu.jgj52.duels.Commands.DuelCommand;
import org.bukkit.plugin.java.JavaPlugin;

public final class Duels extends JavaPlugin {

    public static Duels plugin;

    @Override
    public void onEnable() {
        // Plugin startup logic
        plugin = this;

        getConfig().options().copyDefaults(true);
        saveConfig();

        getCommand("duel").setExecutor(new DuelCommand());
    }

    @Override
    public void onDisable() {
        // Plugin shutdown logic
    }
}

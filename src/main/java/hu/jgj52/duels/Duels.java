package hu.jgj52.duels;

import hu.jgj52.duels.Commands.*;
import hu.jgj52.duels.Listeners.*;
import hu.jgj52.duels.Managers.ArenaManager;
import org.bukkit.Bukkit;
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
        getCommand("acceptduel").setExecutor(new AcceptDuelCommand());
        getCommand("kit").setExecutor(new KitCommand());
        getCommand("arena").setExecutor(new ArenaCommand());
        getCommand("editkit").setExecutor(new EditKitCommand());
        getCommand("forfeit").setExecutor(new ForfeitCommand());
        getCommand("leave").setExecutor(new LeaveCommand());

        getServer().getPluginManager().registerEvents(new DuelRequestListener(), this);
        getServer().getPluginManager().registerEvents(new KitListener(), this);
        getServer().getPluginManager().registerEvents(new ArenaListener(), this);
        getServer().getPluginManager().registerEvents(new DuelEndListener(), this);
        getServer().getPluginManager().registerEvents(new EditKitListener(), this);
        getServer().getPluginManager().registerEvents(new PlayerLeaveListener(), this);

        ArenaManager arenaManager = new ArenaManager();
        Bukkit.getScheduler().runTask(plugin, arenaManager::recreateWorld);
    }

    @Override
    public void onDisable() {
        // Plugin shutdown logic
    }
}

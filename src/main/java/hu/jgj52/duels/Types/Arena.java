package hu.jgj52.duels.Types;

import com.sk89q.worldguard.WorldGuard;
import com.sk89q.worldguard.protection.flags.Flag;
import com.sk89q.worldguard.protection.flags.StateFlag;
import hu.jgj52.duels.Managers.ArenaManager;
import org.bukkit.Location;
import org.bukkit.configuration.ConfigurationSection;

import java.util.*;

import static hu.jgj52.duels.Duels.plugin;

public class Arena {
    private final int id;
    private final Location loc;
    private final List<Region> regions = new ArrayList<>();
    private final int distance;
    private final int cooldown;

    public Arena (int kit, Location loc) {
        List<Integer> possible = plugin.getConfig().getIntegerList("data.kits." + kit + ".arenas");
        this.id = possible.get((int) (Math.random() * possible.size()));

        this.loc = loc;

        this.distance = plugin.getConfig().getInt("data.arenas." + id + ".distance");

        this.cooldown = plugin.getConfig().getInt("data.arenas." + id + ".cooldown");

        ConfigurationSection rgns = plugin.getConfig().getConfigurationSection("data.arenas." + id + ".regions");
        for (String rkey : rgns.getKeys(false)) {
            ConfigurationSection conf = plugin.getConfig().getConfigurationSection("data.arenas." + id + ".regions." + rkey + ".flags");
            Map<StateFlag, StateFlag.State> flags = new HashMap<>();
            for (String key : conf.getKeys(false)) {
                String value = conf.getString(key);

                Flag<?> rawFlag = WorldGuard.getInstance().getFlagRegistry().get(key.toLowerCase());
                if (!(rawFlag instanceof StateFlag flag)) continue;
                StateFlag.State state = StateFlag.State.valueOf(value);

                flags.put(flag, state);
            }

            Region region = new Region(
                    loc.getWorld(),
                    new Location(loc.getWorld(), plugin.getConfig().getDouble("data.arenas." + id + ".regions." + rkey + ".x1"), plugin.getConfig().getDouble("data.arenas." + id + ".regions." + rkey + ".y1"), plugin.getConfig().getDouble("data.arenas." + id + ".regions." + rkey + ".z1")),
                    new Location(loc.getWorld(), plugin.getConfig().getDouble("data.arenas." + id + ".regions." + rkey + ".x2"), plugin.getConfig().getDouble("data.arenas." + id + ".regions." + rkey + ".y2"), plugin.getConfig().getDouble("data.arenas." + id + ".regions." + rkey + ".z2")),
                    flags
            );

            regions.add(region);
        }
    }

    public String getName() {
        return plugin.getConfig().getString("data.arenas." + id + ".name");
    }

    public boolean place() {
        boolean doReturn = false;
        for (Region region : regions) {
            if (!region.place()) doReturn = true;
        }
        if (!new ArenaManager().placeArena(loc.getWorld(), loc.getBlockX(), loc.getBlockY(), loc.getBlockZ(), this.getName(), true)) doReturn = true;
        return !doReturn;
    }

    public List<Region> getRegions() {
        return regions;
    }

    public int getDistance() {
        return distance;
    }

    public int getCooldown() {
        return cooldown;
    }
}

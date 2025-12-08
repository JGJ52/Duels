package hu.jgj52.duels.Managers;

import com.sk89q.worldedit.EditSession;
import com.sk89q.worldedit.WorldEdit;
import com.sk89q.worldedit.bukkit.BukkitAdapter;
import com.sk89q.worldedit.extent.clipboard.Clipboard;
import com.sk89q.worldedit.extent.clipboard.io.ClipboardFormat;
import com.sk89q.worldedit.extent.clipboard.io.ClipboardFormats;
import com.sk89q.worldedit.extent.clipboard.io.ClipboardReader;
import com.sk89q.worldedit.function.operation.Operation;
import com.sk89q.worldedit.function.operation.Operations;
import com.sk89q.worldedit.math.BlockVector3;
import com.sk89q.worldedit.session.ClipboardHolder;
import com.sk89q.worldguard.WorldGuard;
import com.sk89q.worldguard.protection.flags.StateFlag;
import com.sk89q.worldguard.protection.managers.RegionManager;
import com.sk89q.worldguard.protection.managers.storage.StorageException;
import com.sk89q.worldguard.protection.regions.ProtectedCuboidRegion;
import com.sk89q.worldguard.protection.regions.ProtectedRegion;
import com.sk89q.worldguard.protection.regions.RegionContainer;
import hu.jgj52.duels.Utils.RuntimeVariables;
import org.bukkit.*;
import org.bukkit.configuration.ConfigurationSection;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Map;

import static hu.jgj52.duels.Duels.plugin;

public class ArenaManager {
    public boolean recreateWorld() {
        String worldName = "arenas";
        try {
            World world = Bukkit.getWorld(worldName);
            if (world != null) {
                RegionManager manager = WorldGuard.getInstance().getPlatform().getRegionContainer().get(BukkitAdapter.adapt(world));
                for (String regionId : manager.getRegions().keySet()) {
                    manager.removeRegion(regionId);
                }
                world.getPlayers().forEach(PlayerManager::tpToSpawn);
                boolean unloaded = Bukkit.unloadWorld(world, false);
                if (unloaded) {
                    File worldFolder = new File(Bukkit.getWorldContainer(), worldName);
                    if (worldFolder.exists()) {
                        deleteDirectory(worldFolder);
                    }
                } else {
                    plugin.getLogger().warning("Failed to unload arena world.");
                }
            }
            Bukkit.getScheduler().runTaskLater(plugin, () -> {
                WorldCreator creator = new WorldCreator(worldName)
                        .type(WorldType.FLAT)
                        .environment(World.Environment.NORMAL)
                        .generateStructures(false)
                        .generatorSettings("{\"layers\":[{\"block\":\"air\",\"height\":1}],\"biome\":\"plains\"}");
                World newWorld = creator.createWorld();
                if (newWorld != null) {
                    ConfigurationSection gamerules = plugin.getConfig().getConfigurationSection("arenasWorld.gamerules");
                    for (String gamerule : gamerules.getKeys(false)) {
                        GameRule<?> rule = GameRule.getByName(gamerule);
                        if (rule == null) continue;
                        Object value = gamerules.get(gamerule);
                        if (value == null) continue;
                        if (rule.getType() == Boolean.class) {
                            newWorld.setGameRule((GameRule<Boolean>) rule, (Boolean) value);
                        } else if (rule.getType() == Integer.class) {
                            newWorld.setGameRule((GameRule<Integer>) rule, (Integer) value);
                        }
                    }
                    newWorld.setSpawnFlags(false, false);
                    newWorld.setTime(plugin.getConfig().getInt("arenasWorld.time"));
                    newWorld.setStorm(plugin.getConfig().getBoolean("arenasWorld.storm"));
                    newWorld.setThundering(plugin.getConfig().getBoolean("arenasWorld.thundering"));
                    newWorld.setDifficulty(Difficulty.HARD);
                } else {
                    plugin.getLogger().severe("Failed to create arena world!");
                }
            }, 20L);
            RuntimeVariables.usedArenas = new ArrayList<>();
            return true;
        } catch (Exception e) {
            plugin.getLogger().severe("Error recreating arena world: " + e.getMessage());
            e.printStackTrace();
            return false;
        }
    }

    private boolean deleteDirectory(File directory) {
        if (directory.exists()) {
            File[] files = directory.listFiles();
            if (files != null) {
                for (File file : files) {
                    if (file.isDirectory()) {
                        deleteDirectory(file);
                    } else {
                        file.delete();
                    }
                }
            }
        }
        return directory.delete();
    }

    public boolean placeArena(World world, int x, int y, int z, String schematicName, boolean shouldIgnoreAir) {
        File schematicFile = new File(plugin.getDataFolder() + File.separator + "schematics", schematicName + ".schem");
        if (!schematicFile.exists()) {
            plugin.getLogger().warning("Schematic file not found: " + schematicFile.getAbsolutePath());
            return false;
        }

        ClipboardFormat format = ClipboardFormats.findByFile(schematicFile);
        if (format == null) {
            plugin.getLogger().warning("Unknown schematic format for file: " + schematicFile.getAbsolutePath());
            return false;
        }

        Clipboard clipboard;
        try (FileInputStream fis = new FileInputStream(schematicFile);
             ClipboardReader reader = format.getReader(fis)) {
            clipboard = reader.read();
        } catch (IOException e) {
            plugin.getLogger().severe("Error reading schematic file: " + e.getMessage());
            e.printStackTrace();
            return false;
        }

        try {
            com.sk89q.worldedit.world.World weWorld = BukkitAdapter.adapt(world);
            try (EditSession editSession = WorldEdit.getInstance().newEditSession(weWorld)) {
                Operation operation = new ClipboardHolder(clipboard)
                        .createPaste(editSession)
                        .to(BlockVector3.at(x, y, z))
                        .ignoreAirBlocks(shouldIgnoreAir)
                        .build();
                Operations.complete(operation);
                editSession.flushSession();
                return true;
            }
        } catch (Exception e) {
            plugin.getLogger().severe("Error placing schematic: " + e.getMessage());
            e.printStackTrace();
        }
        return false;
    }


    public boolean createRegion(World world, String regionName, Location loc1, Location loc2, Map<StateFlag, StateFlag.State> flags) {
        try {
            RegionContainer container = WorldGuard.getInstance().getPlatform().getRegionContainer();
            RegionManager regions = container.get(BukkitAdapter.adapt(world));
            if (regions == null) {
                plugin.getLogger().warning("No RegionManager found for world: " + world.getName());
                return false;
            }
            BlockVector3 min = BlockVector3.at(Math.min(loc1.getBlockX(), loc2.getBlockX()), Math.min(loc1.getBlockY(), loc2.getBlockY()), Math.min(loc1.getBlockZ(), loc2.getBlockZ()));
            BlockVector3 max = BlockVector3.at(Math.max(loc1.getBlockX(), loc2.getBlockX()), Math.max(loc1.getBlockY(), loc2.getBlockY()), Math.max(loc1.getBlockZ(), loc2.getBlockZ()));
            ProtectedRegion region = new ProtectedCuboidRegion(regionName, min, max);
            flags.forEach(region::setFlag);
            region.setPriority(1);
            regions.addRegion(region);
            regions.save();
            return true;
        } catch (StorageException e) {
            plugin.getLogger().severe("Failed to save WorldGuard regions: " + e.getMessage());
            e.printStackTrace();
            return false;
        } catch (Exception e) {
            plugin.getLogger().severe("Error creating WorldGuard region: " + e.getMessage());
            e.printStackTrace();
            return false;
        }
    }
}

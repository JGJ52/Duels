package hu.jgj52.duels.Commands;

import com.sk89q.worldedit.EmptyClipboardException;
import com.sk89q.worldedit.WorldEdit;
import com.sk89q.worldedit.bukkit.BukkitAdapter;
import com.sk89q.worldedit.extent.clipboard.io.ClipboardFormats;
import com.sk89q.worldedit.extent.clipboard.io.ClipboardWriter;
import com.sk89q.worldedit.session.ClipboardHolder;
import hu.jgj52.duels.Managers.MessageManager;
import hu.jgj52.duels.Utils.Replacer;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabCompleter;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import static hu.jgj52.duels.Duels.plugin;

public class ArenaCommand implements CommandExecutor, TabCompleter {
    @Override
    public boolean onCommand(@NotNull CommandSender sender, @NotNull Command command, @NotNull String label, @NotNull String @NotNull [] args) {
        if (!(sender instanceof Player player)) {
            sender.sendMessage(MessageManager.getMessage("youAreNotAPlayer"));
            return true;
        }

        if (args.length > 0) {
            switch (args[0]) {
                case "create":
                    if (player.hasPermission("duels.command.arena.create")) {
                        if (args.length > 1) {
                            String name = args[1];
                            try {
                                File folder = new File(plugin.getDataFolder(), "schematics");
                                if (!folder.exists()) folder.mkdirs();
                                File file = new File(folder, name.toLowerCase() + ".schem");
                                ClipboardHolder clipboard = WorldEdit.getInstance().getSessionManager().get(BukkitAdapter.adapt(player)).getClipboard();
                                try (FileOutputStream fos = new FileOutputStream(file)) {
                                    ClipboardWriter writer = ClipboardFormats.findByFile(file).getWriter(fos);
                                    writer.write(clipboard.getClipboard());
                                    List<Integer> ids = new ArrayList<>();
                                    ConfigurationSection section = plugin.getConfig().getConfigurationSection("data.arenas");
                                    for (String key : section.getKeys(false)) {
                                        ids.add(Integer.parseInt(key));
                                    }
                                    int id;
                                    if (ids.isEmpty()) id = 1; else id = Collections.max(ids) + 1;
                                    plugin.getConfig().set("data.arenas." + id + ".name", name.toLowerCase());
                                    player.sendMessage(Replacer.value(MessageManager.getMessage("arena.create.success"), name));
                                } catch (IOException e) {
                                    player.sendMessage(Replacer.value(MessageManager.getMessage("arena.create.failed"), name));
                                    throw new RuntimeException(e);
                                }
                            } catch (EmptyClipboardException e) {
                                player.sendMessage(Replacer.value(MessageManager.getMessage("arena.create.failed"), name));
                                throw new RuntimeException(e);
                            }
                        } else player.sendMessage("noArgs");
                    } else player.sendMessage(MessageManager.getMessage("noPerm"));
                    break;
                default:
                    player.sendMessage(MessageManager.getMessage("badArgs"));
            }
        } else player.sendMessage(MessageManager.getMessage("noArgs"));
        return true;
    }

    @Override
    public @Nullable List<String> onTabComplete(@NotNull CommandSender sender, @NotNull Command command, @NotNull String label, @NotNull String @NotNull [] args) {
        return List.of();
    }
}

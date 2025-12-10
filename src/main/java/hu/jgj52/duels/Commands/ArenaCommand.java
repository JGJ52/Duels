package hu.jgj52.duels.Commands;

import com.sk89q.worldedit.EmptyClipboardException;
import com.sk89q.worldedit.WorldEdit;
import com.sk89q.worldedit.bukkit.BukkitAdapter;
import com.sk89q.worldedit.extent.clipboard.io.ClipboardFormats;
import com.sk89q.worldedit.extent.clipboard.io.ClipboardWriter;
import com.sk89q.worldedit.session.ClipboardHolder;
import hu.jgj52.duels.GUIs.ArenaCreaterGUI;
import hu.jgj52.duels.Managers.MessageManager;
import hu.jgj52.duels.Utils.Replacer;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabCompleter;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
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
                            Inventory gui = Bukkit.createInventory(new ArenaCreaterGUI(args[1]), 27, Replacer.value(MessageManager.getMessage("arena.gui.title"), args[1]));

                            ItemStack outline = new ItemStack(Material.BLACK_STAINED_GLASS_PANE);
                            ItemMeta outlineMeta = outline.getItemMeta();
                            outlineMeta.setDisplayName(MessageManager.getMessage("arena.gui.outlineName"));
                            outline.setItemMeta(outlineMeta);

                            ItemStack inline = new ItemStack(Material.GRAY_STAINED_GLASS_PANE);
                            ItemMeta inlineMeta = inline.getItemMeta();
                            inlineMeta.setDisplayName(MessageManager.getMessage("arena.gui.inlineName"));
                            inline.setItemMeta(inlineMeta);

                            ItemStack save = new ItemStack(Material.LIME_CONCRETE);
                            ItemMeta saveMeta = save.getItemMeta();
                            saveMeta.setDisplayName(MessageManager.getMessage("arena.gui.saveName"));
                            save.setItemMeta(saveMeta);

                            ItemStack distance = new ItemStack(Material.BLACK_CONCRETE, 20);
                            ItemMeta distanceMeta = distance.getItemMeta();
                            distanceMeta.setDisplayName(MessageManager.getMessage("arena.gui.distanceName"));
                            distance.setItemMeta(distanceMeta);

                            ItemStack cooldown = new ItemStack(Material.WHITE_CONCRETE, 5);
                            ItemMeta cooldownMeta = cooldown.getItemMeta();
                            cooldownMeta.setDisplayName(MessageManager.getMessage("arena.gui.cooldownName"));
                            cooldown.setItemMeta(cooldownMeta);

                            for (int i = 0; i < 27; i++) {
                                if (i < 10 || (i > 16 && i < 26)) gui.setItem(i, outline);
                                if (List.of(10, 12, 13, 14, 16).contains(i)) gui.setItem(i, inline);
                                if (i == 11) gui.setItem(i, distance);
                                if (i == 15) gui.setItem(i, cooldown);
                                if (i == 26) gui.setItem(i, save);
                            }

                            player.openInventory(gui);
                        } else player.sendMessage(MessageManager.getMessage("noArgs"));
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
        if (args.length == 1) {
            List<String> complete = new ArrayList<>();
            if (sender.hasPermission("duels.command.arena.create")) complete.add("create");
            return complete;
        } else if (args.length == 2) {
            switch (args[1]) {
                case "create":
                    if (sender.hasPermission("duels.command.arena.create")) return List.of("<arenaName>"); else return List.of();
                default:
                    return List.of();
            }
        }
        return List.of();
    }
}

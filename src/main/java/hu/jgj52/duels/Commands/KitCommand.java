package hu.jgj52.duels.Commands;

import hu.jgj52.duels.GUIs.KitCreaterGUI;
import hu.jgj52.duels.Managers.MessageManager;
import hu.jgj52.duels.Types.Kit;
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

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import static hu.jgj52.duels.Duels.plugin;

public class KitCommand implements CommandExecutor, TabCompleter {
    @Override
    public boolean onCommand(@NotNull CommandSender sender, @NotNull Command command, @NotNull String label, @NotNull String @NotNull [] args) {
        if (!(sender instanceof Player player)) {
            sender.sendMessage(MessageManager.getMessage("youAreNotAPlayer"));
            return true;
        }

        if (args.length > 0) {
            switch (args[0]) {
                case "create":
                    if (player.hasPermission("duels.command.kit.create")) {
                        if (args.length > 1) {
                            List<Integer> ids = new ArrayList<>();
                            ConfigurationSection section = plugin.getConfig().getConfigurationSection("data.kits");
                            if (section == null) return false;
                            for (String id : section.getKeys(false)) {
                                ids.add(Integer.parseInt(id));
                            }
                            int id;
                            if (ids.isEmpty()) id = 1; else id = Collections.max(ids) + 1;
                            KitCreaterGUI kitCreaterGUI = new KitCreaterGUI(args[1], id, player.getInventory().getContents());
                            Inventory gui = Bukkit.createInventory(kitCreaterGUI, 27, Replacer.value(MessageManager.getMessage("kit.gui.title"), args[1]));

                            ItemStack outline = new ItemStack(Material.BLACK_STAINED_GLASS_PANE);
                            ItemMeta outlineMeta = outline.getItemMeta();
                            outlineMeta.setDisplayName(MessageManager.getMessage("kit.gui.outlineName"));
                            outline.setItemMeta(outlineMeta);

                            ItemStack inline = new ItemStack(Material.GRAY_STAINED_GLASS_PANE);
                            ItemMeta inlineMeta = inline.getItemMeta();
                            inlineMeta.setDisplayName(MessageManager.getMessage("kit.gui.inlineName"));
                            inline.setItemMeta(inlineMeta);

                            ItemStack maxHealth = new ItemStack(Material.RED_CONCRETE, 20);
                            ItemMeta maxHealthMeta = maxHealth.getItemMeta();
                            maxHealthMeta.setDisplayName(MessageManager.getMessage("kit.gui.maxHealthName"));
                            maxHealth.setItemMeta(maxHealthMeta);

                            ItemStack icon = new ItemStack(kitCreaterGUI.getPossible().get(kitCreaterGUI.getIndex()));
                            ItemMeta iconMeta = icon.getItemMeta();
                            iconMeta.setDisplayName(MessageManager.getMessage("kit.gui.iconName"));
                            icon.setItemMeta(iconMeta);

                            ItemStack arenas = new ItemStack(Material.BLACK_CONCRETE);
                            ItemMeta arenasMeta = arenas.getItemMeta();
                            arenasMeta.setDisplayName(MessageManager.getMessage("kit.gui.arenasName"));
                            arenas.setItemMeta(arenasMeta);

                            ItemStack save = new ItemStack(Material.LIME_CONCRETE);
                            ItemMeta saveMeta = save.getItemMeta();
                            saveMeta.setDisplayName(MessageManager.getMessage("kit.gui.saveName"));
                            save.setItemMeta(saveMeta);

                            for (int i = 0; i < 27; i++) {
                                if (i < 10 || (i > 16 && i < 26)) gui.setItem(i, outline);
                                if (i > 9 && i < 17 && !List.of(11, 13, 15).contains(i)) gui.setItem(i, inline);
                                if (i == 11) gui.setItem(i, maxHealth);
                                if (i == 13) gui.setItem(i, icon);
                                if (i == 15) gui.setItem(i, arenas);
                                if (i == 26) gui.setItem(i, save);
                            }

                            player.openInventory(gui);
                        } else player.sendMessage(MessageManager.getMessage("noArgs"));
                    } else player.sendMessage(MessageManager.getMessage("noPerm"));
                    break;
                case "load":
                    if (player.hasPermission("duels.command.kit.load")) {
                        if (args.length > 1) {
                            Kit kit = new Kit(args[1]);
                            player.getInventory().setContents(kit.getContents());
                            player.sendMessage(Replacer.value(MessageManager.getMessage("kit.loaded"), kit.getName()));
                        } else player.sendMessage(MessageManager.getMessage("noArgs"));
                    } else player.sendMessage(MessageManager.getMessage("noPerm"));
                    break;
                case "delete":
                    if (player.hasPermission("duels.command.kit.delete")) {
                        if (args.length > 1) {
                            Kit kit = new Kit(args[1]);
                            kit.delete();
                            player.sendMessage(Replacer.value(MessageManager.getMessage("kit.deleted"), kit.getName()));
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
            if (sender.hasPermission("duels.command.kit.create")) complete.add("create");
            if (sender.hasPermission("duels.command.kit.load")) complete.add("load");
            if (sender.hasPermission("duels.command.kit.delete")) complete.add("delete");
            return complete;
        } else if (args.length == 2) {
            switch (args[0]) {
                case "create":
                    if (sender.hasPermission("duels.command.kit.create")) return List.of("<kitName>"); else return List.of();
                case "load":
                    if (sender.hasPermission("duels.command.kit.load")) {
                        ConfigurationSection section = plugin.getConfig().getConfigurationSection("data.kits");
                        if (section == null) return List.of();
                        List<String> complete = new ArrayList<>();
                        for (String key : section.getKeys(false)) {
                            complete.add(plugin.getConfig().getString("data.kits." + key + ".name"));
                        }
                        return complete;
                    } else return List.of();
                case "delete":
                    if (sender.hasPermission("duels.command.kit.delete")) {
                        ConfigurationSection section = plugin.getConfig().getConfigurationSection("data.kits");
                        if (section == null) return List.of();
                        List<String> complete = new ArrayList<>();
                        for (String key : section.getKeys(false)) {
                            complete.add(plugin.getConfig().getString("data.kits." + key + ".name"));
                        }
                        return complete;
                    } else return List.of();
                default:
                    return List.of();
            }
        }
        return List.of();
    }
}

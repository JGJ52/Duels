package hu.jgj52.duels.Commands;

import hu.jgj52.duels.Managers.MessageManager;
import hu.jgj52.duels.Types.Kit;
import hu.jgj52.duels.Utils.Replacer;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabCompleter;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.entity.Player;
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
                        if (args.length > 2) {
                            List<Integer> ids = new ArrayList<>();
                            ConfigurationSection section = plugin.getConfig().getConfigurationSection("data.kits");
                            if (section == null) return false;
                            for (String id : section.getKeys(false)) {
                                ids.add(Integer.parseInt(id));
                            }
                            int id;
                            if (ids.isEmpty()) id = 1; else id = Collections.max(ids) + 1;
                            plugin.getConfig().set("data.kits." + id + ".content", player.getInventory().getContents());
                            plugin.getConfig().set("data.kits." + id + ".name", args[1]);
                            plugin.getConfig().set("data.kits." + id + ".maxHealth", Integer.parseInt(args[2]));
                            plugin.saveConfig();
                            plugin.reloadConfig();
                            player.sendMessage(Replacer.value(MessageManager.getMessage("kit.created"), args[1]));
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
            return List.of("create", "load", "delete");
        } else if (args.length == 2) {
            ConfigurationSection section = plugin.getConfig().getConfigurationSection("data.kits");
            if (section == null) return List.of();
            List<String> complete = new ArrayList<>();
            for (String key : section.getKeys(false)) {
                complete.add(plugin.getConfig().getString("data.kits." + key + ".name"));
            }
            return switch (args[0]) {
                case "create" -> List.of("<kitName>");
                case "load", "delete" -> complete;
                default -> List.of();
            };
        } else if (args.length == 3) {
            return switch (args[0]) {
                case "create" -> List.of("<maxHealth>");
                default -> List.of();
            };
        }
        return List.of();
    }
}

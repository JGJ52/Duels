package hu.jgj52.duels.Commands;

import hu.jgj52.duels.Handlers.EditKitHandler;
import hu.jgj52.duels.Managers.MessageManager;
import hu.jgj52.duels.Types.PlayerD;
import hu.jgj52.duels.Types.Kit;
import hu.jgj52.duels.Utils.RuntimeVariables;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabCompleter;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.ArrayList;
import java.util.List;

import static hu.jgj52.duels.Duels.plugin;

public class EditKitCommand extends MessageManager implements CommandExecutor, TabCompleter {
    @Override
    public boolean onCommand(@NotNull CommandSender sender, @NotNull Command command, @NotNull String label, @NotNull String @NotNull [] args) {
        if (!(sender instanceof Player bukkitPlayer)) {
            sender.sendMessage(getMessage("youAreNotAPlayer"));
            return true;
        }
        PlayerD player = new PlayerD(bukkitPlayer);
        if (args.length < 1) {
            player.sendMessage(getMessage("noArgs"));
            return true;
        }
        if (RuntimeVariables.duels.contains(player)) {
            player.sendMessage(getMessage("youAreInDuel"));
            return true;
        }
        return EditKitHandler.handle(player, new Kit(args[0]));
    }

    @Override
    public @Nullable List<String> onTabComplete(@NotNull CommandSender sender, @NotNull Command command, @NotNull String label, @NotNull String @NotNull [] args) {
        ConfigurationSection section = plugin.getConfig().getConfigurationSection("data.kits");
        List<String> kits = new ArrayList<>();
        for (String key : section.getKeys(false)) {
            Kit kit = new Kit(Integer.parseInt(key));
            kits.add(kit.getName());
        }
        return kits;
    }
}

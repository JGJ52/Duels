package hu.jgj52.duels.Commands;

import hu.jgj52.duels.Handlers.DuelRequestHandler;
import hu.jgj52.duels.Managers.MessageManager;
import hu.jgj52.duels.Utils.Replacer;
import hu.jgj52.duels.Utils.RuntimeVariables;
import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabCompleter;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.List;
import java.util.Map;

public class DuelCommand implements CommandExecutor, TabCompleter {
    @Override
    public boolean onCommand(@NotNull CommandSender sender, @NotNull Command command, @NotNull String s, @NotNull String @NotNull [] args) {
        if (!(sender instanceof Player player)) {
            sender.sendMessage(MessageManager.getMessage("youAreNotAPlayer"));
            return true;
        }

        if (args.length < 1) {
            player.sendMessage(MessageManager.getMessage("noArgs"));
            return true;
        }

        if (RuntimeVariables.isInDuel.get(player)) {
            player.sendMessage(MessageManager.getMessage("youAreInDuel"));
            return true;
        }

        Player enemy = Bukkit.getPlayer(args[0]);
        if (enemy == null) {
            player.sendMessage(Replacer.playerName(MessageManager.getMessage("noPlayer"), args[0]));
            return true;
        }

        if (RuntimeVariables.sentDuelRequests.get(Map.of(player, enemy)) != null) {
            player.sendMessage(Replacer.playerName(MessageManager.getMessage("alreadySentDuelRequest"), player));
            return true;
        }

        if (RuntimeVariables.isInDuel.get(enemy)) {
            player.sendMessage(Replacer.playerName(MessageManager.getMessage("enemyIsInDuel"), player));
            return true;
        }

        return DuelRequestHandler.duelRequest(player, enemy);
    }

    @Override
    public @Nullable List<String> onTabComplete(@NotNull CommandSender sender, @NotNull Command command, @NotNull String s, @NotNull String @NotNull [] args) {
        if (args.length == 1) {
            return null;
        }
        return List.of();
    }
}

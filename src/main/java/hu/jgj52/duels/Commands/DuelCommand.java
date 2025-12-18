package hu.jgj52.duels.Commands;

import hu.jgj52.duels.Handlers.DuelRequestHandler;
import hu.jgj52.duels.Managers.PlayerManager;
import hu.jgj52.duels.Types.PlayerD;
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

public class DuelCommand extends Replacer implements CommandExecutor, TabCompleter {
    @Override
    public boolean onCommand(@NotNull CommandSender sender, @NotNull Command command, @NotNull String s, @NotNull String @NotNull [] args) {
        if (!(sender instanceof Player bukkitPlayer)) {
            sender.sendMessage(getMessage("youAreNotAPlayer"));
            return true;
        }
        PlayerD player = PlayerManager.get(bukkitPlayer);

        if (args.length < 1) {
            player.sendMessage(getMessage("noArgs"));
            return true;
        }

        if (player.isInDuel()) {
            player.sendMessage(getMessage("youAreInDuel"));
            return true;
        }

        Player bukkitEnemy = Bukkit.getPlayer(args[0]);
        if (bukkitEnemy == null) {
            player.sendMessage(playerName(getMessage("noPlayer"), args[0]));
            return true;
        }
        PlayerD enemy = PlayerManager.get(bukkitEnemy);

        if (player == enemy) {
            player.sendMessage(getMessage("cantDuelYourself"));
            return true;
        }

        if (RuntimeVariables.sentDuelRequests.get(Map.of(player, enemy)) != null) {
            player.sendMessage(playerName(getMessage("alreadySentDuelRequest"), enemy));
            return true;
        }

        if (player.isInDuel()) {
            player.sendMessage(playerName(getMessage("enemyIsInDuel"), enemy));
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

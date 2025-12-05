package hu.jgj52.duels.Commands;

import hu.jgj52.database.Database;
import hu.jgj52.duels.Handlers.DuelRequestHandler;
import hu.jgj52.duels.Managers.MessageManager;
import hu.jgj52.duels.Utils.Replacer;
import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabCompleter;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.List;
import java.util.concurrent.atomic.AtomicBoolean;

public class DuelCommand implements CommandExecutor, TabCompleter {
    @Override
    public boolean onCommand(@NotNull CommandSender commandSender, @NotNull Command command, @NotNull String s, @NotNull String @NotNull [] strings) {
        if (!(commandSender instanceof Player player)) {
            commandSender.sendMessage(MessageManager.getMessage("youAreNotAPlayer"));
            return true;
        }

        Player enemy = Bukkit.getPlayer(strings[0]);
        if (enemy == null) {
            player.sendMessage(Replacer.playerName(MessageManager.getMessage("noPlayer"), strings[0]));
            return true;
        }

        AtomicBoolean shouldReturn = new AtomicBoolean(false);

        Database.asnycPostgreSQL(Database.postgres.from("duels_duel_requests").eq("player", player.getUniqueId()).eq("enemy", enemy.getUniqueId())).thenAccept(result -> {
            if (!result.isEmpty()) {
                player.sendMessage(Replacer.playerName(MessageManager.getMessage("alreadySentDuelRequest"), enemy));
                shouldReturn.set(true);
            }
        });

        Database.asnycPostgreSQL(Database.postgres.from("duels_duels").eq("player", enemy.getUniqueId())).thenAccept(result -> {
            if (result.isEmpty()) {
                Database.asnycPostgreSQL(Database.postgres.from("duels_duels").eq("enemy", enemy.getUniqueId())).thenAccept(queryResult -> {
                   if (!queryResult.isEmpty()) {
                       player.sendMessage(Replacer.playerName(MessageManager.getMessage("enemyInDuel"), enemy));
                       shouldReturn.set(true);
                   }
                });
            } else {
                player.sendMessage(Replacer.playerName(MessageManager.getMessage("enemyInDuel"), enemy));
                shouldReturn.set(true);
            }
        });

        if (shouldReturn.get()) {
            return true;
        }

        return DuelRequestHandler.duelRequest(player, enemy);
    }

    @Override
    public @Nullable List<String> onTabComplete(@NotNull CommandSender commandSender, @NotNull Command command, @NotNull String s, @NotNull String @NotNull [] strings) {
        if (strings.length == 1) {
            return null;
        }
        return List.of();
    }
}

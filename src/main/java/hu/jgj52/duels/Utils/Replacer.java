package hu.jgj52.duels.Utils;

import org.bukkit.entity.Player;

public class Replacer {
    public static String playerName(String message, Player player) {
        return message.replaceAll("%player_name%", player.getName());
    }
}

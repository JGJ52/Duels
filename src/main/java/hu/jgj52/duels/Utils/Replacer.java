package hu.jgj52.duels.Utils;

import org.bukkit.entity.Player;

public class Replacer {
    public static String playerName(String message, Player player) {
        return message.replaceAll("%player_name%", player.getName());
    }
    public static String playerName(String message, String player) {
        return message.replaceAll("%player_name%", player);
    }
    public static String value(String message, String value) {
        return message.replaceAll("%value%", value);
    }
}

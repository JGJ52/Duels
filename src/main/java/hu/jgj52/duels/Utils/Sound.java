package hu.jgj52.duels.Utils;

import hu.jgj52.duels.Types.PlayerD;

public class Sound {
    public static void open(PlayerD player) {
        player.playSound(player.getLocation(), org.bukkit.Sound.BLOCK_CHEST_OPEN, 1, 1);
    }

    public static void close(PlayerD player) {
        player.playSound(player.getLocation(), org.bukkit.Sound.BLOCK_CHEST_CLOSE, 1, 1);
    }

    public static void toggle(PlayerD player) {
        player.playSound(player.getLocation(), org.bukkit.Sound.BLOCK_BAMBOO_WOOD_BUTTON_CLICK_OFF, 1, 1);
    }

    public static void error(PlayerD player) {
        player.playSound(player.getLocation(), org.bukkit.Sound.ENTITY_VILLAGER_NO, 1, 1);
    }

    public static void success(PlayerD player) {
        player.playSound(player.getLocation(), org.bukkit.Sound.ENTITY_PLAYER_LEVELUP, 1, 1);
    }

    public static void wait(PlayerD player) {
        player.playSound(player.getLocation(), org.bukkit.Sound.BLOCK_NOTE_BLOCK_HAT, 1, 1);
    }

    public static void done(PlayerD player) {
        player.playSound(player.getLocation(), org.bukkit.Sound.BLOCK_NOTE_BLOCK_CHIME, 1, 1);
    }
}

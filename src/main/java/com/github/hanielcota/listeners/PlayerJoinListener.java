package com.github.hanielcota.listeners;

import com.github.hanielcota.AnkaresPlugin;
import com.github.hanielcota.utils.ItemBuilder;
import com.github.hanielcota.utils.LocationBuilder;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.inventory.ItemStack;

public class PlayerJoinListener implements Listener {
    private final AnkaresPlugin plugin;

    public PlayerJoinListener(AnkaresPlugin plugin) {
        this.plugin = plugin;
    }

    @EventHandler
    public void onPlayerJoin(PlayerJoinEvent event) {
        Player player = event.getPlayer();
        teleportPlayerToSpawn(player);
        addItemsToPlayer(player);

        event.setJoinMessage(null);
    }

    @EventHandler
    public void onPlayerPlayedBefore(PlayerJoinEvent event) {
        Player player = event.getPlayer();

        if (!player.hasPlayedBefore()) {
            addItemsToPlayer(player);
            teleportPlayerToSpawn(player);
        }
    }

    private void addItemsToPlayer(Player player) {
        player.getInventory().clear();

        ItemStack compass = new ItemBuilder(Material.COMPASS)
                .setName("§eSelecione o seu time")
                .setLore("§7Clique com botão direito do mouse para escolher o seu time.")
                .build();

        player.getInventory().setItem(4, compass);
    }

    private void teleportPlayerToSpawn(Player player) {
        String locationName = "spawn";
        LocationBuilder locationBuilder = new LocationBuilder(plugin);
        Location location = locationBuilder.getLocation(locationName);

        if (location == null) {
            player.sendMessage("§cLocalização '" + locationName + "' não encontrada.");
            return;
        }

        player.teleport(location);
        player.sendMessage("§eTeleportado para a localização '" + locationName + "'.");
    }
}

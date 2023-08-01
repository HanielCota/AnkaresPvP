package com.github.hanielcota.listeners;

import com.github.hanielcota.AnkaresPlugin;
import com.github.hanielcota.menus.SelectorTeamMenu;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

public class PlayerInteractListener implements Listener {

    private final AnkaresPlugin plugin;

    public PlayerInteractListener(AnkaresPlugin plugin) {
        this.plugin = plugin;
    }

    @EventHandler
    public void onPlayerInteract(PlayerInteractEvent event) {
        Player player = event.getPlayer();
        ItemStack item = player.getInventory().getItemInHand();

        if (item.getType() == Material.COMPASS) {
            ItemMeta meta = item.getItemMeta();
            if (meta != null && meta.getDisplayName().equals("§eSelecione o seu time")) {
                new SelectorTeamMenu(plugin, player);

                event.setCancelled(true);
            }
        }
    }

}

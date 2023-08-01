package com.github.hanielcota.utils.inventory;

import org.bukkit.Bukkit;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.inventory.InventoryCloseEvent;
import org.bukkit.event.inventory.InventoryOpenEvent;
import org.bukkit.event.server.PluginDisableEvent;
import org.bukkit.plugin.Plugin;

import java.util.HashSet;
import java.util.Set;

public final class CustomInventoryListener implements Listener {

    private static final Set<Plugin> REGISTERED_PLUGINS = new HashSet<>();

    private final Plugin plugin;

    public CustomInventoryListener(Plugin plugin) {
        this.plugin = plugin;
    }

    /**
     * Register listeners for CustomInventory.
     *
     * @param plugin plugin to register
     * @throws NullPointerException if plugin is null
     */
    public static void register(Plugin plugin) {
        if (REGISTERED_PLUGINS.contains(plugin)) {
            return; // Already registered
        }

        Bukkit.getPluginManager().registerEvents(new CustomInventoryListener(plugin), plugin);
        REGISTERED_PLUGINS.add(plugin);
    }

    /**
     * Close all open CustomInventory inventories.
     */
    public static void closeAll() {
        Bukkit.getOnlinePlayers().forEach(player -> {
            CustomInventory customInventory = (CustomInventory) player.getOpenInventory().getTopInventory().getHolder();
            if (customInventory != null) {
                player.closeInventory();
            }
        });
    }

    @EventHandler
    public void onInventoryClick(InventoryClickEvent e) {
        if (e.getInventory().getHolder() instanceof CustomInventory customInventory) {
            boolean wasCancelled = e.isCancelled();
            e.setCancelled(true);

            customInventory.handleClick(e);

            // This prevents un-canceling the event if another plugin canceled it before
            if (!wasCancelled && !e.isCancelled()) {
                e.setCancelled(false);
            }
        }
    }

    @EventHandler
    public void onInventoryOpen(InventoryOpenEvent e) {
        if (e.getInventory().getHolder() instanceof CustomInventory customInventory) {
            customInventory.handleOpen(e);
        }
    }

    @EventHandler
    public void onInventoryClose(InventoryCloseEvent e) {
        if (e.getInventory().getHolder() instanceof CustomInventory customInventory && (customInventory.handleClose(e))) {
            Bukkit.getScheduler().runTask(this.plugin, () -> customInventory.open(e.getPlayer()));

        }
    }


    @EventHandler
    public void onPluginDisable(PluginDisableEvent e) {
        if (REGISTERED_PLUGINS.contains(e.getPlugin())) {
            closeAll();
            REGISTERED_PLUGINS.remove(e.getPlugin());
        }
    }
}

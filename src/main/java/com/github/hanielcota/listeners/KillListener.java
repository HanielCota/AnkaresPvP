package com.github.hanielcota.listeners;

import com.github.hanielcota.managers.KillManager;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDeathEvent;
import org.bukkit.event.entity.PlayerDeathEvent;

public class KillListener implements Listener {

    private final KillManager killManager;

    public KillListener(KillManager killManager) {
        this.killManager = killManager;
    }

    @EventHandler
    public void onPlayerDeath(PlayerDeathEvent event) {
        Player killedPlayer = event.getEntity();
        Player killer = killedPlayer.getKiller();

        if (killer != null) {
            killManager.addKill(killer);
        }
    }

    @EventHandler
    public void onEntityDeath(EntityDeathEvent event) {
        Player killer = event.getEntity().getKiller();
        if (killer != null && event.getEntity().getType() == EntityType.PIG) {
            killManager.addKill(killer);
            killer.sendMessage("Voce matou um porco.");
        }
    }
}

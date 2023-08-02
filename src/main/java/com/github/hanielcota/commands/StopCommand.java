package com.github.hanielcota.commands;

import co.aikar.commands.BaseCommand;
import co.aikar.commands.annotation.CommandAlias;
import co.aikar.commands.annotation.Default;
import com.github.hanielcota.AnkaresPlugin;
import org.bukkit.Sound;
import org.bukkit.entity.Player;

@CommandAlias("acabar")
public class StopCommand extends BaseCommand {


    private final AnkaresPlugin plugin;

    public StopCommand(AnkaresPlugin plugin) {
        this.plugin = plugin;
    }

    @Default
    public void onCommand(Player player) {

        plugin.getGameStartManager().endGame(player);
        player.sendMessage("§eVocê forçou o fim do jogo!");
        player.playSound(player.getLocation(), Sound.LEVEL_UP, 10F, 10F);
    }
}

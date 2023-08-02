package com.github.hanielcota.commands;

import co.aikar.commands.BaseCommand;
import co.aikar.commands.annotation.CommandAlias;
import co.aikar.commands.annotation.Default;
import com.github.hanielcota.AnkaresPlugin;
import com.github.hanielcota.managers.GameStartManager;
import org.bukkit.Sound;
import org.bukkit.entity.Player;

@CommandAlias("start")
public class StartCommand extends BaseCommand {

    private final AnkaresPlugin plugin;

    public StartCommand(AnkaresPlugin plugin) {
        this.plugin = plugin;
    }

    @Default
    public void onCommand(Player player) {
        GameStartManager gameStartManager = plugin.getGameStartManager();

        if (gameStartManager.isGameStarted()) {
            player.sendMessage("§eO jogo já está iniciado.");
            return;
        }
        if (plugin.getTeamManager().getPlayerTeam(player) == null) {
            player.sendMessage("§eVocê não está em um time!");
            return;
        }

        gameStartManager.forceStartGame(player);
        player.sendMessage("§eVocê forçou o início do jogo!");
        player.playSound(player.getLocation(), Sound.LEVEL_UP, 10F, 10F);
    }

}

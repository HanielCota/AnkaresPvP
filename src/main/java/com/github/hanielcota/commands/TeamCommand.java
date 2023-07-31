package com.github.hanielcota.commands;

import co.aikar.commands.BaseCommand;
import co.aikar.commands.annotation.CommandAlias;
import co.aikar.commands.annotation.Default;
import com.github.hanielcota.AnkaresPlugin;
import org.bukkit.entity.Player;
import org.bukkit.scoreboard.Team;

@CommandAlias("team")
public class TeamCommand extends BaseCommand {

    private final AnkaresPlugin plugin;

    public TeamCommand(AnkaresPlugin plugin) {
        this.plugin = plugin;
    }

    @Default
    public void onCommand(Player player, String[] args) {
        if (args.length == 0) {
            Team playerTeam = plugin.getTeamManager().getPlayerTeam(player);

            if (playerTeam != null) {
                player.sendMessage("§eVocê está no time: " + playerTeam.getDisplayName());
                return;
            }

            player.sendMessage("§cVocê não está em nenhum time.");
        }
    }
}

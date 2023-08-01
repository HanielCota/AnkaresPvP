package com.github.hanielcota.commands;

import co.aikar.commands.BaseCommand;
import co.aikar.commands.annotation.*;
import com.github.hanielcota.AnkaresPlugin;
import org.bukkit.Bukkit;
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
            } else {
                player.sendMessage("§cVocê não está em nenhum time.");
            }
        }
    }

    @Subcommand("switch")
    @CommandCompletion("@Players")
    @CommandPermission("ankares.admin")
    public void onSwitchCommand(Player player, String[] args) {
        if (args.length < 2) {
            player.sendMessage("§cUso correto: /team switch <nick> <azul|vermelho>");
            return;
        }

        Player target = Bukkit.getPlayerExact(args[0]);
        if (target == null || !target.isOnline()) {
            player.sendMessage("§cJogador não encontrado ou não se encontra online.");
            return;
        }

        Team playerTeam = plugin.getTeamManager().getPlayerTeam(player);
        Team targetTeam = plugin.getTeamManager().getPlayerTeam(target);
        String teamName = args[1].toLowerCase();

        if (!teamName.equals("azul") && !teamName.equals("vermelho")) {
            player.sendMessage("§cTime inválido. Use 'azul' ou 'vermelho'.");
            return;
        }

        if (targetTeam == null || playerTeam == null || player.equals(target) || playerTeam.getName().equalsIgnoreCase(teamName)) {
            player.sendMessage("§cNão é possível mudar o jogador de time.");
            return;
        }

        plugin.getTeamManager().removePlayerFromTeams(target);
        if (teamName.equals("azul")) {
            plugin.getTeamManager().addPlayerToBlueTeam(target);
            player.sendMessage("§aO jogador " + target.getName() + " foi movido para o Time Azul.");
            target.sendMessage("§aVocê foi movido para o Time Azul por " + player.getName() + ".");
        } else {
            plugin.getTeamManager().addPlayerToRedTeam(target);
            player.sendMessage("§aO jogador " + target.getName() + " foi movido para o Time Vermelho.");
            target.sendMessage("§aVocê foi movido para o Time Vermelho por " + player.getName() + ".");
        }
    }
}

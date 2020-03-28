package fr.timothefcn.mc.loupgarou;

import fr.timothefcn.mc.loupgarou.classes.LGGame;
import fr.timothefcn.mc.loupgarou.classes.LGPlayer;
import fr.timothefcn.mc.loupgarou.roles.Role;
import fr.timothefcn.mc.loupgarou.utils.AutoRoles;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.event.player.PlayerJoinEvent;

import java.util.ArrayList;

public class LgCommands implements CommandExecutor {

    @Override
    public boolean onCommand(CommandSender commandSender, Command command, String label, String[] args) {
        if(command.getName().equalsIgnoreCase("create")) {
            if(!(commandSender instanceof Player)){ commandSender.sendMessage("Cette commande doit être executée par un joueur");return true; }
            Player p = (Player) commandSender;
            if(args.length != 2 || Integer.parseInt(args[1]) > 12) {p.sendMessage(ChatColor.RED + "Utilisation: /create <nom de la partie> <nombre de joueurs>"); return true; }
            String name = args[0].toLowerCase();
            if(MainLg.getInstance().getAllGames().containsKey(name)) {
                p.sendMessage(ChatColor.RED + "Une partie en cours existe déjà avec ce nom");
            }
            int nbPlayers = Integer.parseInt(args[1]);

            //Création de la partie
            int[] repartition = AutoRoles.Repartition(nbPlayers);
            MainLg.getInstance().getAllGames().put(name, new LGGame(p, nbPlayers));
            ArrayList<Role> roles = AutoRoles.getRandomRoles(repartition[0], repartition[1], MainLg.getInstance().getAllGames().get(name));
            MainLg.getInstance().getAllGames().get(name).setRoles(roles);
            p.sendMessage(ChatColor.GREEN + "" + ChatColor.BOLD + "La partie '" + name + "' a été crée.");
            p.sendMessage(ChatColor.GREEN + "" + ChatColor.BOLD + "Dis à tes amis d'utiliser la commande '/join " + name + "' pour rejoindre ta partie.");
            p.performCommand("join " + name);
        }

        if(command.getName().equalsIgnoreCase("join")) {
            if(!(commandSender instanceof Player)){ commandSender.sendMessage("Cette commande doit être executée par un joueur");return true; }
            Player p = (Player) commandSender;
            if(args.length != 1) {p.sendMessage(ChatColor.RED + "Utilisation: /join <nom de la partie>"); return true; }
            if(MainLg.getInstance().getAllGames().containsKey(args[0])) {
                if(LGPlayer.thePlayer(p).getGame() != null) {p.sendMessage(ChatColor.RED + "Tu es déjà en partie !"); return true; }
                Bukkit.getPluginManager().callEvent(new PlayerJoinEvent(p, ""));
                LGPlayer.thePlayer(p).join(MainLg.getInstance().getAllGames().get(args[0]));
            }
            else p.sendMessage(ChatColor.RED + "Cette partie n'existe pas.");
        }

        return true;
    }
}

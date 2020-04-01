package fr.timothefcn.mc.loupgarou;

import fr.timothefcn.mc.loupgarou.classes.LGGame;
import fr.timothefcn.mc.loupgarou.classes.LGPlayer;
import fr.timothefcn.mc.loupgarou.events.LGPlayerKilledEvent;
import fr.timothefcn.mc.loupgarou.roles.Role;
import fr.timothefcn.mc.loupgarou.utils.AutoRoles;
import fr.timothefcn.mc.loupgarou.utils.InventoryUtils;
import fr.timothefcn.mc.loupgarou.utils.PlayerUtils;
import fr.timothefcn.mc.loupgarou.utils.VariousUtils;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;

import java.util.ArrayList;

public class LgCommands implements CommandExecutor {

    @Override
    public boolean onCommand(CommandSender commandSender, Command command, String label, String[] args) {
        if (!(commandSender instanceof Player)) {
            commandSender.sendMessage("Cette commande doit être executée par un joueur");
            return true;
        }
        Player p = (Player) commandSender;
        LGPlayer lgp = LGPlayer.thePlayer(p);
        if (command.getName().equalsIgnoreCase("create")) {
            if (args.length < 2) {
                p.sendMessage(ChatColor.RED + "Utilisation: /create <nom de la partie> <auto/custom> [nombre de joueurs]");
                return true;
            }


            String name = args[0].toLowerCase();
            if (MainLg.getInstance().getAllGames().containsKey(name)) {
                p.sendMessage(ChatColor.RED + "Une partie en cours existe déjà avec ce nom");
                return true;
            }

            //Partie AUTO
            if (args[1].equalsIgnoreCase("auto")) {
                if (args.length != 3) {
                    p.sendMessage(ChatColor.RED + "Utilisation: /create <nom de la partie> auto <nombre de joueurs>");
                    return true;
                } else if (!(Integer.parseInt(args[2]) <= 12) && Integer.parseInt(args[2]) >= 3) {
                    p.sendMessage(ChatColor.RED + "Une partie peut compter enree 3 et 12 joueurs pour le moment.");
                    return true;
                } else {
                    int nbPlayers = Integer.parseInt(args[2]);
                    int[] repartition = AutoRoles.Repartition(nbPlayers);
                    MainLg.getInstance().getAllGames().put(name, new LGGame(p, nbPlayers, name)); //Create game
                    ArrayList<Role> roles = AutoRoles.getRandomRoles(repartition[0], repartition[1], MainLg.getInstance().getAllGames().get(name));
                    MainLg.getInstance().getAllGames().get(name).setRoles(roles);
                    p.performCommand("join " + name);
                }
            }

            //Partie CUSTOM
            else if (args[1].equalsIgnoreCase("custom")) {
                MainLg.getInstance().getAllGames().put(name, new LGGame(p, 7, name)); //Create game avec 7 joueurs par défaut
                //Créer l'inventaire de séléction
                p.performCommand("join " + name);
                Inventory gui = InventoryUtils.roleSelector(LGPlayer.thePlayer(p).getGame());
                p.openInventory(gui);
            } else {
                p.sendMessage(ChatColor.RED + "Il faut choisir un mode de répartition des roles: auto ou custom");
                p.sendMessage(ChatColor.RED + "Utilisation: /create <nom de la partie> <nombre de joueurs> <auto/custom>");
                return true;
            }

            p.sendMessage(ChatColor.GREEN + "" + ChatColor.BOLD + "La partie '" + name + "' a été crée.");
            p.sendMessage(ChatColor.GREEN + "" + ChatColor.BOLD + "Dis à tes amis d'utiliser la commande '/join " + name + "' pour rejoindre ta partie.");
        }

        if (command.getName().equalsIgnoreCase("join")) {
            if (args.length != 1) {
                p.sendMessage(ChatColor.RED + "Utilisation: /join <nom de la partie>");
                return true;
            }
            if (MainLg.getInstance().getAllGames().containsKey(args[0])) {
                if (LGPlayer.thePlayer(p).getGame() != null) {
                    p.sendMessage(ChatColor.RED + "Tu es déjà en partie !");
                    return true;
                }
                //  Bukkit.getPluginManager().callEvent(new PlayerJoinEvent(p, ""));
                LGPlayer.thePlayer(p).join(MainLg.getInstance().getAllGames().get(args[0]));
            } else p.sendMessage(ChatColor.RED + "Cette partie n'existe pas.");
        }

        if (command.getName().equalsIgnoreCase("leave")) {
            if (args.length != 0) {
                p.sendMessage(ChatColor.RED + "Utilisation: /leave");
                return true;
            }
            if (LGPlayer.thePlayer(p).getGame() != null) {
                LGGame game = LGPlayer.thePlayer(p).getGame();
                lgp.leaveChat();
                if (lgp.getRole() != null && !lgp.isDead())
                    lgp.getGame().kill(lgp, LGPlayerKilledEvent.Reason.DISCONNECTED, true);
                lgp.getGame().getInGame().remove(lgp);
                lgp.getGame().checkLeave();
                LGPlayer.removePlayer(p);
                lgp.remove();

                if (!(game.getInGame().size() > 0)) VariousUtils.removeGame(game.getGameName());
                PlayerUtils.resetPlayerState(p);
                for (Player online : Bukkit.getOnlinePlayers()) {
                    PlayerUtils.updatePlayerHide(online);
                }
                p.sendMessage(ChatColor.RED + "Tu as quitté ta partie");
            } else p.sendMessage(ChatColor.RED + "Tu n'es pas dans une partie");
        }

        if (command.getName().equalsIgnoreCase("bypass")) {
            if (p.hasPermission("lg.build")) {
                if (MainLg.getInstance().getBypass().contains(p))
                    MainLg.getInstance().getBypass().remove(p);
                else MainLg.getInstance().getBypass().add(p);
            } else p.sendMessage(ChatColor.RED + "Tu n'as pas la permission d'executer cette commande");
        }

        return true;
    }
}

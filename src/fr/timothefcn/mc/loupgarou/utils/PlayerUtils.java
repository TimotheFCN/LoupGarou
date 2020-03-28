package fr.timothefcn.mc.loupgarou.utils;

import fr.timothefcn.mc.loupgarou.MainLg;
import fr.timothefcn.mc.loupgarou.classes.LGPlayer;
import org.bukkit.Bukkit;
import org.bukkit.GameMode;
import org.bukkit.entity.Player;
import org.bukkit.potion.PotionEffect;

public class PlayerUtils {

    public static  void resetPlayerState(Player p) {
        p.setWalkSpeed(0.2f);
        p.setHealth(20);
        p.setFoodLevel(20);
        p.setTotalExperience(0);
        p.getInventory().clear();
        VariousUtils.setWarning(p, false);
        for(PotionEffect effect : p.getActivePotionEffects())
            p.removePotionEffect(effect.getType());
    }

    public static void updatePlayerHide(Player p) {
        LGPlayer lgp = LGPlayer.thePlayer(p);
        if (lgp.getGame() != null) hideEveryone(p);
        else showEveryone(p);
    }

    //Utiliser si joueur pas ou plus en partie
    private static void showEveryone(Player p) { //Affiche tout le monde sauf les joueurs en partie
        if (p.getGameMode().equals(GameMode.SPECTATOR)) return;
        for (Player player : Bukkit.getOnlinePlayers()) {
            if (LGPlayer.thePlayer(p).getGame() == null) p.showPlayer(player);
            if (player != p && LGPlayer.thePlayer(player).getGame() != null) p.hidePlayer(player);
        }
    }

    //Utiliser si joueur en partie
    private static void hideEveryone(Player p) { //Masque tout le monde sauf les joueurs dans la même partie et pas démarrée (évite de réafficher les loups la nuit par exemple)
        for (Player player : Bukkit.getOnlinePlayers()) {
            if (player != p && LGPlayer.thePlayer(player).getGame() != LGPlayer.thePlayer(p).getGame()) p.hidePlayer(player);
            if (LGPlayer.thePlayer(player).getGame() == LGPlayer.thePlayer(p).getGame() && !LGPlayer.thePlayer(p).getGame().isStarted()) p.showPlayer(player);
        }

    }

    public static void sendRessourcePack(Player p) {
        String url = MainLg.getInstance().getConfig().getString("ressourcePack");
        p.setResourcePack(url);
    }
    public static void resetRessourcePack(Player p) {
        p.setResourcePack("https://github.com/Phoenix616/ResourcepacksPlugins/blob/master/Empty.zip?raw=true");
    }

}

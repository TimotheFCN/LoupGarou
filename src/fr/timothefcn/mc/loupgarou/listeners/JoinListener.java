package fr.timothefcn.mc.loupgarou.listeners;

import com.comphenix.protocol.wrappers.WrappedChatComponent;
import fr.timothefcn.mc.com.comphenix.packetwrapper.WrapperPlayServerScoreboardTeam;
import fr.timothefcn.mc.loupgarou.MainLg;
import fr.timothefcn.mc.loupgarou.classes.LGPlayer;
import fr.timothefcn.mc.loupgarou.events.LGPlayerKilledEvent.Reason;
import fr.timothefcn.mc.loupgarou.utils.PlayerUtils;
import org.bukkit.Bukkit;
import org.bukkit.GameMode;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerQuitEvent;
import org.bukkit.event.player.PlayerResourcePackStatusEvent;
import org.bukkit.event.player.PlayerResourcePackStatusEvent.Status;

import java.util.Arrays;

public class JoinListener implements Listener {

    @EventHandler
    public void onJoin(PlayerJoinEvent e) { //TODO: Possiblement à modifier
        Player p = e.getPlayer();
        if(!(p.getGameMode().equals(GameMode.SPECTATOR))) p.setGameMode(GameMode.ADVENTURE);
        LGPlayer.thePlayer(e.getPlayer()).setGame(null); //Au cas où un crash serveur
        PlayerUtils.resetPlayerState(p);
        WrapperPlayServerScoreboardTeam myTeam = new WrapperPlayServerScoreboardTeam();
        myTeam.setName(p.getName());
        myTeam.setPrefix(WrappedChatComponent.fromText(""));
        myTeam.setPlayers(Arrays.asList(p.getName()));
        myTeam.setMode(0);

        LGPlayer.thePlayer(e.getPlayer()).showView(); //TODO: Eviter le doublon
        for (Player online : Bukkit.getOnlinePlayers())
            PlayerUtils.updatePlayerHide(online);

        PlayerUtils.sendRessourcePack(p);

    /*   boolean noSpec = p.getGameMode() != GameMode.SPECTATOR;
        for (Player player : Bukkit.getOnlinePlayers())
            if (player != p) {
                if (player.getGameMode() != GameMode.SPECTATOR)
                    player.hidePlayer(p);
                WrapperPlayServerScoreboardTeam team = new WrapperPlayServerScoreboardTeam();
                team.setName(player.getName());
                team.setPrefix(WrappedChatComponent.fromText(""));
                team.setPlayers(Arrays.asList(player.getName()));
                team.setMode(0);
                team.sendPacket(p);
                myTeam.sendPacket(player);
            }
        p.setFoodLevel(20);
        p.setHealth(20);

        if (e.getJoinMessage() == null || !e.getJoinMessage().equals("joinall")) {}
        //    p.getPlayer().setResourcePack("http://leomelki.fr/mcgames/ressourcepacks/v32/loup_garou.zip");
        else {
            LGPlayer lgp = LGPlayer.thePlayer(e.getPlayer());
            lgp.showView();
        //    lgp.join(MainLg.getInstance().getCurrentGame());
        }
        if (noSpec)
            p.setGameMode(GameMode.ADVENTURE);
        e.setJoinMessage("");
        p.removePotionEffect(PotionEffectType.JUMP);
        p.removePotionEffect(PotionEffectType.INVISIBILITY);
        p.setWalkSpeed(0.2f);*/
    }

    @EventHandler
    public void onResoucePack(PlayerResourcePackStatusEvent e) {
        if (e.getStatus() == Status.SUCCESSFULLY_LOADED) {
            Player p = e.getPlayer();
            LGPlayer lgp = LGPlayer.thePlayer(p);
            lgp.showView();
       //     lgp.join(MainLg.getInstance().getCurrentGame());
        } else if (e.getStatus() == Status.DECLINED || e.getStatus() == Status.FAILED_DOWNLOAD)
            e.getPlayer().kickPlayer(MainLg.getPrefix() + "§cIl vous faut le resourcepack pour jouer ! (" + e.getStatus() + ")");
    }

    @EventHandler
    public void onLeave(PlayerQuitEvent e) {
        Player p = e.getPlayer();
        LGPlayer lgp = LGPlayer.thePlayer(p);
        PlayerUtils.resetRessourcePack(p);
        if (lgp.getGame() != null) {
            lgp.leaveChat();
            if (lgp.getRole() != null && !lgp.isDead())
                lgp.getGame().kill(lgp, Reason.DISCONNECTED, true);
            lgp.getGame().getInGame().remove(lgp);
            lgp.getGame().checkLeave();
        }
        LGPlayer.removePlayer(p);
        lgp.remove();
    }

}

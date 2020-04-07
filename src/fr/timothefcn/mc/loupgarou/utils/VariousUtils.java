package fr.timothefcn.mc.loupgarou.utils;

import com.comphenix.protocol.PacketType;
import com.comphenix.protocol.ProtocolLibrary;
import com.comphenix.protocol.events.PacketContainer;
import com.comphenix.protocol.wrappers.EnumWrappers;
import fr.timothefcn.mc.com.comphenix.packetwrapper.WrapperPlayServerEntityDestroy;
import fr.timothefcn.mc.loupgarou.MainLg;
import fr.timothefcn.mc.loupgarou.classes.LGGame;
import org.bukkit.Location;
import org.bukkit.WorldBorder;
import org.bukkit.entity.Player;

import java.lang.reflect.InvocationTargetException;
import java.util.ArrayList;

public class VariousUtils {
    private static char[] hex = "0123456789abcdef".toCharArray();

    public static double distanceSquaredXZ(Location from, Location to) {
        return Math.pow(from.getX() - to.getX(), 2) + Math.pow(from.getZ() - to.getZ(), 2);
    }

    public static void setWarning(Player p, boolean warning) {
        PacketContainer container = new PacketContainer(PacketType.Play.Server.WORLD_BORDER);
        WorldBorder wb = p.getWorld().getWorldBorder();

        container.getWorldBorderActions().write(0, EnumWrappers.WorldBorderAction.INITIALIZE);

        container.getIntegers().write(0, 29999984);

        container.getDoubles().write(0, p.getLocation().getX());
        container.getDoubles().write(1, p.getLocation().getZ());

        container.getDoubles().write(3, wb.getSize());
        container.getDoubles().write(2, wb.getSize());

        container.getIntegers().write(2, (int) (warning ? wb.getSize() : wb.getWarningDistance()));
        container.getIntegers().write(1, 0);

        container.getLongs().write(0, (long) 0);

        try {
            ProtocolLibrary.getProtocolManager().sendServerPacket(p, container);
        } catch (InvocationTargetException e) {
            e.printStackTrace();
        }
    }

    public static char toHex(int i) {
        return hex[i];
    }

    public static void removeGame(String game) {
        MainLg.getInstance().getAllGames().remove(game, MainLg.getInstance().getAllGames().get(game));
        System.gc();
    }

    public static ArrayList<String> checkGameSettings(LGGame game) {
        ArrayList<String> result = new ArrayList<>();
        if (game.getWolves() < 1) result.add("La partie nécessite au moins un loup.");
        if (game.getMaxPlayers() > 12) result.add("La partie peut accepter jusqu'à 12 joueurs.");
        if (game.getMaxPlayers() < 3) result.add("La partie nécessite au moins 3 joueurs.");
        if (game.getWolves() >= game.getMaxPlayers() - game.getWolves())
            result.add("Il doit y avoir plus de membres du village que de loups.");

        return result;
    }

    public static void clearvotes(Player player, LGGame game) {
        WrapperPlayServerEntityDestroy destroy = new WrapperPlayServerEntityDestroy();
        destroy.setEntityIds(new int[]{Integer.MIN_VALUE + player.getEntityId()});
        int[] ids = new int[game.getInGame().size() + 1];
        for (int i = 0; i < game.getInGame().size(); i++) {
            Player l = game.getInGame().get(i).getPlayer();
            if (l == null)
                continue;
            ids[i] = Integer.MIN_VALUE + l.getEntityId();
            destroy.sendPacket(l);
        }

        ids[ids.length - 1] = -player.getEntityId();// Clear voting

        destroy = new WrapperPlayServerEntityDestroy();
        destroy.setEntityIds(ids);
        destroy.sendPacket(player);

    }

}

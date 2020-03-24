package fr.timothefcn.mc.loupgarou.roles;

import fr.timothefcn.mc.loupgarou.classes.LGGame;
import fr.timothefcn.mc.loupgarou.classes.LGPlayer;
import fr.timothefcn.mc.loupgarou.events.LGRoleTurnEndEvent;
import org.bukkit.event.EventHandler;

import java.util.Arrays;
import java.util.List;

public class RPetiteFille extends Role {
    List<String> customNames = Arrays.asList("Loup Glouton", "Loup Méchant", "Loup Burlesque", "Loup Peureux", "Loup Malingre", "Loup Gentil", "Loup Tueur", "Loup Énervé", "Loup Docteur");

    public RPetiteFille(LGGame game) {
        super(game);
    }

    @Override
    public RoleType getType() {
        return RoleType.VILLAGER;
    }

    @Override
    public RoleWinType getWinType() {
        return RoleWinType.VILLAGE;
    }

    @Override
    public String getName() {
        return "§a§lPetite Fille";
    }

    @Override
    public String getFriendlyName() {
        return "de la " + getName();
    }

    @Override
    public String getShortDescription() {
        return "Tu gagnes avec le §a§lVillage";
    }

    @Override
    public String getDescription() {
        return "Tu gagnes avec le §a§lVillage§f. Chaque nuit, tu peux espionner les §c§lLoups§f.";
    }

    @Override
    public String getTask() {
        return "";
    }

    @Override
    public String getBroadcastedTask() {
        return "";
    }

    @Override
    public int getTimeout() {
        return -1;
    }

    @EventHandler
    public void onChangeRole(LGRoleTurnEndEvent e) {
        if (e.getGame() == getGame()) {
            if (e.getNewRole() instanceof RLoupGarou)
                for (Role role : getGame().getRoles())
                    if (role instanceof RLoupGarou) {
                        RLoupGarou lgRole = (RLoupGarou) role;
                        for (LGPlayer player : getPlayers())
                            if (!player.getCache().getBoolean("infected"))
                                player.joinChat(lgRole.getChat(), (sender, message) -> {
                                    return "§c" + customNames.get(lgRole.getPlayers().indexOf(sender)) + " §6» §f" + message;
                                }, true);
                        break;
                    }
            if (e.getPreviousRole() instanceof RLoupGarou)
                for (LGPlayer player : getPlayers())
                    if (!player.getCache().getBoolean("infected"))
                        player.leaveChat();
        }
    }
}
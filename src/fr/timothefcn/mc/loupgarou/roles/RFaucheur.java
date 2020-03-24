package fr.timothefcn.mc.loupgarou.roles;

import fr.timothefcn.mc.loupgarou.MainLg;
import fr.timothefcn.mc.loupgarou.classes.LGGame;
import fr.timothefcn.mc.loupgarou.classes.LGPlayer;
import fr.timothefcn.mc.loupgarou.events.LGPlayerKilledEvent;
import org.bukkit.Bukkit;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;

import java.util.List;
import java.util.Random;

public class RFaucheur extends Role {
    private static Random random = new Random();

    public RFaucheur(LGGame game) {
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
        return "§a§lFaucheur";
    }

    @Override
    public String getFriendlyName() {
        return "du " + getName();
    }

    @Override
    public String getShortDescription() {
        return "Tu gagnes avec le §a§lVillage";
    }

    @Override
    public String getDescription() {
        return "Tu gagnes avec le §a§lVillage§f. Si les §c§lLoups-Garous§f te tuent pendant la nuit, tu emporteras l’un d’entre eux dans ta mort, mais si tu meurs lors du vote du §a§lvillage§f, ce sont tes deux voisins qui en paieront le prix.";
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

    @EventHandler(priority = EventPriority.LOWEST)
    public void onKill(LGPlayerKilledEvent e) {
        if (e.getKilled().getRole() == this) {
            LGPlayer killed = e.getKilled();
            if (killed.getCache().getBoolean("faucheur_did"))//A déjà fait son coup de faucheur !
                return;
            killed.getCache().set("faucheur_did", true);
            if (e.getReason() == LGPlayerKilledEvent.Reason.LOUP_GAROU || e.getReason() == LGPlayerKilledEvent.Reason.GM_LOUP_GAROU) {//car le switch buggait (wtf)
                // Mort par les LG
                // Tue un lg au hasard
                LGPlayer selected = null;
                for (Role role : getGame().getRoles())
                    if (role instanceof RLoupGarou)
                        selected = role.getPlayers().get(random.nextInt(role.getPlayers().size()));
                if (selected != null) {
                    LGPlayerKilledEvent killEvent = new LGPlayerKilledEvent(getGame(), e.getKilled(), e.getReason());
                    Bukkit.getPluginManager().callEvent(killEvent);
                    e.setKilled(selected);
                    e.setReason(LGPlayerKilledEvent.Reason.FAUCHEUR);
                    if (killEvent.isCancelled())
                        return;
                    getGame().kill(killEvent.getKilled(), killEvent.getReason(), false);
                }
            } else if (e.getReason() == LGPlayerKilledEvent.Reason.VOTE) {
                List<?> original = MainLg.getInstance().getConfig().getList("spawns");
                int size = original.size();
                // double middle = ((double)size)/2D;
                int killedPlace = killed.getPlace();

                LGPlayer droite = null, gauche = null;
                for (int i = killedPlace + 1; ; i++) {
                    if (i == size)
                        i = 0;
                    LGPlayer lgp = getGame().getPlacements().get(i);
                    if (lgp != null && !lgp.isDead()) {
                        droite = lgp;
                        break;
                    }
                    if (lgp == killed)// Fait un tour complet
                        break;
                }
                for (int i = killedPlace - 1; ; i--) {
                    if (i == -1)
                        i = size - 1;
                    LGPlayer lgp = getGame().getPlacements().get(i);
                    if (lgp != null && !lgp.isDead()) {
                        gauche = lgp;
                        break;
                    }
                    if (lgp == killed)// Fait un tour complet
                        break;
                }
                if (droite != null) {
                    LGPlayerKilledEvent killEvent = new LGPlayerKilledEvent(getGame(), e.getKilled(), e.getReason());
                    Bukkit.getPluginManager().callEvent(killEvent);

                    e.setKilled(droite);
                    e.setReason(LGPlayerKilledEvent.Reason.FAUCHEUR);

                    if (!killEvent.isCancelled())
                        getGame().kill(killEvent.getKilled(), killEvent.getReason(), false);
                }
                if (gauche != null) {
                    LGPlayerKilledEvent killEvent;
                    if (droite == null) {
                        killEvent = new LGPlayerKilledEvent(getGame(), e.getKilled(), e.getReason());
                        e.setKilled(gauche);
                        e.setReason(LGPlayerKilledEvent.Reason.FAUCHEUR);
                    } else
                        killEvent = new LGPlayerKilledEvent(getGame(), gauche, LGPlayerKilledEvent.Reason.FAUCHEUR);
                    Bukkit.getPluginManager().callEvent(killEvent);
                    if (!killEvent.isCancelled())
                        getGame().kill(killEvent.getKilled(), killEvent.getReason(), false);
                }
            }
        }
    }
}

package fr.timothefcn.mc.loupgarou.listeners;

import fr.timothefcn.mc.loupgarou.MainLg;
import fr.timothefcn.mc.loupgarou.classes.LGGame;
import fr.timothefcn.mc.loupgarou.classes.LGPlayer;
import fr.timothefcn.mc.loupgarou.roles.Role;
import fr.timothefcn.mc.loupgarou.utils.InventoryUtils;
import fr.timothefcn.mc.loupgarou.utils.ItemBuilder;
import fr.timothefcn.mc.loupgarou.utils.RolesInstancer;
import lombok.Getter;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;

import java.lang.reflect.Constructor;
import java.util.HashMap;

import static org.bukkit.Bukkit.broadcastMessage;

public class RoleInterfaceListener implements Listener {

    @Getter
    private HashMap<String, Constructor<? extends Role>> roles = MainLg.getInstance().getRoles();

    //TODO: Permettre au créateur de la partie de modifier les roles après l'avoir créée

    @EventHandler
    public void onInteract(PlayerInteractEvent e) {
        int index = 0;
        ItemStack item = e.getItem();
        if (item == null) return;

        if (item.getType().equals(Material.MAGMA_CREAM)) {
            Player p = e.getPlayer();
            Inventory gui = InventoryUtils.roleSelector(LGPlayer.thePlayer(p).getGame());
            p.openInventory(gui);
        }

        if (item.getType().equals(Material.EMERALD)) {
            RolesInstancer.createRoleInstances(LGPlayer.thePlayer(e.getPlayer()).getGame());
            if (MainLg.getInstance().getConfig().getList("spawns").size() < LGPlayer.thePlayer(e.getPlayer()).getGame().getMaxPlayers()) {
                broadcastMessage("§4Erreur : §cIl n'y a pas assez de points de spawn !");
            }
            LGPlayer.thePlayer(e.getPlayer()).getGame().updateStart();
        }
    }

    @EventHandler
    public void onInventoryClick(InventoryClickEvent e) {
        if (e.getCurrentItem() == null) return;
        Player p = (Player) e.getWhoClicked();
        if (e.getView().getTitle().equals("Rôles")) {
            if (LGPlayer.thePlayer(p).getGame() == null) return;
            LGGame game = LGPlayer.thePlayer(p).getGame();

            if (e.getCurrentItem().getType() == Material.GOLD_NUGGET) {
                p.closeInventory();
                //TODO: ajouter les sécurités de répartition des roles
                p.getInventory().setItem(5, new ItemBuilder(Material.EMERALD).setName("Lancer la partie").build());
                p.getInventory().setItem(3, new ItemBuilder(Material.MAGMA_CREAM).setName("Choisir les rôles").build());

            } else if (e.isLeftClick()) {
                for (String role : getRoles().keySet()) {
                    if (role.equals(ChatColor.stripColor(e.getCurrentItem().getItemMeta().getDisplayName()))) {
                        if (role.equalsIgnoreCase("LoupGarou")) game.setWolves(game.getWolves() + 1);
                        else if (role.equalsIgnoreCase("Villageois")) game.setVillagers(game.getVillagers() + 1);
                        else if (!game.getRolestoconfig().contains(role)) game.getRolestoconfig().add(role);
                        game.setMaxPlayers(game.rolestoconfig.size() + game.getVillagers() + game.getWolves());
                        p.openInventory(InventoryUtils.roleSelector(LGPlayer.thePlayer(p).getGame()));
                        return;
                    }
                }
            } else if (e.isRightClick()) {
                for (String role : getRoles().keySet()) {
                    if (role.equals(ChatColor.stripColor(e.getCurrentItem().getItemMeta().getDisplayName()))) {
                        if (role.equalsIgnoreCase("LoupGarou") && game.getWolves() > 0)
                            game.setWolves(game.getWolves() - 1);
                        else if (role.equalsIgnoreCase("Villageois") && game.getVillagers() > 0)
                            game.setVillagers(game.getVillagers() - 1);
                        else game.getRolestoconfig().remove(role);
                        game.setMaxPlayers(game.rolestoconfig.size() + game.getVillagers() + game.getWolves());
                        p.openInventory(InventoryUtils.roleSelector(LGPlayer.thePlayer(p).getGame()));
                        return;
                    }
                }
            }
        }
    }
}
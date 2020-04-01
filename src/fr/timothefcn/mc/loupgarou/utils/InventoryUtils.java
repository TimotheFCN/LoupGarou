package fr.timothefcn.mc.loupgarou.utils;

import fr.timothefcn.mc.loupgarou.MainLg;
import fr.timothefcn.mc.loupgarou.classes.LGGame;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.inventory.Inventory;

public class InventoryUtils {
    public static Inventory roleSelector(LGGame game) {
        Inventory gui = Bukkit.createInventory(null, 4 * 9, "Rôles");

        int index = 0;
        for (String role : MainLg.getInstance().getRoles().keySet()) {
            if (!(role.equalsIgnoreCase("LoupGarou") || role.equalsIgnoreCase("Villageois"))) {
                if (game.getRolestoconfig().contains(role))
                    gui.setItem(index++, new ItemBuilder(Material.HEART_OF_THE_SEA, 1).setName(role).addEnchant(Enchantment.DURABILITY, 1).build());
                else gui.setItem(index++, new ItemBuilder(Material.HEART_OF_THE_SEA, 1).setName(role).build());
            } else if (role.equalsIgnoreCase("Villageois")) {
                if (game.getVillagers() == 0)
                    gui.setItem(27, new ItemBuilder(Material.HEART_OF_THE_SEA).setName(role).build());
                else
                    gui.setItem(27, new ItemBuilder(Material.HEART_OF_THE_SEA, game.getVillagers()).setName(role).addEnchant(Enchantment.DURABILITY, 1).build());
            } else if (role.equalsIgnoreCase("LoupGarou")) {
                if (game.getWolves() == 0)
                    gui.setItem(28, new ItemBuilder(Material.HEART_OF_THE_SEA).setName(role).build());
                else
                    gui.setItem(28, new ItemBuilder(Material.HEART_OF_THE_SEA, game.getWolves()).setName(role).addEnchant(Enchantment.DURABILITY, 1).build());
            }
        }

        gui.setItem(34, new ItemBuilder(Material.PLAYER_HEAD, game.getMaxPlayers()).setName("Nombre de joueurs total").build());

        gui.setItem(35, new ItemBuilder(Material.GOLD_NUGGET).setName("Valider").build());
        return gui;
    }
}

package fr.timothefcn.mc.loupgarou.utils;

import fr.timothefcn.mc.loupgarou.MainLg;
import fr.timothefcn.mc.loupgarou.classes.LGGame;
import lombok.SneakyThrows;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.inventory.Inventory;
import org.bukkit.util.ChatPaginator;

import java.util.ArrayList;

public class InventoryUtils {
    public static Inventory roleSelector(LGGame game) {
        Inventory gui = Bukkit.createInventory(null, 4 * 9, "Rôles");
        int index = 0;
        ArrayList<String> rolelist = new ArrayList<String>(MainLg.getInstance().getRoles().keySet());
        java.util.Collections.sort(rolelist);

        for (String role : rolelist) {

            if (role.equalsIgnoreCase("Villageois")) {
                if (game.getVillagers() == 0)
                    gui.setItem(27, generateIcon(role, 1, false, game).build());
                else
                    gui.setItem(27, generateIcon(role, game.getVillagers(), true, game).build());
            } else if (role.equalsIgnoreCase("LoupGarou")) {
                if (game.getWolves() == 0)
                    gui.setItem(28, generateIcon(role, 1, false, game).build());
                else
                    gui.setItem(28, generateIcon(role, game.getWolves(), true, game).build());
            } else {
                if (game.getRolestoconfig().contains(role))
                    gui.setItem(index++, generateIcon(role, 1, true, game).build());
                else gui.setItem(index++, generateIcon(role, 1, false, game).build());
            }
        }

        gui.setItem(34, new ItemBuilder(Material.PLAYER_HEAD, game.getMaxPlayers()).setName("Nombre de joueurs total").build());


        //Vérification des réglages
        if (VariousUtils.checkGameSettings(game).size() > 0) {
            ItemBuilder refuser = new ItemBuilder(Material.IRON_NUGGET).setName("Erreur");
            for (String s : VariousUtils.checkGameSettings(game)) {
                refuser.addLoreLine(ChatColor.YELLOW + s);
            }
            gui.setItem(35, refuser.build());
        } else gui.setItem(35, new ItemBuilder(Material.GOLD_NUGGET).setName("Valider").build());
        return gui;
    }

    @SneakyThrows
    public static ItemBuilder generateIcon(String role, int size, Boolean activated, LGGame game) {
        String[] lore = ChatPaginator.wordWrap(MainLg.getInstance().getRoles().get(role).newInstance(game).getDescription(), 42);
        ItemBuilder icon = new ItemBuilder(Material.HEART_OF_THE_SEA, size);
        if (activated) {
            icon.addEnchant(Enchantment.DURABILITY, 1);
            icon.setName(ChatColor.GREEN + "" + ChatColor.BOLD + role);
        } else icon.setName(ChatColor.RED + "" + ChatColor.BOLD + role);
        icon.setLore(lore);
        icon.setCustomModel(MainLg.getInstance().getRolesIconsID().get(role));
        return icon;
    }
}

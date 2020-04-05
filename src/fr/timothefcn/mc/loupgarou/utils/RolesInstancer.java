package fr.timothefcn.mc.loupgarou.utils;

import fr.timothefcn.mc.loupgarou.MainLg;
import fr.timothefcn.mc.loupgarou.classes.LGGame;
import fr.timothefcn.mc.loupgarou.roles.Role;
import org.bukkit.Bukkit;

import java.util.ArrayList;

public class RolesInstancer {
    static MainLg main = MainLg.getInstance();

    public static void createRoleInstances(LGGame game) {
        ArrayList<Role> roleSelection = new ArrayList<>();
        try {
            //ajout des roles
            for (String role : game.getRolestoconfig()) roleSelection.add(main.getRoles().get(role).newInstance(game));
            //
            Role villager = main.getRoles().get("Villageois").newInstance(game);
            villager.setWaitedPlayers(game.getVillagers());
            roleSelection.add(villager);
            //
            Role loup = main.getRoles().get("LoupGarou").newInstance(game);
            loup.setWaitedPlayers(game.getWolves());
            roleSelection.add(loup);
        } catch (Exception err) {
            Bukkit.broadcastMessage("§4§lUne erreur est survenue lors de la création des roles... Regardez la console !");
            err.printStackTrace();
        }
        game.setRoles(roleSelection);
    }
}

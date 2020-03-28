package fr.timothefcn.mc.loupgarou.utils;

import fr.timothefcn.mc.loupgarou.MainLg;
import fr.timothefcn.mc.loupgarou.classes.LGGame;
import fr.timothefcn.mc.loupgarou.roles.Role;
import org.bukkit.Bukkit;

import java.lang.reflect.Constructor;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Map;

public class AutoRoles {
    static MainLg main = MainLg.getInstance();


    //Tableau [loups,village]
    public static int[] Repartition(int nbplayers) {
        int[] result = new int[2];
        result[1] = nbplayers/3;
        result[0] = nbplayers-(nbplayers/3);
        return result;
    }

    private static List<Boolean> getRandomList(int nbtrue, int nbfalse) {
        List<Boolean> flags = new ArrayList<Boolean>();
        for(int i = 0; i < nbtrue; i++) flags.add(true);
        for(int i = 0; i < nbfalse; i++) flags.add(false);
        Collections.shuffle(flags);
        return flags;
    }

    public static ArrayList<Role> getRandomRoles(int nbvillagers, int nbwolves, LGGame game) {
        ArrayList<Role> roleSelection = new ArrayList<Role>();
        //Ajout du village
        List<Boolean> randomRoles = getRandomList(nbvillagers, 26-main.getBadGuys().size()-nbvillagers);
        try {
            int index = 0;
            System.out.println("Random: " + randomRoles.toString());
            for (Map.Entry<String, Constructor<? extends Role>> role : main.getRoles().entrySet()) {
                if (main.getBadGuys().contains(role.getKey())) {
              //      System.out.println(role.getKey() + " est mechant");
                }
                else {
                    System.out.println("BoolRole: " + randomRoles.get(index));
                    if (randomRoles.get(index)) {
                        roleSelection.add(role.getValue().newInstance(game));
                    }
                    index++;
                }
            }
        } catch (Exception err) {
            Bukkit.broadcastMessage("§4§lUne erreur est survenue lors de la création des roles... Regardez la console !");
            err.printStackTrace();
        }

        //Ajout des loups
        //TODO: Ajouter autres types de loups
        try {
            for (int i = 0; i<nbwolves; i++) roleSelection.add(main.getRoles().get("LoupGarou").newInstance(game));
        } catch (Exception err) {
            err.printStackTrace();
        }
        System.out.println("Roles: " + roleSelection.toString());
        return roleSelection;
    }



}

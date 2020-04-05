package fr.timothefcn.mc.loupgarou.utils;

import fr.timothefcn.mc.loupgarou.MainLg;
import fr.timothefcn.mc.loupgarou.classes.LGGame;
import fr.timothefcn.mc.loupgarou.roles.Role;
import org.bukkit.Bukkit;

import java.lang.reflect.Constructor;
import java.util.*;

public class AutoRoles {
    static MainLg main = MainLg.getInstance();

    //TODO: Meilleure repartition des roles (pack classique, etc)
    //TODO: plusieurs villageois

    //Tableau [loups,village]
    public static int[] Repartition(int nbplayers) {
        int[] result = new int[2];
        result[1] = nbplayers / 3;
        result[0] = nbplayers - (nbplayers / 3);
        return result;
    }

    private static List<Boolean> getRandomList(int nbtrue, int nbfalse) {
        List<Boolean> flags = new ArrayList<>();
        for (int i = 0; i < nbtrue; i++) flags.add(true);
        for (int i = 0; i < nbfalse; i++) flags.add(false);
        Collections.shuffle(flags);
        return flags;
    }

    public static ArrayList<Role> getRandomRoles(int nbvillagers, int nbwolves, LGGame game) {
        //Loup noir or not loup noir ?
        boolean lgnoir = false;
        if (nbvillagers == 5) { //Si 7 joueurs
            lgnoir = new Random().nextInt(14) == 0; //7% d'avoir un loup noir
        } else if (nbvillagers == 6) { //Si 8 joueurs
            lgnoir = new Random().nextInt(10) == 6; //16% d'avoir un loup noir
        }
        if (nbwolves >= 3) { //Si plus de 3 loups (=9 joueurs)
            lgnoir = new Random().nextInt(10) == 0; //30% d'avoir un loup noir
        }
        if (lgnoir) {
            if (nbwolves >= 3) {
                if (new Random().nextInt(2) == 0) {
                    nbwolves--;
                    nbvillagers++;
                } // 1:2 d'avoir un loup normal en moins et un villageois en plus
            }
            nbwolves--;
        }

        //Loup blanc ?
        boolean lgblanc = false;
        if (nbwolves >= 4 && new Random().nextInt(5) == 0) {//20% d'avoir un loup blanc
            lgblanc = true;
            nbwolves--;
        }

        ArrayList<Role> roleSelection = new ArrayList<>();
        //Ajout du village
        List<Boolean> randomRoles = getRandomList(nbvillagers, main.getRoles().size() - main.getBadGuys().size() - nbvillagers);
        try {
            int index = 0;
            //System.out.println("Random: " + randomRoles.toString());
            for (Map.Entry<String, Constructor<? extends Role>> role : main.getRoles().entrySet()) {
                if (main.getBadGuys().contains(role.getKey())) {
                    //      System.out.println(role.getKey() + " est mechant");
                } else {
                    //System.out.println("BoolRole: " + randomRoles.get(index));
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
        //TODO: Ajouter autres types de loups (noir et blanc pour l'instant)
        try {
            Role loup = main.getRoles().get("LoupGarou").newInstance(game);
            roleSelection.add(loup);
            loup.setWaitedPlayers(nbwolves);
            if (lgnoir) roleSelection.add(main.getRoles().get("LoupGarouNoir").newInstance(game));
            if (lgblanc) roleSelection.add(main.getRoles().get("LoupGarouBlanc").newInstance(game));
        } catch (Exception err) {
            err.printStackTrace();
        }
        //  System.out.println("Roles: " + roleSelection.toString());
        return roleSelection;
    }


}

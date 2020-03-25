package fr.timothefcn.mc.loupgarou.utils;

public class AutoRoles {

    //Tableau [loups,village]
    public int[] badGuysRepartition(int nbplayers) {
        int[] result = new int[2];
        result[0] = nbplayers/3;
        result[1] = nbplayers-(nbplayers/3);
        return result;
    }


}

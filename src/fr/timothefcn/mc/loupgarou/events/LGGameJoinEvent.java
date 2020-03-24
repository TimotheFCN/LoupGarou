package fr.timothefcn.mc.loupgarou.events;

import fr.timothefcn.mc.loupgarou.classes.LGGame;
import fr.timothefcn.mc.loupgarou.classes.LGPlayer;
import lombok.Getter;

public class LGGameJoinEvent extends LGEvent {
    @Getter
    LGPlayer player;

    public LGGameJoinEvent(LGGame game, LGPlayer player) {
        super(game);
        this.player = player;
    }
}

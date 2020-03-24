package fr.timothefcn.mc.loupgarou.events;

import fr.timothefcn.mc.loupgarou.classes.LGGame;
import fr.timothefcn.mc.loupgarou.classes.LGPlayer;
import lombok.Getter;

import java.util.List;

public class LGCustomItemChangeEvent extends LGEvent {
    @Getter
    private final LGPlayer player;
    @Getter
    private final List<String> constraints;

    public LGCustomItemChangeEvent(LGGame game, LGPlayer player, List<String> constraints) {
        super(game);
        this.player = player;
        this.constraints = constraints;
    }
}

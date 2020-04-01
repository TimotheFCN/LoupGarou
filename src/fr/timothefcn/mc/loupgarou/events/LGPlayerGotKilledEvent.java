package fr.timothefcn.mc.loupgarou.events;

import fr.timothefcn.mc.loupgarou.classes.LGGame;
import fr.timothefcn.mc.loupgarou.classes.LGPlayer;
import lombok.Getter;

public class LGPlayerGotKilledEvent extends LGEvent {
    @Getter
    private final boolean endGame;
    @Getter
    private final LGPlayer killed;
    @Getter
    private LGPlayerKilledEvent.Reason reason;

    public LGPlayerGotKilledEvent(LGGame game, LGPlayer killed, LGPlayerKilledEvent.Reason reason, boolean endGame) {
        super(game);
        this.killed = killed;
        this.reason = reason;
        this.endGame = endGame;
    }

}

package fr.timothefcn.mc.loupgarou.events;

import fr.timothefcn.mc.loupgarou.classes.LGGame;
import fr.timothefcn.mc.loupgarou.classes.LGWinType;
import lombok.Getter;
import lombok.Setter;

public class LGEndCheckEvent extends LGEvent {
    @Getter
    @Setter
    private LGWinType winType;

    public LGEndCheckEvent(LGGame game, LGWinType winType) {
        super(game);
        this.winType = winType;
    }
}
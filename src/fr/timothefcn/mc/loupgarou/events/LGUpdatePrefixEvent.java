package fr.timothefcn.mc.loupgarou.events;

import fr.timothefcn.mc.loupgarou.classes.LGGame;
import fr.timothefcn.mc.loupgarou.classes.LGPlayer;
import lombok.Getter;
import lombok.Setter;

public class LGUpdatePrefixEvent extends LGEvent {
    @Getter
    private final LGPlayer player, to;
    @Getter
    @Setter
    private String prefix;

    public LGUpdatePrefixEvent(LGGame game, LGPlayer player, LGPlayer to, String prefix) {
        super(game);
        this.player = player;
        this.prefix = prefix;
        this.to = to;
    }

}

package fr.timothefcn.mc.loupgarou.events;

import fr.timothefcn.mc.loupgarou.classes.LGGame;
import fr.timothefcn.mc.loupgarou.classes.LGPlayer;
import lombok.Getter;
import lombok.Setter;
import org.bukkit.event.Cancellable;

public class LGPyromaneGasoilEvent extends LGEvent implements Cancellable {
    @Getter
    @Setter
    private boolean cancelled;
    @Getter
    @Setter
    private LGPlayer player;

    public LGPyromaneGasoilEvent(LGGame game, LGPlayer player) {
        super(game);
        this.player = player;
    }
}
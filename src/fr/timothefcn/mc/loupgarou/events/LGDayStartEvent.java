package fr.timothefcn.mc.loupgarou.events;

import fr.timothefcn.mc.loupgarou.classes.LGGame;
import lombok.Getter;
import lombok.Setter;
import org.bukkit.event.Cancellable;

public class LGDayStartEvent extends LGEvent implements Cancellable {
    @Getter
    @Setter
    private boolean cancelled;

    public LGDayStartEvent(LGGame game) {
        super(game);
    }
}
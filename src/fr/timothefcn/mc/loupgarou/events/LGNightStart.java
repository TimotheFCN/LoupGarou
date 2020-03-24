package fr.timothefcn.mc.loupgarou.events;

import fr.timothefcn.mc.loupgarou.classes.LGGame;
import lombok.Getter;
import lombok.Setter;
import org.bukkit.event.Cancellable;

public class LGNightStart extends LGEvent implements Cancellable {

    @Getter
    @Setter
    boolean cancelled;

    public LGNightStart(LGGame game) {
        super(game);
    }

}

package fr.timothefcn.mc.loupgarou.events;

import fr.timothefcn.mc.loupgarou.classes.LGGame;
import lombok.Getter;
import lombok.Setter;
import org.bukkit.event.Cancellable;

public class LGVoteEvent extends LGEvent implements Cancellable {
    @Getter
    @Setter
    private boolean cancelled;

    public LGVoteEvent(LGGame game) {
        super(game);
    }
}

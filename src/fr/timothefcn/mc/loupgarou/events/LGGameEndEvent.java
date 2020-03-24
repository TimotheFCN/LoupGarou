package fr.timothefcn.mc.loupgarou.events;

import fr.timothefcn.mc.loupgarou.classes.LGGame;
import fr.timothefcn.mc.loupgarou.classes.LGPlayer;
import fr.timothefcn.mc.loupgarou.classes.LGWinType;
import lombok.Getter;
import lombok.Setter;
import org.bukkit.event.Cancellable;

import java.util.List;

public class LGGameEndEvent extends LGEvent implements Cancellable {
    @Getter
    private final LGWinType winType;
    @Getter
    private final List<LGPlayer> winners;
    @Getter
    @Setter
    private boolean cancelled;

    public LGGameEndEvent(LGGame game, LGWinType winType, List<LGPlayer> winners) {
        super(game);
        this.winType = winType;
        this.winners = winners;
    }
}
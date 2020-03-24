package fr.timothefcn.mc.loupgarou.events;

import fr.timothefcn.mc.loupgarou.classes.LGGame;
import fr.timothefcn.mc.loupgarou.roles.Role;
import lombok.Getter;

public class LGRoleTurnEndEvent extends LGEvent {
    @Getter
    private final Role newRole, previousRole;

    public LGRoleTurnEndEvent(LGGame game, Role newRole, Role previousRole) {
        super(game);
        this.newRole = newRole;
        this.previousRole = previousRole;
    }
}
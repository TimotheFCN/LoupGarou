package fr.timothefcn.mc.loupgarou.events;

import fr.timothefcn.mc.loupgarou.classes.LGGame;
import fr.timothefcn.mc.loupgarou.classes.LGPlayer;
import fr.timothefcn.mc.loupgarou.classes.LGVote;
import lombok.Getter;

import java.util.ArrayList;

public class LGVoteLeaderChange extends LGEvent {

    @Getter
    ArrayList<LGPlayer> latest, now;
    @Getter
    LGVote vote;

    public LGVoteLeaderChange(LGGame game, LGVote vote, ArrayList<LGPlayer> latest, ArrayList<LGPlayer> now) {
        super(game);
        this.latest = latest;
        this.now = now;
        this.vote = vote;
    }

}

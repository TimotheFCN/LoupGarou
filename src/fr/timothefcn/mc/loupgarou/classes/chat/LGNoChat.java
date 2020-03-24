package fr.timothefcn.mc.loupgarou.classes.chat;

import fr.timothefcn.mc.loupgarou.classes.LGPlayer;

public class LGNoChat extends LGChat {
    public LGNoChat() {
        super(null);
    }

    public void sendMessage(LGPlayer sender, String message) {
    }

    public void join(LGPlayer player, LGChatCallback callback) {

    }

    public void leave(LGPlayer player) {

    }
}

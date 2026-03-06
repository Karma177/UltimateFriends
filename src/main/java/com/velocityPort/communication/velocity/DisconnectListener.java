package com.velocityPort.communication.velocity;

import com.velocityPort.ClickableMessage;
import com.velocityPort.Friend;
import com.velocityPort.Message;
import com.velocityPort.Options;
import com.velocityPort.PlayerProfile;
import com.velocityPort.SocialSpy;
import com.velocityPort.UltimateFriends;
import com.velocityPort.Utils;
import com.velocityPort.commands.Cmds;
import java.util.Iterator;
import java.util.List;
import com.velocitypowered.api.proxy.Player;
import com.velocitypowered.api.event.connection.DisconnectEvent;
import com.velocitypowered.api.event.Subscribe;

/**
 * DisconnectListener
 * Gestisce l'evento di disconnessione (DisconnectEvent) dei giocatori dal proxy Velocity.
 * Si occupa di rimuovere il profilo dalla cache, notificare gli amici e aggiornare l'ultimo avvistamento.
 */
public class DisconnectListener {
   /**
    * onLeft
    * Metodo callback eseguito quando un giocatore si disconnette.
    * 
    * @param var1 L'evento di disconnessione fornito da Velocity.
    */
   @Subscribe
   public void onLeft(DisconnectEvent var1) {
      String var2 = var1.getPlayer().getUsername();
      PlayerProfile var3 = UltimateFriends.removePlayerProfile(var2);
      if (var3 != null) {
         this.sendLeaveMsg(var2, Utils.toStringList(var3.getFriends()));
         this.updateLastSeen(var2);
      }

      Cmds.cooledDown.remove(var2.toLowerCase());
      SocialSpy.disableSpy(var1.getPlayer());
   }

   /**
    * updateLastSeen
    * Aggiorna il timestamp di "ultimo avvistamento" per il giocatore disconnesso 
    * all'interno dei profili dei suoi amici attualmente online.
    * 
    * @param var1 Il nome del giocatore che ha lasciato il server.
    */
   protected final void updateLastSeen(String var1) {
      Iterator var2 = UltimateFriends.getPlayerProfiles().iterator();

      while(var2.hasNext()) {
         PlayerProfile var3 = (PlayerProfile)var2.next();
         Iterator var4 = var3.getFriends().iterator();

         while(var4.hasNext()) {
            Friend var5 = (Friend)var4.next();
            if (var5.getName().equals(var1)) {
               var5.refreshLastSeen();
            }
         }
      }

   }

   /**
    * sendLeaveMsg
    * Invia un messaggio di notifica a tutti gli amici online del giocatore che si è disconnesso,
    * rispettando le loro preferenze personali di notifica.
    * 
    * @param var1 Il nome del giocatore disconnesso.
    * @param var2 La lista dei nomi degli amici da notificare.
    */
   protected final void sendLeaveMsg(String var1, List<String> var2) {
      Iterator var3 = var2.iterator();

      while(var3.hasNext()) {
         String var4 = (String)var3.next();
         Player var5 = UltimateFriends.server.getPlayer(var4).orElse(null);
         if (var5 != null) {
            PlayerProfile var6 = UltimateFriends.getPlayerProfile(var4);
            if (var6 != null && var6.getOptions().get(Options.Type.SHOW_LEAVE_MSG)) {
               Utils.sendMessage(var5, (new ClickableMessage(Message.FRIEND_LEFT_SERVER.getMsg(true))).clickable(var1).append().build());
            }
         }
      }

   }
}

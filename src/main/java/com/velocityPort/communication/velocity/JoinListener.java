package com.velocityPort.communication.velocity;

import com.velocityPort.ClickableMessage;
import com.velocityPort.Message;
import com.velocityPort.Options;
import com.velocityPort.PlayerProfile;
import com.velocityPort.SocialSpy;
import com.velocityPort.UltimateFriends;
import com.velocityPort.Utils;
import java.util.Iterator;
import java.util.List;
import java.util.UUID;
import net.kyori.adventure.text.event.ClickEvent;
import net.kyori.adventure.text.event.HoverEvent.Action;
import com.velocitypowered.api.event.Subscribe;
import com.velocitypowered.api.event.connection.PostLoginEvent;
import com.velocitypowered.api.proxy.Player;

/**
 * JoinListener
 * Gestisce l'evento di ingresso (PostLoginEvent) di un giocatore nel proxy Velocity.
 * Si occupa del caricamento asincrono del profilo dal database e della notifica agli amici online.
 */
public class JoinListener {
   /**
    * onJoin
    * Metodo callback eseguito dopo che un giocatore ha effettuato l'accesso con successo.
    * 
    * @param var1 L'evento di post-login fornito da Velocity.
    */
   @Subscribe
   public void onJoin(PostLoginEvent var1) {
      if (!UltimateFriends.shuttingDown) {
         Player var2 = var1.getPlayer();
         final String var3 = var2.getUsername();
         if (!Utils.isNameValid(var3)) {
            UltimateFriends.logger.warn("Won't load profile for player '" + var3 + "', invalid characters found");
         } else {
            final UUID var4 = var2.getUniqueId();
            if (Utils.containsIgnoreCase(UltimateFriends.getConfig().getDefaultSocialSpyPlayers(), var3)) {
               SocialSpy.enableSpy(var2);
            }

            if (var2.hasPermission("ultimatefriends.autospy")) {
               SocialSpy.enableSpy(var2);
            }

            Utils.runAsync(new Runnable() {
               public void run() {
                  PlayerProfile var1 = UltimateFriends.getStorage().loadPlayerProfile(var3, var4);
                  UltimateFriends.addPlayerProfile(var3, var1);
                  JoinListener.this.sendJoinMsg(var3, Utils.toStringList(var1.getFriends()));
               }
            });
         }
      }
   }

   /**
    * sendJoinMsg
    * Invia un messaggio di notifica interattivo a tutti gli amici online dell'utente che è entrato.
    * Il messaggio include un'azione per rispondere rapidamente (tooltip e suggest command).
    * 
    * @param var1 Il nome del giocatore appena entrato.
    * @param var2 La lista dei nomi degli amici da notificare.
    */
   protected final void sendJoinMsg(String var1, List<String> var2) {
      Iterator var3 = var2.iterator();

      while(var3.hasNext()) {
         String var4 = (String)var3.next();
         Player var5 = UltimateFriends.server.getPlayer(var4).orElse(null);
         if (var5 != null) {
            PlayerProfile var6 = UltimateFriends.getPlayerProfile(var4);
            if (var6 != null && var6.getOptions().get(Options.Type.SHOW_JOIN_MSG)) {
               Utils.sendMessage(var5, (new ClickableMessage(Message.FRIEND_JOINED_SERVER.getMsg(true))).clickable(var1).hoverEvent(Action.SHOW_TEXT, (new ClickableMessage(Message.FRIEND_LIST_BUTTON_MSG_HOVER.getMsg())).clickable(var1).append().buildString()).clickEvent(ClickEvent.Action.SUGGEST_COMMAND, "/" + UltimateFriends.getConfig().getCmds().getCmd() + " msg " + var1 + " ").append().build());
            }
         }
      }

   }
}

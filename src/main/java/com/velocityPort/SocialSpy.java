package com.velocityPort;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Optional;
import com.velocitypowered.api.proxy.Player;

/**
 * Gestione della modalità SocialSpy.
 * 
 * Permette ai giocatori autorizzati (es. membri dello staff) di visualizzare i messaggi privati 
 * scambiati tra gli altri utenti sulla rete proxy.
 * Mantiene temporaneamente in memoria (RAM) la lista dei giocatori che hanno il SocialSpy attivo
 * per questa sessione.
 */
public class SocialSpy {
   private static List<String> players = new ArrayList<>();

   /**
    * spy
    * Invia il messaggio spiato a tutti i membri dello staff con la modalità attiva.
    * Evita di inviare il messaggio se la spia è il mittente o il destinatario.
    * 
    * @param var0 Il nome del mittente del messaggio originale.
    * @param var1 Il nome del destinatario del messaggio originale.
    * @param var2 Il contenuto del messaggio privato scambiato.
    */
   public static void spy(String var0, String var1, String var2) {
      Iterator<String> var3 = players.iterator();

      while(var3.hasNext()) {
         String var4 = var3.next();
         if (!var1.equalsIgnoreCase(var4) && !var0.equalsIgnoreCase(var4)) {
            Optional<Player> var5 = UltimateFriends.server.getPlayer(var4);
            if (var5.isPresent()) {
               var5.get().sendMessage((new ClickableMessage(Message.SOCIAL_SPY.getMsg(true))).clickable(var0).append().clickable(var1).append().clickable(var2).append().build());
            }
         }
      }

   }

   /**
    * enableSpy
    * Abilita la ricezione dei messaggi in background per il giocatore specificato,
    * aggiungendolo alla lista virtuale.
    * 
    * @param var0 Il giocatore al quale attivare il SocialSpy.
    */
   public static void enableSpy(Player var0) {
      if (!isSpy(var0)) {
         players.add(var0.getUsername().toLowerCase());
      }

   }

   /**
    * disableSpy
    * Rimuove il giocatore dalla lista di coloro che ascoltano il SocialSpy,
    * disabilitando così la ricezione di messaggi privati.
    * 
    * @param var0 Il giocatore al quale disattivare il SocialSpy.
    */
   public static void disableSpy(Player var0) {
      players.remove(var0.getUsername().toLowerCase());
   }

   /**
    * isSpy
    * Controlla se il giocatore fornito ha attualmente attivato la modalità di ascolto dei messaggi.
    * 
    * @param var0 Il giocatore da controllare.
    * @return true se il giocatore è nella lista, false altrimenti.
    */
   public static boolean isSpy(Player var0) {
      return players.contains(var0.getUsername().toLowerCase());
   }
}

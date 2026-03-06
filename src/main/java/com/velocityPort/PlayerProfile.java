package com.velocityPort;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.UUID;
import javax.annotation.Nullable;

public class PlayerProfile extends PlayerID {
   private Options options;
   private List<Friend> friends;
   private List<String> requests;
   private List<String> sentRequests;
   private String lastMsgSender;
   private int page;

   /**
    * PlayerProfile
    * Costruttore della classe PlayerProfile che inizializza il profilo utente con ID, nome e UUID.
    * 
    * @param var1 L'ID univoco numerico del giocatore nel database.
    * @param var2 Il nome utente (username) del giocatore.
    * @param var3 L'UUID univoco dell'account Minecraft.
    */
   public PlayerProfile(int var1, String var2, UUID var3) {
      super(var1, var2, var3);
      this.options = new Options();
      this.friends = new ArrayList();
      this.requests = new ArrayList();
      this.sentRequests = new ArrayList();
   }

   /**
    * getFriend
    * Cerca un amico nella lista del profilo in base al nome (case-insensitive).
    * 
    * @param var1 Il nome dell'amico da cercare.
    * @return L'oggetto Friend trovato, oppure null se non è presente.
    */
   @Nullable
   public Friend getFriend(String var1) {
      Iterator var2 = this.friends.iterator();

      Friend var3;
      do {
         if (!var2.hasNext()) {
            return null;
         }

         var3 = (Friend)var2.next();
      } while(!var3.getName().equalsIgnoreCase(var1));

      return var3;
   }

   /**
    * getRequests
    * Restituisce la lista di richieste di amicizia ricevute in attesa.
    * 
    * @return Una lista di nomi utente che hanno inviato una richiesta.
    */
   public List<String> getRequests() {
      return this.requests;
   }

   /**
    * getSentRequests
    * Restituisce la lista di richieste di amicizia inviate dal giocatore ad altri.
    * 
    * @return Una lista di nomi utente a cui è stata inviata una richiesta.
    */
   public List<String> getSentRequests() {
      return this.sentRequests;
   }

   /**
    * getOptions
    * Ottiene le preferenze e impostazioni del giocatore (es. notifiche, stato).
    * 
    * @return L'oggetto Options contenente i settaggi del profilo.
    */
   public Options getOptions() {
      return this.options;
   }

   /**
    * setOptions
    * Sovrascrive le impostazioni attuali del giocatore con un nuovo set di opzioni.
    * 
    * @param var1 Il nuovo oggetto Options da impostare.
    * @throws IllegalArgumentException se l'argomento è null.
    */
   public void setOptions(Options var1) {
      if (var1 == null) {
         $$$reportNull$$$0(2);
      }

      this.options = var1;
   }

   /**
    * getFriends
    * Restituisce l'intera lista degli amici attuali del giocatore scaricati dal database.
    * 
    * @return Una lista di oggetti Friend.
    */
   public List<Friend> getFriends() {
      return this.friends;
   }

   /**
    * getLastMsgSender
    * Ottiene il nome dell'ultimo utente che ha inviato un messaggio privato a questo giocatore.
    * Utilizzato per il funzionamento del comando /reply.
    * 
    * @return Il nome dell'ultimo mittente (String).
    */
   public String getLastMsgSender() {
      return this.lastMsgSender;
   }

   /**
    * setLastMsgSender
    * Memorizza il nome dell'ultimo giocatore che ha contattato privatamente l'utente.
    * 
    * @param var1 Il nome del giocatore da ricordare come ultimo mittente.
    */
   public void setLastMsgSender(String var1) {
      this.lastMsgSender = var1;
   }

   /**
    * getPage
    * Ottiene l'indice della pagina corrente visualizzata nelle interfacce (GUI/Chat) a scorrimento.
    * 
    * @return Il numero della pagina corrente.
    */
   public int getPage() {
      return this.page;
   }

   /**
    * setPage
    * Imposta l'indice della pagina corrente da mostrare nelle liste paginate.
    * 
    * @param var1 Il numero della pagina da impostare.
    */
   public void setPage(int var1) {
      this.page = var1;
   }

   // $FF: synthetic method
   private static void $$$reportNull$$$0(int var0) {
      Object[] var10001 = new Object[3];
      switch(var0) {
      case 0:
      default:
         var10001[0] = "name";
         break;
      case 1:
         var10001[0] = "uuid";
         break;
      case 2:
         var10001[0] = "options";
      }

      var10001[1] = "com.velocityPort/PlayerProfile";
      switch(var0) {
      case 0:
      case 1:
      default:
         var10001[2] = "<init>";
         break;
      case 2:
         var10001[2] = "setOptions";
      }

      throw new IllegalArgumentException(String.format("Argument for @NotNull parameter '%s' of %s.%s must not be null", var10001));
   }
}

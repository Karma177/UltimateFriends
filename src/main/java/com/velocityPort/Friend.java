package com.velocityPort;

import java.util.UUID;

/**
 * Friend
 * Rappresenta un amico di un giocatore. Estende PlayerID per includere
 * informazioni sulla sua ultima apparizione online (lastSeen).
 */
public class Friend extends PlayerID {
   private long lastSeen;

   /**
    * Friend (Costruttore)
    * Crea un'istanza Friend a partire da un oggetto PlayerID esistente.
    * 
    * @param var1 L'oggetto PlayerID da cui copiare i dati identificativi.
    */
   public Friend(PlayerID var1) {
      this(var1.getId(), var1.getName(), var1.getUuid());
   }

   /**
    * Friend (Costruttore)
    * Crea un'istanza Friend con dati espliciti e aggiorna il timestamp di lastSeen.
    * 
    * @param var1 L'ID univoco del database.
    * @param var2 Il nome utente.
    * @param var3 L'UUID di Minecraft.
    */
   public Friend(int var1, String var2, UUID var3) {
      super(var1, var2, var3);
      this.refreshLastSeen();
   }

   /**
    * Friend (Costruttore)
    * Crea un'istanza Friend convertendo l'UUID da stringa.
    * 
    * @param var1 L'ID univoco del database.
    * @param var2 Il nome utente.
    * @param var3 L'UUID in formato String.
    */
   public Friend(int var1, String var2, String var3) {
      this(var1, var2, UUID.fromString(var3));
   }

   /**
    * getLastSeen
    * Ottiene il timestamp dell'ultima volta che l'amico è stato visto online.
    * 
    * @return Il tempo in millisecondi (long).
    */
   public long getLastSeen() {
      return this.lastSeen;
   }

   /**
    * setLastSeen
    * Imposta manualmente il timestamp dell'ultima apparizione.
    * 
    * @param var1 Il timestamp in millisecondi.
    */
   public void setLastSeen(long var1) {
      this.lastSeen = var1;
   }

   /**
    * refreshLastSeen
    * Aggiorna il timestamp di lastSeen all'ora corrente del sistema.
    */
   public void refreshLastSeen() {
      this.lastSeen = System.currentTimeMillis();
   }

   /**
    * toString
    * Restituisce il nome dell'amico come rappresentazione testuale dell'oggetto.
    * 
    * @return Il nome utente (String).
    */
   public String toString() {
      return this.getName();
   }
}

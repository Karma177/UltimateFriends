package com.velocityPort;

import java.util.UUID;

/**
 * PlayerLookup
 * Enum che definisce i criteri di ricerca (lookup) per un giocatore nel database.
 * Supporta la ricerca tramite nome utente o tramite UUID.
 */
public enum PlayerLookup {
   PLAYER_NAME("name"),
   UID("uuid");

   private String node;

   /**
    * PlayerLookup (Costruttore)
    * Inizializza l'istanza dell'enum con la colonna del database corrispondente.
    * 
    * @param var3 Il nome della colonna (nodo) utilizzato nelle query SQL.
    */
   private PlayerLookup(String var3) {
      this.node = var3;
   }

   /**
    * select
    * Seleziona uno dei due valori forniti (nome o UUID) in base al tipo di lookup corrente.
    * 
    * @param var1 Il nome del giocatore (String).
    * @param var2 L'UUID del giocatore (UUID object).
    * @return Il valore scelto sotto forma di String (nome o UUID.toString()).
    */
   public String select(String var1, UUID var2) {
      return this.select(var1, var2.toString());
   }

   /**
    * select
    * Versione sovraccaricata che accetta entrambi i parametri come String.
    * 
    * @param var1 Il nome del giocatore.
    * @param var2 L'UUID del giocatore già convertito in String.
    * @return Il valore corrispondente al tipo di lookup corrente.
    */
   public String select(String var1, String var2) {
      return this == PLAYER_NAME ? var1 : var2;
   }

   /**
    * negate
    * Restituisce il metodo di lookup opposto a quello corrente.
    * Se è PLAYER_NAME restituisce UID, e viceversa.
    * 
    * @return L'istanza PlayerLookup opposta.
    */
   public PlayerLookup negate() {
      return this == PLAYER_NAME ? UID : PLAYER_NAME;
   }

   /**
    * toString
    * Restituisce la rappresentazione testuale del nodo database associato.
    * 
    * @return Il nome del nodo (name o uuid).
    */
   public String toString() {
      return this.node;
   }
}

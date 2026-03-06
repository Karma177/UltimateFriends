package com.velocityPort;
import java.util.Objects;
import java.util.UUID;
import javax.annotation.Nullable;
import com.velocitypowered.api.proxy.Player;

/**
 * PlayerID
 * Rappresenta l'identità univoca di un giocatore all'interno del sistema UltimateFriends.
 * Combina l'ID numerico del database, l'username e l'UUID di Minecraft per una risoluzione rapida.
 */
public class PlayerID {
   private int id;
   private String name;
   private UUID uuid;

   /**
    * PlayerID (Costruttore)
    * Crea un'istanza che mappa l'utente con i suoi dati identificativi.
    * 
    * @param var1 L'ID univoco assegnato dal database.
    * @param var2 Il nome utente (username) del giocatore.
    * @param var3 L'UUID univoco del giocatore.
    */
   public PlayerID(int var1, String var2, UUID var3) {
      this.id = var1;
      this.name = var2;
      this.uuid = var3;
   }

   /**
    * getId
    * Restituisce l'ID numerico univoco del database associato al giocatore.
    * 
    * @return L'ID intero.
    */
   public int getId() {
      return this.id;
   }

   /**
    * getName
    * Ottiene l'ultimo nome utente conosciuto registrato per questo ID.
    * 
    * @return Il nome utente (String).
    */
   public String getName() {
      return name;
   }

   /**
    * getUuid
    * Ottiene l'UUID univoco dell'account Minecraft.
    * 
    * @return L'oggetto UUID.
    */
   public UUID getUuid() {
      return uuid;
   }

   /**
    * getPlayerName
    * Alias del metodo getName() per ottenere la stringa dell'username.
    * 
    * @return Il nome del giocatore (String).
    */
   public String getPlayerName() {
      return this.name;
   }

   /**
    * getPlayer
    * Tenta di recuperare l'istanza del giocatore online (Player) dal server Velocity
    * utilizzando il nome registrato.
    * 
    * @return L'istanza Player se online, oppure null se offline.
    */
   @Nullable
   public Player getPlayer() {
      // Velocity returns Optional<Player> using standard getPlayer(String) API.
      return UltimateFriends.server.getPlayer(this.name).orElse(null);
   }

   /**
    * toPlayerID
    * Crea e restituisce una copia dell'oggetto corrente come istanza base PlayerID.
    * 
    * @return Un nuovo oggetto PlayerID con gli stessi dati.
    */
   public PlayerID toPlayerID() {
      return new PlayerID(this.id, this.name, this.uuid);
   }

   /**
    * equals
    * Confronta questo oggetto con un altro per verificarne l'uguaglianza.
    * Due PlayerID sono uguali se hanno stesso ID, stesso nome (ignore-case) e stesso UUID.
    * 
    * @param var1 L'oggetto da confrontare.
    * @return true se gli oggetti sono identici o logicamente uguali.
    */
   public boolean equals(Object var1) {
      if (this == var1) {
         return true;
      } else if (var1 != null && this.getClass() == var1.getClass()) {
         PlayerID var2 = (PlayerID)var1;
         return this.id == var2.id && this.name.equalsIgnoreCase(var2.name) && Objects.equals(this.uuid, var2.uuid);
      } else {
         return false;
      }
   }

   /**
    * hashCode
    * Genera un codice hash basato su ID, nome (in minuscolo) e UUID per l'uso nelle collezioni.
    * 
    * @return Il valore hash calcolato.
    */
   public int hashCode() {
      return Objects.hash(new Object[]{this.id, this.name.toLowerCase(), this.uuid});
   }
}

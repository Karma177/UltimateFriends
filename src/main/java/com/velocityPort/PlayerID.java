package com.velocityPort;

import java.util.Objects;
import java.util.UUID;

import javax.annotation.Nullable;

import com.velocitypowered.api.proxy.ProxyServer;
import com.velocitypowered.api.proxy.Player;

public class PlayerID {
   private int id;
   private String name;
   private UUID uuid;

   public PlayerID(int var1, String var2, UUID var3) {
      this.id = var1;
      this.name = var2;
      this.uuid = var3;
   }

   public int getId() {
      return this.id;
   }

   public String getName() {
      return name;
   }

   public UUID getUuid() {
      return uuid;
   }

   public String getPlayerName() {
      return this.name;
   }

   @Nullable
   public Player getPlayer() {
      // Velocity returns Optional<Player> using standard getPlayer(String) API.
      return UltimateFriends.server.getPlayer(this.name).orElse(null);
   }

   public PlayerID toPlayerID() {
      return new PlayerID(this.id, this.name, this.uuid);
   }

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

   public int hashCode() {
      return Objects.hash(new Object[]{this.id, this.name.toLowerCase(), this.uuid});
   }
}

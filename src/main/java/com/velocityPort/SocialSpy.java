package com.velocityPort;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Optional;

import com.velocitypowered.api.proxy.Player;

public class SocialSpy {
   private static List<String> players = new ArrayList<>();

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

   public static void enableSpy(Player var0) {
      if (!isSpy(var0)) {
         players.add(var0.getUsername().toLowerCase());
      }

   }

   public static void disableSpy(Player var0) {
      players.remove(var0.getUsername().toLowerCase());
   }

   public static boolean isSpy(Player var0) {
      return players.contains(var0.getUsername().toLowerCase());
   }
}

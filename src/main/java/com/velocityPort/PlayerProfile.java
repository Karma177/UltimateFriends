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

   public PlayerProfile(int var1, String var2, UUID var3) {
      super(var1, var2, var3);
      this.options = new Options();
      this.friends = new ArrayList();
      this.requests = new ArrayList();
      this.sentRequests = new ArrayList();
   }

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

   public List<String> getRequests() {
      return this.requests;
   }

   public List<String> getSentRequests() {
      return this.sentRequests;
   }

   public Options getOptions() {
      return this.options;
   }

   public void setOptions(Options var1) {
      if (var1 == null) {
         $$$reportNull$$$0(2);
      }

      this.options = var1;
   }

   public List<Friend> getFriends() {
      return this.friends;
   }

   public String getLastMsgSender() {
      return this.lastMsgSender;
   }

   public void setLastMsgSender(String var1) {
      this.lastMsgSender = var1;
   }

   public int getPage() {
      return this.page;
   }

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

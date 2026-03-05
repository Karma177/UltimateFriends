package com.velocityPort.hook;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Set;

import com.velocitypowered.api.plugin.PluginContainer;
import com.velocitypowered.api.proxy.Player;
import com.velocitypowered.api.proxy.server.RegisteredServer;
import com.velocityPort.UltimateFriends;

public class HookManager {
   private Set<Hook> hooks = new HashSet<>();
   private Function connectFn = new Function() {
      public void invoke(Object... var1) {
         Player var2 = (Player)var1[0];
         RegisteredServer var3 = (RegisteredServer)var1[1];
         var2.createConnectionRequest(var3).fireAndForget();
      }
   };

   public void registerHook(Hook var1) {
      this.hooks.add(var1);
   }

   public void enableHooks() {
      Iterator<Hook> var1 = this.hooks.iterator();

      while(true) {
         Hook var2;
         while(true) {
            do {
               do {
                  if (!var1.hasNext()) {
                     return;
                  }

                  var2 = var1.next();
               } while(var2.isEnabled());
            } while(!UltimateFriends.server.getPluginManager().getPlugin(var2.getPluginName()).isPresent());

            String var4 = var2.getPluginClass();
            if (var4 == null) {
               break;
            }

            try {
               Class.forName(var4);
               break;
            } catch (Throwable var7) {
            }
         }

         try {
            var2.enable();
            var2.setEnabled(true);
         } catch (Throwable var6) {
            var6.printStackTrace();
         }
      }
   }

   public List<Hook> getHooks() {
      return new ArrayList<>(this.hooks);
   }

   public void disableHooks() {
      Iterator<Hook> var1 = this.hooks.iterator();

      while(var1.hasNext()) {
         Hook var2 = var1.next();
         if (var2.isEnabled()) {
            try {
               var2.disable();
               var2.setEnabled(false);
            } catch (Throwable var4) {
               var4.printStackTrace();
            }
         }
      }

   }

   private void invokeFn(Function var1, Object... var2) {
      try {
         var1.invoke(var2);
      } catch (Throwable var4) {
         var4.printStackTrace();
      }

   }

   public void connectPlayer(Player var1, RegisteredServer var2) {
      this.invokeFn(this.connectFn, var1, var2);
   }

   public void setConnectFn(Function var1) {
      this.connectFn = var1;
   }
}

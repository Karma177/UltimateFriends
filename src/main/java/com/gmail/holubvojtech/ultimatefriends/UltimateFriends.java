package com.gmail.holubvojtech.ultimatefriends;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.nio.file.CopyOption;
import java.nio.file.Files;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.concurrent.locks.ReadWriteLock;
import java.util.concurrent.locks.ReentrantReadWriteLock;
import java.util.logging.Logger;

import javax.annotation.Nullable;

import com.gmail.holubvojtech.ultimatefriends.commands.MsgCmd;
import com.gmail.holubvojtech.ultimatefriends.commands.ReplyCmd;
import com.gmail.holubvojtech.ultimatefriends.communication.CommunicationModule;
import com.gmail.holubvojtech.ultimatefriends.hook.Hook;
import com.gmail.holubvojtech.ultimatefriends.hook.HookManager;
import com.gmail.holubvojtech.ultimatefriends.migrate.MigrationCheck;
import com.gmail.holubvojtech.ultimatefriends.migrate.MigrationCheck.MigrationUnsuccessfulException;
import com.gmail.holubvojtech.ultimatefriends.storage.Storage;

import net.md_5.bungee.api.ProxyServer;
import net.md_5.bungee.api.plugin.Plugin;
import net.md_5.bungee.api.plugin.PluginManager;

public class UltimateFriends extends Plugin {
   public static ProxyServer server;
   public static UltimateFriends plugin;
   public static Logger logger;
   public static boolean shuttingDown = false;
   private static Storage storage;
   private static CommunicationModule communicationModule;
   private static Config config;
   private static HookManager hookManager = new HookManager();
   private static Map<String, PlayerProfile> playerProfiles = new HashMap();
   private static ReadWriteLock rwLock = new ReentrantReadWriteLock();

   public static PlayerProfile getPlayerProfile(String var0) {
      if (var0 == null) {
         $$$reportNull$$$0(0);
      }

      rwLock.readLock().lock();

      PlayerProfile var1;
      try {
         var1 = (PlayerProfile)playerProfiles.get(var0.toLowerCase());
      } finally {
         rwLock.readLock().unlock();
      }

      return var1;
   }

   public static void addPlayerProfile(String var0, PlayerProfile var1) {
      if (var0 == null) {
         $$$reportNull$$$0(1);
      }

      if (var1 == null) {
         $$$reportNull$$$0(2);
      }

      rwLock.writeLock().lock();

      try {
         playerProfiles.put(var0.toLowerCase(), var1);
      } finally {
         rwLock.writeLock().unlock();
      }

   }

   public static List<PlayerProfile> getPlayerProfiles() {
      rwLock.readLock().lock();

      ArrayList var0;
      try {
         var0 = new ArrayList(playerProfiles.values());
      } finally {
         rwLock.readLock().unlock();
      }

      return var0;
   }

   @Nullable
   public static PlayerProfile removePlayerProfile(String var0) {
      if (var0 == null) {
         $$$reportNull$$$0(3);
      }

      rwLock.writeLock().lock();

      PlayerProfile var1;
      try {
         var1 = (PlayerProfile)playerProfiles.remove(var0.toLowerCase());
      } finally {
         rwLock.writeLock().unlock();
      }

      return var1;
   }

   public static Storage getStorage() {
      return storage;
   }

   public static CommunicationModule getCommunicationModule() {
      return communicationModule;
   }

   public static Config getConfig() {
      return config;
   }

   public static HookManager getHookManager() {
      return hookManager;
   }

   public void onEnable() {
      long var1 = System.currentTimeMillis();
      plugin = this;
      logger = this.getLogger();
      server = this.getProxy();
      if (!this.getDataFolder().exists() && !this.getDataFolder().mkdir()) {
         logger.severe("Cannot create plugin folder!");
      } else if (this.copyFiles()) {
         try {
            config = new Config(new File(this.getDataFolder(), "config.yml"));
         } catch (Exception var7) {
            var7.printStackTrace();
            logger.severe("Cannot load config: " + var7.getMessage());
            logger.severe("Exiting...");
            return;
         }

         try {
            (new MigrationCheck(new File(this.getDataFolder(), "migrate.yml"))).checkAndMigrate();
         } catch (IOException | MigrationUnsuccessfulException var6) {
            var6.printStackTrace();
            logger.severe("Migration failed: " + var6.getMessage());
            logger.severe("Exiting...");
            return;
         }

         storage = config.getStorage();
         logger.info("Opening storage...");
         if (!storage.connect()) {
            logger.severe("Cannot connect to the storage");
            logger.severe("Exiting...");
         } else {
            logger.info("Registering hooks...");
            logger.info("Enabling hooks...");
            hookManager.enableHooks();
            Iterator var3 = hookManager.getHooks().iterator();

            while(var3.hasNext()) {
               Hook var4 = (Hook)var3.next();
               if (var4.isEnabled()) {
                  logger.info("   Plugin hook '" + var4.getPluginName() + "'/" + var4.getClass().getSimpleName() + " was enabled");
               }
            }

            logger.info("Registering listeners...");
            communicationModule = config.getCommunicationModule();
            communicationModule.registerListeners();
            logger.info("Registering commands...");
            PluginManager var8 = this.getProxy().getPluginManager();
            var8.registerCommand(this, config.getCmds());
            ReplyCmd var9 = config.getReplyCmd();
            if (var9 != null) {
               var8.registerCommand(this, var9);
            }

            MsgCmd var5 = config.getMsgCmd();
            if (var5 != null) {
               var8.registerCommand(this, var5);
            }

            logger.info("All done in " + (System.currentTimeMillis() - var1) + " ms");
            logger.info("Enabled");
         }
      }
   }

   public void onDisable() {
      shuttingDown = true;
      long var1 = System.currentTimeMillis();
      logger.info("Disabling listeners...");
      if (communicationModule != null) {
         communicationModule.unregisterListeners();
      }

      logger.info("Disabling hooks...");
      hookManager.disableHooks();
      logger.info("Closing storage...");
      if (storage != null) {
         storage.disconnect();
      }

      logger.info("All done in " + (System.currentTimeMillis() - var1) + " ms");
      logger.info("Disabled");
   }

   private boolean copyFiles() {
      String[] var1 = new String[]{"config.yml", "migrate.yml"};
      String[] var2 = var1;
      int var3 = var1.length;

      for(int var4 = 0; var4 < var3; ++var4) {
         String var5 = var2[var4];
         File var6 = new File(this.getDataFolder(), var5);
         if (!var6.exists()) {
            InputStream var7 = this.getResourceAsStream(var5);

            try {
               Files.copy(var7, var6.toPath(), new CopyOption[0]);
            } catch (IOException var9) {
               var9.printStackTrace();
               return false;
            }
         }
      }

      return true;
   }

   // $FF: synthetic method
   private static void $$$reportNull$$$0(int var0) {
      Object[] var10001 = new Object[3];
      switch(var0) {
      case 0:
      case 1:
      case 3:
      default:
         var10001[0] = "playerName";
         break;
      case 2:
         var10001[0] = "playerProfile";
      }

      var10001[1] = "com/gmail/holubvojtech/ultimatefriends/UltimateFriends";
      switch(var0) {
      case 0:
      default:
         var10001[2] = "getPlayerProfile";
         break;
      case 1:
      case 2:
         var10001[2] = "addPlayerProfile";
         break;
      case 3:
         var10001[2] = "removePlayerProfile";
      }

      throw new IllegalArgumentException(String.format("Argument for @NotNull parameter '%s' of %s.%s must not be null", var10001));
   }
}

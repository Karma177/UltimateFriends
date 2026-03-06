/**
 * UltimateFriends - Velocity Port
 * 
 * Questo progetto è il port nativo per Velocity del plugin UltimateFriends. 
 * Di seguito viene descritta l'architettura principale e lo scopo di ogni package:
 * 
 * Struttura dei Package:
 * - commands/:        Contiene le classi per la definizione e gestione dei comandi in-game (es. /friends, /msg, /reply). 
 *                     Si interfaccia con il CommandManager di Velocity.
 * - communication/:   Gestisce il sistema di messaggistica interna tra il Proxy (Velocity) e i sub-servers (backend) 
 *                     o altri moduli per trasmettere azioni cross-server.
 * - exceptions/:      Raggruppa e gestisce le eccezioni custom generate durante l'esecuzione (es. timeout database, errori formattazione).
 * - hook/:            Sistema di integrazione con plugin di terze parti o API esterne. Permette di registrare o attivare 
 *                     "ganci" (es. interazioni con vanish o party plugin).
 * - jsql/:            Modulo interno per la creazione e astrazione di query SQL (Query Builder). Fornisce un livello agnostico
 *                     tra il codice Java e le differenze sintattiche SQL.
 * - migrate/:         Script e logiche dedicate alla migrazione retroattiva dei dati dai vecchi sistemi di archiviazione 
 *                     (come file piatti yaml o plugin legacy di BungeeCord) verso i nuovi database.
 * - storage/:         Interfacce e implementazioni di database reali (es. cartella mysql/, sqlite/). 
 *                     Gestisce la connessione JDBC cruda e la serializzazione/deserializzazione dei dati permanenti.
 * 
 * Classi Root Principali:
 * - UltimateFriends:  Classe Core (Entry-point). Sottoscrive gli eventi proxy (inizializzazione, chiusura) e fa da orchestratore.
 * - Config:           Carica e memorizza le impostazioni dal file config.yml. Instanzia i moduli come Storage e Communication.
 * - Modelli Base:     PlayerProfile, Friend, Message, Options rappresentano la struttura logica in memoria degli oggetti utente.
 * 
 * Original by M1nef4n. 
 * Port by Karma177, powered by Gemini 3.1 Pro. 
 * Note: Tutti i warning sono dovuti al developer originale e al suo utilizzo dei tipi.
 * Questa port è stata realizzata per essere il più funzionante possibile senza modificare la logica originale, ma alcune ottimizzazioni o refactor potrebbero essere necessari in futuro.
 * Le uniche cose che sono state modificate le chiamate alle API bungee, sostituite con le equivalenti di Velocity.
 * Version 1.0.0 - Prima release per velocity. Versione non ancora testata estensivamente.
 */
package com.velocityPort;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.nio.file.CopyOption;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.concurrent.locks.ReadWriteLock;
import java.util.concurrent.locks.ReentrantReadWriteLock;
import org.slf4j.Logger;

import javax.annotation.Nullable;
import com.google.inject.Inject;
import com.velocitypowered.api.event.Subscribe;
import com.velocitypowered.api.event.proxy.ProxyInitializeEvent;
import com.velocitypowered.api.event.proxy.ProxyShutdownEvent;
import com.velocitypowered.api.plugin.Plugin;
import com.velocitypowered.api.plugin.annotation.DataDirectory;
import com.velocitypowered.api.proxy.ProxyServer;

import com.velocityPort.communication.CommunicationModule;
import com.velocityPort.hook.Hook;
import com.velocityPort.hook.HookManager;
import com.velocityPort.migrate.MigrationCheck;
import com.velocityPort.migrate.MigrationCheck.MigrationUnsuccessfulException;
import com.velocityPort.storage.Storage;

@Plugin(id = "ultimatefriends", name = "UltimateFriends", version = "1.0.0")
public class UltimateFriends {
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
   
   private final Path dataDirectory;

/**
    * getDataDirectory
    * Recupera il percorso della directory dei dati assegnata dal proxy.
    * 
    * @return Il Path della cartella dei dati del plugin
    */
   public Path getDataDirectory() {
      return this.dataDirectory;
   }

/**
    * UltimateFriends
    * Costruttore principale per l'iniezione delle dipendenze di Velocity.
    * 
    * @param server Il server proxy Velocity
    * @param logger Il logger SLF4J
    * @param dataDirectory Il percorso alla directory dei dati
    */
   @Inject
   public UltimateFriends(ProxyServer server, Logger logger, @DataDirectory Path dataDirectory) {
      UltimateFriends.server = server;
      UltimateFriends.logger = logger;
      this.dataDirectory = dataDirectory;
      plugin = this;
   }

   /**
    * getPlayerProfile
    * Recupera il profilo di un giocatore dalla memoria cache.
    * 
    * @param var0 Il nome del giocatore (solitamente minuscolo/case-insensitive)
    * @return L'oggetto PlayerProfile associato, o null se non ? online o non ? trovato
    */
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

/**
    * addPlayerProfile
    * Aggiunge e associa un profilo giocatore alla memoria cache in runtime.
    * 
    * @param var0 Il nome del giocatore a cui associare il record
    * @param var1 L'oggetto PlayerProfile da inserire in memoria
    */
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

/**
    * getPlayerProfiles
    * Restituisce una copia di tutti i profili giocatore correntemente caricati in memoria.
    * 
    * @return Una lista di PlayerProfile attivi
    */
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
   /**
    * removePlayerProfile
    * Rimuove il profilo di un giocatore temporaneo dalla memoria cache, usato alla disconnessione.
    * 
    * @param var0 Il nome del giocatore
    * @return L'oggetto PlayerProfile rimosso con successo, oppure null se non esisteva
    */
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

   /**
    * getStorage
    * Recupera il sistema di archiviazione dati attivo.
    * 
    * @return L'oggetto Storage
    */
   public static Storage getStorage() {
      return storage;
   }

   /**
    * getCommunicationModule
    * Recupera il modulo di comunicazione network fra proxy (es. Bungee, Velocity).
    * 
    * @return L'oggetto CommunicationModule in uso per gestire messaggi cross-server
    */
   public static CommunicationModule getCommunicationModule() {
      return communicationModule;
   }

   /**
    * getConfig
    * Recupera la configurazione corrente del plugin.
    * 
    * @return L'oggetto Config attualmente in uso
    */
   public static Config getConfig() {
      return config;
   }

   /**
    * getHookManager
    * Recupera il gestore degli hook (iniezioni o integrazioni in plugin esterni).
    * 
    * @return l'HookManager installato
    */
   public static HookManager getHookManager() {
      return hookManager;
   }

/**
    * getDataFolder
    * Converte il Path della directory dati in un file locale standard.
    * 
    * @return Un oggetto File che punta alla cartella dei dati
    */
   public File getDataFolder() {
      return dataDirectory.toFile();
   }

/**
    * onProxyInitialization
    * Metodo di avvio eseguito durante l'inizializzazione del Proxy. Allestisce config, storage e moduli.
    * 
    * @param event L'evento di inizializzazione lanciato da Velocity
    */
   @Subscribe
   public void onProxyInitialization(ProxyInitializeEvent event) {
      long var1 = System.currentTimeMillis();
      plugin = this;
      if (!this.getDataFolder().exists() && !this.getDataFolder().mkdir()) {
         logger.error("Cannot create plugin folder!");
      } else if (this.copyFiles()) {
         try {
            config = new Config(new File(this.getDataFolder(), "config.yml"));
         } catch (Exception var7) {
            var7.printStackTrace();
            logger.error("Cannot load config: " + var7.getMessage());
            logger.error("Exiting...");
            return;
         }

         try {
            (new MigrationCheck(new File(this.getDataFolder(), "migrate.yml"))).checkAndMigrate();
         } catch (IOException | MigrationUnsuccessfulException var6) {
            var6.printStackTrace();
            logger.error("Migration failed: " + var6.getMessage());
            logger.error("Exiting...");
            return;
         }

         storage = config.getStorage();
         logger.info("Opening storage...");
         if (!storage.connect()) {
            logger.error("Cannot connect to the storage");
            logger.error("Exiting...");
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
            
            com.velocitypowered.api.command.CommandManager commandManager = server.getCommandManager();

            com.velocityPort.commands.Cmds cmds = config.getCmds();
            if (cmds != null) {
               commandManager.register(
                  commandManager.metaBuilder(cmds.getCmd()).aliases(cmds.getAliases() != null ? cmds.getAliases() : new String[0]).build(),
                  cmds
               );
            }

            com.velocityPort.commands.MsgCmd msgCmd = config.getMsgCmd();
            if (msgCmd != null) {
               commandManager.register(
                  commandManager.metaBuilder(msgCmd.getCmd()).build(),
                  msgCmd
               );
            }

            com.velocityPort.commands.ReplyCmd replyCmd = config.getReplyCmd();
            if (replyCmd != null) {
               commandManager.register(
                  commandManager.metaBuilder(replyCmd.getCmd()).build(),
                  replyCmd
               );
            }
            
            logger.info("All done in " + (System.currentTimeMillis() - var1) + " ms");
            logger.info("Enabled");
         }
      }
   }

/**
    * onProxyShutdown
    * Metodo eseguito durante lo spegnimento del server. Si occupa di salvare, disattivare listeners ed hook.
    * 
    * @param event L'evento di spegnimento lanciato da Velocity
    */
   @Subscribe
   public void onProxyShutdown(ProxyShutdownEvent event) {
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

/**
    * copyFiles
    * Estrae i file di base (config.yml e migrate.yml) dalle risorse del jar all'interno della cartella dei dati se non esistono.
    * 
    * @return true se tutti i file sono stati copiati (o esistono) con successo, false in caso di errori
    */
   private boolean copyFiles() {
      String[] var1 = new String[]{"config.yml", "migrate.yml"};
      String[] var2 = var1;
      int var3 = var1.length;

      for(int var4 = 0; var4 < var3; ++var4) {
         String var5 = var2[var4];
         File var6 = new File(this.getDataFolder(), var5);
         if (!var6.exists()) {
            InputStream var7 = this.getClass().getResourceAsStream("/" + var5);

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

/**
    * $$$reportNull$$$0
    * Metodo sintetico generato dal compilatore per segnalare la mancanza di argomenti nei controlli Null.
    * 
    * @param var0 ID mnemonico del null-check fallito
    * @throws IllegalArgumentException Sempre lanciata quando invocato
    */
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

      var10001[1] = "com.velocityPort/UltimateFriends";
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





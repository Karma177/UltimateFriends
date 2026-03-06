package com.velocityPort;

import java.io.File;
import java.io.IOException;
import net.md_5.bungee.api.ChatColor;
import net.md_5.bungee.config.Configuration;
import net.md_5.bungee.config.ConfigurationProvider;
import net.md_5.bungee.config.YamlConfiguration;

/**
 * LanguageLoader
 * Si occupa di caricare i messaggi di traduzione (lang) dai file .yml.
 * Converte i colori dei codici '&' e popola dinamicamente l'enum Message.
 */
public class LanguageLoader {
   private Configuration configuration;

   /**
    * LanguageLoader (Costruttore)
    * Carica e processa il file di lingua specificato.
    * 
    * @param var1 Il riferimento (File) al file .yml di lingua (es. lang_en_GB.yml).
    * @throws IOException Se si verifica un errore durante il caricamento fisico del file.
    */
   public LanguageLoader(File var1) throws IOException {
      UltimateFriends.logger.info("Lang file: " + var1.getName());
      this.configuration = ConfigurationProvider.getProvider(YamlConfiguration.class).load(var1);
      Message[] var2 = Message.values();
      int var3 = var2.length;

      for(int var4 = 0; var4 < var3; ++var4) {
         Message var5 = var2[var4];
         String var6 = this.configuration.getString(var5.name(), (String)null);
         if (var6 == null) {
            UltimateFriends.logger.warn("Translation: " + var5.name() + " not found!");
         } else {
            var6 = ChatColor.translateAlternateColorCodes('&', var6);
            var5.setMsg(var6);
         }
      }

   }

   /**
    * getConfiguration
    * Restituisce l'oggetto di configurazione Bungee/Velocity caricato in memoria.
    * 
    * @return L'oggetto Configuration contenente le chiavi grezze.
    */
   public Configuration getConfiguration() {
      return this.configuration;
   }
}

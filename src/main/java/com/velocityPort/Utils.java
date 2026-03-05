package com.velocityPort;

import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.Date;
import java.util.Iterator;
import java.util.List;
import java.util.regex.Pattern;
import net.kyori.adventure.text.Component;
import java.util.Optional;
import com.velocitypowered.api.proxy.Player;
import com.velocitypowered.api.proxy.server.RegisteredServer;

public class Utils {
   private static final Pattern PLAYER_NAME_PATTERN = Pattern.compile("[a-zA-Z0-9_]*");

   /**
    * toStringList
    * Converte una lista generica in una lista di stringhe chiamando toString() su ciascun elemento.
    * 
    * @param var0 La lista originale di elementi
    * @return La lista convertita che contiene la rappresentazione stringa degli elementi
    */
   public static <T> List<String> toStringList(List<T> var0) {
      if (var0 == null) {
         return null;
      } else {
         ArrayList var1 = new ArrayList();
         Iterator var2 = var0.iterator();

         while(var2.hasNext()) {
            Object var3 = var2.next();
            var1.add(var3.toString());
         }

         return var1;
      }
   }

   /**
    * concat
    * Concatena due array generici restituendo un nuovo array unito.
    * 
    * @param var0 Il primo array
    * @param var1 Il secondo array
    * @param <T> Il tipo degli array
    * @return Un nuovo array contenente gli elementi concatenati
    */
   public static <T> T[] concat(T[] var0, T[] var1) {
      int var2 = var0.length;
      int var3 = var1.length;
      T[] var4 = (T[]) (Array.newInstance(var0.getClass().getComponentType(), var2 + var3));
      System.arraycopy(var0, 0, var4, 0, var2);
      System.arraycopy(var1, 0, var4, var2, var3);
      return var4;
   }

   /**
    * concat (Component)
    * Concatena due array di Componenti testuali.
    * 
    * @param var0 Il primo array di Componenti
    * @param var1 Il secondo array di Componenti
    * @return Un nuovo array contenente i Componenti concatenati
    */
   public static Component[] concat(Component[] var0, Component[] var1) {
      int var2 = var0.length;
      int var3 = var1.length;
      Component[] var4 = new Component[var2 + var3];
      System.arraycopy(var0, 0, var4, 0, var2);
      System.arraycopy(var1, 0, var4, var2, var3);
      return var4;
   }

   /**
    * ordinalIndexOf
    * Trova l'indice dell'N-esima occorrenza di una sottostringa.
    * 
    * @param var0 La stringa in cui cercare
    * @param var1 La sottostringa da cercare
    * @param var2 L'occorrenza (es. seconda occorrenza)
    * @return L'indice in cui compare la N-esima occorrenza
    */
   public static int ordinalIndexOf(String var0, String var1, int var2) {
      int var3 = var0.indexOf(var1);

      while(true) {
         --var2;
         if (var2 <= 0 || var3 == -1) {
            return var3;
         }

         var3 = var0.indexOf(var1, var3 + 1);
      }
   }

   /**
    * join
    * Unisce un array di stringhe utilizzando un delimitatore.
    * 
    * @param var0 Il delimitatore
    * @param var1 Gli elementi stringa da unire
    * @return La stringa completa unita dal delimitatore
    */
   public static String join(String var0, String... var1) {
      return join(var0, (Collection)Arrays.asList(var1));
   }

   /**
    * join (Collection)
    * Unisce gli elementi di una Collection in una stringa utilizzando un delimitatore.
    * 
    * @param var0 Il delimitatore
    * @param var1 Gli elementi Collection da unire
    * @return La stringa completa unita dal delimitatore
    */
   public static String join(String var0, Collection var1) {
      if (var1 != null && var0 != null) {
         if (var1.isEmpty()) {
            return "";
         } else {
            StringBuilder var2 = new StringBuilder();
            int var3 = 0;

            for(Iterator var4 = var1.iterator(); var4.hasNext(); ++var3) {
               Object var5 = var4.next();
               if (var3 > 0) {
                  var2.append(var0);
               }

               var2.append(var5.toString());
            }

            return var2.toString();
         }
      } else {
         return null;
      }
   }

   /**
    * containsIgnoreCase
    * Verifica se una determinata stringa è presente in una lista ignorando maiuscole e minuscole.
    * 
    * @param var0 La lista di stringhe
    * @param var1 La stringa da verificare ignorando i caratteri maiuscoli o minuscoli
    * @return true se presente, false altrimenti
    */
   public static boolean containsIgnoreCase(List<String> var0, String var1) {
      Iterator var2 = var0.iterator();

      String var3;
      do {
         if (!var2.hasNext()) {
            return false;
         }

         var3 = (String)var2.next();
      } while(!var3.equalsIgnoreCase(var1));

      return true;
   }

   /**
    * isNameValid
    * Valida il nome di un giocatore assicurandosi che sia alfanumerico e non superi i 16 caratteri.
    * 
    * @param var0 Il nome del giocatore da validare
    * @return true se non supera 16 caratteri ed è alfanumerico
    */
   public static boolean isNameValid(String var0) {
      return PLAYER_NAME_PATTERN.matcher(var0).matches() && var0.length() <= 16;
   }

   /**
    * safeConnect
    * Connette in sicurezza un giocatore a un server specificato.
    * 
    * @param var0 Il giocatore da connettere
    * @param var1 Il server a cui inviarlo
    */
   public static void safeConnect(Player var0, RegisteredServer var1) {
      try {
         // ! to review
         // L'Hook in Velocity si connette usando il sistema createConnectionRequest o un hook convertito.
         // Per ora simuliamo/prepariamo la chiamata con il manager convertito. (RichiederÃ  HookManager Velocity)
         // UltimateFriends.getHookManager().connectPlayer(var0, var1);
         var0.createConnectionRequest(var1).fireAndForget();
      } catch (Throwable var3) {
         var3.printStackTrace();
      }

   }

   /**
    * formatTime
    * Formatta un timestamp (in millisecondi) come data leggibile.
    * 
    * @param var0 Il tempo in millisecondi (timestamp)
    * @return La data formattata in base al formato specificato in Config
    */
   public static String formatTime(long var0) {
      return UltimateFriends.getConfig().getDateFormat().format(new Date(var0));
   }

   /**
    * runAsync
    * Esegue un Runnable in modo asincrono sul thread pool di Velocity.
    * 
    * @param r Il Runnable da eseguire asincronamente nel proxy
    */
   public static void runAsync(Runnable r) {
      UltimateFriends.server.getScheduler().buildTask(UltimateFriends.plugin, r).schedule();
   }

   /**
    * sendMessage (legacy)
    * Invia un messaggio testuale utilizzando la sintassi legacy Bungee/Spigot.
    * 
    * @param source La fonte che riceve il messaggio
    * @param message Il testo Legacy (con codici '&') da inviare
    */
   public static void sendMessage(com.velocitypowered.api.command.CommandSource source, String message) {
      if (source == null || message == null) return;
      source.sendMessage(net.kyori.adventure.text.serializer.legacy.LegacyComponentSerializer.legacySection().deserialize(message));
   }

   /**
    * sendMessage (Component)
    * Invia un messaggio di testo utilizzando i Component Kyori Adventure nativi.
    * 
    * @param source La fonte che riceve il messaggio
    * @param message Componente pronto Kyori Adventure da inviare
    */
   public static void sendMessage(com.velocitypowered.api.command.CommandSource source, net.kyori.adventure.text.Component message) {
      if (source == null || message == null) return;
      source.sendMessage(message);
   }
}




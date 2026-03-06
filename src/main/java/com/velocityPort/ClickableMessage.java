package com.velocityPort;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.TreeMap;
import java.util.Map.Entry;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.TextComponent;
import net.kyori.adventure.text.event.ClickEvent;
import net.kyori.adventure.text.event.HoverEvent;
import net.kyori.adventure.text.format.NamedTextColor;
import net.kyori.adventure.text.format.TextColor;
import net.kyori.adventure.text.format.TextDecoration;
import net.kyori.adventure.text.serializer.legacy.LegacyComponentSerializer;

/**
 * ClickableMessage
 * Gestisce la creazione e formattazione di messaggi chat complessi con supporto per
 * colori, stili, eventi di click (es. comandi) ed eventi di hover (es. tooltip).
 * Utilizza la libreria Adventure di Velocity per la composizione dei componenti.
 */
public class ClickableMessage {
   
   /*
      @Deprecated
      private static final String SPLITTER = "\\$";
      private static final char COLOR_CHAR = '§';
   */
   private List<TextComponent> components = new ArrayList<>();
   private List<ClickableMessage.MessageProps> messagePropsList = new ArrayList<>();
   private List<TextComponent> appendList = new ArrayList<>();
   private HashMap<Integer, Integer> appendIndexes = new HashMap<>();
   private boolean built = false;

   /**
    * ClickableMessage (Costruttore)
    * Analizza una stringa formattata con uno schema legacy specifico per separare parti testuali,
    * codici colore e proprietà speciali dei messaggi.
    * 
    * @param var1 La stringa grezza da parsare.
    */
   public ClickableMessage(String var1) {
      String[] var2 = var1.split("\\$");
      ClickableMessage.MessageProps var3 = new ClickableMessage.MessageProps();
      boolean var4 = false;
      StringBuilder var5 = new StringBuilder();
      int var6 = -1;
      int var7 = -1;
      String[] var8 = var2;
      int var9 = var2.length;

      for(int var10 = 0; var10 < var9; ++var10) {
         String var11 = var8[var10];
         ++var6;
         char[] var12 = var11.toCharArray();
         if (var12[var12.length - 1] == '}') {
            int var13;
            for(var13 = var12.length - 1; var13 >= 0 && var12[var13] != '{'; --var13) {
            }

            StringBuilder var14 = new StringBuilder();

            for(var7 = 100 + var6; var13 < var12.length; ++var13) {
               if (var12[var13] != '{' && var12[var13] != '}') {
                  var14.append(var12[var13]);
               }

               var12[var13] = 0;
            }

            String[] var15 = var14.toString().split(";");
            ClickableMessage.MessageProps var16 = new ClickableMessage.MessageProps();
            char[] var17 = var15[0].toCharArray();
            int var18 = var17.length;

            for(int var19 = 0; var19 < var18; ++var19) {
               char var20 = var17[var19];
               if (var20 != 167) {
                  var16.parse(var20);
               }
            }

            if (var15.length >= 2) {
               try {
                  var7 = Integer.parseInt(var15[1]);
               } catch (NumberFormatException var21) {
               }
            }

            this.messagePropsList.add(var16);
         }

         char[] var22 = var12;
         int var23 = var12.length;

         for(int var24 = 0; var24 < var23; ++var24) {
            char var25 = var22[var24];
            if (var25 != 0) {
               if (var25 == 167) {
                  var4 = true;
                  if (var5.length() != 0) {
                     this.components.add(var3.apply(Component.text(var5.toString())));
                     var5 = new StringBuilder();
                  }
               } else if (var4) {
                  var3.parse(var25);
                  var4 = false;
               } else {
                  var5.append(var25);
               }
            }
         }

         var4 = false;
         if (var5.length() != 0) {
            this.components.add(var3.apply(Component.text(var5.toString())));
            var5 = new StringBuilder();
         }

         if (var7 != -1) {
            this.appendIndexes.put(var7, this.components.size());
            var7 = -1;
         }
      }

   }

   private ClickableMessage append(TextComponent var1) {
      this.appendList.add(var1);
      return this;
   }

   /**
    * clickable
    * Inizia la creazione di una parte interattiva del messaggio a partire da una stringa base.
    * 
    * @param var1 Il testo che diventerà la parte cliccabile/formattata.
    * @return Un'istanza di ClickablePart per configurare l'interazione.
    */
   public ClickableMessage.ClickablePart clickable(String var1) {
      return new ClickableMessage.ClickablePart(this, var1);
   }

   /**
    * buildString
    * Compila il messaggio in un Component Adventure e lo serializza in formato stringa legacy.
    * 
    * @return La stringa formattata finale.
    */
   public String buildString() {
      // StringBuilder var1 = new StringBuilder(); UNUSED
      Component var2 = this.build();
      return LegacyComponentSerializer.legacySection().serialize(var2);
   }

   /**
    * build
    * Assembla tutte le componenti (testo statico, parti cliccabili, append) in un unico
    * oggetto Component di Velocity pronto per essere inviato in chat.
    * 
    * @return Il componente Adventure finale.
    */
   public Component build() {
      if (this.built) {
         TextComponent.Builder builder = Component.text();
         for(TextComponent comp : this.components) {
            builder.append(comp);
         }
         return builder.build();
      } else {
         TreeMap<Integer, Integer> var1 = new TreeMap<>(new Comparator<Integer>() {
            public int compare(Integer var1, Integer var2) {
               return (Integer)ClickableMessage.this.appendIndexes.get(var1) <= (Integer)ClickableMessage.this.appendIndexes.get(var2) ? 1 : -1;
            }
         });
         var1.putAll(this.appendIndexes);
         int var2 = this.appendList.size();
         Iterator<Entry<Integer, Integer>> var3 = var1.entrySet().iterator();

         while(var3.hasNext()) {
            Entry<Integer, Integer> var4 = var3.next();
            Integer var5 = var4.getKey();
            Integer var6 = var4.getValue();
            if (var5 >= 100) {
               if (var5 - 100 < var2) {
                  this.components.add(var6, this.appendList.get(var5 - 100));
               }
            } else if (var5 - 1 < var2) {
               this.components.add(var6, this.appendList.get(var5 - 1));
            }
         }

         this.built = true;
         TextComponent.Builder builder = Component.text();
         for(TextComponent comp : this.components) {
            builder.append(comp);
         }
         return builder.build();
      }
   }

   /**
    * MessageProps (Classe Interna)
    * Gestisce lo stato degli stili (colore, grassetto, corsivo, ecc.) durante il parsing del messaggio.
    */
   private class MessageProps {
      private TextColor color;
      private boolean bold;
      private boolean italic;
      private boolean strikeThrough;
      private boolean underlined;
      private boolean obfuscated;

      /**
       * MessageProps (Costruttore)
       * Inizializza le proprietà con i valori predefiniti (Bianco, senza stili).
       */
      private MessageProps() {
         this.color = NamedTextColor.WHITE;
         this.bold = false;
         this.italic = false;
         this.strikeThrough = false;
         this.underlined = false;
         this.obfuscated = false;
      }

      /**
       * resetStyle
       * Reimposta tutti gli stili (grassetto, corsivo, ecc.) a false preservando il colore.
       */
      private void resetStyle() {
         this.bold = false;
         this.italic = false;
         this.strikeThrough = false;
         this.underlined = false;
         this.obfuscated = false;
      }

      /**
       * resetColor
       * Imposta il colore della componente a Bianco (NamedTextColor.WHITE).
       */
      private void resetColor() {
         this.color = NamedTextColor.WHITE;
      }

      /**
       * reset
       * Ripristina lo stato iniziale sia del colore che degli stili.
       */
      private void reset() {
         this.resetColor();
         this.resetStyle();
      }

      /**
       * parse
       * Analizza un carattere di codice colore/stile legacy di Minecraft (es. 'l' per bold, 'c' per red)
       * e aggiorna lo stato delle proprietà correnti.
       * 
       * @param var1 Il codice carattere (senza il simbolo §).
       */
      private void parse(char var1) {
         switch(var1) {
         case 'k':
            this.obfuscated = true;
            return;
         case 'l':
            this.bold = true;
            return;
         case 'm':
            this.strikeThrough = true;
            return;
         case 'n':
            this.underlined = true;
            return;
         case 'o':
            this.italic = true;
            return;
         case 'r':
            this.reset();
         case 'p':
         case 'q':
         default:
            String colorCode = String.valueOf(var1);
            if ("0123456789abcdef".contains(colorCode)) {
                switch(colorCode) {
                    case "0": this.color = NamedTextColor.BLACK; break;
                    case "1": this.color = NamedTextColor.DARK_BLUE; break;
                    case "2": this.color = NamedTextColor.DARK_GREEN; break;
                    case "3": this.color = NamedTextColor.DARK_AQUA; break;
                    case "4": this.color = NamedTextColor.DARK_RED; break;
                    case "5": this.color = NamedTextColor.DARK_PURPLE; break;
                    case "6": this.color = NamedTextColor.GOLD; break;
                    case "7": this.color = NamedTextColor.GRAY; break;
                    case "8": this.color = NamedTextColor.DARK_GRAY; break;
                    case "9": this.color = NamedTextColor.BLUE; break;
                    case "a": this.color = NamedTextColor.GREEN; break;
                    case "b": this.color = NamedTextColor.AQUA; break;
                    case "c": this.color = NamedTextColor.RED; break;
                    case "d": this.color = NamedTextColor.LIGHT_PURPLE; break;
                    case "e": this.color = NamedTextColor.YELLOW; break;
                    case "f": this.color = NamedTextColor.WHITE; break;
                }
               this.resetStyle();
            }

         }
      }

      /* UNUSED */
      /*
      private void parse(String var1) {
         for(int var2 = 0; var2 < var1.length(); ++var2) {
            char var3 = var1.charAt(var2);
            this.parse(var3);
         }

      }
      */

      /* UNUSED */
      /*
      private void parse(ClickableMessage.MessageProps var1) {
         this.bold = var1.bold;
         this.italic = var1.italic;
         this.strikeThrough = var1.strikeThrough;
         this.underlined = var1.underlined;
         this.obfuscated = var1.obfuscated;
         this.color = var1.color;
      }
      */

      /**
       * apply
       * Applica le proprietà di stile correnti (colore, formattazione) a un componente testuale esistente.
       * 
       * @param var1 Il componente base a cui applicare lo stile.
       * @return Il nuovo componente formattato.
       */
      private TextComponent apply(TextComponent var1) {
         TextComponent.Builder builder = var1.toBuilder();
         builder.color(this.color);
         if (this.bold) builder.decorate(TextDecoration.BOLD);
         if (this.italic) builder.decorate(TextDecoration.ITALIC);
         if (this.strikeThrough) builder.decorate(TextDecoration.STRIKETHROUGH);
         if (this.underlined) builder.decorate(TextDecoration.UNDERLINED);
         if (this.obfuscated) builder.decorate(TextDecoration.OBFUSCATED);
         return builder.build();
      }

      /* UNUSED */
      /*
      private boolean isSame(ClickableMessage.MessageProps var1) {
         return this.bold == var1.bold && this.italic == var1.italic && this.strikeThrough == var1.strikeThrough && this.underlined == var1.underlined && this.obfuscated == var1.obfuscated && this.color == var1.color;
      }
      */
      /* UNUSED */
      /*
      public String toMineCraftString() {
         return this.color.toString() + (this.bold ? "§l" : "") + (this.italic ? "§o" : "") + (this.strikeThrough ? "§m" : "") + (this.underlined ? "§n" : "") + (this.obfuscated ? "§k" : "");
      }
      */

      /* UNUSED */
      /*
      public ClickableMessage.MessageProps copy() {
         ClickableMessage.MessageProps var1 = ClickableMessage.this.new MessageProps();
         var1.bold = this.bold;
         var1.italic = this.italic;
         var1.strikeThrough = this.strikeThrough;
         var1.underlined = this.underlined;
         var1.obfuscated = this.obfuscated;
         var1.color = this.color;
         return var1;
      }
      */

      /**
       * toString
       * Fornisce una rappresentazione testuale delle proprietà correnti (utile per debug).
       * 
       * @return La stringa descrittiva dei valori.
       */
      public String toString() {
         return "MessageProps{color=" + this.color + ", bold=" + this.bold + ", italic=" + this.italic + ", strikeThrough=" + this.strikeThrough + ", underlined=" + this.underlined + ", obfuscated=" + this.obfuscated + '}';
      }

      /* UNUSED */
      // $FF: synthetic method
      /*
      MessageProps(Object var2) {
         this();
      }
       */
   }

   public class ClickablePart {
      private ClickableMessage root;
      private TextComponent part;

      /**
       * ClickablePart (Costruttore)
       * Crea una nuova parte interattiva associata al messaggio principale.
       * 
       * @param var2 L'istanza radice di ClickableMessage.
       * @param var3 Il testo di questa parte.
       */
      private ClickablePart(ClickableMessage var2, String var3) {
         this.root = var2;
         this.part = Component.text(var3);
      }

      /**
       * clickEvent
       * Definisce l'azione da compiere al click del giocatore su questa parte di testo.
       * 
       * @param var1 Il tipo di azione (es. RUN_COMMAND, SUGGEST_COMMAND).
       * @param var2 Il valore associato all'azione (es. il comando da eseguire).
       * @return L'istanza corrente di ClickablePart (Fluent API).
       */
      public ClickableMessage.ClickablePart clickEvent(ClickEvent.Action var1, String var2) {
         this.part = this.part.clickEvent(ClickEvent.clickEvent(var1, var2));
         return this;
      }

      /**
       * hoverEvent
       * Definisce il testo da mostrare (tooltip) quando un giocatore passa con il mouse sopra questa parte.
       * 
       * @param var1 Il tipo di hover (es. SHOW_TEXT).
       * @param var2 Il contenuto testuale da mostrare nel tooltip.
       * @return L'istanza corrente di ClickablePart (Fluent API).
       */
      public ClickableMessage.ClickablePart hoverEvent(HoverEvent.Action<Component> var1, String var2) {
         this.part = this.part.hoverEvent(HoverEvent.hoverEvent(var1, LegacyComponentSerializer.legacySection().deserialize(var2)));
         return this;
      }

      /**
       * append
       * Finalizza la costruzione della parte cliccabile applicando gli stili rimanenti e
       * aggiungendola al corpo principale del messaggio.
       * 
       * @return L'istanza radice di ClickableMessage per continuare la costruzione.
       */
      public ClickableMessage append() {
         if (!this.root.messagePropsList.isEmpty()) {
            ClickableMessage.MessageProps var1 = (ClickableMessage.MessageProps)this.root.messagePropsList.remove(0);
            this.part = var1.apply(this.part);
         }

         this.root.append(this.part);
         return this.root;
      }

      /**
       * appendIgnoreProps
       * Finalizza la parte aggiungendola al corpo principale, ma scartando le proprietà
       * di stile residue non ancora applicate.
       * 
       * @return L'istanza radice di ClickableMessage.
       */
      public ClickableMessage appendIgnoreProps() {
         if (!this.root.messagePropsList.isEmpty()) {
            ClickableMessage.MessageProps var1 = (ClickableMessage.MessageProps)this.root.messagePropsList.remove(0);
         }

         this.root.append(this.part);
         return this.root;
      }

      /**
       * getPart
       * Restituisce l'oggetto TextComponent interno che rappresenta questa porzione di messaggio.
       * 
       * @return Il componente Adventure.
       */
      public TextComponent getPart() {
         return this.part;
      }

      // $FF: synthetic method
      ClickablePart(ClickableMessage var2, String var3, Object var4) {
         this(var2, var3);
      }
   }
}

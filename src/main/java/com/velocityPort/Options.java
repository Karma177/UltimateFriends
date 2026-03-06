package com.velocityPort;

/**
 * Options
 * Gestisce le preferenze personali di un giocatore (es. notifiche di join/leave, permessi di messaggistica).
 * Ogni profilo giocatore ha una propria istanza di Options caricata dal database.
 */
public class Options {
   private boolean SHOW_JOIN_MSG = true;
   private boolean SHOW_LEAVE_MSG = true;
   private boolean SHOW_SWITCH_MSG = true;
   private boolean ALLOW_REQUESTS = true;
   private boolean ALLOW_PRIVATE_MSG = true;
   private boolean SHOW_BROADCASTS = true;

   /**
    * Options (Costruttore)
    * Inizializza le opzioni caricando i valori predefiniti definiti nel file di configurazione globale.
    */
   public Options() {
      Config.Options.Values var1 = UltimateFriends.getConfig().getOptions().getDefaults();
      this.SHOW_JOIN_MSG = var1.show_msg_join();
      this.SHOW_LEAVE_MSG = var1.show_msg_left();
      this.SHOW_SWITCH_MSG = var1.show_msg_switch();
      this.ALLOW_REQUESTS = var1.allow_requests();
      this.ALLOW_PRIVATE_MSG = var1.allow_private_msg();
      this.SHOW_BROADCASTS = var1.show_broadcast();
   }

   /**
    * get
    * Recupera lo stato booleano di una specifica impostazione.
    * 
    * @param var1 Il tipo di opzione (Options.Type) da interrogare.
    * @return Lo stato attuale dell'opzione (true/false).
    * @throws IllegalArgumentException Se viene passato un tipo di opzione non riconosciuto.
    */
   public boolean get(Options.Type var1) {

      switch(var1) {
      case SHOW_JOIN_MSG:
         return this.SHOW_JOIN_MSG;
      case SHOW_LEAVE_MSG:
         return this.SHOW_LEAVE_MSG;
      case SHOW_SWITCH_MSG:
         return this.SHOW_SWITCH_MSG;
      case ALLOW_REQUESTS:
         return this.ALLOW_REQUESTS;
      case ALLOW_PRIVATE_MSG:
         return this.ALLOW_PRIVATE_MSG;
      case SHOW_BROADCASTS:
         return this.SHOW_BROADCASTS;
      default:
         throw new IllegalArgumentException("Unknown type");
      }
   }

   /**
    * set
    * Modifica lo stato di una specifica impostazione per il giocatore.
    * 
    * @param var1 Il tipo di opzione da modificare.
    * @param var2 Il nuovo valore booleano da impostare.
    * @throws IllegalArgumentException Se viene passato un tipo di opzione non riconosciuto.
    */
   public void set(Options.Type var1, boolean var2) {

      switch(var1) {
      case SHOW_JOIN_MSG:
         this.SHOW_JOIN_MSG = var2;
         return;
      case SHOW_LEAVE_MSG:
         this.SHOW_LEAVE_MSG = var2;
         return;
      case SHOW_SWITCH_MSG:
         this.SHOW_SWITCH_MSG = var2;
         return;
      case ALLOW_REQUESTS:
         this.ALLOW_REQUESTS = var2;
         return;
      case ALLOW_PRIVATE_MSG:
         this.ALLOW_PRIVATE_MSG = var2;
         return;
      case SHOW_BROADCASTS:
         this.SHOW_BROADCASTS = var2;
         return;
      default:
         throw new IllegalArgumentException("Unknown type");
      }
   }

   /**
    * values
    * Estrae tutti i valori delle opzioni in un array di oggetti, 
    * utile per la serializzazione o il salvataggio massivo.
    * 
    * @return Un array Object[] contenente gli stati booleani di tutte le opzioni.
    */
   public Object[] values() {
      Options.Type[] var1 = Options.Type.values();
      Object[] var2 = new Object[var1.length];

      for(int var3 = 0; var3 < var1.length; ++var3) {
         var2[var3] = this.get(var1[var3]);
      }

      return var2;
   }

   /**
    * Type (Enum)
    * Elenco delle opzioni disponibili con relativi nodi di configurazione e messaggi associati.
    */
   public static enum Type {
      SHOW_JOIN_MSG("show_msg_join", Message.OPTIONS_OPT_SHOW_JOIN),
      SHOW_LEAVE_MSG("show_msg_left", Message.OPTIONS_OPT_SHOW_LEAVE),
      SHOW_SWITCH_MSG("show_msg_switch", Message.OPTIONS_OPT_SHOW_SWITCH),
      ALLOW_REQUESTS("allow_requests", Message.OPTIONS_OPT_ALLOW_REQUESTS),
      ALLOW_PRIVATE_MSG("allow_private_msg", Message.OPTIONS_OPT_ALLOW_PRIVATE_MSG),
      SHOW_BROADCASTS("show_broadcast", Message.OPTIONS_OPT_SHOW_BROADCAST);

      private String node;
      private Message msg;

      /**
       * Type (Costruttore Enum)
       * 
       * @param var3 Il nome tecnico dell'opzione utilizzato nel database/config.
       * @param var4 Il messaggio di traduzione associato per visualizzarlo in-game.
       */
      private Type(String var3, Message var4) {
         this.node = var3;
         this.msg = var4;
      }

      /**
       * getNodes
       * Genera un array di tutti i nomi tecnici delle colonne/nodi delle opzioni.
       * 
       * @return Un array di stringhe con i nomi dei nodi.
       */
      public static String[] getNodes() {
         Options.Type[] var0 = values();
         String[] var1 = new String[var0.length];

         for(int var2 = 0; var2 < var0.length; ++var2) {
            var1[var2] = var0[var2].getNode();
         }

         return var1;
      }

      /**
       * getNode
       * Ottiene il nome tecnico del nodo per l'istanza corrente.
       * 
       * @return Il nome del nodo (String).
       */
      public String getNode() {
         return this.node;
      }

      /**
       * getMsg
       * Ottiene il riferimento al messaggio di lingua associato all'opzione.
       * 
       * @return L'oggetto Message corrispondente.
       */
      public Message getMsg() {
         return this.msg;
      }
   }
}

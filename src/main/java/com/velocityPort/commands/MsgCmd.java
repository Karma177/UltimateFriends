package com.velocityPort.commands;

import com.velocitypowered.api.command.SimpleCommand;
import com.velocitypowered.api.command.CommandSource;
import com.velocitypowered.api.proxy.Player;
import com.velocityPort.UltimateFriends;
import com.velocityPort.Utils;

/**
 * MsgCmd
 * Comando per l'invio rapido di messaggi privati (es. /msg).
 * Reindirizza l'esecuzione alla logica centralizzata in Cmds.
 */
public class MsgCmd implements SimpleCommand {
   private String cmd;

   /**
    * MsgCmd
    * Costruttore del comando messaggio.
    * 
    * @param var1 Il nome del comando configurato.
    */
   public MsgCmd(String var1) {
      this.cmd = var1;
   }

   /**
    * execute
    * Punto di ingresso per Velocity. Concatena gli argomenti e invoca la logica msg di Cmds.
    * 
    * @param invocation Oggetto contenente sorgente e argomenti.
    */
   @Override
   public void execute(Invocation invocation) {
      CommandSource var1 = invocation.source();
      String[] var2 = invocation.arguments();

      if (var1 instanceof Player) {
         if (var2.length >= 2) {
            String[] var3 = (String[])Utils.concat((Object[])(new String[]{"msg"}), (Object[])var2);
            UltimateFriends.getConfig().getCmds().execute(var1, var3);
         }
      }
   }

   /**
    * getCmd
    * Restituisce il nome del comando.
    * 
    * @return Il comando principale.
    */
   public String getCmd() {
      return this.cmd;
   }
}

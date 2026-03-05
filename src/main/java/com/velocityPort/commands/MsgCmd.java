package com.velocityPort.commands;

import com.velocitypowered.api.command.SimpleCommand;
import com.velocitypowered.api.command.CommandSource;
import com.velocitypowered.api.proxy.Player;
import com.velocityPort.UltimateFriends;
import com.velocityPort.Utils;

public class MsgCmd implements SimpleCommand {
   private String cmd;

   public MsgCmd(String var1) {
      this.cmd = var1;
   }

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

   public String getCmd() {
      return this.cmd;
   }
}

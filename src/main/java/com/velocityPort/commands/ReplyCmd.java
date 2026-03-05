package com.velocityPort.commands;

import com.velocitypowered.api.command.SimpleCommand;
import com.velocitypowered.api.command.CommandSource;
import com.velocitypowered.api.proxy.Player;
import com.velocityPort.PlayerProfile;
import com.velocityPort.UltimateFriends;
import com.velocityPort.Utils;

public class ReplyCmd implements SimpleCommand {
   private String cmd;

   public ReplyCmd(String var1) {
      this.cmd = var1;
   }

   @Override
   public void execute(Invocation invocation) {
      CommandSource var1 = invocation.source();
      String[] var2 = invocation.arguments();

      if (var1 instanceof Player) {
         Player var3 = (Player)var1;
         PlayerProfile var4 = UltimateFriends.getPlayerProfile(var3.getUsername());
         if (var4 != null) {
            String var5 = var4.getLastMsgSender();
            if (var5 != null) {
               String[] var6 = (String[])Utils.concat((Object[])(new String[]{"msg", var5}), (Object[])var2);
               UltimateFriends.getConfig().getCmds().execute(var3, var6);
            }

         }
      }
   }

   public String getCmd() {
      return this.cmd;
   }
}

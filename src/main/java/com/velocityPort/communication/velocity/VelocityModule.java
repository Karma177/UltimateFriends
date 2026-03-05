package com.velocityPort.communication.velocity;

import java.util.Iterator;
import java.util.List;

import javax.annotation.Nullable;

import com.velocityPort.ClickableMessage;
import com.velocityPort.Friend;
import com.velocityPort.Message;
import com.velocityPort.Options;
import com.velocityPort.PlayerProfile;
import com.velocityPort.SocialSpy;
import com.velocityPort.UltimateFriends;
import com.velocityPort.Utils;
import com.velocityPort.communication.CommunicationModule;
import com.velocityPort.exceptions.CannotAddYourself;
import com.velocityPort.exceptions.ConnectionDisabledOnServer;
import com.velocityPort.exceptions.FriendListExceeded;
import com.velocityPort.exceptions.FriendOnDisabledServer;
import com.velocityPort.exceptions.PlayerAlreadyFriend;
import com.velocityPort.exceptions.PlayerAlreadyRequested;
import com.velocityPort.exceptions.PlayerDenied;
import com.velocityPort.exceptions.PlayerIsOffline;
import com.velocityPort.exceptions.PlayerNotFriend;

import net.kyori.adventure.text.event.HoverEvent.Action;
import com.velocitypowered.api.proxy.Player;
import com.velocitypowered.api.proxy.server.RegisteredServer;
import com.velocitypowered.api.event.EventManager;

public class VelocityModule implements CommunicationModule {

   private JoinListener joinListener;
   private DisconnectListener disconnectListener;
   private ServerSwitchListener serverSwitchListener;

   public void registerListeners() {
      EventManager var1 = UltimateFriends.server.getEventManager();
      this.joinListener = new JoinListener();
      this.disconnectListener = new DisconnectListener();
      this.serverSwitchListener = new ServerSwitchListener();
      var1.register(UltimateFriends.plugin, this.joinListener);
      var1.register(UltimateFriends.plugin, this.disconnectListener);
      var1.register(UltimateFriends.plugin, this.serverSwitchListener);
   }

   public boolean isOnline(String var1) {
      return UltimateFriends.server.getPlayer(var1).isPresent();
   }

   public String getServer(String var1) {
      return this.getServer(UltimateFriends.server.getPlayer(var1).orElse(null));
   }

   public String getServer(Player var1) {
      if (var1 == null) {
         return null;
      } else {
         RegisteredServer var2 = var1.getCurrentServer().map(connection -> connection.getServer()).orElse(null);
         return var2 == null ? null : var2.getServerInfo().getName();
      }
   }

   public void sendFriendMessage(PlayerProfile var1, String var2, String var3) throws PlayerDenied, FriendOnDisabledServer, PlayerIsOffline, PlayerNotFriend {
      Friend var4 = var1.getFriend(var2);
      if (var4 != null) {
         PlayerProfile var5 = UltimateFriends.getPlayerProfile(var2);
         if (var5 != null) {
            if (!var5.getOptions().get(Options.Type.ALLOW_PRIVATE_MSG)) {
               throw new PlayerDenied();
            } else {
               Player var6 = UltimateFriends.server.getPlayer(var5.getPlayerName()).orElse(null);
               if (var6 != null) {
                  String var7 = this.getServer(var6);
                  if (var7 != null && !UltimateFriends.getConfig().getDisable().getPlugin().contains(var7)) {
                     Utils.sendMessage(var6, (new ClickableMessage(Message.PRIVATE_MSG_FROM.getMsg(true))).clickable(var1.getPlayerName()).hoverEvent(Action.SHOW_TEXT, (new ClickableMessage(Message.FRIEND_LIST_BUTTON_MSG_HOVER.getMsg())).clickable(var1.getPlayerName()).append().buildString()).clickEvent(net.kyori.adventure.text.event.ClickEvent.Action.SUGGEST_COMMAND, "/" + UltimateFriends.getConfig().getCmds().getCmd() + " msg " + var1.getPlayerName() + " ").append().clickable(var3).append().build());
                     var5.setLastMsgSender(var1.getPlayerName());
                     SocialSpy.spy(var1.getPlayerName(), var2, var3);
                  } else {
                     throw new FriendOnDisabledServer();
                  }
               } else {
                  SocialSpy.spy(var1.getPlayerName(), var2, var3);
                  throw new PlayerIsOffline();
               }
            }
         } else {
            SocialSpy.spy(var1.getPlayerName(), var2, var3);
            throw new PlayerIsOffline();
         }
      } else {
         throw new PlayerNotFriend();
      }
   }

   public void sendFriendBroadcastMessage(PlayerProfile var1, String var2) {
      this.sendFriendBroadcastMessage0(var1, var2, (List)null);
      SocialSpy.spy(var1.getPlayerName(), "(Broadcast)", var2);
   }

   protected void sendFriendBroadcastMessage0(PlayerProfile var1, String var2, @Nullable List<String> var3) {
      Iterator var4 = var1.getFriends().iterator();

      while(var4.hasNext()) {
         Friend var5 = (Friend)var4.next();
         PlayerProfile var6 = UltimateFriends.getPlayerProfile(var5.getPlayerName());
         if (var6 != null) {
            if (var6.getOptions().get(Options.Type.SHOW_BROADCASTS)) {
               Player var7 = UltimateFriends.server.getPlayer(var6.getPlayerName()).orElse(null);
               if (var7 != null) {
                  String var8 = this.getServer(var7);
                  if (var8 != null && !UltimateFriends.getConfig().getDisable().getPlugin().contains(var8)) {
                     Utils.sendMessage(var7, (new ClickableMessage(Message.BROADCAST_FROM.getMsg(true))).clickable(var1.getPlayerName()).hoverEvent(Action.SHOW_TEXT, (new ClickableMessage(Message.FRIEND_LIST_BUTTON_MSG_HOVER.getMsg())).clickable(var1.getPlayerName()).append().buildString()).clickEvent(net.kyori.adventure.text.event.ClickEvent.Action.SUGGEST_COMMAND, "/" + UltimateFriends.getConfig().getCmds().getCmd() + " msg " + var1.getPlayerName() + " ").append().clickable(var2).append().build());
                  }
               } else if (var3 != null) {
                  var3.add(var5.getPlayerName());
               }
            }
         } else if (var3 != null) {
            var3.add(var5.getPlayerName());
         }
      }

   }

   public void removeFriend(PlayerProfile var1, Friend var2) throws PlayerNotFriend {
      if (!var1.getFriends().contains(var2)) {
         throw new PlayerNotFriend();
      } else if (!UltimateFriends.getStorage().removeFriend(var1, var2)) {
         throw new RuntimeException("cannot save removed friendship");
      } else {
         PlayerProfile var3 = UltimateFriends.getPlayerProfile(var2.getPlayerName());
         if (var3 != null) {
            Friend var4 = var3.getFriend(var1.getPlayerName());
            if (var4 != null) {
               var3.getFriends().remove(var4);
            }

            Player var5 = UltimateFriends.server.getPlayer(var3.getPlayerName()).orElse(null);
            if (var5 != null) {
               Utils.sendMessage(var5, (new ClickableMessage(Message.FRIEND_REMOVED_YOU.getMsg(true))).clickable(var1.getPlayerName()).append().build());
            }
         }

         var1.getFriends().remove(var2);
      }
   }

   public boolean addFriend(PlayerProfile var1, String var2) throws CannotAddYourself, FriendListExceeded, PlayerAlreadyFriend, PlayerAlreadyRequested, FriendOnDisabledServer, PlayerDenied, PlayerIsOffline {
      if (var1.getPlayerName().equalsIgnoreCase(var2)) {
         throw new CannotAddYourself();
      } else {
         Player var1Player = UltimateFriends.server.getPlayer(var1.getPlayerName()).orElse(null);
         if (var1Player != null && var1.getFriends().size() >= UltimateFriends.getConfig().getMaxFriends(var1Player)) {
            throw new FriendListExceeded();
         } else {
            Friend var3 = var1.getFriend(var2);
            if (var3 != null) {
            throw new PlayerAlreadyFriend();
         } else if (Utils.containsIgnoreCase(var1.getSentRequests(), var2)) {
            throw new PlayerAlreadyRequested();
         } else {
            PlayerProfile var4 = UltimateFriends.getPlayerProfile(var2);
            if (var4 != null) {
               if (!var4.getOptions().get(Options.Type.ALLOW_REQUESTS)) {
                  throw new PlayerDenied();
               } else if (Utils.containsIgnoreCase(var4.getRequests(), var1.getPlayerName())) {
                  var1.getSentRequests().add(var2);
                  throw new PlayerAlreadyRequested();
               } else {
                  Player var5 = UltimateFriends.server.getPlayer(var4.getPlayerName()).orElse(null);
                  if (var5 != null) {
                     String serverName5 = this.getServer(var5);
                     if (serverName5 != null && UltimateFriends.getConfig().getDisable().getPlugin().contains(serverName5)) {
                        throw new FriendOnDisabledServer();
                     } else if (var4.getFriends().size() >= UltimateFriends.getConfig().getMaxFriends(var5)) {
                        return false;
                     } else if (Utils.containsIgnoreCase(var1.getRequests(), var2)) {
                        var1.getRequests().remove(var2);
                        var1.getSentRequests().remove(var2);
                        var4.getRequests().remove(var1.getPlayerName());
                        var4.getSentRequests().remove(var1.getPlayerName());
                        if (!UltimateFriends.getStorage().addFriend(var1, var4)) {
                           throw new RuntimeException("cannot save new friendship");
                        } else {
                           var1.getFriends().add(new Friend(var4));
                           var4.getFriends().add(new Friend(var1));
                           Utils.sendMessage(var5, (new ClickableMessage(Message.FRIEND_ADDED.getMsg(true))).clickable(var1.getPlayerName()).append().build());
                           return true;
                        }
                     } else {
                        var1.getSentRequests().add(var2);
                        var4.getRequests().add(var1.getPlayerName());
                        Utils.sendMessage(var5, (new ClickableMessage(Message.FRIEND_REQUEST.getMsg(true))).clickable(var1.getPlayerName()).append().clickable(Message.FRIEND_REQUEST_BUTTON_ACCEPT_TEXT.getMsg()).hoverEvent(Action.SHOW_TEXT, Message.FRIEND_REQUEST_BUTTON_ACCEPT_HOVER.getMsg()).clickEvent(net.kyori.adventure.text.event.ClickEvent.Action.RUN_COMMAND, "/" + UltimateFriends.getConfig().getCmds().getCmd() + " add " + var1.getPlayerName()).append().build());
                        return false;
                     }
                  } else {
                     throw new PlayerIsOffline();
                  }
               }
            } else {
               throw new PlayerIsOffline();
            }
         }
      }
   }
   }

   public void connect(PlayerProfile var1, String var2) throws PlayerNotFriend, FriendOnDisabledServer, ConnectionDisabledOnServer, PlayerIsOffline {
      Friend var3 = var1.getFriend(var2);
      if (var3 == null) {
         throw new PlayerNotFriend();
      } else {
         PlayerProfile var4 = UltimateFriends.getPlayerProfile(var2);
         if (var4 != null) {
            Player var5 = UltimateFriends.server.getPlayer(var4.getPlayerName()).orElse(null);
            if (var5 != null) {
               String srvName = this.getServer(var5);
               if (srvName != null && UltimateFriends.getConfig().getDisable().getPlugin().contains(srvName)) {
                  throw new FriendOnDisabledServer();
               } else if (srvName != null && UltimateFriends.getConfig().getDisable().getConnection().contains(srvName)) {
                  throw new ConnectionDisabledOnServer();
               } else {
                  Player var6 = UltimateFriends.server.getPlayer(var1.getPlayerName()).orElse(null);
                  var5.getCurrentServer().ifPresent(srv -> {
                     Utils.safeConnect(var6, srv.getServer());
                  });
               }
            } else {
               throw new PlayerIsOffline();
            }
         } else {
            throw new PlayerIsOffline();
         }
      }
   }

   public void unregisterListeners() {
      EventManager var1 = UltimateFriends.server.getEventManager();
      if (this.joinListener != null) {
         var1.unregisterListener(UltimateFriends.plugin, this.joinListener);
      }
      if (this.disconnectListener != null) {
         var1.unregisterListener(UltimateFriends.plugin, this.disconnectListener);
      }
      if (this.serverSwitchListener != null) {
         var1.unregisterListener(UltimateFriends.plugin, this.serverSwitchListener);
      }
   }
}

package com.velocityPort.communication;

import com.velocityPort.Friend;
import com.velocityPort.PlayerProfile;
import com.velocityPort.exceptions.CannotAddYourself;
import com.velocityPort.exceptions.ConnectionDisabledOnServer;
import com.velocityPort.exceptions.FriendListExceeded;
import com.velocityPort.exceptions.FriendOnDisabledServer;
import com.velocityPort.exceptions.PlayerAlreadyFriend;
import com.velocityPort.exceptions.PlayerAlreadyRequested;
import com.velocityPort.exceptions.PlayerDenied;
import com.velocityPort.exceptions.PlayerIsOffline;
import com.velocityPort.exceptions.PlayerNotFriend;

public interface CommunicationModule {
   void registerListeners();

   void unregisterListeners();

   boolean isOnline(String var1);

   String getServer(String var1);

   void sendFriendMessage(PlayerProfile var1, String var2, String var3) throws PlayerIsOffline, PlayerNotFriend, PlayerDenied, FriendOnDisabledServer;

   void sendFriendBroadcastMessage(PlayerProfile var1, String var2);

   void removeFriend(PlayerProfile var1, Friend var2) throws PlayerNotFriend;

   boolean addFriend(PlayerProfile var1, String var2) throws PlayerIsOffline, PlayerAlreadyFriend, CannotAddYourself, PlayerDenied, FriendOnDisabledServer, PlayerAlreadyRequested, FriendListExceeded;

   void connect(PlayerProfile var1, String var2) throws PlayerIsOffline, FriendOnDisabledServer, PlayerNotFriend, ConnectionDisabledOnServer;
}

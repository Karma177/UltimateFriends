package com.velocityPort.storage;

import java.util.List;
import java.util.UUID;

import com.velocityPort.CheckReturnValue;
import com.velocityPort.Friend;
import com.velocityPort.Options;
import com.velocityPort.PlayerID;
import com.velocityPort.PlayerProfile;

public interface Storage {
   @CheckReturnValue
   boolean connect();

   boolean disconnect();

   @CheckReturnValue
   PlayerProfile loadPlayerProfile(String var1, UUID var2);

   @CheckReturnValue
   Options getOptions(PlayerProfile var1);

   boolean saveOptions(PlayerProfile var1);

   @CheckReturnValue
   List<Friend> getFriends(PlayerProfile var1);

   @CheckReturnValue
   boolean addFriend(PlayerProfile var1, PlayerID var2);

   @CheckReturnValue
   boolean removeFriend(PlayerProfile var1, PlayerID var2);
}

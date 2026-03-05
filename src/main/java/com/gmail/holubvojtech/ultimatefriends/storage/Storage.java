package com.gmail.holubvojtech.ultimatefriends.storage;

import java.util.List;
import java.util.UUID;

import com.gmail.holubvojtech.ultimatefriends.CheckReturnValue;
import com.gmail.holubvojtech.ultimatefriends.Friend;
import com.gmail.holubvojtech.ultimatefriends.Options;
import com.gmail.holubvojtech.ultimatefriends.PlayerID;
import com.gmail.holubvojtech.ultimatefriends.PlayerProfile;

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

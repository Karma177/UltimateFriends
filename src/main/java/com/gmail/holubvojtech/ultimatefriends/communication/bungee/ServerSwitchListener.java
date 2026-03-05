package com.gmail.holubvojtech.ultimatefriends.communication.bungee;

import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import com.gmail.holubvojtech.ultimatefriends.ClickableMessage;
import com.gmail.holubvojtech.ultimatefriends.Config.ServerAliases;
import com.gmail.holubvojtech.ultimatefriends.Message;
import com.gmail.holubvojtech.ultimatefriends.Options;
import com.gmail.holubvojtech.ultimatefriends.PlayerProfile;
import com.gmail.holubvojtech.ultimatefriends.UltimateFriends;
import com.gmail.holubvojtech.ultimatefriends.Utils;

import net.md_5.bungee.api.chat.ClickEvent.Action;
import net.md_5.bungee.api.connection.ProxiedPlayer;
import net.md_5.bungee.api.connection.Server;
import net.md_5.bungee.api.event.PlayerDisconnectEvent;
import net.md_5.bungee.api.event.ServerConnectEvent;
import net.md_5.bungee.api.event.ServerConnectedEvent;
import net.md_5.bungee.api.plugin.Listener;
import net.md_5.bungee.event.EventHandler;
import net.md_5.bungee.event.EventPriority;

public class ServerSwitchListener implements Listener {

	private Map<ProxiedPlayer, String> changingPlayers;

	public ServerSwitchListener() {
		this.changingPlayers = new HashMap<>();
	}

	@EventHandler(priority = EventPriority.HIGHEST)
	public void onServerConnecting(ServerConnectEvent event) {
		if (event.isCancelled()) {
			return;
		}

		ProxiedPlayer player = event.getPlayer();
		Server server = player.getServer();
		if (server != null) {
			changingPlayers.put(player, server.getInfo().getName());
		}
	}

	@EventHandler
	public void onServerConnected(ServerConnectedEvent event) {
		Server server = event.getServer();
		if (server == null) {
			return;
		}

		ProxiedPlayer player = event.getPlayer();
		String name = player.getName();
		PlayerProfile profile = UltimateFriends.getPlayerProfile(name);
		if (profile == null) {
			return;
		}

		String from = changingPlayers.get(player);
		if (from == null) {
			return;
		}

		sendSwitchMsg(name, from, server.getInfo().getName(), Utils.toStringList(profile.getFriends()));
	}

	@EventHandler
	public void on(PlayerDisconnectEvent event) {
		changingPlayers.remove(event.getPlayer());
	}

	private void sendSwitchMsg(String playerName, String from, String to, List<String> friends) {
		ServerAliases aliases = UltimateFriends.getConfig().getServerAliases();
		from = aliases.translate(from);
		to = aliases.translate(to);
		Iterator<String> friendsIterator = friends.iterator();

		while (friendsIterator.hasNext()) {
			String friendName = friendsIterator.next();
			ProxiedPlayer friend = UltimateFriends.server.getPlayer(friendName);
			if (friend == null) {
				continue;
			}

			PlayerProfile profile = UltimateFriends.getPlayerProfile(friendName);
			if (profile != null && profile.getOptions().get(Options.Type.SHOW_SWITCH_MSG)) {
				friend.sendMessage(new ClickableMessage(Message.FRIEND_SWITCH_SERVER.getMsg(true)).clickable(playerName).append().clickable(from).append().clickable(to).clickEvent(Action.RUN_COMMAND, "/" + UltimateFriends.getConfig().getCmds().getCmd() + " connect " + playerName).hoverEvent(net.md_5.bungee.api.chat.HoverEvent.Action.SHOW_TEXT, (new ClickableMessage(Message.FRIEND_LIST_BUTTON_CONNECT_HOVER.getMsg())).clickable(to).append().clickable(playerName).append().buildString()).append().build());
			}
		}
	}
}

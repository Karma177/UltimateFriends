package com.velocityPort.communication.velocity;

import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import com.velocityPort.ClickableMessage;
import com.velocityPort.Config.ServerAliases;
import com.velocityPort.Message;
import com.velocityPort.Options;
import com.velocityPort.PlayerProfile;
import com.velocityPort.UltimateFriends;
import com.velocityPort.Utils;

import com.velocitypowered.api.proxy.Player;
import com.velocitypowered.api.proxy.server.RegisteredServer;
import com.velocitypowered.api.event.connection.DisconnectEvent;
import com.velocitypowered.api.event.player.ServerPreConnectEvent;
import com.velocitypowered.api.event.player.ServerConnectedEvent;
import com.velocitypowered.api.event.Subscribe;
import net.kyori.adventure.text.event.ClickEvent;
import net.kyori.adventure.text.event.HoverEvent;

public class ServerSwitchListener {

	private Map<Player, String> changingPlayers;

	public ServerSwitchListener() {
		this.changingPlayers = new HashMap<>();
	}

	@Subscribe
	public void onServerConnecting(ServerPreConnectEvent event) {
		if (!event.getResult().isAllowed()) {
			return;
		}

		Player player = event.getPlayer();
		player.getCurrentServer().ifPresent(server -> {
			changingPlayers.put(player, server.getServerInfo().getName());
		});
	}

	@Subscribe
	public void onServerConnected(ServerConnectedEvent event) {
		RegisteredServer server = event.getServer();
		if (server == null) {
			return;
		}

		Player player = event.getPlayer();
		String name = player.getUsername();
		PlayerProfile profile = UltimateFriends.getPlayerProfile(name);
		if (profile == null) {
			return;
		}

		String from = changingPlayers.get(player);
		if (from == null) {
			return;
		}

		sendSwitchMsg(name, from, server.getServerInfo().getName(), Utils.toStringList(profile.getFriends()));
	}

	@Subscribe
	public void on(DisconnectEvent event) {
		changingPlayers.remove(event.getPlayer());
	}

	private void sendSwitchMsg(String playerName, String from, String to, List<String> friends) {
		ServerAliases aliases = UltimateFriends.getConfig().getServerAliases();
		from = aliases.translate(from);
		to = aliases.translate(to);
		Iterator<String> friendsIterator = friends.iterator();

		while (friendsIterator.hasNext()) {
			String friendName = friendsIterator.next();
			Player friend = UltimateFriends.server.getPlayer(friendName).orElse(null);
			if (friend == null) {
				continue;
			}

			PlayerProfile profile = UltimateFriends.getPlayerProfile(friendName);
			if (profile != null && profile.getOptions().get(Options.Type.SHOW_SWITCH_MSG)) {
				Utils.sendMessage(friend, new ClickableMessage(Message.FRIEND_SWITCH_SERVER.getMsg(true)).clickable(playerName).append().clickable(from).append().clickable(to).clickEvent(ClickEvent.Action.RUN_COMMAND, "/" + UltimateFriends.getConfig().getCmds().getCmd() + " connect " + playerName).hoverEvent(HoverEvent.Action.SHOW_TEXT, (new ClickableMessage(Message.FRIEND_LIST_BUTTON_CONNECT_HOVER.getMsg())).clickable(to).append().clickable(playerName).append().buildString()).append().build());
			}
		}
	}
}

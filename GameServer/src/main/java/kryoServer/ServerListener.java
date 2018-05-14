package kryoServer;

import java.util.HashMap;

import com.esotericsoftware.kryonet.Connection;
import com.esotericsoftware.kryonet.Listener;

public class ServerListener extends Listener {

	private HashMap<Connection, Connection> activePlayers = new HashMap<>();
	private HashMap<String, Connection> notConnectedPalyers = new HashMap<>();
	private String ip1;
	private String ip2;

	@Override
	public void connected(Connection connection) {
		super.connected(connection);
	}

	@Override
	public void received(Connection connection, Object object) {
		try {
			if (object instanceof String) {
				String[] str = ((String) object).split(";");
				if (notConnectedPalyers.containsKey(str[1])) {
					activePlayers.put(connection, notConnectedPalyers.get(str[1]));
					activePlayers.put(notConnectedPalyers.get(str[1]), connection);

					ip1 = connection.getRemoteAddressTCP().toString();
					ip1 = ip1.substring(ip1.indexOf("/") + 1, ip1.indexOf(":"));

					ip2 = notConnectedPalyers.get(str[1]).getRemoteAddressTCP().toString();
					ip2 = ip2.substring(ip2.indexOf("/") + 1, ip2.indexOf(":"));

					if (ip1.equals(ip2)) {

						connection.sendTCP("start server");
						notConnectedPalyers.get(str[1])
								.sendTCP("start client;" + str[2] + ";" + str[3] + ";" + str[4] + ";");

					} else {
						connection.sendTCP("connected");
						notConnectedPalyers.get(str[1]).sendTCP("connected");
					}

					notConnectedPalyers.remove(str[1]);
				} else {
					notConnectedPalyers.put(str[0], connection);
					connection.sendTCP("wait for oponent");
				}

			}

			if (object instanceof Packet) {
				activePlayers.get(connection).sendTCP(object);
			}
		} catch (Exception e) {

		}
	}

	@Override
	public void disconnected(Connection connection) {

		if (activePlayers.containsKey(connection)) {
			try {
				activePlayers.get(connection).sendTCP("oponent disconnected");
			} catch (Exception e) {

			}

			activePlayers.remove(connection);
		} else {
			try {
				notConnectedPalyers.values().remove(connection);
			} catch (Exception e) {

			}
		}

	}

}

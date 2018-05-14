package kryoServer;

import com.esotericsoftware.kryo.Kryo;
import com.esotericsoftware.kryonet.Server;

public class KryoServer {

	public static void stertKryoServer() {

		try {
			ServerListener listener = new ServerListener();
			// Main Server
			Server server = new Server();
			server.start();
			server.bind(7070, 7071);

			Kryo kryo = server.getKryo();
			kryo.register(String.class);
			kryo.register(Packet.class);

			server.addListener(listener);

			// Backup Server
			Server backupServer = new Server();
			backupServer.start();
			backupServer.bind(9090);

			Kryo kryo1 = backupServer.getKryo();
			kryo1.register(String.class);
			kryo1.register(Packet.class);

			backupServer.addListener(listener);

		} catch (Exception e) {
			e.printStackTrace();
		}

	}

}

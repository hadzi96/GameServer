package gameServer;

import java.net.ServerSocket;
import java.net.Socket;
import java.util.HashMap;
import java.util.concurrent.locks.ReentrantLock;

import kryoServer.KryoServer;

public class Server {

	private HashMap<String, User> playerPool = new HashMap<>(); // KEY = username ; VALUE = player
	private HashMap<String, String> users = new HashMap<>(); // KEY = username; VLUE = password
	private ReentrantLock lock = new ReentrantLock();

	public Server() throws Exception {

		KryoServer.stertKryoServer();

		int port = 6000;
		ServerSocket ss = new ServerSocket(port);
		System.out.println("Server running on port: " + port);

		while (true) {
			Socket sock = ss.accept();

			ServerThread st = new ServerThread(sock, this);
			Thread thread = new Thread(st);
			thread.start();
		}

	}

	public boolean register(String username, String password) {
		lock.lock();
		if (users.containsKey(username)) {
			lock.unlock();
			return false;
		}
		users.put(username, password);

		lock.unlock();
		return true;
	}

	public boolean logIn(String username, String password) {
		lock.lock();
		if (users.containsKey(username)) {
			if (users.get(username).equals(password)) {
				lock.unlock();
				return true;
			}
		}

		lock.unlock();
		return false;
	}

	public synchronized void matchmaking(String command, User user) {

		if (command.equals("remove")) {
			if (user.oponent.equals("random")) {
				playerPool.remove("random", user);
			} else {
				playerPool.remove(user.username, user);
			}

			return;
		}

		if (user.oponent.equals("random")) { // matchmaking with random oponent
			if (!playerPool.containsKey("random")) {
				playerPool.put("random", user);
				user.out.println("wait for player");
			} else {
				User user1 = playerPool.get("random");
				playerPool.remove("random");
				P2PBinder.bind(user1, user);
			}

		} else { // matchmaking with specific user
			if (!playerPool.containsKey(user.oponent)) {
				playerPool.put(user.username, user);
				user.out.println("wait for player");
			} else {
				User user1 = playerPool.get(user.oponent);
				if (!user1.username.equals(user.oponent)) // ne slazu im se zahtevi
					return;

				playerPool.remove(user.oponent);
				P2PBinder.bind(user1, user);
			}

		}

	}

	public static void main(String[] args) {
		try {
			new Server();
		} catch (Exception e) {
			e.printStackTrace();
		}

	}

}

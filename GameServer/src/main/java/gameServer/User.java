package gameServer;

import java.io.PrintWriter;

public class User {

	public String username;
	public String oponent;
	public PrintWriter out;

	public User(String username, String oponent, PrintWriter out) {
		this.username = username;
		this.oponent = oponent;
		this.out = out;
	}

}

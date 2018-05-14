package gameServer;

public class P2PBinder {

	public static void bind(User u1, User u2) {
		
		u1.out.println("L;");
		u2.out.println("R;");
		
		
		u1.out.println("connect;" + u2.username + ";");
		u2.out.println("connect;" + u1.username + ";");

	}

}

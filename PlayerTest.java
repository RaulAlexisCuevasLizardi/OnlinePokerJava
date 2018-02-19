package onlinePoker;

import javax.swing.JOptionPane;

public class PlayerTest {

	public static void main(String[] args) {
		Player player = null;
		String ip = null;
		
		if(args.length > 0){
			player = new Player(args[0]);
		}else{
			ip = JOptionPane.showInputDialog("Enter your ip address or leave empty to use local host.");
			if(ip.equals("")){
				player = new Player("127.0.0.1");
			}else{
				System.out.println(ip);
				player = new Player(ip);
			}
		}
		player.startRunning();
	}

}

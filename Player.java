package onlinePoker;

import java.awt.Color;
import java.awt.EventQueue;
import java.awt.Image;
import java.awt.image.BufferedImage;
import java.io.EOFException;
import java.io.File;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.InetAddress;
import java.net.Socket;
import java.net.URL;
import java.net.UnknownHostException;
import java.util.Stack;

import javax.imageio.ImageIO;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JLayeredPane;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.border.EmptyBorder;
import java.awt.event.ActionListener;
import java.awt.event.ActionEvent;

@SuppressWarnings("serial")
public class Player extends JFrame{
	private Cards card1;
	private Cards card2;
	private Cards flopCard1;
	private Cards flopCard2;
	private Cards flopCard3;
	private Cards turnCard;
	private Cards riverCard;
	private Cards opponentCard1;
	private Cards opponentCard2;
	private ObjectOutputStream output;
	private ObjectInputStream input;
	private Socket connection;
	private double money;
	private double opponentMoney;
	private String message;
	private String serverIP;
	private JPanel contentPane;
	private JLayeredPane layeredPane;
	private JLabel opponentCardOne;
	private JLabel opponentCardTwo;
	private JLabel opponentMoneyLabel;
	private JLabel playerMoneyLabel;
	private JLabel playerCardOneLabel;
	private JLabel playerCardTwo;
	private JLabel flop1;
	private JLabel flop2;
	private JLabel flop3;
	private JLabel turn;
	private JLabel river;
	private JLabel opponentLabel;
	private JLabel userLabel;
	private JButton raiseButton;
	private JButton callOrCheckButton;
	private JButton foldButton;
	String sourceFolderPath;
	
	/**
	 * This player class will work as a client and poker player at the same time.
	 * @param host
	 */
	public Player(String host) {
		money = 1500;
		opponentMoney = 1500;
		ClassLoader classLoader = getClass().getClassLoader();
		File file = new File(classLoader.getResource("").getFile());
		System.out.println(file.getAbsolutePath());
		message = "";
		serverIP = host;
		sourceFolderPath = file.getAbsolutePath();
		
		setVisible(true);
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setBounds(100, 100, 815, 540);
		contentPane = new JPanel();
		contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
		setContentPane(contentPane);
		contentPane.setLayout(null);

		layeredPane = new JLayeredPane();
		layeredPane.setBounds(0, 0, 800, 500);
		contentPane.add(layeredPane);
		layeredPane.setLayout(null);

		JLabel tableLabel = new JLabel();
		tableLabel.setBounds(0, 0, 800, 500);
		layeredPane.add(tableLabel);
		tableLabel.setIcon(this.loadPictureAsIcon(sourceFolderPath + "\\table.png",
				tableLabel.getWidth(), tableLabel.getHeight()));
		
		
		opponentLabel = new JLabel("Opponent");
		opponentLabel.setForeground(Color.WHITE);
		opponentLabel.setBounds(270, 146, 65, 14);
		layeredPane.add(opponentLabel, new Integer(1));
		
		opponentMoneyLabel = new JLabel("$" + opponentMoney);
		opponentMoneyLabel.setForeground(Color.WHITE);
		opponentMoneyLabel.setBounds(270, 156, 65, 14);
		layeredPane.add(opponentMoneyLabel, new Integer(1));
		
		userLabel = new JLabel("You");
		userLabel.setForeground(Color.WHITE);
		userLabel.setBounds(302, 289, 46, 14);
		layeredPane.add(userLabel, new Integer(1));
		
		playerMoneyLabel = new JLabel("$" + money);
		playerMoneyLabel.setForeground(Color.WHITE);
		playerMoneyLabel.setBounds(302, 299, 46, 14);
		layeredPane.add(playerMoneyLabel, new Integer(1));
		
		raiseButton = new JButton("Raise");
		raiseButton.setBounds(701, 432, 89, 23);
		layeredPane.add(raiseButton, new Integer(1));
		raiseButton.setEnabled(false);
		
		callOrCheckButton = new JButton("Call");
		callOrCheckButton.setBounds(503, 432, 89, 23);
		layeredPane.add(callOrCheckButton, new Integer(1));
		callOrCheckButton.setEnabled(false);
		
		foldButton = new JButton("Fold");
		foldButton.setBounds(602, 432, 89, 23);
		layeredPane.add(foldButton, new Integer(1));
		foldButton.setEnabled(false);
		
		opponentCardOne = createCardAtLocation(sourceFolderPath + "\\red_back.png", 345, 128);
		layeredPane.add(opponentCardOne, new Integer(1));
		
		opponentCardTwo = createCardAtLocation(sourceFolderPath + "\\red_back.png", 400, 128);
		layeredPane.add(opponentCardTwo, new Integer(1));
	
		raiseButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				sendData("raise");
				callOrCheckButton.setEnabled(false);
				foldButton.setEnabled(false);
				raiseButton.setEnabled(false);
			}
		});
		
		foldButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				sendData("fold");
				callOrCheckButton.setEnabled(false);
				foldButton.setEnabled(false);
				raiseButton.setEnabled(false);
			}
		});
		
		callOrCheckButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				sendData("call");
				callOrCheckButton.setEnabled(false);
				raiseButton.setEnabled(false);
				foldButton.setEnabled(false);
			}
		});
	}
	
	public void startRunning() {
		try {
			connectToServer();
			setupStreams();
			processConnection();
		}catch(EOFException e) {
			System.out.println("Client terminated the connection.");
		}catch(IOException e) {
			e.printStackTrace();
		}finally {
			close();
		}
	}
	
	private void processConnection() throws IOException
	{


		do // process messages sent from server
		{ 
			try // read message and display it
			{
				message = ( String ) input.readObject(); // read new message
				System.out.println(message);
			
				if (message.contains("flop")){
					String flopCards[] = message.split(" ");
					showFlop(flopCards[1], flopCards[2], flopCards[3]);
					raiseButton.setEnabled(true);
					callOrCheckButton.setEnabled(true);
					foldButton.setEnabled(true);
				}else if(message.contains("turn")) {
					String turn[] = message.split(" ");
					showTurn(turn[1]);
					raiseButton.setEnabled(true);
					callOrCheckButton.setEnabled(true);
					foldButton.setEnabled(true);
				}else if(message.contains("river")) {
					String river[] = message.split(" ");
					showRiver(river[1]);
					raiseButton.setEnabled(true);
					callOrCheckButton.setEnabled(true);
					foldButton.setEnabled(true);
				}else if(message.contains("cards")) {
					String cards[] = message.split(" ");
					recieveCards(cards[1], cards[2]);
					raiseButton.setEnabled(true);
					callOrCheckButton.setEnabled(true);
					foldButton.setEnabled(true);
				}else if(message.contains("showdown")) {
					sendData("opponent");
				}else if(message.contains("opponent")) {
					String opponentCards[] = message.split(" ");
					showOpponentCards(opponentCards[1], opponentCards[2]);
					raiseButton.setEnabled(false);
					callOrCheckButton.setEnabled(false);
					foldButton.setEnabled(false);
				}
				
				if(card1 == null) {
					sendData("give me cards");
				}
				
			} // end try
			catch ( ClassNotFoundException classNotFoundException ) 
			{
				System.out.println("Uknown object type recieved.");
			} // end catch
			message = "";
		} while ( !message.equals( "SERVER>>> TERMINATE" ) );
	} // end method processConnection

	private void showOpponentCards(String card1, String card2) {
		for(Cards c: Cards.values()){
			if(card1.equals(c.getCardImageName())) {
				this.opponentCard1 = c;
			}
			if(card2.equals(c.getCardImageName())) {
				this.opponentCard1 = c;
			}
		}
		opponentCardOne = createCardAtLocation(sourceFolderPath + "\\" + card1, 345, 128);
		layeredPane.add(opponentCardOne, new Integer(2));
		
		opponentCardTwo = createCardAtLocation(sourceFolderPath + "\\" + card2, 400, 128);
		layeredPane.add(opponentCardTwo, new Integer(2));
	}

	private void recieveCards(String card1, String card2) {
		for(Cards c: Cards.values()){
			if(card1.equals(c.getCardImageName())) {
				this.card1 = c;
			}
			if(card2.equals(c.getCardImageName())) {
				this.card2 = c;
			}
		}
		playerCardOneLabel = createCardAtLocation(sourceFolderPath + "\\" + card1, 345, 268);
		layeredPane.add(playerCardOneLabel, new Integer(1));
		
		playerCardTwo = createCardAtLocation(sourceFolderPath + "\\" + card2, 400, 268);
		layeredPane.add(playerCardTwo, new Integer(1));
		
	}

	private void showFlop(String card1, String card2, String card3) {
		for(Cards c: Cards.values()){
			if(card1.equals(c.getCardImageName())) {
				this.flopCard1 = c;
			}
			if(card2.equals(c.getCardImageName())) {
				this.flopCard2 = c;
			}
			if(card3.equals(c.getCardImageName())) {
				this.flopCard3 = c;
			}
		}
		flop1 = createCardAtLocation(sourceFolderPath + "\\" + card1, 255, 198);
		layeredPane.add(flop1, new Integer(1));
		
		flop2 = createCardAtLocation(sourceFolderPath + "\\" + card2, 309, 198);
		layeredPane.add(flop2, new Integer(1));
		
		flop3 = createCardAtLocation(sourceFolderPath + "\\" + card3, 363, 198);
		layeredPane.add(flop3, new Integer(1));
		
	}
	
	private void showTurn(String card4) {
		for(Cards c: Cards.values()){
			if(card4.equals(c.getCardImageName())) {
				this.turnCard = c;
			}
		}
		turn = createCardAtLocation(sourceFolderPath + "\\" + card4, 417, 198);
		layeredPane.add(turn, new Integer(1));
		
	}
	
	private void showRiver(String card5) {
		for(Cards c: Cards.values()){
			if(card5.equals(c.getCardImageName())) {
				this.riverCard = c;
			}
		}
		river = createCardAtLocation(sourceFolderPath + "\\" + card5, 470, 198);
		layeredPane.add(river, new Integer(1));
		
	}



	public void setupStreams() throws IOException{
		output = new ObjectOutputStream(connection.getOutputStream());
		output.flush();
		input = new ObjectInputStream(connection.getInputStream());
		System.out.println("Streams are connected.");
	}


	public void connectToServer() throws IOException {
		System.out.println("Attempting connection...");
		connection = new Socket(InetAddress.getByName(serverIP), 6789);
		System.out.println("Connected to: " + connection.getInetAddress().getHostName() );
	}

	public void close() {
		System.out.println("Closing connections...");
		try {
			output.close();
			input.close();
			connection.close();
		}catch(IOException e) {
			e.printStackTrace();
		}
	}
	
	public void sendData(String message) {
		try {
			output.writeObject(message);
			output.flush();
			System.out.println(serverIP + " " + message);
		}catch(IOException e) {
			System.out.println("Something went wrong, couldn't send message.");
		}
	}

	
	
	public ImageIcon loadPictureAsIcon(String path, int width, int height) {
		File f = new File(path);
		BufferedImage pic = null;
		ImageIcon imageIcon = null;
		Image dimg = null;
		try {
			pic = ImageIO.read(f);
		} catch (IOException e) {
			e.printStackTrace();
		}if(pic != null) {
			if(width > 0 && height > 0)
				dimg = pic.getScaledInstance(width, height,
						Image.SCALE_SMOOTH);
			imageIcon = new ImageIcon(dimg);
			System.out.println("Picture " + f.getName() + " successfully loaded.");
		}else {
			System.out.println("Picture failed to load.");
			return null;
		}
		return imageIcon;
	}
	
	public JLabel createCardAtLocation(String path, int x, int y) {
		int width = 36;
		int height = 55;
		JLabel card = new JLabel();
		card.setBounds(x, y, width, height);
		card.setIcon(this.loadPictureAsIcon(path, width, height));
		return card;
	}

}

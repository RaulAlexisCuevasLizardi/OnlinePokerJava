package onlinePoker;

import java.awt.BorderLayout;
import java.io.EOFException;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.ServerSocket;
import java.net.Socket;
import javax.swing.JFrame;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.SwingUtilities;


/**
 * This dealer class will work as a server and a poker dealer at the same time.
 * @author Alexi
 *
 */
@SuppressWarnings("serial")
public class Dealer extends JFrame{
	private DeckOfCards deck;
	private ObjectOutputStream output;
	private ObjectInputStream input;
	private ServerSocket server;
	private Socket connection;
	private String message;
	private JTextArea displayArea;
	private boolean flopShowed;
	private boolean turnShowed;
	private boolean riverShowed;

	public Dealer() {
		setTitle("Dealer Server");
		setVisible(true);
		setBounds(100, 100, 434, 262);
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		displayArea = new JTextArea();
		displayArea.setEditable(false);
		getContentPane().add(new JScrollPane(displayArea), BorderLayout.CENTER);
		flopShowed = false;
		turnShowed = false;
		riverShowed = false;
		deck = new DeckOfCards();
	}

	//set up and run the server
	public void startRunning() throws EmptyDeckException {
		try {
			server = new ServerSocket(6789, 100);
			while(true) {
				try {
					waitForConnection();
					setupStreams();
					processConnection();
				}catch(EOFException e) {
					System.out.println("Server ended the connection.");
					displayMessage("\nServer ended the connection");
				}finally{
					close();
				}
			}
		}catch(IOException e) {
			e.printStackTrace();
		}
	}

	public void sendFlop() throws EmptyDeckException {
		String flop = "flop";
		String card1 = deck.DrawTopCard().getCardImageName();
		String card2 = deck.DrawTopCard().getCardImageName();
		String card3 = deck.DrawTopCard().getCardImageName();
		flop += " " + card1 + " " + card2 + " " + card3;
		Cards card1Temp = null;
		Cards card2Temp = null;
		Cards card3Temp = null;
		for(Cards c: Cards.values()){
			if(card1.equals(c.getCardImageName())) {
				card1Temp = c;
			}
			if(card2.equals(c.getCardImageName())) {
				card2Temp = c;
			}
			if(card3.equals(c.getCardImageName())) {
				card3Temp = c;
			}
		}
		String message = "\nShowing flop " + card1Temp + ", " + card2Temp + ", " + card3Temp;
		displayMessage(message);
		sendData(flop);
	}

	public void sendTurn() throws EmptyDeckException {
		String turn = "turn";
		String card1 = deck.DrawTopCard().getCardImageName();
		turn += " " + card1;
		Cards card1Temp = null;
		for(Cards c: Cards.values()){
			if(card1.equals(c.getCardImageName())) {
				card1Temp = c;
			}
		}
		String message = "\nShowing turn " + card1Temp;
		displayMessage(message);
		sendData(turn);
	}

	public void sendRiver() throws EmptyDeckException {
		String river = "river";
		String card1 = deck.DrawTopCard().getCardImageName();
		river += " " + card1;
		Cards card1Temp = null;
		for(Cards c: Cards.values()){
			if(card1.equals(c.getCardImageName())) {
				card1Temp = c;
			}
		}
		String message = "\nShowing river " + card1Temp;
		displayMessage(message);
		sendData(river);
	}

	private void processConnection() throws IOException, EmptyDeckException
	{
		message = "Connection successful";
		displayMessage("\nConnection successful");
		sendData( message ); // send connection successful message
		do // process messages sent from client
		{ 
			try // read message and display it
			{
				if(message.contains("call")) {
					if(!flopShowed) {
						sendFlop();
						flopShowed = true;
					}else if(!turnShowed) {
						sendTurn();
						turnShowed = true;
					}else if(!riverShowed) {
						sendRiver();
						riverShowed = true;
					}else {
						sendData("showdown");
					}
				}else if(message.contains("raise")) {
					if(!flopShowed) {
						sendFlop();
						flopShowed = true;
					}else if(!turnShowed) {
						sendTurn();
						turnShowed = true;
					}else if(!riverShowed) {
						sendRiver();
						riverShowed = true;
					}else {
						sendData("showdown");
					}
				}else if(message.contains("fold")) {

				}else if(message.contains("cards")){
					dealCards();
					message = "";
				}else if(message.contains("opponent")) {
					dealOpponentCards();
				}else if(message.contains("")){

				}else {
					System.out.println("Invalid message.");
					displayMessage("\nInvalid message");
				}
				message = (String)input.readObject();
				displayMessage("\nRecieved message from: " + connection.getInetAddress().getHostName() + "\n Message: " + message);
			} // end try
			catch ( ClassNotFoundException classNotFoundException ) 
			{
				System.out.println("Unknown object recieved.");
				displayMessage("\nUnknown object recieved.");
			} // end catch
		} while ( !message.equals( "CLIENT>>> TERMINATE" ) );
	} // end method processConnection

	private void dealCards() throws EmptyDeckException {
		String cards = "cards";
		String card1 = deck.DrawTopCard().getCardImageName();
		String card2 = deck.DrawTopCard().getCardImageName();
		cards += " " + card1 + " " + card2;
		Cards card1Temp = null;
		Cards card2Temp = null;
		for(Cards c: Cards.values()){
			if(card1.equals(c.getCardImageName())) {
				card1Temp = c;
			}
			if(card2.equals(c.getCardImageName())) {
				card2Temp = c;
			}
		}
		displayMessage("\nDealing " + card1Temp + " and " + card2Temp);
		sendData(cards);
	}
	
	private void dealOpponentCards() throws EmptyDeckException {
		String cards = "opponent";
		String card1 = deck.DrawTopCard().getCardImageName();
		String card2 = deck.DrawTopCard().getCardImageName();
		cards += " " + card1 + " " + card2;
		Cards card1Temp = null;
		Cards card2Temp = null;
		for(Cards c: Cards.values()){
			if(card1.equals(c.getCardImageName())) {
				card1Temp = c;
			}
			if(card2.equals(c.getCardImageName())) {
				card2Temp = c;
			}
		}
		displayMessage("\nDealing " + card1Temp + " and " + card2Temp + " to opponent");
		sendData(cards);
	}

	private void sendData( String message )
	{
		try // send object to client
		{
			output.writeObject(message);
			output.flush(); // flush output to client

		} // end try
		catch ( IOException ioException ) 
		{
			System.out.println("Error writing object");
			displayMessage("\nError writing object");
		} // end catch
	} // end method sendData

	public void setupStreams() throws IOException{
		output = new ObjectOutputStream(connection.getOutputStream());
		output.flush();
		input = new ObjectInputStream(connection.getInputStream());
		System.out.println("Streams are set up.");
		displayMessage("\nStreams are set up.");
	}
	//wait for connection. then display connection information.
	public void waitForConnection() throws IOException{
		System.out.println("Waiting for someone to connect...");
		displayMessage("\nWaiting for someone to connect...");
		connection = server.accept();
		System.out.println("Now connected to " + connection.getInetAddress().getHostName());
		displayMessage("\nNow connected to " + connection.getInetAddress().getHostName());
	}
	
	private void displayMessage( final String messageToDisplay )
	{
		SwingUtilities.invokeLater(
				new Runnable() 
				{
					public void run() // updates displayArea
					{
						displayArea.append( messageToDisplay ); // append message
					} // end method run
				} // end anonymous inner class
				); // end call to SwingUtilities.invokeLater
	} // end method displayMessage
	
	//close streams and sockets after you are done playing
	public void close() {
		System.out.println("Closing connections...");
		displayMessage("\nClosing connections...");
		try {
			output.close();
			input.close();
			connection.close();
		}catch(IOException e) {
			e.printStackTrace();
		}
	}
}

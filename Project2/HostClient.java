import java.io.*;
import java.net.*;
import java.util.*;
import java.text.*;
import java.lang.*;
import javax.swing.*;

public class HostClient {

	private static final int PORT = 1200;

	public static ArrayList<String> response = new ArrayList<String>();
	
	void contructFileList() {
		
	}

	public static void main(String[] args) throws IOException {

		String message, command = "";
		Scanner clientCommand = new Scanner(System.in);

		try {
			do {

				System.out.print("\nEnter Command: ");

				message = clientCommand.nextLine();
				StringTokenizer tokens = new StringTokenizer(message);
				command = tokens.nextToken();

				if (command.equals("connect")) {

					BufferedReader inFromUser = new BufferedReader(
							new InputStreamReader(System.in));
					Socket controlSocket = new Socket("127.0.0.1",
							PORT);

					System.out.println(
							"\nYou are connected to the server");
					response.add("Connected to 127.0.0.1");
					
					//After the confirmation from the server we need to send over the list of files amd 
				}

				if (command.equals("register")) {
					// CODE FOR REGISTER COMMAND
				}

				if (command.equals("unregister")) {
					// CODE FOR UNREGISTER COMMAND
				}

				if (command.equals("search")) {
					// CODE FOR SEARCH COMMAND

				}

				if (command.equals("get")) {

					// CODE FOR GET COMMAND

				}

			} while (!command.equals("quit"));

		} catch (IOException e) {

			e.printStackTrace();
		}

	}


	public ArrayList<String> getResponse() {
		return response;
	}

}

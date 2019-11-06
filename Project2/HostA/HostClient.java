import java.io.*;
import java.net.*;
import java.util.*;
import java.text.*;
import java.lang.*;
import javax.swing.*;

public class HostClient {

	private static final int PORT = 1200;

	public static ArrayList<String> response = new ArrayList<String>();

	private List<UserFileList> fileList = new ArrayList<>();

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
		// need to iterate through the file that lists 
		// available files and their keywords and add them to
		// the filelist here.
	private void constructFileList(File directory){
		fileInfo temp = new fileInfo();
		// add filename to temp here
		// iteratively add keywords to temp
		fileList.add(temp);
    }

	public ArrayList<String> getResponse() {
		return response;
	}

}

import java.io.*;
import java.net.*;
import java.util.*;
import java.text.*;
import java.lang.*;
import javax.swing.*;

public class HostClient {

	private static final int PORT = 1200;

	public static ArrayList<String> response = new ArrayList<String>();
	public List<String> listOfFilesOnClient = new ArrayList<>();

	public int port1;

	public String responseFromClient = "";
	public File directory = new File(System.getProperty("user.dir"));

	Socket controlSocketCentralServer;
	DataOutputStream outToCentralServer;
	BufferedReader inFromCentralServer;

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

					BufferedReader inFromUser = new BufferedReader(new InputStreamReader(System.in));
					Socket controlSocket = new Socket("127.0.0.1", PORT);

					System.out.println("\nYou are connected to the server");
					response.add("Connected to 127.0.0.1");

					// After the confirmation from the server we need to send over the list of files
					// amd
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

	public void connectToCentralServer(int port, String serverHostName) throws IOException {
		port1 = port + 2;
		controlSocketCentralServer = new Socket(serverHostName, port);
		// System.out.println("\nYou are connected to the server");
		outToCentralServer = new DataOutputStream(controlSocketCentralServer.getOutputStream());
		inFromCentralServer = new BufferedReader(new InputStreamReader(controlSocketCentralServer.getInputStream()));
		// response.add("Connected to " + serverHostName);
		responseFromClient = "Connected to " + serverHostName;
	}

	public void disconnectFromCentralServer() throws IOException {
		port1 += 2;
		outToCentralServer.writeBytes(String.valueOf(port1) + " quit\n");
		outToCentralServer.flush();
		responseFromClient = "Disconnected from central server";
	
	}

	public void registerToCentralServer(String clientUserName, String clientHostName, String connectionType, int serverPort)
			throws IOException {
		port1 += 2;
		//this.uploadFileListToServer(connectionType, clientHostName);
		// delimit using commas and send the registration information to the server
		String userInformation = clientUserName + "," + clientHostName + "," + connectionType + "," + String.valueOf(serverPort);
		outToCentralServer.writeBytes(String.valueOf(port1) + " register " + userInformation + "\n");
		//outToCentralServer.rese;
		
		responseFromClient = "Registered to central server. Uploading file list to server...";
	}

	public void uploadFileListToServer(String clientConnectionType, String clientHostName) throws IOException {
		port1 += 2;
		// get the file list and put it into a string thats delimited by commmas
		serverFiles(directory, listOfFilesOnClient);
		String outputList = new String(String.valueOf(port1) + " uploadFileList " + clientHostName + " " + clientConnectionType + " ");

		if (listOfFilesOnClient.isEmpty()) {
			// dataOutToCentralServer.writeUTF("empty");
			// System.out.println("Telling the client there are no files on the server");
			outputList += "empty";
			outToCentralServer.writeBytes(outputList + "\n");
			outToCentralServer.flush();
			responseFromClient = "You have no files";
		} else {
			int x = 0;
			// outputList.concat("The files on the server are:\n");
			// add list of file names and delimit them using a comma
			while (x < listOfFilesOnClient.size()) {
				outputList = outputList.concat(listOfFilesOnClient.get(x) + ",");
				x++;
			}
			// System.out.println("Sending the client the files on the server");
			// dataOutToCentralServer.writeUTF(outputList);
			outToCentralServer.writeBytes(outputList + "\n");
			outToCentralServer.flush();
			responseFromClient = "Successfully uploaded file list";
		}
		// System.out.println("Closing data connection");

	}

	public void unregisterFromServer (String clientUserName, String clientHostName, String connectionType) throws IOException {
		port1 += 2;
		String userInformation = clientUserName + "," + clientHostName + "," + connectionType;
		outToCentralServer.writeBytes(String.valueOf(port1) + " unregister " + userInformation + "\n");
		outToCentralServer.flush();
		responseFromClient = "Un-registered from central server and removed files from file list";
	}

	public static void serverFiles(File directory, List<String> listOfFilesOnClient) {
		if (!(listOfFilesOnClient.isEmpty())) {
			listOfFilesOnClient.clear();
		}
		if (directory.exists()) {
			for (File file : directory.listFiles()) {
				if (file.isFile()) {
					listOfFilesOnClient.add(file.getName());
				}
			}
		}
	}

	public String queryFileList (String keywordSearch) throws IOException {
		port1 += 2;
		outToCentralServer.writeBytes(String.valueOf(port1) + " query " + keywordSearch + "\n");
		outToCentralServer.flush();
		String fromServer = "";
		String fullEntry = "";
		fromServer = inFromCentralServer.readLine();
		if(fromServer != null){
			String [] stringArray = fromServer.split(",");
			int i = 0;
			for (i = 0; i < stringArray.length; i++){
			fullEntry += stringArray[i] + " ";
			fullEntry += stringArray[++i] + " ";
			fullEntry += stringArray[++i] + " ";
			fullEntry += "\n";
			}
			responseFromClient = "Query returned with results";	
		} else {
			responseFromClient = "Query returned no files";
		}
		return fullEntry;
	}

	public int getClientPort(String userHostName) throws IOException {
		port1 += 2;
		outToCentralServer.writeBytes(String.valueOf(port1) + " retrievePort " + userHostName + "\n");
		outToCentralServer.flush();
		String fromServer = inFromCentralServer.readLine();
		responseFromClient = "Found users server port number...";
		return Integer.parseInt(fromServer);
	}
}

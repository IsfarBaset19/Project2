import java.io.*;
import java.net.*;
import java.util.*;

class CentralizedServer {

	private static final int PORT = 1200;

	// need to iterate through the file that lists
	// available files and their keywords and add them to
	// the filelist here.

	public static void main(String[] args) throws IOException
	{
		//create the new file to hold the client data
		String currentDirectory = System.getProperty("user.dir");
		String serverFileList = currentDirectory + "/allServerFiles.txt";
		File file = new File(serverFileList);
		if(file.exists()){
			file.delete();
		}
		file.createNewFile();
		
		// Create socket server and wait for client to connect
		ServerSocket welcomeSocket = new ServerSocket(PORT);
		do {
			Socket connectionSocket = welcomeSocket.accept();
			System.out.println("\n\nUser Connected!\n\n");

			// Creating threads
			ClientHandler handler = new ClientHandler(connectionSocket);
			handler.start();
		} while (true);
	}
}

// Class that holds the important information for users of the server
class User {
	String username;
	String userHostName;
	String port;
	String connectionSpeed;

	User(String pUsername, String pUserHostName, String pPort, String pConnectionSpeed) {
		this.username = pUsername;
		this.userHostName = pUserHostName;
		this.port = pPort;
		this.connectionSpeed = pConnectionSpeed;
	}

}

// Class that holds multiple file informations for a single user
class UserFileList {
	String hostName;
	List<FileInfo> files;

	void add(FileInfo temp) {
		files.add(temp);
	}

	UserFileList() {
		this.hostName = "";
		this.files = new ArrayList<>();
	}

	UserFileList(String pHostName, List<FileInfo> pFiles) {
		this.hostName = pHostName;
		this.files = new ArrayList<>();
		this.files = pFiles;
	}
}

// Class that holds the filename and any keywords for the file
class FileInfo {
	String fileName;
	List<String> keywords;

	FileInfo() {
		this.fileName = "";
		this.keywords = new ArrayList<>();
	}

	FileInfo(String pFileName, List<String> pKeywords) {
		this.fileName = pFileName;
		this.keywords = new ArrayList<>();
		this.keywords = pKeywords;
	}

}

class ClientHandler extends Thread {

	private DataOutputStream outToClient;
	private BufferedReader inFromClient;

	// This arrayList of Users holds the list of active users currently
	// registered to the server
	private List<User> userList = new ArrayList<>();

	// This arrayList holds the list of UserFileLists
	private List<UserFileList> fileList = new ArrayList<>();

	// Code for registering a new user to the server
	private void addUser(User temp) {
		userList.add(temp);
	}

	// Code for removing a user from the server
	private void removeUser(User temp) {
		userList.remove(temp);
	}

	private void addFiles(String[] fileList) {
		String fileName = "allServerFiles.txt";
		try {
			BufferedWriter out = new BufferedWriter(new FileWriter(fileName, true));
			int i = 0;
			for (i = 0; i < fileList.length; i++){
				out.write(fileList[i] + "\n");
			}
			out.close();
			//byte[] bytesArray = fileList.getBytes();
			System.out.println("Added userFileList to allFiles.txt");
		} catch(Exception e) {

		}
	}

	private void removeFiles() {

	}

	String fromClient;
	String clientCommand;
	byte[] data;

	private Scanner input;
	private String received;

	// FILE PATH
	File directory = new File(".");

	List<String> listOfFiles = new ArrayList<>();
	String firstln;
	private Socket connectionSocket;
	String fileName;
	StringTokenizer tokens = new StringTokenizer("");

	public ClientHandler(Socket socket) {
		connectionSocket = socket;

		try {
			outToClient = new DataOutputStream(connectionSocket.getOutputStream());
			inFromClient = new BufferedReader(new InputStreamReader(connectionSocket.getInputStream()));
		} catch (IOException e) {
			System.out.println("Error in connection");
			System.out.println(e);
		}
	}

	public static void serverFiles(File directory, List<String> listOfFiles) {
		if (!(listOfFiles.isEmpty())) {
			listOfFiles.clear();
		}
		if (directory.exists()) {
			for (File file : directory.listFiles()) {
				if (file.isFile()) {
					listOfFiles.add(file.getName());
				}

			}
		}
	}

	public void run() {
		int port = 1200;
		while (connectionSocket.isClosed() == false) {
			try {
				fromClient = inFromClient.readLine();
				if (fromClient != null) {
					tokens = new StringTokenizer(fromClient);
					firstln = tokens.nextToken();
					// int port;
					port = Integer.parseInt(firstln);
					clientCommand = tokens.nextToken();
					// fileName = tokens.nextToken();
				}

				// if (fileName == null) {
				// fileName = "noFileFound.txt";
				// }
				serverFiles(directory, listOfFiles);

				if (clientCommand.equals("get")) {

					// int PORT = 1234 + 2;

					Socket dataSocket = new Socket(connectionSocket.getInetAddress(), port);
					DataOutputStream dataOutToClient = new DataOutputStream(dataSocket.getOutputStream());

					String fileName = tokens.nextToken();
					String filePath = directory.getPath() + "/" + fileName;
					File myFile = new File(filePath);

					if (myFile.exists()) {
						byte[] mybytearray = new byte[(int) myFile.length() + 1];
						FileInputStream fis = new FileInputStream(myFile);
						BufferedInputStream bis = new BufferedInputStream(fis);
						bis.read(mybytearray, 0, mybytearray.length);
						System.out.println("Sending...");
						dataOutToClient.write(mybytearray, 0, mybytearray.length);
						dataOutToClient.flush();
						bis.close();
					} else {
						System.out.println("File Not Found");
					}

					dataSocket.close();
					System.out.println("Data Socket closed");
				}

				else if (clientCommand.equals("register")) {
					String userInformation = tokens.nextToken();
					String[] userCredentials = userInformation.split(",");
					User newUser = new User(userCredentials[0], userCredentials[1], firstln, userCredentials[2]);
					this.addUser(newUser);
					System.out.println("User Registered\n");
					// System.out.println(this.userList);
					// System.out.println(userList);
				}

				else if (clientCommand.equals("uploadFileList")) {
					String userHostName = tokens.nextToken();
					String userConnectionType = tokens.nextToken();
					String fileListString = tokens.nextToken();
					String[] clientFileList = fileListString.split(",");
					int i = 0;
					// check that the fileList isn't empty
					if (clientFileList[0].equals("empty")) {
						System.out.println("Client has no files to list");
					} else {
						// make the array of strings hostname,connectiontype,clientfileName
						for (i = 0; i < clientFileList.length; i++) {
							clientFileList[i] = userHostName + "," + userConnectionType + "," + clientFileList[i] + ",";
						}
						// write to servers file system
						addFiles(clientFileList);
						System.out.println("Files Uploaded");
					}
				}

				if (clientCommand.equals("unregister")) {
					// CODE FOR UNREGISTERING CLIENT
				}

				if (clientCommand.equals("search")) {
					// CODE FOR SEARCHING FILES
				}

				if (clientCommand.equals("quit")) {
					System.out.println("Closing connection with a client");
					connectionSocket.close();
				}

			}

			catch (IOException e) {
				System.out.println("Error: Unable to disconnect");
				System.out.println(e);
			}
		}
	}
}
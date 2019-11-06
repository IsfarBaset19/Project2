import java.io.*;
import java.net.*;
import java.util.*;



class CentralizedServer {

private static final int PORT = 1235;

// This arrayList of Users holds the list of active users currently registered to the server
List<User> userList = new ArrayList<>();
List<>

// Code for registering a new user to the server
private addUser() {
	
}

// Code for removing a user from the server
private removeUser() {
	
}

public static void main(String[] args) throws IOException

    {


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
	String IPAddress;
	String port;
	String connectionSpeed;
	UserFileList files;
	
	User(String pUsername, String pIPAddress, String pPort, String pConnectionSpeed){
		this.username = pUsername;
		this.IPAddress = pIPAddress;
		this.port = pPort;
		this.connectionSpeed = pConnectionSpeed;
	}
	
}

// Class that holds file information for a single user
class UserFileList{
	
}

class ClientHandler extends Thread {

    private DataOutputStream outToClient;
    private BufferedReader inFromClient;

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
        if(!(listOfFiles.isEmpty())){
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
        while(connectionSocket.isClosed() == false){
            try {

                fromClient = inFromClient.readLine();
                if (fromClient != null) {
                    tokens = new StringTokenizer(fromClient);
                    firstln = tokens.nextToken();
                    int port;
                    port = Integer.parseInt(firstln);
                    clientCommand = tokens.nextToken();
                    // fileName = tokens.nextToken();
                }

                if (fileName == null) {
                    fileName = "noFileFound.txt";
                }
                serverFiles(directory, listOfFiles);


                if (clientCommand.equals("get")) {

                    int PORT = 1234 + 2;
                   
                    Socket dataSocket = new Socket(connectionSocket.getInetAddress(), PORT);
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


            else if(clientCommand.equals("register"))
            {
              
              // CODE FOR REGISTERING NEW CLIENT
            }
          
            if(clientCommand.equals("unregister"))
            {
                // CODE FOR UNREGISTERING CLIENT
            }

            if(clientCommand.equals("search"))
            {
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
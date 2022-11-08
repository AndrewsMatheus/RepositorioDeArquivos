import java.net.*;
import java.io.*;

public class TCPServer {

	public static void main (String args[]) {
		try{
			int serverPort = 7896;
			ServerSocket listenSocket = new ServerSocket(serverPort);

			while(true) {
				Socket clientSocket = listenSocket.accept();
				Connection c = new Connection(clientSocket);
			}
		} 
        catch(IOException e) {
            System.out.println("Escutando socket:"+e.getMessage());
        }
	}
}
class Connection extends Thread {

	DataInputStream in;
	DataOutputStream out;
	Socket clientSocket;

	public Connection (Socket aClientSocket) {
		try {
			clientSocket = aClientSocket;
			in = new DataInputStream(clientSocket.getInputStream());
			out = new DataOutputStream(clientSocket.getOutputStream());
			this.start();
		} 
        catch(IOException e) {
            System.out.println("Conexão:"+e.getMessage());
        }
	}
	public void run(){
		try {			                 // an echo server
			String data = in.readUTF();	                  // read a line of data from the stream
			System.out.println("Mensagem recebida do client: "+data);
			switch (data) {
				case "1":
					data = in.readUTF();	  
					break;
			
				default:
					break;
			}
		}
        catch (EOFException e){
            System.out.println("EOF:"+e.getMessage());
		} 
        catch(IOException e) {
            System.out.println("readline:"+e.getMessage());
		} 
        finally{ 
            try {
                clientSocket.close();
            } catch (IOException e){
                // Falha no fechamento
            }}
	}
	public void CriarDiretorio(String CaminhoDiretorio) throws IOException{
		File novoDiretorio = new File("./CaminhoDiretorio");
		if (novoDiretorio.exists()) {
			throw new IOException();
		}
		novoDiretorio.mkdirs();	
		System.out.println("Pasta Criada");
	}
}
import java.net.*;
import java.io.*;

public class TCPServer {

	public static void main (String args[]) {
		try{
			int serverPort = 7896;
			try (ServerSocket listenSocket = new ServerSocket(serverPort)) {
				while(true) {
					Socket clientSocket = listenSocket.accept();
					Connection c = new Connection(clientSocket);
				}
			}
		} 
        catch(IOException e) {
            System.out.println("Escutando socket:"+e.getMessage());
        }
	}
}
class Connection extends Thread {

	InputStream in;
	DataOutputStream out;
	Socket clientSocket;

	public Connection (Socket aClientSocket) {
		try {
			clientSocket = aClientSocket;
			in = aClientSocket.getInputStream();
			out = new DataOutputStream(clientSocket.getOutputStream());
			this.start();
		} 
        catch(IOException e) {
            System.out.println("Conexão:"+e.getMessage());
        }
	}
	public void run(){
		try {			                 // an echo server
			ObjectInputStream ois = new ObjectInputStream(in);
			Object data = ois.readObject();               // read a line of data from the stream
			String[] strings = (String[]) data;
			switch (strings[0]) {
				case "1":
					CriarDiretorio(strings[1]);	  
					break;
				case "2":
					RemoverDiretorio(strings[1]);
					break;
				case "3":
					ListarDiretorio(strings[1]);
					break;
				case "4":
					out.writeUTF("OK");
					ReceberArquivo(strings[1]);
					break;
				default:
					break;
			}
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (ClassNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
        // catch (EOFException e){
        //     System.out.println("EOF:"+e.getMessage());
		// } 
        // catch(IOException e) {
        //     System.out.println("readline:"+e.getMessage());
		// } 
        finally{ 
            try {
                clientSocket.close();
            } catch (IOException e){
                // Falha no fechamento
            }}
	}
	public void CriarDiretorio(String CaminhoDiretorio) throws IOException{
		File novoDiretorio = new File("./"+CaminhoDiretorio);
		if (novoDiretorio.exists()) {
			throw new IOException();
		}
		novoDiretorio.mkdirs();	
		out.writeUTF("Pasta Criada com sucesso");
	}
	public void RemoverDiretorio(String CaminhoDiretorio) throws IOException {
		try{
			File Diretorio = new File("./"+CaminhoDiretorio);
			if (!Diretorio.exists()) {
				throw new IOException();
			}
			if (!Diretorio.delete()) {
				throw new IOException();
			}	
			out.writeUTF("Pasta Removida");
		}	
		catch (IOException e){
			out.writeUTF("Ocorreu um erro ao deletar o diretório");
        }
	}
	public void ListarDiretorio(String CaminhoDiretorio) throws IOException{
		File Diretorio = new File("./ArquivosCarregados"+CaminhoDiretorio);
		String [] ListaDeArquivos = Diretorio.list();
		ObjectOutputStream objectOutputStream = new ObjectOutputStream(out);
		objectOutputStream.writeObject(ListaDeArquivos);
	}
	public void ReceberArquivo(String NomeArquivo) throws IOException{
		try  {
			int bytesRead;
			DataInputStream clientData = new DataInputStream(
					clientSocket.getInputStream());
			OutputStream output = new FileOutputStream("./ArquivosCarregados/"+clientData.readUTF());
			long size = clientData.readLong();
			byte[] buffer = new byte[100000];
	
			while (size > 0
					&& (bytesRead = clientData.read(buffer, 0,
							(int) Math.min(buffer.length, size))) != -1) {
				output.write(buffer, 0, bytesRead);
				size -= bytesRead;
			}
	
			output.close();
	
		} finally{
			
		}
	}
}
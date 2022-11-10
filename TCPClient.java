import java.net.*;
import java.util.Scanner;
import java.io.*;

public class TCPClient {
	
	public static void main (String args[]) throws ClassNotFoundException {
		// arguments supply message and hostname
		Socket socket = null;
		try{ 
			int serverPort = 7896;
            InetAddress localHost = InetAddress.getByName(InetAddress.getLocalHost().getHostName());
			socket = new Socket(localHost, serverPort);       
			DataInputStream in = new DataInputStream(socket.getInputStream());  
			switch (args[0]) {
				case "1":
					CriarDiretorio(socket);
				break;
				case "2":
					RemoverDiretorio(socket);
				break;
				case "3":
					String[] Arquivos = ListarDiretorio(socket, socket.getInputStream());
					System.out.println("\n");
					for (String arquivo : Arquivos) {
						System.out.println(arquivo+"\n");	
					}
				break;
				case "4":
					EnviarArquivo(socket, new DataOutputStream(socket.getOutputStream()));
					break;
				default:
					break;
			}
			String data = in.readUTF();	    // read a line of data from the stream
			System.out.println("Recebido: "+data) ;
			socket.close(); 
		}
        catch (UnknownHostException e){
            System.out.println("Socket:"+e.getMessage());
		}
        catch (EOFException e){
            System.out.println("EOF:"+e.getMessage());
		}catch (IOException e){
            System.out.println("Lendo linha:"+e.getMessage());
		}
        finally {
            if(socket!=null) try {socket.close();
        }catch (IOException e){
            System.out.println("Fechar: " +e.getMessage());
        }}
     }
    public static void CriarDiretorio(Socket socket) throws IOException{

			Scanner scanner = new Scanner(System.in);
			String[] objetoAEnviar = new String[2];
			System.out.println("Nome da pasta: ");
			objetoAEnviar[0] = "1";
			objetoAEnviar[1] = scanner.nextLine();

			OutputStream outputStream = socket.getOutputStream();
			ObjectOutputStream objectOutputStream = new ObjectOutputStream(outputStream);
			objectOutputStream.writeObject(objetoAEnviar);
			scanner.close();
	}
	public static void RemoverDiretorio(Socket socket) throws IOException {
		Scanner scanner = new Scanner(System.in);
		String[] objetoAEnviar = new String[2];
		System.out.println("Nome da pasta para remover: ");
		objetoAEnviar[0] = "2";
		objetoAEnviar[1] = scanner.nextLine();

		OutputStream outputStream = socket.getOutputStream();
		ObjectOutputStream objectOutputStream = new ObjectOutputStream(outputStream);
		objectOutputStream.writeObject(objetoAEnviar);
		scanner.close();	
	}
	public static String[] ListarDiretorio(Socket socket, InputStream in) throws IOException, ClassNotFoundException{
		Scanner scanner = new Scanner(System.in);
		String[] objetoAEnviar = new String[2];
		System.out.println("Nome da pasta para Listagem: ");
		objetoAEnviar[0] = "3";
		objetoAEnviar[1] = scanner.nextLine();
		

		OutputStream outputStream = socket.getOutputStream();
		ObjectOutputStream objectOutputStream = new ObjectOutputStream(outputStream);
		objectOutputStream.writeObject(objetoAEnviar);
		scanner.close();

		ObjectInputStream ois = new ObjectInputStream(in);
		Object data = ois.readObject();               // read a line of data from the stream
		return (String[]) data;
	}
	public static void EnviarArquivo(Socket socket, OutputStream out) throws IOException, ClassNotFoundException{
		try (Scanner scanner = new Scanner(System.in)) {
			String[] objetoAEnviar = new String[2];
			objetoAEnviar[0] = "4";
			System.out.println("Nome do arquivo para envio: ");
			objetoAEnviar[1] = scanner.nextLine();

			OutputStream outputStream = socket.getOutputStream();
			ObjectOutputStream objectOutputStream = new ObjectOutputStream(outputStream);
			objectOutputStream.writeObject(objetoAEnviar);
			
			try {

				File myFile = new File(objetoAEnviar[1]);
				byte[] mybytearray = new byte[(int) myFile.length()];
		
				FileInputStream fis = new FileInputStream(myFile);
				BufferedInputStream bis = new BufferedInputStream(fis);
		
		
				try (DataInputStream dis = new DataInputStream(bis)) {
					dis.readFully(mybytearray, 0, mybytearray.length);
				}
				OutputStream os = socket.getOutputStream();
		
		
				DataOutputStream dos = new DataOutputStream(os);
				dos.writeUTF(myFile.getName());
				dos.writeLong(mybytearray.length);
				dos.write(mybytearray, 0, mybytearray.length);
				dos.flush();
		
				System.out.println("Arquivo "+objetoAEnviar[1]+" foi enviado para o servidor com sucesso!");
		
			} catch (FileNotFoundException e) {
				System.err.println("Arquivo não existe!");
			} 
		}  
	}
}
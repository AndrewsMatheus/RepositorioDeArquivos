import java.net.*;
import java.io.*;

public class TCPClient {
	public static void main (String args[]) {
		// arguments supply message and hostname
		Socket socket = null;
		try{
			int serverPort = 7896;
            InetAddress localHost = InetAddress.getByName(InetAddress.getLocalHost().getHostName());
			socket = new Socket(localHost, serverPort);       
			DataInputStream in = new DataInputStream(socket.getInputStream());
			DataOutputStream out =new DataOutputStream(socket.getOutputStream());
			out.writeUTF(args[0]);      	// UTF is a string encoding see Sn. 4.4
			String data = in.readUTF();	    // read a line of data from the stream
			System.out.println("Recebido: "+data) ; 
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
}
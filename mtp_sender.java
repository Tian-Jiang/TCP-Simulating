import java.net.*;



/**
 * header structure
 * -------------------------
 * FLAG 1 Byte
 * ACK 1 bit
 * SYN 1 bit
 * 6 bits left empty
 * 
 * Sequence Number 4 Bytes
 * ACK Number 4 Bytes
 * Packet Length  4 Bytes
 * -------------------------
 * @author Alex
 *
 */

public class mtp_sender {

	public static void main(String args[]) {
		Args.RECEIVE_HOST_IP = args[0];
		Args.RECEIVE_PORT = Integer.parseInt(args[1]);
		Args.filename = args[2];
		Args.MWS = Integer.parseInt(args[3]);
		Args.MSS = Integer.parseInt(args[4]);
		Args.timeout = Integer.parseInt(args[5]);
		Args.pdrop = Double.parseDouble(args[6]);
		Args.seed = Long.parseLong(args[7]);
				
		Args.log = new Log();

		
		try {
			Args.ds = new DatagramSocket();
		} catch (SocketException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		Args.local_port = Args.ds.getLocalPort();
		System.out.println("local port number is "+Args.local_port);
		
		Thread tack = new AcceptACK();
		tack.start();
		
        Thread Tconn = new Connect(); 
        Tconn.start();
	}
	
	 public static byte[] Int2Byte(int i) {   
		  byte[] result = new byte[4];   
		  result[0] = (byte)((i >> 24) & 0xFF);
		  result[1] = (byte)((i >> 16) & 0xFF);
		  result[2] = (byte)((i >> 8) & 0xFF); 
		  result[3] = (byte)(i & 0xFF);
		  return result;
		 }
	 
	 
	 public  static int Byte2Int(byte[] bytes) {
		 int num = bytes[3] & 0xFF;
		 num |= ((bytes[2] << 8) & 0xFF00);
		 num |= ((bytes[1] << 16) & 0xFF0000);
		 num |= ((bytes[0] << 24) & 0xFF000000);
		 return num;		 
	}	
}

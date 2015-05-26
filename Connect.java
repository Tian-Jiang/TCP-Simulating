import java.io.IOException;
import java.net.DatagramPacket;
import java.net.InetSocketAddress;
import java.net.SocketException;

public class Connect extends Thread {
	
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
	 
	private void sendPacket(boolean SYN, boolean ACK, int seq_num,
			int ack_num) {
		byte[] header = new byte[Args.header_len];
		byte[] seq = Int2Byte(seq_num);
		byte[] ack = Int2Byte(ack_num);
		System.arraycopy(seq, 0, header, 1, 4);
		System.arraycopy(ack, 0, header, 5, 4);
		/*
		 *  Set flags in header, 0x80 is decimal value of 1000000,
		 *  means SYN is set to 1
		 *  0x40 is decimal value of 01000000, means ACK is set to 1. 
		 */
		if (SYN == true)
			header[0] = (byte) 0x80;
		else if (ACK == true)
			header[0] = 0x40;
		System.arraycopy(Int2Byte(Args.header_len), 0, header, 9, 4);
		Args.log.recordTrans(header, true);
		try {
			DatagramPacket dp = new DatagramPacket(header,
					header.length,
					new InetSocketAddress(Args.RECEIVE_HOST_IP, Args.RECEIVE_PORT));
			Args.ds.send(dp);
		} catch (SocketException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
		
	}

	@Override
	public void run() {		
		int seq_num = 123456;
		System.out.println("Sender starts.");
		
		while (!Args.connected) {
			sendPacket(true, false, seq_num, 0);
			seq_num++;
			System.out.println("SYN sent");			
			try {
				/* After sent a SYN packet, this thread will wait for
				 * 2 seconds, then check whether received a SYN ACK
				 */				
				Thread.sleep(2000);
			} catch (InterruptedException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			/*
			 * If received SYN ACK, it will send a ACK packet,
			 * otherwise, send SYN again.
			 */
			if (!Args.flagSYNACK)
				continue;			
			sendPacket(false, true, seq_num, ++Args.ack_num);
			seq_num++;
			System.out.println("ACK sent.");
			Args.ack_num = 0;
			try {
				Thread.sleep(2000);
			} catch (InterruptedException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		Thread ttrans = new TransferFile();
		ttrans.start();
		
	}
	
}
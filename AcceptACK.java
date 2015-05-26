import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;

public class AcceptACK extends Thread {

	@Override
	public void run() {
		DatagramSocket ds = null;;
		ds = Args.ds;
		int dup = 1;
		while (true) {
			byte[] buffer = new byte[1024];
			DatagramPacket dp = new DatagramPacket(buffer,buffer.length);	
			try {
				ds.receive(dp);
				byte[] buf = dp.getData();
				byte[] plength = new byte[4];
				System.arraycopy(buf, 9, plength, 0, 4);
				int packet_len = Auxiliary.Byte2Int(plength);
				byte[] packet = new byte[packet_len];
				System.arraycopy(buf, 0, packet, 0, packet_len);
				// means this is a SYN ACK packet
				if (packet[0] == -64) {
					Args.log.recordACK(packet);
					Args.flagSYNACK = true;
					byte[] ack = new byte[4];
					System.arraycopy(packet, 5, ack, 0, 4);
					Args.ack_num = Auxiliary.Byte2Int(ack);
					System.out.println("Received a SYN ACK");
				}
				else if (packet[0] == 64 && Args.LastByteSent == 0) {
					Args.log.recordACK(packet);
					Args.connected = true;
					System.out.println("Connection established.");
				}
				// when received an ACK packet
				else if (packet[0] == 64 && Args.LastByteSent != 0) {
		    		Args.log.recordACK(packet);
					byte[] ack = new byte[4];
					System.arraycopy(packet, 5, ack, 0, 4);
					int ack_num = Auxiliary.Byte2Int(ack);
					System.out.println("Received an ACK with ack number "+ack_num);
					if (Args.LastByteAcked < ack_num) {
						Args.LastByteAcked = ack_num;
						Args.base = ack_num;
						dup = 1;
						// move send window
			    		if (Args.file_length - Args.base >= Args.MWS)
			    			Args.baseEnd = Args.base + Args.MWS;
			    		else
			    			Args.baseEnd = Args.base + Args.file_length - Args.base;
					}
					// when received same ACK, plus dup by one
					else if (ack_num == Args.LastByteAcked)
		    			dup++;
					/*
					 * If received 3 consecutive same ack, retransmit.
					 */
		    		if (dup >= 3 && ack_num < Args.file_length) {
		    			TimeoutCheck.SendPacketBySeq(ack_num);
		    			/*
		    			 * After retransmission, restart timer
		    			 */
			        	synchronized (Args.timer) {
			        		SendTimer.resetTimer();
			        		TimeoutCheck st = new TimeoutCheck();
			        		Args.timer.schedule(st, Args.timeout);
			        	}
		    			dup = 1;
		    		}
				}
				// when received a FIN packet, close the socket
				else if (packet[0] == 32) {
					Args.ds.close();
					System.out.println("Socket closed.");
					System.exit(0);
				}
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
		
	}
	
}

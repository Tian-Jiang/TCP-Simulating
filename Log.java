import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;

class Log {
	
	// name of log file
	private static String logname = "mtp_sender_log.txt";
	
	// Constructor of class Log
	public Log() {
		File f = new File(logname);
		if (f.exists()) 
			f.delete();
		new File(logname);
	}


	// write log for all packets received
	public void recordACK(byte[] ack_packet) {
		byte[] header = new byte[Args.header_len];
		System.arraycopy(ack_packet, 0, header, 0, Args.header_len);
		byte[] ack = new byte[4];
		byte[] seq = new byte[4];
		System.arraycopy(header, 5, ack, 0, 4);
		System.arraycopy(header, 1, seq, 0, 4);
		int ack_num = Auxiliary.Byte2Int(ack);
		int seq_num = Auxiliary.Byte2Int(seq);
		int packet_len = ack_packet.length;
		// set time format
		SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
	    String time = df.format(new Date());
	    
		BufferedWriter output = null;
        try {
            output = new BufferedWriter(new FileWriter(logname,true));
			output.write("Time: "+time+'\n');
//			if (header[0] == -128) 
//				output.write("Event: Received a SYN with seq number "+seq_num+'\n');
			if (header[0] == -64) {
				output.write("Event: Received a SYN ACK with ack number "+ack_num+'\n');
				output.write("Packet Details:\n");
				output.write("Header: SYN = 1  ACK = 1 ack_num: "+ack_num+
						" seq_num: "+seq_num+" packet length: "+packet_len+"\n\n");
			}
			else if (header[0] == 64) {
				output.write("Event: Received an ACK with ack number "+ack_num+'\n');
				output.write("Packet Details:\n");
				output.write("Header: SYN = 0  ACK = 1 ack_num: "+ack_num+
						" seq_num: "+seq_num+" packet length: "+packet_len+"\n\n");
			}

		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} finally {   
            try {   
                output.close();   
            } catch (IOException e) {   
                e.printStackTrace();   
            }   
        }
	}
	
	// write log for transmission of data packet
	public void recordTrans(byte[] sent_packet, boolean sent) {
		byte[] header = new byte[Args.header_len];
		byte[] data = new byte[sent_packet.length-Args.header_len];
		System.arraycopy(sent_packet, 0, header, 0, Args.header_len);
		System.arraycopy(sent_packet, Args.header_len, data, 0, data.length);
		byte[] seq = new byte[4];
		byte[] ack = new byte[4];
		System.arraycopy(header, 5, ack, 0, 4);
		System.arraycopy(header, 1, seq, 0, 4);
		int seq_num = Auxiliary.Byte2Int(seq);
		int ack_num = Auxiliary.Byte2Int(ack);
		String content = new String(data);
		int packet_len = sent_packet.length;
		// set time format
		SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
	    String time = df.format(new Date());
		
		BufferedWriter output = null;
        try {
            output = new BufferedWriter(new FileWriter(logname,true));
			output.write("Time: "+time+'\n');
			// transmitted a SYN packet
			if (header[0] == -128) {
				output.write("Event: Transmitted SYN packet with sequence number "+seq_num+'\n');
				output.write("Packet Details:\n");
				output.write("Header: SYN = 1  ACK = 0 seq_num: "+seq_num+" ack_num: "
						+ack_num+" packet length: "+packet_len+"\n\n");
			}
			// transmitted an ACK packet
			else if (header[0] == 64) {
				output.write("Event: Transmitted ACK packet with ack number "+ack_num+'\n');
				output.write("Packet Details:\n");
				output.write("Header: SYN = 0  ACK = 1 seq_num: "+seq_num+" ack_num: "
						+ack_num+" packet length: "+packet_len+"\n\n");
			}
			// transmitted a DATA packet
			else {
				if (sent) 
					output.write("Event: Transmitted DATA packet with sequence number "+seq_num+'\n');
				else
					output.write("Event: Dropped packet with sequence number "+seq_num+'\n');
				output.write("Packet Details:\n");
				output.write("Header: SYN = 0  ACK = 0 seq_num: "+seq_num+
						" packet length: "+packet_len+'\n');
				output.write("Data:\n"+content+"\n\n");
			}
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} finally {   
            try {   
                output.close();   
            } catch (IOException e) {   
                e.printStackTrace();   
            }   
        }
	}
	
	public void recordSendSYN() {
		
	}
	
	public void recordSendACK() {
		
	}
}
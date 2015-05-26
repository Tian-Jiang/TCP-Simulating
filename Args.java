import java.net.DatagramSocket;
import java.util.HashMap;
import java.util.Timer;

public class Args {
	
	public static int header_len = 13;
	public static String RECEIVE_HOST_IP = "127.0.0.1";
	public static int RECEIVE_PORT = 6666;
	public static String filename = "test1.txt";
	public static int MWS = 400;
	public static int MSS = 50;
	public static int timeout = 2000;
	public static double pdrop = 0.5;
	public static long seed;
	public static int seq_num = 0;
	public static int ack_num = 0;
	public static int local_port;
	public static DatagramSocket ds;
	public static int LastByteAcked = 0;
	public static int LastByteSent = 0;
	public static int base = 0;
	public static int baseEnd = base + MWS;
	public static Timer timer = new Timer();
	// HashMap used to simulate send window
	public static HashMap<Integer, byte[]> window = new HashMap<Integer, byte[]>();
	public static int file_length;
	// the instance of class Log
	public static Log log;
	// flag of whether connection is established
	public static boolean connected = false;
	// flag of whether SYN ACK is received
	public static boolean flagSYNACK = false;
	
	public void closeSocket() {
		ds.close();
	}	
}
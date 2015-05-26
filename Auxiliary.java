public class Auxiliary {
	// transfer integer into byte[4]
	 public static byte[] Int2Byte(int i) {   
		  byte[] result = new byte[4];   
		  result[0] = (byte)((i >> 24) & 0xFF);
		  result[1] = (byte)((i >> 16) & 0xFF);
		  result[2] = (byte)((i >> 8) & 0xFF); 
		  result[3] = (byte)(i & 0xFF);
		  return result;
		 }
	 
	 // transfer byte[4] into integer
	 public  static int Byte2Int(byte[] bytes) {
		 int num = bytes[3] & 0xFF;
		 num |= ((bytes[2] << 8) & 0xFF00);
		 num |= ((bytes[1] << 16) & 0xFF0000);
		 num |= ((bytes[0] << 24) & 0xFF000000);
		 return num;		 
	}
}
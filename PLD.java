import java.util.Random;

class PLD {
	
	private static Random random = new Random(Args.seed);
	
	public static boolean Send(){
		double p = random.nextDouble();
		if (p > Args.pdrop)
			return true;
		return false;		
	}	
}

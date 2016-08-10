package uTaboo5;

import java.util.HashMap;

public class SequenceTools {

	/** WTF? "The reverse strand to be read 3'->5'"??.
	 * Reverses a sequence (i.e forward strand). 
	 * The sequence may contain the canonical bases and unknown bases N where N is its own complementary base.
	 * The forward strand is expected to be read 5'-->3' while the reverse strand is expected to be read 3'-->5'.
	 * <br><br>Example:<br>"ATGGN" would return "NCCAT"
	 * @param seq the sequence to be reversed
	 * @return the reverse complementary sequence of input sequence
	 */
	public static String reverseStrandCreator(String seq) {
		StringBuilder revSeq = new StringBuilder(seq.length());

		for(int i = seq.length()-1; i >= 0; i--) {
			char c = seq.charAt(i);

			if(c == 'A') {
				revSeq.append('T');
			}
			else if(c == 'C') {
				revSeq.append('G');
			}
			else if(c == 'G') {
				revSeq.append('C');
			}
			else if(c == 'T') {
				revSeq.append('A');
			}
			else {
				revSeq.append('N');
			}
		}
		return revSeq.toString();
	}

	// What about case sensitivity?
	/**
	 * "Fixes" a sequence to an acceptable format by replacing all bases that are not canonical
	 * or N. Canonical bases and N's are untouched while other will be converted to N's.
	 * <br><br>Example:<br> The sequence ATKAN would become ATNAN
	 * @param seq the sequence to be "fixed"
	 * @return a fixed sequence where all bases are acceptable
	 */
	public static String baseFixer(String seq) {
		seq = seq.toUpperCase();
		StringBuilder b = new StringBuilder(seq.length());

		for(int i = 0; i < seq.length(); i++) {
			char c = seq.charAt(i);
			if(c == 'A' || c == 'C' || c == 'G' || c == 'T' ||  c == 'a' || c == 'c' || c == 'g' || c == 't') {
				
				b.append(Character.toUpperCase(c));
			}
			else {
				b.append('N');
			}
		}

		return b.toString();
	}
	
	// Class custom exception
	public static class SequenceToolException extends Exception{
		public SequenceToolException(String msg) {
			super(msg);
		}
	}
	
	public static void main(String[] args) {
		/*int length = 7000000;

		StringBuilder b = new StringBuilder(length);
		String[] bases = {"A","C","G","T","N","K","M","B","V","S","W","D","Y","R","H"};

		for(int i = 0; i < length; i++) {
			String base = bases[(int)(Math.random()*bases.length)];
			b.append(base);
			//System.out.println(base);
		}
		//System.out.println(b.toString());

		long start = System.currentTimeMillis();
		String s = baseFixer(b.toString());
		long end = System.currentTimeMillis()-start;
		System.out.println(end);

		//System.out.println(s);

		long start2 = System.currentTimeMillis();
		String s2 = reverseStrandCreator(s);
		long end2 = System.currentTimeMillis()-start2;
		System.out.println(end2);*/

		
		System.out.println(reverseStrandCreator("ATGGN"));
		System.out.println(reverseStrandCreator(""));
		System.out.println(baseFixer(""));
		System.out.println(baseFixer("QWERTYUIOPASDFGHJKLZXCVBNM"));
		System.out.println(baseFixer(""));
		//System.out.println(s2);
	}

}

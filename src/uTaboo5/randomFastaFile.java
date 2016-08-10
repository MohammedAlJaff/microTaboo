package uTaboo5;

import java.io.*;
import java.util.Random;


public class randomFastaFile {

	// Instance variables:::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::
	private static int numberOfFiles;
	
	private final int defaultLength = 10000;
	private final double defaultRange = 0.2;
	private final int defaultChunks = 10;
	private final boolean defaultOnlyATCG = true;
	private final int defaultLineLength = 80;
	
	private double range;
	private int length;
	private String filename;
	private int chunks;
	private boolean onlyATCG;
	private String fastaFile;
	private int lineLength;
	
	/**
	 * Generates a fasta file with default parameters
	 */
	public randomFastaFile() {
		numberOfFiles++;
		this.range = defaultRange;
		this.length = defaultLength;
		this.chunks = defaultChunks;
		this.onlyATCG = defaultOnlyATCG;
		this.lineLength = defaultLineLength;
		this.filename = "fastaFile" + numberOfFiles;
		System.out.println("Creating fasta sequence with default parameters");
		this.fastaFile = fastaGenerator();
	}
	
	/**
	 * Generates a fasta file with default parameters
	 * @param filename name of the fasta file
	 */
	public randomFastaFile(String filename) {
		numberOfFiles++;
		this.range = defaultRange;
		this.length = defaultLength;
		this.chunks = defaultChunks;
		this.onlyATCG = defaultOnlyATCG;
		this.lineLength = defaultLineLength;
		this.filename = filename;
		System.out.println("Creating fasta sequence with default parameters");
		this.fastaFile = fastaGenerator();
	}
	
	/**
	 * Generates a fasta file based on specified parameters
	 * @param filename name of the fasta file
	 * @param length length of each chunk body
	 * @param range range of each chunk body (0.0 - 1.0)
	 * @param chunks number of chunks
	 * @param onlyATCG true if only A,T,C,G should be used, false otherwise
	 * @param lineLength length of line in chunk body
	 */
	public randomFastaFile(String filename, int length, double range, int chunks, boolean onlyATCG, int lineLength) {
		this.filename = filename;
		this.length = length;
		this.range = range;
		this.chunks = chunks;
		this.onlyATCG = onlyATCG;
		this.lineLength = lineLength;
		numberOfFiles++;
		
		//System.out.println("Creating fasta sequence with specified parameters");
		checkParameters();
		this.fastaFile = fastaGenerator();
	}
	
	/**
	 * Generates a String representing the contents of a fasta file
	 * as specified by the parameters given to the constructor.
	 * @return a String representing the fasta file contents
	 */
	private String fastaGenerator() {
		StringBuilder fasta = new StringBuilder();
		
		for(int i = 0; i < this.chunks; i++) {
			String header = headerGenerator(i);
			String seq = chopSeq(sequenceGenerator());
			fasta.append(header + seq + "\n");
		}
		
		//return fasta.toString();
		return removeLineBreaks(fasta);
	}
	
	/**
	 * Removes line breaks (\n) from the end of a String.
	 * @param seq The string to remove line breaks from
	 * @return The same String but with removed line breaks at the end
	 */
	private String removeLineBreaks(StringBuilder seq) {
		for(int i = seq.length()-1; i >= 0; i--) {
			if(seq.charAt(i) == '\n') {
				seq.deleteCharAt(i);
			}
			else {
				return seq.toString();
			}
		}
		return seq.toString();
	}
	
	/**
	 * Breaks a sequence into chunks based on parameter {@link randomFastaFile#lineLength}, 
	 * by adding line breaks (\n).
	 * @param seq The sequence to chop up
	 * @return The same sequence, with line breaks added
	 */
	private String chopSeq(String seq) {
		StringBuilder newSeq = new StringBuilder(seq.length());
		
		for(int i = 0; i < seq.length(); i++) {
			if(i%this.lineLength == 0 && i > 0) {
				newSeq.append("\n");
			}
			newSeq.append(seq.charAt(i));
		}
		
		return newSeq.toString();
	}
	
	private String headerGenerator(int i) {
		return ">Sequence " + i + "\n";
	}
	
	/**
	 * Generates a random sequence of {@link length} specified by the parameters in the constructor.
	 * The length of generated sequence varies with the {@link range} parameter.
	 * What bases to include is specified by {@link onlyATCG}.
	 * @return
	 */
	private String sequenceGenerator() {
		StringBuilder seq = new StringBuilder(this.length);
		String[] bases;
		if(this.onlyATCG) {
			String[] tempBases = {"A", "C", "G", "T"};
			bases = tempBases;
		}
		else {
			String[] tempBases = {"A","C","G","T","N","K","M","B","V","S","W","D","Y","R","H"};
			bases = tempBases;
		}
		
		
		for(int i = 0; i < trueLength(); i++) {
			String base = bases[(int)(Math.random()*bases.length)];
			seq.append(base);
		}
		
		return seq.toString();
	}
	
	/**
	 *  Decides on the true length of a  sequence for a given chunk in the final file
	 *  
	 * @return
	 */
	private int trueLength() {
		if(Math.random() > 0.5) {
			return (int) (this.length*(1+(Math.random()*this.range)));
		}
		return (int) (this.length*(1-(Math.random()*this.range)));
	}
	
	/**
	 * The method check for the validity of the inputed parameters to the constructor.
	 */
	private void checkParameters() {
	//	System.out.println("Checking parameters...");
		if(this.length < 0) {
			System.out.println("Problem with length, setting to default value: " + defaultLength);
			this.length = 10000;
		}
		if(this.range < 0 || this.range > 1.0) {
			System.out.println("Problem with range, setting to default value: " + defaultRange);
		}
		if(this.chunks < 0) {
			System.out.println("Problem with chunks, setting to default value: " + defaultChunks);
		}
		if(this.lineLength < 0) {
			System.out.println("Problem with lineLength, setting to default value: " + defaultLineLength);
		}
		//System.out.println("Done checking parameters");
	}
	
	/**
	 * Returns a string containing the parameter information for the callin fastaFile.
	 * @return
	 */
	public String printParameters() {
		return "filename:	" + this.filename + "\n" +
			   "length:		" + this.length + "\n" +
			   "range:		" + this.range + "\n" +
			   "chunks:		" + this.chunks + "\n" +
			   "lineLength:	" + this.lineLength + "\n" +
			   "onlyATCG:	" + this.onlyATCG + "\n";
	}
	
	/**
	 * Returns the complete content of the calling fastaFil,
	 */
	public String toString() {
		return fastaFile;
	}
	
	/**
	 * This method writes the generated randomFastaFile (its content) to 
	 * a file with a filename equals to the the name specified by the field. 
	 */
	public void toFile() {
		try {
			BufferedWriter bw = new BufferedWriter(new FileWriter(new File(this.filename)));
			bw.write(fastaFile);
			bw.close();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	
	
}

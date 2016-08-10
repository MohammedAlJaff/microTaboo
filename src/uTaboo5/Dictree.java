package uTaboo5;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Vector;
import java.util.concurrent.ExecutionException;

import uTaboo5.Encoder.EncoderException;
import uTaboo5.FastaChunk.FastaChunkException;

/**
 * WARNING: NOT DOC'ED!
 * @author RockefellerSuperstar
 *
 */

public class Dictree {

	public static int maxMM = 9999;
	public static boolean last = false;

	// Instance Variable; 
	ArrayList<ArrayList<ArrayList<int[]>>> ad1s; 
	Encoder encoder;
	// Constructor;

	public Dictree(Encoder e) {
		int size = (int) Math.pow(5, e.getNcodeN());
		ad1s = new ArrayList<ArrayList<ArrayList<int[]>>>(size); 

		for(int i=0; i<size ; i++) {
			ArrayList<ArrayList<int[]>> Ad2element = new ArrayList<ArrayList<int[]>>(size);
			ad1s.add(Ad2element);

			for(int j=0; j<size; j++){
				ad1s.get(i).add(new ArrayList<int[]>(1));
			}

		}
	}

	public void populate(int[] word) {
		//this.root.getChild(word[0]).getChild(word[1]).words.add(word);
		this.ad1s.get(word[0]).get(word[1]).add(word);
	}

	public void populate(ArrayList<int[]> table) {
		for(int i=0; i<table.size(); i++) {
			this.populate(table.get(i));
		}
	}

	public void populateDeep(ArrayList<int[]> table) {
		for(int i=0; i<table.size(); i++) {
			this.populate(table.get(i));
		}
	}

	public boolean containsEaxaktly(int[] query) throws EncoderException {;

	if(this.getWords(query[0], query[1]).isEmpty()) {
		return false;
	} else {

		for(int[] w: this.getWords(query[0], query[1])) {
			if(w!= null){
				if(encoder.withinKMismatches(query, w, 0)) {
					return true;
				}
			};
		}
		return false;
	}
	}






	public ArrayList<int[]> getWords(int adress1, int adress2){
		//	return this.root.getChild(adress1).getChild(adress2).words;
		return this.ad1s.get(adress1).get(adress2);

	}

	public void printWordsIn(int adress1, int adress2) {
		for(int[] w : this.getWords(adress1, adress2)) {
			for(int j=0; j<w.length; j++) {
				System.out.print(w[j]+", ");
			}
			System.out.println();
		}
	}

	/** WARNING: NOT DONE. DO!!
	 * 
	 */
	public void emptyDictree() {

	}

	/** Currently only works for one strand.
	 * Creates and returns a Dictree made from the content of a single PartialOrganism Class.
	 * @param p
	 * @param seqWordlength
	 * @param e
	 * @return
	 * @throws EncoderException
	 */
	public static Dictree createPartialDictree(PartialOrganism p, int seqWordLength, Encoder e ) throws EncoderException {

		// Check to see if WordLength is shorter than partialSeq length.
		if(p.getSeqLength()<seqWordLength) {
			throw new RuntimeException("seqWordLength is larger than the size of the partial"); 
		}

		String[] stringTable = new String[p.getSeqLength()-seqWordLength+1];

		for(int pos=0; pos<p.getSeqLength()-seqWordLength+1; pos++) {
			stringTable[pos] = p.getSeq().substring(pos, pos+seqWordLength);
		}

		System.out.print("Sorting stringTable...");
		long t0 = System.currentTimeMillis();
		Arrays.sort(stringTable);
		long t1 = System.currentTimeMillis();
		System.out.println("Done: Elapsed time: " + (t1-t0) + "msek.");

		ArrayList<int[]> encodedWords = new ArrayList<int[]>(stringTable.length);

		System.out.print("Encoding words...");
		t0 = System.currentTimeMillis();
		for(int i=0; i<stringTable.length; i++) {
			encodedWords.add(e.encode(stringTable[i]));
		}
		t1 = System.currentTimeMillis();
		System.out.println("Done: Elapsed time: " + (t1-t0) + "msek.");

		Dictree partialDictree = new Dictree(e);

		partialDictree.populate(encodedWords);

		// Get seq. from partials 
		// Make a string table to store the seqs in.
		// Sort the string table 
		// Convert strings ad put into array of arrays
		// Populate tree with seqs.

		return partialDictree;
	}

	/** NOT DONE - NOT EVEN STARTED ON!!!
	 * Returns all words that begin with the integer that is passed
	 * 
	 * SHOULD THROW EXCEPTION IF INTEGER IS LARGER THAN MAX NcodeN number.
	 * @param adress1
	 * @return
	 */
	//	public ArrayList<ArrayList<int[]>> getWords(int adress1){
	//		
	//		ArrayList<ArrayList<int[]>> temp = new ArrayList<ArrayList<int[]>>();
	//		
	//		return temp;
	//	}

	/**
	 * A static method returns the a sorted array of Strings containing the sequences of
	 * length equal to the inputed seqWordLength taken from the inputed organism and all its
	 * partials.
	 * @param o
	 * @return
	 */
	public static String[] createStringTable(Organism o, int seqWordLength) {

		int s = o.getTotalGenomeLength() + o.getNumbPartials()*(1-seqWordLength);
		ArrayList<String> seqArrayList = new ArrayList<String>(s);

		for(PartialOrganism p: o.getPartials()) {

			for(int pos=0; pos<p.getSeqLength()-seqWordLength+1; pos++) {
				seqArrayList.add(p.getSeq().substring(pos, pos+seqWordLength));
				// ReversStrandMight Not need be included.
				//seqArrayList.add(p.getRevSeq().substring(pos, pos+seqWordLength));				
			}
		}

		String[] stringArray = seqArrayList.toArray(new String[seqArrayList.size()]);

		// sortString Table
		Arrays.sort(stringArray);	

		return stringArray;
	}



	/**
	 * Ram optimized version of the above.
	 */

	public static String[] createStringTableRAM(Organism o, int seqWordLength) {

		int s = 2*(o.getTotalGenomeLength() + o.getNumbPartials()*(1-seqWordLength));
		ArrayList<String> seqArrayList = new ArrayList<String>(s);

		for(PartialOrganism p: o.getPartials()) {

			for(int pos=0; pos<p.getSeqLength()-seqWordLength+1; pos++) {
				seqArrayList.add(p.getSeq().substring(pos, pos+seqWordLength));
				// ReversStrandMight Not need be included.
				seqArrayList.add(p.getRevSeq().substring(pos, pos+seqWordLength));				
			}
		}

		String[] stringArray = seqArrayList.toArray(new String[seqArrayList.size()]);

		// sortString Table
		Arrays.sort(stringArray);	

		return stringArray;
	}






	/**
	 * The method encodes the the inputed string table into Ncode with a given N. 
	 * The method returns the words in array of array.
	 * @param s
	 * @param e
	 * @param NcodeN
	 * @return
	 * @throws EncoderException
	 */
	public static ArrayList<int[]> createEncodedWords(String[] s, Encoder e,  int NcodeN) throws EncoderException{

		if(NcodeN!=e.getNcodeN()) {
			throw new RuntimeException("Wrongly specifief NcodeN value");
		}

		ArrayList<int[]> encodedWords = new ArrayList<int[]>(s.length);

		for(int i=0; i<s.length; i++) {
			encodedWords.add(e.encode(s[i]));
		}

		return encodedWords;
	}

	public static Dictree createDictree(Organism o, Encoder e, int seqWordLength, int NcodeN ) throws EncoderException {

		// Initialize Dictree.
		Dictree dt = new Dictree(e);

		// Get stringStable and populating dictree with encodedWords arraylist.

		dt.populate(
				createEncodedWords(createStringTable(o, seqWordLength), e, NcodeN) 
				);
		// Return populated dictree. 
		return dt;
	}


	public static Dictree createDictreeRAM(Organism o, Encoder e, int seqWordLength, int NcodeN ) throws EncoderException {

		// Initialize Dictree.
		Dictree dt = new Dictree(e);

		// Get stringStable and populating dictree with encodedWords arraylist.

		dt.populate(
				createEncodedWords(createStringTableRAM(o, seqWordLength), e, NcodeN) 
				);
		// Return populated dictree. 
		return dt;
	}



	public int[] containsWithin3(int[] query, int thresh, Encoder e, ArrayList<HashMap<Integer, ArrayList<int[]>>> h, int[] tempVal) throws EncoderException {
		
		if(tempVal[2] == 1) {
			if(tempVal[1] > thresh+1 && tempVal[3] == tempVal[4]-1) {
				//System.out.println(maxMM);
				tempVal[1] = tempVal[1]-1;
				tempVal[0] = 0;
				return tempVal;			
			}
			else {
				tempVal[2] = 0;
				tempVal[1] = 9999;
			}
		}

		int firstElement = query[0];
		int secondElement = query[1];

		ArrayList<int[]> possibleAdress1;

		if(thresh >= e.getNcodeN()) {
			possibleAdress1 = h.get(e.getNcodeN()).get(firstElement);
		} else {
			possibleAdress1 = h.get(thresh).get(firstElement);
		}


		for(int[] brick1 : possibleAdress1 ) {

			ArrayList<int[]> possibleAdress2;

			if(thresh - brick1[1] >= e.getNcodeN()) {
				possibleAdress2 = h.get(e.getNcodeN()).get(secondElement);
			} else {
				possibleAdress2 = h.get(thresh-brick1[1]).get(secondElement);
			}



			for(int[] brick2: possibleAdress2) {

				for(int[] seq: this.getWords(brick1[0], brick2[0])) {
					int mm = e.numbMismatch(query, seq);
					if(mm <= thresh) {
						//System.out.println("Paths traversed: " + pathCount);;
						tempVal[1] = 9999;
						tempVal[2] = 0;
						tempVal[0] = 1;
						return tempVal;
					}
					else {
						if(mm < tempVal[1]) {
							tempVal[1] = mm;
						}
					}
				}
			}	
		}
		//System.out.println("Paths traversed: " + pathCount);;
		if(tempVal[1] != 9999) {
			tempVal[2] = 1;
			tempVal[1] = thresh +1;
		}
		tempVal[0] = 0;
		return tempVal;
	}








}

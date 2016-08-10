package uTaboo5;

import uTaboo5.FastaChunk.FastaChunkException;

public class PartialOrganism {
	
	/**
	 * Identifier for the partial organism, e.g ">E.coli, chromosome 2".
	 */
	private String gi;
	
	/**
	 * Sequence for the partial organism, e.g "ATGCGATG".
	 */
	private final String seq;
	
	/**
	 * The reverse sequence of the {@link PartialOrganism#seq} field.
	 */
	private String revSeq;
	
	/**
	 * Generates a complete PartialOrganism object. This means generating an identifier, a sequence
	 * and the reverse sequence. This constructor also handles any non-canonical bases by....
	 * @param fastaChunk a String consisting of a fasta header and a sequence body
	 * @throws FastaChunkException 
	 */
	public PartialOrganism(FastaChunk chunk) throws FastaChunkException {
		gi = chunk.getHead();
		seq = SequenceTools.baseFixer(chunk.getBody());
		revSeq = SequenceTools.reverseStrandCreator(seq);
		//TODO baseFixer, revSeq
	}
	
	/**
	 * This returns the length of the partialGenome, ie 
	 * the number if bases in the partial.
	 * @return
	 */
	public int getSeqLength() {
		return this.seq.length();
		
	}
	
	/**
	 * Generates a copy partialOrganism of the inputed partialOrganism.
	 * @param p
	 */
	private PartialOrganism(PartialOrganism p) {
		
		this.gi = p.getGi();
		this.seq = p.getSeq();
		this.revSeq = p.getRevSeq();
	}
	
	/**
	 * Gets the gi number of the partial organism
	 * @return the gi number of the partial organism, {@link PartialOrganism#gi}
	 */
	public String getGi() {
		return gi;
	}
	
	/**
	 * Gets the sequence of the partial organism
	 * @return the sequence of the partial organism, {@link PartialOrganism#seq}
	 */
	public String getSeq() {
		return seq;
	}
	
	/**
	 * Gets the reverse sequence of the partial organism
	 * @return the reverse sequence of the partial organism, {@link PartialOrganism#revSeq}
	 */
	public String getRevSeq() {
		return revSeq;
	}
	
	/**
	 * Creates a and returns a clone of the input PartialOrganism
	 */
	public static PartialOrganism PartialOrganismFactory(PartialOrganism p) {
		return new PartialOrganism(p);
	}
	
	/**
	 * Returns the identifier, sequence and reverse sequence of the partial organism<br><br>
	 * Example:<br>
	 * >E.coli, chromosome 2<br>
	 * Seq: &emsp; ATGAT<br>
	 * revSeq: TACTA
	 */
	public String toString() {
		return gi + "\n" + "Seq:\t" + seq + "\nRevSeq:\t" + revSeq;
	}
	
}

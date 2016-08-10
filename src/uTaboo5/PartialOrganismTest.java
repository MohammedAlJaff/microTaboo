package uTaboo5;

import static org.junit.Assert.*;

import java.io.IOException;

import org.junit.Test;

import uTaboo5.FastaChunk.FastaChunkException;

public class PartialOrganismTest {

	@Test 
	public void getLengthTest1() throws FastaChunkException, IOException {
		String s1 = ">Bla1\nACGT";
		String s2 = ">Bla2\nAAACCCGGGTTT";
		
		PartialOrganism p1 = new PartialOrganism(new FastaChunk(s1));
		PartialOrganism p2 = new PartialOrganism(new FastaChunk(s2));
		
		boolean b1 = p1.getSeqLength()==4;
		boolean b2 = p2.getSeqLength()==12;
		
		boolean[] actual = {b1, b2};
		boolean[] expected = {true, true}; 
		
		assertArrayEquals(expected, actual);
	}
	
	
	@Test
	public void testGetSeqAndGetRevSeq() throws FastaChunkException, IOException {
		
		String s1 = ">Bla\nATTTATTT";
		FastaChunk f1 = new FastaChunk(s1);
		PartialOrganism p = new PartialOrganism(f1);
		
		boolean b1 = p.getGi().equals(">Bla");
		boolean b2 = p.getSeq().equals("ATTTATTT");
		boolean b3 = p.getRevSeq().equals("AAATAAAT");
		
		boolean[] actual = {b1, b2, b3};
		boolean[] expected = {true, true, true};
		
		assertArrayEquals(expected, actual);
	}
	
	@Test
	public void testPartialOrganismFactory() throws FastaChunkException, IOException {
		
		String s1 = ">Bla\nATTTATTT";
		FastaChunk f1 = new FastaChunk(s1);
		PartialOrganism p = new PartialOrganism(f1);
		
		PartialOrganism q = PartialOrganism.PartialOrganismFactory(p);
		
		boolean b1 = q.getGi().equals(p.getGi());
		boolean b2 = q.getSeq().equals(p.getSeq());
		boolean b3 = q.getRevSeq().equals(p.getRevSeq()); 
		
		boolean[] actual = {b1, b2, b3};
		boolean[] expected = {true, true, true}; 
		
	}
}


	

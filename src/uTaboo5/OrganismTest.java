package uTaboo5;

import static org.junit.Assert.*;

import org.junit.Test;

import uTaboo5.Encoder.EncoderException;
import uTaboo5.Organism.OrganismException;

public class OrganismTest {

	@Test
	public void numberOfPartialsTest1() {
		randomFastaFile rff = new randomFastaFile("organismTest.txt", 100, 0.2, 6, true, 50);
		rff.toFile();
		Organism o = new Organism("organismTest.txt");
		
		assertEquals(6, o.getPartials().size());
	}
	
	@Test
	public void getGiTest1() {
		randomFastaFile rff = new randomFastaFile("organismTest.txt", 100, 0.2, 6, true, 50);
		rff.toFile();
		Organism o = new Organism("organismTest.txt");
		
		assertEquals("organismTest.txt", o.getGi());
	}

	@Test
	public void getTotalGenomeLength()	{
		
		randomFastaFile f1 = new randomFastaFile("getLength1.txt", 12345, 0, 1,   false,  80);
		randomFastaFile f2 = new randomFastaFile("getLength2.txt", 10000, 0, 5,   false, 100);
		randomFastaFile f3 = new randomFastaFile("getLength3.txt", 100,   0, 100, false,  50);
		
		f1.toFile();
		f2.toFile();
		f3.toFile();
		
		Organism o1 = new Organism("getLength1.txt");
		Organism o2 = new Organism("getLength2.txt");
		Organism o3 = new Organism("getLength3.txt");
		
		boolean b1 = o1.getTotalGenomeLength()==12345;
		boolean b2 = o2.getTotalGenomeLength()==50000;
		boolean b3 = o3.getTotalGenomeLength()==10000;
		
		boolean[] actual = {b1, b2, b3};
		boolean[] expected = {true, true, true};
		
		assertArrayEquals(expected, actual);
	}

	@Test
	public void getNumbPartialTest1() {
		randomFastaFile f1 = new randomFastaFile(
				"getNumbTest1.txt", 100, 0.0, 112, false, 80 );
		f1.toFile();
		Organism o1 = new Organism("getNumbTest1.txt");
		assertEquals(112, o1.getNumbPartials());
	}

}

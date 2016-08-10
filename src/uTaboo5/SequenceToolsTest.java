package uTaboo5;

import static org.junit.Assert.*;

import org.junit.Test;

import uTaboo5.SequenceTools.SequenceToolException;

public class SequenceToolsTest {
	
	@Test (expected = SequenceToolException.class)
	public void customSequenceToolExceptionTest1() {
		fail("Not tested yet!");
	}

	@Test
	public void testReverseStrandCreator() {
		String s1 = "AAATTT";
		String s2 = "TAACGC";
		
		String return1 = SequenceTools.reverseStrandCreator(s1);
		String return2 = SequenceTools.reverseStrandCreator(s2);
		
		boolean b1 = return1.equals("AAATTT");
		boolean b2 = return2.equals("GCGTTA");
		
		boolean[] actual = {b1, b2};
		boolean[] expected = {true, true};
		
		assertArrayEquals(expected, actual);
	}

	@Test
	public void testBaseFixer() {
		String s1 = "AAATTTCGCC";
		String return1 = SequenceTools.baseFixer(s1);	
		
		String s2 = "atcg";
		String return2 = SequenceTools.baseFixer(s2);	
		
		String s3 = "ANATkTGQqCCS";
		String return3 = SequenceTools.baseFixer(s3);	
		
		String s4 = "NaNtNcNg";
		String return4 = SequenceTools.baseFixer(s4);	
			
		boolean b1 = return1.equals(s1);
		boolean b2 = return2.equals("ATCG");
		boolean b3 = return3.equals("ANATNTGNNCCN");
		boolean b4 = return4.equals("NANTNCNG");
		boolean b5 = return2.equals(s2);
		
		boolean[] actual = {b1, b2, b3, b4, b5};
		boolean[] expected = {true, true, true, true, false};
		
		assertArrayEquals(expected, actual);
	}
}

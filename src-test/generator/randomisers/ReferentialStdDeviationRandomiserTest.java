package generator.randomisers;

import static org.junit.Assert.fail;
import generator.extenders.RandomiserInstance;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;

import org.junit.Before;
import org.junit.Test;

public class ReferentialStdDeviationRandomiserTest {
	ReferentialStdDeviationRandomiser generator;
	@Before
	public void setUp() throws Exception {
		generator = new ReferentialStdDeviationRandomiser();
		
		
		LinkedHashMap<String, String> properties = new LinkedHashMap<String, String>();
		properties.put("inputFile", "test/testData.txt");
		properties.put("rangesNum", "2");
		properties.put("amavgPos", "0");
		properties.put("amstdPos", "1");
		properties.put("RefPos0", "0");
		properties.put("KeyPos0", "1");
		properties.put("RefPos1", "0");
		properties.put("KeyPos1", "1");
		
		RandomiserInstance ri = new RandomiserInstance("ReferentialStdDeviationRandomiser", "TEST", "", properties);
		generator.setRandomiserInstance(ri);
	}

	@Test
	public void test() {
		List<String> dsList = new ArrayList<String>();
		generator.generatefromlist(0, 2, dsList);
		fail("Not yet implemented");
	}

}

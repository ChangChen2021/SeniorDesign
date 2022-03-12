package test;

import org.junit.Before;
import org.junit.Test;

import main.fields.VectorField;

/**
 * Test reading in a json file.
 * 
 * @author Manuela Kastner
 *
 */
public class Test_ReadInJSON {
	@Before
	public void init() {
		VectorField.setSavingPath("./testResults/Test_ReadInJSON");
	}
	
	@Test
	public void readInJson() {
		// valid json file that was created with this software
		String jsonFile = "./testResults/Test_ReadInJSON/input.json";

		VectorField field = VectorField.readInJSON(jsonFile);

		
		field.save("output");
	}
	
	@Test
	public void readInJson2() {
		// valid json file with misspelled field ("homogenou")
		String jsonFile = "./testResults/Test_ReadInJSON/input2.json";

		VectorField field = VectorField.readInJSON(jsonFile);

	}
	
	@Test
	public void readInJson3() {
		// valid json file with double value in quotation marks ("1.0")
		String jsonFile = "./testResults/Test_ReadInJSON/input3.json";

		VectorField field = VectorField.readInJSON(jsonFile);

	}
	
	@Test
	public void readInJson4() {
		// valid json file with misspelled field property ("valu")
		String jsonFile = "./testResults/Test_ReadInJSON/input4.json";

		VectorField field = VectorField.readInJSON(jsonFile);

	}
}

package testOptimized;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.io.PrintWriter;
import java.lang.invoke.MethodHandles;
import java.util.UUID;

import javax.json.Json;
import javax.json.JsonObject;
import javax.json.JsonReader;

import org.junit.AfterClass;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.TestWatcher;
import org.junit.runner.Description;

import main.fields.HomogenousField;
import main.fields.SuperposedField;
import main.fields.TangentialField;
import main.fields.VectorField;
import main.util.FieldProperty;

public class TestIdBehaviour {
	//---------For log file only -----------------------------------------------------------------
		private static StringBuilder log = new StringBuilder();
		@AfterClass
	    public static void afterClass() throws IOException {
	        PrintWriter logFile = new PrintWriter("testResults/Test_OptimizedImplementation/" 
	        		+ MethodHandles.lookup().lookupClass().getSimpleName() +".txt", "UTF-8");
	        logFile.write(log.toString());
	        logFile.close();
	    }
		@Rule
	    public TestWatcher watchman = new TestWatcher() {

	        @Override
	        protected void failed(Throwable e, Description description) {
	            if (description != null) {
	        		log.append(description);
	            }
	            if (e != null) {
	                log.append(' ');
	                log.append(e);
	            }
	            log.append(" FAIL\n");
	        }

	        @Override
	        protected void succeeded(Description description) {
	            if (description != null) {
	                log.append(description);
	            }
	            log.append(" OK\n");
	        }
	    };
	  //---------End of logging -----------------------------------------------------------------
	
	@Before
	public void setup() {
		VectorField.setExportPath("./testResults/Test_Opt_ID");
		VectorField.setSavingPath("./testResults/Test_Opt_ID");
	}
	
	@Test
	public void saveID() {
		String fieldName = "testSave";
		HomogenousField hom = new HomogenousField();
		UUID id =hom.ID;
		hom.save(fieldName);
		
		InputStream fis = null;
		try {
			fis = new FileInputStream("./testResults/Test_Opt_ID/" + fieldName + ".json");
		} catch (FileNotFoundException e) {
			System.err.println("Check for misspelled name, missing or extra quotation marks!");
			e.printStackTrace();
		}		
		JsonReader reader = Json.createReader(fis);
		JsonObject fieldObject = reader.readObject();
		reader.close();
		
		String fieldType = fieldObject.keySet().toArray()[0].toString();
		JsonObject fieldProps = fieldObject.getJsonObject(fieldType).getJsonObject(FieldProperty.PROPERTIES.toString().toLowerCase());
		String readId = fieldProps.get(FieldProperty.ID.toString().toLowerCase()).toString();
		UUID readUUID = UUID.fromString(readId.substring(1, readId.length()-1));
		
		Assert.assertEquals(id, readUUID);
		
	}
	
	@Test
	public void loadIdTwice() {
		String fieldName = "testLoadTwice";
		VectorField readField1 = VectorField.readInJSON("./testData/Test_Opt_ID/" + fieldName + ".json");
		VectorField readField2 = VectorField.readInJSON("./testData/Test_Opt_ID/" + fieldName + ".json");
		System.out.println(readField1.ID);
		System.out.println(readField2.ID);
		UUID correctID = UUID.fromString("c796b9c2-187b-4f56-bb0f-8f6a7e6dcd7d");	

		Assert.assertTrue(readField1.ID.equals(correctID)); //first load must get JSON ID
		Assert.assertFalse(readField2.ID.equals(correctID)); //second load may not get JSON ID
		
	}
	
	@Test
	public void loadFalseId() {
		String fieldName = "testCorruptId";
		VectorField readField1 = VectorField.readInJSON("./testData/Test_Opt_ID/" + fieldName + ".json");		
		System.out.println(readField1.ID);
		Assert.assertFalse(readField1.ID.toString().isEmpty());		 //on corrupted JSON ID, the filed must get new id
		Assert.assertFalse(readField1.ID.toString().equals("false-ID")); //new id must be different to the invalid JSON id
			
	}
	
	@Test
	public void loadNoId() {
		String fieldName = "testNoId";
		VectorField readField1 = VectorField.readInJSON("./testData/Test_Opt_ID/" + fieldName + ".json");
		System.out.println(readField1.ID);
		Assert.assertFalse(readField1.ID.toString().isEmpty());		 //on absent JSON ID, the filed must get new id
		
	}
	
	@Test
	public void testNewIdWhenSuperposing() {
		HomogenousField hom = new HomogenousField();
		UUID homID = hom.ID;
		TangentialField tan = new TangentialField();
		UUID tanID = tan.ID;
		SuperposedField sup = VectorField.superposition(hom, tan);
		UUID supID = sup.ID;
		Assert.assertFalse(homID.equals(tanID));
		Assert.assertFalse(homID.equals(supID));
		Assert.assertFalse(tanID.equals(supID));
	}
}

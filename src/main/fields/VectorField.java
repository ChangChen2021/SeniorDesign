package main.fields;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.UUID;

import javax.json.Json;
import javax.json.JsonArray;
import javax.json.JsonArrayBuilder;
import javax.json.JsonObject;
import javax.json.JsonObjectBuilder;
import javax.json.JsonReader;
import javax.json.JsonWriter;
import javax.json.JsonWriterFactory;
import javax.json.stream.JsonGenerator;

import org.apache.commons.math3.geometry.euclidean.twod.Vector2D;
import org.apache.commons.math3.util.Pair;

import javafx.scene.control.TextField;
import main.util.Axis;
import main.util.FieldAndOperation;
import main.util.FieldProperty;
import main.util.Method;
import main.util.OperationProperty;
import main.util.Proportionality;
import main.util.shape.Circle;
import main.util.shape.Ellipse;
import main.util.shape.Rectangle;
import main.util.shape.Shape;
import main.util.shape.ShapeEnum;
import main.util.shape.ShapeProperty;
import main.util.shape.Square;
import main.util.shape.VerticalTunnel;
import model.Data;


/**
 * Represents a vector field.
 * 
 * @author Manuela Kastner
 * @author Christian Weber
 */
public abstract class VectorField {
	public static ArrayList<Data> dataStorage = new ArrayList<Data>();

	private static String exportPath = null;
	private static String savingPath = null;
	
	private static List<UUID> idsInSession = new LinkedList<UUID>();

	private static String parent = "";

	/**
	 * zero point
	 */
	private final Vector2D ZERO = new Vector2D(0.0, 0.0);
	
	/**
	 * UUID of type-4 (random)
	 */
	public final UUID ID;

	/**
	 * contains the fields and operation of this
	 */
	private List<Map<FieldAndOperation, Object[]>> fields = new LinkedList<Map<FieldAndOperation, Object[]>>();

	/**
	 * Alternative vector field representation using matrices for linear transformations
	 */
	private VectorFieldLight lightRepresentation;
	
	
	/**
	 * Constructor: add field to list and to json object
	 * 
	 * @param fieldEnum
	 *            enum to describe field
	 * @param fieldProps
	 *            field properties
	 */
	public VectorField(FieldAndOperation fieldEnum, Object[] fieldProps) {
		UUID candidate;
		do {
			candidate = UUID.randomUUID();
		} while (idsInSession.contains(candidate));
		ID = candidate;
		idsInSession.add(ID);
		
		Map<FieldAndOperation, Object[]> field = new HashMap<>();
		field.put(fieldEnum, fieldProps);
		fields.add(field);
		
		Pair<FieldAndOperation, Object[]> fieldFunction = new Pair<>(fieldEnum, fieldProps);
		lightRepresentation = new VectorFieldLight(fieldFunction);
	}
	
	VectorField(UUID fieldID, FieldAndOperation fieldEnum, Object[] fieldProps) {
		UUID candidate = fieldID;
		while (idsInSession.contains(candidate)) {	//If field gets loaded more than once, a random ID is assigned
			candidate = UUID.randomUUID();
		}
		ID = candidate;
		idsInSession.add(ID);
		
		Map<FieldAndOperation, Object[]> field = new HashMap<>();
		field.put(fieldEnum, fieldProps);
		fields.add(field);
		
		Pair<FieldAndOperation, Object[]> fieldFunction = new Pair<>(fieldEnum, fieldProps);
		lightRepresentation = new VectorFieldLight(fieldFunction);
	}

	/**
	 * Returns the string representation of an object (enum) to lower case
	 * 
	 * @param obj
	 *            enum
	 * @return string representation to lower case
	 */
	private static String prettify(Object obj) {
		return obj.toString().toLowerCase();
	}
	
	/**
	 * Returns a VectorField of type SuperpositionedField, that superposes all passed vector fields (at least two)
	 * 
	 * @param field1 to be superposed
	 * @param field2 to be superposed
	 * @param multibleFields to superpose more than two fields at a time
	 * @return the superposed field
	 */
	public static SuperposedField superposition(VectorField field1, VectorField field2, VectorField... multibleFields) {
		SuperposedField newField = new SuperposedField();
		newField.internalSuperposition(field1);
		newField.internalSuperposition(field2);
		for(VectorField f : multibleFields) {
			newField.internalSuperposition(f);
		}
		return newField;
	}

	/**
	 * Rotate field around point of origin.
	 * 
	 * @param degree
	 *            rotate to this angle
	 */
	public void rotate(double degree) {
		rotate(degree, ZERO);
	}

	/**
	 * Rotate field around specified point.
	 * 
	 * @param degree
	 *            rotate to this angle
	 * @param rotationPoint
	 *            point to rotate around
	 */
	public void rotate(double degree, Vector2D rotationPoint) {
		// -degree so that rotation works clockwise; otherwise it works
		// counter-clockwise
		List<Map<FieldAndOperation, Object[]>> fieldsCopy = deepCopyInternRepresentation();
		Map<FieldAndOperation, Object[]> rot = new HashMap<>();
		rot.put(FieldAndOperation.ROTATE, new Object[] { degree, rotationPoint, fieldsCopy });
		fields.add(rot);
		
		lightRepresentation.rotate(degree, rotationPoint);
	}

	/**
	 * Scale a specified area (shape).
	 * 
	 * @param shape
	 *            shape to scale
	 * @param factor
	 *            scaling factor
	 */
	public void scaleArea(Shape shape, double factor) {
		Map<FieldAndOperation, Object[]> mask = new HashMap<>();
		mask.put(FieldAndOperation.SCALE_AREA, new Object[] { shape, factor });
		fields.add(mask);
		
		lightRepresentation.scaleArea(shape, factor);
	}

	/**
	 * Shear by specified axis.
	 * 
	 * @param factor
	 *            shearing factor
	 */
	public void shear(double factor) {
		shear(factor, Axis.X);
	}

	/**
	 * Shear by specified axis.
	 * 
	 * @param factor
	 *            shearing factor
	 * @param axis
	 *            axis to shear by
	 */
	public void shear(double factor, Axis axis) {
		List<Map<FieldAndOperation, Object[]>> fieldsCopy = deepCopyInternRepresentation();
		Map<FieldAndOperation, Object[]> s = new HashMap<>();
		s.put(FieldAndOperation.SHEAR, new Object[] { factor, axis, fieldsCopy });
		fields.add(s);
		
		lightRepresentation.shear(factor, axis);
	}

	/**
	 * Mask a specified shape.
	 * 
	 * @param shape
	 *            shape to mask
	 */
	public void mask(Shape shape) {
		Map<FieldAndOperation, Object[]> mask = new HashMap<>();
		mask.put(FieldAndOperation.MASK, new Object[] { shape });
		fields.add(mask);
		
		lightRepresentation.mask(shape);
	}

	/**
	 * Translate by specified values.
	 * 
	 * @param alongXaxis
	 *            translation value along x axis
	 * @param alongYaxis
	 *            translation value along y axis
	 */
	public void translate(double alongXaxis, double alongYaxis) {
		List<Map<FieldAndOperation, Object[]>> fieldsCopy = deepCopyInternRepresentation();
		Map<FieldAndOperation, Object[]> s = new HashMap<>();
		s.put(FieldAndOperation.TRANSLATE, new Object[] { alongXaxis, alongYaxis, fieldsCopy });
		fields.add(s);
		
		lightRepresentation.translate(alongXaxis, alongYaxis);
	}

	/**
	 * Deep copy a list of maps.
	 * 
	 * @return copy
	 */
	private List<Map<FieldAndOperation, Object[]>> deepCopyInternRepresentation() {
		List<Map<FieldAndOperation, Object[]>> fieldsCopy = new LinkedList<Map<FieldAndOperation, Object[]>>();
		Map<FieldAndOperation, Object[]> mapCopy = null;
		Iterator<Map<FieldAndOperation, Object[]>> it = fields.iterator();
		Map<FieldAndOperation, Object[]> m = null;
		while (it.hasNext()) {
			m = it.next();
			mapCopy = new HashMap<>();
			mapCopy.put(m.keySet().stream().findFirst().get(), (Object[]) m.values().toArray()[0]);
			fieldsCopy.add(mapCopy);
		}
		return fieldsCopy;
	}

	/**
	 * Superposition a field with another field. 
	 * 
	 * Please use the static superposition method supplied by this class. 
	 * 
	 * @param field
	 *            field to superposition with
	 * @deprecated  As of 2021 replaced by {@link #superposition(VectorField, VectorField, VectorField...)}
	 */
	@Deprecated
	public void superposition(VectorField field) {
		List<Map<FieldAndOperation, Object[]>> fieldsCopy = field.deepCopyInternRepresentation();
		Map<FieldAndOperation, Object[]> s = new HashMap<>();
		s.put(FieldAndOperation.SUPERPOSITION, new Object[] { fieldsCopy });
		fields.add(s);
		
		lightRepresentation.superposition(field.getLightRepresentation());
	}
	
	/**
	 * Superposition a field with another field. 
	 * 
	 * @param field
	 *            field to superposition with
	 */
	protected void internalSuperposition(VectorField field) {
		List<Map<FieldAndOperation, Object[]>> fieldsCopy = field.deepCopyInternRepresentation();
		Map<FieldAndOperation, Object[]> s = new HashMap<>();
		s.put(FieldAndOperation.SUPERPOSITION, new Object[] { fieldsCopy });
		fields.add(s);
		
		lightRepresentation.superposition(field.getLightRepresentation());
	}

	/**
	 * 
	 * @return the light representation of this vector field
	 */
	private VectorFieldLight getLightRepresentation() {
		return lightRepresentation;
	}

	/**
	 * Scale by factor.
	 * 
	 * @param factor
	 *            scaling factor
	 */
	public void scale(double factor) {
		Map<FieldAndOperation, Object[]> s = new HashMap<>();
		s.put(FieldAndOperation.SCALE, new Object[] { factor });
		fields.add(s);
		
		lightRepresentation.scale(factor);
	}

	/**
	 * Return function value at specified point (x,y) using the light representation 
	 * 
	 * @param x coordinate
	 * @param y coordinate
	 * @return vector at given coordniate
	 */
	public Vector2D getFunctionValueOptimized(double x, double y) {
		return lightRepresentation.getFunctionValue(x, y);
	}
	
	/**
	 * Return function value at specified point (x,y)
	 * 
	 * @param x
	 *            x coordinate
	 * @param y
	 *            y coordinate
	 * @return function value
	 */
	public Vector2D getFunctionValue(double x, double y) {
		return getFunctionValue(x, y, this.fields);
	}

	/**
	 * Return function value of a field at specified point (x,y)
	 * 
	 * @param x
	 *            x coordinate
	 * @param y
	 *            y coordinate
	 * @param fields
	 *            field to evaluate
	 * @return function value
	 */
	@SuppressWarnings("unchecked")
	private Vector2D getFunctionValue(double x, double y, List<Map<FieldAndOperation, Object[]>> fields) {
		Vector2D input = new Vector2D(x, y);
		Vector2D intermediateResult = input;
		Vector2D output = null;
		Object[] parameters;
		Map<FieldAndOperation, Object[]> m;
		FieldAndOperation currentOperation;
		Iterator<Map<FieldAndOperation, Object[]>> it = fields.iterator();
		while (it.hasNext()) {
			m = it.next();
			currentOperation = m.keySet().stream().findFirst().get();
			parameters = (Object[]) m.values().toArray()[0];

			// field parameters
			double value;
			double radius;
			Proportionality proportionality;

			// operation parameters
			double angleInDegree;
			Vector2D rotationPoint;
			double factor;
			Axis axis;
			Shape shape;
			double along_x;
			double along_y;
			Vector2D expectedOutput;

			switch (currentOperation) {
			case ZERO:
				output = Method.zero();
				break;
			case HOMOGENOUS:
				value = (double) parameters[0];

				output = Method.homogenous(intermediateResult, value);
				break;
			case RADIAL:
				value = (double) parameters[0];
				radius = (double) parameters[1];
				proportionality = (Proportionality) parameters[2];

				output = Method.radial(intermediateResult, value, radius, proportionality);
				break;
			case TANGENTIAL:
				value = (double) parameters[0];
				radius = (double) parameters[1];
				proportionality = (Proportionality) parameters[2];

				output = Method.tangential(intermediateResult, value, radius, proportionality);
				break;
			case BORDER:
				value = (double) parameters[0];
				proportionality = (Proportionality) parameters[1];

				output = Method.border(intermediateResult, value, proportionality);
				break;
			case TRANSLATE:
				// direct calculation because it's easier
				// no method implemented in Method
				along_x = (double) parameters[0];
				along_y = (double) parameters[1];
				List<Map<FieldAndOperation, Object[]>> fieldWithoutTranslation = (List<Map<FieldAndOperation, Object[]>>) parameters[2];

				output = (getFunctionValue(x - along_x, y - along_y, fieldWithoutTranslation));
				break;
			case MASK:
				shape = (Shape) parameters[0];

				output = Method.mask(intermediateResult, input, shape);
				break;
			case ROTATE:
				angleInDegree = (double) parameters[0];
				rotationPoint = (Vector2D) parameters[1];
				Vector2D rotateInitialInput = Method.rotate(new Vector2D(x, y), -angleInDegree, rotationPoint);
				List<Map<FieldAndOperation, Object[]>> fieldBeforeRotation = (List<Map<FieldAndOperation, Object[]>>) parameters[2];
				expectedOutput = (getFunctionValue(rotateInitialInput.getX(), rotateInitialInput.getY(),
						fieldBeforeRotation));

				output = Method.rotate(expectedOutput, angleInDegree, ZERO);
				break;
			case SCALE:
				factor = (double) parameters[0];
				output = Method.scale(intermediateResult, factor);
				break;
			case SHEAR:
				factor = (double) parameters[0];
				axis = (Axis) parameters[1];
				Vector2D shearInitialInput = Method.shear(new Vector2D(x, y), -factor, axis);
				List<Map<FieldAndOperation, Object[]>> fieldBeforeShearing = (List<Map<FieldAndOperation, Object[]>>) parameters[2];
				expectedOutput = (getFunctionValue(shearInitialInput.getX(), shearInitialInput.getY(),
						fieldBeforeShearing));

				output = Method.shear(expectedOutput, factor, axis);
				break;
			case SUPERPOSITION:
				// direct calculation because it's easier
				// no method implemented in Method
				List<Map<FieldAndOperation, Object[]>> superpostioningField = (List<Map<FieldAndOperation, Object[]>>) parameters[0];

				output = getFunctionValue(x, y, superpostioningField).add(intermediateResult);
				break;
			case SCALE_AREA:
				shape = (Shape) parameters[0];
				factor = (double) parameters[1];

				output = Method.scaleArea(intermediateResult, input, shape, factor);
				break;
			default:
				System.err.println("FIELD OR OPERATION DOES NOT EXIST");
				break;
			}
			intermediateResult = output;

		}
		return output;
	}

	/**
	 * Get json representation of this field.
	 * 
	 * @param input
	 *            internal representation of the vector field
	 * @return json representation
	 */
	private JsonObjectBuilder getJSON(List<Map<FieldAndOperation, Object[]>> input) {
		JsonObjectBuilder job = Json.createObjectBuilder();
		JsonObjectBuilder field = Json.createObjectBuilder();
		JsonArrayBuilder operations = Json.createArrayBuilder();
		
		String fieldName = "";

		Object[] parameters;
		Map<FieldAndOperation, Object[]> m;
		FieldAndOperation currentOperation;
		Iterator<Map<FieldAndOperation, Object[]>> it = input.iterator();
		while (it.hasNext()) {
			m = it.next();
			currentOperation = m.keySet().stream().findFirst().get();
			parameters = (Object[]) m.values().toArray()[0];

			// field parameters
			final String fieldID = ID.toString();
			double value;
			double radius;
			Proportionality proportionality;

			// operation parameters
			double angleInDegree;
			Vector2D rotationPoint;
			double factor;
			Axis axis;
			Shape shape;
			double along_x;
			double along_y;

			switch (currentOperation) {
			case ZERO:
				fieldName = prettify(FieldAndOperation.ZERO);
				
				field.add(prettify(FieldProperty.PROPERTIES),
						Json.createObjectBuilder() ); //empty Properties
				break;
			case HOMOGENOUS:
				value = (double) parameters[0];

				fieldName = prettify(FieldAndOperation.HOMOGENOUS);
				field.add(prettify(FieldProperty.PROPERTIES),
						Json.createObjectBuilder()
							.add(prettify(FieldProperty.ID), fieldID)
							.add(prettify(FieldProperty.VALUE), value));
				break;
			case RADIAL:
				value = (double) parameters[0];
				radius = (double) parameters[1];
				proportionality = (Proportionality) parameters[2];

				fieldName = prettify(FieldAndOperation.RADIAL);
				field.add(prettify(FieldProperty.PROPERTIES),
						Json.createObjectBuilder().add(prettify(FieldProperty.VALUE), value)
								.add(prettify(FieldProperty.ID), fieldID)
								.add(prettify(FieldProperty.RADIUS), radius)
								.add(prettify(FieldProperty.PROPORTIONALITY), prettify(proportionality)));
				break;
			case TANGENTIAL:
				value = (double) parameters[0];
				radius = (double) parameters[1];
				proportionality = (Proportionality) parameters[2];

				fieldName = prettify(FieldAndOperation.TANGENTIAL);
				field.add(prettify(FieldProperty.PROPERTIES),
						Json.createObjectBuilder().add(prettify(FieldProperty.VALUE), value)
								.add(prettify(FieldProperty.ID), fieldID)
								.add(prettify(FieldProperty.RADIUS), radius)
								.add(prettify(FieldProperty.PROPORTIONALITY), prettify(proportionality)));
				break;
			case BORDER:
				value = (double) parameters[0];
				proportionality = (Proportionality) parameters[1];

				fieldName = prettify(FieldAndOperation.BORDER);
				field.add(prettify(FieldProperty.PROPERTIES),
						Json.createObjectBuilder().add(prettify(FieldProperty.VALUE), value)
								.add(prettify(FieldProperty.ID), fieldID)
								.add(prettify(FieldProperty.PROPORTIONALITY), prettify(proportionality)));
				break;
			case TRANSLATE:
				along_x = (double) parameters[0];
				along_y = (double) parameters[1];

				operations.add(Json.createObjectBuilder().add(prettify(FieldAndOperation.TRANSLATE),
						Json.createObjectBuilder().add(prettify(OperationProperty.ALONG_X), along_x)
								.add(prettify(OperationProperty.ALONG_Y), along_y)));
				break;
			case MASK:
				shape = (Shape) parameters[0];

				JsonObjectBuilder jobShape1 = Json.createObjectBuilder();
				ShapeProperty[] shapeProps1 = ((Shape) parameters[0]).getProperties();
				Object[] shapeValues1 = ((Shape) parameters[0]).getValues();

				for (int i = 0; i < shapeProps1.length; i++) {
					jobShape1.add(shapeProps1[i].toString().toLowerCase(), (double) shapeValues1[i]);
				}
				operations.add(Json.createObjectBuilder().add(prettify(FieldAndOperation.MASK),
						Json.createObjectBuilder().add(prettify(shape.getType()), jobShape1)));
				break;
			case ROTATE:
				angleInDegree = (double) parameters[0];
				rotationPoint = (Vector2D) parameters[1];

				operations.add(Json.createObjectBuilder().add(prettify(FieldAndOperation.ROTATE),
						Json.createObjectBuilder().add(prettify(OperationProperty.DEGREE), angleInDegree)
								.add(prettify(OperationProperty.ROTATION_POINT_X), rotationPoint.getX())
								.add(prettify(OperationProperty.ROTATION_POINT_Y), rotationPoint.getY())));
				break;
			case SCALE:
				factor = (double) parameters[0];

				operations.add(Json.createObjectBuilder().add(prettify(FieldAndOperation.SCALE),
						Json.createObjectBuilder().add(prettify(OperationProperty.FACTOR), factor)));
				break;
			case SHEAR:
				factor = (double) parameters[0];
				axis = (Axis) parameters[1];

				operations.add(Json.createObjectBuilder().add(prettify(FieldAndOperation.SHEAR),
						Json.createObjectBuilder().add(prettify(OperationProperty.FACTOR), factor)
								.add(prettify(OperationProperty.AXIS), prettify(axis))));
				break;
			case SUPERPOSITION:
				@SuppressWarnings("unchecked")
				List<Map<FieldAndOperation, Object[]>> superpostioningField = (List<Map<FieldAndOperation, Object[]>>) parameters[0];

				JsonObjectBuilder sup = getJSON(superpostioningField);
				operations.add(Json.createObjectBuilder().add(prettify(FieldAndOperation.SUPERPOSITION), sup));
				break;
			case SCALE_AREA:
				shape = (Shape) parameters[0];
				factor = (double) parameters[1];

				JsonObjectBuilder jobShape2 = Json.createObjectBuilder();
				ShapeProperty[] shapeProps2 = ((Shape) parameters[0]).getProperties();
				Object[] shapeValues2 = ((Shape) parameters[0]).getValues();

				for (int i = 0; i < shapeProps2.length; i++) {
					jobShape2.add(prettify(shapeProps2[i]), (double) shapeValues2[i]);
				}
				operations.add(Json.createObjectBuilder().add(FieldAndOperation.SCALE_AREA.toString().toLowerCase(),
						Json.createObjectBuilder().add(prettify(shape.getType()), jobShape2)
								.add(prettify(OperationProperty.FACTOR), factor)));
				break;
			default:
				System.err.println("FIELD OR OPERATION DOES NOT EXIST");
				break;
			}
		}

		return job.add(fieldName, field.add(prettify(OperationProperty.OPERATIONS), operations));
	}
	
	/**
	 * Get json representation of this field. Changed by Chang based on Ms. Knaster's version to record file name
	 * 
	 * @param input
	 *            internal representation of the vector field
	 * @param fileName
	 * 			  the file's Name
	 * @return json representation
	 */
	private JsonObjectBuilder getJSON(List<Map<FieldAndOperation, Object[]>> input, String fileName) {
		System.out.println(input);
		JsonObjectBuilder job = Json.createObjectBuilder();
		JsonObjectBuilder field = Json.createObjectBuilder();
		JsonArrayBuilder operations = Json.createArrayBuilder();
		
		String fieldName = "";

		Object[] parameters;
		Map<FieldAndOperation, Object[]> m;
		FieldAndOperation currentOperation;
		Iterator<Map<FieldAndOperation, Object[]>> it = input.iterator();
		while (it.hasNext()) {
			m = it.next();
			currentOperation = m.keySet().stream().findFirst().get();
			
			parameters = (Object[]) m.values().toArray()[0];
			// field parameters
			final String fieldID = ID.toString();
			String file_Name = fileName;
			double value;
			double radius;
			Proportionality proportionality;

			// operation parameters
			double angleInDegree;
			Vector2D rotationPoint;
			double factor;
			Axis axis;
			Shape shape;
			double along_x;
			double along_y;
			
			switch (currentOperation) {
			case ZERO:
				fieldName = prettify(FieldAndOperation.ZERO);
				
				field.add(prettify(FieldProperty.PROPERTIES),
						Json.createObjectBuilder().add(prettify(FieldProperty.NAME), file_Name)); //empty Properties
				break;
			case HOMOGENOUS:
				value = (double) parameters[0];

				fieldName = prettify(FieldAndOperation.HOMOGENOUS);
				field.add(prettify(FieldProperty.PROPERTIES),
						Json.createObjectBuilder()
							.add(prettify(FieldProperty.ID), fieldID)
							.add(prettify(FieldProperty.VALUE), value));
				break;
			case RADIAL:
				value = (double) parameters[0];
				radius = (double) parameters[1];
				proportionality = (Proportionality) parameters[2];

				fieldName = prettify(FieldAndOperation.RADIAL);
				field.add(prettify(FieldProperty.PROPERTIES),
						Json.createObjectBuilder().add(prettify(FieldProperty.VALUE), value)
								.add(prettify(FieldProperty.ID), fieldID)
								.add(prettify(FieldProperty.RADIUS), radius)
								.add(prettify(FieldProperty.PROPORTIONALITY), prettify(proportionality)));
				break;
			case TANGENTIAL:
				value = (double) parameters[0];
				radius = (double) parameters[1];
				proportionality = (Proportionality) parameters[2];

				fieldName = prettify(FieldAndOperation.TANGENTIAL);
				field.add(prettify(FieldProperty.PROPERTIES),
						Json.createObjectBuilder().add(prettify(FieldProperty.VALUE), value)
								.add(prettify(FieldProperty.ID), fieldID)
								.add(prettify(FieldProperty.RADIUS), radius)
								.add(prettify(FieldProperty.PROPORTIONALITY), prettify(proportionality)));
				break;
			case BORDER:
				value = (double) parameters[0];
				proportionality = (Proportionality) parameters[1];

				fieldName = prettify(FieldAndOperation.BORDER);
				field.add(prettify(FieldProperty.PROPERTIES),
						Json.createObjectBuilder().add(prettify(FieldProperty.VALUE), value)
								.add(prettify(FieldProperty.ID), fieldID)
								.add(prettify(FieldProperty.PROPORTIONALITY), prettify(proportionality)));
				break;
			case TRANSLATE:
				along_x = (double) parameters[0];
				along_y = (double) parameters[1];

				operations.add(Json.createObjectBuilder().add(prettify(FieldAndOperation.TRANSLATE),
						Json.createObjectBuilder().add(prettify(OperationProperty.ALONG_X), along_x)
								.add(prettify(OperationProperty.ALONG_Y), along_y)));
				break;
			case MASK:
				shape = (Shape) parameters[0];

				JsonObjectBuilder jobShape1 = Json.createObjectBuilder();
				ShapeProperty[] shapeProps1 = ((Shape) parameters[0]).getProperties();
				Object[] shapeValues1 = ((Shape) parameters[0]).getValues();

				for (int i = 0; i < shapeProps1.length; i++) {
					jobShape1.add(shapeProps1[i].toString().toLowerCase(), (double) shapeValues1[i]);
				}
				operations.add(Json.createObjectBuilder().add(prettify(FieldAndOperation.MASK),
						Json.createObjectBuilder().add(prettify(shape.getType()), jobShape1)));
				break;
			case ROTATE:
				angleInDegree = (double) parameters[0];
				rotationPoint = (Vector2D) parameters[1];

				operations.add(Json.createObjectBuilder().add(prettify(FieldAndOperation.ROTATE),
						Json.createObjectBuilder().add(prettify(OperationProperty.DEGREE), angleInDegree)
								.add(prettify(OperationProperty.ROTATION_POINT_X), rotationPoint.getX())
								.add(prettify(OperationProperty.ROTATION_POINT_Y), rotationPoint.getY())));
				break;
			case SCALE:
				factor = (double) parameters[0];

				operations.add(Json.createObjectBuilder().add(prettify(FieldAndOperation.SCALE),
						Json.createObjectBuilder().add(prettify(OperationProperty.FACTOR), factor)));
				break;
			case SHEAR:
				factor = (double) parameters[0];
				axis = (Axis) parameters[1];

				operations.add(Json.createObjectBuilder().add(prettify(FieldAndOperation.SHEAR),
						Json.createObjectBuilder().add(prettify(OperationProperty.FACTOR), factor)
								.add(prettify(OperationProperty.AXIS), prettify(axis))));
				break;
			case SUPERPOSITION:
				@SuppressWarnings("unchecked")
				List<Map<FieldAndOperation, Object[]>> superpostioningField = (List<Map<FieldAndOperation, Object[]>>) parameters[0];

				JsonObjectBuilder sup = getJSON(superpostioningField);
				operations.add(Json.createObjectBuilder().add(prettify(FieldAndOperation.SUPERPOSITION), sup));
				break;
			case SCALE_AREA:
				shape = (Shape) parameters[0];
				factor = (double) parameters[1];

				JsonObjectBuilder jobShape2 = Json.createObjectBuilder();
				ShapeProperty[] shapeProps2 = ((Shape) parameters[0]).getProperties();
				Object[] shapeValues2 = ((Shape) parameters[0]).getValues();

				for (int i = 0; i < shapeProps2.length; i++) {
					jobShape2.add(prettify(shapeProps2[i]), (double) shapeValues2[i]);
				}
				operations.add(Json.createObjectBuilder().add(FieldAndOperation.SCALE_AREA.toString().toLowerCase(),
						Json.createObjectBuilder().add(prettify(shape.getType()), jobShape2)
								.add(prettify(OperationProperty.FACTOR), factor)));
				break;
			default:
				System.err.println("FIELD OR OPERATION DOES NOT EXIST");
				break;
			}
		}

		return job.add(fieldName, field.add(prettify(OperationProperty.OPERATIONS), operations));
	}

	/**
	 * Save field as json file.
	 */
	public void save() {
		save(String.valueOf(System.currentTimeMillis()));
	}

	/**
	 * Save field as json file under specified file name.
	 * 
	 * @param fileName
	 *            name of json file
	 */
	//changed by Chang to fit file chooser in Font-end
	public void save(String fileName) {
		JsonObject object = getJSON(fields, fileName).build();
		OutputStream out;
		try {
			if (savingPath == null) {
				System.out.println("Saving path has not been set. JSON-File will be saved here: ./json/");
				out = new FileOutputStream("./json/" + fileName);
			} else {
				out = new FileOutputStream(savingPath + "/" + fileName);
			}
			Map<String, Boolean> config = new HashMap<String, Boolean>();
			config.put(JsonGenerator.PRETTY_PRINTING, true);
			JsonWriterFactory factory = Json.createWriterFactory(config);
			JsonWriter writer = factory.createWriter(out);
			writer.writeObject(object);
			writer.close();
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		}
	}
	

	/**
	 * Export field as csv file at specified points.
	 * 
	 * @param intervalXaxis
	 *            evaluation interval on x axis
	 * @param samplingDistanceX
	 *            mesh size of the grid on x axis
	 * @param intervalYaxis
	 *            evaluation interval on y axis
	 * @param samplingDistanceY
	 *            mesh size of the grid on y axis
	 */
	public void export(Vector2D intervalXaxis, double samplingDistanceX, Vector2D intervalYaxis,double samplingDistanceY) {
		String fileName = String.valueOf(System.currentTimeMillis());
		export(intervalXaxis, samplingDistanceX, intervalYaxis, samplingDistanceY, fileName);
	}

	/**
	 * Export field as csv file at specified points.
	 * 
	 * @param intervalXaxis
	 *            evaluation interval on x axis
	 * @param intervalYaxis
	 *            evaluation interval on y axis
	 * @param samplingDistanceX
	 *            x size of the grid
	 * @param samplingDistanceY
	 *            y size of the grid
	 * @param fileName
	 *            name of csv file
	 */
	public void export(Vector2D intervalXaxis, double samplingDistanceX, Vector2D intervalYaxis, double samplingDistanceY, String fileName) {
		Vector2D output = null;
		double startX = intervalXaxis.getX();
		double endX = intervalXaxis.getY();
		double startY = intervalYaxis.getX();
		double endY = intervalYaxis.getY();

		FileWriter fileWriter = null;

		try {
			if (exportPath == null) {
				System.out.println("Export path has not been set. CSV-File will be saved here: ./csv/");
				fileWriter = new FileWriter("./csv/" + fileName + ".csv");
			} else {
				fileWriter = new FileWriter(exportPath + "/" + fileName + ".csv");
			}

			fileWriter.append(intervalXaxis.getX() + "," + intervalXaxis.getY() + "," + samplingDistanceX + ","
					+ intervalYaxis.getX() + "," + intervalYaxis.getY() + "," + samplingDistanceY + "\n");
			for (double x = startX; x <= endX; x += samplingDistanceX) {
				for (double y = startY; y <= endY; y += samplingDistanceY) {
					output = getFunctionValue(x, y);
					fileWriter.append(output.getX() + "," + output.getY());
					if (x < endX || y < endY) {
						fileWriter.append("\n");
					}
				}
			}
			fileWriter.flush();
			fileWriter.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	/**
	 * Export field as csv file at specified points using the light representation.
	 * 
	 * @param intervalXaxis
	 *            evaluation interval on x axis
	 * @param intervalYaxis
	 *            evaluation interval on y axis
	 * @param samplingDistanceX
	 *            x size of the grid
	 * @param samplingDistanceY
	 * 			  y size of the grid
	 * @param fileName
	 *            name of csv file
	 */
	public void exportUsingOpt(Vector2D intervalXaxis, double samplingDistanceX, Vector2D intervalYaxis, double samplingDistanceY, String fileName) {
		Vector2D output = null;
		double startX = intervalXaxis.getX();
		double endX = intervalXaxis.getY();
		double startY = intervalYaxis.getX();
		double endY = intervalYaxis.getY();

		FileWriter fileWriter = null;

		try {
			if (exportPath == null) {
				System.out.println("Export path has not been set. CSV-File will be saved here: ./csv/");
				fileWriter = new FileWriter("./csv/" + fileName + ".csv");
			} else {
				fileWriter = new FileWriter(exportPath + "/" + fileName + ".csv");
			}

			fileWriter.append(intervalXaxis.getX() + "," + intervalXaxis.getY() + "," + samplingDistanceX + ","
					+ intervalYaxis.getX() + "," + intervalYaxis.getY() + "," + samplingDistanceY + "\n");
			for (double x = startX; x <= endX; x += samplingDistanceX) {
				for (double y = startY; y <= endY; y += samplingDistanceY) {
					output = getFunctionValueOptimized(x, y);
					fileWriter.append(output.getX() + "," + output.getY());
					if (x < endX || y < endY) {
						fileWriter.append("\n");
					}
				}
			}
			fileWriter.flush();
			fileWriter.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	/**
	 * Set path to csv file.
	 * 
	 * @param path
	 *            exporting path
	 */
	public static void setExportPath(String path) {
		exportPath = path;
	}

	/**
	 * Set path to save json file.
	 * 
	 * @param path
	 *            savin path
	 */
	public static void setSavingPath(String path) {
		savingPath = path;
	}

	// reading in
	// JSON-----------------------------------------------------------------------------------------------------------

	/**
	 * Reading in a json file that represents a field.
	 * 
	 * @param file
	 *            file path and field
	 * @return a vectorfield specified by the json field
	 */
	public static VectorField readInJSON(String file) {
		InputStream fis = null;
		try {
			fis = new FileInputStream(file);
		} catch (FileNotFoundException e) {
			System.err.println("Check for misspelled name, missing or extra quotation marks!");
			e.printStackTrace();
		}
		
		JsonReader reader = Json.createReader(fis);
		JsonObject fieldObject = reader.readObject();
		reader.close();
		VectorField field = readInJson(fieldObject);
		return field;
	}

	/**
	 * Reading in a json file that represents a field recursively.
	 * 
	 * @param fieldObject
	 *            the vector field
	 * @return the read in vector field
	 */
	private static VectorField readInJson(JsonObject fieldObject) {
		
		String fieldType = fieldObject.keySet().toArray()[0].toString();
		VectorField field = null;
		JsonObject fieldProps = fieldObject.getJsonObject(fieldType).getJsonObject(prettify(FieldProperty.PROPERTIES));
		UUID fieldID;
		if (fieldProps.containsKey(prettify(FieldProperty.ID))) {
			try {
				fieldID = UUID.fromString(trimJsonValue(fieldProps.get(prettify(FieldProperty.ID)).toString()));
			}catch (java.lang.IllegalArgumentException e) {
				System.err.println("The ID of the JSON file seems to be corrupt. Generated random ID.");
				fieldID = UUID.randomUUID();
			}
		} else {
			fieldID = UUID.randomUUID();
		}
		if (fieldType.equalsIgnoreCase(FieldAndOperation.ZERO.toString())) {
			field = new SuperposedField(fieldID);
			try {
				parent= fieldProps.get(prettify(FieldProperty.NAME)).toString();
				//Chang modified	
				Data data = new Data(fieldID,field,null,null,null,null,null,null,null,null,parent);
				//enlist the data
				dataStorage.add(data);	
			} catch (Exception e) {
				// TODO: handle exception
			}
		} else if (fieldType.equalsIgnoreCase(FieldAndOperation.HOMOGENOUS.toString())) {
			double value = Double.valueOf(fieldProps.get(prettify(FieldProperty.VALUE)).toString());
			field = new HomogenousField(fieldID, value);
			//Chang modified	
			Data data = new Data(fieldID,field,null,null,null,null,null,null,null,null,"Homogeneous Vector Field_");
			data.setTextField1(new TextField(Double.toString(value)));
			data.setParent(parent);
			//enlist the data
			dataStorage.add(data);
			
		} else if (fieldType.equalsIgnoreCase(FieldAndOperation.RADIAL.toString())) {
			double value = Double.valueOf(fieldProps.get(prettify(FieldProperty.VALUE)).toString());
			double radius = Double.valueOf(fieldProps.get(prettify(FieldProperty.RADIUS)).toString());
			Proportionality proportionality = Proportionality.valueOf(
					trimJsonValue(fieldProps.get(prettify(FieldProperty.PROPORTIONALITY)).toString().toUpperCase()));
			field = new RadialField(fieldID, value, radius, proportionality);
			//Chang modified
			Data data = new Data(fieldID,field,null,null,null,null,null,null,null,null,"Radical Vector Field_");
			data.setTextField1(new TextField(Double.toString(value)));
			data.setTextField2(new TextField(Double.toString(radius)));
			data.setParent(parent);
			//enlist the data
			dataStorage.add(data);
			
		} else if (fieldType.equalsIgnoreCase(FieldAndOperation.TANGENTIAL.toString())) {
			double value = Double.valueOf(fieldProps.get(prettify(FieldProperty.VALUE)).toString());
			double radius = Double.valueOf(fieldProps.get(prettify(FieldProperty.RADIUS)).toString());
			Proportionality proportionality = Proportionality.valueOf(
					trimJsonValue(fieldProps.get(prettify(FieldProperty.PROPORTIONALITY)).toString().toUpperCase()));
			field = new TangentialField(fieldID, value, radius, proportionality);
			//Chang modified
			Data data = new Data(fieldID,field,null,null,null,null,null,null,null,null,"Tangential Vector Field_");
			data.setTextField1(new TextField(Double.toString(value)));
			data.setTextField2(new TextField(Double.toString(radius)));
			data.setParent(parent);
			//enlist the data
			dataStorage.add(data);
			
		} else if (fieldType.equalsIgnoreCase(FieldAndOperation.BORDER.toString())) {
			double value = Double.valueOf(fieldProps.get(prettify(FieldProperty.VALUE)).toString());
			Proportionality proportionality = Proportionality.valueOf(
					trimJsonValue(fieldProps.get(prettify(FieldProperty.PROPORTIONALITY)).toString().toUpperCase()));
			field = new BorderField(fieldID, value, proportionality);
			//Chang modified
			Data data = new Data(fieldID,field,null,null,null,null,null,null,null,null,"Boundary Vector Field_");
			data.setTextField1(new TextField(Double.toString(value)));
			data.setParent(parent);
			//enlist the data
			dataStorage.add(data);
			
		} else {
			System.err.println("FIELD \"" + fieldType + "\" DOES NOT EXIST");
		}

		JsonArray operations = fieldObject.getJsonObject(fieldType)
				.getJsonArray(prettify(OperationProperty.OPERATIONS));
		JsonObject operation = null;
		String opName = "";
		JsonObject operationProp;

		for (int i = 0; i < operations.size(); i++) {
			operation = operations.getJsonObject(i);
			opName = operation.keySet().toArray()[0].toString();

			if (opName.equalsIgnoreCase(FieldAndOperation.SCALE.toString())) {
				operationProp = operation.getJsonObject(prettify(FieldAndOperation.SCALE));
				double factor = Double.valueOf(operationProp.get(prettify(OperationProperty.FACTOR)).toString());
				field.scale(factor);
				//Chang modified
				for(Data data: dataStorage) {
					if(field == data.getResult()) {
						
						data.setScaleFactor(new TextField(Double.toString(factor)));
					}
				}
			} else if (opName.equalsIgnoreCase(FieldAndOperation.ROTATE.toString())) {
				operationProp = operation.getJsonObject(prettify(FieldAndOperation.ROTATE));
				double degree = Double.valueOf(operationProp.get(prettify(OperationProperty.DEGREE)).toString());
				double rotation_point_x = Double
						.valueOf(operationProp.get(prettify(OperationProperty.ROTATION_POINT_X)).toString());
				double rotation_point_y = Double
						.valueOf(operationProp.get(prettify(OperationProperty.ROTATION_POINT_Y)).toString());
				field.rotate(degree, new Vector2D(rotation_point_x, rotation_point_y));
				//Chang modified
				//lack of rotation point x,y
				for(Data data: dataStorage) {
					if(field == data.getResult()) {
						
						data.setDegree(new TextField(Double.toString(degree)));
					}
				}
			} else if (opName.equalsIgnoreCase(FieldAndOperation.TRANSLATE.toString())) {
				operationProp = operation.getJsonObject(prettify(FieldAndOperation.TRANSLATE));
				double along_axis_x = Double.valueOf(operationProp.get(prettify(OperationProperty.ALONG_X)).toString());
				double along_axis_y = Double.valueOf(operationProp.get(prettify(OperationProperty.ALONG_Y)).toString());
				field.translate(along_axis_x, along_axis_y);
				//Chang modified
				for(Data data: dataStorage) {
					if(field == data.getResult()) {
						
						data.setAlongX(new TextField(Double.toString(along_axis_x)));
						data.setAlongY(new TextField(Double.toString(along_axis_y)));
					}
				}
			} else if (opName.equalsIgnoreCase(FieldAndOperation.MASK.toString())) {
				operationProp = operation.getJsonObject(prettify(FieldAndOperation.MASK));
				field = readInShape(field, operationProp, false);
			} else if (opName.equalsIgnoreCase(FieldAndOperation.SUPERPOSITION.toString())) {
				operationProp = operation.getJsonObject(prettify(FieldAndOperation.SUPERPOSITION));
				VectorField superField = readInJson(operationProp);
				field.superposition(superField);
			} else if (opName.equalsIgnoreCase(FieldAndOperation.SCALE_AREA.toString())) {
				operationProp = operation.getJsonObject(prettify(FieldAndOperation.SCALE_AREA));
				field = readInShape(field, operationProp, true);
			} else if (opName.equalsIgnoreCase(FieldAndOperation.SHEAR.toString())) {
				operationProp = operation.getJsonObject(prettify(FieldAndOperation.SHEAR));
				double factor = Double.valueOf(operationProp.get(prettify(OperationProperty.FACTOR)).toString());
				Axis axis = Axis.valueOf(
						trimJsonValue(operationProp.get(prettify(OperationProperty.AXIS)).toString().toUpperCase()));
				field.shear(factor, axis);
				//Chang modified
				for(Data data: dataStorage) {
					if(field == data.getResult()) {
						
						data.setShearFactor(new TextField(Double.toString(factor)));
						data.setAxisToShear(new TextField(axis.toString()));
					}
				}
			} else {
				System.err.println("OPERATION \"" + opName + "\" DOES NOT EXIST");
			}
		}

		return field;

	}

	/**
	 * Read in the json properties that represent a shape
	 * 
	 * @param field
	 *            field for the internal representation
	 * @param operationProp
	 *            properties that contain information about the shape
	 * @param getFactor
	 *            helps to differentitate whether the operation is "mask" or
	 *            "scaleArea"
	 * @return vector field
	 */
	private static VectorField readInShape(VectorField field, JsonObject operationProp, boolean getFactor) {
		String shapeName = operationProp.keySet().toArray()[0].toString();
		JsonObject shapeObject = null;

		if (shapeName.equalsIgnoreCase(ShapeEnum.RECTANGLE.toString())) {
			shapeObject = operationProp.getJsonObject(prettify(ShapeEnum.RECTANGLE));
			double size_x = Double.valueOf(shapeObject.get(prettify(ShapeProperty.SIZE_X)).toString());
			double size_y = Double.valueOf(shapeObject.get(prettify(ShapeProperty.SIZE_Y)).toString());
			double center_x = Double.valueOf(shapeObject.get(prettify(ShapeProperty.CENTER_X)).toString());
			double center_y = Double.valueOf(shapeObject.get(prettify(ShapeProperty.CENTER_Y)).toString());
			Rectangle rectangle = new Rectangle(size_x, size_y, new Vector2D(center_x, center_y));
			if (getFactor) {
				double factor = Double.valueOf(operationProp.get(prettify(OperationProperty.FACTOR)).toString());
				field.scaleArea(rectangle, factor);
			} else {
				field.mask(rectangle);
			}
		} else if (shapeName.equalsIgnoreCase(ShapeEnum.SQUARE.toString())) {
			shapeObject = operationProp.getJsonObject(prettify(ShapeEnum.SQUARE));
			double size_x = Double.valueOf(shapeObject.get(prettify(ShapeProperty.SIZE_X)).toString());
			double center_x = Double.valueOf(shapeObject.get(prettify(ShapeProperty.CENTER_X)).toString());
			double center_y = Double.valueOf(shapeObject.get(prettify(ShapeProperty.CENTER_Y)).toString());
			Square square = new Square(size_x, new Vector2D(center_x, center_y));
			if (getFactor) {
				double factor = Double.valueOf(operationProp.get(prettify(OperationProperty.FACTOR)).toString());
				field.scaleArea(square, factor);
			} else {
				field.mask(square);
			}
		} else if (shapeName.equalsIgnoreCase(ShapeEnum.ELLIPSE.toString())) {
			shapeObject = operationProp.getJsonObject(prettify(ShapeEnum.ELLIPSE));
			double radius_x = Double.valueOf(shapeObject.get(prettify(ShapeProperty.RADIUS_X)).toString());
			double radius_y = Double.valueOf(shapeObject.get(prettify(ShapeProperty.RADIUS_Y)).toString());
			double center_x = Double.valueOf(shapeObject.get(prettify(ShapeProperty.CENTER_X)).toString());
			double center_y = Double.valueOf(shapeObject.get(prettify(ShapeProperty.CENTER_Y)).toString());
			Ellipse ellipse = new Ellipse(radius_x, radius_y, new Vector2D(center_x, center_y));
			if (getFactor) {
				double factor = Double.valueOf(operationProp.get(prettify(OperationProperty.FACTOR)).toString());
				field.scaleArea(ellipse, factor);
			} else {
				field.mask(ellipse);
			}
		} else if (shapeName.equalsIgnoreCase(ShapeEnum.CIRCLE.toString())) {
			shapeObject = operationProp.getJsonObject(prettify(ShapeEnum.CIRCLE));
			double radius_x = Double.valueOf(shapeObject.get(prettify(ShapeProperty.RADIUS_X)).toString());
			double center_x = Double.valueOf(shapeObject.get(prettify(ShapeProperty.CENTER_X)).toString());
			double center_y = Double.valueOf(shapeObject.get(prettify(ShapeProperty.CENTER_Y)).toString());
			Circle circle = new Circle(radius_x, new Vector2D(center_x, center_y));
			if (getFactor) {
				double factor = Double.valueOf(operationProp.get(prettify(OperationProperty.FACTOR)).toString());
				field.scaleArea(circle, factor);
			} else {
				field.mask(circle);
			}
		} else if (shapeName.equalsIgnoreCase(ShapeEnum.VERTICAL_TUNNEL.toString())) {
			shapeObject = operationProp.getJsonObject(prettify(ShapeEnum.VERTICAL_TUNNEL));
			double width = Double.valueOf(shapeObject.get(prettify(ShapeProperty.WIDTH)).toString());
			double center_x = Double.valueOf(shapeObject.get(prettify(ShapeProperty.CENTER_X)).toString());
			VerticalTunnel VerticalTunnel = new VerticalTunnel(width, center_x);
			if (getFactor) {
				double factor = Double.valueOf(operationProp.get(prettify(OperationProperty.FACTOR)).toString());
				field.scaleArea(VerticalTunnel, factor);
			} else {
				field.mask(VerticalTunnel);
			}
		} else {
			System.err.println("SHAPE \"" + shapeName + "\" DOES NOT EXIST");
		}
		return field;
	}

	/**
	 * Remove quotation marks from json value ("1.0" -> 1.0)
	 * 
	 * @param value
	 *            json value
	 * @return trimmed string representation of json value
	 */
	private static String trimJsonValue(String value) {
		return value.substring(1, value.length() - 1);
	}
	
}
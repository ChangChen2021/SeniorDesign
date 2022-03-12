package model;

import java.util.UUID;

import javafx.scene.control.TextField;
import main.fields.VectorField;

public class Data {
	private UUID title;
	private VectorField result;
	private TextField textField1 = null;
	private TextField textField2 = null;
	private TextField degree;
	private TextField alongX;
	private TextField alongY;
	private TextField scaleFactor;
	private TextField shearFactor;
	private TextField axisToShear;
	private String type;
	private String parent = "Result";
	private VectorField origin;


	public VectorField getOrigin() {
		return origin;
	}


	public void setOrigin(VectorField origin) {
		this.origin = origin;
	}


	public String getParent() {
		return parent;
	}


	public void setParent(String parent) {
		this.parent = parent;
	}


	public Data(UUID title, VectorField result, TextField textField1, TextField textField2, TextField degree,
			TextField alongX, TextField alongY, TextField scaleFactor, TextField shearFactor, TextField axisToShear, String type) {
		super();
		this.title = title;
		this.result = result;
		this.textField1 = textField1;
		this.textField2 = textField2;
		this.degree = degree;
		this.alongX = alongX;
		this.alongY = alongY;
		this.scaleFactor = scaleFactor;
		this.shearFactor = shearFactor;
		this.axisToShear = axisToShear;
		this.type=type;
	}


	public String getType() {
		return type;
	}


	public void setType(String type) {
		this.type = type;
	}


	public VectorField getResult() {
		return result;
	}

	public void setResult(VectorField result) {
		this.result = result;
	}

	public TextField getTextField1() {
		return textField1;
	}

	public void setTextField1(TextField textField1) {
		this.textField1 = textField1;
	}

	public TextField getTextField2() {
		return textField2;
	}

	public void setTextField2(TextField textField2) {
		this.textField2 = textField2;
	}

	public TextField getDegree() {
		return degree;
	}

	public void setDegree(TextField degree) {
		this.degree = degree;
	}

	public TextField getAlongX() {
		return alongX;
	}

	public void setAlongX(TextField alongX) {
		this.alongX = alongX;
	}

	public TextField getAlongY() {
		return alongY;
	}

	public void setAlongY(TextField alongY) {
		this.alongY = alongY;
	}

	public TextField getScaleFactor() {
		return scaleFactor;
	}

	public void setScaleFactor(TextField scaleFactor) {
		this.scaleFactor = scaleFactor;
	}

	public TextField getShearFactor() {
		return shearFactor;
	}

	public void setShearFactor(TextField shearFactor) {
		this.shearFactor = shearFactor;
	}

	public TextField getAxisToShear() {
		return axisToShear;
	}

	public void setAxisToShear(TextField axisToShear) {
		this.axisToShear = axisToShear;
	}

	public UUID getTitle() {
		return title;
	}

	public void setTitle(UUID title) {
		this.title = title;
	}

}

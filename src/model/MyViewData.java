package model;

import java.util.List;

import org.apache.commons.math3.geometry.euclidean.twod.Vector2D;

import main.fields.VectorField;

public class MyViewData {
	public VectorField resultVectorField;
	public List<Data> dataStorage;
	public String vectorfield;
	public VectorField supVectorField;
	public Vector2D output;
	public VectorField resultField;
	public double startX;
	public double endX;
	public double interval;
	public double startY;
	public double endY;
	public double tileSize;
	public double magnitude;
	public double originX;
	public double originY;
	public int axisLength;

	public MyViewData(VectorField supVectorField, double startX, double endX, double interval, double startY,
			double endY, double tileSize, double magnitude, double originX, double originY, int axisLength) {
		this.supVectorField = supVectorField;
		this.startX = startX;
		this.endX = endX;
		this.interval = interval;
		this.startY = startY;
		this.endY = endY;
		this.tileSize = tileSize;
		this.magnitude = magnitude;
		this.originX = originX;
		this.originY = originY;
		this.axisLength = axisLength;
	}
}
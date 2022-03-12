package test;

import org.apache.commons.math3.geometry.euclidean.twod.Vector2D;

import main.fields.BorderField;
import main.fields.HomogenousField;
import main.fields.RadialField;
import main.fields.VectorField;
import main.util.Proportionality;
import main.util.shape.Circle;
import main.util.shape.Rectangle;

public class Test_Segelumgebung2 {
	public static void main(String[] args) {
		VectorField.setSavingPath("./testResults/Test_Segelumgebung2");
		VectorField.setExportPath("./testResults/Test_Segelumgebung2");
		
		double gridWidth = 0.5;
		
		HomogenousField hom = new HomogenousField(1.0);
		BorderField b = new BorderField(10.0, Proportionality.INVERSELY_EXPONENTIAL);
		b.translate(-11, 0);
		hom.superposition(b);
		b.rotate(180);
		hom.superposition(b);

		
		hom.export(new Vector2D(-10,10), gridWidth, new Vector2D(-10,10), gridWidth, "ufer");
		
		RadialField rad = new RadialField(10.0,1.0,Proportionality.INVERSELY_EXPONENTIAL);
		rad.translate(0, -5);
		hom.superposition(rad);
		rad.translate(0, 10);
		hom.superposition(rad); 
		hom.scaleArea(new Circle(1.0, new Vector2D(0,-5)), 0);
		hom.scaleArea(new Circle(1.0, new Vector2D(0,5)), 0);
		hom.export(new Vector2D(-10,10), gridWidth, new Vector2D(-10,10), gridWidth, "hindernisse");
		
		HomogenousField hom2 = new HomogenousField(0);
		HomogenousField hom3 = new HomogenousField(0.5);
		hom3.mask(new Rectangle(4,5, new Vector2D(6,-7.5)));
		hom2.superposition(hom3);
		hom3.translate(-12, 15);
		hom2.superposition(hom3);
		
		HomogenousField hom4 = new HomogenousField(0.5);
		hom4.mask(new Rectangle(4,10, new Vector2D(6,0)));
		hom4.shear(-1.2);
		hom4.translate(-5.5, 0);
		hom2.superposition(hom4);
		hom2.export(new Vector2D(-10,10), gridWidth, new Vector2D(-10,10), gridWidth, "kurs");
		
		hom.superposition(hom2);
		hom.export(new Vector2D(-10,10), gridWidth, new Vector2D(-10,10), gridWidth, "done2");
		
		hom.save("segelumgebung2");
	}
}

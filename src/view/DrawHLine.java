/**
 * @author Kejin
 * function: Maybe this class is just used to draw a x-axis	 
 * 
 */
 
package view;
 
import javafx.scene.paint.Color;
import javafx.scene.shape.Line;
 
public class DrawHLine extends Line {
 
	double startX;
	double startY;
	double endX;
	double endY;
	
	public DrawHLine() {
		// TODO Auto-generated constructor stub
	}
 
	public DrawHLine(double centerX,double centerY ,double lineLength) {
		super(centerX-lineLength/2, centerY, centerX+lineLength/2, centerY);
		// TODO Auto-generated constructor stub
		
		getLinePara(centerX, centerY, lineLength);
		selfDefine();	
	}
	
	public void getLinePara(double x,double y,double lineLength) {
		startX = x-lineLength/2;
		endX = x+lineLength/2;
		startY = y;
		endY = y;
	}
 
	public void selfDefine() { 
		setStroke(Color.RED);
		getStrokeDashArray().setAll(); 
	}
}
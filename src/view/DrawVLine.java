/**
 * @author Kejin
 * function: Maybe this class is just used to draw a y-axis	 
 * 
 */
 
package view;
 
import javafx.scene.paint.Color;
import javafx.scene.shape.Line;
 
public class DrawVLine extends Line {
 
	double startX;
	double startY;
	double endX;
	double endY; 
	
	public DrawVLine() {
		// TODO Auto-generated constructor stub
	}
 
	public DrawVLine(double centerX,double centerY ,double lineLength) {
		super(centerX, centerY-lineLength/2, centerX, centerY+lineLength/2);
		// TODO Auto-generated constructor stub
		getLinePara(centerX, centerY, lineLength);
		
		selfDefine();  
	}
	
	public void getLinePara(double x,double y,double lineLength) {
		startX = x ;
		endX = x ;
		startY = y-lineLength/2;
		endY = y+lineLength/2;
	}
 
	public void selfDefine() { 
		setStroke(Color.RED);
		getStrokeDashArray().setAll(); 
	} 
}
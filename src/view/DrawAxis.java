/**
 * @author Kejin 
 * function: draw a coordinate system 
 * 
 * PS:	how to call it?
 * 		DrawAxis axis=new DrawAxis(150, 100, 100);
 *		root.getChildren().addAll(
 *	        			axis.getxAxis(),
 *		        		axis.getyAxis(),
 *		        		axis.getxAxisTip(),
 *		        		axis.getyAxisTip());
 * 
 */ 
 
package view;
 
import javafx.scene.layout.StackPane;
import javafx.scene.paint.Color;
import javafx.scene.shape.Polygon;
import javafx.scene.shape.Shape; 
 
public class DrawAxis extends StackPane{
	private static int PRE_CENTER_X=80;
	private static int PRE_CENTER_Y=80;
	private static int PRE_AXIS_LENGTH=40;
	 
	private int centerX;
	private int centerY;
	private int axisLength; 
	
	//init axis Tip
	double  xPoint[] = new double [6];
	double  yPoint[] = new double [6];  
	
	/**
	 * arrow in the coordinate
	 */
	private Polygon xAxisTip; 
	private Polygon yAxisTip;
	/**
	 * axes
	 */
	private DrawHLine xAxis;
	private DrawVLine yAxis; 
	
	/**
     * Creates an empty instance of DrawAxis
     */
    public DrawAxis() {
		// TODO Auto-generated constructor stub
    	//the parameters is undefined,so set default values;
    	centerX = 40;
    	centerY = 40;
    	axisLength = 20;  
		xAxis = new DrawHLine(centerX, centerY, axisLength);
		yAxis = new DrawVLine(centerX, centerY, axisLength);
		initPara(centerX, centerY, axisLength);
	} 
 
    /**
     * Creates a new instance of Polygon.
     * @param (centerX , centerY) is the coordinate of Axis;
     * @param axisLenth is the length of axis; 
     */ 
	public DrawAxis(int centerX , int centerY , int axisLength){
		 
		xAxis = new DrawHLine(centerX, centerY, axisLength);
		yAxis = new DrawVLine(centerX, centerY, axisLength);
		initPara(centerX, centerY, axisLength);
	} 
 
	public void selfDefine() { 
		 
//		getStrokeDashArray().setAll(); 
	}
	
	public void initPara(int x,int y,int length) {
		this.centerX=x;
		this.centerY=y;
		this.axisLength=length;
		
		//init axis Tip 
		xPoint[0] = centerX+axisLength/2+10;
		xPoint[1] = centerY;
		xPoint[2] = centerX+axisLength/2-1;
		xPoint[3] = centerY+4;
		xPoint[4] = centerX+axisLength/2-1;
		xPoint[5] = centerY-4;
		 
		yPoint[0] = centerX;
		yPoint[1] = centerY+10+axisLength/2;
		yPoint[2] = centerX-4;
		yPoint[3] = centerY+axisLength/2-1;
		yPoint[4] = centerX+4;
		yPoint[5] = centerY+axisLength/2-1; 

		xAxisTip = new Polygon(xPoint);
		yAxisTip = new Polygon(yPoint);
		xAxisTip.setFill(Color.RED);
		yAxisTip.setFill(Color.RED);
		
	} 
	
	/**
	 * 
	 * @return xAxisTip
	 */
	public Polygon getxAxisTip() {
		return xAxisTip;
	}
	
	/**
	 * 
	 * @return yAxisTip 
	 */
	public Polygon getyAxisTip() {
		return yAxisTip;
	}
	
	/**
	 * 
	 * @return yAxisTip 
	 */
	public DrawHLine getxAxis() {
		return xAxis;
	}
 
	/**
	 * 
	 * @return yAxisTip 
	 */
	public DrawVLine getyAxis() {
		return yAxis;
	}
 
}
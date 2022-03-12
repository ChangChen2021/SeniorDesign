package model;

import javafx.geometry.Pos;
import javafx.scene.layout.StackPane;
import javafx.scene.paint.Color;
import javafx.scene.shape.LineTo;
import javafx.scene.shape.MoveTo;
import javafx.scene.shape.Path;
import javafx.scene.shape.Rectangle;
import view.MyView;

public class Tile extends StackPane{
	private MyView myView;
	public Tile(double d, double e, MyView myView) {
		this.myView = myView;
		Arrow vector = new Arrow(0,0,d,e);
		Rectangle border = new Rectangle(myView.model.tileSize,myView.model.tileSize);
		//color of tile
		border.setFill(null);
		//color of the edge of tile
		border.setStroke(null);
//		switch (i%4) {
//		case 1:
//			border.setStroke(Color.BLUE);
//			break;
//		case 2:
//			border.setStroke(Color.YELLOW);
//			break;
//		case 3:
//			border.setStroke(Color.GREEN);
//			break;
//		case 4: 
//			border.setStroke(Color.RED);
//			break;
//		}
		//the start of arrow
		setAlignment(Pos.BOTTOM_LEFT);
		getChildren().addAll(border, vector);
	}
	
}
/**
 * https://gist.github.com/kn0412/2086581e98a32c8dfa1f69772f14bca4
 * @author kn
 */
class Arrow extends Path{
    private static final double defaultArrowHeadSize = 	5.0;

    public Arrow(double startX, double startY, double endX, double endY, double arrowHeadSize){
        super();
        strokeProperty().bind(fillProperty());
        setFill(Color.BLACK);
        
        //Line
        getElements().add(new MoveTo(startX, startY));
        getElements().add(new LineTo(endX, endY));
        
        //ArrowHead
        double angle = Math.atan2((endY - startY), (endX - startX)) - Math.PI / 2.0;
        double sin = Math.sin(angle);
        double cos = Math.cos(angle);
        //point1
        double x1 = (- 1.0 / 2.0 * cos + Math.sqrt(3) / 2 * sin) * arrowHeadSize + endX;
        double y1 = (- 1.0 / 2.0 * sin - Math.sqrt(3) / 2 * cos) * arrowHeadSize + endY;
        //point2
        double x2 = (1.0 / 2.0 * cos + Math.sqrt(3) / 2 * sin) * arrowHeadSize + endX;
        double y2 = (1.0 / 2.0 * sin - Math.sqrt(3) / 2 * cos) * arrowHeadSize + endY;
        
        getElements().add(new LineTo(x1, y1));
        getElements().add(new LineTo(x2, y2));
        getElements().add(new LineTo(endX, endY));
    }
    
    public Arrow(double startX, double startY, double endX, double endY){
        this(startX, startY, endX, endY, defaultArrowHeadSize);
    }
}

package view;

import java.util.ArrayList;

import controller.MyController;
import javafx.scene.control.Label;
import javafx.scene.control.Menu;
import javafx.scene.control.MenuItem;
import javafx.scene.control.ScrollPane;
import javafx.scene.control.ScrollPane.ScrollBarPolicy;
import javafx.scene.control.TextField;
import javafx.scene.control.TreeItem;
import javafx.scene.control.TreeView;
import javafx.scene.input.MouseEvent;
import javafx.scene.control.Button;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Pane;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import main.fields.BorderField;
import main.fields.HomogenousField;
import main.fields.RadialField;
import main.fields.SuperposedField;
import main.fields.TangentialField;
import main.fields.VectorField;
import main.util.Axis;
import main.util.Proportionality;
import model.Data;
import model.MyViewData;
import model.Tile;

/**
 * View class of JavaFX, generates the GUI
 * 
 * @author Chang CHEN
 *
 */
public class MyView extends VBox{
	/**
	 * the class for side bar and depiction panel
	 * @author Chang CHEN
	 *
	 */
	public static class MyViewInfo {
		public TextField textField1;
		public TextField textField2;
		public TextField degree;
		public TextField alongX;
		public TextField alongY;
		public TextField scaleFactor;
		public TextField shearFactor;
		public TextField axisToShear;
		public ScrollPane mySidePanel;
		public ScrollPane myPanel;
		public Label label_start;
		public Pane displayPane;
		public TextField startX_text;
		public TextField startY_text;
		public TextField endX_text;
		public TextField endY_text;
		public TextField interval_text;
		public TextField tileSize_text;
		public TextField magnitude_text;
		public TextField originX_text;
		public TextField originY_text;
		public TextField axisLength_text;
		public Button depict;

		public MyViewInfo(TextField textField1, TextField textField2, TextField degree, ScrollPane mySidePanel) {
			this.textField1 = textField1;
			this.textField2 = textField2;
			this.degree = degree;
			this.mySidePanel = mySidePanel;
		}
	}
	//controller and model
	private MyController myController = new MyController(this);
	public MyViewData model = new MyViewData(new SuperposedField(), -4, 4, 0.3, -4, 4, 100, 5, 0, 0, 800);
	// the view
	private VBox myView;
	private BorderPane console;
	private MyMenu myMenu;
	public TreeView resultTreeView;
	public TreeView libTreeView;
	private TreeView optTreeView;
	public TreeView optInTreeView;
	private TreeView binTreeView;
	private VBox myLeftBar;
	public VBox detailPaneVBox;
	public BorderPane root;
	public Label detailLabel;
	public Label label1;
	private Label label2;
	public TreeItem<String> result = new TreeItem<String>("Result");
	private TreeItem<String> rubbishBin = new TreeItem<String>("Rubbish Bin");
	public TreeItem<String> lib = new TreeItem<String>("Library");
	public TreeItem<String> opt = new TreeItem<String>("Operation");
	public TreeItem<String> optIn = new TreeItem<String>("Operation List");
	public Label save_label = new Label("Save");
	private MenuItem save = new MenuItem("", save_label);
	public Label load_label = new Label("Load");
	private MenuItem load = new MenuItem("", load_label);
	private VBox operationParametersVBox;
	public MyViewInfo info = new MyViewInfo(null, null, null, new ScrollPane());
	/**
	 * Constructor to create a view object
	 * Includes all elements of the GUI and links them to the controller
	 */
	public MyView() {
		/**
		 * initialize()
		 */
		//the root of myView
		root = new BorderPane();
		//the pane display the console
		console = new BorderPane();
		//draw myPanel
		info.myPanel = new ScrollPane();
		info.myPanel.setHbarPolicy(ScrollBarPolicy.ALWAYS);
		info.myPanel.setVbarPolicy(ScrollBarPolicy.ALWAYS);
		info.myPanel.setFitToHeight(true);
		info.myPanel.setFitToWidth(true);
		info.myPanel.setContent(info.displayPane);
		//Pane for display the tiles
		info.displayPane = new Pane();
		info.displayPane.setManaged(false);
		Label startX = new Label(" startX: ");
		info.startX_text = new TextField(Double.toString(this.model.startX));
		Label startY = new Label(" startY: ");
		info.startY_text = new TextField(Double.toString(this.model.startY));
		Label endX = new Label(" endX: ");
		info.endX_text = new TextField(Double.toString(this.model.endX));
		Label endY = new Label(" endY: ");
		info.endY_text = new TextField(Double.toString(this.model.endY));
		Label interval = new Label(" interval: ");
		info.interval_text = new TextField(Double.toString(this.model.interval));
		Label tileSize = new Label(" tileSize: ");
		info.tileSize_text = new TextField(Double.toString(this.model.tileSize));
		Label magnitude = new Label(" magnitude: ");
		info.magnitude_text = new TextField(Double.toString(this.model.magnitude));
		Label originX = new Label(" originX: ");
		info.originX_text = new TextField(Double.toString(this.model.originX));
		Label originY = new Label(" originY: ");
		info.originY_text = new TextField(Double.toString(this.model.originY));
		Label axisLength = new Label(" axisLength: ");
		info.axisLength_text = new TextField(Integer.toString(this.model.axisLength));
		info.depict = new Button("Depict Superposed Field");
		info.depict.setStyle("-fx-background-color: #1885F2; -fx-text-fill: white");
		HBox hbox2 = new HBox(startX, info.startX_text,new Label("pixel(s)  "), startY,info.startY_text,new Label("pixel(s)  "),endX,info.endX_text,new Label("pixel(s)  "),endY,info.endY_text,new Label("pixel(s)  "));
		HBox hbox3 = new HBox(interval,info.interval_text,new Label("pixel(s)  "),tileSize, info.tileSize_text,new Label("pixel(s)  "),magnitude,info.magnitude_text,new Label("time(s)  "));
		HBox hbox4 = new HBox(originX,info.originX_text,new Label("pixel(s)  "),originY,info.originY_text,new Label("pixel(s)  "),axisLength,info.axisLength_text,new Label("pixel(s)  "));
		VBox vBox1 = new VBox(hbox2,hbox3,hbox4);
		HBox hbox5 = new HBox(vBox1,info.depict);
		//in side panel
		info.textField2 = new TextField();
		detailPaneVBox = new VBox();
		info.mySidePanel.setContent(detailPaneVBox);
		drawMenu();
		drawTreeView();		
		//the left bar 
		myLeftBar = new VBox();
		myLeftBar.getChildren().addAll(libTreeView, optTreeView, resultTreeView,binTreeView);
		//console layout
		console.setLeft(myLeftBar);
		console.setRight(info.mySidePanel);
		console.setCenter(info.myPanel);
		console.setTop(hbox5);
		//root layout
		root.setTop(myMenu.menuBar);
		root.setCenter(console);
		/**
		 * activate()
		 */
		resultTreeView.addEventHandler(MouseEvent.MOUSE_CLICKED, myController);
		libTreeView.addEventHandler(MouseEvent.MOUSE_CLICKED, myController);
		optTreeView.addEventHandler(MouseEvent.MOUSE_CLICKED, myController);
		binTreeView.addEventHandler(MouseEvent.MOUSE_CLICKED, myController);
		info.label_start.addEventHandler(MouseEvent.MOUSE_CLICKED, myController);	
		save_label.addEventHandler(MouseEvent.MOUSE_PRESSED, myController);
		load_label.addEventHandler(MouseEvent.MOUSE_PRESSED, myController);
		info.depict.addEventHandler(MouseEvent.MOUSE_CLICKED, myController);
		model.dataStorage = new ArrayList<Data>();
	}
	/**
	 * draw the tree views in the left bar
	 */
	@SuppressWarnings("unchecked")
	private void drawTreeView() {
		result = new TreeItem<String>("Result");
		lib = new TreeItem<String>("Library");
		opt = new TreeItem<String>("Operations");
		rubbishBin = new TreeItem<String>("Rubbish Bin"); 
		rubbishBin.setExpanded(true);
		rubbishBin.getChildren().addAll(
				new TreeItem<String>("(Default)")
		);
		result.setExpanded(true);
		result.getChildren().addAll(
		    new TreeItem<String>("(Superposed Vector Field)")
		); 
		lib.setExpanded(true);
		lib.getChildren().addAll(
		    new TreeItem<String>("Homogeneous Vector Field"),
		new TreeItem<String>("Boundary Vector Field"),
		new TreeItem<String>("Radical Vector Field"),
		new TreeItem<String>("Tangential Vector Field")
		); 
		opt.setExpanded(true);
		opt.getChildren().addAll(
		    new TreeItem<String>("Translate"),
		new TreeItem<String>("Rotate"),
		new TreeItem<String>("Scale by factor"),
		new TreeItem<String>("Shear")
		); 
		resultTreeView = new TreeView<String>(result);
		libTreeView = new TreeView<String>(lib);
		optTreeView = new TreeView<String>(opt);
		binTreeView = new TreeView<String>(rubbishBin);
		resultTreeView.setPrefSize(230,350);
		libTreeView.setPrefSize(230,150);
		optTreeView.setPrefSize(230,150);
		binTreeView.setPrefSize(230,150);
		myController.setCellFac(resultTreeView);
		myController.setCellFac(libTreeView);
		myController.setCellFac(optTreeView);
		myController.setCellFac(binTreeView);
	}	 
	/**
	 * draw menu
	 */
	private void drawMenu() {
		myMenu= new MyMenu();
		myMenu.file.setGraphic(myMenu.label_file);
		myMenu.file.getItems().add(save);
		myMenu.file.getItems().add(load);
		myMenu.start.setGraphic(myMenu.label_start);
		myMenu.insert.setGraphic(myMenu.label_insert);
		myMenu.menuBar.getMenus().add(myMenu.file);
		myMenu.menuBar.getMenus().add(myMenu.start);
		myMenu.menuBar.getMenus().add(myMenu.insert);
		info.label_start = new Label("Superposition ");
		save_label.setTextFill(Color.BLACK);
		load_label.setTextFill(Color.BLACK);
		Menu start = new Menu();
		start.setGraphic(info.label_start);
		myMenu.menuBar.getMenus().add(start);
	}
	/**
	 * display()
	 */
	public void drawDetailPanelHomoBoundary() {
		//clear mySidePanel
		info.mySidePanel = null;
		//draw new side panel
		detailLabel = new Label("Save and Preview");
		label1 = new Label("Value:");
		//input value
		HBox hb1 = new HBox();
		info.textField1 = new TextField ();
		hb1.getChildren().addAll(label1, info.textField1);
		hb1.setSpacing(10);
		detailPaneVBox.setSpacing(10);
		//clear the operation list
		optIn.getChildren().clear();
		optIn.setExpanded(true);
		optInTreeView = new TreeView<String>(optIn);
		optInTreeView.setPrefSize(250,200);   
		optIn.getChildren().addAll(
			     new TreeItem<String>("(Default)")
		); 
		operationParametersVBox=new VBox();
		detailPaneVBox.getChildren().clear();
		detailPaneVBox.getChildren().addAll(detailLabel,hb1,optInTreeView,operationParametersVBox);	
		detailLabel.addEventHandler(MouseEvent.MOUSE_CLICKED, myController);
		myController.setCellFac(optInTreeView);
		optInTreeView.addEventHandler(MouseEvent.MOUSE_CLICKED, myController);
	}
	/**
	 * draw the elementary vector field
	 * @param string the elementary vector field name
	 * @param str1 value, x
	 * @param str2 y
	 * @param data 
	 */
	public void drawPanel(String string, Double str1, Double str2, Data data) {
		//clear the display pane
		info.displayPane.getChildren().clear();
		//clear result vector field
		model.resultVectorField = null;
		if(string.equals("Homogeneous Vector Field")) {
			//modify vector fields
			model.resultVectorField = new HomogenousField(str1);
		}
		if(string.equals("Boundary Vector Field")) {
			//modify vector fields
			//scroll bar needed to choose different proportionality 
			model.resultVectorField = new BorderField(str1, Proportionality.LINEAR);	
		}
		if(string.equals("Radical Vector Field")) {
			//modify vector fields
			//scroll bar needed to choose different proportionality
			model.resultVectorField = new RadialField(str1,str2, Proportionality.LINEAR);
		}
		if(string.equals("Tangential Vector Field")) {
			//modify vector fields
			//scroll bar needed to choose different proportionality
			model.resultVectorField = new TangentialField(str1,str2, Proportionality.LINEAR);	
		}	
		for (TreeItem<String> depNode: optIn.getChildren()) {
			if(depNode.getValue().contentEquals("Rotate")) {
				if(info.degree!=null) {
					model.resultVectorField.rotate(Double.parseDouble(info.degree.getText()));
				}else {
					model.resultVectorField.rotate(Double.parseDouble(data.getDegree().getText()));
					info.degree = new TextField();
					info.degree.setText(data.getDegree().getText());
				}
			}
			if(depNode.getValue().contentEquals("Translate")) {
				if(info.alongX!=null&& info.alongY!=null) {
					model.resultVectorField.translate(Double.parseDouble(info.alongX.getText()), Double.parseDouble(info.alongY.getText()));
				}else {
					model.resultVectorField.translate(Double.parseDouble(data.getAlongX().getText()), Double.parseDouble(data.getAlongY().getText()));
					info.alongX = new TextField();
					info.alongY = new TextField();
					info.alongX.setText(data.getAlongX().getText());
					info.alongY.setText(data.getAlongY().getText());
				}
			}
			if(depNode.getValue().contentEquals("Scale by factor")) {	
				if(info.scaleFactor!=null) {
					model.resultVectorField.scale(Double.parseDouble(info.scaleFactor.getText()));
				}else {
					model.resultVectorField.scale(Double.parseDouble(data.getScaleFactor().getText()));
					info.scaleFactor = new TextField();
					info.scaleFactor.setText(data.getScaleFactor().getText());
				}
			}
			if(depNode.getValue().contentEquals("Shear")) {
				//scroll bar needed to choose Axis
				if(info.shearFactor!=null) {
					model.resultVectorField.shear(Double.parseDouble(info.shearFactor.getText()),Axis.X);
				}else {
					model.resultVectorField.shear(Double.parseDouble(data.getShearFactor().getText()),Axis.X);
					info.shearFactor = new TextField();
					info.shearFactor.setText(data.getShearFactor().getText());
				}
			}
			
		}
		//autoadjust the displayPane
		this.model.originX = this.info.displayPane.getWidth()/2.0;
		this.model.originY = this.info.displayPane.getHeight()/2.0;
		//update the textfield
		this.info.originX_text.setText(Double.toString(this.model.originX));
		this.info.originY_text.setText(Double.toString(this.model.originY));	
		//draw the result
		for (double x = model.startX; x <= model.endX; x += model.interval) {
			for (double y = model.startY; y <= model.endY; y += model.interval) {
				model.output = model.resultVectorField.getFunctionValue(x, y);
				//decide the magnitude of the arrow
				Tile tile = new Tile(model.output.getX()*model.magnitude,model.output.getY()*model.magnitude, this);
				//change the starting point in the panel
				tile.setTranslateX(model.originX+x*model.tileSize);
				tile.setTranslateY(model.originY+y*model.tileSize);
				info.displayPane.getChildren().add(tile);
			}
		}
		DrawAxis axis = new DrawAxis((int)model.originX,(int)model.originY,model.axisLength);
		info.displayPane.getChildren().addAll(axis.getxAxis(),axis.getyAxis(),axis.getxAxisTip(),axis.getyAxisTip());
		
		info.displayPane.setScaleY(-1);
		info.myPanel.setContent(info.displayPane);
	}
	/**
	 * construct the side panel after a rotate is performed on the vector field, and user click on it to change parameters
	 * @param string useless string, mainly as a placeholder
	 */
	public void drawRotatePanel(String string) {
		Label label1 = new Label("Degree:");
		HBox hb1 = new HBox();
		if(myController.model.degree!=null) {
			info.degree = new TextField(myController.model.degree);
		}else {
			info.degree = new TextField ();
		}
		hb1.getChildren().addAll(label1, info.degree);
		hb1.setSpacing(10);
		operationParametersVBox.getChildren().clear();
		operationParametersVBox.getChildren().addAll(hb1);
		
	}
	/**
	 * construct the side panel after a translate is performed on the vector field, and user click on it to change parameters
	 * @param string useless string, mainly as a placeholder
	 */
	public void drawTranslatePanel(String string) {
		Label label1 = new Label("Along X Axis:");
		HBox hb1 = new HBox();
		if(myController.model.alongX!=null) {
			info.alongX = new TextField(myController.model.alongX);
		}else {
			info.alongX = new TextField ();
		}
		hb1.getChildren().addAll(label1, info.alongX);
		hb1.setSpacing(10);
		Label label2 = new Label("Along Y Axis:");
		HBox hb2 = new HBox();
		if(myController.model.alongY!=null) {
			info.alongY = new TextField(myController.model.alongY);
		}else {
			info.alongY = new TextField ();
		}
		hb2.getChildren().addAll(label2, info.alongY);
		hb2.setSpacing(10);
		operationParametersVBox.getChildren().clear();
		operationParametersVBox.getChildren().addAll(hb1,hb2);
	}
	/**
	 * construct the side panel after a scale is performed on the vector field, and user click on it to change parameters
	 * @param string useless string, mainly as a placeholder
	 */
	public void drawScalePanel(String string) {
		Label label1 = new Label("Scaling factor:");
		HBox hb1 = new HBox();
		if(myController.model.scaleFactor!=null) {
			info.scaleFactor = new TextField(myController.model.scaleFactor);
		}else {
			info.scaleFactor = new TextField ();
		}
		hb1.getChildren().addAll(label1, info.scaleFactor);
		hb1.setSpacing(10);
		operationParametersVBox.getChildren().clear();
		operationParametersVBox.getChildren().addAll(hb1);
	}
	/**
	 * construct the side panel after a shear is performed on the vector field, and user click on it to change parameters
	 * @param string useless string, mainly as a placeholder
	 */
	public void drawShearPanel(String string) {
		Label label1 = new Label("Shearing factor:");
		HBox hb1 = new HBox();
		if(myController.model.shearFactor!=null) {
			info.shearFactor = new TextField(myController.model.shearFactor);
		}else {
			info.shearFactor = new TextField ();
		}
		hb1.getChildren().addAll(label1, info.shearFactor);
		hb1.setSpacing(10);
		Label label2 = new Label("Axis to shear by:");
		HBox hb2 = new HBox();
		info.axisToShear = new TextField ("X");
		hb2.getChildren().addAll(label2, info.axisToShear);
		hb2.setSpacing(10);
		operationParametersVBox.getChildren().clear();
		operationParametersVBox.getChildren().addAll(hb1,hb2);
	}
	/**
	 * construct the side panel after the user creates a tangential or radical vector field
	 */
	public void drawDetailPanelTanRadical() {
		info.mySidePanel = null;
		detailLabel = new Label("Save and Preview");
		
		label1 = new Label("X:");
		HBox hb1 = new HBox();
		info.textField1 = new TextField ();
		hb1.getChildren().addAll(label1, info.textField1);
		hb1.setSpacing(10);
		
		Label label2 = new Label("Y:");
		HBox hb2 = new HBox();
		info.textField2 = new TextField ();
		hb2.getChildren().addAll(label2, info.textField2);
		hb2.setSpacing(10);
		
		detailPaneVBox.setSpacing(10);
		//clear the operation list
		optIn.getChildren().clear();
		optIn.setExpanded(true);
		optInTreeView = new TreeView<String>(optIn);
		optInTreeView.setPrefSize(250,200);   
		optIn.getChildren().addAll(
			     new TreeItem<String>("(Default)")
		); 
		operationParametersVBox=new VBox();
		detailPaneVBox.getChildren().clear();
		detailPaneVBox.getChildren().addAll(detailLabel,hb1,hb2,optInTreeView,operationParametersVBox);	
		

		detailLabel.addEventHandler(MouseEvent.MOUSE_CLICKED, myController);
		myController.setCellFac(optInTreeView);
		optInTreeView.addEventHandler(MouseEvent.MOUSE_CLICKED, myController);
	}
	/**
	 * construct the side panel after the user creates a customized vector field
	 */
	public void drawDetailPanelElse() {
		info.mySidePanel = null;
		detailLabel = new Label("Save and Preview");
		
		label1 = new Label("Name: ");
		HBox hb1 = new HBox();
		info.textField1 = new TextField ();
		hb1.getChildren().addAll(label1, info.textField1);
		hb1.setSpacing(10);
			
		detailPaneVBox.setSpacing(10);
		//clear the operation list
		optIn.getChildren().clear();
		optIn.setExpanded(true);
		optInTreeView = new TreeView<String>(optIn);
		optInTreeView.setPrefSize(250,200);   
		optIn.getChildren().addAll(
			     new TreeItem<String>("(Default)")
		); 
		operationParametersVBox=new VBox();
		detailPaneVBox.getChildren().clear();
		detailPaneVBox.getChildren().addAll(detailLabel,hb1,optInTreeView,operationParametersVBox);	
		

		detailLabel.addEventHandler(MouseEvent.MOUSE_CLICKED, myController);
		myController.setCellFac(optInTreeView);
		optInTreeView.addEventHandler(MouseEvent.MOUSE_CLICKED, myController);
		
	}
	/**
	 * display the result vector field and autojust it to the center of the display panel
	 * @param field the vector field needed to be displayed
	 */
	public void display(VectorField field) {
		  //clear the display pane
		  info.displayPane.getChildren().clear();
		  //autoadjust the displayPane
		  model.originX = info.displayPane.getWidth()/2.0;
		  model.originY = info.displayPane.getHeight()/2.0;
		  //update the textfield`1
		  info.originX_text.setText(Double.toString(model.originX));
		  info.originY_text.setText(Double.toString(model.originY));
		  
		  for (double x = model.startX; x <= model.endX; x += model.interval) {
			  for (double y = model.startY; y <= model.endY; y += model.interval) {
				  model.output = field.getFunctionValueOptimized(x, y);
				  //decide the magnitude of the arrow
				  Tile tile = new Tile(model.output.getX()*model.magnitude,model.output.getY()*model.magnitude, this);
				  //change the starting point in the panel
				  tile.setTranslateX(model.originX+x*model.tileSize);
				  tile.setTranslateY(model.originY+y*model.tileSize);
				  info.displayPane.getChildren().add(tile);
			  }
		  }
			
		  DrawAxis axis = new DrawAxis((int)model.originX,(int)model.originY,model.axisLength);
		  axis.setScaleY(-1);
		  info.displayPane.getChildren().addAll(axis.getxAxis(),axis.getyAxis(),axis.getxAxisTip(),axis.getyAxisTip());
		  info.displayPane.setScaleY(-1);
		  info.myPanel.setContent(info.displayPane);
	}
	/**
	 * provide useful information in the result tree view
	 * @param data the data of vector field selected
	 * @param depNode the tree item in the result tree view
	 */
	public void giveInfo(Data data, TreeItem<String> depNode) {
		  //give tree elements
		  if(data.getAlongX()!=null) {
			  depNode.getChildren().add(new TreeItem<String>("Along X: "+data.getAlongX().getText()));
		  }
		  if(data.getAlongY()!=null) {
			  depNode.getChildren().add(new TreeItem<String>("Along Y: "+data.getAlongY().getText()));
		  }
		  if(data.getAxisToShear()!=null) {
			  depNode.getChildren().add(new TreeItem<String>("Axis To Shear: "+data.getAxisToShear().getText()));
		  }
		  if(data.getDegree()!=null) {
			  depNode.getChildren().add(new TreeItem<String>("Degree: "+data.getDegree().getText()));
		  }
		  if(data.getScaleFactor()!=null) {
			  depNode.getChildren().add(new TreeItem<String>("ScaleFactor: "+data.getScaleFactor().getText()));
		  }
		  if(data.getShearFactor()!=null) {
			  depNode.getChildren().add(new TreeItem<String>("ShearFactor: "+data.getShearFactor().getText()));
		  }
		  if(data.getTextField1()!=null) {
			  depNode.getChildren().add(new TreeItem<String>("Text Field1: "+data.getTextField1().getText()));
		  }
		  if(data.getTextField2()!=null) {
			  depNode.getChildren().add(new TreeItem<String>("Text Field2: "+data.getTextField2().getText()));
		  }
	}
	/**
	 * depict the result vector field according to the parameters on display setting
	 */
	public void depict() {
		model.startX = Double.parseDouble(info.startX_text.getText());
		  model.startY = Double.parseDouble(info.startY_text.getText());
		  model.endX = Double.parseDouble(info.endX_text.getText());
		  model.endY = Double.parseDouble(info.endY_text.getText());
		  model.interval= Double.parseDouble(info.interval_text.getText());
		  model.tileSize = Double.parseDouble(info.tileSize_text.getText());
		  model.magnitude = Double.parseDouble(info.magnitude_text.getText());
		  model.originX = Double.parseDouble(info.originX_text.getText());
		  model.originY = Double.parseDouble(info.originY_text.getText());
		  model.axisLength = (int) Double.parseDouble(info.axisLength_text.getText());
		  //clear the display pane
		  info.displayPane.getChildren().clear();
		  display(model.supVectorField);
	}
	/**
	 * construct the result tree view according to the data of the vector field selected
	 * @param data the data of selected vector field
	 */
	public void drawResultTreeView(Data data) {
		if(info.textField1!=null&& data.getTextField1()!=null) {
			  
			  info.textField1.setText(data.getTextField1().getText());
		  }
		  if(info.textField2!=null&& data.getTextField2()!=null) {
			  
			  info.textField2.setText(data.getTextField2().getText());
		  } 
		  //generate the form
		  if(data.getDegree()!=null) {
			  myController.model.degree = data.getDegree().getText();
		  }
		  if(data.getAlongX()!=null) {
			  
			  myController.model.alongX = data.getAlongX().getText();
		  }
		  if(data.getAlongY()!=null) {
			  
			  myController.model.alongY = data.getAlongY().getText();
		  }
		  if(data.getScaleFactor()!=null) {
			  
			  myController.model.scaleFactor = data.getScaleFactor().getText();
		  }
		  if(data.getShearFactor()!=null) {
			  
			  myController.model.shearFactor = data.getShearFactor().getText();
		  }
		  if(data.getAxisToShear()!=null) {
			  
			  myController.model.axisToShear = data.getAxisToShear().getText();
		  }
		  
		  if(data.getDegree()!=null) {
			  optIn.getChildren().addAll(
					     new TreeItem<String>("Rotate")
				); 
		  }
		  if(data.getAlongX()!=null&& data.getAlongY()!=null) {
			  optIn.getChildren().addAll(
					  new TreeItem<String>("Translate")
			    );
		  }
		  if(data.getScaleFactor()!=null) {
			  optIn.getChildren().addAll(
					  new TreeItem<String>("Scale by factor")
			    );
		  }
		  if(data.getShearFactor()!=null) {
			  optIn.getChildren().addAll(
					  new TreeItem<String>("Shear")
			    );
		  }
	}
}

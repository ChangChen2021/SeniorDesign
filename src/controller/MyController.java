package controller;

import java.io.File;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.ListIterator;
import java.util.Set;
import javafx.event.EventHandler;
import javafx.scene.Node;
import javafx.scene.control.TextField;
import javafx.scene.control.TreeCell;
import javafx.scene.control.TreeItem;
import javafx.scene.control.TreeView;
import javafx.scene.input.ClipboardContent;
import javafx.scene.input.DataFormat;
import javafx.scene.input.DragEvent;
import javafx.scene.input.Dragboard;
import javafx.scene.input.MouseEvent;
import javafx.scene.input.TransferMode;
import javafx.stage.FileChooser;
import javafx.stage.FileChooser.ExtensionFilter;
import javafx.stage.Stage;
import main.fields.SuperposedField;
import main.fields.VectorField;
import main.util.Axis;
import model.Data;
import model.MyControllerData;
import model.TaskNode;
import view.MyView;

/**
 * Controller class of JavaFX, acts as an intermediary between view and model
 * defines what should happen on user interaction
 * 
 * @author Chang Chen
 */

public class MyController implements EventHandler<MouseEvent> {
	/**
	 * the class for drag and drop function
	 * @author Chang Chen
	 *
	 */
	public static class DragAndDrop {
		public String oldName;
		public Set<String> parentSet;
		public TreeCell node;
		public TreeItem<TaskNode> draggedItem;
		public int index;
		private static final DataFormat JAVA_FORMAT = new DataFormat("application/x-java-serialized-object");

		public DragAndDrop(int index) {
			this.index = index;
		}
	}
	/**
	 * Fields of the controller which point to view
	 */
	private MyView myView;
	public MyControllerData model = new MyControllerData();
	// the drag and drop function
	private DragAndDrop dragAndDrop = new DragAndDrop(0);

	/**
	 * Constructor to create controller object
	 * 
	 * @param myView view object of JavaFX which is managed by this controller
	 *
	 */
	public MyController(MyView myView) {
		this.myView = myView;
	}
	/**
	 * handle mouse event
	 */
	@Override
	public void handle(MouseEvent event) {
		actionPerformed(event);
	}
	/**
	 * ActionListener, will be called whenever a key on the calculator is pressed
	 */
	@SuppressWarnings("unchecked")
	private void actionPerformed(MouseEvent ev) {
		if (ev.getSource() == myView.save_label) {
			// generate a file chooser
			File file = generateFileChooser();
			// save the file
			if (file != null) {
				VectorField.setSavingPath(file.getParent());
				for (Data data : myView.model.dataStorage) {
					if (data.getType().equals("(Superposed Vector Field)")) {
						data.getResult().save(file.getName());
					}
				}
			}
		}
		if (ev.getSource() == myView.load_label) {
			// generate a file chooser
			File file = generateFileChooser();
			// load the file
			if (file != null) {
				// read in json
				VectorField field = VectorField.readInJSON(file.getAbsolutePath());
				for (Data data : field.dataStorage) {
					if (data.getParent().contentEquals("Result")) {
						TreeItem<String> importedCustomizedVectorField = new TreeItem<String>(data.getType());
						myView.result.getChildren().addAll(importedCustomizedVectorField);
						myView.giveInfo(data, importedCustomizedVectorField);
					} else {
						data.setType(data.getType() + dragAndDrop.index++);
						TreeItem<String> importedVectorField = new TreeItem<String>(data.getType());
						for (TreeItem<String> depNode : myView.result.getChildren()) {
							if (depNode.getValue().equals(data.getParent())) {
								depNode.getChildren().addAll(importedVectorField);
								myView.giveInfo(data, importedVectorField);
							}
						}
					}
					// enlist the data
					myView.model.dataStorage.add(data);
				}
			}
		}
		if (ev.getSource() == myView.libTreeView || ev.getSource() == myView.resultTreeView) {
			if (ev.getPickResult().getIntersectedNode().getClass().toString()
					.equals("class javafx.scene.control.TreeCell")) {
				dragAndDrop.node = (TreeCell) ev.getPickResult().getIntersectedNode();
				boolean flag = false;
				// clear the form
				myView.info.degree = null;
				myView.info.alongX = null;
				myView.info.alongY = null;
				myView.info.scaleFactor = null;
				myView.info.shearFactor = null;
				myView.info.axisToShear = null;
				// Differentiate the field class
				if (dragAndDrop.node.toString().contains("Homogeneous Vector Field")) {
					myView.model.vectorfield = "Homogeneous Vector Field";
					playHomoBoundary((TreeCell) dragAndDrop.node);
					flag = true;
				}
				if (dragAndDrop.node.toString().contains("Boundary Vector Field")) {
					myView.model.vectorfield = "Boundary Vector Field";
					playHomoBoundary((TreeCell) dragAndDrop.node);
					flag = true;
				}
				if (dragAndDrop.node.toString().contains("Radical Vector Field")) {
					myView.model.vectorfield = "Radical Vector Field";
					playTanRadical((TreeCell) dragAndDrop.node);
					flag = true;
				}
				if (dragAndDrop.node.toString().contains("Tangential Vector Field")) {
					myView.model.vectorfield = "Tangential Vector Field";
					playTanRadical((TreeCell) dragAndDrop.node);
					flag = true;
				}
				if (flag == false && dragAndDrop.node.toString() != "") {
					playCustomized((TreeCell) dragAndDrop.node);
				}	
			}
			if (ev.getSource() == myView.resultTreeView) {
				if (ev.getPickResult().getIntersectedNode().getClass().toString()
						.equals("class javafx.scene.control.TreeCell")) {
					dragAndDrop.node = (TreeCell) ev.getPickResult().getIntersectedNode();
					// traverse all the data
					for (Data data : myView.model.dataStorage) {
						if (data.getType().equals(dragAndDrop.node.getItem().toString())) {
							// clear the form
							myView.info.degree = null;
							myView.info.alongX = null;
							myView.info.alongY = null;
							myView.info.scaleFactor = null;
							myView.info.shearFactor = null;
							myView.info.axisToShear = null;
							//clear the operation list
							myView.optIn.getChildren().clear();
							myView.optIn.setExpanded(true);
							myView.optIn.getChildren().addAll(
								     new TreeItem<String>("(Default)")
							); 
							myView.drawResultTreeView(data);
						}
					}	
					// draw the result
					// 1. get the vector field
					for (ListIterator<Data> lir = myView.model.dataStorage.listIterator(); lir.hasNext();) {
						Data data = lir.next();
						// find the vector field
						if (data.getType().equals(dragAndDrop.node.getTreeItem().getValue().toString())
								&& data.getParent().equals(dragAndDrop.node.getTreeItem().getParent().getValue().toString())) {
							// customized vector field
							if (data.getParent().equals("Result") && !data.getType().contains("Vector Field")) {
								// calculate the parent vector field
								calculateParentVectorField(data.getType());
								doOperationOnCustomizedVectorField();
							}
							VectorField field = data.getResult();
							// 2. draw the vector field
							// clear side panel
							myView.info.mySidePanel = null;
							myView.display(field);
						} else if (data.getType().contentEquals(dragAndDrop.node.getText())) {
							data.setParent(dragAndDrop.node.getTreeItem().getParent().getValue().toString());
							VectorField field = data.getResult();
							// 2. draw the vector field
							// clear side panel
							myView.info.mySidePanel = null;
							myView.display(field);
						}
					}
				}
			}
		}
		if (ev.getSource() == myView.detailLabel) {
			// change the name
			if (myView.label1.getText().equals("Name: ")) {
				String name = null;
				name = myView.info.textField1.getText();
				if (!name.equals(dragAndDrop.oldName) && myView.label1.isVisible() == true) {
					// update all the data in dataStorage
					for (Data data : myView.model.dataStorage) {
						if (data.getType().equals(dragAndDrop.oldName)) {
							data.setType(name);
						}
						if (data.getParent().equals(dragAndDrop.oldName)) {
							data.setParent(name);
						}
					}
					// update the result tree view
					for (TreeItem<String> depNode : myView.result.getChildren()) {
						if (depNode.getValue().equals(dragAndDrop.oldName)) {
							depNode.setValue(name);
						}
					}
				}
				if (myView.info.textField1.isVisible() == false) {
					// calculate the supVectorField again
					superposition();
					myView.model.resultVectorField = myView.model.supVectorField;
				} else if(myView.info.textField1.isVisible() == true)  {
					// calculate the parent vector field
					calculateParentVectorField(name);
				}
				doOperationOnCustomizedVectorField();
				myView.display(myView.model.resultVectorField);
				// clear the operation list
				myView.optIn.getChildren().clear();
				myView.optIn.getChildren().addAll(new TreeItem<String>("(Default)"));
				// clear all metadata
				myView.info.textField1 = new TextField();
				myView.info.textField2 = new TextField();
				myView.info.degree = null;
				myView.info.alongX = null;
				myView.info.alongY = null;
				myView.info.scaleFactor = null;
				myView.info.shearFactor = null;
				myView.info.axisToShear = null;
				// block all element
				myView.detailPaneVBox.getChildren().clear();
			} else {
				// draw panel
				Double input1 = null;
				Double input2 = null;
				if (!myView.info.textField1.getText().isEmpty()) {
					input1 = Double.parseDouble(myView.info.textField1.getText());
				}
				if (!myView.info.textField2.getText().isEmpty()) {
					input2 = Double.parseDouble(myView.info.textField2.getText());
				}
				// sava data
				// check whether data was generated before
				boolean flag = false;
				for (Data data : myView.model.dataStorage) {
					if (dragAndDrop.node.getTreeItem().getValue().equals(data.getType())
							&& dragAndDrop.node.getTreeItem().getParent().getValue().equals(data.getParent())) {
						myView.drawPanel(myView.model.vectorfield, input1, input2, data);
						data.setTitle(myView.model.resultVectorField.ID);
						data.setResult(myView.model.resultVectorField);
						data.setTextField1(myView.info.textField1);
						data.setTextField2(myView.info.textField2);
						data.setDegree(myView.info.degree);
						data.setAlongX(myView.info.alongX);
						data.setAlongY(myView.info.alongY);
						data.setScaleFactor(myView.info.scaleFactor);
						data.setShearFactor(myView.info.shearFactor);
						flag = true;
					}
				}
				if (flag == false) {
					myView.drawPanel(myView.model.vectorfield, input1, input2, null);
					// create personal data form
					Data data = new Data(myView.model.resultVectorField.ID, myView.model.resultVectorField,
							myView.info.textField1, myView.info.textField2, myView.info.degree, myView.info.alongX, myView.info.alongY,
							myView.info.scaleFactor, myView.info.shearFactor, myView.info.axisToShear, dragAndDrop.node.getItem().toString());
					data.setParent(dragAndDrop.node.getTreeItem().getParent().getValue().toString());
					// enlist the data
					myView.model.dataStorage.add(data);
				}
			}
			// clear the operation list
			myView.optIn.getChildren().clear();
			myView.optIn.getChildren().addAll(new TreeItem<String>("(Default)"));
			// clear all metadata
			myView.info.textField1 = new TextField();
			myView.info.textField2 = new TextField();
			myView.info.degree = null;
			myView.info.alongX = null;
			myView.info.alongY = null;
			myView.info.scaleFactor = null;
			myView.info.shearFactor = null;
			myView.info.axisToShear = null;
			// block all element
			myView.detailPaneVBox.getChildren().clear();
		}
		if (ev.getSource() == myView.info.depict) {
			myView.depict();
		}
		if (ev.getSource() == myView.info.label_start) {
			superposition();
		}
		if (ev.getSource() == myView.optInTreeView) {
			Node node = ev.getPickResult().getIntersectedNode();
			drawOptInPanel(node);
		}
	}
	/**
	 * perform operation on customized vector field
	 */
	private void doOperationOnCustomizedVectorField() {
		// save the supVectorField
		boolean flag = false;
		for (Iterator<Data> iterator = myView.model.dataStorage.iterator(); iterator.hasNext();) {
			Data data = iterator.next();
			if (data.getType().equals(dragAndDrop.node.getTreeItem().getValue().toString())&& data.getParent().equals("Result") && !data.getType().contains("Vector Field")) {
				for (TreeItem<String> depNode : myView.optIn.getChildren()) {
					if (depNode.getValue().contentEquals("Rotate")) {
						if (myView.info.degree != null) {
							myView.model.resultVectorField.rotate(Double.parseDouble(myView.info.degree.getText()));
						} else {
							myView.model.resultVectorField.rotate(Double.parseDouble(data.getDegree().getText()));
							myView.info.degree = new TextField();
							myView.info.degree.setText(data.getDegree().getText());
						}
					}
					if (depNode.getValue().contentEquals("Translate")) {
						if (myView.info.alongX != null && myView.info.alongY != null) {
							myView.model.resultVectorField.translate(Double.parseDouble(myView.info.alongX.getText()),
									Double.parseDouble(myView.info.alongY.getText()));
						} else {
							myView.model.resultVectorField.translate(Double.parseDouble(data.getAlongX().getText()),
									Double.parseDouble(data.getAlongY().getText()));
							myView.info.alongX = new TextField();
							myView.info.alongY = new TextField();
							myView.info.alongX.setText(data.getAlongX().getText());
							myView.info.alongY.setText(data.getAlongY().getText());
						}
					}
					if (depNode.getValue().contentEquals("Scale by factor")) {
						if (myView.info.scaleFactor != null) {
							myView.model.resultVectorField.scale(Double.parseDouble(myView.info.scaleFactor.getText()));
						} else {
							myView.model.resultVectorField.scale(Double.parseDouble(data.getScaleFactor().getText()));
							myView.info.scaleFactor = new TextField();
							myView.info.scaleFactor.setText(data.getScaleFactor().getText());
						}
					}
					if (depNode.getValue().contentEquals("Shear")) {
						// scroll bar needed to choose Axis
						if (myView.info.shearFactor != null) {
							myView.model.resultVectorField.shear(Double.parseDouble(myView.info.shearFactor.getText()),
									Axis.X);
						} else {
							myView.model.resultVectorField.shear(Double.parseDouble(data.getShearFactor().getText()),
									Axis.X);
							myView.info.shearFactor = new TextField();
							myView.info.shearFactor.setText(data.getShearFactor().getText());
						}
					}
				}
				data.setTitle(myView.model.resultVectorField.ID);
				data.setResult(myView.model.resultVectorField);
				data.setTextField1(myView.info.textField1);
				data.setTextField2(myView.info.textField2);
				data.setDegree(myView.info.degree);
				data.setAlongX(myView.info.alongX);
				data.setAlongY(myView.info.alongY);
				data.setScaleFactor(myView.info.scaleFactor);
				data.setShearFactor(myView.info.shearFactor);
				flag = true;
			}
		}
		if (flag == false) {
			for (TreeItem<String> depNode : myView.optIn.getChildren()) {
				if (depNode.getValue().contentEquals("Rotate")) {
					myView.model.resultVectorField.rotate(Double.parseDouble(myView.info.degree.getText()));
				}
				if (depNode.getValue().contentEquals("Translate")) {
					myView.model.resultVectorField.translate(Double.parseDouble(myView.info.alongX.getText()),
							Double.parseDouble(myView.info.alongY.getText()));
					;
				}
				if (depNode.getValue().contentEquals("Scale by factor")) {
					myView.model.resultVectorField.scale(Double.parseDouble(myView.info.scaleFactor.getText()));
				}
				if (depNode.getValue().contentEquals("Shear")) {
					// scroll bar needed to choose Axis
					myView.model.resultVectorField.shear(Double.parseDouble(myView.info.shearFactor.getText()), Axis.X);
				}
			}
			myView.model.resultVectorField = myView.model.supVectorField;
			Data data = new Data(myView.model.resultVectorField.ID, myView.model.resultVectorField, myView.info.textField1,
					myView.info.textField2, myView.info.degree, myView.info.alongX, myView.info.alongY, myView.info.scaleFactor,
					myView.info.shearFactor, myView.info.axisToShear, dragAndDrop.node.getTreeItem().getValue().toString());
			myView.model.dataStorage.add(data);
		}
	}
	/**
	 * calculate the customized vector field
	 * @param name the name of the customized vector field
	 */
	private void calculateParentVectorField(String name) {
		myView.model.resultVectorField = null;
		myView.model.resultVectorField = new SuperposedField();
		for (Iterator<Data> iterator = myView.model.dataStorage.iterator(); iterator.hasNext();) {
			Data data = iterator.next();
			if (data.getParent().equals(name)) {
				myView.model.resultVectorField = VectorField.superposition(myView.model.resultVectorField,
						data.getResult());
			}
		}
	}
	/**
	 * superpose the vector fields
	 */
	private void superposition() {
		// clear supVectorField
		myView.model.supVectorField = new SuperposedField();
		// clear side panel
		myView.info.mySidePanel = null;
		// get parent set
		dragAndDrop.parentSet = new HashSet<String>();
		for (Data data : myView.model.dataStorage) {
			dragAndDrop.parentSet.add(data.getParent());
		}
		// for each parent
		Iterator<String> it = dragAndDrop.parentSet.iterator();
		while (it.hasNext()) {
			// according to the parent, calculate the vector field
			String parent = it.next();
			if (!parent.equals("Result")) {
				for (Data data : myView.model.dataStorage) {
					for (TreeItem<String> depNode : myView.result.getChildren()) {
						for (TreeItem<String> subNode : depNode.getChildren()) {
							if (subNode.getValue().equals(data.getType())
									&& subNode.getParent().getValue().equals(parent)) {
								subNode.getChildren().clear();
								myView.giveInfo(data, subNode);
							}
						}
					}
				}
			} else {
				for (Data data : myView.model.dataStorage) {
					if (data.getParent().equals("Result")) {
						for (TreeItem<String> depNode : myView.result.getChildren()) {
							// point to the node that links to the data
							if (depNode.getValue().equals(data.getType())
									&& depNode.getParent().getValue().equals(data.getParent())
									&& !depNode.getValue().equals("(Superposed Vector Field)")) {
								myView.model.supVectorField = VectorField.superposition(myView.model.supVectorField,
										data.getResult());
							}
							if (depNode.getValue().equals(data.getType())
									&& depNode.getParent().getValue().equals(data.getParent())) {
								Iterator iterator = depNode.getChildren().iterator();
								List<TreeItem<String>> list = new ArrayList<TreeItem<String>>();
								while (iterator.hasNext()) {
									TreeItem<String> subNode = (TreeItem<String>) iterator.next();
									if (subNode.isLeaf()) {
										list.add(subNode);
									}
								}
								depNode.getChildren().removeAll(list);
								myView.giveInfo(data, depNode);
							}
						}
					}
				}
			}
		}
		// save the supVectorField
		boolean flag = false;
		for (Data data : myView.model.dataStorage) {
			if (data.getType().equals("(Superposed Vector Field)")) {
				data.setResult(myView.model.supVectorField);
				flag = true;
			}
		}
		if (flag == false) {
			myView.model.resultVectorField = myView.model.supVectorField;
			Data data = new Data(myView.model.resultVectorField.ID, myView.model.resultVectorField, myView.info.textField1,
					myView.info.textField2, myView.info.degree, myView.info.alongX, myView.info.alongY, myView.info.scaleFactor,
					myView.info.shearFactor, myView.info.axisToShear, "(Superposed Vector Field)");
			myView.model.dataStorage.add(data);
		}
	}
	/**
	 * construct the optIn panel
	 * @param node the selected node in optIn tree view
	 */
	private void drawOptInPanel(Node node) {
		if (node.toString().contains("Rotate")) {
			myView.drawRotatePanel("Rotate");
		}
		if (node.toString().contains("Translate")) {
			myView.drawTranslatePanel("Translate");
		}
		if (node.toString().contains("Scale by factor")) {
			myView.drawScalePanel("Scale");
		}
		if (node.toString().contains("Shear")) {
			myView.drawShearPanel("Shear");
		}
	}
	/**
	 * generate file chooser
	 * @return the chosen file
	 */
	private File generateFileChooser() {
		FileChooser fileChooser = new FileChooser();
		fileChooser.getExtensionFilters().addAll(new ExtensionFilter("Json Files", "*.json"));
		File file = fileChooser.showSaveDialog(new Stage());
		return file;
	}
	/**
	 * when select customized vector field
	 * @param node2 the selected node
	 */
	private void playCustomized(TreeCell node2) {
		String input1 = null;
		// draw detail panel
		myView.drawDetailPanelElse();
		if (node2.getTreeItem().getValue().equals("(Superposed Vector Field)")) {
			myView.label1.setVisible(false);
			myView.info.textField1.setVisible(false);
			// clear result vector field
			myView.model.resultVectorField = null;
			// draw the result
			// 1. get the vector field
			System.out.println(dragAndDrop.node.getText());
			for (Data data : myView.model.dataStorage) {
				if (dragAndDrop.node.getText().contains(data.getType())) {
					myView.model.resultVectorField = myView.model.supVectorField;
					// 2. draw the vector field
					// clear side panel
					myView.info.mySidePanel = null;
					myView.display(myView.model.resultVectorField);
				}
			}
		} else {
			myView.label1.setVisible(true);
			myView.info.textField1.setVisible(true);
			// get the name
			dragAndDrop.oldName = node2.getItem().toString();
			myView.info.textField1.setText(dragAndDrop.oldName);
			// generate vector fields
			if (!myView.info.textField1.getText().isEmpty()) {
				input1 = myView.info.textField1.getText();
			}
			myView.model.resultVectorField = null;
			// draw the result
			// 1. get the vector field
			for (Data data : myView.model.dataStorage) {
				if (data.getType().equals(dragAndDrop.node.getText())) {
					myView.model.resultVectorField = data.getResult();
					// 2. draw the vector field
					// clear side panel
					myView.info.mySidePanel = null;
					myView.display(myView.model.resultVectorField);
				}
			}
		}

	}
	/**
	 * when select homogeneous or boundary vector field
	 * @param node the selected node
	 */
	private void playHomoBoundary(TreeCell node) {
		Double input1 = null;
		Double input2 = null;
		// draw detail panel

		myView.drawDetailPanelHomoBoundary();
		// generate vector fields
		if (!myView.info.textField1.getText().isEmpty()) {
			input1 = Double.parseDouble(myView.info.textField1.getText());
		}
		if (!myView.info.textField2.getText().isEmpty()) {
			input2 = Double.parseDouble(myView.info.textField2.getText());
		}
		if (!myView.info.textField1.getText().isEmpty()) {
			myView.drawPanel(myView.model.vectorfield, input1, input2, null);
		}
	}
	/**
	 * when select tangential or radical vector field
	 * @param node the selected node
	 */
	private void playTanRadical(TreeCell node) {
		Double input1 = null;
		Double input2 = null;
		// draw detail panel

		myView.drawDetailPanelTanRadical();
		// generate vector fields
		if (!myView.info.textField1.getText().isEmpty()) {
			input1 = Double.parseDouble(myView.info.textField1.getText());
		}
		if (!myView.info.textField2.getText().isEmpty()) {
			input2 = Double.parseDouble(myView.info.textField2.getText());
		}
		if (!myView.info.textField1.getText().isEmpty() && !myView.info.textField2.getText().isEmpty()) {
			myView.drawPanel(myView.model.vectorfield, input1, input2, null);
		}
	}
	/**
	 * set cell factory to implement drag and drop function 
	 * @param treeView the tree view needs this function
	 */
	public void setCellFac(TreeView<String> treeView) {
		treeView.setCellFactory(view -> {
			TreeCell<String> cell = new TreeCell<>();
			cell.treeItemProperty().addListener((value, oldValue, newValue) -> {
				if (newValue != null) {
					cell.setText(newValue.getValue());
				} else {
					cell.setText("");
				}
			});
			cell.setOnDragDetected((MouseEvent event) -> dragDetected(event, cell, treeView));
			cell.setOnDragOver((DragEvent event) -> dragOver(event, cell, treeView));
			cell.setOnDragDropped((DragEvent event) -> drop(event, cell, treeView));
			cell.getTreeItem();
			return cell;
		});
	}
	/**
	 * for drop 
	 * @param event the mouse event
	 * @param treeCell the dragged tree cell
	 * @param treeView the tree view dropped on
	 */
	private void drop(DragEvent event, TreeCell treeCell, TreeView treeView) {
		Dragboard db = event.getDragboard();
		boolean success = false;
		if (!db.hasContent(dragAndDrop.JAVA_FORMAT))
			return;
		TreeItem thisItem = treeCell.getTreeItem();
		TreeItem droppedItemParent = dragAndDrop.draggedItem.getParent();
		if (droppedItemParent == myView.result || droppedItemParent.getParent() == myView.result ||droppedItemParent == myView.optIn) {
			// remove from previous location
			droppedItemParent.getChildren().remove(dragAndDrop.draggedItem);
			success = true;
		}
		int indexInParent = thisItem.getParent().getChildren().indexOf(thisItem);
		// when the draggedItem from lib
		if (droppedItemParent == myView.lib) {
			thisItem.getParent().getChildren().add(indexInParent + 1,
					new TreeItem(dragAndDrop.draggedItem.getValue() + "_" + dragAndDrop.index++));
			success = true;
		} else {
			// check redundancy
			boolean flag = false;
			// for all tree items in treeView
			for (TreeItem<String> depNode : myView.optIn.getChildren()) {
				// check if they are draggedItem
				if (depNode.getValue().equals(dragAndDrop.draggedItem.getValue())) {
					flag = true;
					break;
				}
			}
			if (flag == false) {
				thisItem.getParent().getChildren().add(new TreeItem(dragAndDrop.draggedItem.getValue()));
				success = true;
			}
		}
		event.setDropCompleted(success);
	}
	/**
	 * for drag over
	 * @param event the mouse event
	 * @param treeCell the dragged tree cell
	 * @param treeView placeholder for tree view needed
	 */
	private void dragOver(DragEvent event, TreeCell treeCell, TreeView treeView) {
		if (!event.getDragboard().hasContent(dragAndDrop.JAVA_FORMAT))
			return;
		TreeItem thisItem = treeCell.getTreeItem();
		// can't drop on itself or its tree view
		if (dragAndDrop.draggedItem == null || thisItem == null || thisItem == dragAndDrop.draggedItem
				|| dragAndDrop.draggedItem.getParent() == thisItem.getParent())
			return;
		// can't drop on library and operations
		if (thisItem.getParent() == myView.lib || thisItem.getParent() == myView.opt)
			return;
		event.acceptTransferModes(TransferMode.MOVE);
	}
	/**
	 * drag detected 
	 * @param event the mouse event 
	 * @param treeCell the dragged tree cell
	 * @param treeView placeholder for the tree view needed
	 */
	private void dragDetected(MouseEvent event, TreeCell treeCell, TreeView treeView) {
		dragAndDrop.draggedItem = treeCell.getTreeItem();
		// root can't be dragged
		if (dragAndDrop.draggedItem.getParent() == null)
			return;
		Dragboard db = treeCell.startDragAndDrop(TransferMode.MOVE);
		ClipboardContent content = new ClipboardContent();
		content.put(dragAndDrop.JAVA_FORMAT, dragAndDrop.draggedItem.getValue());
		db.setContent(content);
		db.setDragView(treeCell.snapshot(null, null));
		event.consume();
	}
}

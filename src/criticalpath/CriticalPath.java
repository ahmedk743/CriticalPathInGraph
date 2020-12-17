/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package criticalpath;

import java.util.HashMap;
import java.util.Iterator;
import javafx.application.Application;
import javafx.beans.property.DoubleProperty;
import javafx.beans.property.SimpleDoubleProperty;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.geometry.Pos;
import javafx.scene.Cursor;
import javafx.scene.Group;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.control.Button;
import javafx.scene.control.ContextMenu;
import javafx.scene.control.Label;
import javafx.scene.control.MenuItem;
import javafx.scene.control.ScrollPane;
import javafx.scene.control.Slider;
import javafx.scene.control.TextField;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.Pane;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;
import javafx.scene.shape.Line;
import javafx.scene.shape.TriangleMesh;
import javafx.scene.shape.StrokeLineCap;
import javafx.scene.shape.StrokeType;
import javafx.scene.transform.Scale;
import javafx.stage.Stage;

/**
 *
 * @author ideal
 */

public class CriticalPath extends Application {
   GraphicsContext gc;
   public static AnchorPane parent;
   TriangleMesh triangle;
   Group root;
  double orgSceneX, orgSceneY;
//  Line line1;
   public static HashMap<CustomCircle, CustomLine> circles;
//  Circle blueCircle, sCircle;
  public static double mx, my, mx2, my2; // mouse coordinates
  boolean isNodeSelected;
  public static CustomCircle selectedNode;
  public static CustomLine selectedLine;
  public static boolean lineDragged;
  CustomLine deleteLine;
  CustomCircle deleteCircle;
  
  CustomCircle aikCircle;
  Label title, nameLabel, costLabel, zoom;
  TextField nameField, costField; 
  Button setDetails;
  MainGraphView graphView;
  

  private CustomCircle createCircle(double x, double y, double r, Color color) {
    CustomCircle circle = new CustomCircle(x, y, r, color);

    circle.setCursor(Cursor.HAND);

    circle.setOnMousePressed((t) -> {
        if(t.getSceneX()>580){
            orgSceneX = 580;
        }else if(t.getSceneX()<55){
            orgSceneX = 55;
        }else orgSceneX = t.getSceneX();
      
        if(t.getSceneY()>580){
            orgSceneY = 580;
        }else if(t.getSceneY()<55){
            orgSceneY = 55;
        }else orgSceneY = t.getSceneY();
      
      
      CustomCircle c = (CustomCircle) (t.getSource());
      c.toFront();
//      c.setSizes();
      graphView.handleLineDrag(mx, my, selectedNode, selectedLine);
    });
    circle.setOnMouseDragged((t) -> {
      circle.setSizes();
      double offsetX;
      double offsetY;
      if(t.getSceneX()>580){
            offsetX = 580 - orgSceneX;
      }else if(t.getSceneX()<55){
            offsetX = 55 - orgSceneX;
      }else {offsetX = t.getSceneX() - orgSceneX;}
      
      if(t.getSceneY()>580){
            offsetY = 580 - orgSceneY;
      }else if(t.getSceneY()<55){
            offsetY = 55 - orgSceneY;
      }else offsetY = t.getSceneY() - orgSceneY;
      

      CustomCircle c = (CustomCircle) (t.getSource());

      c.setCenterX(c.getCenterX() + offsetX);
      c.setCenterY(c.getCenterY() + offsetY);

      if(t.getSceneX()>580){
            orgSceneX = 580;
      }else if(t.getSceneX()<55){
            orgSceneX = 55;
      }else orgSceneX = t.getSceneX();
      
      if(t.getSceneY()>580){
            orgSceneY = 580;
        }else if(t.getSceneY()<55){
            orgSceneY = 55;
        }else orgSceneY = t.getSceneY();
      
      
      graphView.handleLineDrag(mx, my, selectedNode, selectedLine);
    });
    return circle;
  }

  private Line connect(CustomCircle c1, CustomCircle c2) {
    CustomLine line = new CustomLine();

    line.startXProperty().bind(c1.centerXProperty());
    line.startYProperty().bind(c1.centerYProperty());

    line.endXProperty().bind(c2.centerXProperty());
    line.endYProperty().bind(c2.centerYProperty());

    line.setStrokeWidth(1);
    line.setStrokeLineCap(StrokeLineCap.BUTT);
    line.getStrokeDashArray().setAll(1.0, 4.0);

    return line;
  }
  private double dist(double x1, double y1, double x2, double y2) {
        return Math.sqrt(Math.pow(x2-x1,2) + Math.pow(y2-y1,2));
    }
  private void handleMoved(MouseEvent mouseEvent) {
        mx = mouseEvent.getX();
        my = mouseEvent.getY();
        lineDragged = true;
        graphView.handleLineDrag(mx, my, selectedNode, selectedLine);
//        draw();
    }

    @Override
    public void start(Stage primaryStage) {
        AnchorPane sidePane = new AnchorPane();
        nameField = new TextField();
        costField = new TextField();
        sidePane.setPrefSize(200, 900);
//        sidePane.translateXProperty();
        AnchorPane home = new AnchorPane();
        VBox vb = new VBox(sidePane);
        root = new Group();
//        vb.setTranslateX(600);
        vb.setStyle("-fx-background-color:lightgray;");
        
        Canvas myCanvas = new Canvas(600,600);
        
        gc = myCanvas.getGraphicsContext2D();
        parent = new AnchorPane(myCanvas);
        circles = new HashMap<>();
        lineDragged = false;

//        selectedNode = null;
        isNodeSelected = false;
        graphView = new MainGraphView();
//        graphView.drawGraph(mx, my);
        Slider slider = new Slider(0.5, 2.0, 0.25);
      slider.setShowTickLabels(true);
      slider.setShowTickMarks(true);
      slider.setMajorTickUnit(0.25);
      slider.setBlockIncrement(0.25);
//      //Setting the width of the slider
      slider.setMaxWidth(350);
      slider.setTranslateX(10);
      slider.setTranslateY(250);
      ZoomingPane zoomingPane = new ZoomingPane(parent);
      zoomingPane.zoomFactorProperty().bind(slider.valueProperty());
      ScrollPane s1 = new ScrollPane(zoomingPane);
//        s1.setPrefSize(600, 600);
        s1.setPannable(true);
        s1.setTranslateX(200);
        root.getChildren().addAll(s1,vb);
        Scene scene = new Scene(root, 800, 600);
        myCanvas.setOnMouseDragged(this::handleMoved);
        
        circles.put(createCircle(190, 160, 40, Color.YELLOWGREEN), new CustomLine());
        circles.forEach((c, l)->{
            c.getName().setText("Start");
            c.getCost().setText("");
            parent.getChildren().addAll(c, c.getName(), c.getCost());
            
        });

        final ContextMenu contextMenu = new ContextMenu();
        MenuItem addNode = new MenuItem("Add Node");
        MenuItem delete = new MenuItem("Delete");

        contextMenu.getItems().add(addNode); 
        contextMenu.getItems().add(delete); 

    //        myCanvas.setOnMousePre
        
        root.setOnMousePressed((MouseEvent event) -> {
            if (event.isSecondaryButtonDown()) {
                contextMenu.show(root, event.getScreenX(), event.getScreenY());
                for (HashMap.Entry mapElem : circles.entrySet()){
                deleteCircle = (CustomCircle) mapElem.getKey();
                deleteLine = (CustomLine) mapElem.getValue();
                    for(int i = 0; i < deleteCircle.getIncidentEdge().size(); i++){
                        if(event.getPickResult().getIntersectedNode() == deleteCircle.getIncidentEdge().get(i)){
                            deleteCircle.getIncidentEdge().get(i).setStroke(Color.RED);
                            deleteCircle.getIncidentEdge().get(i).setStrokeWidth(5);
                        }
                    }
                }
                   
            }
            
            addNode.setOnAction(action -> {
                CustomCircle temp = createCircle(event.getScreenX()-230, event.getScreenY()-70, 40, Color.BLUE);
                CustomLine l = new CustomLine();
                circles.put(temp, l);
    //            CustomLine tempL = connect(c, ci);
    //                        templ.setIsBound(true);
    //                        circles.put(c, tempL);
                parent.getChildren().addAll(temp, temp.getName(), temp.getCost());
                temp.toFront();
    //            root.getChildren().add(l);
//                temp.toFront();
                
                graphView.handleLineDrag(mx, my, selectedNode, selectedLine);

            });
        });


        root.addEventHandler(MouseEvent.MOUSE_PRESSED, event -> {
            circles.forEach((circle, line)->{
                if (event.isPrimaryButtonDown()) {
                    if(event.getPickResult().getIntersectedNode() == circle){
                        selectedNode = circle;
                        selectedLine = line;
                        nameField.setText(circle.getName().getText());
                        costField.setText(circle.getCost().getText());
    //                    selectedNode.getPoitner().setOnMouseDragged(this::handleMoved);
                    }
                    else{
                        
                    }
                }
            });
        });
        root.addEventHandler(MouseEvent.MOUSE_PRESSED, event -> {
    //        contextMenu2.hide();
            contextMenu.hide();
        });

        title = new Label("NODE DETAILS:");
        title.setTranslateX(10);
        title.setTranslateY(50);
        title.setStyle("-fx-font-style:bold; -fx-font-size:20;");
        
        nameLabel = new Label("Name:");
        nameLabel.setTranslateX(10);
        nameLabel.setTranslateY(80);
        
        
        nameField.setTranslateX(10);
        nameField.setTranslateY(100);
        
        costLabel = new Label("Cost:");
        costLabel.setTranslateX(10);
        costLabel.setTranslateY(130);
        
        
        costField.setTranslateX(10);
        costField.setTranslateY(150);
        
        setDetails = new Button();
        setDetails.setText("Set Details");
        setDetails.setTranslateX(10);
        setDetails.setTranslateY(180);
        
        zoom = new Label("Zoom:");
        zoom.setTranslateX(10);
        zoom.setTranslateY(230);
        
        setDetails.setOnAction(action -> {
           if(selectedNode!=null){
//               circles.forEach((circle, line)->{
//                  if(selectedNode == circle){
                      selectedNode.getName().setText(nameField.getText());
                      selectedNode.getCost().setText(costField.getText());
                      circles.put(selectedNode, selectedLine);
//                  } 
//               });
           }

        });
        
        

        
      sidePane.getChildren().setAll(title, nameLabel, nameField, costLabel, costField, setDetails, slider, zoom);

        lineDragged = true;
        graphView.handleLineDrag(mx, my, selectedNode, selectedLine);
        
        primaryStage.setTitle("Critical Path");
        primaryStage.setScene(scene);
        primaryStage.show();
    }

    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) {
        launch(args);
    }
    
}

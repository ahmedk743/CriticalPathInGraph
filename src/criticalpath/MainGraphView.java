/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package criticalpath;

import static criticalpath.CriticalPath.circles;
import static criticalpath.CriticalPath.selectedNode;
import java.util.HashMap;
import java.util.Iterator;
import javafx.beans.InvalidationListener;
import javafx.beans.property.DoubleProperty;
import javafx.scene.Cursor;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.AnchorPane;
import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;
import javafx.scene.shape.Line;
import javafx.scene.shape.StrokeLineCap;
import javafx.scene.shape.StrokeType;

/**
 *
 * @author ideal
 */
public class MainGraphView {
    Graph graph;
    AnchorPane view = CriticalPath.parent;
    boolean lineDragged;
    double mx2, my2;
    Circle oval;
    CustomCircle selectedNode;
    CustomLine selectedLine;
    Line line;
    
    
    public MainGraphView(){
        graph = new Graph();
        oval = null;
        line = null;
        selectedNode = CriticalPath.selectedNode;
        selectedLine = CriticalPath.selectedLine;
    }
    public void drawGraph(double mx, double my){
        selectedNode = CriticalPath.selectedNode;
        selectedLine = CriticalPath.selectedLine;
        HashMap<CustomCircle, CustomLine> circles = CriticalPath.circles;
        for (HashMap.Entry mapElem : circles.entrySet()) {
            CustomCircle ci = (CustomCircle) mapElem.getKey();
            CustomLine li = (CustomLine) mapElem.getValue();
            double cx = ci.getCenterX();
            double cy = ci.getCenterY();
            double r = ci.getRadius();
            double distance = dist(cx,cy,mx,my);
            double nearestX = cx + r * (mx - cx) / distance;
            double nearestY = cy + r * (my - cy) / distance;
            ci.toFront();
            ci.getName().toFront();
            ci.getCost().toFront();
            if(ci!=selectedNode){
                ci.setFill(Color.YELLOWGREEN);
                ci.setStrokeWidth(0);
            }
            
            int size = ci.getIncidentEdge().size();
            
            for(int i = 0; i < size; i++){
                
                ci.getIncidentEdge().get(i).showArrows();
                if(ci.getIncidentEdge().get(i)!=null){
                   ci.getIncidentEdge().get(i).toBack();
                   CriticalPath.parent.getChildren().removeAll(ci.getIncidentEdge().get(i), ci.getIncidentEdge().get(i).getArrow1(), ci.getIncidentEdge().get(i).getArrow2());
//                    li.getIncidentEdge().get(i).setEndX(nearestX);
//                    li.getIncidentEdge().get(i).setEndY(nearestY);
//                    li.setEndX(mx); li.setEndY(my);
                    
                    
                
                
                }
//                ci.getIncidentEdge().get(i).setArrow1EndX(nearestX);
//                ci.getIncidentEdge().get(i).setArrow2EndX(nearestX);
                ci.getIncidentEdge().get(i).setStroke(Color.BLACK);
                ci.getIncidentEdge().get(i).setStrokeWidth(5);
//                li.toBack();
                CriticalPath.parent.getChildren().addAll(ci.getIncidentEdge().get(i), ci.getIncidentEdge().get(i).getArrow1(), ci.getIncidentEdge().get(i).getArrow2());
                
            }
        }
//        System.out.println(graph.toString());
    }
    public void handleLineDrag(double mx, double my, CustomCircle selectedNode, CustomLine selectedLine){
        lineDragged = CriticalPath.lineDragged;
        if(selectedNode!=null){
            selectedNode.setFill(Color.LIGHTGREEN);
            selectedNode.setStrokeWidth(1);
            selectedNode.getStrokeDashArray().addAll(5d);
            selectedNode.setStrokeMiterLimit(10);   
            selectedNode.setStrokeType(StrokeType.INSIDE);
            selectedNode.setStroke(Color.BLACK);
            
            double cx = selectedNode.getCenterX();
            double cy = selectedNode.getCenterY();
            double r = selectedNode.getRadius();
            double distance = dist(cx,cy,mx,my);
            double nearestX = cx + r * (mx - cx) / distance;
            double nearestY = cy + r * (my - cy) / distance;
            
//            if(oval!=null) view.getChildren().remove(oval);
//            oval = new Circle(nearestX-5,nearestY-5,10,Color.GOLD);
//            oval.setStroke(Color.BLACK);
//            oval.toBack();
    //        oval.st.setStroke(nearestX-5,nearestY-5,10,10);
//            view.getChildren().add(oval);
            
            if(line!=null) CriticalPath.parent.getChildren().remove(line);
            if(lineDragged){
                if(mx!=0 && my!=0){
                    line = new Line(cx, cy,mx,my);
                    line.setStrokeWidth(5);
                    line.setFill(Color.RED);
                    CriticalPath.parent.getChildren().add(line);
                }
//                ArrayList<Line> lineArr = new ArrayList<>();
//                CriticalPath.circles.put(selectedNode, new CustomLine(cx, cy,mx,my));
    //            mx2 = mx; my2 = my;
            }
//            else{
//                mx2 = selectedLine.getEndX();
//                my2 = selectedLine.getEndY();
//                if(mx2!=0 && my2!=0){
//                    Line line = new Line(cx, cy,mx,my);
//                    line.setFill(Color.RED);
//                    view.getChildren().add(line);
//                    
//                CriticalPath.circles.put(selectedNode, new CustomLine(cx, cy,mx2, my2));
//                }
//            }
            this.drawGraph(mx, my);
            for (HashMap.Entry mapElem : circles.entrySet()){
            CustomCircle ci = (CustomCircle) mapElem.getKey();
//            ci.toFront(); 
            ci.getName().toFront(); ci.getCost().toFront();
            CustomLine li = (CustomLine) mapElem.getValue();
            double cx2 = ci.getCenterX();
            double cy2 = ci.getCenterY();
            double r2 = ci.getRadius();
            double distance2 = dist(cx2,cy2,mx,my);
            double nearestX2 = cx2 + r2 * (mx - cx2) / distance2;
            double nearestY2 = cy2 + r2 * (my - cy2) / distance2;
//            for(int i = 0; i < selectedLine.getLeavingEdge().size(); i++){
                if(ci.intersects(mx, mx, mx, mx) /*&& !li.getIncidentEdge().contains(selectedLine.getLeavingEdge().get(i))*/){
                    CustomLine temp;
                    temp = connect(selectedNode, ci);
//                    temp.toBack();
                    if(!ci.getIncidentEdge().contains(temp)){
                        ci.getIncidentEdge().add(temp);
                        
                        if(!li.getIsBound()){
                            graph.addEdge(selectedNode, ci, false);
                            li.setIsBound(true);
                        }
                        
                        CriticalPath.circles.put(ci, li);
                    }
    //                temp.setIsBound(true);
//                    Line temp2 = new Line();
//                    if(!li.getLeavingEdge().contains(temp2)){ li.getLeavingEdge().add(temp2);
//                    CriticalPath.circles.put(selectedNode, li);}
//                    break;
                }
//                else{
//                    view.getChildren().remove(line);
//                    break;
//                }
//            }
        }

        }
        CriticalPath.lineDragged = false;
    }
    private double dist(double x1, double y1, double x2, double y2) {
        return Math.sqrt(Math.pow(x2-x1,2) + Math.pow(y2-y1,2));
    }
    private CustomLine connect(CustomCircle c1, CustomCircle c2) {
        CustomLine line = new CustomLine();
//        double mx = CriticalPath.mx;
//        double my = CriticalPath.my;
        line.setCursor(Cursor.HAND);
//        double cx2 = c2.getCenterX();
//        double cy2 = c2.getCenterY();
//        double cx = c1.getCenterX();
//        double cy = c1.getCenterY();
//        double r2 = c2.getRadius();
//        double distance2 = dist(cx2,cy2,cx,cy);
//        double nearestX2 = cx2 + r2 * (cx - cx2) / distance2;
//        double nearestY2 = cy2 + r2 * (cy - cy2) / distance2;
        
        line.startXProperty().bind(c1.centerXProperty());
        line.startYProperty().bind(c1.centerYProperty());

        line.endXProperty().bind(c2.centerXProperty());
        line.endYProperty().bind(c2.centerYProperty());
                
//        line.endXProperty().bind((c2.centerXProperty().subtract(cx2)).add(nearestX2));
//        line.endYProperty().bind((c2.centerYProperty().subtract(cy2)).add(nearestY2));

//        line.setFill(Color.RED);
        line.setStrokeWidth(5);
//        line.setStrokeLineCap(StrokeLineCap.SQUARE);
//        line.setStrokeLineCap(StrokeLineCap.BUTT);
//        line.getStrokeDashArray().setAll(1.0, 4.0);
//        line.setOnMousePressed((MouseEvent event) -> {
//            if (event.isPrimaryButtonDown()) {
//                line.setStroke(Color.RED);
//                line.setStrokeWidth(5);
////                line.setStrokeLineCap(StrokeLineCap.BUTT);
////                line.getStrokeDashArray().setAll(1.0, 4.0);
//                line.addEventHandler(KeyEvent.KEY_PRESSED, ev->{
//                    if(ev.getCode() == KeyCode.D){
////                        selectedLine.getIncidentEdge().remove(line);
//                        CriticalPath.parent.getChildren().remove(line);
//                        System.out.println("delete");
//                    }
//                });
//                
//            }
////            else{
//////                line.setStrokeLineCap(StrokeLineCap.SQUARE);
////            }
//        });
//        line.toFront();
        return line;
  }
}

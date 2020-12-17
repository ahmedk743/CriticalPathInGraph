/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package criticalpath;

import java.util.ArrayList;
import javafx.scene.control.Label;
import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;
import javafx.scene.shape.Line;

/**
 *
 * @author ideal
 */
public class CustomCircle extends Circle {
    Circle poitner;
    ArrayList<CustomLine> incidentEdge;
    ArrayList<CustomLine> leavingEdge;
    public ArrayList<CustomLine> getIncidentEdge() {
        return incidentEdge;
    }

    public void addToIncidentEdge(CustomLine incidentEdge) {
        this.incidentEdge.add(incidentEdge);
    }

    public ArrayList<CustomLine> getLeavingEdge() {
        return leavingEdge;
    }

    public void addToLeavingEdge(CustomLine leavingEdge) {
        this.leavingEdge.add(leavingEdge); 
    }
    public Label getName() {
        return name;
    }

    public void setName(Label name) {
        this.name = name;
    }

    public Label getCost() {
        return cost;
    }

    public void setCost(Label cost) {
        this.cost = cost;
    }
    Label name, cost;
    public void setSizes(){
        this.name.setTranslateX(this.getCenterX()-30);
        this.name.setTranslateY(this.getCenterY()-20);
        
        this.cost.setTranslateX(this.getCenterX()-10);
        this.cost.setTranslateY(this.getCenterY());
    }
    public CustomCircle() {
        super();
        name = new Label("New Activity");
        name.setTextFill(Color.WHITE);
        cost = new Label("0.0");
        cost.setTextFill(Color.WHITE);
        this.setSizes();
        incidentEdge = new ArrayList<>();
        leavingEdge = new ArrayList<>();
    }
    public CustomCircle(double x1, double x2, double r, Color color){
        super(x1, x2, r, color); 
        name = new Label("New Activity");
        name.setTextFill(Color.WHITE);
        cost = new Label("0.0");
        cost.setTextFill(Color.WHITE);
        this.setSizes();
        incidentEdge = new ArrayList<>();
        leavingEdge = new ArrayList<>();
    }

    public Circle getPoitner() {
        return poitner;
    }

    public void setPoitner(Circle poitner) {
        this.poitner = poitner;
    }
    
}

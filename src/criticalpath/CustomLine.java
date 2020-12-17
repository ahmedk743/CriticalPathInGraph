/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package criticalpath;

import javafx.scene.shape.Line;
import java.util.ArrayList;
import javafx.beans.InvalidationListener;

/**
 *
 * @author ideal
 */
public class CustomLine extends Line {
    boolean isBound;
    Line arrow1 , arrow2;
    private static final double arrowLength = 20;
    private static final double arrowWidth = 7;
    public CustomLine(){
        super();
        isBound = false;
        arrow1 = new Line();
        arrow2 = new Line();
    }

    public CustomLine(double x1, double x2, double y1, double y2){
        super(x1, x2, y1, y2);
        InvalidationListener updater = o -> {
            double ex = getEndX();
            double ey = getEndY();
            double sx = getStartX();
            double sy = getStartY();

            arrow1.setEndX(ex-30);
            arrow1.setEndY(ey);
            arrow2.setEndX(ex-40);
            arrow2.setEndY(ey);

            if (ex == sx && ey == sy) {
                // arrow parts of length 0
                arrow1.setStartX(ex);
                arrow1.setStartY(ey);
                arrow2.setStartX(ex);
                arrow2.setStartY(ey);
            } else {
                double factor = arrowLength / Math.hypot(sx-ex, sy-ey);
                double factorO = arrowWidth / Math.hypot(sx-ex, sy-ey);

                // part in direction of main line
                double dx = (sx - ex) * factor;
                double dy = (sy - ey) * factor;

                // part ortogonal to main line
                double ox = (sx - ex) * factorO;
                double oy = (sy - ey) * factorO;

                arrow1.setStartX(ex + dx - oy -40);
                arrow1.setStartY(ey + dy + ox);
                arrow2.setStartX(ex + dx + oy -40);
                arrow2.setStartY(ey + dy - ox);
            }
        };

        // add updater to properties
        startXProperty().addListener(updater);
        startYProperty().addListener(updater);
        endXProperty().addListener(updater);
        endYProperty().addListener(updater);
        updater.invalidated(null);

    }

    public boolean getIsBound() {
        return isBound;
    }

    public void setIsBound(boolean isBound) {
        this.isBound = isBound;
    }
    
    public void setArrow1EndX(double x){
        arrow1.setEndX(x);
    }
    public void setArrow1EndY(double y){
        arrow1.setEndY(y);
    }
    public void setArrow2EndX(double x){
        arrow1.setEndX(x);
    }
    public void setArrow2EndY(double y){
        arrow2.setEndY(y);
    }
    public void setArrow1StartX(double x){
        arrow1.setStartX(x);
    }
    public void setArrow2StartY(double y){
        arrow2.setStartY(y);
    }
    
    
    public Line getArrow1(){
        return arrow1;
    }
    public Line getArrow2(){
        return arrow2;
    }
    public void showArrows(){
        double lineDist = dist(getStartX(), getStartY(),getEndX(),getEndY());
        double ex = getEndX();
        double ey = getEndY();
        double sx = getStartX();
        double sy = getStartY();

        arrow1.setEndX(ex);
        arrow1.setEndY(ey);
        arrow2.setEndX(ex);
        arrow2.setEndY(ey);

        if (ex == sx && ey == sy) {
            // arrow parts of length 0
            arrow1.setStartX(ex);
            arrow1.setStartY(ey);
            arrow2.setStartX(ex);
            arrow2.setStartY(ey);
        } else {
            double factor = arrowLength / Math.hypot(sx-ex, sy-ey);
            double factorO = arrowWidth / Math.hypot(sx-ex, sy-ey);

            // part in direction of main line
            double dx = (sx - ex) * factor;
            double dy = (sy - ey) * factor;

            // part ortogonal to main line
            double ox = (sx - ex) * factorO;
            double oy = (sy - ey) * factorO;

            arrow1.setStartX(ex + dx - oy);
            arrow1.setStartY(ey + dy + ox);
            arrow2.setStartX(ex + dx + oy);
            arrow2.setStartY(ey + dy - ox);
        }
    }
    private double dist(double x1, double y1, double x2, double y2) {
        return Math.sqrt(Math.pow(x2-x1,2) + Math.pow(y2-y1,2));
    }
}


/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package criticalpath;

import javafx.beans.property.DoubleProperty;
import javafx.beans.property.SimpleDoubleProperty;
import javafx.beans.value.ObservableValue;
import javafx.geometry.Pos;
import javafx.scene.Node;
import javafx.scene.layout.Pane;
import javafx.scene.transform.Scale;

/**
 *
 * @author ideal
 */
class ZoomingPane extends Pane {
        Node content;
        private final DoubleProperty zoomFactor = new SimpleDoubleProperty(1);

        ZoomingPane(Node content) {
            this.content = content;
            getChildren().add(content);
            Scale scale = new Scale(1, 1);
            content.getTransforms().add(scale);

            zoomFactor.addListener((ObservableValue<? extends Number> observable, Number oldValue, Number newValue) -> {
                scale.setX(newValue.doubleValue());
                scale.setY(newValue.doubleValue());
                requestLayout();
            });
        }

        @Override
        protected void layoutChildren() {
            Pos pos = Pos.CENTER;
            double width = getWidth();
            double height = getHeight();
            double top = getInsets().getTop();
            double right = getInsets().getRight();
            double left = getInsets().getLeft();
            double bottom = getInsets().getBottom();
            double contentWidth = (width - left - right)/zoomFactor.get();
            double contentHeight = (height - top - bottom)/zoomFactor.get();
            layoutInArea(content, left, top,
                    contentWidth, contentHeight,
                    0, null,
                    pos.getHpos(),
                    pos.getVpos());
        }

        public final Double getZoomFactor() {
            return zoomFactor.get();
        }
        public final void setZoomFactor(Double zoomFactor) {
            this.zoomFactor.set(zoomFactor);
        }
        public final DoubleProperty zoomFactorProperty() {
            return zoomFactor;
        }
    }

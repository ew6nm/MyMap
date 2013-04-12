/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package MyMap;

import java.net.URL;
import java.util.ResourceBundle;
import javafx.beans.property.ReadOnlyDoubleProperty;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.concurrent.Worker;
import javafx.event.ActionEvent;
import javafx.event.Event;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.TextField;
import javafx.scene.web.WebEngine;
import javafx.scene.web.WebView;
import netscape.javascript.JSObject;

/**
 * FXML Controller class
 *
 * @author user
 */
public class MainFrmController implements Initializable {

    private JSObject doc;
    private EventHandler<MapEvent> onMapLatLngChanged;
    private boolean ready;
    @FXML
    private TextField txt1;
    @FXML
    private WebView ww1;
    @FXML
    private WebEngine we1;

    @FXML
    protected void btn1fired(ActionEvent event) {
        txt1.setText("Тестирование ОК");
    }

    protected void btn2fired(ActionEvent event) {
    }

    private void initMap() {

        we1 = ww1.getEngine();
        we1.load(getClass().getResource("map.html").toExternalForm());
        ready = false;
        we1.getLoadWorker().stateProperty().addListener(new ChangeListener<Worker.State>() {
            @Override
            public void changed(final ObservableValue<? extends Worker.State> observableValue,
                    final Worker.State oldState,
                    final Worker.State newState) {
                if (newState == Worker.State.SUCCEEDED) {
                    ready = true;
                }
            }
        });
    }

    private void initCommunication() {
        we1.getLoadWorker().stateProperty().addListener(new ChangeListener<Worker.State>() {
            @Override
            public void changed(final ObservableValue<? extends Worker.State> observableValue,
                    final Worker.State oldState,
                    final Worker.State newState) {
                if (newState == Worker.State.SUCCEEDED) {
                    doc = (JSObject) we1.executeScript("window");
                    doc.setMember("app", MainFrmController.this);
                }
            }
        });
    }

    private void invokeJS(final String str) {
        if (ready) {
            doc.eval(str);
        } else {
            we1.getLoadWorker().stateProperty().addListener(new ChangeListener<Worker.State>() {
                @Override
                public void changed(final ObservableValue<? extends Worker.State> observableValue,
                        final Worker.State oldState,
                        final Worker.State newState) {
                    if (newState == Worker.State.SUCCEEDED) {
                        doc.eval(str);
                    }
                }
            });
        }
    }

    public void setOnMapLatLngChanged(EventHandler<MapEvent> eventHandler) {
        onMapLatLngChanged = eventHandler;
    }

    public void handle(double lat, double lng) {
        if (onMapLatLngChanged != null) {
            MapEvent event = new MapEvent(this, lat, lng);
            onMapLatLngChanged.handle(event);
        }
    }

    public void setMarkerPosition(double lat, double lng) {
        String sLat = Double.toString(lat);
        String sLng = Double.toString(lng);
        invokeJS("setMarkerPosition(" + sLat + ", " + sLng + ")");
    }

    public void setMapCenter(double lat, double lng) {
        String sLat = Double.toString(lat);
        String sLng = Double.toString(lng);
        invokeJS("setMapCenter(" + sLat + ", " + sLng + ")");
    }

    public void switchSatellite() {
        invokeJS("switchSatellite()");
    }

    public void switchRoadmap() {
        invokeJS("switchRoadmap()");
    }

    public void switchHybrid() {
        invokeJS("switchHybrid()");
    }

    public void switchTerrain() {
        invokeJS("switchTerrain()");
    }

    public void startJumping() {
        invokeJS("startJumping()");
    }

    public void stopJumping() {
        invokeJS("stopJumping()");
    }

    public void setHeight(double h) {
        ww1.setPrefHeight(h);
    }

    public void setWidth(double w) {
        ww1.setPrefWidth(w);
    }

    public ReadOnlyDoubleProperty widthProperty() {
        return ww1.widthProperty();
    }

    /**
     * Initializes the controller class.
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        initMap(); // TODO

    }
}

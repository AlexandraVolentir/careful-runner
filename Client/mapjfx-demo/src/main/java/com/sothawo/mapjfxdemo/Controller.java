package com.sothawo.mapjfxdemo;

import com.sothawo.mapjfx.*;
import com.sothawo.mapjfx.event.MapViewEvent;
import javafx.fxml.FXML;

import javafx.scene.control.Accordion;
import javafx.scene.control.Button;
import javafx.scene.control.TitledPane;

import javafx.scene.paint.Color;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.*;

import com.opencsv.CSVWriter;

import java.net.Socket;
import java.net.URL;
import java.nio.charset.StandardCharsets;
import java.util.Optional;
import java.util.stream.Collectors;
import java.util.stream.Stream;


import java.io.IOException;
/**
 * Controller for the FXML defined code.
 */
public class Controller {

    CSVWriter writer;

    /**
     * logger for the class.
     */
    private static final Logger logger = LoggerFactory.getLogger(Controller.class);

    /**
     * some coordinates from around town.
     */
    private static final Coordinate coordBeverlyHills = new Coordinate(34.073620, -118.400352);
    private int count = 1;
    private boolean trackActivated;

    private static final Coordinate coordRodeoBeverlyHills = new Coordinate(34.073490, -118.407830);
    private static final Coordinate coordTheBeverlyHillsHotel = new Coordinate(34.081436, -118.413692);
    private static final Coordinate coordGreystoneMansion = new Coordinate(34.091944, -118.401667);
    private static final Coordinate coordLaBreaTarPits = new Coordinate(34.0628, -118.356);


    /**
     * default zoom value.
     */
    private static final int ZOOM_DEFAULT = 14;

    /**
     * the markers.
     */
    private final Marker markerRodeo;
    private final Marker markerHotel;
    private final Marker markerMansion;
    private final Marker markerLaBreaTarPits;

    /**
     * the labels.
     */
    private final MapLabel labelRode;
    private final MapLabel labelHotel;
    private final MapLabel labelMansion;
    private final MapLabel labelLaBreaTarPits;

    private final int PORT = 2222;
    private Socket socketDescriptor;
    private PrintWriter writerToSocket;
    private BufferedReader reader;
    private String globalMessage;

    /**
     * the MapView containing the map
     */
    @FXML
    private MapView mapView;

    /**
     * Accordion for all the different options
     */
    @FXML
    private Accordion rightControls;

    /**
     * section containing the location button
     */
    @FXML
    private TitledPane optionsLocations;

    /**
     * button to set the map's center
     */
    @FXML
    private Button buttonRodeo;

    /**
     * button to set the map's center
     */
    @FXML
    private Button buttonHotel;

    /**
     * button to set the map's center
     */
    @FXML
    private Button buttonMansion;

    /**
     * button to set the map's center
     */
    @FXML
    private Button buttonLaBreaTarPits;

    /**
     * the first CoordinateLine
     */
    private CoordinateLine trackMagenta1;

    /**
     * params for the WMS server.
     */
    private WMSParam wmsParam = new WMSParam().setUrl("http://ows.terrestris.de/osm/service?").addParam("layers", "OSM-WMS");

    /**
     * controller constructor
     * initialize a a couple of markers using the provided ones
     * the fix label, default style
     * the attached labels, custom style
     */
    public Controller() {

        trackActivated = false;

        markerRodeo = Marker.createProvided(Marker.Provided.BLUE).setPosition(coordRodeoBeverlyHills).setVisible(true);
        markerHotel = Marker.createProvided(Marker.Provided.GREEN).setPosition(coordTheBeverlyHillsHotel).setVisible(true);
        markerMansion = Marker.createProvided(Marker.Provided.RED).setPosition(coordGreystoneMansion).setVisible(true);
        markerLaBreaTarPits = Marker.createProvided(Marker.Provided.ORANGE).setPosition(coordLaBreaTarPits).setVisible(true);
        globalMessage = "";

        labelHotel = new MapLabel("Hotel Beverly Hills", 10, -10).setVisible(true).setCssClass("green-label");
        labelMansion = new MapLabel("Mansion", 10, -10).setVisible(true).setCssClass("red-label");
        labelRode = new MapLabel("Rodeo Beverly Hills", 10, -10).setVisible(true).setCssClass("blue-label");
        labelLaBreaTarPits = new MapLabel("La Brea Tar Pits", 10, -10).setVisible(true).setCssClass("orange-label");

        markerHotel.attachLabel(labelHotel);
        markerMansion.attachLabel(labelMansion);
        markerRodeo.attachLabel(labelRode);
        markerLaBreaTarPits.attachLabel(labelLaBreaTarPits);

        try {
            socketDescriptor = new Socket("localhost", PORT);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        try {
            writerToSocket = new PrintWriter(socketDescriptor.getOutputStream(), true);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        try {
            reader = new BufferedReader(new InputStreamReader(socketDescriptor.getInputStream()));
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    public void getRouteFromServer(String message, Coordinate coordinate){
        mapView.setCenter(coordinate);
        System.out.println("message:" + message);
        sentRequestToServer(message);
        receiveRouteFromServer();
    }

    private void sentRequestToServer(String message) {
        writerToSocket.println(message);
        writerToSocket.flush();
    }

    private void receiveRouteFromServer() {
        try {
            globalMessage = reader.readLine();
            URL url = new URL(globalMessage.trim());
            if(trackActivated) {
                trackMagenta1.setVisible(false);
                mapView.addCoordinateLine(trackMagenta1);
            } else {
                trackActivated = true;
            }
            trackMagenta1 = loadCoordinateLine(url).orElse(new CoordinateLine()).setColor(Color.MAGENTA);
            trackMagenta1.setVisible(true);
            mapView.addCoordinateLine(trackMagenta1);
            System.out.println(trackMagenta1.visibleProperty());
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        System.out.println(globalMessage);
    }
    /**
     * called after the fxml is loaded and all objects are created. This is not called initialize any more,
     * because we need to pass in the projection before initializing.
     *
     * - set the custom css file for the MapView
     * - set the controls to disabled, this will be changed when the MapView is intialized
     * - finally initialize the map view
     * - wire up the location buttons
     * - watch the MapView's initialized property to finish initialization
     * @param projection
     *     the projection to use in the map.
     */
    public void initMapAndControls(Projection projection) {
        logger.trace("begin initialize");
        mapView.setCustomMapviewCssURL(getClass().getResource("/custom_mapview.css"));

        rightControls.setExpandedPane(optionsLocations);
        setControlsDisable(true);

        buttonRodeo.setOnAction(event -> getRouteFromServer("RodeoBeverlyHills", coordRodeoBeverlyHills));
        buttonHotel.setOnAction(event -> getRouteFromServer("Hotel", coordTheBeverlyHillsHotel));
        buttonMansion.setOnAction(event -> getRouteFromServer("Mansion", coordGreystoneMansion));
        buttonLaBreaTarPits.setOnAction(event -> getRouteFromServer("Brea", coordLaBreaTarPits));


        logger.trace("location buttons done");

        mapView.initializedProperty().addListener((observable, oldValue, newValue) -> {
            if (newValue) {
                afterMapIsInitialized();
            }
        });

        mapView.setMapType(MapType.OSM);

        setupEventHandlers();
        mapView.initialize(Configuration.builder().projection(projection).showZoomControls(false).build());
    }

    /**
     * add an event handler for MapViewEvent#MAP_EXTENT and set the extent in the map
     * initializes the event handlers.
     */
    private void setupEventHandlers() {
        mapView.addEventHandler(MapViewEvent.MAP_EXTENT, event -> {
            event.consume();
            mapView.setExtent(event.getExtent());
        });
    }
    /**
     * enables / disables the different controls
     *
     * @param flag
     *     if true the controls are disabled
     */
    private void setControlsDisable(boolean flag) {
        rightControls.setDisable(flag);
    }

    /**
     * finishes setup after the mpa is initialzed
     * tart at the harbour with default zoom
     * add the markers to the map - they are still invisible
     */
    private void afterMapIsInitialized() {
        logger.trace("map intialized");
        logger.debug("setting center and enabling controls...");

        mapView.setZoom(ZOOM_DEFAULT);
        mapView.setCenter(coordBeverlyHills);

        mapView.addMarker(markerRodeo);
        mapView.addMarker(markerHotel);
        mapView.addMarker(markerMansion);
        mapView.addMarker(markerLaBreaTarPits);

        setControlsDisable(false);
    }

    /**
     * load a coordinateLine from the given uri in lat;lon csv format
     *
     * @param url
     *     url where to load from
     * @return optional CoordinateLine object
     * @throws java.lang.NullPointerException
     *     if uri is null
     */
    private Optional<CoordinateLine> loadCoordinateLine(URL url) {
        try (
            Stream<String> lines = new BufferedReader(
                new InputStreamReader(url.openStream(), StandardCharsets.UTF_8)).lines()
        ) {
            return Optional.of(new CoordinateLine(
                lines.map(line -> line.split(";")).filter(array -> array.length == 2)
                    .map(values -> new Coordinate(Double.valueOf(values[0]), Double.valueOf(values[1])))
                    .collect(Collectors.toList())));
        } catch (IOException | NumberFormatException e) {
            logger.error("load {}", url, e);
        }
        return Optional.empty();
    }
}

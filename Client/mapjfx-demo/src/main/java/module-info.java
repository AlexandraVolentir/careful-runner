
module com.sothawo.mapjfxdemo {
    requires com.sothawo.mapjfx;
    requires javafx.controls;
    requires javafx.fxml;
    requires javafx.graphics;
    requires org.slf4j;
    requires opencsv;
    requires java.desktop;

    opens com.sothawo.mapjfxdemo to javafx.fxml, javafx.graphics;
}

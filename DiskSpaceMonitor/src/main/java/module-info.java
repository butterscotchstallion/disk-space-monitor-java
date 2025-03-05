module com.dsm.diskspacemonitor {
    requires javafx.controls;
    requires javafx.fxml;
    requires javafx.web;

    requires org.controlsfx.controls;
    requires org.kordamp.ikonli.javafx;
    requires org.kordamp.bootstrapfx.core;
    requires eu.hansolo.tilesfx;
    requires static lombok;
    requires java.desktop;
    requires java.logging;

    opens com.dsm.diskspacemonitor to javafx.fxml;
    exports com.dsm.diskspacemonitor;
}
module com.dsm.diskspacemonitor {
    requires javafx.fxml;
    requires javafx.web;

    requires org.controlsfx.controls;
    requires org.kordamp.ikonli.javafx;
    requires org.kordamp.bootstrapfx.core;
    requires eu.hansolo.tilesfx;
    requires static lombok;
    requires java.logging;
    requires com.dustinredmond.fxtrayicon;
    requires java.desktop;

    opens com.dsm.diskspacemonitor to javafx.fxml;
    exports com.dsm.diskspacemonitor;
}
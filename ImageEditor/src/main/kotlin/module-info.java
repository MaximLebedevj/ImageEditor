module com.editor.pack {
    requires javafx.controls;
    requires javafx.fxml;
    requires javafx.web;
    requires kotlin.stdlib;

    requires org.controlsfx.controls;
    requires com.dlsc.formsfx;
    requires validatorfx;
    requires org.kordamp.ikonli.javafx;
    requires org.kordamp.bootstrapfx.core;
    requires eu.hansolo.tilesfx;
    requires javafx.swing;
    requires opencv;
    requires com.google.gson;

    opens com.editor.pack to javafx.fxml, com.google.gson;
    exports com.editor.pack;
    opens com.editor.pack.core to javafx.fxml, com.google.gson;
    exports com.editor.pack.core;
    opens com.editor.pack.nodes to javafx.fxml, com.google.gson;
    exports com.editor.pack.nodes;
}
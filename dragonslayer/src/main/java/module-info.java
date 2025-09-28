module org.unocapstone.dragonslair {
    // --- JavaFX ---
    requires javafx.controls;   // UI controls; brings in javafx.graphics/base transitively
    requires javafx.fxml;       // FXMLLoader and @FXML

    // --- JDBC (Derby discovered via META-INF/services) ---
    requires java.sql;
    requires zt.zip;

    requires org.apache.commons.io;
    requires org.apache.poi.ooxml;
    requires org.apache.poi.poi;

    // --- Logging ---
    opens org.unocapstone.dragonslair to javafx.fxml, javafx.base, javafx.graphics;
    opens org.unocapstone.dragonslair.controllers to javafx.fxml, javafx.base, javafx.graphics;
    opens org.unocapstone.dragonslair.ui to javafx.fxml, javafx.base, javafx.graphics;
}
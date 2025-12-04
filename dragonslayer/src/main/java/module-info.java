module org.unocapstone.dragonslair {
    // --- JavaFX ---
    requires javafx.controls;   // UI controls; brings in javafx.graphics/base transitively
    requires javafx.fxml;       // FXMLLoader and @FXML

    // --- JDBC (Derby discovered via META-INF/services) ---
    requires java.sql;
    requires java.xml; // for DataSource via JNDI lookup
    requires zt.zip;

    requires org.apache.commons.io;
    requires org.apache.poi.ooxml;
    requires org.apache.poi.poi;

    // Exposes controllers and related java files to FXMLs reflection system
    opens org.unocapstone.dragonslair to javafx.fxml, javafx.base, javafx.graphics;
    opens org.unocapstone.dragonslair.controllers to javafx.fxml, javafx.base, javafx.graphics;
    opens org.unocapstone.dragonslair.ui to javafx.fxml, javafx.base, javafx.graphics;
}

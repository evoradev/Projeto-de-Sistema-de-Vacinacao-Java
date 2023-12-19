module poov.projetovacinafx {
    requires javafx.controls;
    requires javafx.fxml;
    requires transitive javafx.graphics;
    requires java.sql;

    opens poov.projetovacinafx to javafx.fxml;
    opens poov.projetovacinafx.Controllers to javafx.fxml, javafx.graphics;
    opens poov.projetovacinafx.Model to javafx.base;
    

    exports poov.projetovacinafx;
}

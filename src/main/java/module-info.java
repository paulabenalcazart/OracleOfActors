module ec.edu.uees.oracleofactors {
    requires javafx.controls;
    requires javafx.fxml;
    requires org.json;

    opens ec.edu.uees.oracleofactors to javafx.fxml;
    exports ec.edu.uees.oracleofactors;
}

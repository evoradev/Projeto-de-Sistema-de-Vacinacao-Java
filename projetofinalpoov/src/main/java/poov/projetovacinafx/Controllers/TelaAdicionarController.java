package poov.projetovacinafx.Controllers;

import java.io.IOException;
import java.sql.SQLException;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Stage;
import poov.projetovacinafx.Model.Vacina;
import poov.projetovacinafx.dao.ConexaoFactoryPostgreSQL;
import poov.projetovacinafx.dao.VacinaDAO;
import poov.projetovacinafx.dao.core.ConnectionFactory;
import poov.projetovacinafx.dao.core.DAOFactory;
import poov.projetovacinafx.dao.core.GenericJDBCDAO;

public class TelaAdicionarController {

    Vacina v = new Vacina();
    ConnectionFactory conexaoFactory = new ConexaoFactoryPostgreSQL("localhost:5432/poov", "postgres", "admin");
    DAOFactory factory = new DAOFactory(conexaoFactory);

    @FXML
    private Button adicionarnovavac;

    @FXML
    private Button cancelarnovavac;

    @FXML
    private TextArea descricaonovavac;

    @FXML
    private TextField nomenovavac;

    @FXML
    private AnchorPane novavacinaid;

    @FXML
    private void switchToTelaPrincipal() throws IOException {
        FXMLLoader loader = new FXMLLoader(getClass().getResource("/poov/projetovacinafx/TelaPrincipal.fxml"));
        Parent root = loader.load();
        Stage stage = new Stage();
        Stage atual = (Stage) novavacinaid.getScene().getWindow();
        stage.setScene(new Scene(root));
        atual.close();
        stage.show();
    }

    @FXML
    private void switchToCriarVacina() throws IOException {
        try {
            v.setNome(nomenovavac.getText());
            v.setDescricao(descricaonovavac.getText());

            if (v.getNome().isBlank() || v.getDescricao().isBlank()) {
                Alert alerta = new Alert(Alert.AlertType.ERROR);
                alerta.setContentText("É necessário digitar um nome e uma descrição");
                alerta.showAndWait();
            } else {
                factory.abrirConexao();
                VacinaDAO dao = factory.getDAO(VacinaDAO.class);
                dao.create(v);
            }

        } catch (SQLException ex) {
            GenericJDBCDAO.showSQLException(ex);
        } finally {
            factory.fecharConexao();
            switchToTelaPrincipal();
        }
    }
}

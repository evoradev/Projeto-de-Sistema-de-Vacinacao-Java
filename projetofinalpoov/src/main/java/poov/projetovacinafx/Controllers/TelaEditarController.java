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

public class TelaEditarController {

    @FXML
    private TextField editarid;

    @FXML
    private Button cancelaredicao;

    @FXML
    private Button editarvacina;

    @FXML
    private TextArea editvacinadesc;

    @FXML
    private TextField editvacinanome;

    @FXML
    private AnchorPane editarvacinaid;

    Vacina vacina = new Vacina();

    @FXML
    private void switchToTelaPrincipal() throws IOException {
        FXMLLoader loader = new FXMLLoader(getClass().getResource("/poov/projetovacinafx/TelaPrincipal.fxml"));
        Parent root = loader.load();
        Stage stage = new Stage();
        Stage atual = (Stage) editarvacinaid.getScene().getWindow();
        stage.setScene(new Scene(root));
        atual.close();
        stage.show();
    }

    public void recebeVacina(Vacina v){
        vacina = v;
        configurarDadosVacina(vacina);
    }

    public void configurarDadosVacina(Vacina v) {
        editarid.setText(String.valueOf(v.getCodigo()));
        editvacinanome.setText(v.getNome());
        editvacinadesc.setText(v.getDescricao());
    }

    ConnectionFactory conexaoFactory = new ConexaoFactoryPostgreSQL("localhost:5432/poov", "postgres", "admin");
    DAOFactory factory = new DAOFactory(conexaoFactory);

    @FXML
    private void switchToUpdate() throws IOException {
        try {
            factory.abrirConexao();

            VacinaDAO dao = factory.getDAO(VacinaDAO.class);
            vacina.setNome(editvacinanome.getText());
            vacina.setDescricao(editvacinadesc.getText());

            if (vacina.getNome().isBlank() || vacina.getDescricao().isBlank()) {
                Alert alerta = new Alert(Alert.AlertType.ERROR);
                alerta.setContentText("Os valores nome e descrição não podem ser vazios");
                alerta.showAndWait();
            } else {
                Alert alerta = new Alert(Alert.AlertType.WARNING);
                alerta.setContentText("Deseja realmente alterar essa vacina ?");
                alerta.showAndWait();
                if (alerta.getResult().getText().equals("OK")) {
                    dao.update(vacina);
                    alerta.setContentText("Update efetuado com sucesso!");
                    alerta.showAndWait();
                }
            }

        } catch (SQLException ex) {
            GenericJDBCDAO.showSQLException(ex);
        } finally {
            factory.fecharConexao();
            switchToTelaPrincipal();
        }
    }
}

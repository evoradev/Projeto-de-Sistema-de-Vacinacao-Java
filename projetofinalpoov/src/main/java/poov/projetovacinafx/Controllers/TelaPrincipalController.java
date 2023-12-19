package poov.projetovacinafx.Controllers;

import java.io.IOException;
import java.net.URL;
import java.sql.SQLException;
import java.time.LocalDate;
import java.util.List;
import java.util.ResourceBundle;

import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Stage;
import poov.projetovacinafx.Model.Aplicacao;
import poov.projetovacinafx.Model.Pessoa;
import poov.projetovacinafx.Model.Situacao;
import poov.projetovacinafx.Model.Vacina;
import poov.projetovacinafx.dao.AplicacaoDAO;
import poov.projetovacinafx.dao.ConexaoFactoryPostgreSQL;
import poov.projetovacinafx.dao.PessoaDAO;
import poov.projetovacinafx.dao.VacinaDAO;
import poov.projetovacinafx.dao.core.DAOFactory;
import poov.projetovacinafx.dao.core.GenericJDBCDAO;
import poov.projetovacinafx.dao.core.ConnectionFactory;

public class TelaPrincipalController implements Initializable {

    @FXML
    private TableView<Vacina> tabelavac;
    @FXML
    private TableView<Pessoa> tabelapes;
    @FXML
    private TableColumn<Pessoa, String> TCPnome;
    @FXML
    private TableColumn<Pessoa, Long> TCPcodigo;
    @FXML
    private TableColumn<Pessoa, String> TCPcpf;
    @FXML
    private TableColumn<Pessoa, LocalDate> TCPnascimento;
    @FXML
    private TableColumn<Vacina, Long> TCVcodigo;
    @FXML
    private TableColumn<Vacina, String> TCVdescricao;
    @FXML
    private TableColumn<Vacina, String> TCVnome;
    @FXML
    private AnchorPane telaplrincipalid;
    @FXML
    private TextField codigoVacinaTF;
    @FXML
    private TextField nomeVacinaTF;
    @FXML
    private TextField descricaoVacinaTF;

    ConnectionFactory conexaoFactory = new ConexaoFactoryPostgreSQL("localhost:5432/poov", "postgres", "admin");
    DAOFactory factory = new DAOFactory(conexaoFactory);

    List<Pessoa> pes = null;
    List<Vacina> vac = null;

    String nomeVac = "";
    String coidgoVac = "";
    String descricaoVac = "";

    Stage atual = new Stage();

    private Pessoa pessoa;
    private Vacina vacina;

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        pes = FindAllPes();
        vac = FindAllVac();

        // Inicizaliza a tabela de Pessoas
        TCPcodigo.setCellValueFactory(new PropertyValueFactory<Pessoa, Long>("codigo"));
        TCPnome.setCellValueFactory(new PropertyValueFactory<Pessoa, String>("nome"));
        TCPcpf.setCellValueFactory(new PropertyValueFactory<Pessoa, String>("cpf"));
        // TCPnascimento.setCellValueFactory(new PropertyValueFactory<Pessoa,
        // LocalDate>("nascimento"));

        tabelapes.getItems().addAll(pes);
        tabelapes.setOnMouseClicked(new EventHandler<MouseEvent>() {
            @Override
            public void handle(MouseEvent event) {
                if (event.getClickCount() == 1 && (!tabelapes.getSelectionModel().isEmpty())) {
                    Pessoa p = tabelapes.getSelectionModel().getSelectedItem();
                    pessoa = p;
                }
            }
        });

        // Inicializa a tabela de Vacinas
        TCVcodigo.setCellValueFactory(new PropertyValueFactory<Vacina, Long>("codigo"));
        TCVnome.setCellValueFactory(new PropertyValueFactory<Vacina, String>("nome"));
        TCVdescricao.setCellValueFactory(new PropertyValueFactory<Vacina, String>("descricao"));

        tabelavac.getItems().addAll(vac);

        tabelavac.setOnMouseClicked(new EventHandler<MouseEvent>() {
            @Override
            public void handle(MouseEvent event) {
                if (event.getClickCount() == 1 && (!tabelavac.getSelectionModel().isEmpty())) {
                    Vacina v = tabelavac.getSelectionModel().getSelectedItem();
                    vacina = v;
                }
            }
        });
    }

    // Métodos de buscar todos mostrados em aula
    private List<Pessoa> FindAllPes() {
        try {
            factory.abrirConexao();
            PessoaDAO pessoaDAO = factory.getDAO(PessoaDAO.class);
            List<Pessoa> pessoas = pessoaDAO.findAll();

            if (pessoas.isEmpty()) {
                Alert alerta = new Alert(Alert.AlertType.ERROR);
                alerta.setContentText("Não existem pessoas salvas no BD");
            } else {
                return pessoas;
            }
        } catch (SQLException ex) {
            GenericJDBCDAO.showSQLException(ex);
        } finally {
            factory.fecharConexao();
        }

        return null;
    }

    private List<Vacina> FindAllVac() {
        try {
            factory.abrirConexao();
            VacinaDAO dao = factory.getDAO(VacinaDAO.class);
            List<Vacina> vacinas = dao.findAll();

            if (vacinas.isEmpty()) {
                System.out.println("Não existem vacinas salvas no BD");
            } else {
                return vacinas;
            }
        } catch (SQLException ex) {
            GenericJDBCDAO.showSQLException(ex);
        } finally {
            factory.fecharConexao();
        }

        return null;
    }

    // Método para remoção de vacina em clique
    @FXML
    void switchToRemove(ActionEvent event) {

        if (vacina == null) {
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setContentText("É necessário selecionar uma vacina para remoção !");
            alert.showAndWait();
            return;
        }

        Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
        alert.setTitle("Remover Vacina");
        alert.setContentText("Tem certeza que deseja remover a vacina?");
        alert.showAndWait();
        if (alert.getResult().getText().equals("OK")) {
            try {
                factory.abrirConexao();
                VacinaDAO dao = factory.getDAO(VacinaDAO.class);
                if (vacina != null) {
                    Alert alerta = new Alert(Alert.AlertType.CONFIRMATION);
                    alerta.setTitle("Tela de remoção de vacina");
                    alerta.setContentText("A vacina passará para a situação 'inativa'");
                    if (alert.getResult().getText().equals("OK")) {
                        vacina.setSituacao(Situacao.INATIVO);
                        dao.update(vacina);
                        tabelavac.getItems().remove(vacina);
                    }
                }
                tabelavac.getItems().remove(vacina);

            } catch (SQLException ex) {
                GenericJDBCDAO.showSQLException(ex);
            } finally {
                factory.fecharConexao();
            }
        }
    }

    // Aplicação de vacina
    @FXML
    void switchToAplicarVac(ActionEvent event) {
        Aplicacao ap = new Aplicacao(LocalDate.now(), pessoa, vacina, Situacao.ATIVO);
        if (vacina == null || pessoa == null) {
            Alert alerta = new Alert(Alert.AlertType.WARNING);
            alerta.setContentText("É necessário selecionar uma pessoa e uma vacina");
            alerta.showAndWait();
            return;
        } else {
            Alert alerta = new Alert(Alert.AlertType.CONFIRMATION);
            alerta.setContentText("Aplicação de vacina");
            alerta.showAndWait();
            if (alerta.getResult().getText().equals("Cancel")) {
                return;
            }
        }
        try {
            factory.abrirConexao();
            AplicacaoDAO dao = factory.getDAO(AplicacaoDAO.class);
            dao.create(ap);
            factory.fecharConexao();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    // Outras telas
    @FXML
    private void switchToNova() throws IOException {
        FXMLLoader loader = new FXMLLoader(getClass().getResource("/poov/projetovacinafx/TelaNovaVacina.fxml"));
        Parent root = loader.load();
        Stage stage = new Stage();
        stage.setScene(new Scene(root));
        atual = (Stage) telaplrincipalid.getScene().getWindow();
        atual.close();
        stage.show();
    }

    @FXML
    private void switchToEditar() throws IOException {
        Vacina vacinaSelecionada = tabelavac.getSelectionModel().getSelectedItem();

        if (vacinaSelecionada != null) {

            FXMLLoader loader = new FXMLLoader(getClass().getResource("/poov/projetovacinafx/TelaEditarVacina.fxml"));
            Parent root = loader.load();

            TelaEditarController editController = loader.getController();
            editController.recebeVacina(vacinaSelecionada);

            Stage stage = new Stage();
            stage.setScene(new Scene(root));
            atual = (Stage) telaplrincipalid.getScene().getWindow();
            atual.close();
            stage.show();

        } else {
            Alert alert = new Alert(Alert.AlertType.WARNING);
            alert.setContentText("Selecione uma vacina para editar");
            alert.showAndWait();
        }
    }

}
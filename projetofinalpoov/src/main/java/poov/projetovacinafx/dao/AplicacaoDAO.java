package poov.projetovacinafx.dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import poov.projetovacinafx.dao.core.GenericJDBCDAO;
import poov.projetovacinafx.Model.Aplicacao;
import poov.projetovacinafx.Model.Situacao;

public class AplicacaoDAO extends GenericJDBCDAO<Aplicacao, Long> {

    // Definição das queries SQL
    private static final String FIND_ALL_QUERY = "SELECT codigo, data, codigo_pessoa, codigo_vacina, situacao FROM aplicacao";
    private static final String FIND_BY_KEY_QUERY = FIND_ALL_QUERY + " WHERE codigo=?";
    private static final String CREATE_QUERY = "INSERT INTO aplicacao (data, codigo_pessoa, codigo_vacina, situacao) VALUES (?, ?, ?, ?)";
    private static final String UPDATE_QUERY = "UPDATE aplicacao SET data=?, codigo_pessoa=?, codigo_vacina=?, situacao=? WHERE codigo=?";
    private static final String REMOVE_QUERY = "DELETE FROM aplicacao WHERE codigo=?";

    public AplicacaoDAO(Connection conexao) {
        super(conexao);
    }

    @Override
    protected Aplicacao toEntity(ResultSet resultSet) throws SQLException {
        Aplicacao aplicacao = new Aplicacao();
        aplicacao.setData(resultSet.getDate("data").toLocalDate());
        aplicacao.setSituacao(Situacao.valueOf(resultSet.getString("situacao")));
        return aplicacao;
    }

    @Override
    protected void addParameters(PreparedStatement statement, Aplicacao entity) throws SQLException {
        statement.setDate(1, java.sql.Date.valueOf(entity.getData()));
        statement.setLong(2, entity.getPessoa().getCodigo());
        statement.setLong(3, entity.getVacina().getCodigo());
        statement.setString(4, entity.getSituacao().toString());

    }

    @Override
    protected String findByKeyQuery() {
        return FIND_BY_KEY_QUERY;
    }

    @Override
    protected String findAllQuery() {
        return FIND_ALL_QUERY;
    }

    @Override
    protected String updateQuery() {
        return UPDATE_QUERY;
    }

    @Override
    protected String createQuery() {
        return CREATE_QUERY;
    }

    @Override
    protected String removeQuery() {
        return REMOVE_QUERY;
    }

    @Override
    protected void setKeyInStatementFromEntity(PreparedStatement statement, Aplicacao entity) throws SQLException {
        statement.setLong(1, entity.getCodigo());
    }

    @Override
    protected void setKeyInStatement(PreparedStatement statement, Long key) throws SQLException {
        statement.setLong(1, key);
    }

    @Override
    protected void setKeyInEntity(ResultSet rs, Aplicacao entity) throws SQLException {
        entity.setCodigo(rs.getLong("codigo"));
    }

}
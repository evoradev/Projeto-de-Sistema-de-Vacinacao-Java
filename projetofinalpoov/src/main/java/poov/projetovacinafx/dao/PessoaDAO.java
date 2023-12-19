package poov.projetovacinafx.dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;
import poov.projetovacinafx.dao.core.ConnectionFactory;
import poov.projetovacinafx.dao.core.DAOFactory;
import poov.projetovacinafx.dao.core.GenericJDBCDAO;
import poov.projetovacinafx.Model.Pessoa;
import poov.projetovacinafx.Model.Situacao;



public class PessoaDAO extends GenericJDBCDAO<Pessoa, Long> {

    public PessoaDAO(Connection conexao) {
        super(conexao);
    }

    private static final String FIND_ALL_QUERY = "SELECT codigo, nome, cpf, dataNascimento, situacao FROM pessoa WHERE situacao = 'ATIVO' ";
    private static final String FIND_BY_KEY_QUERY = FIND_ALL_QUERY + "AND codigo=? ";
    private static final String UPDATE_QUERY = "UPDATE pessoa SET nome=?, cpf=?, dataNascimento=?, situacao=? WHERE codigo=?";
    private static final String CREATE_QUERY = "INSERT INTO pessoa (nome, cpf, dataNascimento, situacao) VALUES (?, ?, ?, ?)";
    private static final String REMOVE_QUERY = "DELETE FROM pessoa WHERE codigo=?";

    @Override
    protected Pessoa toEntity(ResultSet resultSet) throws SQLException {
        Pessoa Pessoa = new Pessoa();
        Pessoa.setCodigo(resultSet.getLong("codigo"));
        Pessoa.setNome(resultSet.getString("nome"));
        Pessoa.setCpf(resultSet.getString("cpf"));
        Pessoa.setDataNascimento(resultSet.getDate("dataNascimento").toLocalDate());
        if (resultSet.getString("situacao").equals("ATIVO")) {
            Pessoa.setSituacao(Situacao.ATIVO);
        } else {
            Pessoa.setSituacao(Situacao.INATIVO);
        }
        return Pessoa;
    }

    @Override
    protected void addParameters(PreparedStatement pstmt, Pessoa entity) throws SQLException {
        pstmt.setString(1, entity.getNome());
        pstmt.setString(2, entity.getCpf());
        pstmt.setString(3, entity.getSituacao().toString());
        if (entity.getCodigo() != null) {
            pstmt.setLong(4, entity.getCodigo());
        }
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

    // Buscar todas pessoas baseado no mostrado em sala
    public List<Pessoa> FindAllPessoas() {
        ConnectionFactory conexaoFactory = new ConexaoFactoryPostgreSQL("localhost:5432/poov", "postgres", "admin");
        DAOFactory factory  = new DAOFactory(conexaoFactory);
        try {
            factory.abrirConexao();
            PessoaDAO dao = factory.getDAO(PessoaDAO.class);
            List<Pessoa> pessoas = dao.findAll();

            if (pessoas.isEmpty()) {
                System.out.println("Nao existem pessoas salvas no BD");
            } else {
                for (Pessoa pessoa : pessoas) {
                    System.out.println(pessoa);
                }
            }
            return pessoas;
        } catch (SQLException ex) {
            GenericJDBCDAO.showSQLException(ex);
        } finally {
            factory.fecharConexao();
        }
        return null;
    }

    @Override
    protected void setKeyInStatementFromEntity(PreparedStatement statement, Pessoa entity) throws SQLException {
        statement.setLong(1, entity.getCodigo());
    }

    @Override
    protected void setKeyInStatement(PreparedStatement statement, Long key) throws SQLException {
        statement.setLong(1, key);
    }

    @Override
    protected void setKeyInEntity(ResultSet rs, Pessoa entity) throws SQLException {
        entity.setCodigo(rs.getLong(1));
    }
}

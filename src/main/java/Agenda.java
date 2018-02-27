
import com.mysql.jdbc.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import sun.util.calendar.Gregorian;

public class Agenda {

    private Connection obterConexao() throws ClassNotFoundException, SQLException {
        Connection conn = null;

        Class.forName("com.mysql.jdbc.Driver");

        conn = (Connection) DriverManager.getConnection("jdbc:mysql://localhost:3306/agenda",
                "root",
                "");

        return conn;

    }

    public List<Pessoa> listar() throws ClassNotFoundException, SQLException {

        Connection conn = null;
        PreparedStatement stmt = null;
        ResultSet resultado = null;
        List<Pessoa> listPessoas = new ArrayList<Pessoa>();

        try {
            // 1 - Abrir conexão com BD
            // Declarar o driver JDBC de acordo com o banco de dados usado
            Class.forName("com.mysql.jdbc.Driver");

            conn = (Connection) DriverManager.getConnection("jdbc:mysql://localhost:3306/agenda",
                    "root",
                    "");

            // 2 - executar ações bd
            stmt = conn.prepareStatement("select id, nome, dtnascimento FROM agenda.pessoa");

            resultado = stmt.executeQuery();

            while (resultado.next()) {
                long id = resultado.getLong("id");
                String sNome = resultado.getString("Nome");
                Date dtNascimento = resultado.getDate("dtnascimento");
                Pessoa pessoa = new Pessoa();

                pessoa.setId(id);
                pessoa.setDtNascimento(dtNascimento);
                pessoa.setsNome(sNome);
                listPessoas.add(pessoa);

                //System.out.println(id + " , " + sNome + " , " + dtNascimento);
            }

        } finally {
            //3 - fechando conexão
            if (resultado != null) {
                resultado.close();
            }
            if (stmt != null) {
                stmt.close();
            }
            if (conn != null) {
                conn.close();
            }

        }

        return listPessoas;

    }

    public static void main(String[] args) {
        Agenda agenda = new Agenda();

        try {
            // agenda.incluir();
            List<Pessoa> lista = agenda.listar();

            for (Pessoa p : lista) {
                System.out.println(p.getId() + ", " + p.getsNome() + ", " + p.getDtNascimento());

            }

        } catch (ClassNotFoundException ex) {

            System.out.println(ex.getMessage());
        } catch (SQLException ex) {

            System.out.println(ex.getMessage());

        }
    }

    public void incluir() throws ClassNotFoundException, SQLException {

        PreparedStatement stmt = null;
        ResultSet resultado = null;

        try (Connection conn = obterConexao()) {
            stmt = conn.prepareStatement("INSERT INTO PESSOA (nome, dtnascimento) VALUES (?,?)");
            stmt.setString(1, "Gabriela Nogueira");
            GregorianCalendar cal = new GregorianCalendar(1998, 12, 03);
            stmt.setDate(2, new java.sql.Date(cal.getTimeInMillis()));

            int iStatus = stmt.executeUpdate();
            System.out.println("Status: " + iStatus);
        }

    }

}

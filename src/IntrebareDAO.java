import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class IntrebareDAO {
    private final Connection connection;

    public IntrebareDAO() throws SQLException {
        this.connection = DatabaseConnection.getConnection();
    }

    public void create(Intrebare intrebare) throws SQLException {
        String sql = "INSERT INTO intrebare (text, quiz_id, punctaj) VALUES (?, ?, ?)";
        try (PreparedStatement stmt = connection.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {
            stmt.setString(1, intrebare.getText());
            stmt.setInt(2, intrebare.getQuiz().getId()); // presupunem că Intrebare are getQuiz()
            stmt.setInt(3, intrebare.getPunctaj());
            stmt.executeUpdate();
            ResultSet keys = stmt.getGeneratedKeys();
            if (keys.next()) {
                intrebare.setId(keys.getInt(1));
            }
        }
        AuditService.log("create_intrebare");
    }

    public List<Intrebare> readAll() throws SQLException {
        List<Intrebare> lista = new ArrayList<>();
        String sql = "SELECT i.id, i.text, i.punctaj, i.quiz_id, q.titlu AS quiz_titlu " +
                "FROM intrebare i JOIN quiz q ON i.quiz_id = q.id";
        try (Statement stmt = connection.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {
            while (rs.next()) {
                Intrebare i = new Intrebare();
                i.setId(rs.getInt("id"));
                i.setText(rs.getString("text"));
                i.setPunctaj(rs.getInt("punctaj"));

                // Construim un obiect Quiz minim, doar cu id și titlu
                Quiz q = new Quiz();
                q.setId(rs.getInt("quiz_id"));
                q.setTitlu(rs.getString("quiz_titlu"));
                i.setQuiz(q);

                lista.add(i);
            }
        }
        AuditService.log("read_intrebari");
        return lista;
    }

    public void update(Intrebare intrebare) throws SQLException {
        String sql = "UPDATE intrebare SET text = ?, punctaj = ? WHERE id = ?";
        try (PreparedStatement stmt = connection.prepareStatement(sql)) {
            stmt.setString(1, intrebare.getText());
            stmt.setInt(2, intrebare.getPunctaj());
            stmt.setInt(3, intrebare.getId());
            stmt.executeUpdate();
        }
        AuditService.log("update_intrebare");
    }

    public void delete(int id) throws SQLException {
        String sql = "DELETE FROM intrebare WHERE id = ?";
        try (PreparedStatement stmt = connection.prepareStatement(sql)) {
            stmt.setInt(1, id);
            stmt.executeUpdate();
        }
        AuditService.log("delete_intrebare");
    }
}

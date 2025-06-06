import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class QuizDAO {
    private final Connection connection;

    public QuizDAO() throws SQLException {
        this.connection = DatabaseConnection.getConnection();
    }

    public void create(Quiz quiz) throws SQLException {
        String sql = "INSERT INTO quiz (titlu, descriere) VALUES (?, ?)";
        try (PreparedStatement stmt = connection.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {
            stmt.setString(1, quiz.getTitlu());
            stmt.setString(2, quiz.getDescriere());
            stmt.executeUpdate();
            ResultSet keys = stmt.getGeneratedKeys();
            if (keys.next()) {
                quiz.setId(keys.getInt(1));
            }
        }
        AuditService.log("create_quiz");
    }

    public List<Quiz> readAll() throws SQLException {
        List<Quiz> lista = new ArrayList<>();
        String sql = "SELECT id, titlu, descriere FROM quiz";
        try (Statement stmt = connection.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {
            while (rs.next()) {
                Quiz q = new Quiz();
                q.setId(rs.getInt("id"));
                q.setTitlu(rs.getString("titlu"));
                q.setDescriere(rs.getString("descriere"));
                lista.add(q);
            }
        }
        AuditService.log("read_quizuri");
        return lista;
    }

    public void update(Quiz quiz) throws SQLException {
        String sql = "UPDATE quiz SET titlu = ?, descriere = ? WHERE id = ?";
        try (PreparedStatement stmt = connection.prepareStatement(sql)) {
            stmt.setString(1, quiz.getTitlu());
            stmt.setString(2, quiz.getDescriere());
            stmt.setInt(3, quiz.getId());
            stmt.executeUpdate();
        }
        AuditService.log("update_quiz");
    }

    public void delete(int id) throws SQLException {
        // Înainte să ștergi quiz-ul, ar fi ideal să ștergi toate întrebările asociate (dacă ai constrângere ON DELETE NO ACTION)
        String sqlIntrebari = "DELETE FROM intrebare WHERE quiz_id = ?";
        try (PreparedStatement stmtI = connection.prepareStatement(sqlIntrebari)) {
            stmtI.setInt(1, id);
            stmtI.executeUpdate();
        }

        String sql = "DELETE FROM quiz WHERE id = ?";
        try (PreparedStatement stmt = connection.prepareStatement(sql)) {
            stmt.setInt(1, id);
            stmt.executeUpdate();
        }
        AuditService.log("delete_quiz");
    }


    public Quiz readById(int id) throws SQLException {
        String sql = "SELECT id, titlu, descriere FROM quiz WHERE id = ?";
        try (PreparedStatement stmt = connection.prepareStatement(sql)) {
            stmt.setInt(1, id);
            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) {
                    Quiz quiz = new Quiz();
                    quiz.setId(rs.getInt("id"));
                    quiz.setTitlu(rs.getString("titlu"));
                    quiz.setDescriere(rs.getString("descriere"));
                    return quiz;
                }
            }
        }
        return null;
    }
}

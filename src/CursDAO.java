import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class CursDAO {
    private final Connection connection;

    public CursDAO() throws SQLException {
        this.connection = DatabaseConnection.getConnection();
    }

    public void create(Curs curs) throws SQLException {
        String sql = "INSERT INTO curs (titlu, descriere, profesor_email) VALUES (?, ?, ?)";
        try (PreparedStatement stmt = connection.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {
            stmt.setString(1, curs.getTitlu());
            stmt.setString(2, curs.getDescriere());
            stmt.setString(3, curs.getProfesor().getEmail());
            // presupunem că Curs are getProfesor() care returnează un obiect Profesor
            stmt.executeUpdate();

            ResultSet keys = stmt.getGeneratedKeys();
            if (keys.next()) {
                curs.setId(keys.getInt(1));
            }
        }
        AuditService.log("create_curs");
    }

    public List<Curs> readAll() throws SQLException {
        List<Curs> lista = new ArrayList<>();
        String sql = "SELECT c.id, c.titlu, c.descriere, c.profesor_email, u.nume AS nume_prof " +
                "FROM curs c JOIN utilizatori u ON c.profesor_email = u.email";
        try (Statement stmt = connection.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {
            while (rs.next()) {
                Curs c = new Curs();
                c.setId(rs.getInt("id"));
                c.setTitlu(rs.getString("titlu"));
                c.setDescriere(rs.getString("descriere"));

                // Construim profesorul minim: doar e-mail și nume
                String profEmail = rs.getString("profesor_email");
                String profNume  = rs.getString("nume_prof");
                Profesor prof = new Profesor(profNume, profEmail);
                c.setProfesor(prof);

                lista.add(c);
            }
        }
        AuditService.log("read_cursuri");
        return lista;
    }

    public void update(Curs curs) throws SQLException {
        String sql = "UPDATE curs SET titlu = ?, descriere = ?, profesor_email = ? WHERE id = ?";
        try (PreparedStatement stmt = connection.prepareStatement(sql)) {
            stmt.setString(1, curs.getTitlu());
            stmt.setString(2, curs.getDescriere());
            stmt.setString(3, curs.getProfesor().getEmail());
            stmt.setInt(4, curs.getId());
            stmt.executeUpdate();
        }
        AuditService.log("update_curs");
    }

    public void delete(int id) throws SQLException {
        String sql = "DELETE FROM curs WHERE id = ?";
        try (PreparedStatement stmt = connection.prepareStatement(sql)) {
            stmt.setInt(1, id);
            stmt.executeUpdate();
        }
        // Dacă există și înscrieri în curs_cursanti, trebuie să le ștergi manual înainte (sau să ai ON DELETE CASCADE)
        AuditService.log("delete_curs");
    }

    public void inscriereLaCurs(int cursId, String cursantEmail) throws SQLException {
        String sql = "INSERT INTO curs_cursanti (curs_id, cursant_email) VALUES (?, ?)";
        try (PreparedStatement stmt = connection.prepareStatement(sql)) {
            stmt.setInt(1, cursId);
            stmt.setString(2, cursantEmail);
            stmt.executeUpdate();
        }
        AuditService.log("inscriere_cursant_la_curs");
    }

    public Curs readByTitle(String titlu) throws SQLException {
        String sql = "SELECT id, titlu, descriere FROM curs WHERE titlu = ?";
        try (PreparedStatement stmt = connection.prepareStatement(sql)) {
            stmt.setString(1, titlu);
            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) {
                    Curs curs = new Curs();
                    curs.setId(rs.getInt("id"));
                    curs.setTitlu(rs.getString("titlu"));
                    curs.setDescriere(rs.getString("descriere"));
                    return curs;
                }
            }
        }
        return null;
    }

    public int getNumarCursanti(int cursId) throws SQLException {
        String sql = "SELECT COUNT(*) FROM curs_cursanti WHERE curs_id = ?";
        try (PreparedStatement stmt = connection.prepareStatement(sql)) {
            stmt.setInt(1, cursId);
            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) {
                    return rs.getInt(1);
                }
            }
        }
        return 0;
    }
}

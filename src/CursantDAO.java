import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class CursantDAO {
    private final Connection connection;

    public CursantDAO() throws SQLException {
        this.connection = DatabaseConnection.getConnection();
    }

    public void create(Cursant cursant) throws SQLException {
        // Întâi, înscriem în tabelul utilizatori (pentru că Utilizator e clasa părinte)
        String sqlUtilizator = "INSERT INTO utilizatori (email, nume, tip) VALUES (?, ?, 'Cursant')";
        try (PreparedStatement stmtU = connection.prepareStatement(sqlUtilizator)) {
            stmtU.setString(1, cursant.getEmail());
            stmtU.setString(2, cursant.getNume());
            stmtU.executeUpdate();
        }

        // Apoi, în cursanti
        String sqlCursant = "INSERT INTO cursanti (email, nivel) VALUES (?, ?)";
        try (PreparedStatement stmtC = connection.prepareStatement(sqlCursant)) {
            stmtC.setString(1, cursant.getEmail());
            stmtC.setString(2, cursant.getNivel()); // presupunem că Cursant are getNivel()
            stmtC.executeUpdate();
        }

        AuditService.log("create_cursant");
    }

    public List<Cursant> readAll() throws SQLException {
        List<Cursant> lista = new ArrayList<>();
        String sql = "SELECT u.email, u.nume, c.nivel " +
                "FROM utilizatori u JOIN cursanti c ON u.email = c.email";
        try (Statement stmt = connection.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {
            while (rs.next()) {
                String email = rs.getString("email");
                String nume = rs.getString("nume");
                String nivel = rs.getString("nivel");
                Cursant c = new Cursant(nume, email);
                c.setNivel(nivel);
                lista.add(c);
            }
        }
        AuditService.log("read_cursanti");
        return lista;
    }

    public void update(Cursant cursant) throws SQLException {
        // presupunem că nu se schimbă tipul, doar nume/nivel
        String sqlUtilizator = "UPDATE utilizatori SET nume = ? WHERE email = ?";
        try (PreparedStatement stmtU = connection.prepareStatement(sqlUtilizator)) {
            stmtU.setString(1, cursant.getNume());
            stmtU.setString(2, cursant.getEmail());
            stmtU.executeUpdate();
        }
        String sqlCursant = "UPDATE cursanti SET nivel = ? WHERE email = ?";
        try (PreparedStatement stmtC = connection.prepareStatement(sqlCursant)) {
            stmtC.setString(1, cursant.getNivel());
            stmtC.setString(2, cursant.getEmail());
            stmtC.executeUpdate();
        }
        AuditService.log("update_cursant");
    }

    public void delete(String email) throws SQLException {
        // Stergem mai întâi din cursanti, apoi din utilizatori
        String sqlCursant = "DELETE FROM cursanti WHERE email = ?";
        try (PreparedStatement stmtC = connection.prepareStatement(sqlCursant)) {
            stmtC.setString(1, email);
            stmtC.executeUpdate();
        }
        String sqlUtilizator = "DELETE FROM utilizatori WHERE email = ?";
        try (PreparedStatement stmtU = connection.prepareStatement(sqlUtilizator)) {
            stmtU.setString(1, email);
            stmtU.executeUpdate();
        }
        AuditService.log("delete_cursant");
    }

    public Cursant readByEmail(String email) throws SQLException {
        String sql = "SELECT u.nume, u.email, c.nivel " +
                "FROM utilizatori u JOIN cursanti c ON u.email = c.email " +
                "WHERE u.email = ?";
        try (PreparedStatement stmt = connection.prepareStatement(sql)) {
            stmt.setString(1, email);
            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) {
                    Cursant cursant = new Cursant(rs.getString("nume"), rs.getString("email"));
                    cursant.setNivel(rs.getString("nivel"));
                    return cursant;
                }
            }
        }
        return null;
    }
}

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class ProfesorDAO {
    private final Connection connection;

    public ProfesorDAO() throws SQLException {
        this.connection = DatabaseConnection.getConnection();
    }

    public void create(Profesor profesor) throws SQLException {
        // Întâi, inserăm în utilizatori (tip = 'Profesor')
        String sqlUtilizator = "INSERT INTO utilizatori (email, nume, tip) VALUES (?, ?, 'Profesor')";
        try (PreparedStatement stmtU = connection.prepareStatement(sqlUtilizator)) {
            stmtU.setString(1, profesor.getEmail());
            stmtU.setString(2, profesor.getNume());
            stmtU.executeUpdate();
        }

        String sqlProfesor = "INSERT INTO profesori (email, specializare) VALUES (?, ?)";
        try (PreparedStatement stmtP = connection.prepareStatement(sqlProfesor)) {
            stmtP.setString(1, profesor.getEmail());
            stmtP.setString(2, profesor.getSpecializare()); // presupunem că ai getSpecializare()
            stmtP.executeUpdate();
        }

        AuditService.log("create_profesor");
    }

    public List<Profesor> readAll() throws SQLException {
        List<Profesor> lista = new ArrayList<>();
        String sql = "SELECT u.email, u.nume, p.specializare " +
                "FROM utilizatori u JOIN profesori p ON u.email = p.email";
        try (Statement stmt = connection.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {
            while (rs.next()) {
                String email = rs.getString("email");
                String nume = rs.getString("nume");
                String spec = rs.getString("specializare");
                Profesor p = new Profesor(nume, email);
                p.setSpecializare(spec);
                lista.add(p);
            }
        }
        AuditService.log("read_profesori");
        return lista;
    }

    public void update(Profesor profesor) throws SQLException {
        String sqlUtilizator = "UPDATE utilizatori SET nume = ? WHERE email = ?";
        try (PreparedStatement stmtU = connection.prepareStatement(sqlUtilizator)) {
            stmtU.setString(1, profesor.getNume());
            stmtU.setString(2, profesor.getEmail());
            stmtU.executeUpdate();
        }
        String sqlProfesor = "UPDATE profesori SET specializare = ? WHERE email = ?";
        try (PreparedStatement stmtP = connection.prepareStatement(sqlProfesor)) {
            stmtP.setString(1, profesor.getSpecializare());
            stmtP.setString(2, profesor.getEmail());
            stmtP.executeUpdate();
        }
        AuditService.log("update_profesor");
    }

    public void delete(String email) throws SQLException {
        String sqlProfesor = "DELETE FROM profesori WHERE email = ?";
        try (PreparedStatement stmtP = connection.prepareStatement(sqlProfesor)) {
            stmtP.setString(1, email);
            stmtP.executeUpdate();
        }
        String sqlUtilizator = "DELETE FROM utilizatori WHERE email = ?";
        try (PreparedStatement stmtU = connection.prepareStatement(sqlUtilizator)) {
            stmtU.setString(1, email);
            stmtU.executeUpdate();
        }
        AuditService.log("delete_profesor");
    }

    public Profesor readByEmail(String email) throws SQLException {
        String sql = "SELECT u.nume, u.email, p.specializare " +
                "FROM utilizatori u JOIN profesori p ON u.email = p.email " +
                "WHERE u.email = ?";
        try (PreparedStatement stmt = connection.prepareStatement(sql)) {
            stmt.setString(1, email);
            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) {
                    Profesor profesor = new Profesor(rs.getString("nume"), rs.getString("email"));
                    profesor.setSpecializare(rs.getString("specializare"));
                    return profesor;
                }
            }
        }
        return null;
    }
}

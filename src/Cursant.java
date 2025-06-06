import java.util.ArrayList;
import java.util.List;

public class Cursant extends Utilizator {
    private List<Curs> cursuriInscrise = new ArrayList<>();
    private String nivel;

    public Cursant(String nume, String email) {
        super(nume, email);
    }

    public void inscrieLaCurs(Curs curs) {
        cursuriInscrise.add(curs);
        curs.adaugaCursant(this);
    }

    public List<Curs> getCursuriInscrise() {
        return cursuriInscrise;
    }

    public String getNivel() {
        return nivel;
    }

    public void setNivel(String nivel) {
        this.nivel = nivel;
    }
}
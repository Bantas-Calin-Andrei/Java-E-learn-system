import java.util.*;

public class ServiciuElearn {
    private List<Curs> cursuri = new ArrayList<>();
    private Map<String, Utilizator> utilizatori = new HashMap<>();

    public void adaugaUtilizator(Utilizator u) {
        utilizatori.put(u.getEmail(), u);
    }

    public void adaugaCurs(Curs curs) {
        cursuri.add(curs);
    }

    public void inscriereLaCurs(String emailCursant, String titluCurs) {
        Cursant cursant = (Cursant) utilizatori.get(emailCursant);
        for (Curs c : cursuri) {
            if (c.getTitlu().equals(titluCurs)) {
                cursant.inscrieLaCurs(c);
                break;
            }
        }
    }

    public void afiseazaCursuriSortateDupaPopularitate() {
        List<Curs> sortate = new ArrayList<>(cursuri);
        Collections.sort(sortate);
        for (Curs c : sortate) {
            System.out.println(c.getTitlu() + ": " + c.getCursanti().size() + " inscrisi");
        }
    }
}
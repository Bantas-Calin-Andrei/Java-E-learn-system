import java.util.*;

public class Curs implements Comparable<Curs> {
    private String titlu;
    private Profesor profesor;
    private List<Cursant> cursanti = new ArrayList<>();
    private Set<Quiz> quizuri = new HashSet<>();
    private int id;
    private String descriere;

    public Curs() {
    }

    public Curs(int id, String titlu, String descriere, Profesor profesor) {
        this.id = id;
        this.titlu = titlu;
        this.descriere = descriere;
        this.profesor = profesor;
    }

    public void adaugaCursant(Cursant cursant) {
        cursanti.add(cursant);
    }

    public void adaugaQuiz(Quiz quiz) {
        quizuri.add(quiz);
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getTitlu() {
        return titlu;
    }

    public List<Cursant> getCursanti() {
        return cursanti;
    }

    public void setTitlu(String titlu) {
        this.titlu = titlu;
    }

    public String getDescriere() {
        return descriere;
    }

    public void setDescriere(String descriere) {
        this.descriere = descriere;
    }

    public Profesor getProfesor() {
        return profesor;
    }

    public void setProfesor(Profesor profesor) {
        this.profesor = profesor;
    }

    @Override
    public int compareTo(Curs altCurs) {
        return Integer.compare(altCurs.cursanti.size(), this.cursanti.size());
    }
}
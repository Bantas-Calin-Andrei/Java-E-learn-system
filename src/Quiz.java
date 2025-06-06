import java.util.ArrayList;
import java.util.List;

public class Quiz {
    private int id;
    private String titlu;
    private String descriere;
    private List<Intrebare> intrebari = new ArrayList<>();

    public Quiz() {
    }

    public Quiz(String titlu) {
        this.titlu = titlu;
    }

    public Quiz(int id, String titlu, String descriere) {
        this.id = id;
        this.titlu = titlu;
        this.descriere = descriere;
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

    public void setTitlu(String titlu) {
        this.titlu = titlu;
    }

    public String getDescriere() {
        return descriere;
    }

    public void setDescriere(String descriere) {
        this.descriere = descriere;
    }

    public void adaugaIntrebare(Intrebare intrebare) {
        intrebari.add(intrebare);
    }

    public List<Intrebare> getIntrebari() {
        return intrebari;
    }

    public void setIntrebari(List<Intrebare> intrebari) {
        this.intrebari = intrebari;
    }
}

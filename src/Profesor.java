public class Profesor extends Utilizator {
    private String specializare;
    public Profesor(String nume, String email) {
        super(nume, email);
    }
    public String getSpecializare() { return specializare; }
    public void setSpecializare(String s) { this.specializare = s; }
}

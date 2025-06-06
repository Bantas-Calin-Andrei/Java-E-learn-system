import java.util.List;

public class Intrebare {
    private int id;
    private String text;
    private List<String> optiuni;
    private int raspunsCorect;
    private int punctaj;
    private Quiz quiz;

    public Intrebare() {

    }

    public Intrebare(String text, List<String> optiuni, int raspunsCorect) {
        this.text = text;
        this.optiuni = optiuni;
        this.raspunsCorect = raspunsCorect;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }

    public int getPunctaj() {
        return punctaj;
    }

    public void setPunctaj(int punctaj) {
        this.punctaj = punctaj;
    }

    public Quiz getQuiz() {
        return quiz;
    }

    public void setQuiz(Quiz quiz) {
        this.quiz = quiz;
    }

    public List<String> getOptiuni() {
        return optiuni;
    }

    public void setOptiuni(List<String> optiuni) {
        this.optiuni = optiuni;
    }

    public int getRaspunsCorect() {
        return raspunsCorect;
    }

    public void setRaspunsCorect(int raspunsCorect) {
        this.raspunsCorect = raspunsCorect;
    }
}

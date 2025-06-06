import java.sql.SQLException;
import java.util.List;
import java.util.Scanner;

public class Main {
    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        try {
            ProfesorDAO profesorDAO = new ProfesorDAO();
            CursantDAO cursantDAO = new CursantDAO();
            CursDAO cursDAO = new CursDAO();
            QuizDAO quizDAO = new QuizDAO();
            IntrebareDAO intrebareDAO = new IntrebareDAO();



        while (true) {
            System.out.println("\n--- Meniu principal ---");
            System.out.println("1. Adaugă profesor");
            System.out.println("2. Adaugă cursant");
            System.out.println("3. Adaugă curs");
            System.out.println("4. Adaugă quiz");
            System.out.println("5. Adaugă întrebare");
            System.out.println("6. Afișează nivelul unui cursant");
            System.out.println("7. Afișează cursurile după numărul de cursanți");
            System.out.println("8. Afișează toți cursanții");
            System.out.println("9. Înregistrează cursant la curs");
            System.out.println("0. Ieșire");
            System.out.print("Alege opțiunea: ");
            int opt = Integer.parseInt(scanner.nextLine());

            try {
                switch (opt) {
                    case 1:
                        System.out.print("Nume profesor: ");
                        String numeProf = scanner.nextLine();
                        System.out.print("Email profesor: ");
                        String emailProf = scanner.nextLine();
                        System.out.print("Specializare: ");
                        String spec = scanner.nextLine();
                        Profesor prof = new Profesor(numeProf, emailProf);
                        prof.setSpecializare(spec);
                        profesorDAO.create(prof);
                        System.out.println("Profesor adăugat.");
                        break;
                    case 2:
                        System.out.print("Nume cursant: ");
                        String numeCursant = scanner.nextLine();
                        System.out.print("Email cursant: ");
                        String emailCursant = scanner.nextLine();
                        System.out.print("Nivel: ");
                        String nivel = scanner.nextLine();
                        Cursant cursant = new Cursant(numeCursant, emailCursant);
                        cursant.setNivel(nivel);
                        cursantDAO.create(cursant);
                        System.out.println("Cursant adăugat.");
                        break;
                    case 3:
                        System.out.print("Titlu curs: ");
                        String titluCurs = scanner.nextLine();
                        System.out.print("Descriere curs: ");
                        String descriereCurs = scanner.nextLine();
                        System.out.print("Email profesor (pentru curs): ");
                        String emailProfCurs = scanner.nextLine();
                        Profesor profCurs = profesorDAO.readByEmail(emailProfCurs);
                        if (profCurs == null) {
                            System.out.println("Profesorul nu există!");
                            break;
                        }
                        Curs curs = new Curs();
                        curs.setTitlu(titluCurs);
                        curs.setDescriere(descriereCurs);
                        curs.setProfesor(profCurs);
                        cursDAO.create(curs);
                        System.out.println("Curs adăugat.");
                        break;
                    case 4:
                        System.out.print("Titlu quiz: ");
                        String titluQuiz = scanner.nextLine();
                        System.out.print("Descriere quiz: ");
                        String descriereQuiz = scanner.nextLine();
                        Quiz quiz = new Quiz();
                        quiz.setTitlu(titluQuiz);
                        quiz.setDescriere(descriereQuiz);
                        quizDAO.create(quiz);
                        System.out.println("Quiz adăugat.");
                        break;
                    case 5:
                        System.out.print("Text întrebare: ");
                        String textIntrebare = scanner.nextLine();
                        System.out.print("Punctaj: ");
                        int punctaj = Integer.parseInt(scanner.nextLine());
                        System.out.print("ID quiz: ");
                        int idQuiz = Integer.parseInt(scanner.nextLine());
                        Quiz quizIntrebare = quizDAO.readById(idQuiz);
                        if (quizIntrebare == null) {
                            System.out.println("Quiz-ul nu există!");
                            break;
                        }
                        Intrebare intrebare = new Intrebare();
                        intrebare.setText(textIntrebare);
                        intrebare.setPunctaj(punctaj);
                        intrebare.setQuiz(quizIntrebare);
                        intrebareDAO.create(intrebare);
                        System.out.println("Întrebare adăugată.");
                        break;

                    case 6:
                        System.out.print("Email cursant: ");
                        String emailNivel = scanner.nextLine();
                        Cursant cursantNivel = cursantDAO.readByEmail(emailNivel);
                        if (cursantNivel != null) {
                            System.out.println("Nivelul cursantului: " + cursantNivel.getNivel());
                        } else {
                            System.out.println("Cursantul nu există!");
                        }
                        break;
                    case 7:
                        List<Curs> cursuri = cursDAO.readAll();
                        cursuri.sort((c1, c2) -> {
                            try {
                                int n1 = cursDAO.getNumarCursanti(c1.getId());
                                int n2 = cursDAO.getNumarCursanti(c2.getId());
                                return Integer.compare(n2, n1); // descending
                            } catch (SQLException e) {
                                return 0;
                            }
                        });
                        for (Curs c : cursuri) {
                            int nrCursanti = cursDAO.getNumarCursanti(c.getId());
                            System.out.println(c.getTitlu() + " - " + nrCursanti + " cursanți");
                        }
                        break;
                    case 8:
                        List<Cursant> cursanti = cursantDAO.readAll();
                        for (Cursant c : cursanti) {
                            System.out.println(c.getNume() + " (" + c.getEmail() + ")");
                        }
                        break;
                    case 9:
                        System.out.print("Email student: ");
                        String emailCursantEnroll = scanner.nextLine();
                        System.out.print("Titlu curs: ");
                        String titluCursEnroll = scanner.nextLine();

                        Cursant cursantEnroll = cursantDAO.readByEmail(emailCursantEnroll);
                        Curs cursEnroll = cursDAO.readByTitle(titluCursEnroll);

                        if (cursantEnroll != null && cursEnroll != null) {
                            cursantEnroll.inscrieLaCurs(cursEnroll);
                            cursEnroll.adaugaCursant(cursantEnroll);
                            cursDAO.inscriereLaCurs(cursEnroll.getId(), cursantEnroll.getEmail());
                            System.out.println("Student înrolat cu succes.");
                        } else {
                            System.out.println("Cursantul sau cursul nu există!");
                        }
                        break;
                    case 0:
                        System.out.println("La revedere!");
                        scanner.close();
                        return;
                    default:
                        System.out.println("Opțiune invalidă!");
                }
            } catch (SQLException e) {
                System.out.println("Eroare la accesarea bazei de date: " + e.getMessage());
            }
        }
        } catch (SQLException e) {
            System.out.println("Eroare la inițializarea DAO-urilor: " + e.getMessage());
        }
    }
}

import window.RegistrationWindow;

public class Main {
    public static void main(String[] args) {
        // Запускаем GUI в потоке событий Swing
        javax.swing.SwingUtilities.invokeLater(RegistrationWindow::new);
    }
}

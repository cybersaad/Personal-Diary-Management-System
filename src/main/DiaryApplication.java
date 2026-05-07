package main;

import gui.DashboardFrame;
import javax.swing.SwingUtilities;

public class DiaryApplication {
    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> new DashboardFrame());
    }
}

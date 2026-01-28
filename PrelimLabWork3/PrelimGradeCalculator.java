import javax.swing.*;
import java.awt.*;

public class PrelimGradeCalculator {
    
    public static void main(String[] args) {
        JFrame frame = new JFrame("Prelim Grade Calculator");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setSize(600, 700);
        frame.setLayout(new BorderLayout(10, 10));
        
        Color beigeBackground = new Color(245, 245, 220);
        Color lightBeige = new Color(250, 248, 243);
        Color tanBorder = new Color(212, 197, 169);
        Color brownText = new Color(139, 115, 85);
        Color darkBrownText = new Color(107, 93, 79);
        
        frame.getContentPane().setBackground(beigeBackground);
        
        JPanel mainPanel = new JPanel();
        mainPanel.setLayout(new BoxLayout(mainPanel, BoxLayout.Y_AXIS));
        mainPanel.setBackground(lightBeige);
        mainPanel.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createLineBorder(tanBorder, 2),
            BorderFactory.createEmptyBorder(30, 30, 30, 30)
        ));
        
        JLabel titleLabel = new JLabel("Prelim Grade Calculator");
        titleLabel.setFont(new Font("Segoe UI", Font.BOLD, 24));
        titleLabel.setForeground(brownText);
        titleLabel.setAlignmentX(Component.CENTER_ALIGNMENT);
        mainPanel.add(titleLabel);
        mainPanel.add(Box.createRigidArea(new Dimension(0, 25)));
        
        JLabel attendanceLabel = new JLabel("Number of Attendances:");
        attendanceLabel.setFont(new Font("Segoe UI", Font.BOLD, 14));
        attendanceLabel.setForeground(darkBrownText);
        JTextField attendanceField = new JTextField(20);
        attendanceField.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        attendanceField.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createLineBorder(tanBorder, 1),
            BorderFactory.createEmptyBorder(8, 8, 8, 8)
        ));
        
        JLabel lab1Label = new JLabel("Lab Work 1 Grade:");
        lab1Label.setFont(new Font("Segoe UI", Font.BOLD, 14));
        lab1Label.setForeground(darkBrownText);
        JTextField lab1Field = new JTextField(20);
        lab1Field.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        lab1Field.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createLineBorder(tanBorder, 1),
            BorderFactory.createEmptyBorder(8, 8, 8, 8)
        ));
        
        JLabel lab2Label = new JLabel("Lab Work 2 Grade:");
        lab2Label.setFont(new Font("Segoe UI", Font.BOLD, 14));
        lab2Label.setForeground(darkBrownText);
        JTextField lab2Field = new JTextField(20);
        lab2Field.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        lab2Field.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createLineBorder(tanBorder, 1),
            BorderFactory.createEmptyBorder(8, 8, 8, 8)
        ));
        
        JLabel lab3Label = new JLabel("Lab Work 3 Grade:");
        lab3Label.setFont(new Font("Segoe UI", Font.BOLD, 14));
        lab3Label.setForeground(darkBrownText);
        JTextField lab3Field = new JTextField(20);
        lab3Field.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        lab3Field.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createLineBorder(tanBorder, 1),
            BorderFactory.createEmptyBorder(8, 8, 8, 8)
        ));
        
        mainPanel.add(attendanceLabel);
        mainPanel.add(Box.createRigidArea(new Dimension(0, 5)));
        mainPanel.add(attendanceField);
        mainPanel.add(Box.createRigidArea(new Dimension(0, 15)));
        mainPanel.add(lab1Label);
        mainPanel.add(Box.createRigidArea(new Dimension(0, 5)));
        mainPanel.add(lab1Field);
        mainPanel.add(Box.createRigidArea(new Dimension(0, 15)));
        mainPanel.add(lab2Label);
        mainPanel.add(Box.createRigidArea(new Dimension(0, 5)));
        mainPanel.add(lab2Field);
        mainPanel.add(Box.createRigidArea(new Dimension(0, 15)));
        mainPanel.add(lab3Label);
        mainPanel.add(Box.createRigidArea(new Dimension(0, 5)));
        mainPanel.add(lab3Field);
        mainPanel.add(Box.createRigidArea(new Dimension(0, 20)));
        
        JPanel buttonPanel = new JPanel();
        buttonPanel.setLayout(new FlowLayout(FlowLayout.CENTER, 10, 0));
        buttonPanel.setBackground(lightBeige);
        
        JButton calculateButton = new JButton("Calculate");
        calculateButton.setFont(new Font("Segoe UI", Font.BOLD, 14));
        calculateButton.setPreferredSize(new Dimension(140, 40));
        calculateButton.setBackground(tanBorder);
        calculateButton.setForeground(darkBrownText);
        calculateButton.setFocusPainted(false);
        calculateButton.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createLineBorder(tanBorder, 1),
            BorderFactory.createEmptyBorder(8, 20, 8, 20)
        ));
        
        JButton clearButton = new JButton("Clear");
        clearButton.setFont(new Font("Segoe UI", Font.BOLD, 14));
        clearButton.setPreferredSize(new Dimension(140, 40));
        clearButton.setBackground(beigeBackground);
        clearButton.setForeground(darkBrownText);
        clearButton.setFocusPainted(false);
        clearButton.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createLineBorder(tanBorder, 1),
            BorderFactory.createEmptyBorder(8, 20, 8, 20)
        ));
        
        buttonPanel.add(calculateButton);
        buttonPanel.add(clearButton);
        mainPanel.add(buttonPanel);
        mainPanel.add(Box.createRigidArea(new Dimension(0, 20)));
        
        JTextArea outputArea = new JTextArea(18, 45);
        outputArea.setEditable(false);
        outputArea.setFont(new Font("Monospaced", Font.PLAIN, 12));
        outputArea.setBackground(Color.WHITE);
        outputArea.setForeground(darkBrownText);
        outputArea.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createLineBorder(tanBorder, 1),
            BorderFactory.createEmptyBorder(10, 10, 10, 10)
        ));
        JScrollPane scrollPane = new JScrollPane(outputArea);
        scrollPane.setBorder(BorderFactory.createLineBorder(tanBorder, 1));
        mainPanel.add(scrollPane);
        
        clearButton.addActionListener(e -> {
            attendanceField.setText("");
            lab1Field.setText("");
            lab2Field.setText("");
            lab3Field.setText("");
            outputArea.setText("");
        });
        
        calculateButton.addActionListener(e -> {
            try {
                double attendance = Double.parseDouble(attendanceField.getText());
                double lab1 = Double.parseDouble(lab1Field.getText());
                double lab2 = Double.parseDouble(lab2Field.getText());
                double lab3 = Double.parseDouble(lab3Field.getText());
                
                if (attendance < 0 || lab1 < 0 || lab1 > 100 || lab2 < 0 || lab2 > 100 || lab3 < 0 || lab3 > 100) {
                    outputArea.setText("Error: Invalid input values.\nAttendance must be >= 0\nLab grades must be between 0 and 100");
                    return;
                }
                
                double labWorkAverage = (lab1 + lab2 + lab3) / 3.0;
                
                double classStanding = (attendance * 0.40) + (labWorkAverage * 0.60);
                
                double requiredExamToPass = (75 - (classStanding * 0.70)) / 0.30;
                
                double requiredExamForExcellent = (100 - (classStanding * 0.70)) / 0.30;
                
                StringBuilder output = new StringBuilder();
                output.append("═══════════════════════════════════════════\n");
                output.append("        PRELIM GRADE CALCULATION\n");
                output.append("═══════════════════════════════════════════\n\n");
                
                output.append("INPUT VALUES:\n");
                output.append(String.format("  Attendance:        %.2f", attendance));
                
                if (attendance >= 12) {
                    output.append(" (PASSED)\n");
                } else {
                    output.append(" (FAILED)\n");
                }
                
                output.append(String.format("  Lab Work 1:        %.2f\n", lab1));
                output.append(String.format("  Lab Work 2:        %.2f\n", lab2));
                output.append(String.format("  Lab Work 3:        %.2f\n\n", lab3));
                
                output.append("COMPUTED RESULTS:\n");
                output.append(String.format("  Lab Work Average:  %.2f\n", labWorkAverage));
                output.append(String.format("  Class Standing:    %.2f\n\n", classStanding));
                
                output.append("REQUIRED PRELIM EXAM SCORES:\n");
                output.append(String.format("  To Pass (75):      %.2f\n", requiredExamToPass));
                output.append(String.format("  For Excellent (100): %.2f\n\n", requiredExamForExcellent));
                
                output.append("REMARKS:\n");
                
                if (requiredExamToPass <= 100 && requiredExamToPass >= 0) {
                    output.append(String.format("  ✓ You need %.2f%% on the Prelim Exam to pass.\n", requiredExamToPass));
                } else if (requiredExamToPass < 0) {
                    output.append("  ✓ You have already secured a passing grade!\n");
                } else {
                    output.append("  ✗ It is not possible to pass the Prelim period.\n");
                    output.append("    (Required score exceeds 100%%)\n");
                }
                
                if (requiredExamForExcellent <= 100 && requiredExamForExcellent >= 0) {
                    output.append(String.format("  ✓ You need %.2f%% on the Prelim Exam for excellent.\n", requiredExamForExcellent));
                } else if (requiredExamForExcellent < 0) {
                    output.append("  ✓ You have already achieved excellent standing!\n");
                } else {
                    output.append("  ✗ It is not possible to achieve excellent standing.\n");
                    output.append("    (Required score exceeds 100%%)\n");
                }
                
                output.append("\n═══════════════════════════════════════════");
                
                outputArea.setText(output.toString());
                
            } catch (NumberFormatException ex) {
                outputArea.setText("Error: Please enter valid numeric values for all fields.");
            }
        });
        
        frame.add(mainPanel, BorderLayout.CENTER);
        frame.setLocationRelativeTo(null);
        frame.setVisible(true);
    }
}
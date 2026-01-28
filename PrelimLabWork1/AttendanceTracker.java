import javax.swing.*;
import java.awt.*;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.UUID;

/**
 * AttendanceTracker - A Java Swing application for tracking attendance
 * This application displays a form with attendance information including
 * name, course/year, time in, and a generated e-signature.
 */
public class AttendanceTracker {
    
    // Declare UI components
    private JFrame frame;
    private JTextField nameField;
    private JTextField courseField;
    private JTextField timeInField;
    private JTextField eSignatureField;
    
    /**
     * Constructor - initializes and displays the attendance tracker window
     */
    public AttendanceTracker() {
        createAndShowGUI();
    }
    
    /**
     * Creates and displays the graphical user interface
     */
    private void createAndShowGUI() {
        // Create the main frame
        frame = new JFrame("Attendance Tracker");
        frame.setSize(450, 300);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setLocationRelativeTo(null); // Center the window on screen
        
        // Create main panel with padding
        JPanel mainPanel = new JPanel();
        mainPanel.setLayout(new BorderLayout(10, 10));
        mainPanel.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));
        
        // Create form panel for input fields
        JPanel formPanel = new JPanel();
        formPanel.setLayout(new GridLayout(4, 2, 10, 15)); // 4 rows, 2 columns, with gaps
        
        // Create and add Attendance Name field
        JLabel nameLabel = new JLabel("Attendance Name:");
        nameLabel.setFont(new Font("Arial", Font.BOLD, 14));
        nameField = new JTextField(20);
        formPanel.add(nameLabel);
        formPanel.add(nameField);
        
        // Create and add Course/Year field
        JLabel courseLabel = new JLabel("Course/Year:");
        courseLabel.setFont(new Font("Arial", Font.BOLD, 14));
        courseField = new JTextField(20);
        formPanel.add(courseLabel);
        formPanel.add(courseField);
        
        // Create and add Time In field (auto-filled with current date/time)
        JLabel timeInLabel = new JLabel("Time In:");
        timeInLabel.setFont(new Font("Arial", Font.BOLD, 14));
        timeInField = new JTextField(20);
        timeInField.setEditable(false); // Make it read-only
        timeInField.setBackground(Color.LIGHT_GRAY); // Visual indicator it's read-only
        
        // Get current date and time and format it
        LocalDateTime now = LocalDateTime.now();
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
        String timeIn = now.format(formatter);
        timeInField.setText(timeIn);
        
        formPanel.add(timeInLabel);
        formPanel.add(timeInField);
        
        // Create and add E-Signature field (auto-generated)
        JLabel eSignatureLabel = new JLabel("E-Signature:");
        eSignatureLabel.setFont(new Font("Arial", Font.BOLD, 14));
        eSignatureField = new JTextField(20);
        eSignatureField.setEditable(false); // Make it read-only
        eSignatureField.setBackground(Color.LIGHT_GRAY); // Visual indicator it's read-only
        
        // Generate unique e-signature using UUID
        String eSignature = UUID.randomUUID().toString();
        eSignatureField.setText(eSignature);
        
        formPanel.add(eSignatureLabel);
        formPanel.add(eSignatureField);
        
        // Create title panel
        JPanel titlePanel = new JPanel();
        JLabel titleLabel = new JLabel("Attendance Tracking System");
        titleLabel.setFont(new Font("Arial", Font.BOLD, 18));
        titleLabel.setForeground(new Color(0, 102, 204));
        titlePanel.add(titleLabel);
        
        // Create button panel
        JPanel buttonPanel = new JPanel();
        buttonPanel.setLayout(new FlowLayout(FlowLayout.CENTER, 10, 10));
        
        // Add a Submit button for user interaction
        JButton submitButton = new JButton("Submit Attendance");
        submitButton.setFont(new Font("Arial", Font.BOLD, 12));
        submitButton.addActionListener(e -> handleSubmit());
        
        // Add a Clear button to reset fields
        JButton clearButton = new JButton("Clear");
        clearButton.setFont(new Font("Arial", Font.BOLD, 12));
        clearButton.addActionListener(e -> handleClear());
        
        // Add a View Records button to open the saved file
        JButton viewButton = new JButton("View Records");
        viewButton.setFont(new Font("Arial", Font.BOLD, 12));
        viewButton.addActionListener(e -> viewRecordsFile());
        
        buttonPanel.add(submitButton);
        buttonPanel.add(clearButton);
        buttonPanel.add(viewButton);
        
        // Add all panels to main panel
        mainPanel.add(titlePanel, BorderLayout.NORTH);
        mainPanel.add(formPanel, BorderLayout.CENTER);
        mainPanel.add(buttonPanel, BorderLayout.SOUTH);
        
        // Add main panel to frame
        frame.add(mainPanel);
        
        // Make the frame visible
        frame.setVisible(true);
    }
    
    /**
     * Handles the submit button action
     * Displays the attendance information and saves it to a text file
     */
    private void handleSubmit() {
        // Get values from fields
        String name = nameField.getText().trim();
        String course = courseField.getText().trim();
        String timeIn = timeInField.getText();
        String eSignature = eSignatureField.getText();
        
        // Validate input
        if (name.isEmpty() || course.isEmpty()) {
            JOptionPane.showMessageDialog(frame, 
                "Please fill in all required fields (Name and Course/Year).", 
                "Input Required", 
                JOptionPane.WARNING_MESSAGE);
            return;
        }
        
        // Save to text file
        boolean saved = saveToFile(name, course, timeIn, eSignature);
        
        if (saved) {
            // Display attendance information
            String message = String.format(
                "Attendance Recorded Successfully!\n\n" +
                "Name: %s\n" +
                "Course/Year: %s\n" +
                "Time In: %s\n" +
                "E-Signature: %s\n\n" +
                "Record saved to: attendance_records.txt",
                name, course, timeIn, eSignature
            );
            
            JOptionPane.showMessageDialog(frame, 
                message, 
                "Attendance Confirmation", 
                JOptionPane.INFORMATION_MESSAGE);
            
            // Clear fields after successful submission
            handleClear();
        } else {
            JOptionPane.showMessageDialog(frame, 
                "Error saving attendance record. Please try again.", 
                "Save Error", 
                JOptionPane.ERROR_MESSAGE);
        }
    }
    
    /**
     * Saves attendance record to a text file
     * @param name The student's name
     * @param course The course/year information
     * @param timeIn The time of attendance
     * @param eSignature The unique e-signature
     * @return true if save was successful, false otherwise
     */
    private boolean saveToFile(String name, String course, String timeIn, String eSignature) {
        try {
            // Open file in append mode (true parameter means append, not overwrite)
            FileWriter fileWriter = new FileWriter("attendance_records.txt", true);
            PrintWriter printWriter = new PrintWriter(fileWriter);
            
            // Write header separator for readability
            printWriter.println("========================================");
            printWriter.println("ATTENDANCE RECORD");
            printWriter.println("========================================");
            
            // Write attendance data
            printWriter.println("Name: " + name);
            printWriter.println("Course/Year: " + course);
            printWriter.println("Time In: " + timeIn);
            printWriter.println("E-Signature: " + eSignature);
            printWriter.println("========================================");
            printWriter.println(); // Add blank line between records
            
            // Close the file
            printWriter.close();
            
            return true; // Save successful
            
        } catch (IOException e) {
            // If there's an error writing to file, print error details
            System.err.println("Error saving to file: " + e.getMessage());
            e.printStackTrace();
            return false; // Save failed
        }
    }
    
    /**
     * Opens the attendance records file in the default text editor (Notepad)
     */
    private void viewRecordsFile() {
        try {
            // Check if file exists
            java.io.File file = new java.io.File("attendance_records.txt");
            
            if (!file.exists()) {
                JOptionPane.showMessageDialog(frame, 
                    "No attendance records found yet.\nSubmit an attendance to create the file.", 
                    "No Records", 
                    JOptionPane.INFORMATION_MESSAGE);
                return;
            }
            
            // Open file with default application (Notepad on Windows)
            if (Desktop.isDesktopSupported()) {
                Desktop desktop = Desktop.getDesktop();
                desktop.open(file);
            } else {
                JOptionPane.showMessageDialog(frame, 
                    "Cannot open file automatically.\n" +
                    "Please open 'attendance_records.txt' manually from your project folder.", 
                    "Cannot Open File", 
                    JOptionPane.WARNING_MESSAGE);
            }
            
        } catch (IOException e) {
            JOptionPane.showMessageDialog(frame, 
                "Error opening file: " + e.getMessage(), 
                "Error", 
                JOptionPane.ERROR_MESSAGE);
        }
    }
    
    /**
     * Handles the clear button action
     * Resets input fields and generates new time and e-signature
     */
    private void handleClear() {
        // Clear editable fields
        nameField.setText("");
        courseField.setText("");
        
        // Regenerate time and e-signature
        LocalDateTime now = LocalDateTime.now();
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
        String timeIn = now.format(formatter);
        timeInField.setText(timeIn);
        
        String eSignature = UUID.randomUUID().toString();
        eSignatureField.setText(eSignature);
        
        // Set focus to name field
        nameField.requestFocus();
    }
    
    /**
     * Main method - entry point of the application
     */
    public static void main(String[] args) {
        // Use SwingUtilities.invokeLater to ensure thread safety
        SwingUtilities.invokeLater(() -> {
            new AttendanceTracker();
        });
    }
}
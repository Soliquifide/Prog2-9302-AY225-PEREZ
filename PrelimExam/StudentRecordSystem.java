// Programmer: Mycho Louise D. Perez - 25-2218-250
// Student Record System - Java Swing Implementation

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.io.BufferedReader;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;

public class StudentRecordSystem extends JFrame {
    private JTable table;
    private DefaultTableModel tableModel;
    private DefaultTableModel allDataModel; // Store all data for filtering
    private JTextField txtStudentID, txtFirstName, txtLastName, txtSearch;
    private JButton btnAdd, btnDelete, btnSearch, btnClearSearch, btnEdit, btnCancelEdit;
    private int editingRow = -1; // Track which row is being edited
    
    public StudentRecordSystem() {
        // Set window title with programmer identifier
        this.setTitle("Student Records - Mycho Louise D. Perez [25-2218-250]");
        this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        this.setSize(1200, 600);
        this.setLayout(new BorderLayout());
        
        // Initialize table model with column names
        String[] columns = {"StudentID", "First Name", "Last Name", "LAB WORK 1", 
                          "LAB WORK 2", "LAB WORK 3", "PRELIM EXAM", "ATTENDANCE GRADE"};
        tableModel = new DefaultTableModel(columns, 0);
        allDataModel = new DefaultTableModel(columns, 0); // For storing all data
        table = new JTable(tableModel);
        
        // Add table to scroll pane
        JScrollPane scrollPane = new JScrollPane(table);
        this.add(scrollPane, BorderLayout.CENTER);
        
        // Create search panel at the top
        JPanel searchPanel = new JPanel();
        searchPanel.setLayout(new FlowLayout(FlowLayout.LEFT));
        searchPanel.setBackground(new Color(240, 240, 240));
        
        txtSearch = new JTextField(30);
        btnSearch = new JButton("Search");
        btnClearSearch = new JButton("Clear Filter");
        
        searchPanel.add(new JLabel("🔍 Search:"));
        searchPanel.add(txtSearch);
        searchPanel.add(btnSearch);
        searchPanel.add(btnClearSearch);
        
        this.add(searchPanel, BorderLayout.NORTH);
        
        // Create input panel
        JPanel inputPanel = new JPanel();
        inputPanel.setLayout(new FlowLayout());
        
        // Create text fields
        txtStudentID = new JTextField(15);
        txtFirstName = new JTextField(15);
        txtLastName = new JTextField(15);
        
        // Create buttons
        btnAdd = new JButton("Add");
        btnEdit = new JButton("Edit Selected");
        btnDelete = new JButton("Delete");
        btnCancelEdit = new JButton("Cancel Edit");
        btnCancelEdit.setVisible(false); // Hidden until editing
        
        // Add components to input panel
        inputPanel.add(new JLabel("Student ID:"));
        inputPanel.add(txtStudentID);
        inputPanel.add(new JLabel("First Name:"));
        inputPanel.add(txtFirstName);
        inputPanel.add(new JLabel("Last Name:"));
        inputPanel.add(txtLastName);
        inputPanel.add(btnAdd);
        inputPanel.add(btnEdit);
        inputPanel.add(btnDelete);
        inputPanel.add(btnCancelEdit);
        
        this.add(inputPanel, BorderLayout.SOUTH);
        
        // Add button action listeners
        btnAdd.addActionListener(e -> addStudent());
        btnEdit.addActionListener(e -> editStudent());
        btnDelete.addActionListener(e -> deleteStudent());
        btnSearch.addActionListener(e -> searchStudents());
        btnClearSearch.addActionListener(e -> clearSearch());
        btnCancelEdit.addActionListener(e -> cancelEdit());
        
        // Add Enter key listener to search field
        txtSearch.addActionListener(e -> searchStudents());
        
        // Load data from CSV file
        loadCSVData();
        
        this.setLocationRelativeTo(null);
        this.setVisible(true);
    }
    
    private void loadCSVData() {
        try (BufferedReader br = new BufferedReader(new FileReader("Prog2-9302-AY225-Perez/MOCK_DATA.csv"))) {
            String line;
            boolean isFirstLine = true;
            
            while ((line = br.readLine()) != null) {
                // Skip header line
                if (isFirstLine) {
                    isFirstLine = false;
                    continue;
                }
                
                // Split the CSV line
                String[] data = line.split(",");
                
                // Add row to both table models if we have enough data
                if (data.length >= 8) {
                    tableModel.addRow(data);
                    allDataModel.addRow(data); // Store in allDataModel too
                }
            }
            
            JOptionPane.showMessageDialog(this, 
                "CSV data loaded successfully! Total records: " + tableModel.getRowCount(),
                "Success", JOptionPane.INFORMATION_MESSAGE);
                
        } catch (IOException e) {
            JOptionPane.showMessageDialog(this, 
                "Error reading CSV file: " + e.getMessage(),
                "File Error", JOptionPane.ERROR_MESSAGE);
            e.printStackTrace();
        }
    }
    
    private void addStudent() {
        String studentID = txtStudentID.getText().trim();
        String firstName = txtFirstName.getText().trim();
        String lastName = txtLastName.getText().trim();
        
        if (studentID.isEmpty() || firstName.isEmpty() || lastName.isEmpty()) {
            JOptionPane.showMessageDialog(this, 
                "Please fill in Student ID, First Name, and Last Name",
                "Input Error", JOptionPane.WARNING_MESSAGE);
            return;
        }
        
        if (editingRow != -1) {
            // We're in edit mode, call update instead
            updateStudent();
            return;
        }
        
        // Create new row with default grades of 0
        Object[] newRow = {studentID, firstName, lastName, "0", "0", "0", "0", "0"};
        tableModel.addRow(newRow);
        allDataModel.addRow(newRow); // FIXED: Also add to allDataModel for search
        
        // Save to CSV file
        saveToCSV();
        
        // Clear input fields
        txtStudentID.setText("");
        txtFirstName.setText("");
        txtLastName.setText("");
        
        JOptionPane.showMessageDialog(this, 
            "Student added successfully and saved to CSV!",
            "Success", JOptionPane.INFORMATION_MESSAGE);
    }
    
    private void editStudent() {
        int selectedRow = table.getSelectedRow();
        
        if (selectedRow == -1) {
            JOptionPane.showMessageDialog(this, 
                "Please select a row to edit",
                "Selection Error", JOptionPane.WARNING_MESSAGE);
            return;
        }
        
        // Load data into text fields
        txtStudentID.setText(tableModel.getValueAt(selectedRow, 0).toString());
        txtFirstName.setText(tableModel.getValueAt(selectedRow, 1).toString());
        txtLastName.setText(tableModel.getValueAt(selectedRow, 2).toString());
        
        // Store which row we're editing
        editingRow = selectedRow;
        
        // Change button appearance
        btnAdd.setText("Update");
        btnAdd.setBackground(new Color(255, 152, 0)); // Orange color
        btnEdit.setEnabled(false);
        btnDelete.setEnabled(false);
        btnCancelEdit.setVisible(true);
        
        JOptionPane.showMessageDialog(this, 
            "Edit mode activated. Modify the fields and click 'Update'",
            "Edit Mode", JOptionPane.INFORMATION_MESSAGE);
    }
    
    private void updateStudent() {
        String studentID = txtStudentID.getText().trim();
        String firstName = txtFirstName.getText().trim();
        String lastName = txtLastName.getText().trim();
        
        if (studentID.isEmpty() || firstName.isEmpty() || lastName.isEmpty()) {
            JOptionPane.showMessageDialog(this, 
                "Please fill in Student ID, First Name, and Last Name",
                "Input Error", JOptionPane.WARNING_MESSAGE);
            return;
        }
        
        // Get old student ID to find in allDataModel
        String oldStudentID = tableModel.getValueAt(editingRow, 0).toString();
        
        // Update the display table
        tableModel.setValueAt(studentID, editingRow, 0);
        tableModel.setValueAt(firstName, editingRow, 1);
        tableModel.setValueAt(lastName, editingRow, 2);
        
        // Update allDataModel
        for (int i = 0; i < allDataModel.getRowCount(); i++) {
            if (allDataModel.getValueAt(i, 0).toString().equals(oldStudentID)) {
                allDataModel.setValueAt(studentID, i, 0);
                allDataModel.setValueAt(firstName, i, 1);
                allDataModel.setValueAt(lastName, i, 2);
                break;
            }
        }
        
        // Save to CSV
        saveToCSV();
        
        // Reset edit mode
        cancelEdit();
        
        JOptionPane.showMessageDialog(this, 
            "Student updated successfully and saved to CSV!",
            "Success", JOptionPane.INFORMATION_MESSAGE);
    }
    
    private void cancelEdit() {
        editingRow = -1;
        
        // Clear fields
        txtStudentID.setText("");
        txtFirstName.setText("");
        txtLastName.setText("");
        
        // Reset button appearance
        btnAdd.setText("Add");
        btnAdd.setBackground(null);
        btnEdit.setEnabled(true);
        btnDelete.setEnabled(true);
        btnCancelEdit.setVisible(false);
    }
    
    private void deleteStudent() {
        int selectedRow = table.getSelectedRow();
        
        if (selectedRow == -1) {
            JOptionPane.showMessageDialog(this, 
                "Please select a row to delete",
                "Selection Error", JOptionPane.WARNING_MESSAGE);
            return;
        }
        
        int confirm = JOptionPane.showConfirmDialog(this,
            "Are you sure you want to delete this student record?",
            "Confirm Deletion", JOptionPane.YES_NO_OPTION);
            
        if (confirm == JOptionPane.YES_OPTION) {
            // Get the student ID from the selected row to find it in allDataModel
            String studentID = tableModel.getValueAt(selectedRow, 0).toString();
            
            // Remove from display table
            tableModel.removeRow(selectedRow);
            
            // FIXED: Also remove from allDataModel
            for (int i = 0; i < allDataModel.getRowCount(); i++) {
                if (allDataModel.getValueAt(i, 0).toString().equals(studentID)) {
                    allDataModel.removeRow(i);
                    break;
                }
            }
            
            // Save to CSV file
            saveToCSV();
            
            JOptionPane.showMessageDialog(this, 
                "Student record deleted successfully and CSV updated!",
                "Success", JOptionPane.INFORMATION_MESSAGE);
        }
    }
    
    private void searchStudents() {
        String searchTerm = txtSearch.getText().trim().toLowerCase();
        
        if (searchTerm.isEmpty()) {
            JOptionPane.showMessageDialog(this, 
                "Please enter a search term",
                "Search Error", JOptionPane.WARNING_MESSAGE);
            return;
        }
        
        // Clear current table
        tableModel.setRowCount(0);
        
        int matchCount = 0;
        // Search through all data
        for (int i = 0; i < allDataModel.getRowCount(); i++) {
            boolean found = false;
            
            // Check each column for the search term
            for (int j = 0; j < allDataModel.getColumnCount(); j++) {
                String value = allDataModel.getValueAt(i, j).toString().toLowerCase();
                if (value.contains(searchTerm)) {
                    found = true;
                    break;
                }
            }
            
            // If found, add row to display table
            if (found) {
                Object[] rowData = new Object[allDataModel.getColumnCount()];
                for (int j = 0; j < allDataModel.getColumnCount(); j++) {
                    rowData[j] = allDataModel.getValueAt(i, j);
                }
                tableModel.addRow(rowData);
                matchCount++;
            }
        }
        
        JOptionPane.showMessageDialog(this, 
            "Found " + matchCount + " matching record(s)",
            "Search Results", JOptionPane.INFORMATION_MESSAGE);
    }
    
    private void clearSearch() {
        txtSearch.setText("");
        
        // Clear table and reload all data
        tableModel.setRowCount(0);
        
        for (int i = 0; i < allDataModel.getRowCount(); i++) {
            Object[] rowData = new Object[allDataModel.getColumnCount()];
            for (int j = 0; j < allDataModel.getColumnCount(); j++) {
                rowData[j] = allDataModel.getValueAt(i, j);
            }
            tableModel.addRow(rowData);
        }
        
        JOptionPane.showMessageDialog(this, 
            "Filter cleared. Showing all " + tableModel.getRowCount() + " records",
            "Success", JOptionPane.INFORMATION_MESSAGE);
    }
    
    private void saveToCSV() {
        try (PrintWriter writer = new PrintWriter(new FileWriter("MOCK_DATA.csv"))) {
            // Write header
            writer.println("StudentID,first_name,last_name,LAB WORK 1,LAB WORK 2,LAB WORK 3,PRELIM EXAM,ATTENDANCE GRADE");
            
            // Write all data from allDataModel
            for (int i = 0; i < allDataModel.getRowCount(); i++) {
                StringBuilder line = new StringBuilder();
                for (int j = 0; j < allDataModel.getColumnCount(); j++) {
                    line.append(allDataModel.getValueAt(i, j));
                    if (j < allDataModel.getColumnCount() - 1) {
                        line.append(",");
                    }
                }
                writer.println(line.toString());
            }
            
            System.out.println("CSV file updated successfully!");
            
        } catch (IOException e) {
            JOptionPane.showMessageDialog(this, 
                "Error saving to CSV file: " + e.getMessage(),
                "Save Error", JOptionPane.ERROR_MESSAGE);
            e.printStackTrace();
        }
    }
    
    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> new StudentRecordSystem());
    }
}

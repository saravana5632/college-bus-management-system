import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.sql.*;

public class BusCRUDGUI extends JFrame {

    private static final String DB_URL = "jdbc:mysql://localhost:3306/college_bus_db";
    private static final String USER = "root";
    private static final String PASS = "Your_Password";

    private Connection conn;

    private JTextField txtBusNo;
    private JTextField txtSource;
    private JTextField txtDestination;
    private JTextField txtSourceTime;
    private JTextField txtDestTime;
    private JTable busTable;
    private DefaultTableModel tableModel;

    public BusCRUDGUI() {
        setTitle("College Bus Schedule Management System");
        setSize(950, 600);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);
        setLayout(new BorderLayout(10, 10));

        initDBConnection();
        buildUI();
        loadTableData();
    }

    private void initDBConnection() {
        try {
            conn = DriverManager.getConnection(DB_URL, USER, PASS);
        } catch (SQLException e) {
            JOptionPane.showMessageDialog(this,
                    "Database connection failed!\n" + e.getMessage(),
                    "Connection Error",
                    JOptionPane.ERROR_MESSAGE);
        }
    }

    private void buildUI() {
        JPanel formPanel = new JPanel(new GridLayout(5, 2, 8, 8));
        formPanel.setBorder(BorderFactory.createTitledBorder("Route Details"));

        formPanel.add(new JLabel("Bus No:"));
        txtBusNo = new JTextField();
        formPanel.add(txtBusNo);

        formPanel.add(new JLabel("Source:"));
        txtSource = new JTextField();
        formPanel.add(txtSource);

        formPanel.add(new JLabel("Destination:"));
        txtDestination = new JTextField();
        formPanel.add(txtDestination);

        formPanel.add(new JLabel("Start Time (e.g., 5.30 a.m.):"));
        txtSourceTime = new JTextField();
        formPanel.add(txtSourceTime);

        formPanel.add(new JLabel("Arrival Time (e.g., 7.15 a.m.):"));
        txtDestTime = new JTextField();
        formPanel.add(txtDestTime);

        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.CENTER, 10, 10));
        JButton btnAdd = new JButton("Add Route");
        JButton btnUpdate = new JButton("Update Selected");
        JButton btnDelete = new JButton("Delete Selected");
        JButton btnClear = new JButton("Clear Fields");
        JButton btnLoadCSV = new JButton("Import CSV");
        JButton btnRefresh = new JButton("Refresh");

        buttonPanel.add(btnAdd);
        buttonPanel.add(btnUpdate);
        buttonPanel.add(btnDelete);
        buttonPanel.add(btnClear);
        buttonPanel.add(btnLoadCSV);
        buttonPanel.add(btnRefresh);

        JPanel topContainer = new JPanel(new BorderLayout());
        topContainer.add(formPanel, BorderLayout.CENTER);
        topContainer.add(buttonPanel, BorderLayout.SOUTH);
        add(topContainer, BorderLayout.NORTH);

        String[] columnNames = {"Bus No", "Source", "Destination", "Start Time", "Arrival Time"};
        tableModel = new DefaultTableModel(columnNames, 0) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false;
            }
        };
        busTable = new JTable(tableModel);
        busTable.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);

        busTable.getSelectionModel().addListSelectionListener(e -> {
            int selectedRow = busTable.getSelectedRow();
            if (selectedRow != -1) {
                txtBusNo.setText(tableModel.getValueAt(selectedRow, 0).toString());
                txtSource.setText(tableModel.getValueAt(selectedRow, 1).toString());
                txtDestination.setText(tableModel.getValueAt(selectedRow, 2).toString());
                txtSourceTime.setText(tableModel.getValueAt(selectedRow, 3).toString());
                txtDestTime.setText(tableModel.getValueAt(selectedRow, 4).toString());
            }
        });

        JScrollPane scrollPane = new JScrollPane(busTable);
        scrollPane.setBorder(BorderFactory.createTitledBorder("Bus Schedule Records"));
        add(scrollPane, BorderLayout.CENTER);

        btnAdd.addActionListener(e -> addBus());
        btnUpdate.addActionListener(e -> updateBus());
        btnDelete.addActionListener(e -> deleteBus());
        btnClear.addActionListener(e -> clearFields());
        btnLoadCSV.addActionListener(e -> importCSV());
        btnRefresh.addActionListener(e -> loadTableData());
    }

    private void addBus() {
        if (!validateInputs()) return;

        String query = "INSERT INTO buses (bus_no, source, destination, source_time, destination_time) VALUES (?, ?, ?, ?, ?)";
        try (PreparedStatement pstmt = conn.prepareStatement(query)) {
            pstmt.setString(1, txtBusNo.getText().trim());
            pstmt.setString(2, txtSource.getText().trim());
            pstmt.setString(3, txtDestination.getText().trim());
            pstmt.setString(4, txtSourceTime.getText().trim());
            pstmt.setString(5, txtDestTime.getText().trim());

            pstmt.executeUpdate();
            JOptionPane.showMessageDialog(this, "Bus route added successfully!");
            clearFields();
            loadTableData();
        } catch (SQLIntegrityConstraintViolationException e) {
            JOptionPane.showMessageDialog(this, "Bus No '" + txtBusNo.getText() + "' already exists!", "Duplicate Key", JOptionPane.WARNING_MESSAGE);
        } catch (SQLException e) {
            showError("Insert failed", e);
        }
    }

    private void loadTableData() {
        tableModel.setRowCount(0);
        String query = "SELECT bus_no, source, destination, source_time, destination_time FROM buses ORDER BY CAST(bus_no AS UNSIGNED)";

        try (Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(query)) {

            while (rs.next()) {
                tableModel.addRow(new Object[]{
                        rs.getString("bus_no"),
                        rs.getString("source"),
                        rs.getString("destination"),
                        rs.getString("source_time"),
                        rs.getString("destination_time")
                });
            }
        } catch (SQLException e) {
            showError("Failed to fetch records", e);
        }
    }

    private void updateBus() {
        if (!validateInputs()) return;

        String query = "UPDATE buses SET source = ?, destination = ?, source_time = ?, destination_time = ? WHERE bus_no = ?";
        try (PreparedStatement pstmt = conn.prepareStatement(query)) {
            pstmt.setString(1, txtSource.getText().trim());
            pstmt.setString(2, txtDestination.getText().trim());
            pstmt.setString(3, txtSourceTime.getText().trim());
            pstmt.setString(4, txtDestTime.getText().trim());
            pstmt.setString(5, txtBusNo.getText().trim());

            int rows = pstmt.executeUpdate();
            if (rows > 0) {
                JOptionPane.showMessageDialog(this, "Bus No '" + txtBusNo.getText() + "' updated successfully!");
                clearFields();
                loadTableData();
            } else {
                JOptionPane.showMessageDialog(this, "Bus No '" + txtBusNo.getText() + "' not found.", "Not Found", JOptionPane.WARNING_MESSAGE);
            }
        } catch (SQLException e) {
            showError("Update failed", e);
        }
    }

    private void deleteBus() {
        String busNo = txtBusNo.getText().trim();
        if (busNo.isEmpty()) {
            JOptionPane.showMessageDialog(this, "Please enter or select a Bus No to delete.", "Warning", JOptionPane.WARNING_MESSAGE);
            return;
        }

        int confirm = JOptionPane.showConfirmDialog(this,
                "Are you sure you want to delete Bus No: " + busNo + "?",
                "Confirm Delete",
                JOptionPane.YES_NO_OPTION);

        if (confirm != JOptionPane.YES_OPTION) return;

        String query = "DELETE FROM buses WHERE bus_no = ?";
        try (PreparedStatement pstmt = conn.prepareStatement(query)) {
            pstmt.setString(1, busNo);
            int rows = pstmt.executeUpdate();

            if (rows > 0) {
                JOptionPane.showMessageDialog(this, "Bus deleted successfully!");
                clearFields();
                loadTableData();
            } else {
                JOptionPane.showMessageDialog(this, "Bus No '" + busNo + "' not found.", "Not Found", JOptionPane.WARNING_MESSAGE);
            }
        } catch (SQLException e) {
            showError("Delete failed", e);
        }
    }

    private void importCSV() {
        JFileChooser fileChooser = new JFileChooser();
        fileChooser.setDialogTitle("Select Bus Timings CSV File");

        int userSelection = fileChooser.showOpenDialog(this);
        if (userSelection != JFileChooser.APPROVE_OPTION) return;

        File csvFile = fileChooser.getSelectedFile();
        String insertQuery = "REPLACE INTO buses (bus_no, source, destination, source_time, destination_time) VALUES (?, ?, ?, ?, ?)";

        try (BufferedReader br = new BufferedReader(new FileReader(csvFile));
             PreparedStatement pstmt = conn.prepareStatement(insertQuery)) {

            br.readLine();
            String line;
            int count = 0;

            while ((line = br.readLine()) != null) {
                String[] busData = line.split(",");
                if (busData.length >= 6) {
                    pstmt.setString(1, busData[1].trim());
                    pstmt.setString(2, busData[2].trim());
                    pstmt.setString(3, busData[3].trim());
                    pstmt.setString(4, busData[4].trim());
                    pstmt.setString(5, busData[5].trim());
                    pstmt.addBatch();
                    count++;
                }
            }

            pstmt.executeBatch();
            JOptionPane.showMessageDialog(this, "Loaded " + count + " routes from CSV!");
            loadTableData();

        } catch (IOException | SQLException e) {
            showError("Error importing CSV file", e);
        }
    }

    private boolean validateInputs() {
        if (txtBusNo.getText().trim().isEmpty() ||
            txtSource.getText().trim().isEmpty() ||
            txtDestination.getText().trim().isEmpty() ||
            txtSourceTime.getText().trim().isEmpty() ||
            txtDestTime.getText().trim().isEmpty()) {

            JOptionPane.showMessageDialog(this, "All input fields are required!", "Validation Error", JOptionPane.WARNING_MESSAGE);
            return false;
        }
        return true;
    }

    private void clearFields() {
        txtBusNo.setText("");
        txtSource.setText("");
        txtDestination.setText("");
        txtSourceTime.setText("");
        txtDestTime.setText("");
        busTable.clearSelection();
    }

    private void showError(String title, Exception e) {
        JOptionPane.showMessageDialog(this, title + ":\n" + e.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
    }

    public static void main(String[] args) {
        try {
            UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
        } catch (Exception ignored) {}

        SwingUtilities.invokeLater(() -> new BusCRUDGUI().setVisible(true));
    }
}
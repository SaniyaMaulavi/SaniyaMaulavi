import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.awt.event.*;
import java.sql.*;

public class HotelManagementJDBC {
    private JFrame frame;
    private JTable table;
    private DefaultTableModel tableModel;
    private Connection connection;

    // Database connection details
    private final String URL = "jdbc:mysql://localhost:3306/hotel_management";
    private final String USER = "root";  // Change to your MySQL username
    private final String PASSWORD = "password";  // Change to your MySQL password

    public HotelManagementJDBC() {
        initializeDB();
        initializeGUI();
        loadCustomers();
    }

    private void initializeDB() {
        try {
            connection = DriverManager.getConnection(URL, USER, PASSWORD);
        } catch (SQLException e) {
            JOptionPane.showMessageDialog(null, "Database Connection Failed!", "Error", JOptionPane.ERROR_MESSAGE);
            System.exit(1);
        }
    }

    private void initializeGUI() {
        frame = new JFrame("Hotel Management System (JDBC)");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setSize(600, 400);
        frame.setLayout(new BorderLayout());

        // Table setup
        String[] columnNames = {"ID", "Name", "Room Type", "Days"};
        tableModel = new DefaultTableModel(columnNames, 0);
        table = new JTable(tableModel);
        JScrollPane scrollPane = new JScrollPane(table);
        frame.add(scrollPane, BorderLayout.CENTER);

        // Form panel
        JPanel panel = new JPanel(new GridLayout(5, 2, 10, 10));
        JTextField idField = new JTextField();
        JTextField nameField = new JTextField();
        JTextField roomTypeField = new JTextField();
        JTextField daysField = new JTextField();

        panel.add(new JLabel("Customer ID:"));
        panel.add(idField);
        panel.add(new JLabel("Name:"));
        panel.add(nameField);
        panel.add(new JLabel("Room Type (Single/Double/Suite):"));
        panel.add(roomTypeField);
        panel.add(new JLabel("Number of Days:"));
        panel.add(daysField);

        JButton addButton = new JButton("Add Customer");
        JButton removeButton = new JButton("Remove Customer");
        panel.add(addButton);
        panel.add(removeButton);
        frame.add(panel, BorderLayout.SOUTH);

        // Button actions
        addButton.addActionListener(e -> {
            try {
                int id = Integer.parseInt(idField.getText());
                String name = nameField.getText();
                String roomType = roomTypeField.getText();
                int days = Integer.parseInt(daysField.getText());

                PreparedStatement pst = connection.prepareStatement("INSERT INTO customers VALUES (?, ?, ?, ?)");
                pst.setInt(1, id);
                pst.setString(2, name);
                pst.setString(3, roomType);
                pst.setInt(4, days);
                pst.executeUpdate();

                tableModel.addRow(new Object[]{id, name, roomType, days});
                clearFields(idField, nameField, roomTypeField, daysField);
                JOptionPane.showMessageDialog(frame, "Customer added successfully!");
            } catch (Exception ex) {
                JOptionPane.showMessageDialog(frame, "Error: " + ex.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
            }
        });

        removeButton.addActionListener(e -> {
            int selectedRow = table.getSelectedRow();
            if (selectedRow >= 0) {
                int id = (int) tableModel.getValueAt(selectedRow, 0);
                try {
                    PreparedStatement pst = connection.prepareStatement("DELETE FROM customers WHERE id = ?");
                    pst.setInt(1, id);
                    pst.executeUpdate();

                    tableModel.removeRow(selectedRow);
                    JOptionPane.showMessageDialog(frame, "Customer removed successfully!");
                } catch (SQLException ex) {
                    JOptionPane.showMessageDialog(frame, "Error: " + ex.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
                }
            } else {
                JOptionPane.showMessageDialog(frame, "Please select a customer to remove.", "Error", JOptionPane.ERROR_MESSAGE);
            }
        });

        frame.setVisible(true);
    }

    private void loadCustomers() {
        try {
            Statement stmt = connection.createStatement();
            ResultSet rs = stmt.executeQuery("SELECT * FROM customers");
            while (rs.next()) {
                tableModel.addRow(new Object[]{
                    rs.getInt("id"),
                    rs.getString("name"),
                    rs.getString("room_type"),
                    rs.getInt("days")
                });
            }
        } catch (SQLException e) {
            JOptionPane.showMessageDialog(frame, "Error loading customers: " + e.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    private void clearFields(JTextField... fields) {
        for (JTextField field : fields) {
            field.setText("");
        }
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(HotelManagementJDBC::new);
    }
}
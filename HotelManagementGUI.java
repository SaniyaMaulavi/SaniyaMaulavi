import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.awt.event.*;
import java.util.Vector;

class Customer {
    int id;
    String name;
    String roomType;
    int days;

    Customer(int id, String name, String roomType, int days) {
        this.id = id;
        this.name = name;
        this.roomType = roomType;
        this.days = days;
    }

    Object[] toObjectArray() {
        return new Object[]{id, name, roomType, days};
    }
}

public class HotelManagementGUI {
    private JFrame frame;
    private JTable table;
    private DefaultTableModel tableModel;
    private Vector<Customer> customers;

    public HotelManagementGUI() {
        customers = new Vector<>();
        initialize();
    }

    private void initialize() {
        frame = new JFrame("Hotel Management System");
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

                Customer customer = new Customer(id, name, roomType, days);
                customers.add(customer);
                tableModel.addRow(customer.toObjectArray());

                clearFields(idField, nameField, roomTypeField, daysField);
            } catch (NumberFormatException ex) {
                JOptionPane.showMessageDialog(frame, "Invalid input! Please enter valid data.", "Error", JOptionPane.ERROR_MESSAGE);
            }
        });

        removeButton.addActionListener(e -> {
            int selectedRow = table.getSelectedRow();
            if (selectedRow >= 0) {
                customers.remove(selectedRow);
                tableModel.removeRow(selectedRow);
            } else {
                JOptionPane.showMessageDialog(frame, "Please select a customer to remove.", "Error", JOptionPane.ERROR_MESSAGE);
            }
        });

        frame.setVisible(true);
    }

    private void clearFields(JTextField... fields) {
        for (JTextField field : fields) {
            field.setText("");
        }
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(HotelManagementGUI::new);
    }
}
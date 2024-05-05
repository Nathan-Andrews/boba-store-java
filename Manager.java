import java.awt.*;
 
import javax.swing.*;
import javax.swing.border.CompoundBorder;
import javax.swing.border.EmptyBorder;
import javax.swing.border.LineBorder;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableCellRenderer;
import javax.swing.table.TableColumn;
 
public class Manager extends Page {
    private JTable menuTable;
    private menuHandler menuHandler;
 
    // Custom cell renderer to display a button
    class ButtonRenderer extends JButton implements TableCellRenderer {
        public ButtonRenderer() {
            setOpaque(true);
        }
 
        @Override
        public Component getTableCellRendererComponent(JTable table, Object value, boolean isSelected, boolean hasFocus, int row, int column) {
            setText("Change Price");
            return this;
        }
    }
    // Custom cell renderer to display a button
    class NameChangeRenderer extends JButton implements TableCellRenderer {
        public NameChangeRenderer() {
            setOpaque(true);
        }
 
        @Override
        public Component getTableCellRendererComponent(JTable table, Object value, boolean isSelected, boolean hasFocus, int row, int column) {
            setText("Change Name");
            return this;
        }
    }
    // Custom cell renderer to display a button
    class GraphRenderer extends JButton implements TableCellRenderer {
        public GraphRenderer() {
            setOpaque(true);
        }
 
        @Override
        public Component getTableCellRendererComponent(JTable table, Object value, boolean isSelected, boolean hasFocus, int row, int column) {
            setText("View Sales");
            return this;
        }
    }
    // Custom cell editor to handle button clicks for change price
    class ButtonEditor extends DefaultCellEditor {
        private JButton button;
        private String itemName;
        public ButtonEditor(JCheckBox checkBox) {
            super(checkBox);
            button = new JButton("Change Price");
            button.setOpaque(true);
 
            button.addActionListener(e -> {
                // Handle button click action here
                String newPrice = JOptionPane.showInputDialog("Enter the new price:");
                if (newPrice.isEmpty()) {
                    throw new NullPointerException("no input given");
                } else {
                    if (!menuHandler.setPriceOfItem(itemName, newPrice)){
                        // error message
                    }
 
                    menuHandler.populateMenuModel((DefaultTableModel) menuTable.getModel());
                }
            });
        }
 
        @Override
        public Component getTableCellEditorComponent(JTable table, Object value, boolean isSelected, int row, int column) {
            itemName = (String) table.getValueAt(row, 0);
            return button;
        }
    }
 
    // Custom cell editor to handle button clicks for change name
    class NameChangeEditor extends DefaultCellEditor {
        private JButton button;
        private String itemName;
        public NameChangeEditor(JCheckBox checkBox) {
            super(checkBox);
            button = new JButton("Change Name");
            button.setOpaque(true);
 
            button.addActionListener(e -> {
                // Handle button click action here
                String newName = JOptionPane.showInputDialog("Enter the new name:");
                if (newName.isEmpty()) {
                    throw new NullPointerException("no input given");
                } else {
                    // Assuming db.getInstance() returns database connection instance
                    if (!menuHandler.updateNameOfItem(itemName, newName)){
                        // error message
                    }
 
                    menuHandler.populateMenuModel(
                        (DefaultTableModel) menuTable.getModel()
                    );
                }
            });
        }
 
        @Override
        public Component getTableCellEditorComponent(JTable table, Object value, boolean isSelected, int row, int column) {
            itemName = (String) table.getValueAt(row, 0);
            return button;
        }
    }

    class GraphButtonEditor extends DefaultCellEditor {
        private JButton button;
        private String itemName;
        public GraphButtonEditor(JCheckBox checkBox) {
            super(checkBox);
            button = new JButton("View Sales");
            button.setOpaque(true);
 
            button.addActionListener(e -> {
                // Handle button click action here
                new Graph(itemName);
            });
        }
 
        @Override
        public Component getTableCellEditorComponent(JTable table, Object value, boolean isSelected, int row, int column) {
            itemName = (String) table.getValueAt(row, 0);
            return button;
        }
    }
 
 
    // inherets JPanel panel from Page which is where we display things
 
    // constuctor
    Manager(GUI g) {
        super(g);
         
        // Create a table model with two columns: Name and Price
        DefaultTableModel model = new DefaultTableModel();
        model.addColumn("Name");
        model.addColumn("ID");
        model.addColumn("Price");
        
        model.addColumn("Change Name");
        model.addColumn("Change Price");
        model.addColumn("Sales Graph");
        // Create the JTable with the model
        menuTable = new JTable(model);
        menuHandler = new menuHandler();
        
        

        panel.setLayout(new BorderLayout());


         
        // Create and set the custom cell renderer and cell editor for the "Change" column
        TableColumn changeNameColumn = menuTable.getColumnModel().getColumn(3);
        changeNameColumn.setCellRenderer(new NameChangeRenderer());
        changeNameColumn.setCellEditor(new NameChangeEditor(new JCheckBox())); // Create NameChangeEditor class
        
        TableColumn changeColumn = menuTable.getColumnModel().getColumn(4);
        changeColumn.setCellRenderer(new ButtonRenderer());
        changeColumn.setCellEditor(new ButtonEditor(new JCheckBox()));

        TableColumn graphColumn = menuTable.getColumnModel().getColumn(5);
        graphColumn.setCellRenderer(new GraphRenderer());
        graphColumn.setCellEditor(new GraphButtonEditor(new JCheckBox()));
 
        
        
        // Add the JTable to a scroll pane for scrolling if needed
        JScrollPane scrollPane = new JScrollPane(menuTable);
        scrollPane.getVerticalScrollBar();
        panel.add(scrollPane, BorderLayout.CENTER);

        int borderWidth = 3; // You can adjust this value to make the border thicker
        LineBorder blackLineBorder = new LineBorder(Color.BLACK, borderWidth);
        EmptyBorder emptyBorder = new EmptyBorder(borderWidth, borderWidth, borderWidth, borderWidth);
        CompoundBorder blackBorder = new CompoundBorder(blackLineBorder, emptyBorder);
        scrollPane.setBorder(blackBorder);

        JButton addButton = new JButton("Add");
        addButton.addActionListener(e -> {
            String newName = JOptionPane.showInputDialog("Enter the new item's name:");
            String newID = JOptionPane.showInputDialog("Enter the new item's ID:");
            String newPrice = JOptionPane.showInputDialog("Enter the new item's price:");
            if (newName.isEmpty()||newPrice.isEmpty()) {
                throw new NullPointerException("no input given");
            } else {
                if (!menuHandler.addItem(newName, Integer.parseInt(newID), newPrice)){
                    // error
                }

                menuHandler.populateMenuModel(
                    (DefaultTableModel) menuTable.getModel()
                );
            }
        });

        JButton removeButton = new JButton("Remove");
        removeButton.addActionListener(e -> {
            String name = JOptionPane.showInputDialog("Enter the item's name you want to remove:");
            if (name.isEmpty()) {
                throw new NullPointerException("no input given");
            } else {
                if (!menuHandler.removeItem(name)){
                    // error
                }

                menuHandler.populateMenuModel(
                    (DefaultTableModel) menuTable.getModel()
                );
            }
        });

        JButton itemGroupButton = new JButton("See Item Groups");
        itemGroupButton.addActionListener(e -> {
            new SoldTogether();
        });


        JButton backButton = new JButton("Back");
        backButton.addActionListener(e -> {
            switchPage("managerhome");
        });
    
        JPanel buttonPanel1 = new JPanel();
        buttonPanel1.add(addButton);
        buttonPanel1.add(removeButton);
        buttonPanel1.add(itemGroupButton);
        panel.add(buttonPanel1, BorderLayout.EAST);
        
        JPanel buttonPanel = new JPanel();
        buttonPanel.add(backButton);
        panel.add(buttonPanel, BorderLayout.SOUTH);
        
        menuHandler.populateMenuModel(
            (DefaultTableModel) menuTable.getModel()
        );
        
        //JTextPane
        JLabel l1=new JLabel("MANAGER PAGE");  
        panel.add(l1,BorderLayout.NORTH);   
    }
}
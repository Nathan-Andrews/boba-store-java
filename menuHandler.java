import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.*; 
import java.math.BigDecimal;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;

import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableCellRenderer;

/**
 * Handler class responsible for managing menu items and their related operations such as adding,
 * deleting, updating, and retrieving menu items.
 */
public class menuHandler {
    /**
     * Default constructor for the menuHandler class.
     */
    menuHandler(){

    }

    /**
     * Retrieves menu items from the database.
     *
     * @return An ArrayList containing names of menu items.
     */
    ArrayList<String> getMenuItems(){
        db database = db.getInstance();
        String sql = "SELECT Name FROM menu_items ORDER BY id";

        try(PreparedStatement statement = database.getConnection().prepareStatement(sql)){

            ArrayList<String> names = new ArrayList<>();
            ResultSet result = statement.executeQuery();

            while (result.next())
                names.add(result.getString(1));

            return names;
        }catch (SQLException e) {
            e.printStackTrace();
        }

        return null;
    }

     /**
     * Populates the menu list with items retrieved from the database.
     *
     * @param l A list that will hold menu items.
     */
    void populateMenuList(ArrayList<Server.Drink> l) {
        db database = db.getInstance();
        String sql = "SELECT Name, Price, id FROM menu_items ORDER BY Price";

        try(PreparedStatement statement = database.getConnection().prepareStatement(sql)){
            ResultSet resultSet = statement.executeQuery();

            while (resultSet.next()) {
                String itemName = resultSet.getString("Name");
                double itemPrice = resultSet.getDouble("Price");
                int id = resultSet.getInt("id");

                l.add(new Server.Drink(itemName,itemPrice,id));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
    
    /**
     * Populates a table model with menu items retrieved from the database.
     *
     * @param model The table model to be populated.
     */
    void populateMenuModel(DefaultTableModel model){
        db database = db.getInstance();
        String sql = "SELECT Name, id, Price FROM menu_items ORDER BY Price";
 
        try(PreparedStatement statement = database.getConnection().prepareStatement(sql)){
            ResultSet resultSet = statement.executeQuery();
 
            // Clear the table model before populating it
            model.setRowCount(0);
 
            while (resultSet.next()) {
                String itemName = resultSet.getString("Name");
                double itemID = resultSet.getInt("ID");
                double itemPrice = resultSet.getDouble("Price");
                model.addRow(new Object[]{itemName, itemID, itemPrice});
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    /**
     * Updates the price of a menu item in the database.
     *
     * @param itemName The name of the item whose price is to be updated.
     * @param newPrice The new price to be set.
     * @return true if the operation was successful, false otherwise.
     */
    boolean setPriceOfItem(String itemName, String newPrice){
        db database = db.getInstance();
        String sql = "UPDATE menu_items SET Price = ? WHERE Name = ?";

        try (PreparedStatement statement = database.getConnection().prepareStatement(sql)) {
            BigDecimal newPriceDecimal = new BigDecimal(newPrice);
            statement.setBigDecimal(1, newPriceDecimal);// Set the new price
            statement.setString(2, itemName); // Set the item name

            return statement.executeUpdate() != 0;
        } catch (SQLException e1) {
            e1.printStackTrace();
        }

        return false;
    }

    /**
     * Updates the name of a menu item in the database.
     *
     * @param oldName The old name of the item.
     * @param newName The new name to be set.
     * @return true if the operation was successful, false otherwise.
     */
    boolean updateNameOfItem(String oldName, String newName){
        db database = db.getInstance();

        String sql = "UPDATE menu_items SET Name = ? WHERE Name = ?";
        try (PreparedStatement statement = database.getConnection().prepareStatement(sql)) {
            statement.setString(1, newName);
            statement.setString(2, oldName); // Set the item name
            statement.executeUpdate();
        } catch (SQLException e1) {
            e1.printStackTrace();
        }

        return false;
    }

    /**
     * Adds a new item to the menu in the database.
     *
     * @param name  The name of the new item.
     * @param id    The id of the new item.
     * @param price The price of the new item.
     * @return true if the item was added successfully, false otherwise.
     */
    boolean addItem(String name, Integer id, String price){
        db database = db.getInstance();

        String sql = "INSERT INTO menu_items (Name, Id, Price) VALUES (?, ?, ?)";
        try (PreparedStatement statement = database.getConnection().prepareStatement(sql)) {
            statement.setString(1, name);
            statement.setInt(2, id);
            statement.setDouble(3, Double.parseDouble(price)); 
            
            return statement.executeUpdate() != 0;
        } catch (SQLException e1) {
            e1.printStackTrace();
        }

        return false;
    }

    /**
     * Adds a component to a menu item in the database.
     *
     * @param menuID       The ID of the menu item.
     * @param ingredientID The ID of the ingredient.
     * @param amount       The amount of the ingredient.
     * @return true if the component was added successfully, false otherwise.
     */
    boolean addMenuComp(Integer menuID, Integer ingredientID, Integer amount){
        db database = db.getInstance();

        String sql = "INSERT INTO menu_components (menu_item, ingredient_item, amount) VALUES (?, ?, ?)";
        try (PreparedStatement statement = database.getConnection().prepareStatement(sql)) {
            statement.setInt(1, menuID);
            statement.setInt(2, ingredientID);
            statement.setInt(3, amount);
            
            return statement.executeUpdate() != 0;
        } catch (SQLException e1) {
            e1.printStackTrace();
        }

        return false;
    }

    /**
     * A renderer class for handling button rendering in a table cell.
     */
    class ButtonRenderer extends JButton implements TableCellRenderer {
        ButtonRenderer(String text) {
            setOpaque(true);
            setText(text);
        }

        public Component getTableCellRendererComponent(JTable table, Object value, boolean isSelected, boolean hasFocus, int row, int column) {
            return this;
        }
    }

    /**
     * A editor class for handling button actions in a table cell.
     */
    class ButtonEditor extends DefaultCellEditor {
        private JButton button;
        private int row;
        private int column;//not used here but used in get table cell editor
        private DefaultTableModel tableModel;

        ButtonEditor(String text) {
            super(new JTextField());
            button = new JButton(text);
            button.setOpaque(true);

            button.addActionListener(new ActionListener() {
                public void actionPerformed(ActionEvent e) {
                    SwingUtilities.invokeLater(() -> {
                        int quantity = (int) tableModel.getValueAt(row, 2); // Get the current quantity
                        if (text.equals("+")) {
                            quantity++;
                        } else if (text.equals("-")) {
                            if (quantity > 0) {
                                quantity--;
                            }
                        }
                        tableModel.setValueAt(quantity, row, 2); // Update the quantity in the table
                    });
                }
            });
        }

        public Component getTableCellEditorComponent(JTable table, Object value, boolean isSelected, int row, int column) {
            this.row = row;
            this.column = column;
            tableModel = (DefaultTableModel) table.getModel();
            return button;
        }
    }

    /**
     * Shows a dialog for ingredient selection.
     *
     * @param menuID The ID of the menu item for which ingredients are being selected.
     */
    void showIngredientDialog(Integer menuID) {
        JDialog ingredientDialog = new JDialog();
        ingredientDialog.setTitle("Ingredient Selection");
        ingredientDialog.setLayout(new BorderLayout());
        ingredientDialog.setDefaultCloseOperation(JDialog.DISPOSE_ON_CLOSE);

        DefaultTableModel ingredientTableModel = new DefaultTableModel();
        JTable ingredientTable = new JTable(ingredientTableModel);
        ingredientTableModel.addColumn("Ingredient");
        ingredientTableModel.addColumn("ID");
        ingredientTableModel.addColumn("Quantity");
        ingredientTableModel.addColumn("Add");
        ingredientTableModel.addColumn("Remove");

        db database = db.getInstance();
        String sql = "SELECT Name, id FROM ingredients ORDER BY id";

        try(PreparedStatement statement = database.getConnection().prepareStatement(sql)){
            ResultSet resultSet = statement.executeQuery();

            // Clear the table model before populating it
            ingredientTableModel.setRowCount(0);

            while (resultSet.next()) {
                String itemName = resultSet.getString("Name");
                Integer itemID = resultSet.getInt("ID");
                ingredientTableModel.addRow(new Object[]{itemName, itemID, 0});
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }

        ingredientTable.getColumn("Add").setCellRenderer(new ButtonRenderer("+"));
        ingredientTable.getColumn("Add").setCellEditor(new ButtonEditor("+"));
        ingredientTable.getColumn("Remove").setCellRenderer(new ButtonRenderer("-"));
        ingredientTable.getColumn("Remove").setCellEditor(new ButtonEditor("-"));
    
        JScrollPane scrollPane = new JScrollPane(ingredientTable);
        ingredientDialog.add(scrollPane, BorderLayout.CENTER);
    

        JButton confirmButton = new JButton("Confirm");
        confirmButton.addActionListener(e -> {
            // Loop through the rows of the ingredientTableModel
            for (int row = 0; row < ingredientTableModel.getRowCount(); row++) {
                if ((Integer)ingredientTableModel.getValueAt(row, ingredientTableModel.findColumn("Quantity"))>0) {
                    Integer id = (Integer) ingredientTableModel.getValueAt(row, ingredientTableModel.findColumn("ID"));
                    Integer quantity = (Integer) ingredientTableModel.getValueAt(row, ingredientTableModel.findColumn("Quantity"));
                    addMenuComp(menuID,id,quantity);
                }
            }
            // Close the dialog
            ingredientDialog.dispose();
        });

        ingredientDialog.add(confirmButton, BorderLayout.SOUTH);

        ingredientDialog.setSize(700, 700);
        ingredientDialog.setLocationRelativeTo(null); // Center the dialog
        ingredientDialog.setVisible(true);
    }

    /**
     * Removes an item from the menu in the database.
     *
     * @param name The name of the item to be removed.
     * @return true if the item was removed successfully, false otherwise.
     */
    boolean removeItem(String name){
        db database = db.getInstance();
        
        String selectSql = "SELECT id FROM menu_items WHERE Name = ?";
        String deleteSql = "DELETE FROM menu_items WHERE Name = ?";
        int menuID = -1;
        //selecting the menu id
        try (PreparedStatement selectStatement = database.getConnection().prepareStatement(selectSql)) {
            selectStatement.setString(1, name);
            ResultSet resultSet = selectStatement.executeQuery();
            if (resultSet.next()) {
                menuID = resultSet.getInt("id");
            }
        } catch (SQLException e1) {
            e1.printStackTrace();
        }
        //deleting from the menu_components
        String sql = "DELETE FROM menu_components WHERE menu_item = ?";
        try (PreparedStatement statement = database.getConnection().prepareStatement(sql)) {
            statement.setInt(1, menuID);
            statement.executeUpdate();
        } catch (SQLException e1) {
            e1.printStackTrace();
        }

        //deleting the item from menu_items
        try (PreparedStatement deleteStatement = database.getConnection().prepareStatement(deleteSql)) {
            deleteStatement.setString(1, name);
            deleteStatement.executeUpdate();
        }catch (SQLException e1) {
            e1.printStackTrace();
        }

        
        

        return false;
    }
    
}
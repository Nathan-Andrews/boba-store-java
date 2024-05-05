import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.time.Instant;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

public class orders {
    /**
     * A utility record that represents a tuple of two generic types.
     * It is used to hold and manipulate pairs of values.
     *
     * @param <A> the type of the first element in the tuple
     * @param <B> the type of the second element in the tuple
     */
    public static record Tuple<A, B>(A _1, B _2) { }
    
    /**
     * Default constructor of the orders class.
     */
    orders(){

    }

    /**
     * Retrieves detailed information of a list of orders from the database.
     *
     * @param orders A list containing tuples of order keys and timestamps
     * @return A map where each order key is associated with a list of menu item names
     */
   public HashMap<Integer, ArrayList<String>> getOrdersInfo(ArrayList<Tuple<Integer, Long>> orders) {
        HashMap<Integer, ArrayList<String>> ordersInfo = new HashMap<>();
        ArrayList<String> menuItemNames = new menuHandler().getMenuItems();
        
        final int batchSize = 20_000;

        for (int i = 0; i < orders.size(); i += batchSize) {
            List<Tuple<Integer, Long>> batch = orders.subList(i, Math.min(orders.size(), i + batchSize));
            
            Set<Integer> orderKeys = batch.stream()
                                        .map(Tuple::_1)
                                        .collect(Collectors.toSet());

            String sql = "SELECT order_key, menu_item FROM order_menu_components WHERE order_key IN ("
                    + String.join(",", Collections.nCopies(orderKeys.size(), "?"))
                    + ")";

            try (PreparedStatement preparedStatement = db.getInstance().getConnection().prepareStatement(sql)) {
                int index = 1;
                for (Integer orderKey : orderKeys) {
                    preparedStatement.setInt(index++, orderKey);
                }
                
                ResultSet resultSet = preparedStatement.executeQuery();
                
                while (resultSet.next()) {
                    int orderKey = resultSet.getInt("order_key");
                    int menuItem = resultSet.getInt("menu_item");
                    
                    ordersInfo
                        .computeIfAbsent(orderKey, k -> new ArrayList<>())
                        .add(menuItemNames.get(menuItem - 1));
                }

            } catch (Exception e) {
                e.printStackTrace();
            }
        }

        return ordersInfo;
    }

    /**
     * Retrieves orders that fall within a specific timestamp range from the database.
     *
     * @param timestampFrom The start of the timestamp range
     * @param timestampTo The end of the timestamp range
     * @return A list of tuples where each tuple contains an order key and a timestamp
     */
    public ArrayList<Tuple<Integer, Long>> getOrders(long timestampFrom, long timestampTo){
        String sql = """
            SELECT * 
            FROM orders 
            WHERE timestamp BETWEEN ? AND ?;
                """;

        db database = db.getInstance();
        Connection conn = database.getConnection();
        try (PreparedStatement preparedStatement = conn.prepareStatement(sql);){
            preparedStatement.setLong(1, timestampFrom);
            preparedStatement.setLong(2, timestampTo);

            ArrayList<Tuple<Integer, Long>> orders = new ArrayList<Tuple<Integer, Long>>();
            ResultSet result = preparedStatement.executeQuery();
            while (result.next()){
                orders.add(
                    new Tuple<Integer,Long>(
                        result.getInt("order_key"),
                        result.getLong("timestamp")
                    )
                );
            }
            
            return orders;
        }catch(Exception e){
            e.printStackTrace();
            System.err.println(e.getClass().getName()+": "+e.getMessage());
        }

        return null;
    }

    /**
     * Adds a new order to the database.
     *
     * @param items A list of tuples where each tuple contains a menu item and a count
     * @return true if the order was added successfully, false otherwise
     */
    boolean addOrder(ArrayList<Tuple<Integer, Integer>> items){
        long timestamp = Instant.now().getEpochSecond();
        String sql = """
            INSERT INTO orders (order_key, timestamp) 
            VALUES (?, ?);
                """;
        String sql2 = """
            INSERT INTO order_menu_components (order_key, menu_item, count) 
            VALUES (?, ?, ?);
                """;

        String sql3 = """
                SELECT MAX(order_key) AS max_orderKey FROM orders;
                """;

        db database = db.getInstance();
        Connection conn = database.getConnection();
        try (
            PreparedStatement preparedStatement1 = conn.prepareStatement(sql);
            PreparedStatement preparedStatement2 = conn.prepareStatement(sql2);
            PreparedStatement preparedStatementGetOrderKey = conn.prepareStatement(sql3);
        ){
            int maxOrderKey = 0;
            ResultSet rs = preparedStatementGetOrderKey.executeQuery();
            if (rs.next())
                maxOrderKey = rs.getInt("max_orderKey") + 1;

            preparedStatement1.setInt(1, maxOrderKey);
            preparedStatement1.setLong(2, timestamp);
            preparedStatement1.addBatch();

            for (Tuple<Integer, Integer> item : items){
                preparedStatement2.setInt(1, maxOrderKey);
                preparedStatement2.setInt(2, item._1());
                preparedStatement2.setInt(3, item._2());
                preparedStatement2.addBatch();
            }

            preparedStatement1.executeBatch();
            preparedStatement2.executeBatch();

            return true;
        }catch(Exception e){
            e.printStackTrace();
            System.err.println(e.getClass().getName()+": "+e.getMessage());
        }

        return false;
    }
}
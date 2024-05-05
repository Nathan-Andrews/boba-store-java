import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.Instant;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;
import java.util.Map.Entry;

class ItemPair {
    orders.Tuple<Integer, Integer> items;
    int sales;

    /**
     * Constructor for the ItemPair class.
     *
     * @param item1 First item ID.
     * @param item2 Second item ID.
     * @param sales The number of sales for this item pair.
     */
    public ItemPair(int item1, int item2, int sales) {
        this.items = new orders.Tuple<>(item1, item2);
        this.sales = sales;
    }
}

public class reportsHandler {
    /**
     * Retrieves the list of ingredients from the database.
     *
     * @return A list of ingredient names, or null if a SQLException occurs.
     */
    ArrayList<String> getIngredients() {
        db database = db.getInstance();
        String sql = """
                SELECT name 
                FROM ingredients 
                ORDER BY id;
                """;

        try(PreparedStatement statement = database.getConnection().prepareStatement(sql)) {
            ArrayList<String> ingredients = new ArrayList<>();
            ResultSet result = statement.executeQuery();

            while (result.next())
                ingredients.add(result.getString(1));
            
            return ingredients;
        }catch (SQLException e) {
            e.printStackTrace();
        }

        return null;
    }

    /**
     * Retrieves items sold below a certain quantity.
     *
     * @param timestampTo The timestamp to filter orders.
     * @return A map where the key is the item ID and the value is the item name, or null if a SQLException occurs.
     */
    HashMap<Integer, String> getItemsBelow(long timestampTo){
        long timestampFrom = Instant.now().toEpochMilli();
        orders orders = new orders();
        ArrayList<String> menuItems = new menuHandler().getMenuItems();
        HashMap<Integer, ArrayList<String>> ordersInfo = orders.getOrdersInfo(
            orders.getOrders(timestampTo, timestampFrom)
        );

        String sql = """
            SELECT 
                ingredient_id,
                SUM(amount) AS total_amount
            FROM 
                batches
            GROUP BY 
                ingredient_id
            ORDER BY
                ingredient_id;
                """;

        //String placeholder = String.join(",", java.util.Collections.nCopies(menuItems.size(), "?"));
        String sql2 = "SELECT * FROM menu_components;";

        db database = db.getInstance();
        try(
            PreparedStatement statement = database.getConnection().prepareStatement(sql);
            PreparedStatement statement2 = database.getConnection().prepareStatement(sql2);
        ){
            ResultSet result = statement.executeQuery();

            HashMap<Integer, Integer> ingredientAmountMap = new HashMap<>();
            while (result.next()) {
                int ingredientId = result.getInt("ingredient_id");
                int totalAmount = result.getInt("total_amount");
                ingredientAmountMap.put(ingredientId, totalAmount);
            }

            HashMap<Integer, Integer> itemSalesMap = new HashMap<>();
            for (Map.Entry<Integer, ArrayList<String>> entry : ordersInfo.entrySet()){
                ArrayList<String> names = entry.getValue();
                for (String name : names){
                    int id = menuItems.indexOf(name) + 1;

                    itemSalesMap.merge(id, 1, Integer::sum);
                }
            }

            // for (int i = 0; i < menuItems.size(); i++)
            //     statement2.setInt(i + 1, i + 1);
            
            ResultSet result2 = statement2.executeQuery();

            Map<Integer, Map<Integer, Integer>> menuIngredientMap = new HashMap<>();
            while (result2.next()) {
                int menuItem = result2.getInt("menu_item");
                int ingredientItem = result2.getInt("ingredient_item");
                int amount = result2.getInt("amount");

                menuIngredientMap
                    .computeIfAbsent(menuItem, k -> new HashMap<>())
                    .put(ingredientItem, amount);
            }

            HashMap<Integer, String> itemsSoldBelow = new HashMap<>();
            for (Map.Entry<Integer, Integer> entry : itemSalesMap.entrySet()){
                int id = entry.getKey();
                int amount = entry.getValue();

                Map<Integer, Integer> ingredientAmounts = menuIngredientMap.get(id);
                for (Map.Entry<Integer, Integer> entry2 : ingredientAmounts.entrySet()){
                    int ingredAmount = ingredientAmountMap.get(entry2.getKey()) != null ? ingredientAmountMap.get(entry2.getKey()) : 0;
                    if (ingredAmount > entry2.getValue()*amount/10){
                        itemsSoldBelow.put(id, menuItems.get(id-1));
                    }
                }
            }

            return itemsSoldBelow;
        }catch (SQLException e) {
            e.printStackTrace();
        }

        return null;
    }

    /**
     * Generates a list of ingredients to restock based on their quantities.
     *
     * @return A list of ingredient names to be restocked, or null in case of an exception.
     */
    ArrayList<String> restockList(){
        ArrayList<String> menuItems = getIngredients();

        String sql = """
            SELECT 
                ingredient_id,
                SUM(amount) AS total_amount
            FROM 
                batches
            GROUP BY 
                ingredient_id
            ORDER BY
                ingredient_id;
                """;

        db database = db.getInstance();
        try(
            PreparedStatement statement = database.getConnection().prepareStatement(sql);
        ){
            ResultSet result = statement.executeQuery();

            ArrayList<String> ingredients = new ArrayList<>();
            while (result.next()) {
                int ingredientId = result.getInt("ingredient_id");
                int totalAmount = result.getInt("total_amount");
                
                if (totalAmount < 50){
                    ingredients.add(menuItems.get(ingredientId - 1));
                }
            }

            return ingredients;
        }catch (Exception e){
            e.printStackTrace();
        }

        return null;
    }

    /**
     * Determines what items are commonly sold together.
     *
     * @param timestampTo   The end timestamp for the sales period.
     * @param timestampFrom The start timestamp for the sales period.
     * @return A list of ItemPair objects representing item pairs and their sale counts, sorted in descending order of sales.
     */
    ArrayList<ItemPair> whatSalesTogether(long timestampTo, long timestampFrom){
        HashMap<Integer, ArrayList<Integer>> together = new HashMap<>();

        orders orders = new orders();
        HashMap<Integer, ArrayList<String>> ordersInfo = orders.getOrdersInfo(
            orders.getOrders(timestampTo, timestampFrom)
        );
        ArrayList<String> menuItems = new menuHandler().getMenuItems();

        for (Map.Entry<Integer, ArrayList<String>> entry : ordersInfo.entrySet()){
            ArrayList<String> items = entry.getValue();
            ArrayList<Integer> itemIds = new ArrayList<>();

            for (String item : items){
                itemIds.add(menuItems.indexOf(item) + 1);
            }

            Collections.sort(itemIds);

            for (int i = 0; i < itemIds.size(); i++){
                for (int j = Math.min(i, itemIds.size()-1); j < itemIds.size(); j++){
                    final int j2 = j;
                    together.computeIfAbsent(i, k -> {
                        ArrayList<Integer> list = new ArrayList<>();
                        for (int c = 0; c < menuItems.size()+1; c++)
                            list.add(-1);

                        return list;

                    });

                    int index = itemIds.get(j2);
                    ArrayList<Integer> list = together.get(i);
                    list.set(index, list.get(index) == -1 ? 1 : list.get(index)+1);
                }
            }
        }

        ArrayList<ItemPair> pairs = new ArrayList<>();

        for (Map.Entry<Integer, ArrayList<Integer>> entry : together.entrySet()) {
            int item1 = entry.getKey();
            ArrayList<Integer> sales = entry.getValue();

            for (int item2 = 0; item2 < sales.size(); item2++) {
                int saleCount = sales.get(item2);
                if (saleCount == -1)
                    continue;

                pairs.add(new ItemPair(item1+1, item2 + 1, saleCount));
            }
        }

        pairs.sort((p1, p2) -> Integer.compare(p2.sales, p1.sales));

        return pairs;
    }


    /**
     * Default constructor for the reportsHandler class.
     */
    reportsHandler(){

    }
/* 
    public static void main(String[] args){
        reportsHandler handlerTest = new reportsHandler();

        /* 
        for (Map.Entry<Integer, String> entry : handlerTest.getItemsBelow(1694759205).entrySet()){
            System.out.println(entry.getValue());
        }
        

        ArrayList<ItemPair> pairs = handlerTest.whatSalesTogether(1694759205, 1695104805);
        for (int i = 0; i < Math.min(10, pairs.size()); i ++){

            System.out.println(pairs.get(i).items._1() + " " + pairs.get(i).items._2() + " " + pairs.get(i).sales);
        }

        for (String entry : handlerTest.restockList()){
            System.out.println(entry);
        }
    }
    */
}

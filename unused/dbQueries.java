/*
package unused;
import java.util.ArrayList; // import the ArrayList class
import java.util.Arrays;

public class dbQueries {
    public static ArrayList<String> queries = new ArrayList<String>();
    static {
         //Contains the following queries:
        //52 Weeks of Sales History: select count of orders grouped by week.
        queries.add("""
            SELECT EXTRACT(WEEK FROM TO_TIMESTAMP(timestamp)) AS week_number, COUNT(*) AS order_count
            FROM orders
            GROUP BY EXTRACT(WEEK FROM TO_TIMESTAMP(timestamp));
             """);
        //Realistic Sales History: select count of orders, sum of order total grouped by hour.
        queries.add("""
          SELECT EXTRACT(HOUR FROM TO_TIMESTAMP(timestamp) AT TIME ZONE 'UTC') AS hour_of_day, COUNT(*)
          FROM orders
          GROUP BY EXTRACT(HOUR FROM TO_TIMESTAMP(timestamp) AT TIME ZONE 'UTC');
             """);
        //2 Peak Days: select top 10 sums of order total grouped by day in descending order.
        queries.add("""
            SELECT DATE(TO_TIMESTAMP(timestamp)), COUNT(*) 
            FROM orders
            GROUP BY DATE(TO_TIMESTAMP(timestamp))
            ORDER BY COUNT(*) DESC
            LIMIT 10;
               """);
        //20 Items in Inventory: select row count from inventory.
        queries.add("""
            SELECT COUNT(*) FROM ingredients LIMIT 20;
            """);
        //grouping menu components by the ingredient item id
        queries.add("""
            SELECT ingredient_item FROM menu_components
            GROUP BY ingredient_item;
            """);
        //grouping menu components by the menu item id
        queries.add("""
            SELECT menu_item FROM menu_components
            GROUP BY menu_item;
            """);
        //Beginning Query contains:
        //Grabbing the number of ingredients from the database

        //Further is test queries
        queries.add("SELECT COUNT(*) AS menu_ingredients FROM ingredients;");
        queries.add("SELECT * FROM menu_components;");
        //queries.add("SELECT * FROM batches;");
        queries.add("SELECT * FROM ingredients;");
        queries.add("SELECT * FROM accounts;");
        queries.add("SELECT * FROM menu_items LIMIT 10;");
       // queries.add("SELECT batches FROM ingredients;");
        queries.add("SELECT COUNT(*) FROM ingredients;");
        // Query for all orders which were made after 8 pm on Oct 10, 2022 (rat poison incident)
        queries.add("""
            SELECT *
            FROM
              orders
            WHERE
              timestamp > 1665450000; 
            """);
        // Query for the ten most expensive items on the menu
        queries.add("""
            SELECT
              name,
              id,
              price
            FROM
              menu_items
            ORDER BY
              price DESC
            LIMIT 10;
            """);
        // Name and price of all items on the menu when a 50% off coupon is applied (price will be an int rounded down)
        queries.add("""
            SELECT
              name,
              price / 2
            FROM
              menu_items;
            """);
        //Grab all batches that have expired.
        queries.add("""
            SELECT * FROM batches
            WHERE CAST(EXTRACT(epoch FROM NOW()) AS INT) >= expiration_date;
        """);
        //Grab all manager accounts
        queries.add("""
            SELECT * FROM accounts
            WHERE account_type = 0;
        """);
        //Grab all employee accounts
        queries.add("""
            SELECT * FROM accounts
            WHERE account_type = 1;
        """);
        //Grab most popular items ordered in descending order
        queries.add("""
            SELECT
              menu_item_id
            FROM
              order_menu_components
            GROUP BY
              menu_item_id
            ORDER BY
              SUM(count) DESC
        """);
        //Grab most popular item on the menu
        queries.add("""
            SELECT
              menu_item_id
            FROM
              order_menu_components
            GROUP BY
              menu_item_id
            ORDER BY
              SUM(count) DESC
            LIMIT 1;
        """);
        //Grab oldest batches
        queries.add("""
            SELECT *
            FROM batches
            ORDER BY in_date ASC;
        """);
    }
}
*/
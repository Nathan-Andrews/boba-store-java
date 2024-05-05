package unused;
/*
import java.util.ArrayList;

import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.io.BufferedReader;
import java.io.FileReader;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.util.HashMap;

class dbAddDataEntries {
    static List<String[]> getRecords(){
        List<String[]> records = new ArrayList<>();

        try (BufferedReader reader = new BufferedReader(new FileReader("data.csv"))) {
            String line;
            while ((line = reader.readLine()) != null) {
                String[] fields = line.split(",");
                records.add(fields);
            }
        }catch(Exception e){
            e.printStackTrace();
            System.err.println(e.getClass().getName()+": "+e.getMessage());
            System.exit(0);
        }

        return records;
    }

    public static void populateEntries(){
        db database = db.getInstance();
    }

    public void run(){
        Map<String, List<String[]>> dataByOrderKey = new HashMap<>();

        // sort the CSV document by OrderKey in DataByOrderKey
        List<String[]> records = getRecords();
        for (String[] record : records){
            String orderKey = record[0];

            // if the map does not have the order key, then add it
            boolean containsOrderKey = dataByOrderKey.containsKey(orderKey);
            if (!containsOrderKey) {
                dataByOrderKey.put(orderKey, new ArrayList<>());
            }

            dataByOrderKey.get(orderKey).add(record);
        }
    }

        String[][] items = {
            {"Coffee Milk Tea",     "1",  "4"},
            {"Coconut Milk Tea",    "2",  "5"},
            {"Almond Milk Tea",     "3",  "6"},
            {"Thai Milk Tea",       "4",  "6"},
            {"Rosehip Milk Tea",    "5",  "5"},
            {"Coffee Slushie",      "6",  "5"},
            {"Oreo Slushie",        "7",  "5"},
            {"Pina Colada Slushie", "8",  "4"},
            {"Pineapple Slushie",   "9",  "5"},
            {"Mango Slushie",       "10", "7"},
        };

/*
        for (String[] item : items){
            String name = item[0];
            Integer id = Integer.parseInt(item[1]);
            Integer price = Integer.parseInt(item[2]);

            String sql = "INSERT INTO menu_items (id, price, name) VALUES (?, ?, ?)";

            Connection conn = database.getConnection();
            try (PreparedStatement preparedStatement = conn.prepareStatement(sql)) {
                preparedStatement.setInt(1, id);
                preparedStatement.setLong(2, price);
                preparedStatement.setString(3, name);

                int rowsInserted = preparedStatement.executeUpdate();
                if (rowsInserted > 0) {
                    System.out.println("A new menu_item has been inserted: " + name);
                } else {
                    System.out.println("Insertion failed.");
                }
            }catch (Exception e){
                e.printStackTrace();
                System.err.println(e.getClass().getName()+": "+e.getMessage());

                //System.exit(0);
            }
        }

        Connection conn = database.getConnection();

        Set<Map.Entry<String, List<String[]>>> entries = dataByOrderKey.entrySet();
        Iterator<Map.Entry<String, List<String[]>>> entriesIterator = entries.iterator();

        int dataSize = dataByOrderKey.size();
        int i = 0;
        int batch = 0;
        while (i < dataSize){
            try (
                PreparedStatement preparedStatement1 = conn.prepareStatement("INSERT INTO orders (order_key, timestamp) VALUES (?, ?)");
                PreparedStatement preparedStatement2 = conn.prepareStatement("INSERT INTO order_menu_components (order_key, menu_item, count) VALUES (?, ?, ?)");
            ) {
                int go = Math.min(i + 750, dataSize);
                for (; i < go; i++){
                    if (!entriesIterator.hasNext()){
                        break;
                    }

                    Map.Entry<String, List<String[]>> entry = entriesIterator.next();
                    String orderKey = entry.getKey();
                    List<String[]> orders = entry.getValue();

                    long timestamp;
                    try{
                        timestamp = Long.parseLong(orders.get(0)[1]);
                    } catch(Exception e){
                        continue;
                    }

                    Map<Integer, Integer> menuItems = new HashMap<>();
                    for (String[] order : orders){
                        Integer menuItem = Integer.parseInt(order[2]);
                        menuItems.merge(menuItem, 1, (a, b) -> a+b);
                    }

                    // order
                    preparedStatement1.setInt(1, Integer.parseInt(orderKey));
                    preparedStatement1.setLong(2, timestamp);
                    preparedStatement1.addBatch();

                    // order_components
                    for (Map.Entry<Integer, Integer> menuItemEntry : menuItems.entrySet()){
                        Integer menuItemId = menuItemEntry.getKey();
                        Integer amount = menuItemEntry.getValue()/3;
                        if (amount.intValue() == 0){
                            amount = 1;
                        }
                        
                        preparedStatement2.setInt(1, Integer.parseInt(orderKey));
                        preparedStatement2.setInt(2, menuItemId);
                        preparedStatement2.setInt(3, amount);
                        preparedStatement2.addBatch();
                    }
                }
            }
        try (CSVReader csvReader = new CSVReader(new FileReader(csvFilePath))) {
            String[] header = csvReader.readNext(); // Read headers (OrderKey, Timestamp, ItemId)

            String[] line;
            while ((line = csvReader.readNext()) != null) {
                String orderKey = line[0];
                if (!dataByOrderKey.containsKey(orderKey)) {
                    dataByOrderKey.put(orderKey, new ArrayList<>());
                }

                preparedStatement1.executeBatch();
                preparedStatement2.executeBatch();

                batch += 1;
                System.out.println("Batch " + batch + " executed successfully!");
            }
        }catch(Exception e){
                e.printStackTrace();
                System.err.println(e.getClass().getName()+": "+e.getMessage());
                dataByOrderKey.get(orderKey).add(line);
            }
        }

        System.out.println("Finished!");
    }
}
}
*/
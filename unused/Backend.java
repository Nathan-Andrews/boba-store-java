package unused;
/* 
import java.util.ArrayList;

import unused.dbQueries;

import java.sql.ResultSet;
import java.sql.ResultSetMetaData;

public class Backend{
    public static void main(String[] args){
        db database = db.getInstance();

        int queryCount = 0;

        ArrayList<String> queries = dbQueries.queries;
        for (String query : queries){
            queryCount += 1;

            System.out.println("\n\nQUERY COUNT: " + queryCount + "\nSTARTING:\n\n");

            try{
                ResultSet result = database.executeQuery(query);
                ResultSetMetaData metadata = result.getMetaData();
                int columnsNumber = metadata.getColumnCount();
                int count = 0; // so we only print 50 rows maximum

                while (result.next() && count <= 50) {
                    for (int i = 1; i <= columnsNumber; i++) {
                        if (i > 1) System.out.print(",  ");

                        String columnValue = result.getString(i);
                        System.out.print(columnValue + " (" + metadata.getColumnName(i) + ")");
                    }

                    System.out.println("");
                    count += 1;
                }


                if (count >= 50)
                    System.out.println("... More but stopped printing due to sheer length.");
            }catch (Exception e){
                System.out.println("Query failed!");
                e.printStackTrace();
            }
            database.executeQuery(query);
        }

        //dbAddDataEntries.populateEntries();
    }
}
*/
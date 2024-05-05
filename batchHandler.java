import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.time.Instant;
import java.util.ArrayList;

public class batchHandler {
    /**
     * Default constructor for the batchHandler class.
     */
    batchHandler(){

    }

    /**
     * Retrieves a list of ingredient names.
     *
     * @return An ArrayList containing the names of all ingredients.
     */
    public ArrayList<String> getIngredients(){
        return new reportsHandler().getIngredients();
    }

    /**
     * Adds a new batch entry into the database.
     *
     * @param batchKey      The unique key for the batch.
     * @param in_timestamp  The input timestamp of the batch.
     * @param amount        The amount to be added.
     * @param ingredientId  The ID of the ingredient.
     * @return true if the batch entry is added successfully, false otherwise.
     */
    public boolean addBatch(String batchKey, long in_timestamp, String amount, String ingredientId){
        long end_timestamp = in_timestamp + 60*60*24*7;

        String sql = "INSERT INTO batches (batch_key, in_date, expiration_date, amount, ingredient_id) VALUES (?, ?, ?, ?, ?)";

        Connection conn = db.getInstance().getConnection();
        try (PreparedStatement preparedStatement = conn.prepareStatement(sql)) {
            preparedStatement.setInt(1, Integer.parseInt(batchKey));
            preparedStatement.setLong(2, in_timestamp);
            preparedStatement.setLong(3, end_timestamp);
            preparedStatement.setInt(4, Integer.parseInt(amount));
            preparedStatement.setInt(5, Integer.parseInt(ingredientId));

            return preparedStatement.executeUpdate() != 0;
        }catch (Exception e){
            e.printStackTrace();
            System.err.println(e.getClass().getName()+": "+e.getMessage());

            //System.exit(0);
        }

        return false;
    }


    /**
     * Removes a batch entry from the database based on batch key.
     *
     * @param batchKey The unique key for the batch.
     * @return true if the batch entry is removed successfully, false otherwise.
     */

    boolean removeBatch(String batchKey){
        String sql = "DELETE FROM batches WHERE batch_key = ?;";

        Connection conn = db.getInstance().getConnection();
        try (PreparedStatement preparedStatement = conn.prepareStatement(sql)) {
            preparedStatement.setInt(1, Integer.parseInt(batchKey));

            return preparedStatement.executeUpdate() != 0;
        }catch (Exception e){
            e.printStackTrace();
            System.err.println(e.getClass().getName()+": "+e.getMessage());

            //System.exit(0);
        }

        return false;
    }

    /**
     * Adds a certain amount to a batch's existing amount.
     *
     * @param batchKey   The unique key for the batch.
     * @param increment  The amount to be added to the batch.
     * @return true if the amount is added successfully, false otherwise.
     */
    public boolean addAmountToBatch(String batchKey, Integer increment){
        String sql = "UPDATE batches SET amount = amount + ? WHERE batch_key = ?;";

        try (PreparedStatement preparedStatement = db.getInstance().getConnection().prepareStatement(sql)) {
            preparedStatement.setInt(1, increment);
            preparedStatement.setInt(2, Integer.parseInt(batchKey));

            return preparedStatement.executeUpdate() != 0;
        }catch(Exception e){
            e.printStackTrace();
        }

        return false;
    }

    /**
     * Removes a certain amount from a batch's existing amount.
     *
     * @param batchKey   The unique key for the batch.
     * @param increment  The amount to be removed from the batch.
     * @return true if the amount is removed successfully, false otherwise.
     */
    public boolean removeAmountToBatch(String batchKey, Integer increment){
        String sql = "UPDATE batches SET amount = amount - ? WHERE batch_key = ?;";

        try (PreparedStatement preparedStatement = db.getInstance().getConnection().prepareStatement(sql)) {
            preparedStatement.setInt(1, increment);
            preparedStatement.setInt(2, Integer.parseInt(batchKey));

            return preparedStatement.executeUpdate() != 0;
        }catch(Exception e){
            e.printStackTrace();
        }

        return false;
    }

    /**
     * Retrieves batches related to a specific ingredient.
     *
     * @param itemName The name of the ingredient.
     * @return An ArrayList of BatchEntry objects corresponding to the ingredient.
     */
    public ArrayList<BatchEntry> getBatchesFromIngredient(String itemName){
        int id = 0;
        ArrayList<String> ingredients = getIngredients();
        for (String str : ingredients){
            id += 1;
            if (str.equals(itemName))
                break;
        }

        String sql = "SELECT * FROM batches WHERE ingredient_id = ?;";

        try (PreparedStatement preparedStatement = db.getInstance().getConnection().prepareStatement(sql)) {
            preparedStatement.setInt(1, id);

            ArrayList<BatchEntry> batches = new ArrayList<BatchEntry>();
            ResultSet result = preparedStatement.executeQuery();
            
            while (result.next()){
                batches.add(
                    new BatchEntry(
                        result.getInt(1),
                        result.getLong(2),
                        result.getLong(3),
                        result.getInt(4),
                        result.getInt(5),
                        ingredients.get(result.getInt(5)-1)
                    )
                );
            }
            
            return batches;
        }catch(Exception e){
            e.printStackTrace();
        }

        return null;
    }

    /**
     * Adds a new batch entry with a specified amount and ingredient name.
     *
     * @param amount The amount of the ingredient in the batch.
     * @param name   The name of the ingredient.
     * @return true if the batch is added successfully, false otherwise.
     */
    public boolean addBatch(int amount, String name){
        int ingredient_id = 0;
        ArrayList<String> ingredients = this.getIngredients();
        for (String s : ingredients){
            ingredient_id += 1;
            if (s.equals(name))
                break;

        }

        System.out.println(ingredient_id);
        String sql = "INSERT INTO batches (in_date, expiration_date, ingredient_id, amount, batch_key) VALUES (?, ?, ?, ?, ?);";

        try (PreparedStatement preparedStatement = db.getInstance().getConnection().prepareStatement(sql)) {
            ResultSet resultSet = db.getInstance().getConnection().prepareStatement("SELECT COALESCE(MAX(batch_key), 0) + 1 FROM batches;").executeQuery();
            int nextBatchKey = 1;
            if (resultSet.next()) {
                nextBatchKey = resultSet.getInt(1);
            }

            preparedStatement.setLong(1, Instant.now().toEpochMilli());
            preparedStatement.setLong(2, Instant.now().toEpochMilli() + 60*60*24*7);
            preparedStatement.setInt(3, ingredient_id);
            preparedStatement.setInt(4, amount);
            preparedStatement.setInt(5, nextBatchKey);
            
            return preparedStatement.executeUpdate() != 0;
        }catch (Exception e){
            e.printStackTrace();
        }

        return false;
    }
}

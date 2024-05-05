package unused;
import java.sql.Connection;
import java.sql.PreparedStatement;

import db;

public class addBatches {
    public static void main(String[] args) {
        new addBatches();
    }

    addBatches() {
        // batch key, in_date, expiration date, amount, ingredient_id
        String[][] batches = {
            // Cups
            {"1", "1696654800", "4102466400", "10", "1", "Cups"},
            {"2", "1696827600", "4102466400", "25", "1", "Cups"},
            {"3", "1697000400", "4102466400", "25", "1", "Cups"},

            // CupLids
            {"4", "1696654800", "4102466400", "10", "2", "CupLids"},
            {"5", "1696827600", "4102466400", "25", "2", "CupLids"},
            {"6", "1697000400", "4102466400", "25", "2", "CupLids"},

            // Straws
            {"7", "1696654800", "4102466400", "10", "3", "Straws"},
            {"8", "1696827600", "4102466400", "25", "3", "Straws"},
            {"9", "1697000400", "4102466400", "25", "3", "Straws"},

            // Napkins
            {"10", "1696827600", "4102466400", "20", "4", "Napkins"},
            {"11", "1697000400", "4102466400", "25", "4", "Napkins"},

            // Water
            {"12", "1696136400", "4102466400", "40", "5", "Water"},
            {"13", "1696136400", "4102466400", "50", "5", "Water"},
            {"14", "1696136400", "4102466400", "50", "5", "Water"},

            // Milk
            {"15", "1696741200", "1697346000", "4", "6", "Milk"},
            {"16", "1696741200", "1697346000", "15", "6", "Milk"},

            // CoconutMilk
            {"17", "1696770000", "1697374800", "6", "7", "CoconutMilk"},
            {"18", "1696770000", "1697374800", "10", "7", "CoconutMilk"},

            // AlmondMilk
            {"19", "1696770000", "1697374800", "2", "8", "AlmondMilk"},
            {"20", "1696770000", "1697374800", "10", "8", "AlmondMilk"},

            // RoseWater
            {"21", "1696860000", "1697464800", "5", "9", "RoseWater"},
            {"22", "1696860000", "1697464800", "10", "9", "RoseWater"},

            // PineappleConcentrate
            {"23", "1696687200", "1697292000", "14", "10", "PineappleConcentrate"},
            {"24", "1696687200", "1697292000", "25", "10", "PineappleConcentrate"},

            // MangoConcentrate
            {"25", "1696687200", "1697292000", "11", "11", "MangoConcentrate"},
            {"26", "1696687200", "1697292000", "25", "11", "MangoConcentrate"},

            // Coconut
            {"27", "1696860000", "1697464800", "1", "12", "Coconut"},
            {"28", "1696860000", "1697464800", "5", "12", "Coconut"},

            // BlackTeaBag
            {"29", "1696946400", "1697551200", "15", "13", "BlackTeaBag"},
            {"30", "1696946400", "1697551200", "20", "13", "BlackTeaBag"},
            {"31", "1696946400", "1697551200", "20", "13", "BlackTeaBag"},

            // GreenTeaBag
            {"32", "1696946400", "1697551200", "2", "14", "GreenTeaBag"},
            {"33", "1696946400", "1697551200", "20", "14", "GreenTeaBag"},
            {"34", "1696946400", "1697551200", "20", "14", "GreenTeaBag"},
            {"35", "1696946400", "1697551200", "20", "14", "GreenTeaBag"},

            // Oreo
            {"36", "1697014800", "1697619600", "5", "15", "Oreo"},
            {"37", "1697014800", "1697619600", "10", "15", "Oreo"},

            // Rosehip
            {"38", "1696860000", "1697464800", "3", "16", "Rosehip"},
            {"39", "1696860000", "1697464800", "5", "16", "Rosehip"},

            // CoffeePowder
            {"40", "1696928400", "1697533200", "10", "17", "CoffeePowder"},
            {"41", "1696928400", "1697533200", "20", "17", "CoffeePowder"},
            {"42", "1696928400", "1697533200", "20", "17", "CoffeePowder"},

            // EmulsifiedCoconut
            {"43", "1696928400", "1697533200", "4", "18", "EmulsifiedCoconut"},
            {"44", "1696928400", "1697533200", "15", "18", "EmulsifiedCoconut"},

            // AlmondPowder
            {"45", "1696928400", "1697533200", "12", "19", "AlmondPowder"},
            {"46", "1696928400", "1697533200", "20", "19", "AlmondPowder"},
            {"47", "1696928400", "1697533200", "20", "19", "AlmondPowder"},

            // MatchaPowder
            {"48", "1696928400", "1697533200", "8", "20", "MatchaPower"},
            {"49", "1696928400", "1697533200", "20", "20", "MatchaPower"},
            {"50", "1696928400", "1697533200", "20", "20", "MatchaPower"},

            // OreoMix
            {"51", "1697014800", "1697619600", "2", "21", "OreoMix"},
            {"52", "1697014800", "1697619600", "10", "21", "OreoMix"},
            {"53", "1697014800", "1697619600", "10", "21", "OreoMix"},

            // PinaColada
            {"54", "1697014800", "1697619600", "4", "22", "PinaColada"},
            {"55", "1697014800", "1697619600", "10", "22", "PinaColada"},
            {"56", "1697014800", "1697619600", "10", "22", "PinaColada"},

            // Pineapple
            {"57", "1696860000", "1697464800", "4", "23", "Pineapple"},


//
        };


        for (String[] batch : batches){
            Integer batch_key = Integer.parseInt(batch[0]);
            Long in_date = Long.parseLong(batch[1]);
            Long expiration_date = Long.parseLong(batch[2]);
            Integer amount = Integer.parseInt(batch[3]);
            Integer ingredient_id = Integer.parseInt(batch[4]);


            String sql = "INSERT INTO batches (batch_key, in_date, expiration_date, amount, ingredient_id) VALUES (?, ?, ?, ?, ?)";

            try (PreparedStatement preparedStatement = db.getInstance().getConnection().prepareStatement(sql)) {
                preparedStatement.setInt(1, batch_key);
                preparedStatement.setLong(2, in_date);
                preparedStatement.setLong(3, expiration_date);
                preparedStatement.setInt(4, amount);
                preparedStatement.setInt(5, ingredient_id);

                int rowsInserted = preparedStatement.executeUpdate();
                if (rowsInserted > 0) {
                    System.out.println("Batch " + batch_key + " has been added.");
                } else {
                    System.out.println("Insertion failed.");
                }
            }catch (Exception e){
                e.printStackTrace();
                System.err.println(e.getClass().getName()+": "+e.getMessage());

                //System.exit(0);
            }
        }
    }
}
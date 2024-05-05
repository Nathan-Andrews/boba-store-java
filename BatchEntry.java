import java.time.Instant;

public class BatchEntry {

    private int batchKey;
    private Instant inDate;
    private Instant expirationDate;
    private int amount;
    private int ingredientId;
    private String itemName;

    BatchEntry(int batchKey, long inDate, long expirationDate, int amount, int ingredientId, String itemName) {
        this.batchKey = batchKey;
        this.inDate = Instant.ofEpochSecond(inDate);
        this.expirationDate = Instant.ofEpochSecond(expirationDate);
        this.amount = amount;
        this.ingredientId = ingredientId;
        this.itemName = itemName;
    }

    // Getters and setters

    public String getItemName() {
        return itemName;
    }

    public int getBatchKey() {
        return batchKey;
    }

    public void setBatchKey(int batchKey) {
        this.batchKey = batchKey;
    }

    public Instant getInDate() {
        return inDate;
    }

    public void setInDate(Instant inDate) {
        this.inDate = inDate;
    }

    public Instant getExpirationDate() {
        return expirationDate;
    }

    public void setExpirationDate(Instant expirationDate) {
        this.expirationDate = expirationDate;
    }

    public int getAmount() {
        return amount;
    }

    public void setAmount(int amount) {
        this.amount = amount;
    }

    public int getIngredientId() {
        return ingredientId;
    }

    public void setIngredientId(int ingredientId) {
        this.ingredientId = ingredientId;
    }

    @Override
    public String toString() {
        return "Batch{" +
                "batchKey='" + batchKey + '\'' +
                ", inDate=" + inDate +
                ", expirationDate=" + expirationDate +
                ", amount=" + amount +
                ", ingredientId='" + ingredientId + '\'' +
                '}';
    }
}

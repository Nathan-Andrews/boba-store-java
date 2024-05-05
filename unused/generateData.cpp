#include <vector>
#include <string>
#include <iostream>
#include <fstream>

struct Item{
private:
    std::string name;
    int id;
    int price;
    double probability;

public:
    Item(std::string name, int id, int price, double probability) : name(name), id(id), price(price), probability(probability) {};

    double getProbability() { return probability; };
    int getId() {return id;};
    int getPrice() {return price;};
};

int main(){
    int totalPrice = 0;

    // so we all have same 'random' data
    srand( 3879696601 );

    // prices and names are pretty much useless but help with understanding what is what
    std::vector<Item> items = {
        Item("Coffee Milk Tea",     1,  4, 0.05),
        Item("Coconut Milk Tea",    2,  4, 0.1),
        Item("Almond Milk Tea",     3,  6, 0.15),
        Item("Thai Milk Tea",       4,  6, 0.1),
        Item("Rosehip Milk Tea",    5,  5, 0.075),
        Item("Coffee Slushie",      6,  5, 0.125),
        Item("Oreo Slushie",        7,  5, 0.15),
        Item("Pina Colada Slushie", 8,  4, 0.1),
        Item("Pineapple Slushie",   9,  5, 0.13),
        Item("Mango Slushie",       10, 7, 0.02),
    };

    std::time_t unixTimestamp   = 1696136400; // time(NULL)
    std::time_t oneDay          = 24*60*60;
    std::time_t oneWeek         = oneDay*7;
    std::time_t oneYear         = oneWeek * 52;
    std::time_t currTime        = unixTimestamp - oneYear;

    std::fstream data;
    data.open("data.csv", std::fstream::in | std::fstream::out | std::fstream::app);
    data << "OrderKey,Timestamp,ItemId\n";

    // index is order_size 0.2 p for 1 size, 0.5 p for 2 or less, etc
    std::vector<double> orderSizeCDF = {0.2, 0.5, 0.7, 1};
    auto generateOrder = [&items, &orderSizeCDF, &totalPrice]() -> std::vector<Item>{
        std::vector<Item> order;

        // CDF for size
        int size = 4;
        double prob = (double)(rand() % 10)/10.0;
        for (int i = 0; i < orderSizeCDF.size()-1; i++){
            bool isWithinCDFBounds = prob < orderSizeCDF[i];
            if (isWithinCDFBounds){
                size = i+1;

                break;
            }
        };

        // 'CDF' for items
        for (int i = 0; i < size; i++){
            prob = (double)(rand() % 100)/100.0;
            for (Item item : items){
                prob -= item.getProbability();
                if (prob <= 0){
                    order.push_back(item);

                    totalPrice += item.getPrice();

                    break;
                }
            }
        }

        return order;
    };

    int orderNum = 0;
    auto populateDay = [&](double strength) -> void{
        int ordersToday = rand() % 350 + 50;
        int start = orderNum;

        for (; orderNum < start+ordersToday; orderNum++){
            std::time_t timeOfDay = rand() % oneDay;

            // we only save orders in database via one item at a time, so this is fine.
            std::vector<Item> order = generateOrder();
            time_t orderTimestamp = currTime + timeOfDay;

            for (Item item : order){
                std::string format = 
                    std::to_string(orderNum)        + "," + 
                    std::to_string(orderTimestamp)  + "," +
                    std::to_string(item.getId())    + "\n";

                data << format;
            }
        }
    };

    // we go back a year then increment by week and populate days in week
    while (currTime < unixTimestamp){
        double weekStrength = (double)(rand() % 15 + 5)/10.0;
        for (int day = 0; day < 7; day++)
            populateDay(weekStrength);
        
        currTime += oneWeek;
    }

    std::cout << "Total Price of Generated Data: " << totalPrice << std::endl;
}
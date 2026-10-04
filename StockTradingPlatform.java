package Java_Project;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Scanner;

public class StockTradingPlatform {
    // Stock class
    static class Stock{
        String symbol;
        String companyName;
        double price;
        Stock(String symbol, String companyName, double price){
            this.symbol = symbol;
            this.companyName = companyName;
            this.price = price;
        }
    }

    // Transaction class
    static class Transaction{
        String type;
        String stockSymbol;
        int quantity;
        double amount;
        Transaction(String type, String stockSymbol, int quantity, double amount){
            this.type = type;
            this.stockSymbol = stockSymbol;
            this.quantity = quantity;
            this.amount = amount;
        }
        public String toString(){
            return type+" | Stock: "+ stockSymbol + " | Quantity: "+ quantity +" | Amount: "+ amount;
        }
    }

    // User class
    static class User{
        String name;
        double balance;
        HashMap<String,Integer> portfolio;
        User(String name, double balance){
            this.name = name;
            this.balance = balance;
            this.portfolio = new HashMap<>();
        }
    }

    static ArrayList<Stock> marketStocks = new ArrayList<>();
    static ArrayList<Transaction> transactions = new ArrayList<>();

    // Find Stock
    static Stock findStock(String symbol){
        for(Stock stock : marketStocks){
            if(stock.symbol.equalsIgnoreCase(symbol)){
                return stock;
            }
        }
        return null;
    }

    // Display Market Data
    static void displayMarketData(){
        System.out.println("\n============== MARKET DATA ==============");
        System.out.println("-----------------------------------------");
        System.out.printf("%-10s %-20s %-10s\n", "Symbol","Company","Price");
        System.out.println("-----------------------------------------");
        for(Stock stock : marketStocks){
            System.out.printf("%-10s %-20s ₹%.2f\n",stock.symbol,stock.companyName,stock.price);
        }
    }

    // Buy Stock
    static void buyStock(User user, Scanner sc){
        System.out.print("Enter Stock Symbol: ");
        String symbol = sc.next();
        Stock stock = findStock(symbol);
        if(stock == null){
            System.out.println("Stock Not Found!");
            return;
        }
        System.out.print("Enter Quantity: ");
        int qty = sc.nextInt();

        double totalCost = stock.price*qty;
        if(user.balance >= totalCost){
            user.balance -= totalCost;
            user.portfolio.put(stock.symbol, user.portfolio.getOrDefault(stock.symbol, 0) + qty);
            transactions.add(new Transaction("BUY",stock.symbol,qty,totalCost));
            System.out.println("Stock Purchase Successfully!");
        }
        else {
            System.out.println("Insufficient Balance!");
        }
    }

    // Sell Stock
    static void sellStock(User user, Scanner sc){
        System.out.print("Enter Stock Symbol: ");
        String symbol = sc.next();

        Stock stock = findStock(symbol);

        if(stock == null){
            System.out.println("Stock Not Found!");
            return;
        }
        if(!user.portfolio.containsKey(stock.symbol)){
            System.out.println("you do not own this stock.");
            return;
        }
        System.out.print("Enter Quantity: ");
        int qty = sc.nextInt();
        int ownedQty = user.portfolio.get(stock.symbol);

        if(qty > ownedQty){
            System.out.println("Not Enough Shares!");
            return;
        }
        double amount = qty * stock.price;
        user.balance += amount;

        if(qty == ownedQty){
            user.portfolio.remove(stock.symbol);
        }
        else{
            user.portfolio.put(stock.symbol, ownedQty - qty);
        }

        transactions.add(new Transaction("SELL",stock.symbol,qty,amount));
        System.out.println("Stock Sold Successfully!");
    }

    // View Portfolio
    static void viewPortfolio(User user){
        System.out.println("\n=============== PORTFOLIO ===============");

        if(user.portfolio.isEmpty()){
            System.out.println("Portfolio Empty.");
            return;
        }

        double portfolioValue = 0;
        System.out.printf("%-10s %-10s %-10s %-10s\n","Stock","Qty","Price","Value");
        for (String symbol : user.portfolio.keySet()){
            int qty = user.portfolio.get(symbol);
            Stock stock = findStock(symbol);
            double value = qty * stock.price;
            portfolioValue += value;
            System.out.printf("%-10s %-10d ₹%-9.2f ₹%.2f\n", symbol, qty, stock.price, value);
        }

        System.out.println("\nBalance: ₹" + user.balance);
        System.out.println("Portfolio Value: ₹" + portfolioValue);
        System.out.println("Total Assets: ₹" + (user.balance + portfolioValue));
    }

    // Transaction History
    static void showTransactions(){
        System.out.println("===== TRANSACTION HISTORY =====");

        if(transactions.isEmpty()){
            System.out.println("No Transactions Yet.");
            return;
        }

        for(Transaction t : transactions){
            System.out.println(t);
        }
    }

    static void main(String[] args) {
        Scanner sc = new Scanner(System.in);
        // Market Stock
        marketStocks.add(new Stock("TCS","Tata Consultansy", 3500));
        marketStocks.add(new Stock("INFY", "Infoys", 1800));
        marketStocks.add(new Stock("WIPRO","Wipro Ltd", 450));
        marketStocks.add(new Stock("HCL", "HCL Tech", 1500));
        marketStocks.add(new Stock("RELIANCE", "Reliance Industries",2800));

        User user = new User("Sohel",100000);

        System.out.println("\n====== STOCK TRADING PLATFORM =====");
        System.out.println("1. Display Market Data");
        System.out.println("2. Buy Stock");
        System.out.println("3. Sell Stock");
        System.out.println("3. View Portfolio");
        System.out.println("4. Transaction History");
        System.out.println("6. Exit");

        int choice;
        do{
            System.out.print("\nEnter Choice : ");
            choice = sc.nextInt();
            switch (choice){
                case 1:
                    displayMarketData();
                    break;
                case 2:
                    buyStock(user, sc);
                    break;
                case 3:
                    sellStock(user, sc);
                    break;
                case 4:
                    viewPortfolio(user);
                    break;
                case 5:
                    showTransactions();
                    break;
                case 6:
                    System.out.println("Thank You!");
                    break;
                default:
                    System.out.println("Invalid Choice!");
            }
        } while(choice != 6);
        sc.close();
    }
}

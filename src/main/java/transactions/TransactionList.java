package transactions;

import com.sun.java.accessibility.util.Translator;
import transactions.Trader;
import transactions.Transaction;

import java.util.*;
import java.util.stream.Collectors;

/**
 * @author Dieter Holz
     */
    public class TransactionList {
        private final List<Transaction> allTransactions = new ArrayList<>();

        public void addTransaction(Transaction transaction) {
            allTransactions.add(transaction);
        }

        public int size() {
            return allTransactions.size();
        }

        /**
         * @param year year which all transactions are filtered by
         * @return a list with transactions of given year
         */
        public List<Transaction> transactionsInYear(int year) {
            return allTransactions.stream()
                    .filter(transaction -> transaction.getYear() == year)
                    .sorted(Comparator.comparing(Transaction::getValue))
                    .collect(Collectors.toList());
        }

        public List<String> cities() {
            return allTransactions.stream()
                    .map(transaction -> transaction.getTrader().getCity()) // faster with 2 method references?
                    .distinct()
                    .collect(Collectors.toList());
        }

        /**
         * @param city the trader's city
         * @return all traders from given city sorted by name.
         */
        public List<Trader> traders(String city) {
            return allTransactions.stream()
                    .filter(transaction -> transaction.getTrader().getCity().equals(city))
                    .sorted(Comparator.comparing(transaction -> transaction.getTrader().getName()))
                    .map(Transaction::getTrader)
                    .distinct()
                    .collect(Collectors.toList());
        }

        /**
         * Returns a Map of all transactions.
         * @return a Map with the year as key and a list of all transactions of this year as value
         */
        public Map<Integer, List<Transaction>> transactionsByYear() {
            return allTransactions.stream()
                    .collect(Collectors.groupingBy(Transaction::getYear));
        }

        /**
         * @param city the city
         * @return true if there are any trader based in given city
         */
        public boolean traderInCity(String city) {
            return allTransactions.stream()
                    .anyMatch(transaction -> transaction.getTrader().getCity().equals(city));
        }

        /**
         * @param from the trader's current location
         * @param to   the trader's new location
         */
        public void relocateTraders(String from, String to) {
            allTransactions.stream()
                .filter(transaction -> transaction.getTrader().getCity().equals(from))
                .forEach(transaction -> transaction.getTrader().setCity(to));

        }

        /**
         * @return the highest value in all the transactions
         */
        public int highestValue() {
            return allTransactions.stream()
                    .mapToInt(Transaction::getValue).max()
                    .orElse(0);
        }

        /**
         * @return the sum of all transaction values
         */
        public int totalValue() {
            return allTransactions.stream().mapToInt(Transaction::getValue).sum();

            /* before refactoring:
            return allTransactions.stream()
            .collect(Collectors.summingInt(Transaction::getValue));
             */
        }

        /**
         * @return the transactions.Transaction with the lowest value
         */
        public Transaction getLowestValueTransaction(){
            return allTransactions.stream()
                    .min(Comparator.comparing(Transaction::getValue))
                    .orElse(null);
        }

        /**
         * @return a string of all traders’ names sorted alphabetically
         */
        public String traderNames() {
            return allTransactions.stream()
                    .sorted(Comparator.comparing(transaction -> transaction.getTrader().getName()))
                    .map(transaction -> transaction.getTrader().getName())
                    .distinct()
                    .collect(Collectors.joining(""));

            /* before refactoring:
            return String.join("",
            allTransactions.stream()
                    .sorted(Comparator.comparing(transaction -> transaction.getTrader().getName()))
                    .map(transaction -> transaction.getTrader().getName())
                    .distinct()
                    .collect(Collectors.toList()));
             */
        }
    }

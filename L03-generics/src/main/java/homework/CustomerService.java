package homework;

import java.util.Map;
import java.util.NavigableMap;
import java.util.TreeMap;

public class CustomerService {

    private final NavigableMap<Customer, String> map = new TreeMap<>();

    public Map.Entry<Customer, String> getSmallest() {
        return copy(map.firstEntry());
    }

    public Map.Entry<Customer, String> getNext(Customer customer) {
        return copy(map.higherEntry(customer));
    }

    public void add(Customer customer, String data) {
        map.put(customer, data);
    }

    private Map.Entry<Customer, String> copy(Map.Entry<Customer, String> entry) {
        if (entry == null) {
            return null;
        }
        var customer = entry.getKey();

        return Map.entry(
                new Customer(
                        customer.getId(),
                        customer.getName(),
                        customer.getScores()
                ),
                entry.getValue()
        );
    }
}

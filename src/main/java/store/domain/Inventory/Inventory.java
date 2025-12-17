package store.domain.Inventory;


import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import store.domain.products.Product;

public class Inventory {
    private final List<Product> products;

    public Inventory(List<Product> products) {
        this.products = products;
    }

    public List<Product> getProducts() {
        return products;
    }

    public Product findProduct(String name, boolean hasPromotion) {
        return products.stream()
                .filter(product -> product.getName().equals(name))
                .filter(product -> product.hasPromotion() == hasPromotion)
                .findFirst()
                .orElse(null);
    }

    public List<Product> findProductsByName(String name) {
        return products.stream()
                .filter(product -> product.getName().equals(name))
                .toList();
    }

    public boolean hasProduct(String name) {
        return products.stream()
                .anyMatch(product -> product.getName().equals(name));
    }

    public int getTotalQuantity(String name) {
        return products.stream()
                .filter(product -> product.getName().equals(name))
                .mapToInt(Product::getQuantity)
                .sum();
    }

    public Map<String, List<Product>> getGroupedProducts() {
        Map<String, List<Product>> grouped = new LinkedHashMap<>();

        for (Product product : products) {
            grouped.computeIfAbsent(product.getName(), k -> new ArrayList<>()).add(product);
        }

        return grouped;
    }
}

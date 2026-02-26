package drinkshop.receipt;

import drinkshop.domain.Order;
import drinkshop.domain.OrderItem;
import drinkshop.domain.Product;

import java.util.List;

public class ReceiptGenerator {
    public String generate(Order order) {
        StringBuilder sb = new StringBuilder();

        sb.append("RECEIPT").append('\n');
        sb.append("Order ID: ").append(order.getId()).append('\n');

        for (OrderItem item : order.getItems()) {
            sb.append(item.getProduct().getNume())
                    .append(" x ")
                    .append(item.getQuantity())
                    .append('\n');
        }

        sb.append("Total: ").append(order.getTotalPrice()).append('\n');
        return sb.toString();
    }
}
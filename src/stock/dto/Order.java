package stock.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class Order {
    private int orderId;
    private int accountId;
    private int stockId;
    private String orderType;
    private int quantity;
    private int priceAtTrade;
    private String tradeDate;
}
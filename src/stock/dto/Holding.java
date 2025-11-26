package stock.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class Holding {
	private int holdingId;
	private int accountId;
	private int stockId;
	private int quantity;
	private int avgPrice;
}

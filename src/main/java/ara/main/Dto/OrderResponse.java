package ara.main.Dto;

import jakarta.persistence.Column;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;

@Builder
@Data
@AllArgsConstructor
public class OrderResponse {
    private String idOrders;
    private double totalPrice;
    private int statePayment;
    private String identification;
    private String dateOrder;
}

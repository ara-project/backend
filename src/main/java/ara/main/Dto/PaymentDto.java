package ara.main.Dto;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class PaymentDto {
    private String token;
    private String idOrder;
    private String cardNumber;
    private String accountHolder;
    private String cardExpiration;
    private String securityCode;
    private int paymentInstallments;
    private int idMethod;
    private Double totalPaid;
}

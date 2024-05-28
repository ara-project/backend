package ara.main.Entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;

@Entity
@Data
@AllArgsConstructor
@NoArgsConstructor
public class Payment {
    @Id
    @Column(name = "payment_id")
    private String paymentId;
    private int state;
    @Column(name = "realization_date")
    private Date realizationDate;
    private String identification;
    @Column(name = "id_order")
    private String idOrder;
    @Column(name = "card_number")
    private String cardNumber;
    @Column(name = "account_holder")
    private String accountHolder;
    @Column(name = "card_expiration")
    private Date cardExpiration;
    @Column(name = "security_code")
    private String securityCode;
    @Column(name = "payment_installments")
    private int paymentInstallments;
    @Column(name = "id_method")
    private int idMethod;
    @Column(name = "total_paid")
    private Double totalPaid;
}

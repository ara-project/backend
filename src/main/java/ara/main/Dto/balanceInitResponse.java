package ara.main.Dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;

import java.util.Date;
@Data
@AllArgsConstructor
@Builder
public class balanceInitResponse {
    private double totalPaid;
    private Date datePay;
}

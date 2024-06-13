package ara.main.Dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;

import java.util.List;
@Data
@AllArgsConstructor
@Builder
public class balanceResponse {
    List<balanceInitResponse> listPayments;
    private double balance;
}

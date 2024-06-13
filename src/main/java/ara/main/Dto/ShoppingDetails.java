package ara.main.Dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;

@Data
@AllArgsConstructor
@Builder
public class ShoppingDetails {
    private double priceTaxes;
    private String img;
    private String nameProduct;
}

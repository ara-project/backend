package ara.main.Dto;

import lombok.AllArgsConstructor;
import lombok.Data;

import java.util.Date;
@AllArgsConstructor
@Data
public class FilterDateDto {
    private Date primaryDate;
    private Date secondaryDate;
}

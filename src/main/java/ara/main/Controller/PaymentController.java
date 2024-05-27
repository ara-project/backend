package ara.main.Controller;

import ara.main.Dto.FilterDateDto;
import ara.main.Dto.OrderDto;
import ara.main.Entity.Orders;
import ara.main.Entity.Payment;
import ara.main.Repositories.JDBCQuerys;
import ara.main.Service.PaymentService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

@RestController
@RequestMapping("/Payment")
@RequiredArgsConstructor
public class PaymentController {
    private final PaymentService paymentService;
    private final JDBCQuerys jdbcQuerys;
    @PostMapping("/save")
    public ResponseEntity<String> saveOrderDetails(@RequestBody Payment payment){
        return paymentService.register(payment);
    }
    @GetMapping("/filter/{primaryDateStr}/{secondaryDateStr}")
    public ResponseEntity<List<Orders>> getOrders(@PathVariable String primaryDateStr, @PathVariable String secondaryDateStr){
        try {
            SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd"); // Formato de fecha esperado

            Date primaryDate = sdf.parse(primaryDateStr);
            Date secondaryDate = sdf.parse(secondaryDateStr);

            List<Orders> list = jdbcQuerys.getHistoricDates(primaryDate,secondaryDate);
            return ResponseEntity.ok(list);
        }catch (ParseException e) {
            throw new RuntimeException(e);
        }
    }
}

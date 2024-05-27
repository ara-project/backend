package ara.main.Controller;

import ara.main.Dto.FilterDateDto;
import ara.main.Dto.OrderDto;
import ara.main.Entity.Orders;
import ara.main.Entity.Payment;
import ara.main.Repositories.JDBCQuerys;
import ara.main.Service.PaymentService;
import ara.main.Service.StatsService;
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
    private final StatsService statsService;
    @PostMapping("/save")
    public ResponseEntity<String> saveOrderDetails(@RequestBody Payment payment){
        return paymentService.register(payment);
    }
    @GetMapping("/filter/{primaryDateStr}/{secondaryDateStr}/{token}")
    public ResponseEntity<List<Orders>> getOrders(@PathVariable String primaryDateStr,
                                                  @PathVariable String secondaryDateStr,@PathVariable String token){
        return statsService.getOrders(primaryDateStr,secondaryDateStr,token);
    }
    @GetMapping("/total/{token}")
    public ResponseEntity<Double> getTotalSpend(@PathVariable String token){
        return statsService.getTotalSpend(token);
    }
}

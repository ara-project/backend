package ara.main.Service;
import ara.main.Config.GeneratorId;
import ara.main.Dto.PaymentDto;
import ara.main.Entity.Payment;
import ara.main.Repositories.PaymentRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.List;
@Service
@RequiredArgsConstructor
public class PaymentService {
    private final PaymentRepository paymentRepository;
    private final GeneratorId generatorId;
    private final JwtService jwtService;
    public ResponseEntity<String> register(PaymentDto payment){
        Date now=new Date();
        try {
            Payment pay= Payment.builder()
                    .paymentId(generatorId.IdGenerator())
                    .state(3)
                    .realizationDate(now)
                    .identification(jwtService.extractID(payment.getToken()))
                    .idOrder(payment.getIdOrder())
                    .cardNumber(payment.getCardNumber())
                    .accountHolder(payment.getAccountHolder())
                    .cardExpiration(payment.getCardExpiration())
                    .securityCode(payment.getCardExpiration())
                    .paymentInstallments(payment.getPaymentInstallments())
                    .idMethod(payment.getIdMethod())
                    .totalPaid(payment.getTotalPaid())
                    .build();
            if (paymentRepository.existsById(pay.getPaymentId())){
                return ResponseEntity.badRequest().body("Ya existe este pago");
            }
            paymentRepository.save(pay);
            return ResponseEntity.ok("El producto fue registrado con exito");
        }
        catch(Exception e){
            return ResponseEntity.badRequest().body(e.toString());
        }
    }
    public ResponseEntity<List<Payment>> getAll(){
        try{
            return ResponseEntity.ok(paymentRepository.findAll());
        }catch(Exception e){
            return ResponseEntity.badRequest().body(null);
        }

    }
}

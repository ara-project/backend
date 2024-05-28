package ara.main.Service;


import ara.main.Entity.MonthlyExpenses;
import ara.main.Repositories.MonthlyExpensesRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class MonthlyExpensesService {
    private final MonthlyExpensesRepository monthlyExpensesRepository;
    private final JwtService jwtService;

    public ResponseEntity<String> createBalance(MonthlyExpenses request){
        String idByToken=jwtService.extractID(request.getIdentification());
        if (monthlyExpensesRepository.existsById(idByToken)){
            MonthlyExpenses balance=monthlyExpensesRepository.findByIdentification(idByToken).orElse(null);
            assert balance != null;
            request.setIdBalance(balance.getIdBalance());
            request.setIdentification(idByToken);
            monthlyExpensesRepository.save(request);
            return ResponseEntity.ok("Guardado Correctamente");
        }else{
            return ResponseEntity.badRequest().body("No existe el id del balance");
        }
    }
}

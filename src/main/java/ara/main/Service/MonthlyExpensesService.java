package ara.main.Service;


import ara.main.Dto.balanceResponse;
import ara.main.Entity.MonthlyExpenses;
import ara.main.Repositories.JDBCQuerys;
import ara.main.Repositories.MonthlyExpensesRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class MonthlyExpensesService {
    private final MonthlyExpensesRepository monthlyExpensesRepository;
    private final JwtService jwtService;
    private final JDBCQuerys repository;

    public ResponseEntity<String> createBalance(MonthlyExpenses request){
        String idByToken=jwtService.extractID(request.getIdentification());
        MonthlyExpenses balance=monthlyExpensesRepository.findByIdentification(idByToken).orElse(null);
        assert balance != null;
        if (monthlyExpensesRepository.existsById(balance.getIdBalance())){
            request.setIdBalance(balance.getIdBalance());
            request.setIdentification(idByToken);
            request.setBalance(balance.getBalance()+ request.getBalance());
            monthlyExpensesRepository.save(request);
            return ResponseEntity.ok("Guardado Correctamente");
        }else{
            return ResponseEntity.badRequest().body("No existe el id del balance");
        }
    }
    public ResponseEntity<balanceResponse> getBalance(String token){
        String idByToken=jwtService.extractID(token);
        MonthlyExpenses balance=monthlyExpensesRepository.findByIdentification(idByToken).orElse(null);
        var response = balanceResponse.builder()
                .listPayments(repository.getPaid(idByToken))
                .balance(balance.getBalance())
                .build();
        return ResponseEntity.ok(response);
    }
}

package ara.main.Service;

import ara.main.Dto.FilterDateDto;
import ara.main.Entity.Orders;
import ara.main.Repositories.JDBCQuerys;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.PathVariable;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.LocalDateTime;
import java.util.Date;
import java.util.List;

@Service
@RequiredArgsConstructor
public class StatsService {
    private final JDBCQuerys jdbcQuerys;
    private final JwtService jwtService;
    public ResponseEntity<List<Orders>> getOrders(String primaryDateStr, String secondaryDateStr,String token){
        Date primaryDate = formatterDate(primaryDateStr);
        Date secondaryDate = formatterDate(secondaryDateStr);
        List<Orders> list = jdbcQuerys.getHistoricDates(primaryDate,secondaryDate, jwtService.extractID(token));
        return ResponseEntity.ok(list);
    }

    public ResponseEntity<Double> getTotalSpend(String token){
        //Obtenemos la hora local
        LocalDateTime now = LocalDateTime.now();
        Date primaryDate = formatterDate(now.withDayOfMonth(1).toString());
        Date secondaryDate = formatterDate(now.withDayOfMonth(31).toString());
        return ResponseEntity.ok(jdbcQuerys.getTotalSpent(primaryDate,secondaryDate,jwtService.extractID(token)));
    }

    private Date formatterDate(String datetSr){
        try {
            SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd"); // Formato de fecha esperado
            Date dateForrmatted = sdf.parse(datetSr);
            return dateForrmatted;
        }catch (ParseException e) {
            throw new RuntimeException(e);
        }
    }
}

package ara.main.Service;

import ara.main.Config.GeneratorId;
import ara.main.Dto.OrderResponse;
import ara.main.Entity.Orders;
import ara.main.Entity.Payment;
import ara.main.Repositories.OrdersRepository;
import ara.main.Repositories.PaymentRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
public class OrdersService implements CRUDInterface<Orders>{
    @Autowired
    private OrdersRepository ordersRepository;
    @Autowired
    private GeneratorId generatorId;
    @Autowired
    private JwtService jwtService;
    @Autowired
    private PaymentRepository paymentRepository;
    @Override
    public ResponseEntity<String> register(Orders order) {
        if (!ordersRepository.existsById(order.getIdOrders())){
            return ResponseEntity.ok("Guardado Correctamente"+saveOrder(order));
        }
        return ResponseEntity.badRequest().body("Ya existe una orden asociada a ese Id");
    }
    @Override
    public ResponseEntity<String> modify(Orders order) {
        if (ordersRepository.existsById(order.getIdOrders())){
            ResponseEntity.ok("Modificado Correctamente"+saveOrder(order));
        }
        return ResponseEntity.badRequest().body("No existe una orden asociada a ese Id");
    }
    @Override
    public ResponseEntity<List<Orders>> getAll() {
        return ResponseEntity.ok(ordersRepository.findAll());
    }
    public ResponseEntity<List<OrderResponse>> getAllByIdUser(String token) {
        List<OrderResponse> listResponse=new ArrayList<>();
        String idUser= jwtService.extractID(token);
        SimpleDateFormat formatter = new SimpleDateFormat("dd MMMM yyyy");
        for (var item: ordersRepository.findAllByIdentification(idUser)){
            Payment data=paymentRepository.findByIdOrder(item.getIdOrders()).orElse(null);
            if (data!=null){
                var responseUser=OrderResponse.builder()
                        .idOrders(item.getIdOrders())
                        .totalPrice(item.getTotalPrice())
                        .statePayment(item.getStatePayment())
                        .identification(item.getIdentification())
                        .dateOrder(formatter.format(data.getRealizationDate()))
                        .build();
                listResponse.add(responseUser);
            }
        }
        return ResponseEntity.ok(listResponse);
    }
    protected String saveOrder(Orders order){
        String Id=generatorId.IdGenerator();
        var orders=Orders.builder()
                .idOrders(Id)
                .totalPrice(order.getTotalPrice())
                .statePayment(3)
                .identification(order.getIdentification())
                .build();
        ordersRepository.save(orders);
        return Id;
    }
}

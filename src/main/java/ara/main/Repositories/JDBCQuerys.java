package ara.main.Repositories;

import ara.main.Dto.FilterDateDto;
import ara.main.Dto.balanceInitResponse;
import ara.main.Entity.Orders;
import ara.main.Entity.Product;
import lombok.RequiredArgsConstructor;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

import java.math.BigInteger;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

@Repository
@RequiredArgsConstructor
public class JDBCQuerys {
    private final JdbcTemplate jdbcTemplate;

    public List<Product> findProduct(String character){
        try{
            String sql = "SELECT * FROM product WHERE name LIKE ?";
            List<Product> ListProduct = jdbcTemplate.query(sql, new Object[] { "%" + character + "%"  } ,(resultSet, rowNum) -> {
                Product product = new Product();
                product.setIdProduct(BigInteger.valueOf(resultSet.getLong("id_product")));
                product.setName(resultSet.getString("name"));
                product.setQuantityAvalaible(resultSet.getInt("quantity_avalaible"));
                product.setCategory(resultSet.getInt("category"));
                product.setState(resultSet.getInt("state"));
                product.setPrice(resultSet.getDouble("price"));
                product.setDescribe(resultSet.getString("describe"));
                product.setPackaging(resultSet.getInt("packaging"));
                product.setBrand(resultSet.getInt("brand"));
                product.setDiscount(resultSet.getDouble("discount"));
                product.setImg_src(resultSet.getString("img_src"));
                product.setContent(resultSet.getString("content"));
                return product;
            });
            return ListProduct;
        }catch (Exception e){
            return null;
        }
    }

    public List<BigInteger> getMostPurchased(String identfication){
        try{
            String sql= """
                    SELECT id_product FROM order_details
                    where id_order=(
                    	SELECT id_orders FROM orders
                    	where identification = ?
                    )
                    """;
            List<BigInteger> product= jdbcTemplate.query(sql,new Object[] { identfication },(resultSet,rowNum)-> BigInteger.valueOf(resultSet.getLong("id_product")));
            return product;
        }catch (Exception e){
            return null;
        }
    }
    public int updatePassword(String confirmPassword,String identification){
        try {
            String sql= """
                    UPDATE persons
                    SET password=?
                    WHERE identification = ?
                    """;
            return jdbcTemplate.update(sql,confirmPassword,identification);
        }catch (Exception e){
            throw new RuntimeException("Hubo en error en la llamada al metodo");
        }
    }
    public String getPasswordByUsername(String username){
        String sql = "SELECT password FROM persons WHERE username = ?";
        try {
            return jdbcTemplate.queryForObject(sql, new Object[]{username}, String.class);
        } catch (EmptyResultDataAccessException e) {
            return "El usuario no existe";
        }
    }

    public List<Orders> getHistoricDates(Date primaryDate, Date secondaryDate,String id){
        try{
            String sql = """
                SELECT * FROM orders
                WHERE id_orders IN (
                    SELECT id_orders FROM payment
                    WHERE realization_date BETWEEN ? AND ?
                    AND identification = ?
                );
                """;
            List<Orders> ListOrder = jdbcTemplate.query(sql, new Object[] { primaryDate, secondaryDate, id } ,(resultSet, rowNum) -> {
                Orders order = new Orders();
                order.setIdOrders(resultSet.getString("id_orders"));
                order.setTotalPrice(resultSet.getDouble("total_price"));
                order.setStatePayment(resultSet.getInt("state_payment"));
                order.setIdentification(resultSet.getString("identification"));

                return order;
            });
            return ListOrder;
        }catch (Exception e){
            throw new RuntimeException(e.getCause());
        }
    }
    public Double getTotalSpent(Date primaryDate, Date secondaryDate,String id){
        try{
            String sql= """
                    SELECT sum(total_paid) FROM payment
                    WHERE realization_date BETWEEN ? AND ?
                    AND identification = ?
                    """;
            return jdbcTemplate.queryForObject(sql, new Object[]{primaryDate,secondaryDate,id}, Double.class);
        }catch (Exception e){
            throw new RuntimeException(e.getMessage());
        }
    }
    public int completePayment(String idPayment){
        try {
            String sql="UPDATE payment SET state=4 WHERE payment_id = ?";
            return jdbcTemplate.update(sql,idPayment);
        }catch(Exception e){
            throw new RuntimeException(e.getMessage());
        }
    }
    public List<balanceInitResponse> getPaid(String idUser){
        try{
            String sql= """
                    SELECT realization_date, total_paid
                    FROM payment
                    WHERE identification=?
                    ORDER BY realization_date DESC
                    LIMIT 3;
                    """;
            List<balanceInitResponse> ListOrder = jdbcTemplate.query(sql, new Object[] { idUser } ,(resultSet, rowNum) -> {
                balanceInitResponse order = balanceInitResponse.builder()
                                .datePay(resultSet.getDate("realization_date"))
                                .totalPaid(resultSet.getDouble("total_paid"))
                                .build();

                return order;
            });
            return ListOrder;
        }catch (Exception e){
            throw new RuntimeException(e.getMessage());
        }
    }
}

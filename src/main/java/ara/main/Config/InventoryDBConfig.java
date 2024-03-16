package ara.main.Config;

import jakarta.persistence.EntityManagerFactory;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.boot.jdbc.DataSourceBuilder;
import org.springframework.boot.orm.jpa.EntityManagerFactoryBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.orm.jpa.JpaTransactionManager;
import org.springframework.orm.jpa.LocalContainerEntityManagerFactoryBean;
import org.springframework.transaction.PlatformTransactionManager;
import org.springframework.transaction.annotation.EnableTransactionManagement;

import javax.sql.DataSource;


@Configuration
@EnableTransactionManagement
@EnableJpaRepositories(
        entityManagerFactoryRef = "inventoryEntityManagerFactory",
        transactionManagerRef = "inventoryTransactionManager",
        basePackages = {"ara.main.Repositories.InventoryRepository"}
)
public class InventoryDBConfig {

    @Primary
    @Bean(name = "inventoryDataSource")
    @ConfigurationProperties(prefix = "spring.inventoryDB.datasource")
    public DataSource inventoryDataSource(){
        return DataSourceBuilder.create().build();
    }

    @Primary
    @Bean(name = "inventoryEntityManagerFactory")
    public LocalContainerEntityManagerFactoryBean inventoryEntityManagerFactory(@org.jetbrains.annotations.NotNull EntityManagerFactoryBuilder builder,
                                                                                @Qualifier("inventoryDataSource") DataSource inventoryDataSource) {
        return builder
                .dataSource(inventoryDataSource)
                .packages("ara.main.Entity.InventoryEntities")
                .build();
    }

    @Bean(name = "inventoryTransactionManager")
    public PlatformTransactionManager inventoryTransactionManager(
            @Qualifier("inventoryEntityManagerFactory") EntityManagerFactory inventoryEntityManagerFactory) {
        return new JpaTransactionManager(inventoryEntityManagerFactory);
    }

}


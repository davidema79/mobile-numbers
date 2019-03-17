package eu.davidemartorana.mobile.numbers.config;

import javax.persistence.EntityManagerFactory;
import javax.sql.DataSource;

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

import eu.davidemartorana.mobile.numbers.source.domain.MobileSubscription;
import eu.davidemartorana.mobile.numbers.source.repositories.MobileSubscriptionsRepository;

/**
 * Configurations regarding data sources
 * 
 * @author davidemartorana
 *
 */
@Configuration
@EnableJpaRepositories(basePackageClasses = { MobileSubscriptionsRepository.class})
public class DataSourceConfig {

	/*
	 * Please note that I might have used the @EnableAutoConfiguration
	 */
	
	@Primary
	@Bean
	@ConfigurationProperties(prefix = "spring.datasource")
	public DataSource dataSource() {
		return DataSourceBuilder
				.create()
				.build();
	}
	
	@Primary
	@Bean
	public LocalContainerEntityManagerFactoryBean entityManagerFactory(final EntityManagerFactoryBuilder builder, final DataSource dataSource) {
		return builder
				.dataSource(dataSource)
				.packages(MobileSubscription.class)
				.build();
	}

	@Bean
	public PlatformTransactionManager transactionManager(final EntityManagerFactory entityManagerFactory) {
		return new JpaTransactionManager(entityManagerFactory);
	}
	
}

package com.sap.training.ems.persistence;

import java.sql.Connection;
import java.util.HashMap;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.Persistence;
import javax.sql.DataSource;

import org.eclipse.persistence.config.PersistenceUnitProperties;
import org.springframework.beans.factory.annotation.Autowire;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.config.ConfigurableBeanFactory;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Lazy;
import org.springframework.context.annotation.Primary;
import org.springframework.context.annotation.Scope;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.orm.jpa.JpaTransactionManager;
import org.springframework.orm.jpa.SharedEntityManagerCreator;
import org.springframework.transaction.PlatformTransactionManager;
import org.springframework.transaction.annotation.EnableTransactionManagement;

import com.sap.training.ems.persistence.context.DataSourceContext;

@Configuration
@EnableJpaRepositories
@EnableTransactionManagement
@EnableJpaAuditing
public class SpringPersistenceConfig {

	protected DataSource dataSource;
	@Autowired
	protected DataSourceContext dataSourceContext;

	protected EntityManagerFactory entityManagerFactory;

	protected EntityManager entityManager;

	@Bean
	public DataSource dataSource() {
		if (this.dataSource != null || this.dataSourceContext == null) {
			System.out.println("datasource is null");
			return this.dataSource;
		}
		try {
			final DataSource dataSource = this.dataSourceContext.getDataSource();
			try (final Connection connection = dataSource.getConnection()) {
				return this.dataSource = dataSource;
			}
		} catch (Exception e) {
			throw new IllegalStateException("No dataSource exist or be created!");
		}
	}

	@Bean
	public EntityManagerFactory entityManagerFactory() {
		HashMap<String, Object> properties = new HashMap<String, Object>();
		properties.put(PersistenceUnitProperties.NON_JTA_DATASOURCE,
				this.dataSourceContext.getDataSource());
		this.entityManagerFactory = Persistence.createEntityManagerFactory(
				"springEmployee", properties);

		return this.entityManagerFactory;
	}
	
	@Bean
	@Primary
	public EntityManager entityManager() {
		if(this.entityManager == null) {
			this.entityManager = SharedEntityManagerCreator.createSharedEntityManager(this.entityManagerFactory());
		}
		return this.entityManager;
	}

	@Bean(autowire = Autowire.BY_TYPE)
	@Scope(ConfigurableBeanFactory.SCOPE_SINGLETON)
	@Lazy(true)
	public synchronized PlatformTransactionManager transactionManager() {
		return new JpaTransactionManager(this.entityManagerFactory());
	}
}

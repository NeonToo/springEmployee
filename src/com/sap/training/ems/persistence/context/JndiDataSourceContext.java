package com.sap.training.ems.persistence.context;

import javax.naming.InitialContext;
import javax.naming.NamingException;
import javax.sql.DataSource;

import org.springframework.beans.factory.config.ConfigurableBeanFactory;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

@Component
@Scope(ConfigurableBeanFactory.SCOPE_SINGLETON)
public class JndiDataSourceContext implements DataSourceContext {
	
	protected DataSource dataSource;
	
	@Override
	public DataSource getDataSource() {
		// TODO Auto-generated method stub
		if(this.dataSource == null) {
			try {
				InitialContext initialContext = new InitialContext();
				this.dataSource = (DataSource) initialContext.lookup("java:comp/env/jdbc/DefaultDB");
			} catch(NamingException e){
				throw new IllegalStateException(e);
			}
		}
		return this.dataSource;
	}

}

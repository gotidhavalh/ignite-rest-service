package io.project.app;

import static org.apache.ignite.cache.CacheAtomicityMode.TRANSACTIONAL;

import java.sql.Types;

import javax.sql.DataSource;

import org.apache.ignite.Ignite;
import org.apache.ignite.Ignition;
import org.apache.ignite.cache.CacheMode;
import org.apache.ignite.cache.store.jdbc.CacheJdbcPojoStoreFactory;
import org.apache.ignite.cache.store.jdbc.JdbcType;
import org.apache.ignite.cache.store.jdbc.JdbcTypeField;
import org.apache.ignite.cache.store.jdbc.dialect.MySQLDialect;
import org.apache.ignite.configuration.CacheConfiguration;
import org.apache.ignite.configuration.IgniteConfiguration;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import io.project.app.model.Flight;

/**
 *
 * @author armdev
 */
@Configuration
public class IgniteConfig {
	
	@Autowired
	private DataSource datasource;

    @Bean(destroyMethod = "close")
    @SuppressWarnings("unchecked")
    public Ignite igniteInstance() {
        IgniteConfiguration igniteConfiguration = new IgniteConfiguration();
        // Setting some custom name for the node.
        igniteConfiguration.setIgniteInstanceName("springDataNode");
        // Enabling peer-class loading feature.
        igniteConfiguration.setPeerClassLoadingEnabled(true);

        // Defining and creating a new cache to be used by Ignite Spring Data
        // repository.
        CacheConfiguration<Long,Flight> cacheConfiguration = new CacheConfiguration<>("FlightCache");
        cacheConfiguration.setAtomicityMode(TRANSACTIONAL);
        //cacheConfiguration.setCacheStoreFactory(new FactoryBuilder.SingletonFactory<CacheStore>());
        cacheConfiguration.setCacheMode(CacheMode.REPLICATED);

        // cacheConfiguration.setCacheStoreFactory(FactoryBuilder.factoryOf(FlightRepository.class));
        //cacheConfiguration.setReadThrough(true);
       // cacheConfiguration.setWriteThrough(true);
        cacheConfiguration.setWriteBehindEnabled(true);
        // Setting SQL schema for the cache.
        cacheConfiguration.setIndexedTypes(Long.class, Flight.class);
        igniteConfiguration.setActiveOnStart(true);
        CacheJdbcPojoStoreFactory<Long, Flight> f = new CacheJdbcPojoStoreFactory<>();
		f.setDataSource(datasource);
		f.setDialect(new MySQLDialect());
		JdbcType jdbcType = new JdbcType();
		jdbcType.setCacheName("FlightCache");
		jdbcType.setKeyType(Long.class);
		jdbcType.setValueType(Flight.class);
		jdbcType.setDatabaseTable("flight");
		jdbcType.setDatabaseSchema("demo");
		jdbcType.setKeyFields(new JdbcTypeField(Types.INTEGER, "id", Long.class, "id"));
		jdbcType.setValueFields(new JdbcTypeField(Types.VARCHAR, "first_name", String.class, "firstName"),
				new JdbcTypeField(Types.VARCHAR, "last_name", String.class, "lastName"),
				new JdbcTypeField(Types.VARCHAR, "country", String.class, "country"),
				new JdbcTypeField(Types.VARCHAR, "city", String.class, "city"),
				new JdbcTypeField(Types.VARCHAR, "address", String.class, "address"),
				new JdbcTypeField(Types.VARCHAR, "birth_date", String.class, "birthDate"));
		f.setTypes(jdbcType);
		cacheConfiguration.setCacheStoreFactory(f);
        igniteConfiguration.setCacheConfiguration(cacheConfiguration);
//        SpringCacheManager springCacheManager = new SpringCacheManager();
//        springCacheManager.setConfiguration(igniteConfiguration);
        //Ignite ignite = Ignition.start("example-ignite.xml");
        // IgniteCache<Long, Flight> cache = ignite.getOrCreateCache(cacheConfiguration);
        return Ignition.start(igniteConfiguration);

        //return ignite;
    }

    // https://github.com/apache/ignite/blob/master/examples/src/main/java/org/apache/ignite/examples/datagrid/CacheQueryExample.java
}

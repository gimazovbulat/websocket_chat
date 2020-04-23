package ru.itis.config;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.zaxxer.hikari.HikariConfig;
import com.zaxxer.hikari.HikariDataSource;
import org.springframework.context.annotation.*;
import org.springframework.core.env.Environment;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.datasource.DriverManagerDataSource;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.JavaMailSenderImpl;
import org.springframework.orm.jpa.JpaTransactionManager;
import org.springframework.orm.jpa.JpaVendorAdapter;
import org.springframework.orm.jpa.LocalContainerEntityManagerFactoryBean;
import org.springframework.orm.jpa.vendor.HibernateJpaVendorAdapter;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.transaction.PlatformTransactionManager;
import org.springframework.transaction.annotation.EnableTransactionManagement;
import org.springframework.web.multipart.commons.CommonsMultipartResolver;

import javax.persistence.EntityManagerFactory;
import javax.sql.DataSource;
import java.util.Properties;

@Configuration
@ComponentScan(basePackages = "ru.itis")
@EnableAspectJAutoProxy
@EnableScheduling
@PropertySource("classpath:/application.properties")
@EnableTransactionManagement
public class ApplicationContextImpl {
    private final static String DB_USERNAME = "db.username";
    private final static String DB_PASSWORD = "db.password";
    private final static String DB_DRIVER = "db.driver";
    private final static String DB_URL = "db.url";

    private final static String MAIL_USERNAME = "mail.username";
    private final static String MAIL_PASSWORD = "mail.password";
    private final static String MAIL_DEBUG = "mail.debug";

    private final static String PROP_DIALECT = "hibernate.dialect";
    private final static String PROP_HIBERNATE_SHOW_SQL = "hibernate.show_sql";
    private final static String PROP_HIBERNATE_ENTITYMANAGER_PACKAGES_TO_SCAN = "entitymanager.packages.to.scan";
    private final static String PROP_HIBERNATE_HBM2DDL_AUTO = "hibernate.hbm2ddl.auto";
    final Environment environment;

    public ApplicationContextImpl(Environment environment) {
        this.environment = environment;
    }

    @Bean
    public JdbcTemplate jdbcTemplate() {
        return new JdbcTemplate(datasource());
    }

    @Bean
    public DataSource datasource() {
        DriverManagerDataSource driverManagerDataSource = new DriverManagerDataSource();
        driverManagerDataSource.setDriverClassName(environment.getRequiredProperty(DB_DRIVER));
        driverManagerDataSource.setUsername(environment.getRequiredProperty(DB_USERNAME));
        driverManagerDataSource.setPassword(environment.getRequiredProperty(DB_PASSWORD));
        driverManagerDataSource.setUrl(environment.getRequiredProperty(DB_URL));
        return driverManagerDataSource;
    }

    @Bean(name = "entityManagerFactory")
    public LocalContainerEntityManagerFactoryBean localContainerEntityManagerFactoryBean() {
        LocalContainerEntityManagerFactoryBean localContainerEntityManagerFactoryBean = new
                LocalContainerEntityManagerFactoryBean();
        localContainerEntityManagerFactoryBean.setDataSource(datasource());
        localContainerEntityManagerFactoryBean.setPackagesToScan(environment.getProperty(PROP_HIBERNATE_ENTITYMANAGER_PACKAGES_TO_SCAN));

        JpaVendorAdapter jpaVendorAdapter = new HibernateJpaVendorAdapter();
        localContainerEntityManagerFactoryBean.setJpaVendorAdapter(jpaVendorAdapter);
        localContainerEntityManagerFactoryBean.setJpaProperties(additionalProperties());
        return localContainerEntityManagerFactoryBean;
    }

    @Bean(name = "transactionManager")
    public PlatformTransactionManager platformTransactionManager(EntityManagerFactory entityManagerFactory) {
        JpaTransactionManager jpaTransactionManager = new JpaTransactionManager(entityManagerFactory);
        jpaTransactionManager.setEntityManagerFactory(entityManagerFactory);
        return jpaTransactionManager;
    }

    @Bean
    public HikariConfig hikariConfig() {
        HikariConfig config = new HikariConfig();
        config.setJdbcUrl(environment.getProperty(DB_URL));
        config.setUsername(environment.getProperty(DB_USERNAME));
        config.setPassword(environment.getProperty(DB_PASSWORD));
        config.setDriverClassName(environment.getProperty(DB_DRIVER));
        return config;
    }

    @Bean
    public DataSource hikariDataSource() {
        return new HikariDataSource(hikariConfig());
    }

    @Bean
    JavaMailSender mailSender() {
        JavaMailSenderImpl mailSender = new JavaMailSenderImpl();
        mailSender.setHost("smtp.gmail.com");
        mailSender.setPort(587);

        mailSender.setUsername(environment.getProperty(MAIL_USERNAME));
        mailSender.setPassword(environment.getProperty(MAIL_PASSWORD));

        Properties props = mailSender.getJavaMailProperties();
        props.put("mail.transport.protocol", "smtp");
        props.put("mail.smtp.auth", "true");
        props.put("mail.smtp.starttls.enable", "true");
        props.put("mail.debug", environment.getProperty(MAIL_DEBUG));

        return mailSender;
    }

    @Bean(name = "multipartResolver")
    public CommonsMultipartResolver multipartResolver() {
        CommonsMultipartResolver multipartResolver = new CommonsMultipartResolver();
        multipartResolver.setMaxUploadSize(100000);
        return multipartResolver;
    }

    @Bean
    Properties additionalProperties() {
        Properties properties = new Properties();
        properties.put(PROP_DIALECT, environment.getRequiredProperty(PROP_DIALECT));
        properties.put(PROP_HIBERNATE_ENTITYMANAGER_PACKAGES_TO_SCAN, environment.getRequiredProperty(PROP_HIBERNATE_ENTITYMANAGER_PACKAGES_TO_SCAN));
        properties.put(PROP_HIBERNATE_SHOW_SQL, environment.getRequiredProperty(PROP_HIBERNATE_SHOW_SQL));
        properties.put(PROP_HIBERNATE_HBM2DDL_AUTO, environment.getRequiredProperty(PROP_HIBERNATE_HBM2DDL_AUTO));
        return properties;
    }

    @Bean
    public PasswordEncoder passwordEncoder(){
        return new BCryptPasswordEncoder();
    }

    @Bean
    public ObjectMapper objectMapper() {
        return new ObjectMapper();
    }

}

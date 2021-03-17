package com.aditor.bi.commercials;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.builder.SpringApplicationBuilder;
import org.springframework.boot.context.web.SpringBootServletInitializer;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Import;
import org.springframework.context.annotation.Profile;
import org.springframework.core.task.AsyncTaskExecutor;
import org.springframework.format.Formatter;
import org.springframework.format.FormatterRegistry;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.datasource.DriverManagerDataSource;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurerAdapter;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

@EnableScheduling
@EnableWebSecurity
@SpringBootApplication
@ComponentScan(value = "com.aditor.bi.commercials")
@Import(SecurityConfiguration.class)
public class Application extends SpringBootServletInitializer {

    public static void main(String[] args)
    {
        SpringApplication.run(Application.class, args);
    }

    @Override
    protected SpringApplicationBuilder configure(SpringApplicationBuilder builder) {
        return builder.sources(Application.class);
    }

    @Bean
    public WebMvcConfigurerAdapter webMvcConfigurerAdapter()
    {
        return new WebMvcConfigurerAdapter() {
            @Override
            public void addFormatters(FormatterRegistry registry) {
                super.addFormatters(registry);
                registry.addFormatter(new Formatter<Date>() {
                    @Override
                    public Date parse(String text, Locale locale) throws ParseException {
                        SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd");

                        return format.parse(text);
                    }

                    @Override
                    public String print(Date object, Locale locale) {
                        SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd");

                        return format.format(object);
                    }
                });
            }
        };
    }

    @Profile(BuildProfiles.PROD)
    @Bean(name = "appslfyerJDBCTemplate")
    public JdbcTemplate jdbcTemplate() {
        DriverManagerDataSource dataSource = new DriverManagerDataSource();
        dataSource.setDriverClassName("com.mysql.jdbc.Driver");
        dataSource.setUrl("jdbc:mysql://10.240.0.15:3306/appsflyer?useSSL=false&zeroDateTimeBehavior=convertToNull");
        dataSource.setUsername("root");
        dataSource.setPassword("Q!w2E#r4T%");

        return new JdbcTemplate(dataSource);
    }

    @Bean
    public AsyncTaskExecutor taskExecutor() {
        ThreadPoolTaskExecutor taskExecutor = new ThreadPoolTaskExecutor();
        taskExecutor.setMaxPoolSize(10);

        return taskExecutor;
    }

    @Bean
    public WebMvcConfigurer webMvcConfigurer() {
        return new WebMvcConfigurerAdapter() {
            @Override
            public void addCorsMappings(CorsRegistry registry) {
                registry.addMapping("/api/**")
                        .maxAge(3600)
                        .allowedMethods("GET", "POST", "PUT", "DELETE");
            }
        };
    }

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

}

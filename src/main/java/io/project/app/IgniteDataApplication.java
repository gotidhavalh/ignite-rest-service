package io.project.app;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;

@SpringBootApplication
//@EnableJpaRepositories("io.project.jparepositories")
//@EntityScan(basePackageClasses = Flight.class)
//@EnableIgniteRepositories("io.project.repositories")
//@Import({SpringConfig.class,IgniteConfig.class})
@EnableJpaRepositories
public class IgniteDataApplication {

    public static void main(String[] args) {
        System.setProperty("IGNITE_QUIET", "false");
        SpringApplication.run(IgniteDataApplication.class, args);
    }  
   

}

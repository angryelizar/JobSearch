package org.example.jobsearch;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.ApplicationContext;

@SpringBootApplication
public class JobSearchApplication {

    public static void main(String[] args) {
//        SpringApplication.run(JobSearchApplication.class, args);

        ApplicationContext applicationContext = SpringApplication.run(JobSearchApplication.class, args);
        CurrencyService bean = applicationContext.getBean(CurrencyService.class);
        bean.calculate();
    }


}

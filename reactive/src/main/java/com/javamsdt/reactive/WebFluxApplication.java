package com.javamsdt.reactive;

import com.javamsdt.reactive.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class WebFluxApplication {

    static {
        //  BlockHound.install();
    }

    public static void main(String[] args) {
        SpringApplication.run(WebFluxApplication.class, args);
    }

}

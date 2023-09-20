package project;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.actuate.autoconfigure.security.servlet.ManagementWebSecurityAutoConfiguration;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.security.servlet.SecurityAutoConfiguration;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.boot.CommandLineRunner;
import project.service.FilesStorageService;

import jakarta.annotation.Resource;

@RestController
@SpringBootApplication(exclude = { SecurityAutoConfiguration.class, ManagementWebSecurityAutoConfiguration.class })
public class PersonalprojectApplication implements CommandLineRunner {

    @Resource
    FilesStorageService storageService;

    public static void main(String[] args) {
        SpringApplication.run(PersonalprojectApplication.class, args);
    }

    @Override
    public void run(String... arg) throws Exception {
        // storageService.deleteAll();
        storageService.init();

    }

}

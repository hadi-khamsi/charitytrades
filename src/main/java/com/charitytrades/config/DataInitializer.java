package com.charitytrades.config;

import com.charitytrades.entity.*;
import com.charitytrades.repository.CharityRepository;
import com.charitytrades.repository.UserRepository;
import com.charitytrades.service.ExchangeService;
import com.charitytrades.service.GlobalGivingImportService;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.List;

@Component
public class DataInitializer implements CommandLineRunner {

    private final CharityRepository charityRepository;
    private final UserRepository userRepository;
    private final GlobalGivingImportService globalGivingImportService;
    private final ExchangeService exchangeService;

    public DataInitializer(CharityRepository charityRepository,
                          UserRepository userRepository,
                          GlobalGivingImportService globalGivingImportService,
                          ExchangeService exchangeService) {
        this.charityRepository = charityRepository;
        this.userRepository = userRepository;
        this.globalGivingImportService = globalGivingImportService;
        this.exchangeService = exchangeService;
    }

    @Override
    @Transactional
    public void run(String... args) {
        if (charityRepository.count() > 0) {
            return;
        }

        List<Project> projects = globalGivingImportService.importFeaturedProjects(10);
        System.out.println("Loaded " + projects.size() + " projects from GlobalGiving");

        if (userRepository.count() == 0) {
            userRepository.save(new User("guest", "guest@charitytrades.com", "-", AccountType.PERSONAL));
            System.out.println("Created guest user");
        }

        // Create corporate matcher and add matching pledges for select projects
        createCorporateMatchers(projects);
    }

    private void createCorporateMatchers(List<Project> projects) {
        User corporate = userRepository.save(
            new User("MatchCorp", "matching@corp.example", "-", AccountType.CORPORATE)
        );
        System.out.println("Created corporate matcher user");

        // Add matching pledges for projects at indices 0, 1, 4, 6, 7 (IDs will be 1, 2, 5, 7, 8)
        int[] projectIndices = {0, 1, 4, 6, 7};
        BigDecimal[] matchRatios = {
            new BigDecimal("1.0"),   // 1:1 match
            new BigDecimal("2.0"),   // 2:1 match
            new BigDecimal("1.5"),   // 1.5:1 match
            new BigDecimal("1.0"),   // 1:1 match
            new BigDecimal("0.5")    // 0.5:1 match
        };
        BigDecimal[] maxAmounts = {
            new BigDecimal("500"),
            new BigDecimal("1000"),
            new BigDecimal("750"),
            new BigDecimal("500"),
            new BigDecimal("250")
        };

        for (int i = 0; i < projectIndices.length; i++) {
            int idx = projectIndices[i];
            if (idx < projects.size()) {
                Project project = projects.get(idx);
                exchangeService.createMatchingPledge(
                    corporate.getId(),
                    project.getId(),
                    matchRatios[i],
                    maxAmounts[i]
                );
                System.out.println("Added matching pledge for project: " + project.getName());
            }
        }
    }
}

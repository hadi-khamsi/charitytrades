package com.charitytrades.config;

import com.charitytrades.entity.*;
import com.charitytrades.repository.*;
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
    private final MatchingOrderRepository matchingOrderRepository;
    private final CentralBookRepository centralBookRepository;
    private final GlobalGivingImportService globalGivingImportService;

    public DataInitializer(CharityRepository charityRepository,
                          UserRepository userRepository,
                          MatchingOrderRepository matchingOrderRepository,
                          CentralBookRepository centralBookRepository,
                          GlobalGivingImportService globalGivingImportService) {
        this.charityRepository = charityRepository;
        this.userRepository = userRepository;
        this.matchingOrderRepository = matchingOrderRepository;
        this.centralBookRepository = centralBookRepository;
        this.globalGivingImportService = globalGivingImportService;
    }

    @Override
    @Transactional
    public void run(String... args) {
        if (charityRepository.count() > 0) {
            return;
        }

        List<Project> projects = globalGivingImportService.importFeaturedProjects(10);
        System.out.println("Loaded " + projects.size() + " projects from GlobalGiving");

        createDemoUsers(projects);
    }

    private void createDemoUsers(List<Project> projects) {
        userRepository.save(new User("demo_user", "demo@charitytrades.com", "demo123", AccountType.PERSONAL));
        userRepository.save(new User("test_trader", "test@charitytrades.com", "test123", AccountType.PERSONAL));

        User techCorp = userRepository.save(new User("TechCorp", "matching@techcorp.com", "corp123", AccountType.CORPORATE));
        User greenFoundation = userRepository.save(new User("GreenFoundation", "grants@greenfoundation.org", "green123", AccountType.CORPORATE));
        User globalBank = userRepository.save(new User("GlobalBank", "csr@globalbank.com", "bank123", AccountType.CORPORATE));

        if (projects.size() >= 1) {
            createMatchingPledge(techCorp, projects.get(0), new BigDecimal("1.0"), new BigDecimal("500.00"));
            createMatchingPledge(greenFoundation, projects.get(0), new BigDecimal("2.0"), new BigDecimal("2000.00"));
        }
        if (projects.size() >= 2) {
            createMatchingPledge(globalBank, projects.get(1), new BigDecimal("1.0"), new BigDecimal("5000.00"));
        }
        if (projects.size() >= 3) {
            createMatchingPledge(techCorp, projects.get(2), new BigDecimal("1.5"), new BigDecimal("1000.00"));
        }
        if (projects.size() >= 4) {
            createMatchingPledge(greenFoundation, projects.get(3), new BigDecimal("1.0"), new BigDecimal("750.00"));
        }
    }

    private void createMatchingPledge(User corporate, Project project, BigDecimal ratio, BigDecimal maxAmount) {
        MatchingOrder pledge = new MatchingOrder(corporate, project, ratio, maxAmount);
        pledge = matchingOrderRepository.save(pledge);

        CentralBook clob = centralBookRepository.findByProjectId(project.getId())
                .orElseGet(() -> centralBookRepository.save(new CentralBook(project)));
        clob.addAsk(pledge);
        centralBookRepository.save(clob);
    }
}

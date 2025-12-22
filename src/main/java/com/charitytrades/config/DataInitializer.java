package com.charitytrades.config;

import com.charitytrades.entity.Charity;
import com.charitytrades.entity.Project;
import com.charitytrades.entity.User;
import com.charitytrades.repository.CharityRepository;
import com.charitytrades.repository.ProjectRepository;
import com.charitytrades.repository.UserRepository;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;

@Component
public class DataInitializer implements CommandLineRunner {

    private final CharityRepository charityRepository;
    private final ProjectRepository projectRepository;
    private final UserRepository userRepository;

    public DataInitializer(CharityRepository charityRepository,
                          ProjectRepository projectRepository,
                          UserRepository userRepository) {
        this.charityRepository = charityRepository;
        this.projectRepository = projectRepository;
        this.userRepository = userRepository;
    }

    @Override
    public void run(String... args) {
        if (charityRepository.count() > 0) {
            return;
        }

        Charity mercy = charityRepository.save(new Charity(
            "Mercy Corps",
            "Humanitarian organization helping communities in crisis survive and thrive through economic development, health, and infrastructure programs."
        ));

        Charity waterAid = charityRepository.save(new Charity(
            "WaterAid Tanzania",
            "Providing clean water, decent toilets, and hygiene education to communities across East Africa."
        ));

        Charity roomToRead = charityRepository.save(new Charity(
            "Room to Read",
            "Transforming lives through literacy and gender equality in education across Asia and Africa."
        ));

        Charity heifer = charityRepository.save(new Charity(
            "Heifer International",
            "Ending hunger and poverty through sustainable agriculture and livestock programs."
        ));

        Charity directRelief = charityRepository.save(new Charity(
            "Direct Relief",
            "Improving health and lives of people affected by poverty and emergencies through essential medical resources."
        ));

        Charity kiva = charityRepository.save(new Charity(
            "Kiva Microfunds",
            "Expanding financial access to underserved communities through crowdfunded microloans."
        ));

        projectRepository.save(createProject(
            "Emergency Food Aid - Yemen Crisis",
            "Providing emergency food packages to families displaced by conflict in Yemen.",
            new BigDecimal("35.00"),
            "https://images.unsplash.com/photo-1488521787991-ed7bbaae773c?w=400",
            mercy
        ));

        projectRepository.save(createProject(
            "Small Business Recovery - Syria",
            "Helping Syrian refugees restart small businesses with microloans and business training.",
            new BigDecimal("100.00"),
            "https://images.unsplash.com/photo-1559027615-cd4628902d4a?w=400",
            mercy
        ));

        projectRepository.save(createProject(
            "Drill a Well in Rural Tanzania",
            "Constructing deep boreholes to provide clean drinking water to villages.",
            new BigDecimal("50.00"),
            "https://images.unsplash.com/photo-1541544537156-7627a7a4aa1c?w=400",
            waterAid
        ));

        projectRepository.save(createProject(
            "School Sanitation - Dodoma Region",
            "Building toilet blocks and handwashing stations at rural primary schools.",
            new BigDecimal("25.00"),
            "https://images.unsplash.com/photo-1594398901394-4e34939a4fd0?w=400",
            waterAid
        ));

        projectRepository.save(createProject(
            "Girls Education Scholarship - Nepal",
            "Full scholarship covering school fees, uniforms, and supplies for girls in rural Nepal.",
            new BigDecimal("150.00"),
            "https://images.unsplash.com/photo-1497633762265-9d179a990aa6?w=400",
            roomToRead
        ));

        projectRepository.save(createProject(
            "Library Construction - Cambodia",
            "Building school libraries stocked with local-language children's books.",
            new BigDecimal("75.00"),
            "https://images.unsplash.com/photo-1481627834876-b7833e8f5570?w=400",
            roomToRead
        ));

        projectRepository.save(createProject(
            "Teacher Training - Vietnam",
            "Training teachers in literacy instruction methods to improve reading outcomes.",
            new BigDecimal("40.00"),
            "https://images.unsplash.com/photo-1509062522246-3755977927d7?w=400",
            roomToRead
        ));

        projectRepository.save(createProject(
            "Dairy Goats for Families - Uganda",
            "Providing dairy goats to families with training on animal husbandry.",
            new BigDecimal("120.00"),
            "https://images.unsplash.com/photo-1524024973431-2ad916746881?w=400",
            heifer
        ));

        projectRepository.save(createProject(
            "Beekeeping Cooperative - Honduras",
            "Establishing beekeeping cooperatives with hives, equipment, and marketing training.",
            new BigDecimal("60.00"),
            "https://images.unsplash.com/photo-1558642452-9d2a7deb7f62?w=400",
            heifer
        ));

        projectRepository.save(createProject(
            "Medical Supplies - Gaza Emergency",
            "Emergency medical supplies including trauma kits, surgical supplies, and medications.",
            new BigDecimal("45.00"),
            "https://images.unsplash.com/photo-1584820927498-cfe5211fd8bf?w=400",
            directRelief
        ));

        projectRepository.save(createProject(
            "Maternal Health Kits - Malawi",
            "Safe delivery kits for expecting mothers in areas with limited healthcare access.",
            new BigDecimal("30.00"),
            "https://images.unsplash.com/photo-1584515933487-779824d29309?w=400",
            directRelief
        ));

        projectRepository.save(createProject(
            "Diabetes Care Program - Mexico",
            "Insulin and testing supplies for underserved diabetes patients.",
            new BigDecimal("55.00"),
            "https://images.unsplash.com/photo-1579684385127-1ef15d508118?w=400",
            directRelief
        ));

        projectRepository.save(createProject(
            "Women Entrepreneurs Fund - Kenya",
            "Microloans for women starting or expanding small businesses in Nairobi.",
            new BigDecimal("25.00"),
            "https://images.unsplash.com/photo-1573497019940-1c28c88b4f3e?w=400",
            kiva
        ));

        projectRepository.save(createProject(
            "Farming Equipment Loans - Philippines",
            "Low-interest loans for farmers to purchase equipment and improve crop yields.",
            new BigDecimal("50.00"),
            "https://images.unsplash.com/photo-1500937386664-56d1dfef3854?w=400",
            kiva
        ));

        userRepository.save(new User("demo_user", "demo@charitytrades.com", "demo123"));
        userRepository.save(new User("test_trader", "test@charitytrades.com", "test123"));
    }

    private Project createProject(String name, String description, BigDecimal minAmount, String imageUrl, Charity charity) {
        Project p = new Project();
        p.setName(name);
        p.setDescription(description);
        p.setMinimumAmount(minAmount);
        p.setImageUrl(imageUrl);
        p.setCharity(charity);
        return p;
    }
}

package com.campusopportunity.config;

import com.campusopportunity.entity.Opportunity;
import com.campusopportunity.repository.OpportunityRepository;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

import java.time.LocalDate;

// Adds FICTIONAL demo opportunities the first time the app starts (only if the table is empty).
@Component
public class DataLoader implements CommandLineRunner {

    private final OpportunityRepository opportunityRepository;

    public DataLoader(OpportunityRepository opportunityRepository) {
        this.opportunityRepository = opportunityRepository;
    }

    @Override
    public void run(String... args) {
        if (opportunityRepository.count() > 0) {
            return;
        }
        LocalDate today = LocalDate.now();

        add("[DEMO] Software Engineering Internship", "DemoSoft Technologies (Fictional)", "Internship",
                "Fictional demo opportunity. A 3-month summer internship working on web applications with Java and SQL.",
                "Hyderabad", today.plusDays(25), 7.0, "CSE, IT, ECE", 2028, "Java, SQL", null);

        add("[DEMO] Campus Coding Competition", "CodeArena Club (Fictional)", "Competition",
                "Fictional demo opportunity. An individual competitive programming contest with problems on arrays, strings and recursion.",
                "Online", today.plusDays(5), 0.0, "All", null, "Java", null);

        add("[DEMO] Campus Placement Drive", "ExampleCorp Solutions (Fictional)", "Placement",
                "Fictional demo opportunity. On-campus recruitment drive: aptitude test, technical interview and HR round.",
                "Bengaluru", today.plusDays(40), 7.5, "CSE, IT", 2026, "Java, SQL, Problem Solving", 0);

        add("[DEMO] Student Merit Scholarship", "Sample Education Trust (Fictional)", "Scholarship",
                "Fictional demo opportunity. Financial support for students with strong academic performance.",
                "India", today.plusDays(60), 8.5, "All", null, null, 0);

        add("[DEMO] College Hackathon 24h", "TechFest Student Chapter (Fictional)", "Hackathon",
                "Fictional demo opportunity. A 24-hour team hackathon to build a working prototype for a campus problem.",
                "Hyderabad", today.plusDays(12), 6.0, "All", null, "HTML, JavaScript", null);

        add("[DEMO] Data Analytics Internship", "DataNest Labs (Fictional)", "Internship",
                "Fictional demo opportunity. Work with a small team on dashboards and reports using SQL and Excel.",
                "Remote", today.plusDays(18), 7.5, "CSE, IT, AIDS", 2027, "SQL, Excel", null);

        add("[DEMO] Women in Tech Scholarship", "Sample Foundation (Fictional)", "Scholarship",
                "Fictional demo opportunity. Example scholarship description. Deadline already passed (shows the Closed badge).",
                "India", today.minusDays(10), 7.0, "CSE, IT, ECE, EEE", null, null, null);

        add("[DEMO] Web Development Hackathon", "BuildIt Community (Fictional)", "Hackathon",
                "Fictional demo opportunity. Build a responsive website in 48 hours.",
                "Chennai", today.plusDays(3), 0.0, "All", null, "HTML, CSS", null);

        System.out.println("CampusOpportunity: demo opportunities inserted.");
    }

    private void add(String title, String company, String type, String description, String location,
                     LocalDate deadline, Double minCgpa, String branch, Integer gradYear,
                     String skills, Integer maxBacklogs) {
        Opportunity o = new Opportunity();
        o.setTitle(title);
        o.setCompany(company);
        o.setType(type);
        o.setDescription(description);
        o.setLocation(location);
        o.setDeadline(deadline);
        o.setMinCgpa(minCgpa);
        o.setEligibleBranch(branch);
        o.setGraduationYear(gradYear);
        o.setRequiredSkills(skills);
        o.setApplicationLink("https://example.com/demo-apply");
        o.setMaxBacklogs(maxBacklogs);
        opportunityRepository.save(o);
    }
}

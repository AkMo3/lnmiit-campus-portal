package com.example.application.data.generator;

import com.example.application.data.entity.HostelAdmin;
import com.example.application.data.entity.Student;
import com.example.application.data.entity.TripDetail;
import com.example.application.data.repository.HostelAdminRepository;
import com.example.application.data.repository.StudentRepository;
import com.example.application.data.repository.TripDetailRepository;
import com.vaadin.flow.spring.annotation.SpringComponent;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Profile;
import org.vaadin.artur.exampledata.DataType;
import org.vaadin.artur.exampledata.ExampleDataGenerator;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Random;
import java.util.Set;
import java.util.stream.Collectors;
import java.util.stream.Stream;

@SpringComponent
public class DataGenerator {

    private final static String HASHED_PASSWORD = "$2a$10$vgfFjVuLHzI.Janws1mW0.j0kuln/fhXytOboG6RhPqI2cGLXS2Qy";

    @Bean
    @Profile({"!production"})
    public CommandLineRunner loadData(TripDetailRepository tripDetailRepository,
                                      StudentRepository studentRepository,
                                      HostelAdminRepository hostelAdminRepository) {

        return args -> {
            Logger logger = LoggerFactory.getLogger(getClass());
            if (tripDetailRepository.count() != 0L) {
                logger.info("Using existing database");
                return;
            }
            int seed = 123;
            Random r = new Random(seed);

            logger.info("Generating demo data");
            Student student1 = Student.builder().firstName("Akash").lastName("Mondal")
                    .hashedPassword(HASHED_PASSWORD).hostel("BH4").phoneNumber("7498550574")
                    .role("STUDENT").rollNumber("20UCS013").build();

            Student student2 = Student.builder().firstName("Arjun").lastName("Aggarwal").rollNumber("20UCS017")
                    .hostel("BH2").hashedPassword(HASHED_PASSWORD).role("STUDENT").phoneNumber("88888888").build();

            student1 = studentRepository.save(student1);
            student2 = studentRepository.save(student2);

            List<Student> studentList = List.of(student1, student2);
            List<String> placeList = Stream.of("Raja Park", "Railway Station", "LNMIIT")
                    .collect(Collectors.toList());

            logger.info("... generating 50 Trip entities...");
            ExampleDataGenerator<TripDetail> tripDetailGenerator = new ExampleDataGenerator<>(TripDetail.class,
                    LocalDateTime.now());
            tripDetailGenerator.setData(TripDetail::setTimeOfArrival, DataType.DATETIME_NEXT_7_DAYS);

            List<TripDetail> tripDetails = tripDetailGenerator.create(5, seed).stream().peek(tripDetail -> {
                tripDetail.setStudent(studentList.get(r.nextInt(studentList.size())));
                tripDetail.setToLocation(placeList.get(r.nextInt(placeList.size())));
                tripDetail.setTimeOfDeparture(LocalDateTime.now());
            }).collect(Collectors.toList());

            tripDetailRepository.saveAll(tripDetails);

            HostelAdmin admin1 = HostelAdmin.builder().emailId("BH2support@gmail.com")
                    .hashedPassword(HASHED_PASSWORD).accountName("BH2 Support").role("CARETAKER")
                    .hostel(Set.of("BH2")).build();

            HostelAdmin admin2 = HostelAdmin.builder().emailId("BH2warden@gmail.com")
                    .hashedPassword(HASHED_PASSWORD).accountName("BH2 Warden").role("WARDEN")
                            .hostel(Set.of("BH2", "BH4")).build();

            HostelAdmin ghAdmin = HostelAdmin.builder().emailId("ghSupport@gmail.com")
                            .hashedPassword(HASHED_PASSWORD).accountName("GH Support").role("CARETAKER")
                            .hostel(Set.of("GH")).build();

            hostelAdminRepository.saveAll(List.of(admin1, admin2, ghAdmin));
            logger.info("Generated demo data");
        };
//        return args -> {};
    }

}

package com.asj.gestionhorarios.config.seeder;

import com.asj.gestionhorarios.exception.customExceptions.NotFoundException;
import com.asj.gestionhorarios.model.entity.*;
import com.asj.gestionhorarios.model.enums.RoleTypes;
import com.asj.gestionhorarios.repository.*;
import com.github.javafaker.Faker;
import com.github.javafaker.service.FakeValuesService;
import com.github.javafaker.service.RandomService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.event.ContextRefreshedEvent;
import org.springframework.context.event.EventListener;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.lang.reflect.Field;
import java.time.ZoneId;
import java.util.*;
import java.util.concurrent.TimeUnit;

@RequiredArgsConstructor
@Service
@Slf4j
public class DataSeeder {
    @Value("${dummies-pass}")
    private String dummiesPass;
    private final RoleRepository roleRepository;
    private final MonthRepository monthRepository;
    private final PriorityRepository priorityRepository;
    private final StatusRepository statusRepository;
    private final PersonRepository personRepository;
    private final ProjectRepository projectRepository;
    private final PasswordEncoder passwordEncoder;
    private final ClientRepository clientRepository;
    private final SprintRepository sprintRepository;
    private final TaskRepository taskRepository;
    private final Faker faker;
    private final Random random = new Random();
    FakeValuesService fakeValuesService = new FakeValuesService(
            new Locale("es-AR"), new RandomService());
    List<Person> people = new ArrayList<>();
    List<Project> projects = new ArrayList<>();
    Set<String> usedProjectNames = new HashSet<>();
    List<String> personId = new ArrayList<>();


    @EventListener
    public void seed(ContextRefreshedEvent event) {
        if (personRepository.existsByEmail("admin@mail.com")) {
            return;
        }
        log.info("Inicializando base de datos. Por favor espere.");
        final Role[] roleList = {new Role("ADMIN"),
                new Role("MANAGEMENT"),
                new Role("DEVELOPER"),
                new Role("BLOCKED"),
                new Role("PENDING")};

        final Priority[] PRIORITYNAME = {
                new Priority("HIGH"),
                new Priority("MEDIUM"),
                new Priority("LOW"),
        };

        final Status[] STATUSNAME = {
                new Status("PENDING"),
                new Status("IN_PROGRESS"),
                new Status("DONE"),
                new Status("CANCELLED"),
                new Status("REVIEWING")
        };

        final Month[] MONTHDATA = {
                new Month(2023, 5, 20),
                new Month(2023, 6, 20),
                new Month(2023, 7, 21),
                new Month(2023, 8, 22),
                new Month(2023, 9, 21),
                new Month(2023, 10, 20),
                new Month(2023, 11, 21),
                new Month(2023, 12, 19),
                new Month(2024, 1, 19),
                new Month(2024, 2, 19),
                new Month(2024, 3, 20),
                new Month(2024, 4, 21),
        };
        List<GrantedAuthority> authorities = Arrays.asList(new SimpleGrantedAuthority("ADMIN"));
        UserDetails userDetails = new User("admin@mail.com", "", authorities);
        Authentication authentication = new UsernamePasswordAuthenticationToken(userDetails, null, authorities);
        SecurityContextHolder.getContext().setAuthentication(authentication);

        testSeed(PRIORITYNAME, STATUSNAME, MONTHDATA, roleList);
        createPerson(roleList);
        createClient();
        createProject();
        createTask();
        log.info("Seeder generado.");
    }

    public Set<Role> addRoles(Role[] dataRoles) {
        Set<Role> roles = new HashSet<>();
        for (Role rolName : dataRoles) {
            try {
                Field field = RoleTypes.class.getField(rolName.getRole_name());
                Object value = field.get(null);
                roles.add((Role) value);
            } catch (NoSuchFieldException | IllegalAccessException e) {
                e.printStackTrace();
            }
        }
        return roles;
    }

    public void createPerson(Role[] roleList) {
        for (int i = 0; i < 100; i++) {
            String name = faker.name().lastName();
            String lastname = faker.name().lastName();
            Role[] role = new Role[]{roleList[0]};
            Person person = Person.builder()
                    .name(name)
                    .lastname(lastname)
                    .cuil(fakeValuesService.regexify("[20-27]{2}[0-9]{8}"))
                    .email(generateEmail(name, lastname))
                    .password(passwordEncoder.encode(dummiesPass))
                    .tel(faker.phoneNumber().phoneNumber())
                    .roles(addRoles(role))
                    .build();
            personRepository.save(person);
            people.add(person);
            personId.add(person.getEmail());
        }

        for (int i = 0; i < 30; i++) {
            String name = faker.name().lastName();
            String lastname = faker.name().lastName();
            Role[] role = new Role[]{roleList[2]};
            Person person = Person.builder()
                    .name(name)
                    .lastname(lastname)
                    .cuil(fakeValuesService.regexify("[20-27]{2}[0-9]{8}"))
                    .email(generateEmail(name, lastname))
                    .password(passwordEncoder.encode(dummiesPass))
                    .tel(faker.phoneNumber().phoneNumber())
                    .roles(addRoles(role))
                    .build();
            personRepository.save(person);
            people.add(person);
            personId.add(person.getEmail());
        }

        for (int i = 0; i < 67; i++) {
            String name = faker.name().lastName();
            String lastname = faker.name().lastName();
            Role[] role = new Role[]{roleList[1]};
            Person person = Person.builder()
                    .name(name)
                    .lastname(lastname)
                    .cuil(fakeValuesService.regexify("[20-27]{2}[0-9]{8}"))
                    .email(generateEmail(name, lastname))
                    .password(passwordEncoder.encode(dummiesPass))
                    .tel(faker.phoneNumber().phoneNumber())
                    .roles(addRoles(role))
                    .build();
            personRepository.save(person);
            people.add(person);
            personId.add(person.getEmail());
        }

        String name = "Admin";
        String lastname = "Admin";
        Role[] role = new Role[]{roleList[0]};
        Person admin = Person.builder()
                .name(name)
                .lastname(lastname)
                .cuil(fakeValuesService.regexify("[20-27]{2}[0-9]{8}"))
                .email("admin@mail.com")
                .password(passwordEncoder.encode(dummiesPass))
                .tel(faker.phoneNumber().phoneNumber())
                .roles(addRoles(role))
                .build();
        personRepository.save(admin);

        String devName = "Developer";
        String devLastname = "Developer";
        Role[] devRole = new Role[]{roleList[2]};
        Person dev = Person.builder()
                .name(devName)
                .lastname(devLastname)
                .cuil(fakeValuesService.regexify("[20-27]{2}[0-9]{8}"))
                .email("developer@mail.com")
                .password(passwordEncoder.encode(dummiesPass))
                .tel(faker.phoneNumber().phoneNumber())
                .roles(addRoles(devRole))
                .build();
        personRepository.save(dev);

        String managName = "Management";
        String managLastname = "Management";
        Role[] managRole = new Role[]{roleList[1]};
        Person manag = Person.builder()
                .name(managName)
                .lastname(managLastname)
                .cuil(fakeValuesService.regexify("[20-27]{2}[0-9]{8}"))
                .email("management@mail.com")
                .password(passwordEncoder.encode(dummiesPass))
                .tel(faker.phoneNumber().phoneNumber())
                .roles(addRoles(managRole))
                .build();
        personRepository.save(manag);

    }

    private static String generateEmail(String name, String lastname) {
        String mail1 = name.toLowerCase();
        String mail2 = lastname.toLowerCase();
        return mail1 + "." + mail2 + "@mail.com";
    }

    public void createClient() {
        Set<String> usedNames = new HashSet<>();
        Set<String> usedEmails = new HashSet<>();

        for (int i = 0; i < 500; i++) {
            String businessName = generateUniqueBusinessName(usedNames);
            String emailName = generateUniqueEmailName(usedEmails);
            Client client = Client.builder()
                    .business_name(businessName)
                    .address(faker.address().streetAddress())
                    .email(generateBusinessEmail(businessName))
                    .initial_date(faker.date().past(150, TimeUnit.DAYS).toInstant().atZone(ZoneId.systemDefault()).toLocalDate())
                    .build();
            clientRepository.save(client);
            usedNames.add(businessName);
            usedEmails.add(emailName);
        }
    }

    private static String generateBusinessEmail(String businessName) {
        String sinEspacios = businessName.toLowerCase().replace(" ", "");
        String sinComa = sinEspacios.replace(",", "");
        String mail = sinComa.replace("-", "");

        return mail + "@mail.com";
    }

    private String generateUniqueBusinessName(Set<String> usedNames) {
        String businessName = faker.company().name();
        while (usedNames.contains(businessName)) {
            businessName = faker.company().name();
        }
        return businessName;
    }

    private String generateUniqueEmailName(Set<String> usedEmails) {
        String emailName = faker.internet().emailAddress();
        while (usedEmails.contains(emailName)) {
            emailName = faker.internet().emailAddress();
        }
        return emailName;
    }

    public void createProject() {

        String[] tecnologias = {"Java", "TypeScript", "JavaScript", "Ruby", "C#", "PHP"};
        for (int i = 0; i < 300; i++) {
            String projectName = generateUniqueProjectName(usedProjectNames);
            Client clientRandom = clientRepository.getReferenceById(Long.valueOf(faker.random().nextInt(1, 500)));
            Project project = Project.builder()
                    .hour_price(faker.number().numberBetween(100, 200))
                    .name(projectName)
                    .stack(faker.options().nextElement(tecnologias))
                    .description(faker.lorem().sentence())
                    .hours_estimate(Long.valueOf(faker.random().nextInt(1, 100)))
                    .end_estimate_date(faker.date().future(365, TimeUnit.DAYS).toInstant().atZone(ZoneId.systemDefault()).toLocalDate())
                    .client(clientRandom)
                    .sprints(randomSprint(i + 1))
                    .people(randomPeople(people))
                    .build();
            projectRepository.save(project);
            projects.add(project);
            usedProjectNames.add(projectName);
        }
    }

    private List<Sprint> randomSprint(int i) {
        int random = faker.random().nextInt(1, 5);
        List<Sprint> sprints = new ArrayList();
        for (int j = 0; j < random; j++) {
            sprints.add(new Sprint(j, faker.date().past(1, TimeUnit.DAYS).toInstant().atZone(ZoneId.systemDefault()).toLocalDate(),
                    faker.date().future(365, TimeUnit.DAYS).toInstant().atZone(ZoneId.systemDefault()).toLocalDate(),
                    (long) i));
        }
        return sprints;
    }

    private List<Person> randomPeople(List<Person> people) {
        int random = faker.random().nextInt(1, 5);
        List<Person> people1 = new ArrayList<>();
        for (int i = 0; i < random; i++) {
            people1.add(people.get(i));
        }
        return people1;
    }

    private String generateUniqueProjectName(Set<String> usedProjectNames) {
        String projectName = faker.commerce().productName();
        while (usedProjectNames.contains(projectName)) {
            projectName = faker.commerce().productName();
        }
        return projectName;
    }

    public void createTask() {
        double[] storyPoints = {1, 2, 3, 5, 8, 13};
        for (int i = 0; i < 1500; i++) {
            double storyPoint = storyPoints[random.nextInt(storyPoints.length)];
            Priority priority = priorityRepository.getReferenceById(Long.valueOf(faker.random().nextInt(1, 3)));
            Long id_project=Long.valueOf(faker.random().nextInt(1, projects.size()));
            Project project = projectRepository.findProjectWithPeople(id_project).orElseThrow(() -> new NotFoundException("Project not found."));
            List<Person> listPerson = project.getPeople();
            Random random = new Random();
            Person personRandom = listPerson.get(random.nextInt(listPerson.size()));
            Sprint sprint = sprintRepository.getReferenceById(Long.valueOf(faker.random().nextInt(1, 4)));
            Status status = statusRepository.getReferenceById(Long.valueOf(faker.random().nextInt(1, 5)));
            Task task = Task.builder()
                    .title(generateTaskTitle(faker, 40))
                    .description(generateTaskDescription(faker, 255))
                    .story_points(storyPoint)
                    .start_date(faker.date().past(150, TimeUnit.DAYS).toInstant().atZone(ZoneId.systemDefault()).toLocalDate())
                    .end_date(faker.date().future(250, TimeUnit.DAYS).toInstant().atZone(ZoneId.systemDefault()).toLocalDate())
                    .hours_estimate(Long.valueOf(faker.random().nextInt(1, 100)))
                    .worked_hours(faker.number().numberBetween(1, 800))
                    .person(personRandom)
                    .priority(priority)
                    .project(project)
                    .sprint(sprint)
                    .status(status)
                    .build();
            taskRepository.save(task);
        }
    }

    public static String generateTaskTitle(Faker faker, int maxLength) {
        String title = faker.lorem().sentence();
        if (title.length() > maxLength) {
            title = title.substring(0, maxLength);
        }
        return title;
    }

    public static String generateTaskDescription(Faker faker, int maxLength) {
        String description = faker.lorem().paragraph();
        if (description.length() > maxLength) {
            description = description.substring(0, maxLength);
        }
        return description;
    }

    public void testSeed(Priority[] PRIORITYNAME, Status[] STATUSNAME, Month[] MONTHDATA, Role[] roleList) {
        //Create Priority
        for (Priority priority : PRIORITYNAME) {
            priorityRepository.findByPriority_name(priority.getPriority_name())
                    .orElseGet(() -> priorityRepository.save(priority));
        }

        //Create Status
        for (Status status : STATUSNAME) {
            statusRepository.findByStatus_name(status.getStatus_name())
                    .orElseGet(() -> statusRepository.save(status));
        }

        // Create Months
        monthRepository.saveAll(Arrays.asList(MONTHDATA));

        // Create Roles
        for (Role role : roleList) {
            roleRepository.findByRoleName(role.getRole_name())
                    .orElseGet(() -> roleRepository.save(new Role(role.getRole_name())));
        }
    }
}

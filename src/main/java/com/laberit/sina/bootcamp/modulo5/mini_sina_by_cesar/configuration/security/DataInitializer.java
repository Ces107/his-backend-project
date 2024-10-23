package com.laberit.sina.bootcamp.modulo5.mini_sina_by_cesar.configuration.security;

import com.laberit.sina.bootcamp.modulo5.mini_sina_by_cesar.model.*;
import com.laberit.sina.bootcamp.modulo5.mini_sina_by_cesar.model.enumerations.*;
import com.laberit.sina.bootcamp.modulo5.mini_sina_by_cesar.repository.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Component;

import java.util.*;

@Component
public class DataInitializer implements CommandLineRunner {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private PatientRepository patientRepository;

    @Autowired
    private DiagnosisRepository diagnosisRepository;

    @Autowired
    private AppointmentRepository appointmentRepository;

    @Override
    public void run(String... args) {

        BCryptPasswordEncoder passwordEncoder = new BCryptPasswordEncoder();

        // Crear roles y permisos
        Map<UserRole, Set<String>> rolePermissions = new HashMap<>();
        rolePermissions.put(UserRole.ADMIN, Set.of("MANAGE_USERS", "VIEW_PATIENTS", "VIEW_ANALYTICS"));
        rolePermissions.put(UserRole.DOCTOR, Set.of("MANAGE_DIAGNOSIS", "VIEW_PATIENTS", "MANAGE_APPOINTMENTS"));
        rolePermissions.put(UserRole.MANAGER, Set.of("VIEW_ANALYTICS"));
        rolePermissions.put(UserRole.PATIENT, Set.of("VIEW_OWN_APPOINTMENTS"));

        // Crear un administrador
        User adminUser = createUserIfNotExists("Admin", "User", "admin", passwordEncoder.encode("admin"), UserRole.ADMIN, rolePermissions.get(UserRole.ADMIN));

        // Crear 5 doctores
        List<User> doctors = new ArrayList<>();
        for (int i = 1; i <= 5; i++) {
            String username = "doctor" + i;
            User doctor = createUserIfNotExists("Doctor", "User" + i, username, passwordEncoder.encode("doctor" + i), UserRole.DOCTOR, rolePermissions.get(UserRole.DOCTOR));
            doctors.add(doctor);
        }

        // Crear 50 pacientes
        List<Patient> patients = new ArrayList<>();
        Random random = new Random();
        Gender[] genders = Gender.values();
        for (int i = 1; i <= 50; i++) {
            String username = "patient" + i;
            Gender gender = genders[random.nextInt(genders.length)];
            Date dateOfBirth = generateRandomDateOfBirth();
            // Asignar un doctor al azar
            User assignedDoctor = doctors.get(random.nextInt(doctors.size()));
            Patient patient = createPatientIfNotExists("Patient", "User" + i, username, passwordEncoder.encode("password" + i), gender, dateOfBirth, assignedDoctor);
            patients.add(patient);
        }

        // Crear diagnósticos para cada paciente
        Disease[] diseases = Disease.values();
        DiagnosisStatus[] diagnosisStatuses = DiagnosisStatus.values();
        for (Patient patient : patients) {
            Disease disease = diseases[random.nextInt(diseases.length)];
            DiagnosisStatus status = diagnosisStatuses[random.nextInt(diagnosisStatuses.length)];
            createDiagnosisIfNotExists(patient, disease, status);
        }

        // Crear citas para cada paciente
        AppointmentStatus[] appointmentStatuses = AppointmentStatus.values();
        for (Patient patient : patients) {
            // Asignar una fecha aleatoria en los próximos 30 días
            Date appointmentDate = generateRandomFutureDate();
            AppointmentStatus status = appointmentStatuses[random.nextInt(appointmentStatuses.length)];
            createAppointmentIfNotExists(patient, patient.getDoctor(), appointmentDate, status);
        }
    }

    private User createUserIfNotExists(String firstName, String lastName, String username, String encryptedPassword, UserRole role, Set<String> permissions) {
        if (!userRepository.existsByUsername(username)) {
            User user = new User(firstName, lastName, username, encryptedPassword, role, permissions);
            userRepository.save(user);
            System.out.println("User " + username + " created!");
            return user;
        } else {
            return userRepository.findByUsername(username).orElse(null);
        }
    }

    private Patient createPatientIfNotExists(String firstName, String lastName, String username, String encryptedPassword, Gender gender, Date dateOfBirth, User doctor) {
        if (!userRepository.existsByUsername(username)) {
            // Crear usuario para el paciente
            User patientUser = new User(firstName, lastName, username, encryptedPassword, UserRole.PATIENT, Set.of("VIEW_OWN_APPOINTMENTS"));
            userRepository.save(patientUser);

            // Crear paciente
            Patient patient = new Patient();
            patient.setFirstName(firstName + " " + username);
            patient.setLastName(lastName);
            patient.setDateOfBirth(dateOfBirth);
            patient.setGender(gender);
            patient.setDoctor(doctor);
            patient.setUser(patientUser);
            patientRepository.save(patient);

            System.out.println("Patient " + patient.getFirstName() + " created!");
            return patient;
        } else {
            User existingUser = userRepository.findByUsername(username).orElse(null);
            return patientRepository.findByUser(existingUser).orElse(null);
        }
    }

    private void createDiagnosisIfNotExists(Patient patient, Disease disease, DiagnosisStatus status) {
        // Verificar si ya existe un diagnóstico para el paciente y la enfermedad
        Optional<Diagnosis> existingDiagnosis = diagnosisRepository.findByPatientAndDisease(patient, disease);
        if (existingDiagnosis.isEmpty()) {
            Diagnosis diagnosis = new Diagnosis();
            diagnosis.setPatient(patient);
            diagnosis.setDisease(disease);
            diagnosis.setStatus(status);
            diagnosisRepository.save(diagnosis);
            System.out.println("Diagnosis for patient " + patient.getFirstName() + " created!");
        }
    }

    private void createAppointmentIfNotExists(Patient patient, User doctor, Date date, AppointmentStatus status) {
        // Verificar si ya existe una cita para el paciente en la misma fecha y con el mismo doctor
        Optional<Appointment> existingAppointment = appointmentRepository.findByPatientAndDoctorAndDate(patient, doctor, date);
        if (existingAppointment.isEmpty()) {
            Appointment appointment = new Appointment();
            appointment.setPatient(patient);
            appointment.setDoctor(doctor);
            appointment.setDate(date);
            appointment.setStatus(status);
            appointmentRepository.save(appointment);
            System.out.println("Appointment for patient " + patient.getFirstName() + " created!");
        }
    }

    private Date generateRandomDateOfBirth() {
        Random random = new Random();
        int minYear = 1950;
        int maxYear = 2010;
        int year = minYear + random.nextInt(maxYear - minYear + 1);
        int dayOfYear = 1 + random.nextInt(365);
        Calendar calendar = Calendar.getInstance();
        calendar.set(Calendar.YEAR, year);
        calendar.set(Calendar.DAY_OF_YEAR, dayOfYear);
        return calendar.getTime();
    }

    private Date generateRandomFutureDate() {
        Random random = new Random();
        int daysAhead = random.nextInt(30); // Próximos 30 días
        Calendar calendar = Calendar.getInstance();
        calendar.add(Calendar.DAY_OF_YEAR, daysAhead);
        return calendar.getTime();
    }
}

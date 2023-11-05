package hu.cubix.hr.Szilard.web;


import hu.cubix.hr.Szilard.dto.EmployeeDto;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.web.reactive.server.WebTestClient;

import java.time.LocalDateTime;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public class EmployeeControllerIT {

    private static final String BASE_URI = "/api/employees";

    @Autowired
    WebTestClient webTestClient;


    @Test
    void testThatNewValidEmployeeCanBeSaved() throws Exception {
        List<EmployeeDto> employeesBefore = getAllEmployees();

        EmployeeDto newEmployee = new EmployeeDto(0L, "ABC", "student", 200000, LocalDateTime.of(2019, 01, 01, 8, 0, 0));
        saveEmployee(newEmployee)
                .expectStatus()
                .isOk();

        List<EmployeeDto> employeesAfter = getAllEmployees();

        assertThat(employeesAfter.size()).isEqualTo(employeesBefore.size() + 1);
        assertThat(employeesAfter.get(employeesAfter.size()-1))
                .usingRecursiveComparison()
                .ignoringFields("id")
                .isEqualTo(newEmployee);
    }

    @Test
    void testThatNewInvalidEmployeeCannotBeSaved() throws Exception {
        List<EmployeeDto> employeesBefore = getAllEmployees();

        EmployeeDto newEmployee = newInvalidEmployee();
        saveEmployee(newEmployee)
                .expectStatus()
                .isBadRequest();

        List<EmployeeDto> employeesAfter = getAllEmployees();

        assertThat(employeesAfter).hasSameSizeAs(employeesBefore);
    }

    private EmployeeDto newInvalidEmployee() {
        return new EmployeeDto(0L, "", "student", 200000, LocalDateTime.of(2019, 01, 01, 8, 0, 0));
    }

    @Test
    void testThatEmployeeCanBeUpdatedWithValidFields() throws Exception {

        EmployeeDto newEmployee = new EmployeeDto(0L, "ABC", "student", 200000, LocalDateTime.of(2019, 01, 01, 8, 0, 0));
        EmployeeDto savedEmployee = saveEmployee(newEmployee)
                .expectStatus().isOk()
                .expectBody(EmployeeDto.class).returnResult().getResponseBody();

        List<EmployeeDto> employeesBefore = getAllEmployees();
        savedEmployee.setName("modified");
        modifyEmployee(savedEmployee).expectStatus().isOk();

        List<EmployeeDto> employeesAfter = getAllEmployees();

        assertThat(employeesAfter).hasSameSizeAs(employeesBefore);
        assertThat(employeesAfter.get(employeesAfter.size()-1))
                .usingRecursiveComparison()
                .isEqualTo(savedEmployee);
    }

    @Test
    void testThatEmployeeCannotBeUpdatedWithInvalidFields() throws Exception {
        EmployeeDto newEmployee = new EmployeeDto(0L, "ABC", "student", 200000, LocalDateTime.of(2019, 01, 01, 8, 0, 0));
        EmployeeDto savedEmployee = saveEmployee(newEmployee)
                .expectStatus().isOk()
                .expectBody(EmployeeDto.class)
                .returnResult()
                .getResponseBody();

        List<EmployeeDto> employeesBefore = getAllEmployees();
        EmployeeDto invalidEmployee = newInvalidEmployee();
        invalidEmployee.setId(savedEmployee.getId());
        modifyEmployee(invalidEmployee).expectStatus().isBadRequest();

        List<EmployeeDto> employeesAfter = getAllEmployees();

        assertThat(employeesAfter).hasSameSizeAs(employeesBefore);
        assertThat(employeesAfter.get(employeesAfter.size()-1))
                .usingRecursiveComparison()
                .isEqualTo(savedEmployee);
    }

    private WebTestClient.ResponseSpec modifyEmployee(EmployeeDto newEmployee) {
        String path = BASE_URI + "/" + newEmployee.getId();
        return webTestClient
                .put()
                .uri(path)
                .bodyValue(newEmployee)
                .exchange();
    }

    private WebTestClient.ResponseSpec saveEmployee(EmployeeDto newEmployee) {
        return webTestClient
                .post()
                .uri(BASE_URI)
                .bodyValue(newEmployee)
                .exchange();
    }

    private List<EmployeeDto> getAllEmployees() {
        List<EmployeeDto> responseList = webTestClient
                .get()
                .uri(BASE_URI)
                .exchange()
                .expectStatus()
                .isOk()
                .expectBodyList(EmployeeDto.class)
                .returnResult()
                .getResponseBody();
        Collections.sort(responseList, Comparator.comparing(EmployeeDto::getId));
        return responseList;
    }
}

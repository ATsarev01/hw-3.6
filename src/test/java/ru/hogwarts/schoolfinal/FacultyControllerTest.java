package ru.hogwarts.schoolfinal;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.boot.test.mock.mockito.SpyBean;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.mock.web.MockHttpServletRequest;
import org.springframework.mock.web.MockHttpServletResponse;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;
import ru.hogwarts.schoolfinal.controller.FacultyController;
import ru.hogwarts.schoolfinal.entity.Faculty;
import ru.hogwarts.schoolfinal.repository.FacultyRepository;
import ru.hogwarts.schoolfinal.repository.StudentRepository;
import ru.hogwarts.schoolfinal.service.FacultyService;

import java.nio.charset.StandardCharsets;
import java.util.Optional;

import static org.assertj.core.api.AssertionsForInterfaceTypes.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;
@WebMvcTest(controllers = FacultyController.class)
public class FacultyControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @MockBean
    private FacultyRepository facultyRepository;

    @MockBean
    private StudentRepository studentRepository;

    @SpyBean
    private FacultyService facultyService;

    @Test
    public void createTest() throws Exception{
        Faculty faculty = new Faculty();
        faculty.setId(1L);
        faculty.setColor("Красный");
        faculty.setName("Гриффиндор");

        when(facultyRepository.save(any())).thenReturn(faculty);

        mockMvc.perform(
                MockMvcRequestBuilders.post("/faculty")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(faculty))
        ).andExpect(result -> {
            MockHttpServletResponse mockHttpServletResponse = result.getResponse();
            Faculty facultyRecordResult = objectMapper.readValue(
                    mockHttpServletResponse.getContentAsString(StandardCharsets.UTF_8),
                    Faculty.class
            );
            assertThat(mockHttpServletResponse.getStatus()).isEqualTo(HttpStatus.OK.value());
            assertThat(facultyRecordResult).usingRecursiveComparison().ignoringFields("id")
                    .isEqualTo(faculty);
            assertThat(facultyRecordResult.getId()).isEqualTo(faculty.getId());
        });

    }
    @Test
    public void add() throws Exception {
        Faculty faculty = new Faculty();
        faculty.setName("Test Faculty");
        faculty.setColor("Red");

        ResultActions resultActions =  mockMvc.perform(MockMvcRequestBuilders.post("/faculty")
                .content("{\"name\": \"Test Faculty\", \"color\": \"Red\"}")
                .contentType(MediaType.APPLICATION_JSON));
        resultActions.andExpect(MockMvcResultMatchers.status().isOk());
    }
    @Test
    public void updateTest() throws Exception {
        Faculty faculty = new Faculty();
        faculty.setId(1L);
        faculty.setColor("Желтый");
        faculty.setName("Хаффлпафф");

        when(facultyRepository.findById(any())).thenReturn(Optional.of(faculty));
        when(facultyRepository.save(any())).thenReturn(faculty);

        mockMvc.perform(
                MockMvcRequestBuilders.put("/faculty/{id}", 1L)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(faculty))
        ).andExpect(result -> {
            MockHttpServletResponse mockHttpServletResponse = result.getResponse();
            Faculty facultyRecordResult = objectMapper.readValue(
                    mockHttpServletResponse.getContentAsString(StandardCharsets.UTF_8),
                    Faculty.class
            );
            assertThat(mockHttpServletResponse.getStatus()).isEqualTo(HttpStatus.OK.value());
            assertThat(facultyRecordResult).usingRecursiveComparison()
                    .isEqualTo(faculty);
        });
    }

    @Test
    public void getAllByNameOrColor() throws Exception {
        ResultActions resultActions = mockMvc.perform(MockMvcRequestBuilders.get("/faculty")
                .param("nameOrColor", "Test")
                .contentType(MediaType.APPLICATION_JSON));
        resultActions.andExpect(MockMvcResultMatchers.status().isOk());
    }

    @Test
    public void getStudentByFacultyId() throws Exception {
        ResultActions resultActions = mockMvc.perform(MockMvcRequestBuilders.get("/faculty/1/students")
                .contentType(MediaType.APPLICATION_JSON));
        resultActions.andExpect(MockMvcResultMatchers.status().isOk());
    }




}

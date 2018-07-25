package ua.slavik.carwash.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.hamcrest.Matchers;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.RequestBuilder;
import ua.slavik.carwash.dto.jobItem.CreateJobItemDTO;
import ua.slavik.carwash.model.Job;
import ua.slavik.carwash.model.JobStatus;
import ua.slavik.carwash.repository.JobRepository;
import java.util.Date;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@RunWith(SpringRunner.class)
@SpringBootTest
@AutoConfigureMockMvc
@DirtiesContext(classMode = DirtiesContext.ClassMode.AFTER_EACH_TEST_METHOD)
public class JobItemValidatorTest {
    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private JobRepository jobRepository;

    @Autowired
    private ObjectMapper objectMapper;

    @Test
    public void jobItemValidatorTooShortTest() throws Exception {
        Job mockJob = Job.builder()
                .startDate(new Date(1532460764L))
                .endDate(new Date(1532460766L))
                .status(JobStatus.NOT_STARTED)
                .id(1L)
                .build();
        jobRepository.save(mockJob);

        CreateJobItemDTO mockJobItemDTO = CreateJobItemDTO.builder()
                .name("w")
                .description("")
                .price(-500)
                .duration(-10)
                .priority(-1)
                .status(JobStatus.IN_PROGRESS)
                .repeatable(false)
                .jobId(1L)
                .build();

        String mockJobItemDTOJSON = objectMapper.writeValueAsString(mockJobItemDTO);

        RequestBuilder requestBuilder = post("/jobitem/")
                .contentType(MediaType.APPLICATION_JSON_UTF8)
                .content(mockJobItemDTOJSON);

        mockMvc.perform(requestBuilder)
                .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.errors").value(Matchers.containsInAnyOrder(
                        "Invalid description.",
                        "Invalid price.",
                        "Invalid priority.",
                        "Invalid duration."
                )));
    }

    @Test
    public void jobItemValidatorNullTest() throws Exception {
        Job mockJob = Job.builder()
                .startDate(new Date(1532460764L))
                .endDate(new Date(1532460766L))
                .status(JobStatus.NOT_STARTED)
                .id(1L)
                .build();
        jobRepository.save(mockJob);

        CreateJobItemDTO mockJobItemDTO = CreateJobItemDTO.builder()
                .name(null)
                .description(null)
                .price(-500)
                .duration(-10)
                .priority(-1)
                .status(JobStatus.IN_PROGRESS)
                .repeatable(false)
                .jobId(1L)
                .build();

        String mockJobItemDTOJSON = objectMapper.writeValueAsString(mockJobItemDTO);

        RequestBuilder requestBuilder = post("/jobitem/")
                .contentType(MediaType.APPLICATION_JSON_UTF8)
                .content(mockJobItemDTOJSON);

        mockMvc.perform(requestBuilder)
                .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.errors").value(Matchers.containsInAnyOrder(
                        "Invalid description.",
                        "Invalid price.",
                        "Invalid priority.",
                        "Invalid duration."
                )));
    }
}
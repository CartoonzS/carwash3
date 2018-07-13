package ua.slavik.carwash.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.SpyBean;
import org.springframework.http.MediaType;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.RequestBuilder;
import ua.slavik.carwash.dto.job.CreateJobDTO;
import ua.slavik.carwash.dto.job.UpdateJobDTO;
import ua.slavik.carwash.dto.jobItem.UpdateJobItemDTO;
import ua.slavik.carwash.model.Job;
import ua.slavik.carwash.model.JobItem;
import ua.slavik.carwash.model.JobStatus;
import ua.slavik.carwash.repository.JobItemRepository;
import ua.slavik.carwash.repository.JobRepository;
import ua.slavik.carwash.service.Impl.JobServiceImpl;
import java.util.Date;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@RunWith(SpringRunner.class)
@SpringBootTest
@AutoConfigureMockMvc
@DirtiesContext(classMode = DirtiesContext.ClassMode.AFTER_EACH_TEST_METHOD)
public class JobControllerTest {
    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private JobRepository jobRepository;

    @Autowired
    private JobItemRepository jobItemRepository;

    @Autowired
    private ObjectMapper objectMapper;

    @SpyBean
    private JobServiceImpl jobServiceMock;

    @Test
    public void getJob() throws Exception {
        Job mockJob = Job.builder()
                .startDate(new Date(1531282957L))
                .endDate(new Date(1531282992L))
                .status(JobStatus.IN_PROGRESS)
                .id(1L)
                .build();

        Mockito.when(
                jobServiceMock.getJobById(1L)
        ).thenReturn(mockJob);

        RequestBuilder requestBuilder = get("/job/{id}", 1L);

        mockMvc.perform(requestBuilder)
                .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(mockJob.getId()))
                .andExpect(jsonPath("$.status").value(mockJob.getStatus().toString()));
    }

    @Test
    public void postJob() throws Exception {
        CreateJobDTO mockJobDTO = CreateJobDTO.builder()
                .startDate(new Date(1531282957L))
                .endDate(new Date(1531282992L))
                .status(JobStatus.IN_PROGRESS)
                .build();

        String mockJobDTOJSON = objectMapper.writeValueAsString(mockJobDTO);

        RequestBuilder requestBuilder = post("/job/")
                .contentType(MediaType.APPLICATION_JSON_UTF8)
                .content(mockJobDTOJSON);

        mockMvc.perform(requestBuilder)
                .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.id").value(1))
                .andExpect(jsonPath("$.status").value(mockJobDTO.getStatus().toString()));

    }

    @Test
    public void updateJob() throws Exception {
        Job mockJob = Job.builder()
                .startDate(new Date(1531282957L))
                .endDate(new Date(1531282992L))
                .status(JobStatus.IN_PROGRESS)
                .build();
        jobRepository.save(mockJob);

        UpdateJobDTO jobUpdate = UpdateJobDTO.builder()
                .startDate(new Date(1531358161L))
                .endDate(new Date(1531358169L))
                .status(JobStatus.COMPLETED)
                .id(1L)
                .build();

        RequestBuilder requestBuilder = put("/job/")
                .contentType(MediaType.APPLICATION_JSON_UTF8)
                .content(objectMapper.writeValueAsString(jobUpdate));

        mockMvc.perform(requestBuilder)
                .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(1))
                .andExpect(jsonPath("$.status").value(jobUpdate.getStatus().toString()));
    }

    @Test
    public void deleteJob() throws Exception {
        Job mockJob = Job.builder()
                .startDate(new Date(1531282957L))
                .endDate(new Date(1531282992L))
                .status(JobStatus.IN_PROGRESS)
                .build();
        jobRepository.save(mockJob);

        RequestBuilder requestBuilder = delete("/job/{id}", 1L);

        mockMvc.perform(requestBuilder)
                .andExpect(content().string("deleted"))
                .andExpect(status().isOk());
    }

    @Test
    public void addJobItemToJob() throws Exception {
        Job mockJob = Job.builder()
                .startDate(new Date(1531282957L))
                .endDate(new Date(1531282992L))
                .status(JobStatus.IN_PROGRESS)
                .id(1L)
                .build();
        jobRepository.save(mockJob);

        JobItem mockJobItem = JobItem.builder()
                .description("Car wash")
                .name("Car wash")
                .price(600)
                .priority(1)
                .repeatable(false)
                .status(JobStatus.IN_PROGRESS)
                .id(1L)
                .build();
        jobItemRepository.save(mockJobItem);

        UpdateJobItemDTO jobItemUpdate = UpdateJobItemDTO.builder()
                .id(1L)
                .description("Car windows wash")
                .name("Window wash")
                .price(300)
                .priority(2)
                .repeatable(true)
                .status(JobStatus.IN_PROGRESS)
                .jobId(1L)
                .build();

        RequestBuilder requestBuilder = put("/job/{jobId}/jobItem/{jobItemId}", 1L, 1L);

        mockMvc.perform(requestBuilder)
                .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(1))
                .andExpect(jsonPath("$.status").value(jobItemUpdate.getStatus().toString()));
    }
}


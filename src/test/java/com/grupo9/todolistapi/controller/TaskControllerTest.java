package com.grupo9.todolistapi.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.grupo9.todolistapi.model.Task;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import static org.hamcrest.Matchers.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest(classes = com.grupo9.todo_list_api.TodoListApiApplication.class)
@AutoConfigureMockMvc
public class TaskControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @Test
    public void contextLoads() {
    }

    @Test
    public void testCrudOperations() throws Exception {
        // 1. Get all tasks initially - should be empty (or check if it returns 200 OK)
        mockMvc.perform(get("/api/tasks"))
                .andExpect(status().isOk());

        // 2. Create a new task
        Task task = new Task(null, "Test Task", false);
        String taskJson = objectMapper.writeValueAsString(task);

        String response = mockMvc.perform(post("/api/tasks")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(taskJson))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.id", notNullValue()))
                .andExpect(jsonPath("$.title", is("Test Task")))
                .andExpect(jsonPath("$.completed", is(false)))
                .andReturn().getResponse().getContentAsString();

        // Parse generated ID from response
        Task createdTask = objectMapper.readValue(response, Task.class);
        Long createdId = createdTask.getId();

        // 3. Get task by ID
        mockMvc.perform(get("/api/tasks/" + createdId))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id", is(createdId.intValue())))
                .andExpect(jsonPath("$.title", is("Test Task")));

        // 4. Update the task
        Task updatedTask = new Task(null, "Updated Task", true);
        String updatedJson = objectMapper.writeValueAsString(updatedTask);

        mockMvc.perform(put("/api/tasks/" + createdId)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(updatedJson))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id", is(createdId.intValue())))
                .andExpect(jsonPath("$.title", is("Updated Task")))
                .andExpect(jsonPath("$.completed", is(true)));

        // 5. Delete the task
        mockMvc.perform(delete("/api/tasks/" + createdId))
                .andExpect(status().isNoContent());

        // 6. Get deleted task by ID - should return 404
        mockMvc.perform(get("/api/tasks/" + createdId))
                .andExpect(status().isNotFound());
    }
}

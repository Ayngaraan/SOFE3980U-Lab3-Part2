package com.ontariotechu.sofe3980U;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@RunWith(SpringRunner.class)
@WebMvcTest(BinaryAPIController.class)
public class BinaryAPIControllerTest {

    @Autowired
    private MockMvc mvc;

    @Test
    public void add() throws Exception {
        this.mvc.perform(get("/add").param("operand1", "111").param("operand2", "1010"))
            .andExpect(status().isOk())
            .andExpect(content().string("10001"));
    }

    @Test
    public void addJSON() throws Exception {
        this.mvc.perform(get("/add_json").param("operand1", "111").param("operand2", "1010"))
            .andExpect(status().isOk())
            .andExpect(jsonPath("$.operand1").value("111"))
            .andExpect(jsonPath("$.operand2").value("1010"))
            .andExpect(jsonPath("$.result").value("10001"))
            .andExpect(jsonPath("$.operator").value("add"));
    }

    // --- REQUIRED NEW TEST CASES ---

    @Test
    public void multiply() throws Exception {
        this.mvc.perform(get("/multiply").param("operand1", "11").param("operand2", "101")) // 3 * 5 = 15 (1111)
            .andExpect(status().isOk())
            .andExpect(content().string("1111"));
    }

    @Test
    public void andJSON() throws Exception {
        this.mvc.perform(get("/and_json").param("operand1", "111").param("operand2", "101")) // 111 & 101 = 101
            .andExpect(status().isOk())
            .andExpect(jsonPath("$.operand1").value("111"))
            .andExpect(jsonPath("$.operand2").value("101"))
            .andExpect(jsonPath("$.result").value("101"))
            .andExpect(jsonPath("$.operator").value("and"));
    }

    @Test
    public void orJSON() throws Exception {
        this.mvc.perform(get("/or_json").param("operand1", "111").param("operand2", "101")) // 111 | 101 = 111
            .andExpect(status().isOk())
            .andExpect(jsonPath("$.operand1").value("111"))
            .andExpect(jsonPath("$.operand2").value("101"))
            .andExpect(jsonPath("$.result").value("111"))
            .andExpect(jsonPath("$.operator").value("or"));
    }
}
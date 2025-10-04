package com.davi.gestaoescolar.gestao_escolar;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.web.servlet.MockMvc;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;   // ✅ importa o get()
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status; // ✅ importa o status()

@SpringBootTest
@AutoConfigureMockMvc
class EndpointSmokeTest {

    @Autowired
    private MockMvc mockMvc;

    @Test
    void allEndpointsShouldReturn200() throws Exception {
        String[] endpoints = {
                "/api/alunos"
                // "/api/professores",
                // "/api/cursos"
        };

        for (String endpoint : endpoints) {
            String response = mockMvc.perform(get(endpoint))
                    .andExpect(status().isOk())
                    .andReturn()
                    .getResponse()
                    .getContentAsString();

            System.out.println("Response from " + endpoint + ": " + response);
        }
    }
}

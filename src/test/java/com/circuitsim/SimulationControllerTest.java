package com.circuitsim;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
class SimulationControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Test
    void shouldSimulateHalfAdderThroughApi() throws Exception {
        String requestBody = """
            {
              "inputs": [
                {
                  "name": "A",
                  "value": true
                },
                {
                  "name": "B",
                  "value": false
                }
              ],
              "gates": [
                {
                  "name": "SUM",
                  "type": "XOR"
                },
                {
                  "name": "CARRY",
                  "type": "AND"
                }
              ],
              "connections": [
                {
                  "source": "A",
                  "destination": "SUM",
                  "inputIndex": 0
                },
                {
                  "source": "B",
                  "destination": "SUM",
                  "inputIndex": 1
                },
                {
                  "source": "A",
                  "destination": "CARRY",
                  "inputIndex": 0
                },
                {
                  "source": "B",
                  "destination": "CARRY",
                  "inputIndex": 1
                }
              ],
              "outputs": [
                "SUM",
                "CARRY"
              ]
            }
            """;

        mockMvc.perform(
                post("/api/simulate")
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(requestBody)
            )
            .andExpect(status().isOk())
            .andExpect(jsonPath("$.outputs.SUM").value(true))
            .andExpect(jsonPath("$.outputs.CARRY").value(false));
    }
}
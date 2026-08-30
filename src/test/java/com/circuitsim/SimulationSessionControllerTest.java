package com.circuitsim;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
public class SimulationSessionControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @Test
    void preservesFlipFlopStateAcrossRequests() throws Exception {

        String createRequest = """
            {
              "inputs": [
                {
                  "name": "D",
                  "value": false
                },
                {
                  "name": "CLK",
                  "value": false
                }
              ],
              "gates": [
                {
                  "name": "DFF1",
                  "type": "DFF"
                }
              ],
              "connections": [
                {
                  "source": "D",
                  "destination": "DFF1",
                  "inputIndex": 0
                },
                {
                  "source": "CLK",
                  "destination": "DFF1",
                  "inputIndex": 1
                }
              ],
              "outputs": [
                "DFF1"
              ]
            }
            """;

        String createResponse =
            mockMvc.perform(
                    post("/api/simulation-sessions")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(createRequest)
                )
                .andExpect(status().isOk())
                .andReturn()
                .getResponse()
                .getContentAsString();

        JsonNode createJson =
            objectMapper.readTree(createResponse);

        String sessionId =
            createJson.get("sessionId").asText();

        // Initial state: D=0, CLK=0
        boolean initialQ =
            simulate(
                sessionId,
                false,
                false
            );

        assertFalse(initialQ);

        // D becomes 1 while clock is still low.
        // Q should remain 0.
        boolean beforeEdgeQ =
            simulate(
                sessionId,
                true,
                false
            );

        assertFalse(beforeEdgeQ);

        // Rising edge: CLK 0 -> 1.
        // Q should capture D=1.
        boolean risingEdgeQ =
            simulate(
                sessionId,
                true,
                true
            );

        assertTrue(risingEdgeQ);

        // D changes back to 0,
        // but CLK remains high.
        // Q should remain 1.
        boolean clockStillHighQ =
            simulate(
                sessionId,
                false,
                true
            );

        assertTrue(clockStillHighQ);

        // Falling edge.
        // Q should still remain 1.
        boolean fallingEdgeQ =
            simulate(
                sessionId,
                false,
                false
            );

        assertTrue(fallingEdgeQ);

        // Next rising edge with D=0.
        // Q should capture 0.
        boolean secondRisingEdgeQ =
            simulate(
                sessionId,
                false,
                true
            );

        assertFalse(secondRisingEdgeQ);
    }

    private boolean simulate(
        String sessionId,
        boolean d,
        boolean clock
    ) throws Exception {

        String request = """
            {
              "inputs": [
                {
                  "name": "D",
                  "value": %s
                },
                {
                  "name": "CLK",
                  "value": %s
                }
              ],
              "outputs": [
                "DFF1"
              ]
            }
            """.formatted(d, clock);

        String response =
            mockMvc.perform(
                    post(
                        "/api/simulation-sessions/"
                        + sessionId
                        + "/simulate"
                    )
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(request)
                )
                .andExpect(status().isOk())
                .andReturn()
                .getResponse()
                .getContentAsString();

        JsonNode json =
            objectMapper.readTree(response);

        return json
            .get("outputs")
            .get("DFF1")
            .asBoolean();
    }
}
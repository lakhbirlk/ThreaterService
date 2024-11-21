package show;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.http.MediaType;
import org.theatremanagement.show.controller.ShowController;
import org.theatremanagement.show.model.Show;
import org.theatremanagement.show.service.ShowService;

import java.util.List;
import java.util.Collections;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@ExtendWith(MockitoExtension.class)
public class ShowControllerTest {

    @Mock
    private ShowService showService;

    @InjectMocks
    private ShowController showController;

    private MockMvc mockMvc;
    private ObjectMapper objectMapper;
    private Show show;

    @BeforeEach
    public void setUp() {
        mockMvc = MockMvcBuilders.standaloneSetup(showController).build();
        objectMapper = new ObjectMapper();

        // Set up a sample show object
        show = new Show();
        show.setId(1L);
        show.setName("Movie Show");
        show.setDurationDays(120);
        show.setScreenId(1L);
    }

    @Test
    public void testAddShow() throws Exception {
        when(showService.addShow(eq(1L), any(Show.class))).thenReturn(show);

        ResultActions result = mockMvc.perform(post("/api/v1/screens/{screenId}/shows", 1L)
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(show)));

        result.andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(1))
                .andExpect(jsonPath("$.name").value("Movie Show"))
                .andExpect(jsonPath("$.duration").value(120));

        verify(showService, times(1)).addShow(eq(1L), any(Show.class));
    }

    @Test
    public void testGetShowsByScreen() throws Exception {
        when(showService.getShowsByScreen(1L)).thenReturn(List.of(show));

        ResultActions result = mockMvc.perform(get("/api/v1/screens/{screenId}/shows", 1L));

        result.andExpect(status().isOk())
                .andExpect(jsonPath("$[0].id").value(1))
                .andExpect(jsonPath("$[0].name").value("Movie Show"))
                .andExpect(jsonPath("$[0].duration").value(120));

        verify(showService, times(1)).getShowsByScreen(1L);
    }

    @Test
    public void testGetShow() throws Exception {
        when(showService.getShow(1L)).thenReturn(show);

        ResultActions result = mockMvc.perform(get("/api/v1/shows/{showId}", 1L));

        result.andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(1))
                .andExpect(jsonPath("$.name").value("Movie Show"))
                .andExpect(jsonPath("$.duration").value(120));

        verify(showService, times(1)).getShow(1L);
    }

    @Test
    public void testUpdateShow() throws Exception {
        when(showService.updateShow(eq(1L), any(Show.class))).thenReturn(show);

        show.setName("Updated Show");

        ResultActions result = mockMvc.perform(put("/api/v1/shows/{showId}", 1L)
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(show)));

        result.andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(1))
                .andExpect(jsonPath("$.name").value("Updated Show"))
                .andExpect(jsonPath("$.duration").value(120));

        verify(showService, times(1)).updateShow(eq(1L), any(Show.class));
    }

    @Test
    public void testDeleteShow() throws Exception {
        doNothing().when(showService).deleteShow(1L);

        ResultActions result = mockMvc.perform(delete("/api/v1/shows/{showId}", 1L));

        result.andExpect(status().isOk());

        verify(showService, times(1)).deleteShow(1L);
    }
}

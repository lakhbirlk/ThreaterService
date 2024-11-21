package screen;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.theatremanagement.screen.controller.ScreenController;
import org.theatremanagement.screen.model.Screen;
import org.theatremanagement.screen.service.ScreenService;
import org.springframework.test.web.servlet.ResultActions;

import java.util.Optional;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@ExtendWith(MockitoExtension.class)
public class ScreenControllerTest {

    @Mock
    private ScreenService screenService;

    @InjectMocks
    private ScreenController screenController;

    private MockMvc mockMvc;
    private ObjectMapper objectMapper;
    private Screen screen;

    @BeforeEach
    public void setUp() {
        mockMvc = MockMvcBuilders.standaloneSetup(screenController).build();
        objectMapper = new ObjectMapper();

        screen = new Screen();
        screen.setId(1L);
        screen.setName("Screen 1");
        screen.setSeatingCapacity(100);
    }

    @Test
    public void testAddScreen() throws Exception {
        when(screenService.addscreen(any(Screen.class))).thenReturn(screen);

        ResultActions result = mockMvc.perform(post("/api/v1/screens")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(screen)));

        result.andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(1))
                .andExpect(jsonPath("$.name").value("Screen 1"))
                .andExpect(jsonPath("$.seatingCapacity").value(100));

        verify(screenService, times(1)).addscreen(any(Screen.class));
    }

    @Test
    public void testGetScreen() throws Exception {
        when(screenService.getscreen(1L)).thenReturn(screen);

        ResultActions result = mockMvc.perform(get("/api/v1/screens/{screenId}", 1L));

        result.andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(1))
                .andExpect(jsonPath("$.name").value("Screen 1"))
                .andExpect(jsonPath("$.seatingCapacity").value(100));

        verify(screenService, times(1)).getscreen(1L);
    }

    @Test
    public void testGetScreenNotFound() throws Exception {
        when(screenService.getscreen(1L)).thenThrow(new RuntimeException("screen not found"));

        ResultActions result = mockMvc.perform(get("/api/v1/screens/{screenId}", 1L));

        result.andExpect(status().isNotFound())
                .andExpect(content().string("screen not found"));

        verify(screenService, times(1)).getscreen(1L);
    }

    @Test
    public void testUpdateScreen() throws Exception {
        when(screenService.updatescreen(eq(1L), any(Screen.class))).thenReturn(screen);

        screen.setName("Updated Screen");

        ResultActions result = mockMvc.perform(put("/api/v1/screens/{screenId}", 1L)
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(screen)));

        result.andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(1))
                .andExpect(jsonPath("$.name").value("Updated Screen"))
                .andExpect(jsonPath("$.seatingCapacity").value(100));

        verify(screenService, times(1)).updatescreen(eq(1L), any(Screen.class));
    }

    @Test
    public void testDeleteScreen() throws Exception {
        doNothing().when(screenService).deleteScreen(1L);

        ResultActions result = mockMvc.perform(delete("/api/v1/screens/{screenId}", 1L));

        result.andExpect(status().isOk());

        verify(screenService, times(1)).deleteScreen(1L);
    }

    @Test
    public void testSearchByName() throws Exception {
        when(screenService.searchByName("Screen")).thenReturn(java.util.List.of(screen));

        ResultActions result = mockMvc.perform(get("/api/v1/screens/search")
                .param("name", "Screen"));

        result.andExpect(status().isOk())
                .andExpect(jsonPath("$[0].id").value(1))
                .andExpect(jsonPath("$[0].name").value("Screen 1"))
                .andExpect(jsonPath("$[0].seatingCapacity").value(100));

        verify(screenService, times(1)).searchByName("Screen");
    }
}

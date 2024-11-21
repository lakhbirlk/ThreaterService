package theatre;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.theatremanagement.theatre.model.Theatre;
import org.theatremanagement.theatre.controller.TheatreController;
import org.theatremanagement.theatre.service.TheatreService;

import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import java.util.Arrays;

@ExtendWith(MockitoExtension.class)
public class TheatreControllerTest {

    @Mock
    private TheatreService theatreService;

    @InjectMocks
    private TheatreController theatreController;

    private MockMvc mockMvc;

    private Theatre theatre;

    @BeforeEach
    public void setUp() {
        mockMvc = MockMvcBuilders.standaloneSetup(theatreController).build();

        theatre = new Theatre();
        theatre.setId(1L);
        theatre.setName("Royal Theatre");
        theatre.setLocation("New York");
    }

    @Test
    public void testCreateTheatre() throws Exception {
        when(theatreService.addTheatre(any(Theatre.class))).thenReturn(theatre);

        mockMvc.perform(post("/api/v1/theatres")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{\"name\":\"Royal Theatre\",\"location\":\"New York\"}"))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.name").value("Royal Theatre"))
                .andExpect(jsonPath("$.location").value("New York"));

        verify(theatreService, times(1)).addTheatre(any(Theatre.class));
    }

    @Test
    public void testGetAllTheatres() throws Exception {
        when(theatreService.getAllTheatres()).thenReturn(Arrays.asList(theatre));

        mockMvc.perform(get("/api/v1/theatres"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].name").value("Royal Theatre"))
                .andExpect(jsonPath("$[0].location").value("New York"));

        verify(theatreService, times(1)).getAllTheatres();
    }

    @Test
    public void testGetTheatre() throws Exception {
        when(theatreService.getTheatre(1L)).thenReturn(theatre);

        mockMvc.perform(get("/api/v1/theatres/1"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.name").value("Royal Theatre"))
                .andExpect(jsonPath("$.location").value("New York"));

        verify(theatreService, times(1)).getTheatre(1L);
    }

    @Test
    public void testUpdateTheatre() throws Exception {
        Theatre updatedTheatre = new Theatre();
        updatedTheatre.setId(1L);
        updatedTheatre.setName("Updated Royal Theatre");
        updatedTheatre.setLocation("Los Angeles");

        when(theatreService.updateTheatre(1L, updatedTheatre)).thenReturn(updatedTheatre);

        mockMvc.perform(put("/api/v1/theatres/1")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{\"name\":\"Updated Royal Theatre\",\"location\":\"Los Angeles\"}"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.name").value("Updated Royal Theatre"))
                .andExpect(jsonPath("$.location").value("Los Angeles"));

        verify(theatreService, times(1)).updateTheatre(1L, updatedTheatre);
    }

    @Test
    public void testDeleteTheatre() throws Exception {
        doNothing().when(theatreService).deleteTheatre(1L);

        mockMvc.perform(delete("/api/v1/theatres/1"))
                .andExpect(status().isNoContent());

        verify(theatreService, times(1)).deleteTheatre(1L);
    }

    @Test
    public void testSearchTheatres() throws Exception {
        when(theatreService.searchByName("Royal")).thenReturn(Arrays.asList(theatre));

        mockMvc.perform(get("/api/v1/theatres/search?name=Royal"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].name").value("Royal Theatre"))
                .andExpect(jsonPath("$[0].location").value("New York"));

        verify(theatreService, times(1)).searchByName("Royal");
    }

    @Test
    public void testSearchByLocation() throws Exception {
        when(theatreService.searchByLocation("New York")).thenReturn(Arrays.asList(theatre));

        mockMvc.perform(get("/api/v1/theatres/search/location?location=New York"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].name").value("Royal Theatre"))
                .andExpect(jsonPath("$[0].location").value("New York"));

        verify(theatreService, times(1)).searchByLocation("New York");
    }
}

package ru.practicum.shareit.item;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;
import ru.practicum.shareit.item.dto.CommentDto;
import ru.practicum.shareit.item.dto.ItemDto;
import ru.practicum.shareit.item.dto.ItemDtoResponse;
import ru.practicum.shareit.item.service.ItemService;

import java.util.Collections;

import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(ItemController.class)
public class ItemControllerTest {
    @Autowired
    private MockMvc mockMvc;

    @MockitoBean
    private ItemService itemService;

    private final ObjectMapper objectMapper = new ObjectMapper();


    @Test
    void createItem_ShouldReturnItemDto() throws Exception {
        ItemDto itemDto = new ItemDto();
        itemDto.setName("Item");
        itemDto.setDescription("Description");
        itemDto.setAvailable(true);

        when(itemService.createItem(anyLong(), any(ItemDto.class))).thenReturn(itemDto);

        mockMvc.perform(post("/items")
                        .header("X-Sharer-User-Id", 1L)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(itemDto)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.name").value("Item"));
    }

    @Test
    void addComment_ShouldReturnCommentDto() throws Exception {
        CommentDto commentDto = new CommentDto();
        commentDto.setText("Comment");

        when(itemService.addComment(anyLong(), anyLong(), any(CommentDto.class))).thenReturn(commentDto);

        mockMvc.perform(post("/items/{itemId}/comment", 1L)
                        .header("X-Sharer-User-Id", 1L)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(commentDto)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.text").value("Comment"));
    }

    @Test
    void getItem_ShouldReturnItemDtoResponse() throws Exception {
        ItemDtoResponse itemDtoResponse = new ItemDtoResponse();
        itemDtoResponse.setId(1L);
        itemDtoResponse.setName("Item");

        when(itemService.getItem(anyLong(), anyLong())).thenReturn(itemDtoResponse);

        mockMvc.perform(get("/items/{itemId}", 1L)
                        .header("X-Sharer-User-Id", 1L))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.name").value("Item"));
    }

    @Test
    void getUserItems_ShouldReturnListOfItemDtoResponse() throws Exception {
        ItemDtoResponse itemDtoResponse = new ItemDtoResponse();
        itemDtoResponse.setId(1L);
        itemDtoResponse.setName("Item");

        when(itemService.getUserItems(anyLong())).thenReturn(Collections.singletonList(itemDtoResponse));

        mockMvc.perform(get("/items")
                        .header("X-Sharer-User-Id", 1L))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].name").value("Item"));
    }

    @Test
    void getItemsByPattern_ShouldReturnListOfItemDtoResponse() throws Exception {
        ItemDtoResponse itemDtoResponse = new ItemDtoResponse();
        itemDtoResponse.setId(1L);
        itemDtoResponse.setName("Item");

        when(itemService.getItemsByPattern(anyString())).thenReturn(Collections.singletonList(itemDtoResponse));

        mockMvc.perform(get("/items/search")
                        .param("text", "Item"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].name").value("Item"));
    }

    @Test
    void updateItem_ShouldReturnItemDto() throws Exception {
        ItemDto itemDto = new ItemDto();
        itemDto.setName("Updated Item");
        itemDto.setDescription("Updated Description");
        itemDto.setAvailable(true);

        when(itemService.updateItem(anyLong(), anyLong(), any(ItemDto.class))).thenReturn(itemDto);

        mockMvc.perform(patch("/items/{itemId}", 1L)
                        .header("X-Sharer-User-Id", 1L)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(itemDto)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.name").value("Updated Item"));
    }

    @Test
    void deleteItem_ShouldReturnNoContent() throws Exception {
        mockMvc.perform(delete("/items/{id}", 1L))
                .andExpect(status().isOk());
    }
}

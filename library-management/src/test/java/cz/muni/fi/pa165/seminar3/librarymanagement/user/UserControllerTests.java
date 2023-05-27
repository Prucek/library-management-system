package cz.muni.fi.pa165.seminar3.librarymanagement.user;

import static cz.muni.fi.pa165.seminar3.librarymanagement.model.dto.AuthConstants.LIBRARIAN_SCOPE;
import static cz.muni.fi.pa165.seminar3.librarymanagement.model.dto.AuthConstants.USER_SCOPE;
import static cz.muni.fi.pa165.seminar3.librarymanagement.utils.UserUtils.fakeUserDto;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyInt;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.doThrow;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.github.javafaker.Faker;
import cz.muni.fi.pa165.seminar3.librarymanagement.model.dto.common.Result;
import cz.muni.fi.pa165.seminar3.librarymanagement.model.dto.exceptions.NotFoundException;
import cz.muni.fi.pa165.seminar3.librarymanagement.model.dto.user.UserCreateDto;
import cz.muni.fi.pa165.seminar3.librarymanagement.model.dto.user.UserDto;
import cz.muni.fi.pa165.seminar3.librarymanagement.model.dto.user.UserType;
import java.util.UUID;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;

/**
 * Tests for user controller.
 *
 * @author Peter Rúček
 */
@WebMvcTest(controllers = {UserController.class, UserMapper.class})
public class UserControllerTests {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @MockBean
    private UserFacade userFacade;

    private final Faker faker = new Faker();

    @Test
    void createSuccessful() throws Exception {
        UserDto user = fakeUserDto(faker, null);

        given(userFacade.create(any())).willReturn(user);

        mockMvc.perform(post("/users").contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(UserCreateDto.builder()
                                .username(user.getUsername())
                                .firstName(user.getFirstName())
                                .lastName(user.getLastName())
                                .email(user.getEmail())
                                .street(user.getStreet())
                                .houseNumber(user.getHouseNumber())
                                .city(user.getCity())
                                .zip(user.getZip())
                                .country(user.getCountry())
                                .build())))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.username").value(user.getUsername()))
                .andExpect(jsonPath("$.email").value(user.getEmail()))
                .andExpect(jsonPath("$.firstName").value(user.getFirstName()))
                .andExpect(jsonPath("$.lastName").value(user.getLastName()))
                .andExpect(jsonPath("$.email").value(user.getEmail()))
                .andExpect(jsonPath("$.country").value(user.getCountry()))
                .andExpect(jsonPath("$.city").value(user.getCity()))
                .andExpect(jsonPath("$.street").value(user.getStreet()))
                .andExpect(jsonPath("$.houseNumber").value(user.getHouseNumber()))
                .andExpect(jsonPath("$.zip").value(user.getZip()));

    }

    @Test
    public void createEntityNotFound() throws Exception {
        UserDto userDto = fakeUserDto(faker, null);
        // mock facade
        given(userFacade.create(any(UserCreateDto.class))).willThrow(NotFoundException.class);

        // perform test
        mockMvc.perform(post("/users").contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(
                        UserCreateDto.builder().firstName(userDto.getFirstName()).email(userDto.getEmail()).build())));
        mockMvc.perform(post("/users").contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(
                        UserCreateDto.builder().firstName(userDto.getFirstName()).email(userDto.getEmail()).build())));
    }

    @Test
    @WithMockUser(authorities = "SCOPE_" + LIBRARIAN_SCOPE)
    void findAll() throws Exception {
        Result<UserDto> userDtoResult =
                Result.of(fakeUserDto(faker, UserType.CLIENT), fakeUserDto(faker, UserType.CLIENT));

        given(userFacade.findAll(eq(0), anyInt())).willReturn(userDtoResult);

        mockMvc.perform(get("/users").with(csrf()))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.total").value(userDtoResult.getTotal()))
                .andExpect(jsonPath("$.items[0].username").value(userDtoResult.getItems().get(0).getUsername()))
                .andExpect(jsonPath("$.items[0].email").value(userDtoResult.getItems().get(0).getEmail()))
                .andExpect(jsonPath("$.items[0].firstName").value(userDtoResult.getItems().get(0).getFirstName()))
                .andExpect(jsonPath("$.items[0].lastName").value(userDtoResult.getItems().get(0).getLastName()))
                .andExpect(jsonPath("$.items[0].email").value(userDtoResult.getItems().get(0).getEmail()))
                .andExpect(jsonPath("$.items[0].country").value(userDtoResult.getItems().get(0).getCountry()))
                .andExpect(jsonPath("$.items[0].city").value(userDtoResult.getItems().get(0).getCity()))
                .andExpect(jsonPath("$.items[0].street").value(userDtoResult.getItems().get(0).getStreet()))
                .andExpect(jsonPath("$.items[0].houseNumber").value(userDtoResult.getItems().get(0).getHouseNumber()))
                .andExpect(jsonPath("$.items[0].zip").value(userDtoResult.getItems().get(0).getZip()))
                .andExpect(jsonPath("$.items[1].username").value(userDtoResult.getItems().get(1).getUsername()))
                .andExpect(jsonPath("$.items[1].email").value(userDtoResult.getItems().get(1).getEmail()))
                .andExpect(jsonPath("$.items[1].firstName").value(userDtoResult.getItems().get(1).getFirstName()))
                .andExpect(jsonPath("$.items[1].lastName").value(userDtoResult.getItems().get(1).getLastName()))
                .andExpect(jsonPath("$.items[1].email").value(userDtoResult.getItems().get(1).getEmail()))
                .andExpect(jsonPath("$.items[1].country").value(userDtoResult.getItems().get(1).getCountry()))
                .andExpect(jsonPath("$.items[1].city").value(userDtoResult.getItems().get(1).getCity()))
                .andExpect(jsonPath("$.items[1].street").value(userDtoResult.getItems().get(1).getStreet()))
                .andExpect(jsonPath("$.items[1].houseNumber").value(userDtoResult.getItems().get(1).getHouseNumber()))
                .andExpect(jsonPath("$.items[1].zip").value(userDtoResult.getItems().get(1).getZip()));
    }

    @Test
    @WithMockUser(authorities = "SCOPE_" + USER_SCOPE)
    void findSuccessful() throws Exception {
        UserDto user = fakeUserDto(faker, UserType.CLIENT);

        given(userFacade.find(user.getId())).willReturn(user);

        mockMvc.perform(get("/users/" + user.getId()).with(csrf()))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.username").value(user.getUsername()))
                .andExpect(jsonPath("$.email").value(user.getEmail()))
                .andExpect(jsonPath("$.firstName").value(user.getFirstName()))
                .andExpect(jsonPath("$.lastName").value(user.getLastName()))
                .andExpect(jsonPath("$.email").value(user.getEmail()))
                .andExpect(jsonPath("$.country").value(user.getCountry()))
                .andExpect(jsonPath("$.city").value(user.getCity()))
                .andExpect(jsonPath("$.street").value(user.getStreet()))
                .andExpect(jsonPath("$.houseNumber").value(user.getHouseNumber()))
                .andExpect(jsonPath("$.zip").value(user.getZip()));
    }

    @Test
    @WithMockUser(authorities = "SCOPE_" + USER_SCOPE)
    public void findNotFound() throws Exception {
        String userId = UUID.randomUUID().toString();
        // mock facade
        given(userFacade.find(userId)).willThrow(NotFoundException.class);

        // perform test
        mockMvc.perform(get("/users/" + userId).with(csrf())).andExpect(status().isNotFound());
    }

    @Test
    @WithMockUser(authorities = "SCOPE_" + USER_SCOPE)
    public void updateSuccessful() throws Exception {
        UserDto user = fakeUserDto(faker, UserType.CLIENT);
        UserDto updatedUser = fakeUserDto(faker, UserType.CLIENT);
        updatedUser.setUsername("newName");

        given(userFacade.find(user.getId())).willReturn(user);
        given(userFacade.update(eq(user.getId()), any())).willReturn(updatedUser);

        mockMvc.perform(put("/users/" + user.getId()).contentType(MediaType.APPLICATION_JSON)
                        .with(csrf())
                        .content(objectMapper.writeValueAsString(updatedUser)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.username").value(updatedUser.getUsername()))
                .andExpect(jsonPath("$.email").value(updatedUser.getEmail()))
                .andExpect(jsonPath("$.firstName").value(updatedUser.getFirstName()))
                .andExpect(jsonPath("$.lastName").value(updatedUser.getLastName()))
                .andExpect(jsonPath("$.email").value(updatedUser.getEmail()))
                .andExpect(jsonPath("$.country").value(updatedUser.getCountry()))
                .andExpect(jsonPath("$.city").value(updatedUser.getCity()))
                .andExpect(jsonPath("$.street").value(updatedUser.getStreet()))
                .andExpect(jsonPath("$.houseNumber").value(updatedUser.getHouseNumber()))
                .andExpect(jsonPath("$.zip").value(updatedUser.getZip()));
    }

    @Test
    @WithMockUser(authorities = "SCOPE_" + USER_SCOPE)
    public void updateNotFound() throws Exception {
        UserDto user = fakeUserDto(faker, UserType.CLIENT);
        // mock facade
        given(userFacade.update(eq(user.getId()), any())).willThrow(NotFoundException.class);

        // perform test
        mockMvc.perform(put("/users/" + user.getId()).contentType(MediaType.APPLICATION_JSON)
                .with(csrf())
                .content(objectMapper.writeValueAsString(
                        UserCreateDto.builder().firstName(user.getFirstName()).email(user.getEmail()).build())));
    }

    @Test
    @WithMockUser(authorities = "SCOPE_" + USER_SCOPE)
    public void deleteSuccessful() throws Exception {
        UserDto user = fakeUserDto(faker, UserType.CLIENT);

        given(userFacade.find("/users/" + user.getId())).willReturn(user);

        mockMvc.perform(delete("/users/" + user.getId()).with(csrf())).andExpect(status().isNoContent());
    }

    @Test
    @WithMockUser(authorities = "SCOPE_" + USER_SCOPE)
    public void deleteNotFound() throws Exception {
        // mock services
        doThrow(NotFoundException.class).when(userFacade).delete(any());

        // perform test
        mockMvc.perform(delete("/users/" + UUID.randomUUID()).with(csrf())).andExpect(status().isNotFound());
    }
}

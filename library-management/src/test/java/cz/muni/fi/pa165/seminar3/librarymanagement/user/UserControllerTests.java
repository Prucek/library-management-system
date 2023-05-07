package cz.muni.fi.pa165.seminar3.librarymanagement.user;

import static cz.muni.fi.pa165.seminar3.librarymanagement.LibraryManagementApplication.USER_SCOPE;
import static cz.muni.fi.pa165.seminar3.librarymanagement.utils.UserUtils.fakeUserDto;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyInt;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.doThrow;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
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

import static cz.muni.fi.pa165.seminar3.librarymanagement.LibraryManagementApplication.LIBRARIAN_SCOPE;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;

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
//    @WithMockUser(authorities = "SCOPE_" + LIBRARIAN_SCOPE)
    void createSuccessful() throws Exception {
        UserDto user = fakeUserDto(faker, null);

        given(userFacade.create(any())).willReturn(user);

        mockMvc.perform(post("/users").contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(UserCreateDto.builder()
                                .username(user.getUsername())
                                .firstName(user.getFirstName())
                                .lastName(user.getLastName())
                                .email(user.getEmail())
                                .addresses(user.getAddresses())
                                .build())))
                .andDo(print())
                .andExpect(status().is2xxSuccessful())
                .andExpect(jsonPath("$.username").value(user.getUsername()))
                .andExpect(jsonPath("$.email").value(user.getEmail()))
                .andExpect(jsonPath("$.firstName").value(user.getFirstName()))
                .andExpect(jsonPath("$.lastName").value(user.getLastName()))
                .andExpect(jsonPath("$.email").value(user.getEmail()))
                .andExpect(jsonPath("$.addresses[0].country").value(user.getAddresses().get(0).getCountry()))
                .andExpect(jsonPath("$.addresses[0].city").value(user.getAddresses().get(0).getCity()))
                .andExpect(jsonPath("$.addresses[0].street").value(user.getAddresses().get(0).getStreet()))
                .andExpect(jsonPath("$.addresses[0].houseNumber").value(user.getAddresses().get(0).getHouseNumber()));

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
                .content(objectMapper.writeValueAsString(UserCreateDto.builder()
                        .firstName(userDto.getFirstName())
                        .email(userDto.getEmail())
                        .build())));
    }

    @Test
    @WithMockUser(authorities = "SCOPE_" + LIBRARIAN_SCOPE)
    void findAll() throws Exception {
        Result<UserDto> userDtoResult =
                Result.of(fakeUserDto(faker, UserType.CLIENT), fakeUserDto(faker, UserType.CLIENT));

        given(userFacade.findAll(eq(0), anyInt())).willReturn(userDtoResult);

        mockMvc.perform(get("/users").with(csrf()))
                .andDo(print())
                .andExpect(status().is2xxSuccessful())
                .andExpect(jsonPath("$.total").value(userDtoResult.getTotal()))
                .andExpect(jsonPath("$.items[0].username").value(userDtoResult.getItems().get(0).getUsername()))
                .andExpect(jsonPath("$.items[0].email").value(userDtoResult.getItems().get(0).getEmail()))
                .andExpect(jsonPath("$.items[0].firstName").value(userDtoResult.getItems().get(0).getFirstName()))
                .andExpect(jsonPath("$.items[0].lastName").value(userDtoResult.getItems().get(0).getLastName()))
                .andExpect(jsonPath("$.items[0].email").value(userDtoResult.getItems().get(0).getEmail()))
                .andExpect(jsonPath("$.items[0].addresses[0].country").value(
                        userDtoResult.getItems().get(0).getAddresses().get(0).getCountry()))
                .andExpect(jsonPath("$.items[0].addresses[0].city").value(
                        userDtoResult.getItems().get(0).getAddresses().get(0).getCity()))
                .andExpect(jsonPath("$.items[0].addresses[0].street").value(
                        userDtoResult.getItems().get(0).getAddresses().get(0).getStreet()))
                .andExpect(jsonPath("$.items[0].addresses[0].houseNumber").value(
                        userDtoResult.getItems().get(0).getAddresses().get(0).getHouseNumber()))
                .andExpect(jsonPath("$.items[1].username").value(userDtoResult.getItems().get(1).getUsername()))
                .andExpect(jsonPath("$.items[1].email").value(userDtoResult.getItems().get(1).getEmail()))
                .andExpect(jsonPath("$.items[1].firstName").value(userDtoResult.getItems().get(1).getFirstName()))
                .andExpect(jsonPath("$.items[1].lastName").value(userDtoResult.getItems().get(1).getLastName()))
                .andExpect(jsonPath("$.items[1].email").value(userDtoResult.getItems().get(1).getEmail()))
                .andExpect(jsonPath("$.items[1].addresses[0].country").value(
                        userDtoResult.getItems().get(1).getAddresses().get(0).getCountry()))
                .andExpect(jsonPath("$.items[1].addresses[0].city").value(
                        userDtoResult.getItems().get(1).getAddresses().get(0).getCity()))
                .andExpect(jsonPath("$.items[1].addresses[0].street").value(
                        userDtoResult.getItems().get(1).getAddresses().get(0).getStreet()))
                .andExpect(jsonPath("$.items[1].addresses[0].houseNumber").value(
                        userDtoResult.getItems().get(1).getAddresses().get(0).getHouseNumber()));
    }

    @Test
    @WithMockUser(authorities = "SCOPE_" + USER_SCOPE)
    void findSuccessful() throws Exception {
        UserDto user = fakeUserDto(faker, UserType.CLIENT);

        given(userFacade.find(user.getId())).willReturn(user);

        mockMvc.perform(get("/users/" + user.getId()).with(csrf()))
                .andDo(print())
                .andExpect(status().is2xxSuccessful())
                .andExpect(jsonPath("$.username").value(user.getUsername()))
                .andExpect(jsonPath("$.email").value(user.getEmail()))
                .andExpect(jsonPath("$.firstName").value(user.getFirstName()))
                .andExpect(jsonPath("$.lastName").value(user.getLastName()))
                .andExpect(jsonPath("$.email").value(user.getEmail()))
                .andExpect(jsonPath("$.addresses[0].country").value(user.getAddresses().get(0).getCountry()))
                .andExpect(jsonPath("$.addresses[0].city").value(user.getAddresses().get(0).getCity()))
                .andExpect(jsonPath("$.addresses[0].street").value(user.getAddresses().get(0).getStreet()))
                .andExpect(jsonPath("$.addresses[0].houseNumber").value(user.getAddresses().get(0).getHouseNumber()));
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
    public void updateSuccessful() throws Exception{
        UserDto user = fakeUserDto(faker, UserType.CLIENT);
        UserDto updatedUser = fakeUserDto(faker, UserType.CLIENT);
        updatedUser.setUsername("newName");

        given(userFacade.find(user.getId())).willReturn(user);
        given(userFacade.update(eq(user.getId()), any())).willReturn(updatedUser);

        mockMvc.perform(put("/users/" + user.getId()).contentType(MediaType.APPLICATION_JSON).with(csrf())
                        .content(objectMapper.writeValueAsString(updatedUser)))
                .andDo(print())
                .andExpect(status().is2xxSuccessful())
                .andExpect(jsonPath("$.username").value(updatedUser.getUsername()))
                .andExpect(jsonPath("$.email").value(updatedUser.getEmail()))
                .andExpect(jsonPath("$.firstName").value(updatedUser.getFirstName()))
                .andExpect(jsonPath("$.lastName").value(updatedUser.getLastName()))
                .andExpect(jsonPath("$.email").value(updatedUser.getEmail()))
                .andExpect(jsonPath("$.addresses[0].country").value(updatedUser.getAddresses().get(0).getCountry()))
                .andExpect(jsonPath("$.addresses[0].city").value(updatedUser.getAddresses().get(0).getCity()))
                .andExpect(jsonPath("$.addresses[0].street").value(updatedUser.getAddresses().get(0).getStreet()))
                .andExpect(jsonPath("$.addresses[0].houseNumber").value(
                        updatedUser.getAddresses().get(0).getHouseNumber()));
    }

    @Test
    @WithMockUser(authorities = "SCOPE_" + USER_SCOPE)
    public void updateNotFound() throws Exception {
        UserDto user = fakeUserDto(faker, UserType.CLIENT);
        // mock facade
        given(userFacade.update(eq(user.getId()), any())).willThrow(NotFoundException.class);

        // perform test
        mockMvc.perform(put("/users/" + user.getId()).contentType(MediaType.APPLICATION_JSON).with(csrf())
                .content(objectMapper.writeValueAsString(
                        UserCreateDto.builder().firstName(user.getFirstName()).email(user.getEmail()).build())));
    }

    @Test
    @WithMockUser(authorities = "SCOPE_" + USER_SCOPE)
    public void deleteSuccessful() throws Exception {
        UserDto user = fakeUserDto(faker, UserType.CLIENT);

        given(userFacade.find("/users/" + user.getId())).willReturn(user);

        mockMvc.perform(delete("/users/" + user.getId()).with(csrf())).andExpect(status().is2xxSuccessful());
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

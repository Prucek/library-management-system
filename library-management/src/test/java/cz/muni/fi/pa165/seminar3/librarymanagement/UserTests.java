package cz.muni.fi.pa165.seminar3.librarymanagement;

import com.fasterxml.jackson.databind.ObjectMapper;
import cz.muni.fi.pa165.seminar3.librarymanagement.address.Address;
import cz.muni.fi.pa165.seminar3.librarymanagement.model.dto.address.AddressDto;
import cz.muni.fi.pa165.seminar3.librarymanagement.model.dto.user.UserCreateDto;
import cz.muni.fi.pa165.seminar3.librarymanagement.model.dto.user.UserType;
import cz.muni.fi.pa165.seminar3.librarymanagement.user.*;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.data.domain.PageImpl;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import java.util.List;
import java.util.UUID;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(controllers = {UserController.class, UserMapper.class})
public class UserTests {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @MockBean
    private UserService userService;

    User getExampleUser(){
        return User.builder()
                .id(UUID.randomUUID().toString())
                .email("alexcolinski@email.com")
                .firstName("Alex")
                .lastName("Colinski")
                .username("AlexX")
                .password("password")
                .userType(UserType.CLIENT)
                .address(Address.builder().city("Brno").country("CZ").street("Hradecka").houseNumber("31").build())
                .build();
    }

    @Test
    void find_all() throws Exception {
        User user1 = getExampleUser();

        User user2 = User.builder()
                .email("joshnsmith@email.com")
                .firstName("John")
                .lastName("Smith")
                .username("JohnSm")
                .address(Address.builder().city("Prague").country("CZ").street("Brnenska").houseNumber("18").build())
                .build();

        List<User> users = List.of(user1, user2);
        given(userService.findAll(any())).willReturn(new PageImpl<>(users));

        System.out.println( user1.getAddresses().get(0).getCity());

        mockMvc.perform(get("/users"))
                .andDo(print())
                .andExpect(status().is2xxSuccessful())
                .andExpect(jsonPath("$.total").value(users.size()))
                .andExpect(jsonPath("$.items[0].username").value(user1.getUsername()))
                .andExpect(jsonPath("$.items[0].email").value(user1.getEmail()))
                .andExpect(jsonPath("$.items[0].firstName").value(user1.getFirstName()))
                .andExpect(jsonPath("$.items[0].lastName").value(user1.getLastName()))
                .andExpect(jsonPath("$.items[0].email").value(user1.getEmail()))
                .andExpect(jsonPath("$.items[0].addresses[0].country").value(user1.getAddresses().get(0).getCountry()))
                .andExpect(jsonPath("$.items[0].addresses[0].city").value(user1.getAddresses().get(0).getCity()))
                .andExpect(jsonPath("$.items[0].addresses[0].street").value(user1.getAddresses().get(0).getStreet()))
                .andExpect(jsonPath("$.items[0].addresses[0].houseNumber").value(user1.getAddresses().get(0).getHouseNumber()))
                .andExpect(jsonPath("$.items[1].username").value(user2.getUsername()))
                .andExpect(jsonPath("$.items[1].email").value(user2.getEmail()))
                .andExpect(jsonPath("$.items[1].firstName").value(user2.getFirstName()))
                .andExpect(jsonPath("$.items[1].lastName").value(user2.getLastName()))
                .andExpect(jsonPath("$.items[1].email").value(user2.getEmail()))
                .andExpect(jsonPath("$.items[1].addresses[0].country").value(user2.getAddresses().get(0).getCountry()))
                .andExpect(jsonPath("$.items[1].addresses[0].city").value(user2.getAddresses().get(0).getCity()))
                .andExpect(jsonPath("$.items[1].addresses[0].street").value(user2.getAddresses().get(0).getStreet()))
                .andExpect(jsonPath("$.items[1].addresses[0].houseNumber").value(user2.getAddresses().get(0).getHouseNumber()));
    }

    @Test
    void find_user() throws Exception {
        User user = getExampleUser();

        given(userService.find(user.getId())).willReturn(user);

        mockMvc.perform(get("/users/"+user.getId()))
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
    void create_user() throws Exception {
        User user = getExampleUser();

        UserCreateDto userCreateDto = new UserCreateDto();
        userCreateDto.setUsername("johnik");
        userCreateDto.setFirstName("AlexX");
        userCreateDto.setLastName("Colinski");
        userCreateDto.setEmail("alexcolinski@email.com");
        userCreateDto.setPassword("password");
        userCreateDto.setAddresses(List.of(AddressDto.builder()
                .city("Brno")
                .country("CZ")
                .street("Hradecka")
                .houseNumber("31")
                .build()));

        given(userService.create(any())).willReturn(user);

        mockMvc.perform(post("/users").contentType(MediaType.APPLICATION_JSON)
                    .content(objectMapper.writeValueAsString(userCreateDto)))
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
    public void delete_user() throws Exception{
        User user = getExampleUser();

        given(userService.find("/users/" + user.getId())).willReturn(user);

        mockMvc.perform(delete("/users/" + user.getId()))
                .andExpect(status().is2xxSuccessful());
    }

    @Test
    public void update_user() throws Exception{
        User user = getExampleUser();
        User updatedUser = getExampleUser();
        updatedUser.setUsername("newName");

        given(userService.find(user.getId())).willReturn(user);
        given(userService.create(user)).willReturn(updatedUser);

        mockMvc.perform(put("/users/" + user.getId()).contentType(MediaType.APPLICATION_JSON)
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
                .andExpect(jsonPath("$.addresses[0].houseNumber").value(updatedUser.getAddresses().get(0).getHouseNumber()));
    }
}

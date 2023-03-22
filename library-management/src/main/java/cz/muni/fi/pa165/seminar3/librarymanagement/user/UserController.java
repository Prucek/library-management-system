package cz.muni.fi.pa165.seminar3.librarymanagement.user;

import cz.muni.fi.pa165.seminar3.librarymanagement.common.ErrorMessage;
import cz.muni.fi.pa165.seminar3.librarymanagement.model.dto.common.Result;
import cz.muni.fi.pa165.seminar3.librarymanagement.model.dto.user.UserCreateDto;
import cz.muni.fi.pa165.seminar3.librarymanagement.model.dto.user.UserDto;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/users")
public class UserController {

    private final UserService service;

    private final UserMapper mapper;

    @Autowired
    public UserController(UserService service, UserMapper mapper) {
        this.service = service;
        this.mapper = mapper;
    }

    @GetMapping("/{id}")
    public UserDto find(@PathVariable String id) {
        User user = service.find(id);
        if (user != null){
            return mapper.toDto(user);
        } else {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "user with id=" + id + " not found");
        }
    }

    @PostMapping
    public UserDto create(@RequestBody UserCreateDto dto) {
        return mapper.toDto(service.create(mapper.fromCreateDto(dto)));
    }

    @GetMapping
    public Result<UserDto> findAll(Pageable page) {
        return mapper.toResult(service.findAll(page));
    }
}
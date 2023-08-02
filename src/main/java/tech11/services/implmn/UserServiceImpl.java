package tech11.services.implmn;

import io.quarkus.hibernate.orm.panache.PanacheQuery;
import io.quarkus.panache.common.Page;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import jakarta.transaction.Transactional;

import jakarta.ws.rs.InternalServerErrorException;
import jakarta.ws.rs.NotFoundException;
import lombok.extern.slf4j.Slf4j;
import org.modelmapper.ModelMapper;
import tech11.dto.AddUserRequest;
import tech11.dto.UpdateUserRequest;
import tech11.dto.UserDTO;
import tech11.models.User;
import tech11.repositories.UserRepository;
import tech11.services.UserService;
import tech11.utils.password.PasswordUtils;
import tech11.utils.request.ApiResponse;
import tech11.utils.request.Pagination;

import java.security.NoSuchAlgorithmException;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.List;


@ApplicationScoped
@Slf4j
@Transactional
public class UserServiceImpl implements UserService {

    @Inject
    UserRepository repository;

    @Inject
    ModelMapper modelMapper;

    @Override
    public ApiResponse<List<UserDTO>> fetchUsers(int page, int size) {
        // specify page and size for requested data
        Page pageable = Page.of(page, size);


        PanacheQuery<User> usersQuery = repository.findAll();
        List<User> users = usersQuery.page(pageable).list();

        Pagination pagination = Pagination.build(usersQuery);

        List<UserDTO> userDTOS = users.stream()
                .map( user -> modelMapper.map(user, UserDTO.class))
                .toList();
        log.info("Mapped fetched users to a presentable form ");

        ApiResponse<List<UserDTO>> response = ApiResponse.<List<UserDTO>>builder()
                .pagination(pagination)
                .data(userDTOS)
                .message("Operation completed Successfully")
                .build();
        log.info("Successfully built response for request to fetch all users");
        return response;
    }

    @Override
    public UserDTO addUser(AddUserRequest addUserRequest) {
        try {
            User user = User.builder()
                    .email(addUserRequest.email())
                    .lastname(addUserRequest.lastname())
                    .firstname(addUserRequest.firstname())
                    .phone(addUserRequest.phone())
                    .build();

            // todo add format to application.properties file

            DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd-MM-yyyy");

            LocalDate dob = LocalDate.parse(addUserRequest.dateOfBirth(), formatter);

            boolean isFuture = dob.isAfter(LocalDate.now());
            if (isFuture) throw new NotFoundException("You should provided a past date");

            user.setDateOfBirth(dob);
            user.setBirthday(dob.format(DateTimeFormatter.ofPattern("dd-MM")));

            // the test mention nothing about hashing password so I took the liberty of implementing a
            // simple hashing with Java as it was stated to use only jakarta provided dependencies
            String hashedPassword = PasswordUtils.hashPassword(addUserRequest.password());
            user.setPassword(hashedPassword);

            repository.persist(user);

            return modelMapper.map(user, UserDTO.class);
        } catch (NoSuchAlgorithmException e) {
            throw new InternalServerErrorException("We Failed To Save User. Try Again or Contact Support");
        }
    }

    @Override
    public UserDTO fetchSingleUser(long id) {
        User user = repository.findByIdOptional(id)
                .orElseThrow( () -> {
                    log.error("Requested User with ID not found [id={}]", id);
                    return new NotFoundException("We Cannot Find Requested User");
                });
        log.info("Requested User successfully retrieved [user={}]", user.toString());
        return modelMapper.map(user, UserDTO.class);
    }

    @Override
    public UserDTO modifyUser(long id, UpdateUserRequest requestData) {
        User user = repository.findByIdOptional(id)
                .orElseThrow( () -> {
                    log.error("Requested User with ID not found [id={}]", id);
                    return new NotFoundException("We Cannot Find Requested User");
                });
        log.info("Requested User successfully retrieved [user={}]", user.toString());

        try {
            user.setFirstname(requestData.firstname());
            user.setLastname(requestData.lastname());
            user.setPhone(requestData.phone());
            repository.persist(user);
            log.info("User modified successfully [id={}, user={}]", id, user);

            return modelMapper.map(user, UserDTO.class);
        } catch (RuntimeException e) {
            log.error("Failed to save user update [error={}]", e.getMessage());
            throw new InternalServerErrorException("We Failed To Update User");
        }
    }

    @Override
    public UserDTO deleteUser(long id) {
        User user = repository.findByIdOptional(id)
                .orElseThrow( () -> {
                    log.error("Requested User with ID not found [id={}]", id);
                    return new NotFoundException("We Cannot Find Requested User");
                });
        log.info("Requested User successfully retrieved [user={}]", user.toString());

        try {
            repository.deleteById(id);
            return modelMapper.map(user, UserDTO.class);
        } catch (RuntimeException e) {
            log.error("Failed to delete user [user={}]", user);
            throw new InternalServerErrorException("We Failed To Delete User");
        }
    }



}

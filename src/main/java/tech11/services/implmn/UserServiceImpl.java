package tech11.services.implmn;

import io.quarkus.hibernate.orm.panache.PanacheQuery;
import io.quarkus.panache.common.Page;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import jakarta.transaction.Transactional;

import jakarta.ws.rs.BadRequestException;
import jakarta.ws.rs.InternalServerErrorException;
import jakarta.ws.rs.NotFoundException;
import jakarta.ws.rs.core.Response;
import lombok.extern.slf4j.Slf4j;
import org.h2.jdbc.JdbcSQLIntegrityConstraintViolationException;
import org.hibernate.JDBCException;
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
import java.util.stream.Collectors;


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
                        .collect(Collectors.toList());
        log.info("Mapped fetched users to a presentable form ");

        ApiResponse<List<UserDTO>> response = ApiResponse.<List<UserDTO>>builder()
                .pagination(pagination)
                .data(userDTOS)
                .message("Operation completed Successfully").status(Response.Status.OK.name())
                .build();
        log.info("Sending response for request to fetch all users");
        return response;
    }

    @Override
    public UserDTO addUser(AddUserRequest addUserRequest) throws JDBCException {
        try {
            User user = User.builder()
                    .email(addUserRequest.getEmail())
                    .lastname(addUserRequest.getLastname())
                    .firstname(addUserRequest.getFirstname())
                    .phone(addUserRequest.getPhone())
                    .build();


            repository.findByUsername(addUserRequest.getUsername()).ifPresent( user1 -> {
                throw new BadRequestException("User already exists");
            });

            repository.findByEmail(addUserRequest.getEmail()).ifPresent( user1 -> {
                throw new BadRequestException("User already exists");
            });

            DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd-MM-yyyy");

            LocalDate dob = LocalDate.parse(addUserRequest.getDateOfBirth(), formatter);

            boolean isFuture = dob.isAfter(LocalDate.now());
            if (isFuture) throw new BadRequestException("You provided a future date");

            user.setDateOfBirth(dob);
            user.setBirthday(dob.format(DateTimeFormatter.ofPattern("dd-MM")));

            // the test mentioned nothing about hashing password so I took the liberty of implementing a
            // simple hashing with Java as it was stated to use only jakarta provided dependencies
            String hashedPassword = PasswordUtils.hashPassword(addUserRequest.getPassword());
            user.setPassword(hashedPassword);

            user.setUsername(addUserRequest.getUsername());

            repository.persist(user);

            return modelMapper.map(user, UserDTO.class);
        } catch (BadRequestException e) {
            log.error("Client Error Occurred : [error={}]", e.getMessage());
            throw new BadRequestException(e.getMessage());
        } catch (NoSuchAlgorithmException e) {
            log.error("Server error occurred while trying to hashing password [exception={}]", e.getMessage());
            throw new InternalServerErrorException("We Failed To Save User. Try Again or Contact Support");
        } catch (Exception e) {
            log.error("Server error occurred [exception={}]", e.getMessage());
            throw new InternalServerErrorException("We Failed To Save User. Try Again or Contact Support");
        }
    }

    @Override
    public UserDTO fetchSingleUser(String id) {
        User user = repository.findByIdOptional(id)
                .orElseThrow( () -> {
                    log.error("Requested User with ID not found [id={}]", id);
                    return new NotFoundException("We Cannot Find Requested User");
                });
        log.info("Requested User successfully retrieved [user={}]", user.toString());
        return modelMapper.map(user, UserDTO.class);
    }

    @Override
    public UserDTO fetchSingleUserByUsername(String username) {
        User user = repository.findByUsername(username)
                .orElseThrow( () -> {
                    log.error("Requested User with Username not found [username={}]", username);
                    return new NotFoundException("We Cannot Find Requested User");
                });
        log.info("Requested User successfully retrieved [user={}]", user.toString());
        return modelMapper.map(user, UserDTO.class);
    }

    @Override
    public UserDTO modifyUser(String id, UpdateUserRequest requestData) {
        User user = repository.findByIdOptional(id)
                .orElseThrow( () -> {
                    log.error("Requested User with ID not found [id={}]", id);
                    return new NotFoundException("We Cannot Find Requested User");
                });
        log.info("Requested User successfully retrieved [user={}]", user.toString());

        try {
            user.setFirstname(requestData.getFirstname());
            user.setLastname(requestData.getLastname());
            user.setPhone(requestData.getPhone());
            repository.persist(user);
            log.info("User modified successfully [id={}, user={}]", id, user);

            return modelMapper.map(user, UserDTO.class);
        } catch (RuntimeException e) {
            log.error("Failed to save user update [error={}]", e.getMessage());
            throw new InternalServerErrorException("We Failed To Update User");
        }
    }

    @Override
    public UserDTO deleteUser(String id) {
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

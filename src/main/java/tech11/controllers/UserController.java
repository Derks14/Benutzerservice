package tech11.controllers;

import jakarta.inject.Inject;
import jakarta.validation.Valid;
import jakarta.ws.rs.*;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;
import lombok.extern.slf4j.Slf4j;
import org.eclipse.microprofile.openapi.annotations.Operation;
import tech11.dto.AddUserRequest;
import tech11.dto.UpdateUserRequest;
import tech11.dto.UserDTO;
import tech11.services.UserService;
import tech11.utils.request.ApiResponse;

import java.util.List;

@Path("/api/users")
@Slf4j
@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)
public class UserController {
    @Inject
    UserService userService;

    @POST
    @Operation(summary = "Add new users")
    public Response addUser(@Valid AddUserRequest request) {
        log.info("Request to add User [request={}]", request);

        ApiResponse<UserDTO> response = ApiResponse.<UserDTO>builder()
                .data(userService.addUser(request))
                .message("User Added Successfully").status(Response.Status.CREATED.name())
                .build();

        return Response.status(Response.Status.CREATED).entity(response).build();
    }

    @GET
    @Operation(summary = "Fetch all users per page and size")
    public ApiResponse<List<UserDTO>> fetchUsers( @QueryParam("page") @DefaultValue("0") int page,
                                                  @QueryParam("size") @DefaultValue("10") int size ) {
        log.info("Request to fetch users.[page={}, size={}]", page, size);
        return userService.fetchUsers(page, size);
    }

    @GET
    @Path("{id}")
    @Operation(summary = "Retrieve single user with id")
    public ApiResponse<UserDTO> getUser(@PathParam("id") String id) {
        log.info("Request to fetch single user [userId={}]", id);
        UserDTO userDTO = userService.fetchSingleUser(id);
        ApiResponse<UserDTO> response = ApiResponse.<UserDTO>builder()
                .data(userDTO)
                .message("User Retrieved Successfully").status(Response.Status.OK.name())
                .build();
        return response;
    }

    @GET
    @Path("username/{username}")
    @Operation(summary = "Retrieve single user by username")
    public ApiResponse<UserDTO> getByUsername(@PathParam("username") String username) {
        log.info("Request to fetch single user by username [username={}]", username);
        UserDTO userDTO = userService.fetchSingleUserByUsername(username);
        return  ApiResponse.<UserDTO>builder()
                .data(userDTO)
                .message("User Retrieved Successfully").status(Response.Status.OK.name())
                .build();
    }

    @PUT
    @Path("{id}")
    @Operation(summary = "Modify single user with id ")
    public ApiResponse<UserDTO> modifyUser(@PathParam("id") String id, @Valid UpdateUserRequest updateUserRequest) {
        log.info("Request to modify user [userId={}]", id);
        UserDTO userDTO = userService.modifyUser(id, updateUserRequest);
        return ApiResponse.<UserDTO>builder()
                .data(userDTO)
                .message("User Updated Successfully").status(Response.Status.OK.name())
                .build();
    }


    @DELETE
    @Path("{id}")
    @Operation(summary = "Delete single user from id")
    public ApiResponse<UserDTO> deleteUser(@PathParam("id") String id) {
        log.info("Request to delete user [userId={}]", id);
        UserDTO userDTO = userService.deleteUser(id);
        return ApiResponse.<UserDTO>builder()
                .data(userDTO)
                .message("User deleted Successfully").status(Response.Status.OK.name())
                .build();
    }
}

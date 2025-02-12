package org.example.bookstore.service;

import org.example.bookstore.enums.ErrorCode;
import org.example.bookstore.exception.AppException;
import org.example.bookstore.model.*;
import org.example.bookstore.payload.CartDTO;
import org.example.bookstore.payload.CartItemDTO;
import org.example.bookstore.payload.UserDTO;
import org.example.bookstore.payload.response.UserResponse;
import org.example.bookstore.repository.RoleRepository;
import org.example.bookstore.repository.UserRepository;
import org.example.bookstore.service.Interface.CartService;
import org.example.bookstore.service.Interface.UserService;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
public class UserServiceImpl implements UserService {
    @Autowired
    private UserRepository userRepository;

    @Autowired
    private ModelMapper modelMapper;

    @Autowired
    private CartService cartService;

    @Autowired
    private RoleRepository roleRepository;


    private PasswordEncoder passwordEncoder;

    @Override
    public UserDTO registerUser(UserDTO userDTO) {
        if (userRepository.findUserByEmail(userDTO.getEmail()).isPresent()) {
            throw new AppException(ErrorCode.USER_WITH_EMAIL_EXISTED);
        }
        try {
            User user = modelMapper.map(userDTO, User.class);

            Cart cart = new Cart();
            user.setCart(cart);

            Role role = roleRepository.findByRoleName("USER");
            if(role == null) {
                throw new AppException(ErrorCode.ROLE_NOT_FOUND);
            }
            user.setRoles(Set.of(role));
            User registeredUser = userRepository.save(user);

            cart.setUser(registeredUser);

            return modelMapper.map(registeredUser, UserDTO.class);
        } catch (DataIntegrityViolationException e) {
            throw new RuntimeException("DATA_INTEGRITY_VIOLATION");
        } catch (Exception e) {
            throw new RuntimeException("Error registering user", e);
        }
    }


    @Override
    public UserResponse getAllUsers(Integer pageNumber, Integer pageSize, String sortBy, String sortOrder) {
        Sort sortByAndOrder = sortOrder.equalsIgnoreCase("asc") ? Sort.by(sortBy).ascending()
                : Sort.by(sortBy).descending();
        Pageable pageDetails = PageRequest.of(pageNumber, pageSize, sortByAndOrder);
        Page<User> pageUsers = userRepository.findAll(pageDetails);
        List<UserDTO> userDTOs = pageUsers.getContent().stream()
                .map(user -> {
                    UserDTO userDTO = modelMapper.map(user, UserDTO.class);
                    return userDTO;
                }).toList();

        UserResponse userResponse = new UserResponse();
        userResponse.setContent(userDTOs);
        userResponse.setPageNumber(pageUsers.getNumber());
        userResponse.setPageSize(pageUsers.getSize());
        userResponse.setTotalElements(pageUsers.getTotalElements());
        userResponse.setTotalPages(pageUsers.getTotalPages());
        userResponse.setLastPage(pageUsers.isLast());
        return userResponse;

    }

    @Override
    public UserDTO getUserById(UUID userId) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new RuntimeException("User with email not found"));

        UserDTO userDTO = modelMapper.map(user, UserDTO.class);
        CartDTO cart = modelMapper.map(user.getCart(), CartDTO.class);

        List<CartItemDTO> cartItemDTOS = user.getCart().getCartItems().stream()
                .map(item -> modelMapper.map(item.getBook(), CartItemDTO.class)).collect(Collectors.toList());
        userDTO.setCart(cart);

        userDTO.getCart().setCartItem(cartItemDTOS);

        return userDTO;
    }

    @Override
    public UserDTO updateUser(UUID userId, UserDTO userDTO) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new RuntimeException("User with email not found"));

        String encodedPass = passwordEncoder.encode(userDTO.getPassword());

        user.setFirstName(userDTO.getFirstName());
        user.setLastName(userDTO.getLastName());
        user.setPhoneNumber(userDTO.getPhoneNumber());
        user.setEmail(userDTO.getEmail());
        user.setPassword(encodedPass);
        user.setAddress(userDTO.getAddress());

        userDTO = modelMapper.map(user, UserDTO.class);
        CartDTO cart = modelMapper.map(user.getCart(), CartDTO.class);
        List<CartItemDTO> cartItemDTOS = user.getCart().getCartItems().stream()
                .map(item -> modelMapper.map(item.getBook(), CartItemDTO.class)).collect(Collectors.toList());
        userDTO.setCart(cart);
        userDTO.getCart().setCartItem(cartItemDTOS);
        return userDTO;
    }

    @Override
    public String deleteUser(UUID userId) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new RuntimeException("User with email not found"));

        List<CartItem> cartItems = user.getCart().getCartItems();
        Cart cart = user.getCart();

        cartItems.forEach(item -> {

            UUID bookId = item.getBook().getId();

            cartService.deleteProductFromCart(cart.getId(), bookId);
        });

        userRepository.delete(user);

        return "User with userId " + userId + " deleted successfully!!!";
    }

    @Override
    public List<UserResponse> getAllUser() {
        List<User> users = userRepository.findAll();
        return users.stream()
                .map(user -> modelMapper.map(user, UserResponse.class))
                .collect(Collectors.toList());
    }

    @Override
    public UserDTO getMyProfile(String username) {
        User user = userRepository.findUserByUsername(username)
                .orElseThrow(() -> new AppException(ErrorCode.USER_NOT_FOUND));
        UserDTO userDTO = modelMapper.map(user, UserDTO.class);
        return userDTO;
    }

}

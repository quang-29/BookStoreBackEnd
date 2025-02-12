package org.example.bookstore.mapper;

import org.example.bookstore.model.Role;
import org.example.bookstore.model.User;
import org.example.bookstore.payload.CartDTO;
import org.example.bookstore.payload.UserDTO;
import org.example.bookstore.repository.RoleRepository;
import org.springframework.stereotype.Component;

import java.util.Set;
import java.util.stream.Collectors;

@Component
public class UserMapper {

//    public static UserDTO toDTO(User user) {
//        return new UserDTO(
//                user.getId(),
//                user.getUsername(),
//                user.getFirstName(),
//                user.getLastName(),
//                user.getPhoneNumber(),
//                user.getEmail(),
//                null,
//                user.getAddress(),
//                user.getCart() != null ? new CartDTO(user.getCart().getId(),user.getCart().getCartItems(),user.getCart().getTotalPrice()) : null,
//                user.getRoles().stream()
//                        .map(Role::getRoleName)
//                        .collect(Collectors.toSet())
//        );
//    }
//
//    public static User toEntity(UserDTO userDTO, RoleRepository roleRepository) {
//        User user = new User();
//        user.setId(userDTO.getUserId());
//        user.setUsername(userDTO.getUsername());
//        user.setFirstName(userDTO.getFirstName());
//        user.setLastName(userDTO.getLastName());
//        user.setPhoneNumber(userDTO.getPhoneNumber());
//        user.setEmail(userDTO.getEmail());
//        user.setPassword(userDTO.getPassword());
//        user.setAddress(userDTO.getAddress());
//
//        if (userDTO.getRoleNames() != null) {
//            Set<Role> roles = userDTO.getRoleNames().stream()
//                    .map(roleRepository::findByRoleName)
//                    .collect(Collectors.toSet());
//            user.setRoles(roles);
//        }
//
//        return user;
//    }
}

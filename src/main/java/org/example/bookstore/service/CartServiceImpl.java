package org.example.bookstore.service;


import jakarta.transaction.Transactional;
import org.example.bookstore.enums.ErrorCode;
import org.example.bookstore.exception.APIException;
import org.example.bookstore.exception.AppException;
import org.example.bookstore.exception.ResourceNotFoundException;
import org.example.bookstore.model.Book;
import org.example.bookstore.model.Cart;
import org.example.bookstore.model.CartItem;
import org.example.bookstore.payload.BookDTO;
import org.example.bookstore.payload.CartDTO;
import org.example.bookstore.payload.CartItemDTO;
import org.example.bookstore.repository.BookRepository;
import org.example.bookstore.repository.CartItemRepository;
import org.example.bookstore.repository.CartRepository;
import org.example.bookstore.service.Interface.CartService;
import org.modelmapper.ModelMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
public class CartServiceImpl implements CartService {
    private final CartRepository cartRepository;
    private final BookRepository bookRepository;
    private final CartItemRepository cartItemRepository;

    @Autowired
    private ModelMapper modelMapper;

    private static final Logger logger = LoggerFactory.getLogger(CartServiceImpl.class);

    public CartServiceImpl(CartRepository cartRepository, BookRepository bookRepository, CartItemRepository cartItemRepository) {
        this.cartRepository = cartRepository;
        this.bookRepository = bookRepository;
        this.cartItemRepository = cartItemRepository;
    }

    @Override
    public CartDTO addProductToCart(UUID cartId, UUID bookId, Integer quantity) {

        Cart cart = cartRepository.findById(cartId)
                .orElseThrow(() -> new AppException(ErrorCode.CART_NOT_FOUND));
        Book book = bookRepository.findById(bookId)
                .orElseThrow(() -> new AppException(ErrorCode.BOOK_NOT_FOUND));

        // Kiểm tra số lượng tồn kho
        if (book.getStock() < quantity) {
            throw new AppException(ErrorCode.BOOK_STOCK_PROBLEM);
        }

        // Tìm CartItem trong giỏ hàng
        CartItem cartItem = cartItemRepository.findCartItemByCartIdAndBookId(cartId, bookId);

        if (cartItem != null) {
            // Nếu sản phẩm đã có trong giỏ hàng, cập nhật số lượng và giá
            cartItem.setQuantity(cartItem.getQuantity() + quantity);
            cartItem.setBookPrice(cartItem.getBook().getPrice());
            cartItemRepository.save(cartItem);
        } else {
            // Nếu sản phẩm chưa có, tạo mới CartItem
            CartItem newCartItem = new CartItem();
            newCartItem.setCart(cart);
            newCartItem.setBook(book);
            newCartItem.setQuantity(quantity);
            newCartItem.setBookPrice(book.getPrice()); // Tính giá đúng
            cartItemRepository.save(newCartItem);
        }

        book.setStock(book.getStock() - quantity);
        bookRepository.save(book);

        updateCartTotalPrice(cart);
        cartRepository.save(cart);

        CartDTO cartDTO = new CartDTO();
        cartDTO.setCartId(cartId);
        cartDTO.setTotalPrice(cart.getTotalPrice());
        List<CartItemDTO> cartItemDTOS = new ArrayList<>();
        cart.getCartItems().forEach(cartItem1 -> {
            CartItemDTO cartItemDTO = new CartItemDTO();
            cartItemDTO.setCartItemId(cartItem1.getId());
            cartItemDTO.setQuantity(cartItem1.getQuantity());
            cartItemDTO.setBook(modelMapper.map(cartItem1.getBook(), BookDTO.class));
            cartItemDTO.setBookPrice(cartItem1.getBookPrice());
            cartItemDTOS.add(cartItemDTO);
        });
        cartDTO.setCartItem(cartItemDTOS);
        return cartDTO;
    }

    private void updateCartTotalPrice(Cart cart) {
        BigDecimal totalPrice = cart.getCartItems().stream()
                .map(cartItem -> cartItem.getBookPrice().multiply(BigDecimal.valueOf(cartItem.getQuantity())))
                .reduce(BigDecimal.ZERO, BigDecimal::add);
        cart.setTotalPrice(totalPrice);
    }


    @Override
    public List<CartDTO> getAllCarts() {
        List<Cart> carts = cartRepository.findAll();
        if (carts.size() == 0) {
            throw new AppException(ErrorCode.CART_NOT_FOUND);
        }
        List<CartDTO> cartDTOs = carts.stream().map(cart -> {
            CartDTO cartDTO = modelMapper.map(cart, CartDTO.class);
            List<CartItemDTO> cartItemDTOS = cart.getCartItems().stream()
                    .map(p -> modelMapper.map(p.getBook(), CartItemDTO.class)).collect(Collectors.toList());
            cartDTO.setCartItem(cartItemDTOS);
            return cartDTO;
        }).collect(Collectors.toList());
        return cartDTOs;
    }



    @Override
    public CartDTO getCartByUserName(String userName) {
        Cart cart = cartRepository.getCartByUserName(userName);
        CartDTO cartDTO = new CartDTO();
        cartDTO.setCartId(cart.getId());
        cartDTO.setTotalPrice(cart.getTotalPrice());
        if(cart.getCartItems().size() == 0){
            logger.info("Empty cart!");
            cartDTO.setTotalPrice(BigDecimal.ZERO);
        }
        List<CartItemDTO> cartItemDTOS = new ArrayList<>();
        cart.getCartItems().forEach(cartItem -> {
            CartItemDTO cartItemDTO = new CartItemDTO();
            cartItemDTO.setCartItemId(cartItem.getId());
            cartItemDTO.setQuantity(cartItem.getQuantity());
            cartItemDTO.setBook(modelMapper.map(cartItem.getBook(), BookDTO.class));
            cartItemDTO.setBookPrice(cartItem.getBook().getPrice());
            cartItemDTOS.add(cartItemDTO);
        });
        cartDTO.setCartItem(cartItemDTOS);
        return cartDTO;
    }

    @Override
    @Transactional
    public boolean deleteProductFromCart(UUID cartId, UUID bookId) {
        Cart cart = cartRepository.findById(cartId)
                .orElseThrow(() -> new AppException(ErrorCode.CART_NOT_FOUND));

        CartItem cartItem = cartItemRepository.findCartItemByCartIdAndBookId(cartId, bookId);

        if (cartItem == null) {
            throw new AppException(ErrorCode.CART_NO_FOUND_BOOK);
        }

        cart.setTotalPrice(cart.getTotalPrice().subtract(cartItem.getBookPrice().multiply(BigDecimal.valueOf(cartItem.getQuantity()))));
        cartRepository.save(cart);
        Book book = cartItem.getBook();
        book.setStock(book.getStock() + cartItem.getQuantity());
        cartItemRepository.deleteCartItemByCartIdAndBookId(cartId, bookId);
        return true;

    }
}

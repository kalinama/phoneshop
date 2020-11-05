package com.es.core.cart.service;

import com.es.core.cart.entity.Cart;
import com.es.core.cart.entity.CartItem;
import com.es.core.cart.service.exception.PhoneNotFoundException;
import com.es.core.order.service.exception.OutOfStockException;
import com.es.core.phone.dao.PhoneDao;
import com.es.core.phone.entity.Phone;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;

import javax.servlet.http.HttpSession;
import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.math.BigDecimal;
import java.util.Optional;

import static org.junit.Assert.*;
import static org.junit.Assert.assertNotSame;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

@RunWith(MockitoJUnitRunner.class)
public class HttpSessionCartServiceTest {
    @Mock
    HttpSession httpSession;
    @Mock
    private PhoneDao phoneDao;

    @InjectMocks
    private HttpSessionCartService cartService;

    private Cart cart;

    private final String CART_SESSION_ATTRIBUTE_TEST;

    public HttpSessionCartServiceTest() throws IllegalAccessException, NoSuchFieldException {
        Field field = HttpSessionCartService.class.getDeclaredField("CART_SESSION_ATTRIBUTE");
        field.setAccessible(true);
        CART_SESSION_ATTRIBUTE_TEST = (String) field.get(null);
    }

    @Before
    public void setup() {
        cart = new Cart();
        Phone testPhone = new Phone(1L, new BigDecimal(100));
        cart.getCartItems().add(new CartItem(testPhone, 1L));

       when(httpSession.getId()).thenReturn(anyString());
       when(httpSession.getAttribute(CART_SESSION_ATTRIBUTE_TEST)).thenReturn(cart);
    }

    @Test
    public void getCartForSameSessionsTest() {
        Cart firstCart = cartService.getCart(httpSession);
        Cart secondCart = cartService.getCart(httpSession);

        assertSame(firstCart, secondCart);
    }

    @Test
    public void getCartForDifferentSessionsTest() {
        HttpSession otherSession = mock(HttpSession.class);
        when(otherSession.getId()).thenReturn(anyString());
        when(otherSession.getAttribute(CART_SESSION_ATTRIBUTE_TEST)).thenReturn(null);

        Cart firstCart = cartService.getCart(httpSession);
        Cart secondCart = cartService.getCart(otherSession);

        assertNotSame(firstCart, secondCart);
    }

    @Test
    public void addNewPhoneToCartTest() {
        Phone phone = new Phone(2L, new BigDecimal(150));
        Long quantity = 3L;
        when(phoneDao.get(anyLong())).thenReturn(Optional.of(phone));
        cartService.addPhone(cart, anyLong(), quantity);

        Optional<CartItem> cartItem = cart.getCartItems().stream()
                .filter(item -> item.getPhone().equals(phone)).findFirst();
        assertTrue(cartItem.isPresent());
        assertEquals(cartItem.get().getQuantity(), quantity);
    }

    @Test(expected = IllegalArgumentException.class)
    public void addNewPhoneThrowIllegalArgumentException() {
        cartService.addPhone(cart,1L, 0L);
    }

    @Test(expected = PhoneNotFoundException.class)
    public void addNewPhoneThrowPhoneNotFoundException() {
        cartService.addPhone(cart, anyLong(), 1L);
        when(phoneDao.get(anyLong())).thenReturn(Optional.empty());
    }

    @Test
    public void addExistedPhoneToCartTest() {
        CartItem cartItem = cart.getCartItems().get(0);
        Long quantityBeforeAdding = cartItem.getQuantity();
        Long quantity = 3L;
        when(phoneDao.get(anyLong())).thenReturn(Optional.of(cart.getCartItems().get(0).getPhone()));
        cartService.addPhone(cart, anyLong(), quantity);

        assertEquals((long) cartItem.getQuantity(), quantity + quantityBeforeAdding);
    }


    @Test
    public void recalculateCartTest() throws NoSuchMethodException, InvocationTargetException, IllegalAccessException, OutOfStockException {
        long quantityNewPhone = 3;
        Phone newPhone = new Phone(10L, new BigDecimal(134));
        when(phoneDao.get(newPhone.getId())).thenReturn(Optional.of(newPhone));
        cartService.addPhone(cart, newPhone.getId(), quantityNewPhone);

        long quantityExistedPhoneBeforeAdding = cart.getCartItems().get(0).getQuantity();
        long quantityExistedPhone = 3;
        Phone existedProduct = cart.getCartItems().get(0).getPhone();
        when(phoneDao.get(existedProduct.getId())).thenReturn(Optional.of(existedProduct));
        cartService.addPhone(cart, existedProduct.getId(), quantityExistedPhone);

        Method recalculateCart = HttpSessionCartService.class.getDeclaredMethod("recalculateCart", Cart.class);
        recalculateCart.setAccessible(true);
        recalculateCart.invoke(cartService, cart);

        long totalCost = newPhone.getPrice().longValue() * quantityNewPhone
                + existedProduct.getPrice().longValue() * (quantityExistedPhone + quantityExistedPhoneBeforeAdding);

        assertEquals((long) cart.getTotalQuantity(), quantityNewPhone + quantityExistedPhone + quantityExistedPhoneBeforeAdding);
        assertEquals(cart.getTotalCost(), new BigDecimal(totalCost));
    }
}

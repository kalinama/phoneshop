package com.es.core.cart.service.impl;

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
import java.util.*;

import static org.junit.Assert.*;
import static org.mockito.ArgumentMatchers.anyLong;
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
        Optional<CartItem> cartItem = getCartItemByPhone(phone);

        assertTrue(cartItem.isPresent());
        assertEquals(cartItem.get().getQuantity(), quantity);
    }

    private Optional<CartItem> getCartItemByPhone(Phone phone){
         return cart.getCartItems().stream()
                .filter(item -> item.getPhone().equals(phone))
                .findFirst();
    }

    @Test(expected = IllegalArgumentException.class)
    public void addNewPhoneThrowIllegalArgumentException() {
        cartService.addPhone(cart,1L, 0L);
    }

    @Test(expected = PhoneNotFoundException.class)
    public void addNewPhoneThrowPhoneNotFoundException() {
        when(phoneDao.get(anyLong())).thenReturn(Optional.empty());
        cartService.addPhone(cart, anyLong(), 1L);
    }

    @Test
    public void addExistedPhoneToCartTest() {
        CartItem cartItem = cart.getCartItems().get(0);
        Long quantityBeforeAdding = cartItem.getQuantity();
        Long quantity = 3L;

        when(phoneDao.get(anyLong())).thenReturn(Optional.of(cartItem.getPhone()));
        cartService.addPhone(cart, anyLong(), quantity);

        assertEquals((long) cartItem.getQuantity(), quantity + quantityBeforeAdding);
    }

    @Test
    public void updateCartSuccessTest() {
        Long quantity1 = 2L;
        Long quantity2 = 6L;

        CartItem cartItem1 = cart.getCartItems().get(0);
        CartItem cartItem2 = new CartItem(new Phone(45L, new BigDecimal(200)), 1L);
        cart.getCartItems().add(cartItem2);

        Map<Long, Long> map = new HashMap<>();
        map.put(cartItem1.getPhone().getId(), quantity1);
        map.put(cartItem2.getPhone().getId(), quantity2);

        cartService.update(cart, map);

        assertEquals(cartItem1.getQuantity(), quantity1);
        assertEquals(cartItem2.getQuantity(), quantity2);
    }

    @Test(expected = IllegalArgumentException.class)
    public void updateCartPhoneWithThisIdNotExistTest() {
        Map<Long, Long> map = Collections.singletonMap(100L, 5L);
        cartService.update(cart, map);
    }

    @Test
    public void removeExistedCartItemTest() {
        int oldSize = cart.getCartItems().size();
        CartItem testCartItem = cart.getCartItems().get(0);
        Phone testPhone = testCartItem.getPhone();

        cartService.remove(cart, testPhone.getId());
        assertFalse(cart.getCartItems().contains(testCartItem));
        assertNotEquals(oldSize, cart.getCartItems().size());
    }

    @Test
    public void removeNotExistedCartItemTest() {
        int oldSize = cart.getCartItems().size();

        cartService.remove(cart, 0L);
        assertEquals(oldSize, cart.getCartItems().size());
    }

    @Test
    public void recalculateCartTest() throws NoSuchMethodException, InvocationTargetException, IllegalAccessException, OutOfStockException {
        long quantityNewPhone = 3;
        Phone newPhone = new Phone(10L, new BigDecimal(134));
        when(phoneDao.get(newPhone.getId())).thenReturn(Optional.of(newPhone));
        cartService.addPhone(cart, newPhone.getId(), quantityNewPhone);

        long quantityExistedPhone = cart.getCartItems().get(0).getQuantity();
        Phone existedPhone = cart.getCartItems().get(0).getPhone();

        long totalCost = newPhone.getPrice().longValue() * quantityNewPhone
                + existedPhone.getPrice().longValue() * quantityExistedPhone;

        recalculateCart();

        assertEquals((long) cart.getTotalQuantity(), quantityNewPhone + quantityExistedPhone);
        assertEquals(cart.getTotalCost(), new BigDecimal(totalCost));
    }

    private void recalculateCart() throws NoSuchMethodException, InvocationTargetException, IllegalAccessException {
        Method recalculateCart = HttpSessionCartService.class.getDeclaredMethod("recalculateCart", Cart.class);
        recalculateCart.setAccessible(true);
        recalculateCart.invoke(cartService, cart);
    }
}

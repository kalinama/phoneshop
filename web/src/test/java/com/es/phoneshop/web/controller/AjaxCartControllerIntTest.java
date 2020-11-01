package com.es.phoneshop.web.controller;

import com.es.core.cart.entity.Cart;
import com.es.core.cart.service.CartService;
import com.es.phoneshop.web.validator.InputForAddToCartValidator;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.validation.Validator;
import org.springframework.web.servlet.view.InternalResourceViewResolver;

import javax.servlet.http.HttpSession;
import java.lang.reflect.Field;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@RunWith(MockitoJUnitRunner.class)
public class AjaxCartControllerIntTest {
    @Mock
    private CartService cartService;
    @Mock
    private Validator validator;
    @InjectMocks
    private AjaxCartController ajaxCartController;

    private MockMvc mockMvc;

    @Before
    public void setup() {
        InternalResourceViewResolver viewResolver = new InternalResourceViewResolver();
        viewResolver.setPrefix("/WEB-INF/jsp/");
        viewResolver.setSuffix(".jsp");

        this.mockMvc = MockMvcBuilders.standaloneSetup(ajaxCartController)
                .setViewResolvers(viewResolver).build();
    }

    @Test
    public void addPhoneWithValidDataSuccessTest() throws Exception {
        when(cartService.getCart(any(HttpSession.class))).thenReturn(new Cart());
        String phoneId = "1";
        String quantity = "2";
        String content = "{\"phoneId\": \"" + phoneId + "\", \"quantity\": \"" + quantity + "\"}";

        mockMvc.perform(MockMvcRequestBuilders.post("/ajaxCart")
                .content(content)
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk());

        verify(validator).validate(any(), any());
        verify(cartService).getCart(any(HttpSession.class));
        verify(cartService).addPhone(any(), any(), any());
    }

    @Test
    public void addPhoneWithValidDataErrorTest() throws Exception {
        Validator validator = new InputForAddToCartValidator();
        Field field = AjaxCartController.class.getDeclaredField("quantityValidator");
        field.setAccessible(true);
        field.set(ajaxCartController, validator);

        String phoneId = "1";
        String quantity = "ll";
        String content = "{\"phoneId\": \"" + phoneId + "\", \"quantity\": \"" + quantity + "\"}";

        mockMvc.perform(MockMvcRequestBuilders.post("/ajaxCart")
                .content(content)
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk());

        verify(cartService, never()).getCart(any(HttpSession.class));
        verify(cartService, never()).addPhone(any(), any(), any());
    }

    @Test
    public void addPhoneWithInvalidDataTest() throws Exception {
        mockMvc.perform(MockMvcRequestBuilders.post("/ajaxCart")
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isBadRequest());
    }

    @Test
    public void getCartTest() throws Exception {
        when(cartService.getCart(any(HttpSession.class))).thenReturn(new Cart());
        mockMvc.perform(MockMvcRequestBuilders.get("/ajaxCart")
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk());
    }

}

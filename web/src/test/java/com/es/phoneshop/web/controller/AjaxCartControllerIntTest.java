package com.es.phoneshop.web.controller;

import com.es.core.cart.entity.Cart;
import com.es.core.cart.service.CartService;
import com.es.phoneshop.web.controller.helper.BindingResultHelper;
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
import org.springframework.validation.BindingResult;
import org.springframework.web.servlet.view.InternalResourceViewResolver;

import javax.servlet.http.HttpSession;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@RunWith(MockitoJUnitRunner.class)
public class AjaxCartControllerIntTest {
    @Mock
    private CartService cartService;
    @Mock
    private BindingResultHelper bindingResultHelper;
    @Mock
    private BindingResult bindingResult;
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
        String phoneId = "1";
        String quantity = "2";

        when(cartService.getCart(any(HttpSession.class))).thenReturn(new Cart());
        when(bindingResultHelper.getBindingResult(any(), any())).thenReturn(bindingResult);
        when(bindingResult.hasErrors()).thenReturn(false);

        mockMvc.perform(MockMvcRequestBuilders.post("/ajaxCart")
                .param("phoneId", phoneId)
                .param("quantity", quantity))
                .andExpect(status().isOk());

        verify(cartService).getCart(any(HttpSession.class));
        verify(cartService).addPhone(any(), any(), any());
    }

    @Test
    public void addPhoneWithValidDataErrorTest() throws Exception {
        String phoneId = "1";
        String quantity = "ll";

        when(bindingResultHelper.getBindingResult(any(), any())).thenReturn(bindingResult);
        when(bindingResult.hasErrors()).thenReturn(true);

        mockMvc.perform(MockMvcRequestBuilders.post("/ajaxCart")
                .param("phoneId", phoneId)
                .param("quantity", quantity))
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

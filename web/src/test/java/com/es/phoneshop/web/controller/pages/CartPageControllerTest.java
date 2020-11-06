package com.es.phoneshop.web.controller.pages;

import com.es.core.cart.entity.Cart;
import com.es.core.cart.service.CartService;
import com.es.phoneshop.web.controller.helper.BindingResultHelper;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.validation.BindingResult;
import org.springframework.web.servlet.view.InternalResourceViewResolver;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@RunWith(MockitoJUnitRunner.class)
public class CartPageControllerTest {
    @Mock
    private CartService cartService;
    @Mock
    private BindingResultHelper bindingResultHelper;
    @Mock
    private BindingResult bindingResult;
    @InjectMocks
    private CartPageController cartPageController;

    private MockMvc mockMvc;

    @Before
    public void setup() {
        InternalResourceViewResolver viewResolver = new InternalResourceViewResolver();
        viewResolver.setPrefix("/WEB-INF/jsp/");
        viewResolver.setSuffix(".jsp");

        this.mockMvc = MockMvcBuilders.standaloneSetup(cartPageController)
                .setViewResolvers(viewResolver).build();
    }

    @Test
    public void returnIndexJSPViewNameGetCartTest() throws Exception {
        mockMvc.perform(MockMvcRequestBuilders.get("/cart"))
                .andExpect(MockMvcResultMatchers.view().name("cart"));
    }

    @Test
    public void checkModelAttributesGetCartTest() throws Exception {
        Cart cart = new Cart();
        when(cartService.getCart(any())).thenReturn(cart);

        mockMvc.perform(MockMvcRequestBuilders.get("/cart"))
                .andExpect(MockMvcResultMatchers.model().attribute("cart", cart));
    }

    @Test
    public void deleteCartItemTest() throws Exception {
        doNothing().when(cartService).remove(any(), anyLong());
        mockMvc.perform(MockMvcRequestBuilders.delete("/cart/" + anyLong()))
                .andExpect(MockMvcResultMatchers.redirectedUrl("/cart"));
        verify(cartService).remove(any(), anyLong());
    }


    @Test
    public void updateCartSuccessTest() throws Exception {
        when(bindingResultHelper.getBindingResult(any(), any())).thenReturn(bindingResult);
        when(bindingResult.hasErrors()).thenReturn(false);
        doNothing().when(cartService).update(any(), any());

        getInitResultActionForUpdate()
                .andExpect(MockMvcResultMatchers.model().attributeDoesNotExist("errors"));

        verify(cartService).update(any(), any());
    }

    @Test
    public void updateCartErrorTest() throws Exception {
        when(bindingResultHelper.getBindingResult(any(), any())).thenReturn(bindingResult);
        when(bindingResult.hasErrors()).thenReturn(true);
        when(bindingResultHelper.getErrorMessage(bindingResult)).thenReturn(anyString());

        getInitResultActionForUpdate()
                .andExpect(MockMvcResultMatchers.model().attributeExists("errors"));

        verify(cartService, never()).update(any(), any());
    }

    private ResultActions getInitResultActionForUpdate() throws Exception {
       return mockMvc.perform(MockMvcRequestBuilders.put("/cart")
                .param("quantity", "1")
                .param("phoneId", "1000"))
                .andExpect(MockMvcResultMatchers.view().name("cart"));
    }

}
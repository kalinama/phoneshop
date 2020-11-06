package com.es.phoneshop.web.controller.pages;

import com.es.core.phone.dao.PhoneDao;
import com.es.core.phone.entity.Phone;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.servlet.view.InternalResourceViewResolver;

import java.util.Optional;

import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.model;

@RunWith(MockitoJUnitRunner.class)
public class ProductDetailsPageControllerTest{

    @Mock
    private PhoneDao phoneDao;
    @InjectMocks
    private ProductDetailsPageController productDetailsPageController;

    private MockMvc mockMvc;

    @Before
    public void setup() {
        InternalResourceViewResolver viewResolver = new InternalResourceViewResolver();
        viewResolver.setPrefix("/WEB-INF/jsp/");
        viewResolver.setSuffix(".jsp");

        this.mockMvc = MockMvcBuilders.standaloneSetup(productDetailsPageController)
                .setViewResolvers(viewResolver).build();
    }

    @Test
    public void returnIndexJSPViewNameShowProductDetailsSuccessTest() throws Exception {
        long id = 1;
        when(phoneDao.get(id)).thenReturn(Optional.of(new Phone()));

        mockMvc.perform(MockMvcRequestBuilders.get("/productDetails/" + id))
                .andExpect(MockMvcResultMatchers.view().name("productDetails"));
    }

    @Test
    public void returnIndexJSPViewNameShowProductDetailsErrorTest() throws Exception {
        long id = 1;
        when(phoneDao.get(id)).thenReturn(Optional.empty());

        mockMvc.perform(MockMvcRequestBuilders.get("/productDetails/" + id))
                .andExpect(MockMvcResultMatchers.view().name("/errors/productNotFoundException"));
    }

    @Test
    public void checkModelAttributesShowProductDetailsSuccessTest() throws Exception {
        long id = 1;
        Phone phone = new Phone();
        when(phoneDao.get(id)).thenReturn(Optional.of(phone));

        mockMvc.perform(MockMvcRequestBuilders.get("/productDetails/" + id))
                .andExpect(model().attribute("phone", phone));
    }

    @Test
    public void checkModelAttributesShowProductDetailsErrorTest() throws Exception {
        long id = 1;
        when(phoneDao.get(id)).thenReturn(Optional.empty());

        mockMvc.perform(MockMvcRequestBuilders.get("/productDetails/" + id))
                .andExpect(model().attribute("id", id));
    }
}
package com.es.phoneshop.web.controller.pages;

import com.es.core.phone.dao.PhoneDao;
import com.es.core.phone.entity.Phone;
import com.es.core.phone.enums.SortOrder;
import com.es.core.phone.enums.SortParameter;
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

import java.lang.reflect.Method;
import java.util.Collections;
import java.util.List;

import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.model;

@RunWith(MockitoJUnitRunner.class)
public class ProductListPageControllerIntTest {
    @Mock
    private PhoneDao phoneDao;
    @InjectMocks
    private ProductListPageController productListPageController;

    private MockMvc mockMvc;

    @Before
    public void setup() {
        InternalResourceViewResolver viewResolver = new InternalResourceViewResolver();
        viewResolver.setPrefix("/WEB-INF/jsp/");
        viewResolver.setSuffix(".jsp");

        this.mockMvc = MockMvcBuilders.standaloneSetup(productListPageController)
                .setViewResolvers(viewResolver).build();
    }

    @Test
    public void returnIndexJSPViewNameTest() throws Exception {
        mockMvc.perform(MockMvcRequestBuilders.get("/productList"))
                .andExpect(MockMvcResultMatchers.view().name("productList"));
    }

    @Test
    public void checkModelAttributesTest() throws Exception {
        mockMvc.perform(MockMvcRequestBuilders.get("/productList"))
                .andExpect(model().attributeExists("phones", "maxPageNumber"));
    }

  @Test
    public void checkModelAttributeMaxPageNumberTest() throws Exception {
        int phonesQuantity = 3;
        Method getMaxPageQuantity = ProductListPageController.class.getDeclaredMethod("getMaxPageQuantity", int.class);
        getMaxPageQuantity.setAccessible(true);
        int maxPageQuantity = (Integer) getMaxPageQuantity.invoke(productListPageController, phonesQuantity);
        when(phoneDao.getQuantity(anyString())).thenReturn(phonesQuantity);

        mockMvc.perform(get("/productList").param("query", anyString()))
                .andExpect(model().attribute("maxPageNumber", maxPageQuantity));
    }

    @Test
    public void checkModelAttributePhoneTest() throws Exception {
        List<Phone> phones = Collections.singletonList(new Phone());
        when(phoneDao.findAll(anyInt(), anyInt(), anyString(), any(SortParameter.class), any(SortOrder.class)))
                .thenReturn(phones);

        mockMvc.perform(get("/productList").param("query", anyString()))
                .andExpect(model().attribute("phones", phones));
    }
}

package com.dbalthassat.restapi.controller;

import com.dbalthassat.restapi.config.Application;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.SpringApplicationConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.web.WebAppConfiguration;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.web.context.WebApplicationContext;

import static org.junit.Assert.assertNotEquals;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.setup.MockMvcBuilders.webAppContextSetup;

@RunWith(SpringJUnit4ClassRunner.class)
@SpringApplicationConfiguration(classes = Application.class)
@WebAppConfiguration
public class GreetingControllerTest {
    private MockMvc mockMvc;

    @Autowired
    private WebApplicationContext webApplicationContext;

    @Before
    public void setUp() {
        this.mockMvc = webAppContextSetup(webApplicationContext).build();
    }

    @Test
    public void testPretiffy() throws Exception {
        for(int i = 0; i < 100; ++i) {
            String resultNotPretiffied = "[{\"id\":1,\"name\":\"world\"},{\"id\":2,\"name\":\"tata\"},{\"id\":3," +
                    "\"name\":\"toto\",\"description\":\"A small description\"},{\"id\":4,\"name\":\"titi\"}]";
            mockMvc.perform(
                get("/greetings")
            ).andExpect(
                content().string(resultNotPretiffied)
            );
            MvcResult result = mockMvc.perform(
                    get("/greetings?pretiffy=true")
            ).andReturn();
            assertNotEquals(result.getResponse().getContentAsString(), resultNotPretiffied);
        }
    }
}

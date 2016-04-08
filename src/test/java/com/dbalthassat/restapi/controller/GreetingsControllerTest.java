package com.dbalthassat.restapi.controller;

import com.dbalthassat.restapi.config.Application;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.slf4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.SpringApplicationConfiguration;
import org.springframework.http.MediaType;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.web.WebAppConfiguration;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.web.context.WebApplicationContext;

import java.util.concurrent.atomic.AtomicInteger;

import static org.junit.Assert.assertEquals;
import static org.slf4j.LoggerFactory.getLogger;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.setup.MockMvcBuilders.webAppContextSetup;

@RunWith(SpringJUnit4ClassRunner.class)
@SpringApplicationConfiguration(classes = Application.class)
@WebAppConfiguration
public class GreetingsControllerTest {
    private static final Logger LOGGER = getLogger(GreetingsControllerTest.class);

    private MockMvc mockMvc;

    @Autowired
    private WebApplicationContext webApplicationContext;

    @Before
    public void setUp() {
        this.mockMvc = webAppContextSetup(webApplicationContext).build();
    }

    /**
     * Vérifie que l'utilisation du header {@code Accept: application/json+pretty} permet d'afficher la réponse du
     * serveur lisible telle quelle. Pour cela, on vérifie simplement qu'il y a une différence de contenu entre un appel
     * get standard et un appel get avec le header.
     *
     * Au vu de l'implémentation, on lance plusieurs threads qui exécutent plusieurs requêtes en parallèle pour s'assurer
     * que le résultat est toujours cohérent et qu'on récupère bien le résultat attendu.
     *
     * @throws Exception
     */
    @Test
    public void testPretty() throws Exception {
        Thread[] notPretty = new Thread[10];
        Thread[] pretty = new Thread[10];
        String resultNotPretty = "{\"content\":[{\"id\":1},{\"id\":2},{\"id\":3},{\"id\":4},{\"id\":5}]," +
                "\"last\":true,\"totalElements\":5,\"totalPages\":1,\"size\":10,\"number\":0," +
                "\"first\":true,\"numberOfElements\":5}";
        AtomicInteger errors = new AtomicInteger();
        AtomicInteger requests = new AtomicInteger();
        for(int i = 0; i < 10; ++i) {
            notPretty[i] = new Thread(() -> {
                for(int j = 0; j < 100; ++j) {
                    requests.incrementAndGet();
                    try {
                        MvcResult result = mockMvc.perform(
                                get("/greetings?fields=id")
                        ).andReturn();
                        if(!resultNotPretty.equals(result.getResponse().getContentAsString())) {
                            errors.incrementAndGet();
                        }
                    } catch(Exception e) {
                        LOGGER.error(e.getMessage(), e);
                    }
                }
            });
            pretty[i] = new Thread(() -> {
                for(int j = 0; j < 100; ++j) {
                    requests.incrementAndGet();
                    try {
                        MvcResult result = mockMvc.perform(
                                get("/greetings?fields=id")
                                .accept(MediaType.APPLICATION_JSON + "+pretty")
                        ).andReturn();
                        if(resultNotPretty.equals(result.getResponse().getContentAsString())) {
                            errors.incrementAndGet();
                        }
                    } catch(Exception e) {
                        LOGGER.error(e.getMessage(), e);
                    }
                }
            });
            notPretty[i].start();
            pretty[i].start();
        }
        for(int i = 0; i < 10; ++i) {
            notPretty[i].join();
            pretty[i].join();
        }
        assertEquals("The result is not acceptable. " + errors.get() + " errors for " + requests.get() + " requests.", 0, errors.get());
    }
}

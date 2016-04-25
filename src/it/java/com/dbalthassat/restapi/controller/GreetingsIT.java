package com.dbalthassat.restapi.controller;

import com.dbalthassat.restapi.config.Application;
import com.dbalthassat.restapi.dao.ExceptionDao;
import com.dbalthassat.restapi.dao.ValidationExceptionDao;
import com.dbalthassat.restapi.entity.Greetings;
import com.dbalthassat.restapi.entity.Pageable;
import com.dbalthassat.restapi.exception.ExceptionValues;
import com.dbalthassat.restapi.repository.GreetingsRepository;
import com.dbalthassat.restapi.utils.JacksonUtils;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.slf4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.SpringApplicationConfiguration;
import org.springframework.http.MediaType;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.web.WebAppConfiguration;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.request.MockHttpServletRequestBuilder;
import org.springframework.util.StringUtils;
import org.springframework.web.context.WebApplicationContext;

import java.util.LinkedList;
import java.util.List;
import java.util.concurrent.atomic.AtomicInteger;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;
import static org.slf4j.LoggerFactory.getLogger;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.springframework.test.web.servlet.setup.MockMvcBuilders.webAppContextSetup;

/**
 * Tests d'intégration sur le endpoint /greetings
 */
@SuppressWarnings("unchecked")
@RunWith(SpringJUnit4ClassRunner.class)
@SpringApplicationConfiguration(classes = Application.class)
@WebAppConfiguration
@ActiveProfiles("test")
public class GreetingsIT {
    private static final Logger LOGGER = getLogger(GreetingsIT.class);

    /**
     * Compte le nombre d'éléments enregistrés en base de données
     */
    private static int count;

    private MockMvc mockMvc;

    @Autowired
    private WebApplicationContext webApplicationContext;

    @Autowired
    private GreetingsRepository greetingsRepository;

    @Before
    public void before() {
        setUp();
        initDatabase();
    }

    private void setUp() {
        this.mockMvc = webAppContextSetup(webApplicationContext).build();
    }

    private void initDatabase() {
        List<Greetings> greetings = new LinkedList<>();
        greetings.add(new Greetings("world"));
        greetings.add(new Greetings("tata"));
        greetings.add(new Greetings("toto", "A small description"));
        greetings.add(new Greetings("a", "B"));
        greetings.add(new Greetings("titi"));
        greetingsRepository.save(greetings);
        count += 5;
    }

    /**
     * Teste le retour du endpoint GET /greetings
     *
     * Cet endpoint doit renvoyer la liste de tous les greetings
     *
     * @throws Exception
     */
    @Test
    public void testGetAll() throws Exception {
        MvcResult result = mockMvc.perform(get("/greetings?fields=id")).andReturn();
        Pageable<Greetings> response = JacksonUtils.getResponse(result, GreetingsPageable.class);
        assertEquals(count, response.getTotalElements());
    }

    /**
     * Teste le retour du endpoint GET /greetings/{id} avec une id valide et qui correspond à un objet
     *
     * Le code retour de la réponse doit être 200
     *
     * @throws Exception
     */
    @Test
    public void testGetOne() throws Exception {
        MvcResult result = mockMvc.perform(get("/greetings/1")).andExpect(status().isOk()).andReturn();
        Greetings response = JacksonUtils.getResponse(result, Greetings.class);
        assertEquals(Long.valueOf(1), response.getId());
        assertEquals("world", response.getName());
        assertTrue(StringUtils.isEmpty(response.getDescription()));
        assertEquals(response.getCreatedAt(), response.getUpdatedAt());
    }

    /**
     * Teste le retour du endpoint GET /greetings/{id} avec une id valide mais qui ne correspond à aucun objet
     *
     * Le code retour de la réponse doit être 404
     *
     * @throws Exception
     */
    @Test
    public void testGetOneNoResult() throws Exception {
        mockMvc.perform(get("/greetings/1437894")).andExpect(status().isNotFound());
    }

    /**
     * Teste le retour du endpoint GET /greetings/{id} avec une id invalide
     *
     * Le code retour de la réponse doit être 400
     *
     * @throws Exception
     */
    @Test
    public void testGetOneWithBadId() throws Exception {
        mockMvc.perform(get("/greetings/foo")).andExpect(status().isBadRequest());
    }

    /**
     * Teste le retour du endpoint POST /greetings avec un corps vide
     *
     * Le code retour de la réponse doit être 400
     *
     * @throws Exception
     */
    @Test
    public void testPostWithEmptyBody() throws Exception {
        MvcResult result = mockMvc.perform(createPostRequest("/greetings/")).andExpect(status().isBadRequest()).andReturn();
        ExceptionDao response = JacksonUtils.getResponse(result, ExceptionDao.class);
        assertEquals(ExceptionValues.REQUEST_BODY_IS_MISSING.getMessage(), response.getMessage());
        assertEquals(ExceptionValues.REQUEST_BODY_IS_MISSING.getCode(), response.getCode());
    }

    /**
     * Teste le retour du endpoint POST /greetings avec un corps non valide ({})
     *
     * Le code retour de la réponse doit être 400
     *
     * @throws Exception
     */
    @Test
    public void testPostWithInvalidBody() throws Exception {
        Greetings greetings = new Greetings();
        MockHttpServletRequestBuilder request = createPostRequest("/greetings").content(JacksonUtils.parse(greetings));
        MvcResult result = mockMvc.perform(request).andExpect(status().isUnprocessableEntity()).andReturn();
        ValidationExceptionDao response = JacksonUtils.getResponse(result, ValidationExceptionDao.class);
        assertEquals(ExceptionValues.VALIDATION.getMessage(), response.getMessage());
        assertEquals(ExceptionValues.VALIDATION.getCode(), response.getCode());
    }

    /**
     * Teste le retour du endpoint POST /greetings avec un corps valide ({"name":"toto","description":"test"}). Vérifie
     * également que la ressource a bien été créée
     *
     * Le code retour de la réponse doit être 400
     *
     * @throws Exception
     */
    @Test
    public void testPostWithValidBody() throws Exception {
        Greetings greetings = new Greetings();
        greetings.setName("toto");
        greetings.setDescription("description");
        MockHttpServletRequestBuilder postRequest = createPostRequest("/greetings").content(JacksonUtils.parse(greetings));
        MvcResult postResult = mockMvc.perform(postRequest).andExpect(status().isCreated()).andReturn();
        Greetings postResponse = JacksonUtils.getResponse(postResult, Greetings.class);
        assertEquals(String.format(GreetingsController.TEMPLATE, "toto"), postResponse.getName());
        assertEquals("description", postResponse.getDescription());
        MockHttpServletRequestBuilder getRequest = get("/greetings/" + postResponse.getId());
        MvcResult getResult = mockMvc.perform(getRequest).andExpect(status().isOk()).andReturn();
        Greetings getResponse = JacksonUtils.getResponse(getResult, Greetings.class);
        assertEquals(postResponse, getResponse);
    }

    /**
     * Crée une requête post avec un corps vide et le header Content-Type égal à application/json
     *
     * @param req le endpoint de la requête post à créer
     * @return la requête post
     */
    private MockHttpServletRequestBuilder createPostRequest(String req) {
        return post(req)
                .contentType(MediaType.APPLICATION_JSON);
    }

    /**
     * Vérifie que l'utilisation du header {@code Accept: application/json+pretty} permet d'afficher la réponse du
     * serveur lisible telle quelle. Pour cela, on vérifie simplement qu'il y a une différence de contenu entre un appel
     * get standard et un appel get avec le header.
     *
     * Au vu de l'implémentation, on lance plusieurs threads qui exécutent plusieurs requêtes en parallèle pour s'assurer
     * que le résultat est toujours cohérent et qu'on récupère bien le résultat attendu.
     *
     * @throws InterruptedException
     */
    @Test
    public void testPretty() throws InterruptedException {
        Thread[] notPretty = new Thread[10];
        Thread[] pretty = new Thread[10];
        String resultNotPretty = "{\"id\":1}";
        AtomicInteger errors = new AtomicInteger();
        AtomicInteger requests = new AtomicInteger();
        for(int i = 0; i < 10; ++i) {
            notPretty[i] = new Thread(() -> {
                for(int j = 0; j < 10; ++j) {
                    requests.incrementAndGet();
                    try {
                        MvcResult result = mockMvc.perform(
                                get("/greetings/1?fields=id")
                        ).andReturn();
                        if(!result.getResponse().getContentAsString().contains(resultNotPretty)) {
                            errors.incrementAndGet();
                        }
                    } catch(Exception e) {
                        LOGGER.error(e.getMessage(), e);
                    }
                }
            });
            pretty[i] = new Thread(() -> {
                for(int j = 0; j < 10; ++j) {
                    requests.incrementAndGet();
                    try {
                        MvcResult result = mockMvc.perform(
                                get("/greetings/1?fields=id")
                                        .accept(MediaType.APPLICATION_JSON + "+pretty")
                        ).andReturn();
                        if(result.getResponse().getContentAsString().contains(resultNotPretty)) {
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

    private static class GreetingsPageable extends Pageable<Greetings> {}
}


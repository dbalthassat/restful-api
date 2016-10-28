package com.dbalthassat.restapi.controller;

import com.dbalthassat.restapi.entity.Greetings;
import com.dbalthassat.restapi.exception.clientError.notFound.IdNotFoundException;
import com.dbalthassat.restapi.service.GreetingsService;
import org.junit.Test;
import org.mockito.Mockito;

import static org.junit.Assert.assertEquals;

/**
 * Tests unitaires sur la classe GreetingsController
 */
public class GreetingsControllerTest {
    private static final Greetings SAMPLE_GREETING = new Greetings("name", "description");

    /**
     * Teste la méthode get()
     * Si l'objet existe, doit le renvoyer tel quel
     */
    @Test
    public void testSimpleGet() {
        assertEquals(SAMPLE_GREETING, getMockedController().get(1L));
    }

    /**
     * Teste la méthode get()
     * Si l'objet n'existe pas, doit lancer une IdNotFoundException
     */
    @Test(expected = IdNotFoundException.class)
    public void testNotFound() {
        getMockedController().get(2L);
    }

    /**
     * Retourne un GreetingsController qui contient un GreetingsService mocké pour les tests.
     *
     * Ce service effectue les actions suivantes :
     * - findOne(1L) : renvoie {"name":"name", "description":"description"}
     * - findOne(2L) : lance une NotFoundException
     *
     * @return l'objet GreetingsController
     */
    private GreetingsController getMockedController() {
        GreetingsService service = Mockito.mock(GreetingsService.class);
        GreetingsController controller = new GreetingsController(service);
        Mockito.when(service.findOne(1L)).thenReturn(SAMPLE_GREETING);
        Mockito.when(service.findOne(2L)).thenThrow(new IdNotFoundException(2L));
        return controller;
    }
}

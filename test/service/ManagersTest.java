package service;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertNotNull;


@DisplayName("Менеджер")
class ManagersTest {

    Managers managers= new Managers();

    @Test
    @DisplayName("getDefaultHistory не равен @Null при возврате")
    void shouldGetDefaultHistoryNotNull(){
        assertNotNull(managers.getDefaultHistory());
    }

    @Test
    @DisplayName("getDefault не равен @Null при возврате")
    void shouldGetDefaultNotNull2(){
        assertNotNull(managers.getDefault());
    }

}
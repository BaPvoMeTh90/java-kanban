package service;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertNotNull;


@DisplayName("Менеджер")
class ManagersTest {

    @Test
    @DisplayName("getDefaultHistory не равен @Null при возврате")
    void shouldGetDefaultHistoryNotNull(){
        assertNotNull(Managers.getDefaultHistory());
    }

    @Test
    @DisplayName("getDefault не равен @Null при возврате")
    void shouldGetDefaultNotNull2(){
        assertNotNull(Managers.getDefault());
    }

}
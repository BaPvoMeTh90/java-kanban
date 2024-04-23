package service;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;


@DisplayName("Менеджер")
class ManagersTest {

    Managers managers= new Managers();

    @Test
    @DisplayName("не равен @Null при возврате")
    void shouldEqualsNotNull(){
        assertNotNull(managers);
    }

}
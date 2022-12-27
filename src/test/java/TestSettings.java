import Settings.Settings;
import org.junit.jupiter.api.*;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class TestSettings {
    Settings sut;
    @BeforeAll
    public static void initTests(){
        System.out.println("Начинаем тесты...");
    }
    @AfterAll
    public static void endTests(){
        System.out.println("Закончили наши тесты");
    }
    @BeforeEach
    public void initSetting(){
        sut = new Settings();
    }
    @AfterEach
    public void endSettings(){
        sut = null;
    }
    @Test
    public void getHost(){
        String expected = "localhost";
        String result = sut.getHost();
        assertEquals(expected, result);

    }
    @Test void getPort(){
        int expected = 6319;
        int result = sut.getPort();
        assertEquals(expected, result);
    }

    }


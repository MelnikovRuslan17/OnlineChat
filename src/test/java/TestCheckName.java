
import Server.Server;
import org.junit.jupiter.api.*;


import static org.junit.jupiter.api.Assertions.assertEquals;

public class TestCheckName {
    Server server;
    @BeforeAll
    public static void initTests(){
        System.out.println("Начинаем тесты...");
    }
    @AfterAll
    public static void endTests(){
        System.out.println("Закончили наши тесты ");
    }
    @BeforeEach
   public void initServer(){
        server = new Server();
    }
    @AfterEach
    public void endServer(){
        server = null;

    }
    @Test
    public void checkNameTest(){
        String name = "Руслан";
        Boolean expected = true;
        Boolean result = server.checkName(name);
        assertEquals(expected,result);
    }
}



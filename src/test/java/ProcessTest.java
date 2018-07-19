import org.junit.jupiter.api.Test;
import ru.cd.Command;
import ru.cd.Connection;

import java.io.IOException;

import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.mockito.Mockito.mock;

public class ProcessTest {

    @Test
    void simpleTest() {
        Connection connection = mock(Connection.class);
        Command command = new Command(connection, "echo test");
        command.run();
        assertDoesNotThrow(command::close);
    }


    @Test
    void severalTasks() throws IOException {
        Connection connection = mock(Connection.class);
        Command command = new Command(connection, "find /home/pekar/Soft/android-studio");
        command.run();
    }


    @Test
    void longTest() {
        Connection connection = mock(Connection.class);
        Command command = new Command(connection, "find /home/pekar/Soft");
        new Thread(() -> {
            while (true) {
                System.out.println(command.isAlive());
                assertDoesNotThrow(() -> Thread.sleep(5000));
                if (!command.isAlive()) return;
            }
        }).start();
        command.run();
        assertDoesNotThrow(command::close);
    }
}

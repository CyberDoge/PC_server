import org.junit.jupiter.api.Test;
import ru.cd.Command;
import ru.cd.Connection;

import java.io.IOException;

import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.mockito.Mockito.mock;

public class ProcessTest {

    @Test
    void simpleTest() throws InterruptedException {
        Connection connection = mock(Connection.class);
        Command command = new Command(connection, "echo test");
        var thread = new Thread(command);
        thread.start();
        thread.join();
        assertDoesNotThrow(command::close);
    }


    @Test
    void severalTasks() throws IOException, InterruptedException {
        Connection connection = mock(Connection.class);
        var command1 = new Command(connection, "find /home/pekar/Soft/apache-tomcat-9.0.10");
        var thread1 = new Thread(command1);

        var command2 = new Command(connection, "ping -c 3 ya.ru");
        var thread2 = new Thread(command2);

        thread1.start();
        thread2.start();

        thread1.join();
        thread2.join();
        assertDoesNotThrow(command1::close);
        assertDoesNotThrow(command2::close);
    }

    @Test
    void runTaskWithSeveralInput() throws InterruptedException, IOException {
        Connection connection = mock(Connection.class);
        Command command = new Command(connection, "nano");
        var thread = new Thread(command);
        thread.start();
        thread.join();
        assertDoesNotThrow(command::close);
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

    @Test
    void getExitCode() throws InterruptedException {
        Connection connection = mock(Connection.class);
        Command command = new Command(connection, "ping -c 3 ya.ru");
        var thread = new Thread(command);
        thread.start();
        thread.join();
        System.out.println("2 = " + command.getExitCode());
        assertDoesNotThrow(command::close);
    }
}

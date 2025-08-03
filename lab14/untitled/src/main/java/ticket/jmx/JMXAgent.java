package ticket.jmx;

import javax.management.*;
import java.lang.management.ManagementFactory;

public class JMXAgent {
    public static void registerMBean() {
        try {
            MBeanServer server = ManagementFactory.getPlatformMBeanServer();
            ObjectName name = new ObjectName("ticket:type=TicketSystemManager");
            TicketSystemManager mbean = new TicketSystemManager();
            server.registerMBean(mbean, name);
            System.out.println("Zarejestrowano ziarenko JMX");
        } catch (Exception e) {
            e.printStackTrace();
        }
        System.out.println("✔️ Zarejestrowano ziarenko JMX!");
    }

}

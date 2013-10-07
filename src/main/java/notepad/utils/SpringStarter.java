package notepad.utils;

import org.apache.log4j.Logger;
import org.springframework.context.support.FileSystemXmlApplicationContext;

/**
 * Evgeny Vanslov
 * vans239@gmail.com
 */
public class SpringStarter {
    private static final Logger log = Logger.getLogger(SpringStarter.class);

    public static void main(String[] args) {
        log.info("Module started");
        final FileSystemXmlApplicationContext context = new FileSystemXmlApplicationContext("src/main/resources/beans.xml");
    }
}

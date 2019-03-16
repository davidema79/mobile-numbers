package eu.davidemartorana.mobile.numbers;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class ApplicationLauncher {

	private static final Logger LOGGER = LoggerFactory.getLogger(ApplicationLauncher.class);

	
	public static void main(String[] args) {
		
		LOGGER.info("############################################################################################");
		LOGGER.info("###########                 APPLICATION MOBILE NUMBERS STARTING....              ###########");
		LOGGER.info("############################################################################################");
		
		try {
			SpringApplication.run(ApplicationLauncher.class, args);
		} catch (final Exception e) {
			// Avoid zombies process
			LOGGER.info("############################################################################################################");
			LOGGER.info("###########             ....APPLICATION MOBILE NUMBERS FAILED TO START           ###########################");
			LOGGER.info("############################################################################################################");
			LOGGER.error(String.format("Exception: %s", e.getMessage()), e);
			
			System.exit(-1);
		}

		LOGGER.info("############################################################################################");
		LOGGER.info("###########                 .....APPLICATION MOBILE NUMBERS STARTED              ###########");
		LOGGER.info("############################################################################################");
	}

}

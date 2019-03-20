package eu.davidemartorana.mobile.numbers.filters;

import java.io.IOException;
import java.util.UUID;

import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;

import org.slf4j.MDC;
import org.springframework.stereotype.Component;

/**
 * Add Transaction info into the logging lines
 * 
 * @author davidemartorana
 *
 */
@Component
public class MDCRequestFilters implements Filter {

	@Override
	public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain)
			throws IOException, ServletException {

		final UUID transactionId = UUID.randomUUID();
		MDC.put("transactionId", transactionId.toString());
		chain.doFilter(request, response);
	}

}

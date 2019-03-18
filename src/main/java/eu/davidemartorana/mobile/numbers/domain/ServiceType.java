package eu.davidemartorana.mobile.numbers.domain;

import java.util.HashMap;
import java.util.Map;

import org.apache.commons.lang3.StringUtils;

/**
 * 
 * @author davidemartorana
 *
 */
public enum ServiceType {

	MOBILE_PREPAID("Prepaid"),
	MOBILE_POSTPAID("Postpaid");
	
	private static final Map<String, ServiceType> LOOKUP_MAP = new HashMap<>();
	
	static {
		for(final ServiceType item : ServiceType.values()) {
			LOOKUP_MAP.put(item.labelServiceValue.toLowerCase(), item);
		}
	}
	
	private final String labelServiceValue;
	
	private ServiceType(final String serviceValue) {
		this.labelServiceValue = serviceValue;
	}
	
	public static ServiceType fromLabelService(final String label) {
		if(StringUtils.isEmpty(label)) {
			throw new IllegalArgumentException("Label cannot be empty.");
		}
		final ServiceType type = LOOKUP_MAP.get(label.toLowerCase());
		if(type == null) {
			throw new IllegalArgumentException("Label not valid: " + label);
		}
		
		return type;
	}
	
	public String toLabelService() {
		return this.labelServiceValue;
	}
}

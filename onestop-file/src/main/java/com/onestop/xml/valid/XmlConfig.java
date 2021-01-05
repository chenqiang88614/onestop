package com.onestop.xml.valid;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

@Component
public class XmlConfig {

	public static String	configPath;
	public static String	validatePath;

	@Value("${Configuration.customer.xsd}")
	public void setConfigPath(String configPath) {
		XmlConfig.configPath = configPath + "/ima";
	}

	@Value("${Configuration.customer.xsd}")
	public void setValidatePath(String validatePath) {
		XmlConfig.validatePath = validatePath + "/valid";
	}
}

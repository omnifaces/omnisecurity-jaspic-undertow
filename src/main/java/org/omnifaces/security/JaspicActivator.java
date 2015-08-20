/*
 * Copyright 2015 OmniFaces.
 *
 * Licensed under the Apache License, Version 2.0 (the "License"); you may not use this file except in compliance with
 * the License. You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software distributed under the License is distributed on
 * an "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied. See the License for the
 * specific language governing permissions and limitations under the License.
 */

package org.omnifaces.security;

import java.lang.reflect.Field;
import java.util.logging.Level;
import java.util.logging.Logger;

import org.jboss.as.security.plugins.SecurityDomainContext;
import org.jboss.security.auth.login.JASPIAuthenticationInfo;
import org.jboss.security.config.ApplicationPolicy;
import org.jboss.security.config.SecurityConfiguration;
import org.wildfly.extension.undertow.security.JAASIdentityManagerImpl;
import org.wildfly.extension.undertow.security.jaspi.JASPIAuthenticationMechanism;
import org.wildfly.extension.undertow.security.jaspi.JASPICSecurityContextFactory;

import io.undertow.security.idm.IdentityManager;
import io.undertow.servlet.api.DeploymentInfo;

/**
 * This is a hack to activate JASPIC for JBoss WildFly 8.2+. It replaces the hack with the dummy domain
 * in <code>standalone.xml</code>:
 * 
 * <p>
 * <pre>
 *   &lt;security-domain name="other" cache-type="default"&gt;
 *       &lt;authentication-jaspi&gt;
 *          &lt;login-module-stack name="dummy"&gt;
 *              &lt;login-module code="Dummy" flag="optional"/&gt;
 *          &lt;/login-module-stack&gt;
 *          &lt;auth-module code="Dummy"/&gt;
 *       &lt;/authentication-jaspi&gt;
 *    &lt;/security-domain&gt;
 * </pre>
 * 
 * <p>
 * If/when a future version of JBoss/Undertow ever has JASPIC activated by default, this hack can be removed.
 *
 * 
 * @author Arjan Tijms
 *
 */
public class JaspicActivator {

	private static final Logger logger = Logger.getLogger(JaspicActivator.class.getName());

	public static DeploymentInfo activateJaspic(final DeploymentInfo deploymentInfo) {
		
		String securityDomain = "other";

		IdentityManager identityManager = deploymentInfo.getIdentityManager();
		if (identityManager instanceof JAASIdentityManagerImpl) {
			try {
				Field securityDomainContextField = JAASIdentityManagerImpl.class.getDeclaredField("securityDomainContext");
				securityDomainContextField.setAccessible(true);
				SecurityDomainContext securityDomainContext = (SecurityDomainContext) securityDomainContextField.get(identityManager);

				securityDomain = securityDomainContext.getAuthenticationManager().getSecurityDomain();

			} catch (NoSuchFieldException | SecurityException | IllegalArgumentException | IllegalAccessException e) {
				logger.log(Level.SEVERE, "Can't obtain name of security domain, using 'other' now", e);
			}
		}

		ApplicationPolicy applicationPolicy = new ApplicationPolicy(securityDomain);
		JASPIAuthenticationInfo authenticationInfo = new JASPIAuthenticationInfo(securityDomain);
		applicationPolicy.setAuthenticationInfo(authenticationInfo);
		SecurityConfiguration.addApplicationPolicy(applicationPolicy);

		deploymentInfo.setJaspiAuthenticationMechanism(new JASPIAuthenticationMechanism(securityDomain, null));
		deploymentInfo.setSecurityContextFactory(new JASPICSecurityContextFactory(securityDomain));
		
		return deploymentInfo;
	}

}

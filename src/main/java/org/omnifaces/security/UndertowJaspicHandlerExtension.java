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

import static org.omnifaces.security.JaspicActivator.activateJaspic;

import javax.servlet.ServletContext;

import io.undertow.servlet.ServletExtension;
import io.undertow.servlet.api.DeploymentInfo;

/**
 * Extension that calls code to activate JASPIC
 * 
 * @author Arjan Tijms
 *
 */
public class UndertowJaspicHandlerExtension implements ServletExtension {

	@Override
	public void handleDeployment(final DeploymentInfo deploymentInfo, final ServletContext servletContext) {
		activateJaspic(deploymentInfo);
	}
}
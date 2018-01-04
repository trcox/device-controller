/*******************************************************************************
 * Copyright 2016-2017 Dell Inc.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 *
 * @microservice: device-controller
 * @author: Tyler Cox, Dell
 * @version: 1.0.0
 *******************************************************************************/
package org.edgexfoundry.device.controller.impl;

import org.edgexfoundry.device.controller.StatusController;
import org.edgexfoundry.support.logging.client.EdgeXLogger;
import org.edgexfoundry.support.logging.client.EdgeXLoggerFactory;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/v1/ping")
public class StatusControllerImpl implements StatusController {

  private final EdgeXLogger logger =
      EdgeXLoggerFactory.getEdgeXLogger(this.getClass());
  
  public static final String PING_RESPONSE = "pong";

  @Override
  @RequestMapping(method = RequestMethod.GET)
  public @ResponseBody String ping() {
    logger.debug("Device service pinged - yes its up!");
    return PING_RESPONSE;
  }
}

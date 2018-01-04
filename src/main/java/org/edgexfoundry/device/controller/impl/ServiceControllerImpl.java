/*******************************************************************************
 * Copyright 2016-2017 Dell Inc.
 *
 * Licensed under the Apache License, Version 2.0 (the "License"); you may not use this file except
 * in compliance with the License. You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software distributed under the License
 * is distributed on an "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express
 * or implied. See the License for the specific language governing permissions and limitations under
 * the License.
 *
 * @microservice: device-controller
 * @author: Tyler Cox, Dell
 * @version: 1.0.0
 *******************************************************************************/
package org.edgexfoundry.device.controller.impl;

import org.edgexfoundry.device.controller.ServiceController;
import org.edgexfoundry.service.handler.ServiceHandler;
import org.edgexfoundry.service.transform.ObjectTransform;
import org.edgexfoundry.support.logging.client.EdgeXLogger;
import org.edgexfoundry.support.logging.client.EdgeXLoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/v1/")
public class ServiceControllerImpl implements ServiceController {

  private final EdgeXLogger logger =
      EdgeXLoggerFactory.getEdgeXLogger(this.getClass());

  @Autowired
  ObjectTransform transform;

  @Autowired
  ServiceHandler handler;

  @Override
  @RequestMapping(path = "/debug/transformData/{transformData}", method = RequestMethod.GET)
  public @ResponseBody String setTransformData(@PathVariable boolean transformData) {
    logger.info("Setting transform data to: " + transformData);
    transform.setTransformData(transformData);
    return "Set transform data to: " + transformData;
  }

  @Override
  @RequestMapping(path = "/discovery", method = RequestMethod.POST)
  public @ResponseBody String doDiscovery() {
    logger.info("Running discovery request");
    handler.scan();
    return "Running discovery";
  }
}

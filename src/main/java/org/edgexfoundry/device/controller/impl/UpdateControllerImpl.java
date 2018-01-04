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

import javax.servlet.http.HttpServletRequest;

import org.edgexfoundry.device.controller.UpdateController;
import org.edgexfoundry.domain.meta.ActionType;
import org.edgexfoundry.domain.meta.CallbackAlert;
import org.edgexfoundry.exception.controller.ClientException;
import org.edgexfoundry.exception.controller.NotFoundException;
import org.edgexfoundry.service.handler.SchedulerCallbackHandler;
import org.edgexfoundry.service.handler.UpdateHandler;
import org.edgexfoundry.support.logging.client.EdgeXLogger;
import org.edgexfoundry.support.logging.client.EdgeXLoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class UpdateControllerImpl implements UpdateController {

  private static final String PROV_WATCHER_TYPE = "ProvisionWatcher";
  private static final String DEVICE_TYPE = "Device";
  private static final String PROFILE_TYPE = "Profile";

  private final EdgeXLogger logger =
      EdgeXLoggerFactory.getEdgeXLogger(this.getClass());

  @Autowired
  UpdateHandler update;

  @Autowired
  private SchedulerCallbackHandler callbackHandler;

  @Override
  @RequestMapping("/${service.callback}")
  public void getCallback(HttpServletRequest request,
      @RequestBody(required = false) CallbackAlert data) {

    if (data != null) {
      ActionType actionType = data.getType();
      String id = data.getId();
      String method = request.getMethod();

      if (actionType == null || id == null || method == null) {
        throw new ClientException("Callback parameters were null");
      }

      switch (actionType) {
        case DEVICE:
          deviceUpdate(id, method);
          break;
        case PROFILE:
          if (method.equals("PUT")) {
            updateProfile(id);
          }
          break;
        case PROVISIONWATCHER:
          provisionWatcherUpdate(id, method);
          break;
        case SCHEDULE:
        case SCHEDULEEVENT:
          scheduleUpdate(data, method);
          break;
        default:
          break;
      }
    } else {
      logger.error("No data supplied to update controller");
    }
  }

  private void deviceUpdate(String id, String method) {
    switch (method) {
      case "POST":
        addDevice(id);
        break;
      case "PUT":
        updateDevice(id);
        break;
      case "DELETE":
        deleteDevice(id);
        break;
      default:
        break;
    }
  }

  private void provisionWatcherUpdate(String id, String method) {
    switch (method) {
      case "POST":
        addWatcher(id);
        break;
      case "PUT":
        updateWatcher(id);
        break;
      case "DELETE":
        deleteWatcher(id);
        break;
      default:
        break;
    }
  }

  private void scheduleUpdate(CallbackAlert data, String method) {
    switch (method) {
      case "POST":
        callbackHandler.handlePost(data);
        break;
      case "PUT":
        callbackHandler.handlePut(data);
        break;
      case "DELETE":
        callbackHandler.handleDelete(data);
        break;
      default:
        break;
    }
  }

  private void addWatcher(@RequestBody String provisionWatcher) {
    if (provisionWatcher != null) {
      if (update.addWatcher(provisionWatcher)) {
        logger.debug("New device watcher received to add devices with provision watcher id:"
            + provisionWatcher);
      } else {
        logger.error("Received add device provision watcher request without an id attached.");
        throw new NotFoundException(PROV_WATCHER_TYPE, provisionWatcher);
      }
    }
  }

  private void updateWatcher(@RequestBody String provisionWatcher) {
    if (provisionWatcher != null) {
      if (update.updateWatcher(provisionWatcher)) {
        logger.debug("Update device provision watcher with id:" + provisionWatcher);
      } else {
        logger.error("Received update device provision watcher request without an id attached.");
        throw new NotFoundException(PROV_WATCHER_TYPE, provisionWatcher);
      }
    }
  }

  private void deleteWatcher(@RequestBody String provisionWatcher) {
    if (provisionWatcher != null) {
      if (update.removeWatcher(provisionWatcher)) {
        logger.debug("Remove device provision watcher with id:" + provisionWatcher);
      } else {
        logger.error("Received remove device provision watcher request without an id attached.");
        throw new NotFoundException(PROV_WATCHER_TYPE, provisionWatcher);
      }
    }
  }

  private void addDevice(@RequestBody String deviceId) {
    if (deviceId != null) {
      if (update.addDevice(deviceId)) {
        logger.debug("Added device.  Received add device request with id:" + deviceId);
      } else {
        logger.error("Received add device request without a device id attached.");
        throw new NotFoundException(DEVICE_TYPE, deviceId);
      }
    }
  }

  private void updateDevice(@RequestBody String deviceId) {
    if (deviceId != null) {
      if (update.updateDevice(deviceId)) {
        logger.debug("Updated device. Received update device request with id:" + deviceId);
      } else {
        logger.error("Received update device request without a device id attached.");
        throw new NotFoundException(DEVICE_TYPE, deviceId);
      }
    }
  }

  private void deleteDevice(@RequestBody String deviceId) {
    if (deviceId != null) {
      if (update.deleteDevice(deviceId)) {
        logger.debug("Removing device. Received delete device request with id:" + deviceId);
      } else {
        logger.error("Received delete device request without a device id attached.");
        throw new NotFoundException(DEVICE_TYPE, deviceId);
      }
    }
  }

  private void updateProfile(@RequestBody String profileId) {
    if (profileId != null) {
      if (update.updateProfile(profileId)) {
        logger.debug("Updated profile. Received update profile request with id:" + profileId);
      } else {
        logger.error("Received update profile request without a profile id attached.");
        throw new NotFoundException(PROFILE_TYPE, profileId);
      }
    }
  }
}

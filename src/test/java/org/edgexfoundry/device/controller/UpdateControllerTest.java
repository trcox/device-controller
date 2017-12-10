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
 * @author: Jim White, Dell
 * @version: 1.0.0
 *******************************************************************************/

package org.edgexfoundry.device.controller;

import static org.mockito.Mockito.when;

import javax.servlet.http.HttpServletRequest;

import org.edgexfoundry.device.controller.impl.UpdateControllerImpl;
import org.edgexfoundry.domain.meta.ActionType;
import org.edgexfoundry.domain.meta.CallbackAlert;
import org.edgexfoundry.exception.controller.ClientException;
import org.edgexfoundry.exception.controller.NotFoundException;
import org.edgexfoundry.service.handler.SchedulerCallbackHandler;
import org.edgexfoundry.service.handler.UpdateHandler;
import org.edgexfoundry.test.category.RequiresNone;
import org.junit.Before;
import org.junit.Test;
import org.junit.experimental.categories.Category;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

@Category(RequiresNone.class)
public class UpdateControllerTest {

  private static final String TEST_ID = "test_id";

  @InjectMocks
  private UpdateControllerImpl controller;

  @Mock
  UpdateHandler update;

  @Mock
  private SchedulerCallbackHandler callbackHandler;

  @Mock
  private HttpServletRequest request;

  private CallbackAlert alert;

  @Before
  public void setup() {
    MockitoAnnotations.initMocks(this);
    alert = new CallbackAlert();
    alert.setType(ActionType.SCHEDULE);
    alert.setId(TEST_ID);
  }

  @Test
  public void testGetCallbackNullAlert() {
    when(request.getMethod()).thenReturn("GET");
    controller.getCallback(request, null);
  }

  @Test(expected = ClientException.class)
  public void testGetCallbackClientException() {
    when(request.getMethod()).thenReturn(null);
    alert.setId(null);
    alert.setType(null);
    controller.getCallback(request, alert);
  }

  @Test
  public void testGetCallbackForDevice() {
    alert.setType(ActionType.DEVICE);
    when(request.getMethod()).thenReturn("GET");
    controller.getCallback(request, alert);
  }

  @Test
  public void testGetCallbackForDevicePost() {
    alert.setType(ActionType.DEVICE);
    when(request.getMethod()).thenReturn("POST");
    when(update.addDevice(TEST_ID)).thenReturn(true);
    controller.getCallback(request, alert);
  }

  @Test
  public void testGetCallbackForDevicePut() {
    alert.setType(ActionType.DEVICE);
    when(request.getMethod()).thenReturn("PUT");
    when(update.updateDevice(TEST_ID)).thenReturn(true);
    controller.getCallback(request, alert);
  }

  @Test
  public void testGetCallbackForDeviceDelete() {
    alert.setType(ActionType.DEVICE);
    when(request.getMethod()).thenReturn("DELETE");
    when(update.deleteDevice(TEST_ID)).thenReturn(true);
    controller.getCallback(request, alert);
  }

  @Test(expected = NotFoundException.class)
  public void testGetCallbackForDevicePostNotFound() {
    alert.setType(ActionType.DEVICE);
    when(request.getMethod()).thenReturn("POST");
    when(update.addDevice(TEST_ID)).thenReturn(false);
    controller.getCallback(request, alert);
  }

  @Test(expected = NotFoundException.class)
  public void testGetCallbackForDevicePutNotFound() {
    alert.setType(ActionType.DEVICE);
    when(request.getMethod()).thenReturn("PUT");
    when(update.updateDevice(TEST_ID)).thenReturn(false);
    controller.getCallback(request, alert);
  }

  @Test(expected = NotFoundException.class)
  public void testGetCallbackForDeviceDeleteNotFound() {
    alert.setType(ActionType.DEVICE);
    when(request.getMethod()).thenReturn("DELETE");
    when(update.deleteDevice(TEST_ID)).thenReturn(false);
    controller.getCallback(request, alert);
  }

  @Test
  public void testGetCallbackForProfile() {
    alert.setType(ActionType.PROFILE);
    when(request.getMethod()).thenReturn("GET");
    controller.getCallback(request, alert);
  }

  @Test
  public void testGetCallbackForProfileWithPut() {
    alert.setType(ActionType.PROFILE);
    when(request.getMethod()).thenReturn("PUT");
    when(update.updateProfile(TEST_ID)).thenReturn(true);
    controller.getCallback(request, alert);
  }

  @Test(expected = NotFoundException.class)
  public void testGetCallbackForProfileWithPutNotFound() {
    alert.setType(ActionType.PROFILE);
    when(request.getMethod()).thenReturn("PUT");
    when(update.updateProfile(TEST_ID)).thenReturn(false);
    controller.getCallback(request, alert);
  }

  @Test
  public void testGetCallbackForProvisionWatcher() {
    alert.setType(ActionType.PROVISIONWATCHER);
    when(request.getMethod()).thenReturn("GET");
    controller.getCallback(request, alert);
  }

  @Test
  public void testGetCallbackForProvisionPost() {
    alert.setType(ActionType.PROVISIONWATCHER);
    when(request.getMethod()).thenReturn("POST");
    when(update.addWatcher(TEST_ID)).thenReturn(true);
    controller.getCallback(request, alert);
  }

  @Test
  public void testGetCallbackForProvisionPut() {
    alert.setType(ActionType.PROVISIONWATCHER);
    when(request.getMethod()).thenReturn("PUT");
    when(update.updateWatcher(TEST_ID)).thenReturn(true);
    controller.getCallback(request, alert);
  }

  @Test
  public void testGetCallbackForProvisionDelete() {
    alert.setType(ActionType.PROVISIONWATCHER);
    when(request.getMethod()).thenReturn("DELETE");
    when(update.removeWatcher(TEST_ID)).thenReturn(true);
    controller.getCallback(request, alert);
  }

  @Test(expected = NotFoundException.class)
  public void testGetCallbackForProvisionPostException() {
    alert.setType(ActionType.PROVISIONWATCHER);
    when(request.getMethod()).thenReturn("POST");
    when(update.addWatcher(TEST_ID)).thenReturn(false);
    controller.getCallback(request, alert);
  }

  @Test(expected = NotFoundException.class)
  public void testGetCallbackForProvisionPutException() {
    alert.setType(ActionType.PROVISIONWATCHER);
    when(request.getMethod()).thenReturn("PUT");
    when(update.updateWatcher(TEST_ID)).thenReturn(false);
    controller.getCallback(request, alert);
  }

  @Test(expected = NotFoundException.class)
  public void testGetCallbackForProvisionDeleteException() {
    alert.setType(ActionType.PROVISIONWATCHER);
    when(request.getMethod()).thenReturn("DELETE");
    when(update.removeWatcher(TEST_ID)).thenReturn(false);
    controller.getCallback(request, alert);
  }

  @Test
  public void testGetCallbackForSchedule() {
    when(request.getMethod()).thenReturn("GET");
    controller.getCallback(request, alert);
  }

  @Test
  public void testGetCallbackForSchedulePost() {
    when(request.getMethod()).thenReturn("POST");
    when(callbackHandler.handlePost(alert)).thenReturn(true);
    controller.getCallback(request, alert);
  }

  @Test
  public void testGetCallbackForSchedulePut() {
    when(request.getMethod()).thenReturn("PUT");
    when(callbackHandler.handlePut(alert)).thenReturn(true);
    controller.getCallback(request, alert);
  }

  @Test
  public void testGetCallbackForScheduleDelete() {
    when(request.getMethod()).thenReturn("DELETE");
    when(callbackHandler.handleDelete(alert)).thenReturn(true);
    controller.getCallback(request, alert);
  }

  @Test
  public void testGetCallbackForOther() {
    alert.setType(ActionType.SERVICE);
    when(request.getMethod()).thenReturn("GET");
    controller.getCallback(request, alert);
  }

}

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

import org.edgexfoundry.device.controller.impl.CommandControllerImpl;
import org.edgexfoundry.service.handler.CommandHandler;
import org.edgexfoundry.test.category.RequiresNone;
import org.junit.Before;
import org.junit.Test;
import org.junit.experimental.categories.Category;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;
import javax.servlet.http.HttpServletRequest;

@Category(RequiresNone.class)
public class CommandControllerTest extends Mockito {

  private static final String TEST_DEVICE_ID = "test_device";
  private static final String TEST_CMD = "test_cmd";
  private static final String TEST_ARGS = "test_arg";

  @InjectMocks
  private CommandControllerImpl controller;

  @Mock
  private CommandHandler command;

  @Before
  public void setup() {
    MockitoAnnotations.initMocks(this);
  }

  @Test
  public void testGetCommand() {
    HttpServletRequest request = mock(HttpServletRequest.class);
    when(request.getMethod()).thenReturn("PUT");
    controller.getCommand(TEST_DEVICE_ID, TEST_CMD, TEST_ARGS, request);
  }

  @Test
  public void testGetCommands() {
    HttpServletRequest request = mock(HttpServletRequest.class);
    when(request.getMethod()).thenReturn("PUT");
    controller.getCommands(TEST_CMD, TEST_ARGS, request);
  }

}

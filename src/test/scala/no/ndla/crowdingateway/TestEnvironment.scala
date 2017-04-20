/*
 * Part of NDLA crowdin_gateway.
 * Copyright (C) 2017 NDLA
 *
 * See LICENSE
 */

package no.ndla.crowdingateway

import no.ndla.crowdingateway.controller.{CrowdinController, HealthController}
import no.ndla.crowdingateway.integration.CrowdinClient
import no.ndla.crowdingateway.service.ConverterService
import no.ndla.network.NdlaClient
import org.mockito.Mockito
import org.scalatest.mockito.MockitoSugar


trait TestEnvironment extends CrowdinController with HealthController with ConverterService with CrowdinClient with NdlaClient with MockitoSugar {
  val crowdinClient = mock[CrowdinClient]
  val healthController = mock[HealthController]
  val crowdinController = mock[CrowdinController]
  val ndlaClient = mock[NdlaClient]
  val converterService = mock[ConverterService]

  def resetMocks = {
    Mockito.reset(
      crowdinClient,
      healthController,
      crowdinController,
      ndlaClient,
      converterService)
  }
}

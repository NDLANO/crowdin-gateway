/*
 * Part of NDLA crowdin_gateway.
 * Copyright (C) 2017 NDLA
 *
 * See LICENSE
 */

package no.ndla.crowdingateway

import no.ndla.crowdingateway.controller.{CrowdinController, HealthController}
import no.ndla.crowdingateway.integration.CrowdinClient
import no.ndla.crowdingateway.service.{ConverterService, UploadService}
import no.ndla.network.NdlaClient
import org.scalatest.mockito.MockitoSugar


trait TestEnvironment extends CrowdinController with HealthController with UploadService with ConverterService with CrowdinClient with NdlaClient with MockitoSugar {
  val crowdinClient = mock[CrowdinClient]
  val healthController = mock[HealthController]
  val crowdinController = mock[CrowdinController]
  val ndlaClient = mock[NdlaClient]
  val uploadService = mock[UploadService]
  val converterService = mock[ConverterService]
}

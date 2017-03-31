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

object ComponentRegistry extends CrowdinController with HealthController with UploadService with ConverterService with CrowdinClient with NdlaClient {
  implicit val swagger = new CrowdinSwagger
  lazy val resourcesApp = new ResourcesApp
  lazy val crowdinController = new CrowdinController
  lazy val healthController = new HealthController
  lazy val crowdinClient = new CrowdinClient
  lazy val ndlaClient = new NdlaClient
  lazy val uploadService = new UploadService
  lazy val converterService = new ConverterService
}

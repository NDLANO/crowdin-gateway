/*
 * Part of NDLA crowdin_gateway.
 * Copyright (C) 2017 NDLA
 *
 * See LICENSE
 */

package no.ndla.crowdingateway.service

import no.ndla.crowdingateway.CrowdinGatewayProperties
import no.ndla.crowdingateway.integration.{CrowdinLanguage, CrowdinProject}
import no.ndla.crowdingateway.model.domain.{Language, Project}


trait ConverterService {
  val converterService: ConverterService

  class ConverterService {

    def asLanguage(sourceLanguage: CrowdinLanguage): Language = {
      Language(sourceLanguage.name, sourceLanguage.code)
    }

    def asProject(crowdinProject: CrowdinProject): Project = {

      Project(
        crowdinProject.details.identifier,
        crowdinProject.apiKey,
        crowdinProject.details.name,
        asLanguage(crowdinProject.details.sourceLanguage),
        crowdinProject.languages.map(asLanguage)
      )
    }
  }
}

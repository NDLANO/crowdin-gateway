/*
 * Part of NDLA crowdin_gateway.
 * Copyright (C) 2017 NDLA
 *
 * See LICENSE
 */

package no.ndla.crowdingateway.service

import com.typesafe.scalalogging.LazyLogging
import no.ndla.crowdingateway.integration.CrowdinClient
import no.ndla.crowdingateway.model.api.TranslationRequest
import no.ndla.crowdingateway.model.domain.Project

import scala.util.{Failure, Success, Try}


trait UploadService {
  this: CrowdinClient with ConverterService =>
  val uploadService: UploadService

  class UploadService extends LazyLogging {

    def projectForSourceLanguage(fromLanguage: String): Try[Project] = {
      crowdinClient.getProject(fromLanguage).map(converterService.asProject)
    }

    def assertHasTargetLanguage(project: Project, toLanguage: String): Try[Project] = {
        if(!project.supports(toLanguage)) {
          crowdinClient.addTargetLanguage(project, toLanguage).map(converterService.asProject)
        } else {
          Success(project)
        }
    }

    def upload(projectWithTargetLanguage: Project, translationRequest: TranslationRequest): Try[String] = {
      Failure(new NoSuchMethodException("NotImplemented"))
    }
  }


}

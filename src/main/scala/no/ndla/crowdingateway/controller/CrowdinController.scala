/*
 * Part of NDLA crowdin_gateway.
 * Copyright (C) 2017 NDLA
 *
 * See LICENSE
 */

package no.ndla.crowdingateway.controller

import no.ndla.crowdingateway.integration.CrowdinClient
import no.ndla.crowdingateway.model.api.{Error, TranslationRequest, TranslationResponse}
import no.ndla.crowdingateway.service.ConverterService
import org.json4s.{DefaultFormats, Formats}
import org.scalatra.swagger._

import scala.util.{Failure, Success}

trait CrowdinController {
  this: CrowdinClient with ConverterService =>
  val crowdinController: CrowdinController

  class CrowdinController(implicit val swagger: Swagger) extends NdlaController with SwaggerSupport {
    protected val applicationDescription = "API for integrating with Crowdin."
    protected implicit override val jsonFormats: Formats = DefaultFormats

    registerModel[Error]()

    val response403 = ResponseMessage(403, "Access Denied", Some("Error"))
    val response500 = ResponseMessage(500, "Unknown error", Some("Error"))


    val orderArticleTranslation =
      (apiOperation[TranslationResponse]("orderArticleTranslation")
        summary "Sends the given content to Crowdin for translation"
        notes "Sends the given content to Crowdin for translation"
        parameters(
        headerParam[Option[String]]("X-Correlation-ID").description("User supplied correlation-id. May be omitted."),
        bodyParam[TranslationRequest])
        responseMessages(response403, response500)
        authorizations "oauth2")

    post("/article", operation(orderArticleTranslation)) {
      val translationRequest = extract[TranslationRequest](request.body)
      val directoryName = translationRequest.id

      val translationResult = for {
        project <- crowdinClient.getProject(translationRequest.fromLanguage)
        projectWithLanguage <- crowdinClient.addTargetLanguage(project, translationRequest.toLanguage)
        directory <- crowdinClient.createDirectory(projectWithLanguage, directoryName)
        uploaded <- crowdinClient.uploadTo(projectWithLanguage, directory, translationRequest.metaData, translationRequest.content)
      } yield uploaded

      translationResult match {
        case Success(x) => converterService.asTranslationResponse(x)
        case Failure(f) => errorHandler(f)
      }
    }
  }
}

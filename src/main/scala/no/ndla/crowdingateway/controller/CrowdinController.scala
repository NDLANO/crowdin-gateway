/*
 * Part of NDLA crowdin_gateway.
 * Copyright (C) 2017 NDLA
 *
 * See LICENSE
 */

package no.ndla.crowdingateway.controller

import no.ndla.crowdingateway.CrowdinGatewayProperties
import no.ndla.crowdingateway.model.api.{TranslationRequest, TranslationResponse}
import no.ndla.crowdingateway.service.UploadService
import org.json4s.{DefaultFormats, Formats}
import org.scalatra.Ok
import org.scalatra.swagger._

trait CrowdinController {
  this: UploadService =>
  val crowdinController: CrowdinController

  class CrowdinController(implicit val swagger: Swagger) extends NdlaController with SwaggerSupport {
    protected val applicationDescription = "API for integrating with Crowdin."
    protected implicit override val jsonFormats: Formats = DefaultFormats

    registerModel[Error]()

    val response403 = ResponseMessage(403, "Access Denied", Some("Error"))
    val response500 = ResponseMessage(500, "Unknown error", Some("Error"))

    get("/") {
      CrowdinGatewayProperties.CrowdinProjects.foreach(tuple => println(s"${tuple._1} = ${tuple._2}"))
      Ok(body = "Heisann")
    }

    post("/") {
      val translationRequest = extract[TranslationRequest](request.body)
      for {
        project <- uploadService.projectForSourceLanguage(translationRequest.fromLanguage)
        projectWithTargetLanguage <- uploadService.assertHasTargetLanguage(project, translationRequest.toLanguage)
        uploadResult <- uploadService.upload(projectWithTargetLanguage, translationRequest)
      } yield uploadResult

      TranslationResponse("123")
    }
  }
}

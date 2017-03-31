/*
 * Part of NDLA crowdin_gateway.
 * Copyright (C) 2017 NDLA
 *
 * See LICENSE
 */

package no.ndla.crowdingateway.integration

import no.ndla.crowdingateway.CrowdinGatewayProperties.CrowdinProjects
import no.ndla.crowdingateway.model.ProjectNotFoundException
import no.ndla.crowdingateway.model.api.TranslationRequest
import no.ndla.crowdingateway.model.domain.Project
import no.ndla.network.NdlaClient

import scala.util.{Failure, Try}
import scalaj.http.Http


trait CrowdinClient {
  this: NdlaClient =>
  val crowdinClient: CrowdinClient

  class CrowdinClient {
    private val crowdinApi = "https://api.crowdin.com/api"
    private val crowdinProjectInfo = s"$crowdinApi/project/{PROJECT_ID}/info?key={PROJECT_KEY}&json"
    private val editProject = s"$crowdinApi/project/{PROJECT_ID}/edit-project?key={PROJECT_KEY}&json"

    def addTargetLanguage(project: Project, toLanguage: String): Try[CrowdinProject] = {
      val languageList: Seq[String] = project.targetLanguages.map(_.code) :+ toLanguage
      val request = Http(crowdinUrl(editProject, project.identifier, project.apiKey)).postForm(languageList.map(lang => ("languages", lang)))
      ndlaClient.fetch[CrowdinProject](request)
    }

    def getProject(sourceLanguage: String): Try[CrowdinProject] = {
      CrowdinProjects.get(sourceLanguage) match {
        case Some(projectDef) => ndlaClient.fetch[CrowdinProject](Http(crowdinUrl(crowdinProjectInfo, projectDef.identifier, projectDef.apiKey))).map(x => x.copy(apiKey = projectDef.apiKey))
        case None => Failure(new ProjectNotFoundException(s"No project for source language $sourceLanguage"))
      }
    }

    def upload(toProject: CrowdinProject, translationRequest: TranslationRequest) = {

    }

    private def crowdinUrl(url: String, projectId: String, projectKey: String) =
      url.replace("{PROJECT_ID}", projectId).replace("{PROJECT_KEY}", projectKey)
  }
}

case class CrowdinLanguage(name: String, code: String, canTranslate: Option[Int], canApprove: Option[Int])
case class CrowdinFile(nodeType: String)
case class CrowdinProjectDetails(sourceLanguage: CrowdinLanguage, name: String, identifier: String)
case class CrowdinProject(languages: Seq[CrowdinLanguage], files: Seq[CrowdinFile], details: CrowdinProjectDetails, apiKey: String) {
  def this(languages: Seq[CrowdinLanguage], files: Seq[CrowdinFile], details: CrowdinProjectDetails, apiKey: Option[String]) = this(languages, files, details, "")
}

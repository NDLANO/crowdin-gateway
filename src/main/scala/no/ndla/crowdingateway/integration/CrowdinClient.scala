/*
 * Part of NDLA crowdin_gateway.
 * Copyright (C) 2017 NDLA
 *
 * See LICENSE
 */

package no.ndla.crowdingateway.integration

import no.ndla.crowdingateway.CrowdinGatewayProperties.CrowdinProjects
import no.ndla.crowdingateway.model.domain.{AddDirectoryResponse, AddFileResponse, CrowdinProject, EditProjectResponse}
import no.ndla.crowdingateway.model.{ContentAlreadyInProgressException, ProjectNotFoundException}
import no.ndla.network.NdlaClient

import scala.util.{Failure, Success, Try}
import scalaj.http.{Http, MultiPart}


trait CrowdinClient {
  this: NdlaClient =>
  val crowdinClient: CrowdinClient

  class CrowdinClient {

    private val BASE_URL = "https://api.crowdin.com/api/"
    private val crowdinApi = "https://api.crowdin.com/api"
    private val crowdinProjectInfo = s"$crowdinApi/project/{PROJECT_ID}/info?key={PROJECT_KEY}&json"
    private val editProject = s"$crowdinApi/project/{PROJECT_ID}/edit-project?key={PROJECT_KEY}&json"
    private val addDirectory = s"$crowdinApi/project/{PROJECT_ID}/add-directory?key={PROJECT_KEY}&json"
    private val addFile = s"$crowdinApi/project/{PROJECT_ID}/add-file?key={PROJECT_KEY}&json"

    def getProject(sourceLanguage: String): Try[CrowdinProject] = {
      CrowdinProjects.get(sourceLanguage) match {
        case Some(projectDef) => ndlaClient.fetch[CrowdinProject](
          Http(crowdinUrl(crowdinProjectInfo, projectDef.identifier, projectDef.apiKey))).map(x => x.copy(apiKey = projectDef.apiKey))

        case None => Failure(new ProjectNotFoundException(s"No project for source language '$sourceLanguage'"))
      }
    }

    def addTargetLanguage(project: CrowdinProject, toLanguage: String): Try[CrowdinProject] = {
      if(project.supports(toLanguage)) {
        Success(project)
      } else {
        val languageList: Seq[String] = project.languages.map(_.code) :+ toLanguage
        val request = Http(crowdinUrl(editProject, project.details.identifier, project.apiKey)).postForm(
          languageList.zipWithIndex.map(t => (s"languages[${t._2}]", t._1)))

        ndlaClient.fetch[EditProjectResponse](request) match {
          case Failure(f) => Failure(f)
          case Success(_) => Success(project)
        }
      }
    }

    def createDirectory(project: CrowdinProject, directoryName: String): Try[String] = {
      if(project.hasDirectory(directoryName)) {
        Failure(new ContentAlreadyInProgressException(s"The content with name '$directoryName' is already being translated"))
      } else {
        val request = Http(crowdinUrl(addDirectory, project.details.identifier, project.apiKey)).postForm(Seq(("name", directoryName)))
        ndlaClient.fetch[AddDirectoryResponse](request) match {
          case Success(_) => Success(directoryName)
          case Failure(f) => Failure(f)
        }
      }
    }

    def uploadTo(project: CrowdinProject, directoryName: String, metadata:String, content: String): Try[AddFileResponse] = {
      val request = Http(crowdinUrl(addFile, project.details.identifier, project.apiKey)).postMulti(
        MultiPart(s"files[$directoryName/metadata.json]", "metadata.json", "application/json", metadata),
        MultiPart(s"files[$directoryName/content.html]", "content.html", "text/html", content)
      )
      ndlaClient.fetch[AddFileResponse](request)
    }

    private def crowdinUrl(url: String, projectId: String, projectKey: String) =
      url.replace("{PROJECT_ID}", projectId).replace("{PROJECT_KEY}", projectKey)
  }
}


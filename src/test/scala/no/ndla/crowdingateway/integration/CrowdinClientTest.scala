/*
 * Part of NDLA crowdin_gateway.
 * Copyright (C) 2017 NDLA
 *
 * See LICENSE
 */

package no.ndla.crowdingateway.integration

import no.ndla.crowdingateway.model.domain._
import no.ndla.crowdingateway.{TestEnvironment, UnitSuite}
import org.mockito.Matchers.{eq => eqTo, _}
import org.mockito.Mockito._

import scala.util.{Failure, Success}
import scalaj.http.HttpRequest


class CrowdinClientTest extends UnitSuite with TestEnvironment {

  val client = new CrowdinClient

  override def beforeEach(): Unit = resetMocks

  val DefaultProject = CrowdinProject(Seq(), Seq(),
    CrowdinProjectDetails(CrowdinLanguage("Norwegian", "nb", None, None), "test", "test"), "")

  test("That getProject returns Failure when trying to fetch an unsupported language") {
    val sourceLanguage = "klingon"

    assertResult(s"No project for source language '$sourceLanguage'") {
      client.getProject(sourceLanguage).failed.get.getMessage
    }
  }

  test("That getProject returns Failure when error received from Crowdin") {
    when(ndlaClient.fetch[CrowdinProject](any[HttpRequest])(any[Manifest[CrowdinProject]])).thenReturn(Failure(new RuntimeException("Failure from Crowdin")))
    assertResult("Failure from Crowdin") {
      client.getProject("nb").failed.get.getMessage
    }
  }

  test("That getProject returns Success when all ok") {
    val sourceLanguage = "nb"
    val name = "test-project"
    val identifier = "test-project"
    val expectedKey = "test-key"

    val language = CrowdinLanguage("Norwegian", sourceLanguage, None, None)

    val project = CrowdinProject(Seq(), Seq(), CrowdinProjectDetails(language, name, identifier), "")
    when(ndlaClient.fetch[CrowdinProject](any[HttpRequest])(any[Manifest[CrowdinProject]])).thenReturn(Success(project))

    client.getProject(sourceLanguage).map(_.apiKey).get should equal(expectedKey)
  }


  test("That addTargetLanguage returns Success when project already supports language") {
    val project = CrowdinProject(
      Seq(CrowdinLanguage("French", "fr", None, None)), Seq(),
      CrowdinProjectDetails(CrowdinLanguage("Norwegian", "nb", None, None), "test", "test"), "")

    client.addTargetLanguage(project, "fr").get should equal (project)
  }

  test("That addTargetLanguage returns Failure when error received from Crowdin") {
    val project = CrowdinProject(Seq(), Seq(),
      CrowdinProjectDetails(CrowdinLanguage("Norwegian", "nb", None, None), "test", "test"), "")

    when(ndlaClient.fetch[CrowdinProject](any[HttpRequest])(any[Manifest[CrowdinProject]])).thenReturn(Failure(new RuntimeException("Failure from Crowdin")))
    assertResult("Failure from Crowdin") {
      client.addTargetLanguage(project, "fr").failed.get.getMessage
    }
  }

  test("That addTargetLanguage returns Success when all ok") {
    when(ndlaClient.fetch[EditProjectResponse](any[HttpRequest])(any[Manifest[EditProjectResponse]])).thenReturn(Success(EditProjectResponse(EditedProject(true, "", ""))))
    client.addTargetLanguage(DefaultProject, "fr").get should equal (DefaultProject)
  }

  test("That createDirectory returns Failure if directory already exists") {
    val directory = CrowdinFile("directory", "1", "directory", None, None, None, None, Seq())

    assertResult("The content with name 'directory' is already being translated") {
      client.createDirectory(DefaultProject.copy(files = Seq(directory)), "directory").failed.get.getMessage
    }
  }

  test("That createDirectory returns Failure when error received from Crowdin") {
    when(ndlaClient.fetch[AddDirectoryResponse](any[HttpRequest])(any[Manifest[AddDirectoryResponse]])).thenReturn(Failure(new RuntimeException("Failure from Crowdin")))
    assertResult("Failure from Crowdin") {
      client.createDirectory(DefaultProject, "directory").failed.get.getMessage
    }
  }

  test("That createDirectory returns Success when all ok") {
    when(ndlaClient.fetch[AddDirectoryResponse](any[HttpRequest])(any[Manifest[AddDirectoryResponse]])).thenReturn(Success(AddDirectoryResponse(true)))
    client.createDirectory(DefaultProject, "directory").get should equal("directory")

  }

  test("That uploadTo returns Failure when error received from Crowdin") {
    when(ndlaClient.fetch[AddFileResponse](any[HttpRequest])(any[Manifest[AddFileResponse]])).thenReturn(Failure(new RuntimeException("Failure from Crowdin")))
    assertResult("Failure from Crowdin") {
      client.uploadTo(DefaultProject, "directory", "metadata", "content").failed.get.getMessage
    }
  }

  test("That uploadTo returns Success when all ok") {
    val stats = AddedStats(Seq())
    when(ndlaClient.fetch[AddFileResponse](any[HttpRequest])(any[Manifest[AddFileResponse]])).thenReturn(Success(AddFileResponse(true, stats)))
    client.uploadTo(DefaultProject, "director", "metadata", "content") should equal (Success(AddFileResponse(true, stats)))
  }
}

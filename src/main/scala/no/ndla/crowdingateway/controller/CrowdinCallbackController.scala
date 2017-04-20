/*
 * Part of NDLA crowdin_gateway.
 * Copyright (C) 2017 NDLA
 *
 * See LICENSE
 */

package no.ndla.crowdingateway.controller

import no.ndla.crowdingateway.integration.CrowdinClient
import no.ndla.crowdingateway.service.ConverterService
import org.json4s.{DefaultFormats, Formats}

import scala.concurrent.ExecutionContext.Implicits.global
import scala.concurrent.Future

trait CrowdinCallbackController {
  this: CrowdinClient with ConverterService =>
  val crowdinCallbackController: CrowdinCallbackController

  class CrowdinCallbackController extends NdlaController {
    protected implicit override val jsonFormats: Formats = DefaultFormats

    get("/file-translated") {
      for {
        project <- requireParam("project")
        language <- requireParam("language")
        fileId <- requireParam("file_id")
        file <- requireParam("file")
      } yield fetchResult(project, language, fileId, file)
    }

    get("/file-proofread") {
      for {
        project <- requireParam("project")
        language <- requireParam("language")
        fileId <- requireParam("file_id")
        file <- requireParam("file")
      } yield fetchResult(project, language, fileId, file)
    }

    def fetchResult(project: String, language: String, fileId: String, file: String): Unit = Future {
      Thread.sleep(10000)
      logger.info(s"$project, $language, $fileId, $file")
    }
  }
}

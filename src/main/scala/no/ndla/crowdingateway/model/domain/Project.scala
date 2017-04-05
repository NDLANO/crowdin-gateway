/*
 * Part of NDLA crowdin_gateway.
 * Copyright (C) 2017 NDLA
 *
 * See LICENSE
 */

package no.ndla.crowdingateway.model.domain

case class ProjectDefinition(identifier: String, apiKey: String)

case class CrowdinLanguage(name: String, code: String, canTranslate: Option[Int], canApprove: Option[Int])
case class CrowdinFile(nodeType: String, id: String, name: String, created: Option[String], lastUpdated: Option[String], lastAccessed: Option[String], lastRevision: Option[String], files: Seq[CrowdinFile])
case class CrowdinProjectDetails(sourceLanguage: CrowdinLanguage, name: String, identifier: String)

case class EditProjectResponse(project: EditedProject)
case class EditedProject(success: Boolean, invitation: String, url: String)
case class AddDirectoryResponse(success: Boolean)

case class AddedFile(fileId: Int, name: String, strings: Int, words: Int)
case class AddedStats(files: Seq[AddedFile])
case class AddFileResponse(success: Boolean, stats: AddedStats)

case class CrowdinProject(languages: Seq[CrowdinLanguage], files: Seq[CrowdinFile], details: CrowdinProjectDetails, apiKey: String) {
  def this(languages: Seq[CrowdinLanguage], files: Seq[CrowdinFile], details: CrowdinProjectDetails, apiKey: Option[String]) = this(languages, files, details, "")

  def supports(toLanguage: String): Boolean = {
    languages.exists(_.code == toLanguage)
  }

  def hasDirectory(directoryName: String): Boolean = {
    files.exists(f => f.nodeType== "directory" && f.name == directoryName)
  }
}


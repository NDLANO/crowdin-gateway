/*
 * Part of NDLA crowdin_gateway.
 * Copyright (C) 2017 NDLA
 *
 * See LICENSE
 */

package no.ndla.crowdingateway.model.domain

sealed trait ProjectIdentifier {
  def identifier: String
  def apiKey: String
  def sourceLanguage: Language
}

case class ProjectDefinition(identifier: String, apiKey: String, sourceLanguage: Language) extends ProjectIdentifier
case class Project (identifier: String, apiKey: String, name: String, sourceLanguage: Language, targetLanguages: Seq[Language]) extends ProjectIdentifier {
  def supports(toLanguage: String): Boolean = {
    targetLanguages.exists(_.code == toLanguage)
  }
}

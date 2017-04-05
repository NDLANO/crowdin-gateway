/*
 * Part of NDLA crowdin_gateway.
 * Copyright (C) 2017 NDLA
 *
 * See LICENSE
 */

package no.ndla.crowdingateway.model.api

case class TranslationFile(id: Int, name: String, numberOfStrings: Int, numberOfWords: Int)
case class TranslationResponse(translationIds: Seq[Int], translationNames: Seq[String], totalStrings: Int, totalWords: Int)

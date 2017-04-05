/*
 * Part of NDLA crowdin_gateway.
 * Copyright (C) 2017 NDLA
 *
 * See LICENSE
 */

package no.ndla.crowdingateway.model.api


case class TranslationRequest (fromLanguage: String, toLanguage: String, metaData: String, content: String, id: String)

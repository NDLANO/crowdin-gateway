/*
 * Part of NDLA crowdin_gateway.
 * Copyright (C) 2017 NDLA
 *
 * See LICENSE
 */

package no.ndla.crowdingateway.service

import no.ndla.crowdingateway.model.api.TranslationResponse
import no.ndla.crowdingateway.model.domain.AddFileResponse


trait ConverterService {
  val converterService: ConverterService

  class ConverterService {
    def asTranslationResponse(x: AddFileResponse): TranslationResponse = {
      TranslationResponse(
        x.stats.files.map(_.fileId),
        x.stats.files.map(_.name),
        x.stats.files.map(_.words).sum,
        x.stats.files.map(_.strings).sum)
    }
  }
}

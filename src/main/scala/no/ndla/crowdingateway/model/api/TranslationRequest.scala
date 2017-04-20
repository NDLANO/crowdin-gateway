/*
 * Part of NDLA crowdin_gateway.
 * Copyright (C) 2017 NDLA
 *
 * See LICENSE
 */

package no.ndla.crowdingateway.model.api

import org.scalatra.swagger.annotations.ApiModel
import org.scalatra.swagger.runtime.annotations.ApiModelProperty

import scala.annotation.meta.field

@ApiModel(description = "Information about a request for translation")
case class TranslationRequest(@(ApiModelProperty@field)(description = "The current language of the content") fromLanguage: String,
                              @(ApiModelProperty@field)(description = "The language to have the content translated to") toLanguage: String,
                              @(ApiModelProperty@field)(description = "Metadata belonging to the content. Needs to be valid JSON.") metaData: String,
                              @(ApiModelProperty@field)(description = "The HTML-content to be translated") content: String,
                              @(ApiModelProperty@field)(description = "The id of the content to be translated") id: String)

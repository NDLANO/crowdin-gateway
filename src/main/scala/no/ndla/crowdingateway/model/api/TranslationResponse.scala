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

@ApiModel(description = "Information about a translation response")
case class TranslationResponse(@(ApiModelProperty@field)(description = "Crowdin specific IDs for the content") translationIds: Seq[Int],
                               @(ApiModelProperty@field)(description = "Crowdin specific file names for the content") translationNames: Seq[String],
                               @(ApiModelProperty@field)(description = "Total number of strings to translate") totalStrings: Int,
                               @(ApiModelProperty@field)(description = "Total number of words to translate") totalWords: Int)

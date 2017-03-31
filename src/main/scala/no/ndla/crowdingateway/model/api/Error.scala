/*
 * Part of NDLA crowdin_gateway.
 * Copyright (C) 2017 NDLA
 *
 * See LICENSE
 */

package no.ndla.crowdingateway.model.api

import java.text.SimpleDateFormat
import java.util.Date

import no.ndla.crowdingateway.CrowdinGatewayProperties
import org.scalatra.swagger.annotations.{ApiModel, ApiModelProperty}

import scala.annotation.meta.field

@ApiModel(description = "Information about errors")
case class Error(@(ApiModelProperty@field)(description = "Code stating the type of error") code: String,
                 @(ApiModelProperty@field)(description = "Description of the error") description: String,
                 @(ApiModelProperty@field)(description = "When the error occured") occuredAt: String = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss.SSS").format(new Date()))

object Error {
  val GENERIC = "GENERIC"
  val NOT_FOUND = "NOT FOUND"
  val INDEX_MISSING = "INDEX MISSING"
  val VALIDATION = "VALIDATION"
  val FILE_TOO_BIG = "FILE TOO BIG"
  val ACCESS_DENIED = "ACCESS DENIED"

  val GenericError = Error(GENERIC, s"Ooops. Something we didn't anticipate occured. We have logged the error, and will look into it. But feel free to contact ${CrowdinGatewayProperties.ContactEmail} if the error persists.")
  val IndexMissingError = Error(INDEX_MISSING, s"Ooops. Our search index is not available at the moment, but we are trying to recreate it. Please try again in a few minutes. Feel free to contact ${CrowdinGatewayProperties.ContactEmail} if the error persists.")

}
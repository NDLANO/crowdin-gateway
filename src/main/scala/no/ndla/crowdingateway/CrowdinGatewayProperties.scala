/*
 * Part of NDLA crowdin_gateway.
 * Copyright (C) 2017 NDLA
 *
 * See LICENSE
 */

package no.ndla.crowdingateway

import no.ndla.crowdingateway.model.domain.{Language, ProjectDefinition}

import scala.util.Properties.envOrNone


object CrowdinGatewayProperties {
  val ApplicationPort = propOrElse("APPLICATION_PORT", "80").toInt
  val ContactEmail = "christergundersen@ndla.no"
  val CorrelationIdKey = "correlationID"
  val CorrelationIdHeader = "X-Correlation-ID"
  val HealthControllerPath = "/health"
  val CrowdinControllerPath = "/crowdin-gateway/v1/crowdin"


  private val projectList = propOrElse("CROWDIN_PROJECTS", "").split(",").map(_.trim)

  val CrowdinProjects: Map[String, ProjectDefinition] = projectList.map(key => {
    val projectKey = s"CROWDIN_$key"
    val lang = key.toLowerCase
    val details = prop(projectKey).split(";").map(_.trim)

    lang -> ProjectDefinition(details(0), details(1))
  }) toMap


  def prop(key: String): String = {
    propOrElse(key, throw new RuntimeException(s"Unable to load property $key"))
  }

  def propOrElse(key: String, default: => String): String = {
    envOrNone(key) match {
      case Some(env) => env
      case None => default
    }
  }
}

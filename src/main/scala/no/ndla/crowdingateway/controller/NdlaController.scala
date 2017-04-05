/*
 * Part of NDLA crowdin_gateway.
 * Copyright (C) 2017 NDLA
 *
 * See LICENSE
 */

package no.ndla.crowdingateway.controller

import com.typesafe.scalalogging.LazyLogging
import no.ndla.crowdingateway.CrowdinGatewayProperties
import no.ndla.crowdingateway.model.api.Error
import no.ndla.crowdingateway.model.{AccessDeniedException, ContentAlreadyInProgressException, ValidationException, ValidationMessage}
import no.ndla.network.{ApplicationUrl, AuthUser, CorrelationID}
import org.apache.logging.log4j.ThreadContext
import org.json4s.native.Serialization.read
import org.json4s.{DefaultFormats, Formats}
import org.scalatra.json.NativeJsonSupport
import org.scalatra.{BadRequest, InternalServerError, ScalatraServlet}

abstract class NdlaController extends ScalatraServlet with NativeJsonSupport with LazyLogging {
  protected implicit override val jsonFormats: Formats = DefaultFormats

  before() {
    contentType = formats("json")
    CorrelationID.set(Option(request.getHeader(CrowdinGatewayProperties.CorrelationIdHeader)))
    ThreadContext.put(CrowdinGatewayProperties.CorrelationIdKey, CorrelationID.get.getOrElse(""))
    ApplicationUrl.set(request)
    AuthUser.set(request)
    logger.info("{} {}{}", request.getMethod, request.getRequestURI, Option(request.getQueryString).map(s => s"?$s").getOrElse(""))
  }

  after() {
    CorrelationID.clear()
    ThreadContext.remove(CrowdinGatewayProperties.CorrelationIdKey)
    ApplicationUrl.clear
    AuthUser.clear()
  }

  error {
    case c: ContentAlreadyInProgressException => BadRequest(body = Error.InProgressError)
    case t: Throwable => {
      logger.error(Error.GenericError.toString, t)
      InternalServerError(body=Error.GenericError)
    }
  }

  def extract[T](json: String)(implicit mf: scala.reflect.Manifest[T]): T = {
    try {
      read[T](json)
    } catch {
      case e: Exception => {
        logger.error(e.getMessage, e)
        throw new ValidationException(errors = List(ValidationMessage("body", e.getMessage)))
      }
    }
  }

  def assertHasRole(role: String): Unit = {
    if (!AuthUser.hasRole(role))
      throw new AccessDeniedException("User is missing required role to perform this operation")
  }

}

/*
 * Part of NDLA crowdin_gateway.
 * Copyright (C) 2017 NDLA
 *
 * See LICENSE
 */

package no.ndla.crowdingateway.controller

import javax.servlet.http.HttpServletRequest

import com.typesafe.scalalogging.LazyLogging
import no.ndla.crowdingateway.CrowdinGatewayProperties
import no.ndla.crowdingateway.model.api.{Error, ValidationError, ValidationMessage}
import no.ndla.crowdingateway.model.{AccessDeniedException, ContentAlreadyInProgressException, ParameterMissingException, ValidationException}
import no.ndla.network.{ApplicationUrl, AuthUser, CorrelationID}
import org.apache.logging.log4j.ThreadContext
import org.json4s.native.Serialization.read
import org.json4s.{DefaultFormats, Formats}
import org.scalatra.json.NativeJsonSupport
import org.scalatra.{BadRequest, InternalServerError, RenderPipeline, ScalatraServlet}

import scala.util.{Failure, Success, Try}

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
    case v: ValidationException =>
      logger.warn(v.getMessage, v)
      BadRequest(body = ValidationError(messages = v.errors))

    case c: ContentAlreadyInProgressException => BadRequest(body = Error.InProgressError)
    case t: Throwable => {
      logger.error(Error.GenericError.toString, t)
      InternalServerError(body = Error.GenericError)
    }
  }

  private val customRenderer: RenderPipeline = {
    case Failure(e) => errorHandler(e)
    case Success(s) => s
  }

  override def renderPipeline = customRenderer orElse super.renderPipeline

  def requireParam(paramName: String)(implicit request: HttpServletRequest): Try[String] = {
    params.get(paramName) match {
      case Some(value) => Success(value)
      case None =>
        logger.warn(s"Request made to ${request.getRequestURI} without required parameter $paramName")
        Failure(new ParameterMissingException(s"The required parameter $paramName is missing."))
    }
  }

  def extract[T](json: String)(implicit mf: scala.reflect.Manifest[T]): Try[T] = {
    Try(read[T](json)) match {
      case Success(t) => Success(t)
      case Failure(e) => Failure(new ValidationException(errors = List(ValidationMessage("body", e.getMessage))))
    }
  }

  def assertHasRole(role: String): Unit = {
    if (!AuthUser.hasRole(role))
      throw new AccessDeniedException("User is missing required role to perform this operation")
  }

}

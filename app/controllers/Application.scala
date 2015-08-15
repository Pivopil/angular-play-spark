package controllers

import play.api.Play
import play.api.libs.json.Json
import play.api.mvc._
import utils.SparkSQL
import utils.SparkSQL.DataResponse

import scala.concurrent.Future

object Application extends Controller {

  implicit val dataResponseFormat = Json.format[DataResponse]

  def index = Action {
    Ok(views.html.index())
  }

  def spark(date: String) = Action.async { implicit request =>
    Future.successful(Ok(Json.toJson(SparkSQL.getCategoriesByDate(date))))
  }

  def sparkRange(resolution: String) = Action.async { implicit request =>
    Future.successful(Ok(Json.toJson(SparkSQL.getDateRange(resolution: String))))
  }

  def config = Action {
    val apiMap = Map(
      "dateFormat"-> Play.current.configuration.getString("general.dateFormat").getOrElse(""),
      "sparkRangeUrl"-> Play.current.configuration.getString("angular.sparkRangeUrl").getOrElse(""),
      "sparkUrl"-> Play.current.configuration.getString("angular.sparkUrl").getOrElse("")
    )

    Ok(Json.toJson(apiMap)).as(JSON)
  }

}

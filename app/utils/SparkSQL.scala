package utils

import org.apache.spark.{SparkConf, SparkContext}
import org.joda.time.{DurationFieldType, LocalDate, Months}
import play.api.Play

object SparkSQL extends Serializable {

  def getConfigProperty(propertyName: String, defaultPropertyValue: String) = Play.current.configuration.getString(propertyName).getOrElse(defaultPropertyValue)

  case class DataResponse(x: Seq[String], data: Map[String, Seq[Long]])

  val format = new java.text.SimpleDateFormat("yyyy-MM-dd HH:mm:ss.S")

  val driverHost = "localhost"

  val conf = new SparkConf(false)
    .setMaster("local[4]")
    .setAppName("play")
    .set("spark.logConf", "true")
    .set("spark.driver.host", s"$driverHost")

  val sc = new SparkContext(conf)

  val sqlContext = new org.apache.spark.sql.SQLContext(sc)

  val options = Map(
    "url" -> getConfigProperty("db.spark.url", ""),
    "user" -> getConfigProperty("db.spark.user", ""),
    "password" -> getConfigProperty("db.spark.password", ""),
    "dbtable" -> getConfigProperty("db.spark.dbtable", ""),
    "driver" -> getConfigProperty("db.spark.driver", "")
  )

  val jdbcDF = sqlContext.read.format("jdbc").options(options).load()
  val dateCategoryPairs = jdbcDF.map(x => (format.parse("" + x(4)), "" + x(1))).groupByKey()
  val dateCategoryPairsWithMappedValues = dateCategoryPairs.map(x => (x._1, x._2.map(y => (y, 1))))
  val finalPass = dateCategoryPairsWithMappedValues.map(x => (x._1, x._2.groupBy(_._1).map(y => (y._1, y._2.size))))
  finalPass.cache()

  def getCategoriesByDate(date: String): Map[String, Int] = {

    val filteredRDD = finalPass.filter {
      _._1.getTime <= format.parse(date + " 00:00:00.0").getTime
    }
    filteredRDD.flatMap(_._2).reduceByKey(_ + _).collect().toMap

  }

  def getDateRange(maybeString: String): DataResponse = {
    var dates = Seq.empty[String]
    var c3data = Map.empty[String, Seq[Long]]

    val from = LocalDate.fromDateFields(format.parse("2005-08-01 00:00:00.0"))
    val until = LocalDate.fromDateFields(format.parse("2015-08-01 00:00:00.0"))

    val numberOfMonth = Months.monthsBetween(from, until).get(DurationFieldType.months())
    val monthDates = for (f <- 0 to numberOfMonth) yield from.plusMonths(f)
    val reverseMonthDates = monthDates.reverse

    def loadDataForDay(date: LocalDate): Unit = {

      def updateMap(key: String, value: Long): Unit = c3data += key -> (c3data.getOrElse(key, Seq.empty[Long]) :+ value)

      dates = dates :+ date.toString
      val args = getCategoriesByDate(date.toString)
      args foreach { case (key, value) => updateMap(key, value) }
    }

    reverseMonthDates foreach (loadDataForDay(_))

    DataResponse(dates, c3data)
  }
}

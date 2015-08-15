# angular-play-spark
Simple web app for data analysis: Angular, C3js, Scala, Play, Apache Spark
# How to run
# 1. Open terminal and execute commands: 
git clone https://github.com/Pivopil/angular-play-spark.git
cd angular-play-spark
bower install
# 2. Set correct application.conf properties:  
db.spark.url, db.spark.user, db.spark.password, db.spark.dbtable, db.spark.driver
# 3. Open terminal and execute command in project root folder:
sbt run

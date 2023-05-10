package examples.featuresservice

import examples.featuresservice.model.{Feature, ProductConfiguration, Product}

import scala.util.Random

object util {
  var id=20
  def getNextId(rand: Random): Int = {
    val tmpid = id
    id+=1
    tmpid
  }
  def configName(rand: Random): String = {
    rand.alphanumeric.take(20).mkString
  }
  def descr(rand: Random): Some[String] = {
    Some(rand.alphanumeric.take(20).mkString)
  }
  var featureName1 = ""
  def setFeatureName1(rand: Random): String = {
    featureName1 = rand.alphanumeric.take(20).mkString
    featureName1
  }
  var featureName2 = ""
  def setFeatureName2(rand: Random): String = {
    featureName2 = rand.alphanumeric.take(20).mkString
    featureName2
  }
  var featureName3 = ""
  def setFeatureName3(rand: Random): String = {
    featureName3 = rand.alphanumeric.take(20).mkString
    featureName3
  }
  def randDesc(rand: Random): String = {
    rand.alphanumeric.take(20).mkString
  }
  def const(string: String, rand: Random): String ={
    string
  }
  def optDescr(rand: Random): Option[String] = {
    Option(rand.alphanumeric.take(20).mkString)
  }
  def rand(min: String, max: String, rand: Random): Int ={
    min.toInt + rand.nextInt( (max.toInt - min.toInt) + 1 )
  }
  def getFeatureName1(rand: Random): Some[String] = {
    Some(featureName1)
  }
  def getFeatureName2(rand: Random): Some[String] = {
    Some(featureName1)
  }
  def checkConfig(configName: String, config: ProductConfiguration): Boolean ={
    config.name.get==configName
  }
  var product: Product = null
  def setProduct(product: Product): Boolean = {
    this.product = product
    true
  }
  def getProductId(rand: Random): Long = {
    this.product.constraints.get.head.id.get
  }
  def checkFeatures(features: Set[Feature], featureName1: String, featureName2: String): Boolean = {
    val featureNames = features.map(_.name.get)
    featureNames.contains(featureName1) && featureNames.contains(featureName2)
  }
}

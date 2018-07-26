package com.flipkart.metrics

import io.dropwizard.metrics5._
import collection.JavaConversions._

/**
 * Created by kinshuk.bairagi on 11/02/16.
 */
trait InstrumentedBase {

  def registry:MetricRegistry

  protected def getMetric(name: String, tags: Map[String, String]): MetricName = {
    val metric = MetricRegistry.name(getClass, name)
    if (tags.nonEmpty)
      metric.tagged(tags)
    else
      metric
  }

  def profile[T](metricName: String, tags: Map[String, String] = Map.empty)(fn: ⇒ T): T = {
    val context = registry.timer(getMetric(metricName, tags)).time()
    try {
      fn
    } finally {
      context.stop()
    }
  }

  def counter(metricName: String, tags: Map[String, String] = Map.empty): Counter = registry.counter(getMetric(metricName, tags))

  def meter(metricName: String, tags: Map[String, String] = Map.empty): Meter = registry.meter(getMetric(metricName, tags))

  def timer(metricName: String, tags: Map[String, String] = Map.empty): Timer = registry.timer(getMetric(metricName, tags))


}


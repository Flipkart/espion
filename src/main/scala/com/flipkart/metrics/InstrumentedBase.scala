package com.flipkart.metrics

import com.codahale.metrics.{Counter, Meter, MetricRegistry, Timer}

/**
 * Created by kinshuk.bairagi on 11/02/16.
 */
trait InstrumentedBase {

  def registry:MetricRegistry

  protected def getMetricName(name: String): String = com.codahale.metrics.MetricRegistry.name(getClass, name).replace("$","")

  def profile[T](metricName: String)(fn: ⇒ T): T = {
    val context = registry.timer(getMetricName(metricName)).time()
    try {
      fn
    } finally {
      context.stop()
    }
  }

  def counter(metricName: String): Counter = registry.counter(getMetricName(metricName))

  def meter(metricName: String): Meter = registry.meter(getMetricName(metricName))

  def timer(metricName: String): Timer = registry.timer(getMetricName(metricName))


}


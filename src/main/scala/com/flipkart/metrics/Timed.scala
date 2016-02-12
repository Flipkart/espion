package com.flipkart.metrics


import scala.annotation.StaticAnnotation
import scala.collection.immutable.::
import scala.reflect.macros.whitebox
import scala.language.experimental.macros

/**
 * Created by kinshuk.bairagi on 11/02/16.
 *
 * Annotation for timer metric of functions.
 * Your class must extend Instrumented for this annotation to work.
 *
 */
class Timed(name: String) extends StaticAnnotation {

  def macroTransform(annottees: Any*) = macro TimedImpl.impl

}

object TimedImpl {

  def impl(c: whitebox.Context)(annottees: c.Expr[Any]*): c.Expr[Any] = {
    import c.universe._

    def extractAnnotationParameters(tree: Tree): List[c.universe.Tree] = tree match {
      case q"new $name( ..$params )" => params
      case _ => throw new Exception("Timed annotation must have the meter name.")
    }

    def getMeterName(tree: Tree):String = tree match {
      case Literal(Constant(field: String)) => field
      case _ => c.abort(c.enclosingPosition, s"[getMeterName] Match error with $tree")
    }

    val result = {
      annottees.map(_.tree).toList match {
        case q"$mods def $tname[..$tparams](...$paramss): $tpt = { ..$body }" :: Nil =>

          val annotationParams = extractAnnotationParameters(c.prefix.tree)
          val meterName = annotationParams.map(getMeterName).head

          q"""
             $mods def $tname[..$tparams](...$paramss): $tpt = {
               val timer = registry.timer(getMetricName($meterName))
               val context = timer.time()
               try {
               ..$body
               } finally {
                  context.stop()
             }
             }
           """

      }

    }
    c.Expr[Any](result)
  }
}
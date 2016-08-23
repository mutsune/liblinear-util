package com.mutsune.liblinear.util

import scala.collection.mutable

/**
  * Created by nakayama.
  */
case class LiblinearInstance(classLabel: String,
                             features: mutable.Map[String, Double])

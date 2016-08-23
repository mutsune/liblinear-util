package com.mutsune.liblinear.util

import de.bwaldvogel.liblinear.{Feature, FeatureNode, Linear, Model}

/**
  * Created by nakayama.
  */
final case class Predictor(model: Model, liblinearMapper: LiblinearMapper) {

    def predict(features: Map[String, Double]): Int =
        Linear.predict(model, liblinearPredictFormat(features)).toInt

    def predictProbability(features: Map[String, Double]): (String, Double) = {
        val probabilityResults = new Array[Double](2)
        val labelIndex: Int =
            Linear.predictProbability(model, liblinearPredictFormat(features), probabilityResults).toInt
        val label: String = liblinearMapper.classIndexToClassLabel(labelIndex) match {
            case Some(l) => l
            case _ => throw new RuntimeException("Unknown label index: " + labelIndex)
        }
        // Positive: probabilityResults(0), Negative: probabilityResults(1)
        (label, probabilityResults(0))
    }

    def liblinearPredictFormat(features: Map[String, Double]): Array[Feature] = {
        features.collect {
            // Collect features which are only appeared in a training set
            case (k, v) if liblinearMapper.featureNameIndex.contains(k) =>
                new FeatureNode(liblinearMapper.featureNameIndex(k), v)
        }.toArray
    }

}

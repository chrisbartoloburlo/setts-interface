S_fs=!DefaultApi.addProduct(productName: String(const "Product_1")).?C201().rec X.(
    +{!DefaultApi.addConfiguration(productName, configName: String(configName))<0.3>.?C201().
        !DefaultApi.getConfigurationsForProduct(productName).?C200(body: "Seq[String]")<body.contains(configName)>.
        !DefaultApi.getConfigurationWithNameForProduct(productName, configName).?C200(config: ProductConfiguration)<util.checkConfig(configName, config)>.
        !DefaultApi.addFeatureToProduct(productName, featureName1: String(setFeatureName1), description: String(descr)).?C201().
            +{!DefaultApi.addFeatureToProduct(productName, featureName2: String(setFeatureName2), description: String(descr))<0.4>.?C201().
                +{!DefaultApi.addRequiresConstraintToProduct(productName, featureName2, featureName1)<0.3>.?C201().
                    !DefaultApi.getProductByName(productName).?C200(product: Product)<util.setProduct(product)>.
                    !DefaultApi.addFeatureToConfiguration(productName, configName, featureName2).?C201().
                        +{!DefaultApi.deleteFeature(productName, configName, featureName1)<0.3>.?C400().X,
                        !DefaultApi.deleteConstraint(productName, constraintId: Long(getProductId))<0.3>.?C204().X,
                        !DefaultApi.getConfigurationActivedFeatures(productName, configName)<0.4>.?C200(activeFeatures: "Seq[String]")<activeFeatures.contains(featureName1)&&activeFeatures.contains(featureName2)>.X},
                !DefaultApi.addExcludesConstraintToProduct(productName, featureName2, featureName1)<0.3>.?C201().
                    !DefaultApi.getProductByName(productName).?C200(product: Product)<util.setProduct(product)>.
                    !DefaultApi.addFeatureToConfiguration(productName, configName, featureName2).?C201().
                        +{!DefaultApi.deleteFeatureOfProduct(productName, featureName2)<0.3>.?C204().X,
                        !DefaultApi.addFeatureToConfiguration(productName, configName, featureName1)<0.3>.?C400().X,
                        !DefaultApi.deleteConstraint(productName, constraintId: Long(getProductId))<0.4>.?C204().X},
                !DefaultApi.deleteConfiguration(productName, configName)<0.2>.?C204().X,
                !DefaultApi.getFeaturesForProduct(productName)<0.2>.?C200(features: "Set[Feature]")<util.checkFeatures(features, featureName1, featureName2)>.X},
            !DefaultApi.updateFeatureOfProduct(productName, featureName1)<0.15>.?C200(body: Feature)<body.name.get==featureName1>.X,
            !DefaultApi.updateFeatureOfProduct(productName, featureName3: String(setFeatureName3))<0.15>.?C404().X,
            !DefaultApi.deleteFeatureOfProduct(productName, featureName1)<0.1>.?C204().X,
            !DefaultApi.addFeatureToConfiguration(productName, configName, featureName1)<0.1>.?C201().
                +{!DefaultApi.addFeatureToConfiguration(productName, configName, featureName1)<0.5>.?C400().X,
                !DefaultApi.deleteFeature(productName, configName, featureName1)<0.5>.?C204().X},
            !DefaultApi.addFeatureToProduct(productName, featureName1, description: String(descr))<0.1>.?C400().X
            },
    !DefaultApi.getAllProducts()<0.3>.?C200(body: "Seq[String]")<body.contains(productName)>.X,
    !DefaultApi.getProductByName(productName)<0.3>.?C200(body: Product)<productName==body.name.get>.!DefaultApi.deleteProductByName(productName).?C204(),
    !DefaultApi.getProductByName(nonExistentProductName: String(const "Product_2"))<0.1>.?C404()})

package examples.featuresservice
import examples.featuresservice.api._
import examples.featuresservice.model._
import sttp.client3._
import driver.util.{logger, csvLogger}
import scala.util.control.TailCalls.{TailRec, done, tailcall}
import java.io.File
class driver(setup: => Unit, teardown: => Unit, minimise: Boolean, max: Int, var seed: Int, repetitions: Int, report: String => Unit) extends Runnable {
	object info {
		object addProduct_62 {
			var productName: String = _
			var prob = -1.0
		}
		object C201_61 {
			var freq = 0
		}
		object addConfiguration_52 {
			var configName: String = _
			var prob = 0.3
		}
		object C201_51 {
			var freq = 0
		}
		object getConfigurationsForProduct_50 {
			var prob = -1.0
		}
		object C200_49 {
			var body: Seq[String] = _
			var freq = 0
		}
		object getConfigurationWithNameForProduct_48 {
			var prob = -1.0
		}
		object C200_47 {
			var config: ProductConfiguration = _
			var freq = 0
		}
		object addFeatureToProduct_46 {
			var featureName1: String = _
			var description: String = _
			var prob = -1.0
		}
		object C201_45 {
			var freq = 0
		}
		object addFeatureToProduct_30 {
			var featureName2: String = _
			var description: String = _
			var prob = 0.4
		}
		object C201_29 {
			var freq = 0
		}
		object addRequiresConstraintToProduct_12 {
			var prob = 0.3
		}
		object C201_11 {
			var freq = 0
		}
		object getProductByName_10 {
			var prob = -1.0
		}
		object C200_9 {
			var product: Product = _
			var freq = 0
		}
		object addFeatureToConfiguration_8 {
			var prob = -1.0
		}
		object C201_7 {
			var freq = 0
		}
		object deleteFeature_2 {
			var prob = 0.3
		}
		object C400_1 {
			var freq = 0
		}
		object deleteConstraint_4 {
			var constraintId: Long = _
			var prob = 0.3
		}
		object C204_3 {
			var freq = 0
		}
		object getConfigurationActivedFeatures_6 {
			var prob = 0.4
		}
		object C200_5 {
			var activeFeatures: Seq[String] = _
			var freq = 0
		}
		object addExcludesConstraintToProduct_24 {
			var prob = 0.3
		}
		object C201_23 {
			var freq = 0
		}
		object getProductByName_22 {
			var prob = -1.0
		}
		object C200_21 {
			var product: Product = _
			var freq = 0
		}
		object addFeatureToConfiguration_20 {
			var prob = -1.0
		}
		object C201_19 {
			var freq = 0
		}
		object deleteFeatureOfProduct_14 {
			var prob = 0.3
		}
		object C204_13 {
			var freq = 0
		}
		object addFeatureToConfiguration_16 {
			var prob = 0.3
		}
		object C400_15 {
			var freq = 0
		}
		object deleteConstraint_18 {
			var constraintId: Long = _
			var prob = 0.4
		}
		object C204_17 {
			var freq = 0
		}
		object deleteConfiguration_26 {
			var prob = 0.2
		}
		object C204_25 {
			var freq = 0
		}
		object getFeaturesForProduct_28 {
			var prob = 0.2
		}
		object C200_27 {
			var features: Set[Feature] = _
			var freq = 0
		}
		object updateFeatureOfProduct_32 {
			var prob = 0.15
		}
		object C200_31 {
			var body: Feature = _
			var freq = 0
		}
		object updateFeatureOfProduct_34 {
			var featureName3: String = _
			var prob = 0.15
		}
		object C404_33 {
			var freq = 0
		}
		object deleteFeatureOfProduct_36 {
			var prob = 0.1
		}
		object C204_35 {
			var freq = 0
		}
		object addFeatureToConfiguration_42 {
			var prob = 0.1
		}
		object C201_41 {
			var freq = 0
		}
		object addFeatureToConfiguration_38 {
			var prob = 0.5
		}
		object C400_37 {
			var freq = 0
		}
		object deleteFeature_40 {
			var prob = 0.5
		}
		object C204_39 {
			var freq = 0
		}
		object addFeatureToProduct_44 {
			var description: String = _
			var prob = 0.1
		}
		object C400_43 {
			var freq = 0
		}
		object getAllProducts_54 {
			var prob = 0.3
		}
		object C200_53 {
			var body: Seq[String] = _
			var freq = 0
		}
		object getProductByName_58 {
			var prob = 0.3
		}
		object C200_57 {
			var body: Product = _
			var freq = 0
		}
		object deleteProductByName_56 {
			var prob = -1.0
		}
		object C204_55 {
			var freq = 0
		}
		object getProductByName_60 {
			var nonExistentProductName: String = _
			var prob = 0.1
		}
		object C404_59 {
			var freq = 0
		}
	}
	abstract class DriverException(choice: Any, message: String) extends Exception {
		def getInfo: (Any, String) = {
			(choice, message)
		}
	}
	class InvalidMessageException(choice: Any, message: String) extends DriverException(choice, message)
	class AssertionViolationException(choice: Any, message: String) extends DriverException(choice, message)
	class InvalidTestException(choice: Any, message: String) extends DriverException(choice, message)
	var InvalidMessageExceptions:collection.mutable.Map[Any, List[(InvalidMessageException, String, Int)]] = collection.mutable.Map()
	var AssertionViolationExceptions:collection.mutable.Map[Any, List[(AssertionViolationException, String, Int)]] = collection.mutable.Map()
	var r = new scala.util.Random(seed)
	var passed = true
	var pass = 0.0
	var fail = 0.0
	val animationChars = List[Char]('|', '/', '-', '\\')
	val backend: SttpBackend[Identity, Any] = HttpURLConnectionBackend()
	val defaultApi = DefaultApi.apply()
	override def run(): Unit = {
		report("[DRIVER] Starting tests...\n")
		val l: csvLogger = new csvLogger(f"${System.getProperty("user.dir")}/logs/S_fs_tests.csv")
		l.log("rep seed passed seq_length sequence")
		report("\n")
		for(rep <- 1 to repetitions){
			setup
			report("\u001b[1A\u001b[2K")
			report(f"[DRIVER] Running test: $rep ${animationChars(rep % 4)}\n")
			val sequence = new StringBuilder()
			val curlSequence = new StringBuilder()
			try {
				sendaddProduct_62(0, sequence, curlSequence).result
				pass+=1
			} catch {
				case e: InvalidMessageException =>
					sequence.append(e.getInfo._2)
					val e_s_r: (InvalidMessageException, String, Int) = InvalidMessageExceptions.getOrElse(e.getInfo._1, List():+(e, sequence.toString(), rep)).minBy(e_s => e_s._2.length)
					InvalidMessageExceptions.update(e.getInfo._1, InvalidMessageExceptions.getOrElse(e.getInfo._1, List()):+(e, sequence.toString(), rep))
					if(minimise) {
						if(e_s_r._2.length > sequence.length() || e_s_r._2.length == sequence.length() && e_s_r._1==e) {
							new File(f"${System.getProperty("user.dir")}/debug/S_fs/invalid_msg_${e_s_r._3}.sh").delete()
							val debugLog: logger = new logger(f"${System.getProperty("user.dir")}/debug/S_fs/invalid_msg_$rep.sh")
							debugLog.log(curlSequence.toString())
						}
					} else {
						val debugLog: logger = new logger(f"${System.getProperty("user.dir")}/debug/S_pc/invalid_msg_$rep.sh")
						debugLog.log(curlSequence.toString())
					}
					passed=false; fail+=1
					teardown
				case e: AssertionViolationException =>
					sequence.append(e.getInfo._2)
					val e_s_r: (AssertionViolationException, String, Int) = AssertionViolationExceptions.getOrElse(e.getInfo._1, List():+(e, sequence.toString(), rep)).minBy(e_s => e_s._2.length)
					AssertionViolationExceptions.update(e.getInfo._1, AssertionViolationExceptions.getOrElse(e.getInfo._1, List()):+(e, sequence.toString(), rep))
					if(minimise) {
						if(e_s_r._2.length > sequence.length() || e_s_r._2.length == sequence.length() && e_s_r._1==e) {
							new File(f"${System.getProperty("user.dir")}/debug/S_fs/assertion_violation_${e_s_r._3}.sh").delete()
							val debugLog: logger = new logger(f"${System.getProperty("user.dir")}/debug/S_fs/assertion_violation_$rep.sh")
							debugLog.log(curlSequence.toString())
						}
					} else {
						val debugLog: logger = new logger(f"${System.getProperty("user.dir")}/debug/S_pc/assertion_violation_$rep.sh")
						debugLog.log(curlSequence.toString())
					}
					passed=false; fail+=1
					teardown
				case e: InvalidTestException =>
					sequence.append(e)
					l.log(f"$rep $seed $passed ${sequence.toString().split('.').length} ${sequence.insert(0,"\"").append("\"").toString()}")
					teardown
					throw new Exception(f"Error in test (choice: ${e.getInfo._1}) ${e.getInfo._2}")
				case e: Throwable =>
					sequence.append(e.getMessage)
					l.log(f"$rep $seed $passed ${sequence.toString().split('.').length} ${sequence.insert(0,"\"").append("\"").toString()}")
					teardown
					throw new Exception("Error in test")
			}
			l.log(f"$rep $seed $passed ${sequence.toString().split('.').length} ${sequence.insert(0,"\"").append("\"").toString()}")
			passed=true; seed=r.nextInt(); r=new scala.util.Random(seed)
		}
		reportSummary()
	}
	def reportSummary(): Unit = {
		report("[DRIVER] TESTS SUMMARY\n")
		report("Number of tests: "+repetitions+"\n")
		report("Passed (%): "+(pass/repetitions.toDouble)*100+"\n")
		report("Failed (%): "+(fail/repetitions.toDouble)*100+"\n")
		report("Invalid Message violations: "+InvalidMessageExceptions.size+"\n")
		report("Assertion violations: "+AssertionViolationExceptions.size+"\n")
	}
	def sendaddProduct_62(count: Int, sequence: StringBuilder, curlSequence: StringBuilder): TailRec[Unit] = {
		var productName = util.const("Product_1",r)
		val request = defaultApi.addProduct(productName)
val response = request.send(backend)
		sequence.append(f"!addProduct($productName).")
		curlSequence.append(request.toCurl+"\n\n")
		info.addProduct_62.productName = productName
		if (count < max) {
			receiveC201_61(response.code.toString.toInt, null, count+1, sequence, curlSequence)
		} else { tailcall(receiveC201_61(response.code.toString.toInt, null, 0, sequence, curlSequence)) }
	}
	def receiveC201_61(responseCode: Int, body: Null, count: Int, sequence: StringBuilder, curlSequence: StringBuilder): TailRec[Unit] = {
		responseCode match {
			case code @ 201 =>
			sequence.append(f"?$code().")
				info.C201_61.freq+=1
				if (count < max) {
					sendInternalChoice6(count+1, sequence, curlSequence)
				} else { tailcall(sendInternalChoice6(0, sequence, curlSequence)) }
			case code @ _ => sequence.append(f"?$code()."); passed=false; throw new InvalidMessageException("C201_61", f"Unknown message: $code"); 
		}
	}
	def sendInternalChoice6(count: Int, sequence: StringBuilder, curlSequence: StringBuilder): TailRec[Unit] = {
		val rand = r.nextDouble()
		if (rand <= info.addConfiguration_52.prob){
			var configName = util.configName(r)
		val request = defaultApi.addConfiguration(info.addProduct_62.productName, configName)
val response = request.send(backend)
		sequence.append(f"!addConfiguration(${info.addProduct_62.productName}, $configName).")
		curlSequence.append(request.toCurl+"\n\n")
		info.addConfiguration_52.configName = configName
		if (count < max) {
			receiveC201_51(response.code.toString.toInt, null, count+1, sequence, curlSequence)
		} else { tailcall(receiveC201_51(response.code.toString.toInt, null, 0, sequence, curlSequence)) }
		} else if (rand <= info.getAllProducts_54.prob+0.3){
		val request = defaultApi.getAllProducts()
val response = request.send(backend)
		sequence.append(f"!getAllProducts().")
		curlSequence.append(request.toCurl+"\n\n")
		try {
			if (count < max) {
			receiveC200_53(response.code.toString.toInt, if(response.body.isInstanceOf[Right[Any, Any]]) response.body.asInstanceOf[Right[Any, Any]].value.asInstanceOf[Seq[String]] else response.body.asInstanceOf[Left[Any, Any]].value.asInstanceOf[Seq[String]], count+1, sequence, curlSequence)
		} else { tailcall(receiveC200_53(response.code.toString.toInt, if(response.body.isInstanceOf[Right[Any, Any]]) response.body.asInstanceOf[Right[Any, Any]].value.asInstanceOf[Seq[String]] else response.body.asInstanceOf[Left[Any, Any]].value.asInstanceOf[Seq[String]], 0, sequence, curlSequence)) }
		} catch {
			case e: java.lang.ClassCastException =>
				throw new InvalidMessageException("C200_53", f"Unexpected response: $response")
			}
		} else if (rand <= info.getProductByName_58.prob+0.6){
		val request = defaultApi.getProductByName(info.addProduct_62.productName)
val response = request.send(backend)
		sequence.append(f"!getProductByName_58(${info.addProduct_62.productName}).")
		curlSequence.append(request.toCurl+"\n\n")
		try {
			if (count < max) {
			receiveC200_57(response.code.toString.toInt, if(response.body.isInstanceOf[Right[Any, Any]]) response.body.asInstanceOf[Right[Any, Any]].value.asInstanceOf[Product] else response.body.asInstanceOf[Left[Any, Any]].value.asInstanceOf[Product], count+1, sequence, curlSequence)
		} else { tailcall(receiveC200_57(response.code.toString.toInt, if(response.body.isInstanceOf[Right[Any, Any]]) response.body.asInstanceOf[Right[Any, Any]].value.asInstanceOf[Product] else response.body.asInstanceOf[Left[Any, Any]].value.asInstanceOf[Product], 0, sequence, curlSequence)) }
		} catch {
			case e: java.lang.ClassCastException =>
				throw new InvalidMessageException("C200_57", f"Unexpected response: $response")
			}
		} else if (rand <= info.getProductByName_60.prob+0.8999999999999999){
			var nonExistentProductName = util.const("Product_2",r)
		val request = defaultApi.getProductByName(nonExistentProductName)
val response = request.send(backend)
		sequence.append(f"!getProductByName_60($nonExistentProductName).")
		curlSequence.append(request.toCurl+"\n\n")
		if (count < max) {
			receiveC404_59(response.code.toString.toInt, null, count+1, sequence, curlSequence)
		} else { tailcall(receiveC404_59(response.code.toString.toInt, null, 0, sequence, curlSequence)) }
		} else { throw new Exception("[DRIVER] Error in test") }
	}
	def receiveC201_51(responseCode: Int, body: Null, count: Int, sequence: StringBuilder, curlSequence: StringBuilder): TailRec[Unit] = {
		responseCode match {
			case code @ 201 =>
			sequence.append(f"?$code().")
				info.C201_51.freq+=1
				if (count < max) {
					sendgetConfigurationsForProduct_50(count+1, sequence, curlSequence)
				} else { tailcall(sendgetConfigurationsForProduct_50(0, sequence, curlSequence)) }
			case code @ _ => sequence.append(f"?$code()."); passed=false; throw new InvalidMessageException("C201_51", f"Unknown message: $code"); 
		}
	}
	def sendgetConfigurationsForProduct_50(count: Int, sequence: StringBuilder, curlSequence: StringBuilder): TailRec[Unit] = {
		val request = defaultApi.getConfigurationsForProduct(info.addProduct_62.productName)
val response = request.send(backend)
		sequence.append(f"!getConfigurationsForProduct(${info.addProduct_62.productName}).")
		curlSequence.append(request.toCurl+"\n\n")
		try {
			if (count < max) {
			receiveC200_49(response.code.toString.toInt, if(response.body.isInstanceOf[Right[Any, Any]]) response.body.asInstanceOf[Right[Any, Any]].value.asInstanceOf[Seq[String]] else response.body.asInstanceOf[Left[Any, Any]].value.asInstanceOf[Seq[String]], count+1, sequence, curlSequence)
		} else { tailcall(receiveC200_49(response.code.toString.toInt, if(response.body.isInstanceOf[Right[Any, Any]]) response.body.asInstanceOf[Right[Any, Any]].value.asInstanceOf[Seq[String]] else response.body.asInstanceOf[Left[Any, Any]].value.asInstanceOf[Seq[String]], 0, sequence, curlSequence)) }
		} catch {
			case e: java.lang.ClassCastException =>
				throw new InvalidMessageException("C200_49", f"Unexpected response: $response")
			}
	}
	def receiveC200_49(responseCode: Int, body: Seq[String], count: Int, sequence: StringBuilder, curlSequence: StringBuilder): TailRec[Unit] = {
		responseCode match {
			case code @ 200 =>
			sequence.append(f"?$code($body).")
				if(body.contains(info.addConfiguration_52.configName)){
					info.C200_49.freq+=1
					if (count < max) {
						sendgetConfigurationWithNameForProduct_48(count+1, sequence, curlSequence)
					} else { tailcall(sendgetConfigurationWithNameForProduct_48(0, sequence, curlSequence)) }
				} else {
				passed=false; throw new AssertionViolationException("C200_49", "Violation in Assertion: body.contains(configName)");  }
			case code @ _ => sequence.append(f"?$code($body)."); passed=false; throw new InvalidMessageException("C200_49", f"Unknown message: $code"); 
		}
	}
	def sendgetConfigurationWithNameForProduct_48(count: Int, sequence: StringBuilder, curlSequence: StringBuilder): TailRec[Unit] = {
		val request = defaultApi.getConfigurationWithNameForProduct(info.addProduct_62.productName, info.addConfiguration_52.configName)
val response = request.send(backend)
		sequence.append(f"!getConfigurationWithNameForProduct(${info.addProduct_62.productName}, ${info.addConfiguration_52.configName}).")
		curlSequence.append(request.toCurl+"\n\n")
		try {
			if (count < max) {
			receiveC200_47(response.code.toString.toInt, if(response.body.isInstanceOf[Right[Any, Any]]) response.body.asInstanceOf[Right[Any, Any]].value.asInstanceOf[ProductConfiguration] else response.body.asInstanceOf[Left[Any, Any]].value.asInstanceOf[ProductConfiguration], count+1, sequence, curlSequence)
		} else { tailcall(receiveC200_47(response.code.toString.toInt, if(response.body.isInstanceOf[Right[Any, Any]]) response.body.asInstanceOf[Right[Any, Any]].value.asInstanceOf[ProductConfiguration] else response.body.asInstanceOf[Left[Any, Any]].value.asInstanceOf[ProductConfiguration], 0, sequence, curlSequence)) }
		} catch {
			case e: java.lang.ClassCastException =>
				throw new InvalidMessageException("C200_47", f"Unexpected response: $response")
			}
	}
	def receiveC200_47(responseCode: Int, config: ProductConfiguration, count: Int, sequence: StringBuilder, curlSequence: StringBuilder): TailRec[Unit] = {
		responseCode match {
			case code @ 200 =>
			sequence.append(f"?$code($config).")
				if(util.checkConfig(info.addConfiguration_52.configName, config)){
					info.C200_47.freq+=1
					if (count < max) {
						sendaddFeatureToProduct_46(count+1, sequence, curlSequence)
					} else { tailcall(sendaddFeatureToProduct_46(0, sequence, curlSequence)) }
				} else {
				passed=false; throw new AssertionViolationException("C200_47", "Violation in Assertion: util.checkConfig(configName, config)");  }
			case code @ _ => sequence.append(f"?$code($config)."); passed=false; throw new InvalidMessageException("C200_47", f"Unknown message: $code"); 
		}
	}
	def sendaddFeatureToProduct_46(count: Int, sequence: StringBuilder, curlSequence: StringBuilder): TailRec[Unit] = {
		var featureName1 = util.setFeatureName1(r)
		var description = util.descr(r)
		val request = defaultApi.addFeatureToProduct(info.addProduct_62.productName, featureName1, description)
val response = request.send(backend)
		sequence.append(f"!addFeatureToProduct_46(${info.addProduct_62.productName}, $featureName1, $description).")
		curlSequence.append(request.toCurl+"\n\n")
		info.addFeatureToProduct_46.featureName1 = featureName1
		if (count < max) {
			receiveC201_45(response.code.toString.toInt, null, count+1, sequence, curlSequence)
		} else { tailcall(receiveC201_45(response.code.toString.toInt, null, 0, sequence, curlSequence)) }
	}
	def receiveC201_45(responseCode: Int, body: Null, count: Int, sequence: StringBuilder, curlSequence: StringBuilder): TailRec[Unit] = {
		responseCode match {
			case code @ 201 =>
			sequence.append(f"?$code().")
				info.C201_45.freq+=1
				if (count < max) {
					sendInternalChoice5(count+1, sequence, curlSequence)
				} else { tailcall(sendInternalChoice5(0, sequence, curlSequence)) }
			case code @ _ => sequence.append(f"?$code()."); passed=false; throw new InvalidMessageException("C201_45", f"Unknown message: $code"); 
		}
	}
	def sendInternalChoice5(count: Int, sequence: StringBuilder, curlSequence: StringBuilder): TailRec[Unit] = {
		val rand = r.nextDouble()
		if (rand <= info.addFeatureToProduct_30.prob){
			var featureName2 = util.setFeatureName2(r)
			var description = util.descr(r)
		val request = defaultApi.addFeatureToProduct(info.addProduct_62.productName, featureName2, description)
val response = request.send(backend)
		sequence.append(f"!addFeatureToProduct_30(${info.addProduct_62.productName}, $featureName2, $description).")
		curlSequence.append(request.toCurl+"\n\n")
		info.addFeatureToProduct_30.featureName2 = featureName2
		if (count < max) {
			receiveC201_29(response.code.toString.toInt, null, count+1, sequence, curlSequence)
		} else { tailcall(receiveC201_29(response.code.toString.toInt, null, 0, sequence, curlSequence)) }
		} else if (rand <= info.updateFeatureOfProduct_32.prob+0.4){
		val request = defaultApi.updateFeatureOfProduct(info.addProduct_62.productName, info.addFeatureToProduct_46.featureName1)
val response = request.send(backend)
		sequence.append(f"!updateFeatureOfProduct_32(${info.addProduct_62.productName}, ${info.addFeatureToProduct_46.featureName1}).")
		curlSequence.append(request.toCurl+"\n\n")
		try {
			if (count < max) {
			receiveC200_31(response.code.toString.toInt, if(response.body.isInstanceOf[Right[Any, Any]]) response.body.asInstanceOf[Right[Any, Any]].value.asInstanceOf[Feature] else response.body.asInstanceOf[Left[Any, Any]].value.asInstanceOf[Feature], count+1, sequence, curlSequence)
		} else { tailcall(receiveC200_31(response.code.toString.toInt, if(response.body.isInstanceOf[Right[Any, Any]]) response.body.asInstanceOf[Right[Any, Any]].value.asInstanceOf[Feature] else response.body.asInstanceOf[Left[Any, Any]].value.asInstanceOf[Feature], 0, sequence, curlSequence)) }
		} catch {
			case e: java.lang.ClassCastException =>
				throw new InvalidMessageException("C200_31", f"Unexpected response: $response")
			}
		} else if (rand <= info.updateFeatureOfProduct_34.prob+0.55){
			var featureName3 = util.setFeatureName3(r)
		val request = defaultApi.updateFeatureOfProduct(info.addProduct_62.productName, featureName3)
val response = request.send(backend)
		sequence.append(f"!updateFeatureOfProduct_34(${info.addProduct_62.productName}, $featureName3).")
		curlSequence.append(request.toCurl+"\n\n")
		if (count < max) {
			receiveC404_33(response.code.toString.toInt, null, count+1, sequence, curlSequence)
		} else { tailcall(receiveC404_33(response.code.toString.toInt, null, 0, sequence, curlSequence)) }
		} else if (rand <= info.deleteFeatureOfProduct_36.prob+0.7000000000000001){
		val request = defaultApi.deleteFeatureOfProduct(info.addProduct_62.productName, info.addFeatureToProduct_46.featureName1)
val response = request.send(backend)
		sequence.append(f"!deleteFeatureOfProduct_36(${info.addProduct_62.productName}, ${info.addFeatureToProduct_46.featureName1}).")
		curlSequence.append(request.toCurl+"\n\n")
		if (count < max) {
			receiveC204_35(response.code.toString.toInt, null, count+1, sequence, curlSequence)
		} else { tailcall(receiveC204_35(response.code.toString.toInt, null, 0, sequence, curlSequence)) }
		} else if (rand <= info.addFeatureToConfiguration_42.prob+0.8){
		val request = defaultApi.addFeatureToConfiguration(info.addProduct_62.productName, info.addConfiguration_52.configName, info.addFeatureToProduct_46.featureName1)
val response = request.send(backend)
		sequence.append(f"!addFeatureToConfiguration_42(${info.addProduct_62.productName}, ${info.addConfiguration_52.configName}, ${info.addFeatureToProduct_46.featureName1}).")
		curlSequence.append(request.toCurl+"\n\n")
		if (count < max) {
			receiveC201_41(response.code.toString.toInt, null, count+1, sequence, curlSequence)
		} else { tailcall(receiveC201_41(response.code.toString.toInt, null, 0, sequence, curlSequence)) }
		} else if (rand <= info.addFeatureToProduct_44.prob+0.9){
			var description = util.descr(r)
		val request = defaultApi.addFeatureToProduct(info.addProduct_62.productName, info.addFeatureToProduct_46.featureName1, description)
val response = request.send(backend)
		sequence.append(f"!addFeatureToProduct_44(${info.addProduct_62.productName}, ${info.addFeatureToProduct_46.featureName1}, $description).")
		curlSequence.append(request.toCurl+"\n\n")
		if (count < max) {
			receiveC400_43(response.code.toString.toInt, null, count+1, sequence, curlSequence)
		} else { tailcall(receiveC400_43(response.code.toString.toInt, null, 0, sequence, curlSequence)) }
		} else { throw new Exception("[DRIVER] Error in test") }
	}
	def receiveC201_29(responseCode: Int, body: Null, count: Int, sequence: StringBuilder, curlSequence: StringBuilder): TailRec[Unit] = {
		responseCode match {
			case code @ 201 =>
			sequence.append(f"?$code().")
				info.C201_29.freq+=1
				if (count < max) {
					sendInternalChoice3(count+1, sequence, curlSequence)
				} else { tailcall(sendInternalChoice3(0, sequence, curlSequence)) }
			case code @ _ => sequence.append(f"?$code()."); passed=false; throw new InvalidMessageException("C201_29", f"Unknown message: $code"); 
		}
	}
	def sendInternalChoice3(count: Int, sequence: StringBuilder, curlSequence: StringBuilder): TailRec[Unit] = {
		val rand = r.nextDouble()
		if (rand <= info.addRequiresConstraintToProduct_12.prob){
		val request = defaultApi.addRequiresConstraintToProduct(info.addProduct_62.productName, info.addFeatureToProduct_30.featureName2, info.addFeatureToProduct_46.featureName1)
val response = request.send(backend)
		sequence.append(f"!addRequiresConstraintToProduct(${info.addProduct_62.productName}, ${info.addFeatureToProduct_30.featureName2}, ${info.addFeatureToProduct_46.featureName1}).")
		curlSequence.append(request.toCurl+"\n\n")
		if (count < max) {
			receiveC201_11(response.code.toString.toInt, null, count+1, sequence, curlSequence)
		} else { tailcall(receiveC201_11(response.code.toString.toInt, null, 0, sequence, curlSequence)) }
		} else if (rand <= info.addExcludesConstraintToProduct_24.prob+0.3){
		val request = defaultApi.addExcludesConstraintToProduct(info.addProduct_62.productName, info.addFeatureToProduct_30.featureName2, info.addFeatureToProduct_46.featureName1)
val response = request.send(backend)
		sequence.append(f"!addExcludesConstraintToProduct(${info.addProduct_62.productName}, ${info.addFeatureToProduct_30.featureName2}, ${info.addFeatureToProduct_46.featureName1}).")
		curlSequence.append(request.toCurl+"\n\n")
		if (count < max) {
			receiveC201_23(response.code.toString.toInt, null, count+1, sequence, curlSequence)
		} else { tailcall(receiveC201_23(response.code.toString.toInt, null, 0, sequence, curlSequence)) }
		} else if (rand <= info.deleteConfiguration_26.prob+0.6){
		val request = defaultApi.deleteConfiguration(info.addProduct_62.productName, info.addConfiguration_52.configName)
val response = request.send(backend)
		sequence.append(f"!deleteConfiguration(${info.addProduct_62.productName}, ${info.addConfiguration_52.configName}).")
		curlSequence.append(request.toCurl+"\n\n")
		if (count < max) {
			receiveC204_25(response.code.toString.toInt, null, count+1, sequence, curlSequence)
		} else { tailcall(receiveC204_25(response.code.toString.toInt, null, 0, sequence, curlSequence)) }
		} else if (rand <= info.getFeaturesForProduct_28.prob+0.8){
		val request = defaultApi.getFeaturesForProduct(info.addProduct_62.productName)
val response = request.send(backend)
		sequence.append(f"!getFeaturesForProduct(${info.addProduct_62.productName}).")
		curlSequence.append(request.toCurl+"\n\n")
		try {
			if (count < max) {
			receiveC200_27(response.code.toString.toInt, if(response.body.isInstanceOf[Right[Any, Any]]) response.body.asInstanceOf[Right[Any, Any]].value.asInstanceOf[Set[Feature]] else response.body.asInstanceOf[Left[Any, Any]].value.asInstanceOf[Set[Feature]], count+1, sequence, curlSequence)
		} else { tailcall(receiveC200_27(response.code.toString.toInt, if(response.body.isInstanceOf[Right[Any, Any]]) response.body.asInstanceOf[Right[Any, Any]].value.asInstanceOf[Set[Feature]] else response.body.asInstanceOf[Left[Any, Any]].value.asInstanceOf[Set[Feature]], 0, sequence, curlSequence)) }
		} catch {
			case e: java.lang.ClassCastException =>
				throw new InvalidMessageException("C200_27", f"Unexpected response: $response")
			}
		} else { throw new Exception("[DRIVER] Error in test") }
	}
	def receiveC201_11(responseCode: Int, body: Null, count: Int, sequence: StringBuilder, curlSequence: StringBuilder): TailRec[Unit] = {
		responseCode match {
			case code @ 201 =>
			sequence.append(f"?$code().")
				info.C201_11.freq+=1
				if (count < max) {
					sendgetProductByName_10(count+1, sequence, curlSequence)
				} else { tailcall(sendgetProductByName_10(0, sequence, curlSequence)) }
			case code @ _ => sequence.append(f"?$code()."); passed=false; throw new InvalidMessageException("C201_11", f"Unknown message: $code"); 
		}
	}
	def sendgetProductByName_10(count: Int, sequence: StringBuilder, curlSequence: StringBuilder): TailRec[Unit] = {
		val request = defaultApi.getProductByName(info.addProduct_62.productName)
val response = request.send(backend)
		sequence.append(f"!getProductByName_10(${info.addProduct_62.productName}).")
		curlSequence.append(request.toCurl+"\n\n")
		try {
			if (count < max) {
			receiveC200_9(response.code.toString.toInt, if(response.body.isInstanceOf[Right[Any, Any]]) response.body.asInstanceOf[Right[Any, Any]].value.asInstanceOf[Product] else response.body.asInstanceOf[Left[Any, Any]].value.asInstanceOf[Product], count+1, sequence, curlSequence)
		} else { tailcall(receiveC200_9(response.code.toString.toInt, if(response.body.isInstanceOf[Right[Any, Any]]) response.body.asInstanceOf[Right[Any, Any]].value.asInstanceOf[Product] else response.body.asInstanceOf[Left[Any, Any]].value.asInstanceOf[Product], 0, sequence, curlSequence)) }
		} catch {
			case e: java.lang.ClassCastException =>
				throw new InvalidMessageException("C200_9", f"Unexpected response: $response")
			}
	}
	def receiveC200_9(responseCode: Int, product: Product, count: Int, sequence: StringBuilder, curlSequence: StringBuilder): TailRec[Unit] = {
		responseCode match {
			case code @ 200 =>
			sequence.append(f"?$code($product).")
				if(util.setProduct(product)){
					info.C200_9.freq+=1
					if (count < max) {
						sendaddFeatureToConfiguration_8(count+1, sequence, curlSequence)
					} else { tailcall(sendaddFeatureToConfiguration_8(0, sequence, curlSequence)) }
				} else {
				passed=false; throw new AssertionViolationException("C200_9", "Violation in Assertion: util.setProduct(product)");  }
			case code @ _ => sequence.append(f"?$code($product)."); passed=false; throw new InvalidMessageException("C200_9", f"Unknown message: $code"); 
		}
	}
	def sendaddFeatureToConfiguration_8(count: Int, sequence: StringBuilder, curlSequence: StringBuilder): TailRec[Unit] = {
		val request = defaultApi.addFeatureToConfiguration(info.addProduct_62.productName, info.addConfiguration_52.configName, info.addFeatureToProduct_30.featureName2)
val response = request.send(backend)
		sequence.append(f"!addFeatureToConfiguration_8(${info.addProduct_62.productName}, ${info.addConfiguration_52.configName}, ${info.addFeatureToProduct_30.featureName2}).")
		curlSequence.append(request.toCurl+"\n\n")
		if (count < max) {
			receiveC201_7(response.code.toString.toInt, null, count+1, sequence, curlSequence)
		} else { tailcall(receiveC201_7(response.code.toString.toInt, null, 0, sequence, curlSequence)) }
	}
	def receiveC201_7(responseCode: Int, body: Null, count: Int, sequence: StringBuilder, curlSequence: StringBuilder): TailRec[Unit] = {
		responseCode match {
			case code @ 201 =>
			sequence.append(f"?$code().")
				info.C201_7.freq+=1
				if (count < max) {
					sendInternalChoice1(count+1, sequence, curlSequence)
				} else { tailcall(sendInternalChoice1(0, sequence, curlSequence)) }
			case code @ _ => sequence.append(f"?$code()."); passed=false; throw new InvalidMessageException("C201_7", f"Unknown message: $code"); 
		}
	}
	def sendInternalChoice1(count: Int, sequence: StringBuilder, curlSequence: StringBuilder): TailRec[Unit] = {
		val rand = r.nextDouble()
		if (rand <= info.deleteFeature_2.prob){
		val request = defaultApi.deleteFeature(info.addProduct_62.productName, info.addConfiguration_52.configName, info.addFeatureToProduct_46.featureName1)
val response = request.send(backend)
		sequence.append(f"!deleteFeature_2(${info.addProduct_62.productName}, ${info.addConfiguration_52.configName}, ${info.addFeatureToProduct_46.featureName1}).")
		curlSequence.append(request.toCurl+"\n\n")
		if (count < max) {
			receiveC400_1(response.code.toString.toInt, null, count+1, sequence, curlSequence)
		} else { tailcall(receiveC400_1(response.code.toString.toInt, null, 0, sequence, curlSequence)) }
		} else if (rand <= info.deleteConstraint_4.prob+0.3){
			var constraintId = util.getProductId(r)
		val request = defaultApi.deleteConstraint(info.addProduct_62.productName, constraintId)
val response = request.send(backend)
		sequence.append(f"!deleteConstraint_4(${info.addProduct_62.productName}, $constraintId).")
		curlSequence.append(request.toCurl+"\n\n")
		if (count < max) {
			receiveC204_3(response.code.toString.toInt, null, count+1, sequence, curlSequence)
		} else { tailcall(receiveC204_3(response.code.toString.toInt, null, 0, sequence, curlSequence)) }
		} else if (rand <= info.getConfigurationActivedFeatures_6.prob+0.6){
		val request = defaultApi.getConfigurationActivedFeatures(info.addProduct_62.productName, info.addConfiguration_52.configName)
val response = request.send(backend)
		sequence.append(f"!getConfigurationActivedFeatures(${info.addProduct_62.productName}, ${info.addConfiguration_52.configName}).")
		curlSequence.append(request.toCurl+"\n\n")
		try {
			if (count < max) {
			receiveC200_5(response.code.toString.toInt, if(response.body.isInstanceOf[Right[Any, Any]]) response.body.asInstanceOf[Right[Any, Any]].value.asInstanceOf[Seq[String]] else response.body.asInstanceOf[Left[Any, Any]].value.asInstanceOf[Seq[String]], count+1, sequence, curlSequence)
		} else { tailcall(receiveC200_5(response.code.toString.toInt, if(response.body.isInstanceOf[Right[Any, Any]]) response.body.asInstanceOf[Right[Any, Any]].value.asInstanceOf[Seq[String]] else response.body.asInstanceOf[Left[Any, Any]].value.asInstanceOf[Seq[String]], 0, sequence, curlSequence)) }
		} catch {
			case e: java.lang.ClassCastException =>
				throw new InvalidMessageException("C200_5", f"Unexpected response: $response")
			}
		} else { throw new Exception("[DRIVER] Error in test") }
	}
	def receiveC400_1(responseCode: Int, body: Null, count: Int, sequence: StringBuilder, curlSequence: StringBuilder): TailRec[Unit] = {
		responseCode match {
			case code @ 400 =>
			sequence.append(f"?$code().")
				info.C400_1.freq+=1
				if (count < max) {
					sendInternalChoice6(count+1, sequence, curlSequence)
				} else { tailcall(sendInternalChoice6(0, sequence, curlSequence)) }
			case code @ _ => sequence.append(f"?$code()."); passed=false; throw new InvalidMessageException("C400_1", f"Unknown message: $code"); 
		}
	}
	def receiveC204_3(responseCode: Int, body: Null, count: Int, sequence: StringBuilder, curlSequence: StringBuilder): TailRec[Unit] = {
		responseCode match {
			case code @ 204 =>
			sequence.append(f"?$code().")
				info.C204_3.freq+=1
				if (count < max) {
					sendInternalChoice6(count+1, sequence, curlSequence)
				} else { tailcall(sendInternalChoice6(0, sequence, curlSequence)) }
			case code @ _ => sequence.append(f"?$code()."); passed=false; throw new InvalidMessageException("C204_3", f"Unknown message: $code"); 
		}
	}
	def receiveC200_5(responseCode: Int, activeFeatures: Seq[String], count: Int, sequence: StringBuilder, curlSequence: StringBuilder): TailRec[Unit] = {
		responseCode match {
			case code @ 200 =>
			sequence.append(f"?$code($activeFeatures).")
				if(activeFeatures.contains(info.addFeatureToProduct_46.featureName1)&&activeFeatures.contains(info.addFeatureToProduct_30.featureName2)){
					info.C200_5.freq+=1
					if (count < max) {
						sendInternalChoice6(count+1, sequence, curlSequence)
					} else { tailcall(sendInternalChoice6(0, sequence, curlSequence)) }
				} else {
				passed=false; throw new AssertionViolationException("C200_5", "Violation in Assertion: activeFeatures.contains(featureName1)&&activeFeatures.contains(featureName2)");  }
			case code @ _ => sequence.append(f"?$code($activeFeatures)."); passed=false; throw new InvalidMessageException("C200_5", f"Unknown message: $code"); 
		}
	}
	def receiveC201_23(responseCode: Int, body: Null, count: Int, sequence: StringBuilder, curlSequence: StringBuilder): TailRec[Unit] = {
		responseCode match {
			case code @ 201 =>
			sequence.append(f"?$code().")
				info.C201_23.freq+=1
				if (count < max) {
					sendgetProductByName_22(count+1, sequence, curlSequence)
				} else { tailcall(sendgetProductByName_22(0, sequence, curlSequence)) }
			case code @ _ => sequence.append(f"?$code()."); passed=false; throw new InvalidMessageException("C201_23", f"Unknown message: $code"); 
		}
	}
	def sendgetProductByName_22(count: Int, sequence: StringBuilder, curlSequence: StringBuilder): TailRec[Unit] = {
		val request = defaultApi.getProductByName(info.addProduct_62.productName)
val response = request.send(backend)
		sequence.append(f"!getProductByName_22(${info.addProduct_62.productName}).")
		curlSequence.append(request.toCurl+"\n\n")
		try {
			if (count < max) {
			receiveC200_21(response.code.toString.toInt, if(response.body.isInstanceOf[Right[Any, Any]]) response.body.asInstanceOf[Right[Any, Any]].value.asInstanceOf[Product] else response.body.asInstanceOf[Left[Any, Any]].value.asInstanceOf[Product], count+1, sequence, curlSequence)
		} else { tailcall(receiveC200_21(response.code.toString.toInt, if(response.body.isInstanceOf[Right[Any, Any]]) response.body.asInstanceOf[Right[Any, Any]].value.asInstanceOf[Product] else response.body.asInstanceOf[Left[Any, Any]].value.asInstanceOf[Product], 0, sequence, curlSequence)) }
		} catch {
			case e: java.lang.ClassCastException =>
				throw new InvalidMessageException("C200_21", f"Unexpected response: $response")
			}
	}
	def receiveC200_21(responseCode: Int, product: Product, count: Int, sequence: StringBuilder, curlSequence: StringBuilder): TailRec[Unit] = {
		responseCode match {
			case code @ 200 =>
			sequence.append(f"?$code($product).")
				if(util.setProduct(product)){
					info.C200_21.freq+=1
					if (count < max) {
						sendaddFeatureToConfiguration_20(count+1, sequence, curlSequence)
					} else { tailcall(sendaddFeatureToConfiguration_20(0, sequence, curlSequence)) }
				} else {
				passed=false; throw new AssertionViolationException("C200_21", "Violation in Assertion: util.setProduct(product)");  }
			case code @ _ => sequence.append(f"?$code($product)."); passed=false; throw new InvalidMessageException("C200_21", f"Unknown message: $code"); 
		}
	}
	def sendaddFeatureToConfiguration_20(count: Int, sequence: StringBuilder, curlSequence: StringBuilder): TailRec[Unit] = {
		val request = defaultApi.addFeatureToConfiguration(info.addProduct_62.productName, info.addConfiguration_52.configName, info.addFeatureToProduct_30.featureName2)
val response = request.send(backend)
		sequence.append(f"!addFeatureToConfiguration_20(${info.addProduct_62.productName}, ${info.addConfiguration_52.configName}, ${info.addFeatureToProduct_30.featureName2}).")
		curlSequence.append(request.toCurl+"\n\n")
		if (count < max) {
			receiveC201_19(response.code.toString.toInt, null, count+1, sequence, curlSequence)
		} else { tailcall(receiveC201_19(response.code.toString.toInt, null, 0, sequence, curlSequence)) }
	}
	def receiveC201_19(responseCode: Int, body: Null, count: Int, sequence: StringBuilder, curlSequence: StringBuilder): TailRec[Unit] = {
		responseCode match {
			case code @ 201 =>
			sequence.append(f"?$code().")
				info.C201_19.freq+=1
				if (count < max) {
					sendInternalChoice2(count+1, sequence, curlSequence)
				} else { tailcall(sendInternalChoice2(0, sequence, curlSequence)) }
			case code @ _ => sequence.append(f"?$code()."); passed=false; throw new InvalidMessageException("C201_19", f"Unknown message: $code"); 
		}
	}
	def sendInternalChoice2(count: Int, sequence: StringBuilder, curlSequence: StringBuilder): TailRec[Unit] = {
		val rand = r.nextDouble()
		if (rand <= info.deleteFeatureOfProduct_14.prob){
		val request = defaultApi.deleteFeatureOfProduct(info.addProduct_62.productName, info.addFeatureToProduct_30.featureName2)
val response = request.send(backend)
		sequence.append(f"!deleteFeatureOfProduct_14(${info.addProduct_62.productName}, ${info.addFeatureToProduct_30.featureName2}).")
		curlSequence.append(request.toCurl+"\n\n")
		if (count < max) {
			receiveC204_13(response.code.toString.toInt, null, count+1, sequence, curlSequence)
		} else { tailcall(receiveC204_13(response.code.toString.toInt, null, 0, sequence, curlSequence)) }
		} else if (rand <= info.addFeatureToConfiguration_16.prob+0.3){
		val request = defaultApi.addFeatureToConfiguration(info.addProduct_62.productName, info.addConfiguration_52.configName, info.addFeatureToProduct_46.featureName1)
val response = request.send(backend)
		sequence.append(f"!addFeatureToConfiguration_16(${info.addProduct_62.productName}, ${info.addConfiguration_52.configName}, ${info.addFeatureToProduct_46.featureName1}).")
		curlSequence.append(request.toCurl+"\n\n")
		if (count < max) {
			receiveC400_15(response.code.toString.toInt, null, count+1, sequence, curlSequence)
		} else { tailcall(receiveC400_15(response.code.toString.toInt, null, 0, sequence, curlSequence)) }
		} else if (rand <= info.deleteConstraint_18.prob+0.6){
			var constraintId = util.getProductId(r)
		val request = defaultApi.deleteConstraint(info.addProduct_62.productName, constraintId)
val response = request.send(backend)
		sequence.append(f"!deleteConstraint_18(${info.addProduct_62.productName}, $constraintId).")
		curlSequence.append(request.toCurl+"\n\n")
		if (count < max) {
			receiveC204_17(response.code.toString.toInt, null, count+1, sequence, curlSequence)
		} else { tailcall(receiveC204_17(response.code.toString.toInt, null, 0, sequence, curlSequence)) }
		} else { throw new Exception("[DRIVER] Error in test") }
	}
	def receiveC204_13(responseCode: Int, body: Null, count: Int, sequence: StringBuilder, curlSequence: StringBuilder): TailRec[Unit] = {
		responseCode match {
			case code @ 204 =>
			sequence.append(f"?$code().")
				info.C204_13.freq+=1
				if (count < max) {
					sendInternalChoice6(count+1, sequence, curlSequence)
				} else { tailcall(sendInternalChoice6(0, sequence, curlSequence)) }
			case code @ _ => sequence.append(f"?$code()."); passed=false; throw new InvalidMessageException("C204_13", f"Unknown message: $code"); 
		}
	}
	def receiveC400_15(responseCode: Int, body: Null, count: Int, sequence: StringBuilder, curlSequence: StringBuilder): TailRec[Unit] = {
		responseCode match {
			case code @ 400 =>
			sequence.append(f"?$code().")
				info.C400_15.freq+=1
				if (count < max) {
					sendInternalChoice6(count+1, sequence, curlSequence)
				} else { tailcall(sendInternalChoice6(0, sequence, curlSequence)) }
			case code @ _ => sequence.append(f"?$code()."); passed=false; throw new InvalidMessageException("C400_15", f"Unknown message: $code"); 
		}
	}
	def receiveC204_17(responseCode: Int, body: Null, count: Int, sequence: StringBuilder, curlSequence: StringBuilder): TailRec[Unit] = {
		responseCode match {
			case code @ 204 =>
			sequence.append(f"?$code().")
				info.C204_17.freq+=1
				if (count < max) {
					sendInternalChoice6(count+1, sequence, curlSequence)
				} else { tailcall(sendInternalChoice6(0, sequence, curlSequence)) }
			case code @ _ => sequence.append(f"?$code()."); passed=false; throw new InvalidMessageException("C204_17", f"Unknown message: $code"); 
		}
	}
	def receiveC204_25(responseCode: Int, body: Null, count: Int, sequence: StringBuilder, curlSequence: StringBuilder): TailRec[Unit] = {
		responseCode match {
			case code @ 204 =>
			sequence.append(f"?$code().")
				info.C204_25.freq+=1
				if (count < max) {
					sendInternalChoice6(count+1, sequence, curlSequence)
				} else { tailcall(sendInternalChoice6(0, sequence, curlSequence)) }
			case code @ _ => sequence.append(f"?$code()."); passed=false; throw new InvalidMessageException("C204_25", f"Unknown message: $code"); 
		}
	}
	def receiveC200_27(responseCode: Int, features: Set[Feature], count: Int, sequence: StringBuilder, curlSequence: StringBuilder): TailRec[Unit] = {
		responseCode match {
			case code @ 200 =>
			sequence.append(f"?$code($features).")
				if(util.checkFeatures(features, info.addFeatureToProduct_46.featureName1, info.addFeatureToProduct_30.featureName2)){
					info.C200_27.freq+=1
					if (count < max) {
						sendInternalChoice6(count+1, sequence, curlSequence)
					} else { tailcall(sendInternalChoice6(0, sequence, curlSequence)) }
				} else {
				passed=false; throw new AssertionViolationException("C200_27", "Violation in Assertion: util.checkFeatures(features, featureName1, featureName2)");  }
			case code @ _ => sequence.append(f"?$code($features)."); passed=false; throw new InvalidMessageException("C200_27", f"Unknown message: $code"); 
		}
	}
	def receiveC200_31(responseCode: Int, body: Feature, count: Int, sequence: StringBuilder, curlSequence: StringBuilder): TailRec[Unit] = {
		responseCode match {
			case code @ 200 =>
			sequence.append(f"?$code($body).")
				if(body.name.get==info.addFeatureToProduct_46.featureName1){
					info.C200_31.freq+=1
					if (count < max) {
						sendInternalChoice6(count+1, sequence, curlSequence)
					} else { tailcall(sendInternalChoice6(0, sequence, curlSequence)) }
				} else {
				passed=false; throw new AssertionViolationException("C200_31", "Violation in Assertion: body.name.get==featureName1");  }
			case code @ _ => sequence.append(f"?$code($body)."); passed=false; throw new InvalidMessageException("C200_31", f"Unknown message: $code"); 
		}
	}
	def receiveC404_33(responseCode: Int, body: Null, count: Int, sequence: StringBuilder, curlSequence: StringBuilder): TailRec[Unit] = {
		responseCode match {
			case code @ 404 =>
			sequence.append(f"?$code().")
				info.C404_33.freq+=1
				if (count < max) {
					sendInternalChoice6(count+1, sequence, curlSequence)
				} else { tailcall(sendInternalChoice6(0, sequence, curlSequence)) }
			case code @ _ => sequence.append(f"?$code()."); passed=false; throw new InvalidMessageException("C404_33", f"Unknown message: $code"); 
		}
	}
	def receiveC204_35(responseCode: Int, body: Null, count: Int, sequence: StringBuilder, curlSequence: StringBuilder): TailRec[Unit] = {
		responseCode match {
			case code @ 204 =>
			sequence.append(f"?$code().")
				info.C204_35.freq+=1
				if (count < max) {
					sendInternalChoice6(count+1, sequence, curlSequence)
				} else { tailcall(sendInternalChoice6(0, sequence, curlSequence)) }
			case code @ _ => sequence.append(f"?$code()."); passed=false; throw new InvalidMessageException("C204_35", f"Unknown message: $code"); 
		}
	}
	def receiveC201_41(responseCode: Int, body: Null, count: Int, sequence: StringBuilder, curlSequence: StringBuilder): TailRec[Unit] = {
		responseCode match {
			case code @ 201 =>
			sequence.append(f"?$code().")
				info.C201_41.freq+=1
				if (count < max) {
					sendInternalChoice4(count+1, sequence, curlSequence)
				} else { tailcall(sendInternalChoice4(0, sequence, curlSequence)) }
			case code @ _ => sequence.append(f"?$code()."); passed=false; throw new InvalidMessageException("C201_41", f"Unknown message: $code"); 
		}
	}
	def sendInternalChoice4(count: Int, sequence: StringBuilder, curlSequence: StringBuilder): TailRec[Unit] = {
		val rand = r.nextDouble()
		if (rand <= info.addFeatureToConfiguration_38.prob){
		val request = defaultApi.addFeatureToConfiguration(info.addProduct_62.productName, info.addConfiguration_52.configName, info.addFeatureToProduct_46.featureName1)
val response = request.send(backend)
		sequence.append(f"!addFeatureToConfiguration_38(${info.addProduct_62.productName}, ${info.addConfiguration_52.configName}, ${info.addFeatureToProduct_46.featureName1}).")
		curlSequence.append(request.toCurl+"\n\n")
		if (count < max) {
			receiveC400_37(response.code.toString.toInt, null, count+1, sequence, curlSequence)
		} else { tailcall(receiveC400_37(response.code.toString.toInt, null, 0, sequence, curlSequence)) }
		} else if (rand <= info.deleteFeature_40.prob+0.5){
		val request = defaultApi.deleteFeature(info.addProduct_62.productName, info.addConfiguration_52.configName, info.addFeatureToProduct_46.featureName1)
val response = request.send(backend)
		sequence.append(f"!deleteFeature_40(${info.addProduct_62.productName}, ${info.addConfiguration_52.configName}, ${info.addFeatureToProduct_46.featureName1}).")
		curlSequence.append(request.toCurl+"\n\n")
		if (count < max) {
			receiveC204_39(response.code.toString.toInt, null, count+1, sequence, curlSequence)
		} else { tailcall(receiveC204_39(response.code.toString.toInt, null, 0, sequence, curlSequence)) }
		} else { throw new Exception("[DRIVER] Error in test") }
	}
	def receiveC400_37(responseCode: Int, body: Null, count: Int, sequence: StringBuilder, curlSequence: StringBuilder): TailRec[Unit] = {
		responseCode match {
			case code @ 400 =>
			sequence.append(f"?$code().")
				info.C400_37.freq+=1
				if (count < max) {
					sendInternalChoice6(count+1, sequence, curlSequence)
				} else { tailcall(sendInternalChoice6(0, sequence, curlSequence)) }
			case code @ _ => sequence.append(f"?$code()."); passed=false; throw new InvalidMessageException("C400_37", f"Unknown message: $code"); 
		}
	}
	def receiveC204_39(responseCode: Int, body: Null, count: Int, sequence: StringBuilder, curlSequence: StringBuilder): TailRec[Unit] = {
		responseCode match {
			case code @ 204 =>
			sequence.append(f"?$code().")
				info.C204_39.freq+=1
				if (count < max) {
					sendInternalChoice6(count+1, sequence, curlSequence)
				} else { tailcall(sendInternalChoice6(0, sequence, curlSequence)) }
			case code @ _ => sequence.append(f"?$code()."); passed=false; throw new InvalidMessageException("C204_39", f"Unknown message: $code"); 
		}
	}
	def receiveC400_43(responseCode: Int, body: Null, count: Int, sequence: StringBuilder, curlSequence: StringBuilder): TailRec[Unit] = {
		responseCode match {
			case code @ 400 =>
			sequence.append(f"?$code().")
				info.C400_43.freq+=1
				if (count < max) {
					sendInternalChoice6(count+1, sequence, curlSequence)
				} else { tailcall(sendInternalChoice6(0, sequence, curlSequence)) }
			case code @ _ => sequence.append(f"?$code()."); passed=false; throw new InvalidMessageException("C400_43", f"Unknown message: $code"); 
		}
	}
	def receiveC200_53(responseCode: Int, body: Seq[String], count: Int, sequence: StringBuilder, curlSequence: StringBuilder): TailRec[Unit] = {
		responseCode match {
			case code @ 200 =>
			sequence.append(f"?$code($body).")
				if(body.contains(info.addProduct_62.productName)){
					info.C200_53.freq+=1
					if (count < max) {
						sendInternalChoice6(count+1, sequence, curlSequence)
					} else { tailcall(sendInternalChoice6(0, sequence, curlSequence)) }
				} else {
				passed=false; throw new AssertionViolationException("C200_53", "Violation in Assertion: body.contains(productName)");  }
			case code @ _ => sequence.append(f"?$code($body)."); passed=false; throw new InvalidMessageException("C200_53", f"Unknown message: $code"); 
		}
	}
	def receiveC200_57(responseCode: Int, body: Product, count: Int, sequence: StringBuilder, curlSequence: StringBuilder): TailRec[Unit] = {
		responseCode match {
			case code @ 200 =>
			sequence.append(f"?$code($body).")
				if(info.addProduct_62.productName==body.name.get){
					info.C200_57.freq+=1
					if (count < max) {
						senddeleteProductByName_56(count+1, sequence, curlSequence)
					} else { tailcall(senddeleteProductByName_56(0, sequence, curlSequence)) }
				} else {
				passed=false; throw new AssertionViolationException("C200_57", "Violation in Assertion: productName==body.name.get");  }
			case code @ _ => sequence.append(f"?$code($body)."); passed=false; throw new InvalidMessageException("C200_57", f"Unknown message: $code"); 
		}
	}
	def senddeleteProductByName_56(count: Int, sequence: StringBuilder, curlSequence: StringBuilder): TailRec[Unit] = {
		val request = defaultApi.deleteProductByName(info.addProduct_62.productName)
val response = request.send(backend)
		sequence.append(f"!deleteProductByName(${info.addProduct_62.productName}).")
		curlSequence.append(request.toCurl+"\n\n")
		if (count < max) {
			receiveC204_55(response.code.toString.toInt, null, count+1, sequence, curlSequence)
		} else { tailcall(receiveC204_55(response.code.toString.toInt, null, 0, sequence, curlSequence)) }
	}
	def receiveC204_55(responseCode: Int, body: Null, count: Int, sequence: StringBuilder, curlSequence: StringBuilder): TailRec[Unit] = {
		responseCode match {
			case code @ 204 =>
			sequence.append(f"?$code().")
				info.C204_55.freq+=1
				done()
			case code @ _ => sequence.append(f"?$code()."); passed=false; throw new InvalidMessageException("C204_55", f"Unknown message: $code"); 
		}
	}
	def receiveC404_59(responseCode: Int, body: Null, count: Int, sequence: StringBuilder, curlSequence: StringBuilder): TailRec[Unit] = {
		responseCode match {
			case code @ 404 =>
			sequence.append(f"?$code().")
				info.C404_59.freq+=1
				done()
			case code @ _ => sequence.append(f"?$code()."); passed=false; throw new InvalidMessageException("C404_59", f"Unknown message: $code"); 
		}
	}
}
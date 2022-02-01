package no.nav.syfo

import io.prometheus.client.hotspot.DefaultExports
import kotlinx.coroutines.DelicateCoroutinesApi
import no.nav.syfo.application.ApplicationServer
import no.nav.syfo.application.ApplicationState
import no.nav.syfo.application.createApplicationEngine
import no.nav.syfo.etterlevelse.EtterlevelseService
import no.nav.syfo.etterlevelse.model.JuridiskVurderingKafkaMessage
import no.nav.syfo.kafka.aiven.KafkaUtils
import no.nav.syfo.kafka.toConsumerConfig
import no.nav.syfo.kafka.toProducerConfig
import no.nav.syfo.model.ValidationResult
import no.nav.syfo.util.JacksonKafkaDeserializer
import no.nav.syfo.util.JacksonKafkaSerializer
import org.apache.kafka.clients.consumer.ConsumerConfig
import org.apache.kafka.clients.consumer.KafkaConsumer
import org.apache.kafka.clients.producer.KafkaProducer
import org.apache.kafka.common.serialization.StringDeserializer
import org.slf4j.Logger
import org.slf4j.LoggerFactory

val log: Logger = LoggerFactory.getLogger("no.nav.syfo.teamsykmelding-pik")

@DelicateCoroutinesApi
fun main() {
    val env = Environment()
    DefaultExports.initialize()
    val applicationState = ApplicationState()

    val kafkaConsumer = KafkaConsumer(
        KafkaUtils.getAivenKafkaConfig().also {
            it[ConsumerConfig.AUTO_OFFSET_RESET_CONFIG] = "earliest"
        }.toConsumerConfig(env.applicationName, JacksonKafkaDeserializer::class),
        StringDeserializer(),
        JacksonKafkaDeserializer(ValidationResult::class)
    )

    val kafkaProducer = KafkaProducer<String, JuridiskVurderingKafkaMessage>(
        KafkaUtils.getAivenKafkaConfig()
            .toProducerConfig(env.applicationName, valueSerializer = JacksonKafkaSerializer::class)
    )
    val etterlevelseService = EtterlevelseService(
        applicationState = applicationState,
        kafkaConsumer = kafkaConsumer,
        internPikTopic = env.internPikTopic,
        kafkaProducer = kafkaProducer,
        etterlevelseTopic = env.etterlevelseTopic
    )

    val applicationEngine = createApplicationEngine(
        env,
        applicationState
    )
    val applicationServer = ApplicationServer(applicationEngine, applicationState)
    applicationServer.start()
    applicationState.ready = true

    etterlevelseService.startConsumer()
}

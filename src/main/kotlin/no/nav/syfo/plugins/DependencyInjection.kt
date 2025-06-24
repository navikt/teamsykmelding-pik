package no.nav.syfo.plugins

import io.ktor.server.application.Application
import io.ktor.server.application.install
import no.nav.syfo.etterlevelse.EtterlevelseService
import no.nav.syfo.etterlevelse.model.JuridiskVurderingKafkaMessage
import no.nav.syfo.util.JacksonKafkaDeserializer
import no.nav.syfo.util.JacksonKafkaSerializer
import org.apache.kafka.clients.consumer.ConsumerConfig
import org.apache.kafka.clients.consumer.KafkaConsumer
import org.apache.kafka.clients.producer.KafkaProducer
import org.apache.kafka.clients.producer.ProducerConfig
import org.apache.kafka.common.serialization.StringDeserializer
import org.apache.kafka.common.serialization.StringSerializer
import org.koin.dsl.module
import org.koin.ktor.plugin.Koin
import org.koin.logger.slf4jLogger
import java.util.Properties

fun Application.configureDependencyInjection() {
    install(Koin) {
        slf4jLogger()

        modules(
            environmentModule(),
            etterlevelseModule,
        )
    }
}


fun Application.environmentModule() = module {
    single<Environment> { createEnvironment() }
}

val etterlevelseModule = module {
    single {
        val env = get<Environment>()
        val consumer = KafkaConsumer(
                Properties().apply {
                    putAll(env.kafkaConfig)
                    this[ConsumerConfig.VALUE_DESERIALIZER_CLASS_CONFIG] =
                            JacksonKafkaDeserializer::class.java.name
                    this[ConsumerConfig.KEY_DESERIALIZER_CLASS_CONFIG] =
                            StringDeserializer::class.java.name
                    this[ConsumerConfig.GROUP_ID_CONFIG] = "teamsykmelding-pik-2-consumer"
                    this[ConsumerConfig.CLIENT_ID_CONFIG] =
                            "${env.hostname}-consumer"
                    this[ConsumerConfig.AUTO_OFFSET_RESET_CONFIG] = "none"
                    this[ConsumerConfig.ENABLE_AUTO_COMMIT_CONFIG] = "true"
                    this[ConsumerConfig.MAX_POLL_RECORDS_CONFIG] = "1"
                },
                StringDeserializer(), JacksonKafkaDeserializer(),
        )

        val kafkaProduccer = KafkaProducer<String, JuridiskVurderingKafkaMessage>(
            Properties().apply {
                putAll(env.kafkaConfig)
                this[ProducerConfig.VALUE_SERIALIZER_CLASS_CONFIG] = JacksonKafkaSerializer::class.java.name
                this[ProducerConfig.KEY_SERIALIZER_CLASS_CONFIG] = StringSerializer::class.java.name
                this[ProducerConfig.ACKS_CONFIG] = "all"
                this[ProducerConfig.CLIENT_ID_CONFIG] = "${env.hostname}-producer"
                this[ProducerConfig.COMPRESSION_TYPE_CONFIG] = "gzip"
                this[ProducerConfig.ENABLE_IDEMPOTENCE_CONFIG] = "true"
            }
        )

        EtterlevelseService(
            consumer,
            env.internPikTopic,
            kafkaProduccer,
            env.etterlevelseTopic
        )
    }
}

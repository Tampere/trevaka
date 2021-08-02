package fi.tampere.trevaka.scheduler.config

import fi.espoo.evaka.shared.job.DefaultJobSchedule
import fi.espoo.evaka.shared.job.JobSchedule
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.core.env.Environment

@Configuration
class SchedulerConfiguration {

    @Bean
    fun jobSchedule(env: Environment): JobSchedule = DefaultJobSchedule.fromEnvironment(env)

}

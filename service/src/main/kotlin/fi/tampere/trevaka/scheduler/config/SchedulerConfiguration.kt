package fi.tampere.trevaka.scheduler.config

import fi.espoo.evaka.shared.daily.DailyJob
import fi.espoo.evaka.shared.daily.DailySchedule
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import java.time.LocalTime

@Configuration
class SchedulerConfiguration {

    @Bean
    fun dailySchedule(): DailySchedule = TrevakaDailySchedule()

}

internal class TrevakaDailySchedule : DailySchedule {
    override fun getTimeForJob(job: DailyJob): LocalTime? = when (job) {
        DailyJob.EndOfDayAttendanceUpkeep -> LocalTime.of(0, 0)
    }
}

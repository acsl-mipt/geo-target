class Info(str: Option[String] = None)

class Ns(name: Option[String] = None, _info: Option[String] = None) {
  def this(name: String) = this(Some(name))
  def info = _info
  def info_= (str: String) = _info
  def apply(block: => Any): Ns = { block; this }
}

object namespace {
  def apply(name: String) = new Ns(Some(name))
}

println(namespace("ru.mipt.acsl.scripting") {
  info = "Модуль управления бортовыми скриптами (циклограммами)"
})

/*
import ru.mipt.acsl.foundation.{s, b8, u8, guid}

'Идентификатор скрипта'
type script_id guid

'Информация о скрипте'
type script_info struct
(
  script_id scriptId,
  'Данные скрипта'
[u8]      scriptData,
'Байткод скрипта'
[u8]      scriptCode,
)

'Дата и время'
type date_time ber
'Период времени'
type period    ber

'Запланированный запуск скрипта'
type script_run_timing struct
(
  'Идентификатор запланированного запуска'
guid        ^id,
'Запуск запланирован'
b8          isActive,
script_id   scriptId,
'Время запуска'
date_time   runOn,
'Периодический скрипт'
b8          isRepeated,
'Период повторения'
period /s/  repeatPeriod,
'Повторение ограничено'
b8          isRepeatingLimitedByDate,
'Дата и время окончания повторения запусков скрипта'
date_time   repeatUntil,
)

type scripting_error enum ber (INVALID_ARGUMENT = 1, INVALID_STATE = 2)

'Управление бортовыми скриптами (циклограммами)'
component Scripting
  {
    parameters
    (
      'Идентификаторы бортовых скриптов'
    [guid]              scriptsIds,
    'Бортовые скрипты'
    [script_info]       scripts,
    'Данные планировщика запуска скриптов'
    [script_run_timing] scriptsRunTimings,
    )

    'Загрузить скрипт'
    command uploadScript
      (
        script_info script,
        ) -> scripting_error?

    'Удалить скрипт'
    command deleteScript(script_id scriptId) -> scripting_error?

    'Запустить скрипт незамедлительно'
    command runScriptNow(script_id scriptId) -> scripting_error?

    'Запланировать запуск скрипта'
    command scheduleScriptRun(script_run_timing scriptRunTiming) -> scripting_error?

    'Активировать запланированный запуск скрипта'
    command enableScriptRunTiming(guid scriptRunTimingId) -> scripting_error?

    'Деактивировать запланированный запуск скрипта'
    command disableScriptRunTiming(guid scriptRunTimingId) -> scripting_error?

    'Перечень загруженных скриптов'
    status availableScriptsIds
      (
        scriptsIds,
        )

    'Данные планировщика запуска скриптов'
    status scriptsRunTimings
      (
        scriptsRunTimings,
        )
  }*/


package run.cfloat.pkg

import com.google.gson.Gson
import jakarta.validation.Validation
import jakarta.validation.Validator

/** 参数校验 */
val validatorFactory: Validator = Validation.buildDefaultValidatorFactory().validator

/** 处理json */
val gson = Gson()
